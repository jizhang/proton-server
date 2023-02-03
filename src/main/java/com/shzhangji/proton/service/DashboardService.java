package com.shzhangji.proton.service;

import com.shzhangji.proton.entity.UserCountRt;
import com.shzhangji.proton.entity.UserPrimaryDaily;
import com.shzhangji.proton.repository.UserCount1minRepository;
import com.shzhangji.proton.repository.UserCountRtRepository;
import com.shzhangji.proton.repository.UserPrimaryRepository;
import com.shzhangji.proton.repository.UserSourceDailyRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardService {
  private static final DateTimeFormatter DF_DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd");

  private final UserCountRtRepository userCountRtRepo;
  private final UserCount1minRepository userCount1minRepo;
  private final UserPrimaryRepository userPrimaryRepo;
  private final UserSourceDailyRepository userSourceRepo;

  public ActiveUserData getActiveUser() {
    var now = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
    var dt2min = now.minusMinutes(2);
    var count = userCountRtRepo.findFirstByReportMinuteAfterOrderByReportMinuteDesc(dt2min)
        .map(UserCountRt::getUserCount5min)
        .orElse(0L);

    var dt30min = now.minusMinutes(30);
    var count1min = userCount1minRepo.findByReportMinuteAfter(dt30min);
    var countMap = new HashMap<LocalDateTime, Long>();
    for (var row : count1min) {
      countMap.put(row.getReportMinute(), row.getUserCount());
    }

    var minutes = new long[30];
    for (int i = 1; i <= 30; ++i) {
      minutes[i - 1] = countMap.getOrDefault(dt30min.plusMinutes(i), 0L);
    }

    return new ActiveUserData(count, minutes);
  }

  public record ActiveUserData(long count, long[] minutes) {}

  public PrimaryData getPrimaryData(int days) {
    var endDate = LocalDate.now().minusDays(1);
    var startDate = endDate.minusDays(days * 2 - 1);
    var data = userPrimaryRepo.getUserPrimaryData(startDate, endDate);
    var dataMap = new HashMap<LocalDate, UserPrimaryDaily>();
    for (var item : data) {
      dataMap.put(item.getReportDate(), item);
    }

    var users = new PrimaryMeasure("users", "Users", "integer",
        generateMeasureData(endDate, days, dataMap, UserPrimaryDaily::getUserCount));
    var sessions = new PrimaryMeasure("session", "Session", "integer",
        generateMeasureData(endDate, days, dataMap, UserPrimaryDaily::getSessionCount));
    var bounceRate = new PrimaryMeasure("bounce_rate", "Bounce Rate", "percent",
        generateMeasureData(endDate, days, dataMap, UserPrimaryDaily::getBounceRate));
    var sessionDuration = new PrimaryMeasure("session_duration", "Session Duration", "interval",
        generateMeasureData(endDate, days, dataMap, UserPrimaryDaily::getSessionDuration));

    return new PrimaryData(new PrimaryMeasure[] { users, sessions, bounceRate, sessionDuration });
  }

  private PrimaryMeasureData[] generateMeasureData(
      LocalDate endDate,
      int days,
      Map<LocalDate, UserPrimaryDaily> dataMap,
      Function<UserPrimaryDaily, Number> getter) {

    var currentDate = endDate.minusDays(days - 1);
    var dataArr = new PrimaryMeasureData[days];
    for (int i = 0; i < days; ++i) {
      Number current = null;
      Number previous = null;

      var currentItem = dataMap.get(currentDate);
      if (currentItem != null) {
        current = getter.apply(currentItem);
      }

      var previousDate = currentDate.minusDays(days);
      var previousItem = dataMap.get(previousDate);
      if (previousItem != null) {
        previous = getter.apply(previousItem);
      }

      dataArr[i] = new PrimaryMeasureData(
          currentDate.format(DF_DATE),
          current,
          previous,
          previousDate.format(DF_DATE));

      currentDate = currentDate.plusDays(1);
    }

    return dataArr;
  }

  public record PrimaryData(PrimaryMeasure[] measures) {}

  public record PrimaryMeasure(
      String name,
      String label,
      String format,
      PrimaryMeasureData[] data
  ) {}

  public record PrimaryMeasureData(
      String date,
      Number current,
      Number previous,
      String previousDate
  ) {}

  public SourceData getUserSource(int days) {
    var endDate = LocalDate.now().minusDays(1);
    var startDate = endDate.minusDays(days - 1);
    var trafficChannelData = userSourceRepo.getChannelData(startDate, endDate).stream()
        .map(item -> new SourceMeasureData(
            item.getReportDate().format(DF_DATE),
            item.getChannel(),
            item.getUserCount()))
        .toArray(SourceMeasureData[]::new);
    var trafficChannel = new SourceMeasure("traffic_channel", "Traffic Channel", trafficChannelData);

    var sourceMediumData = userSourceRepo.getSourceMediumData(startDate, endDate).stream()
        .map(item -> new SourceMeasureData(
            item.getReportDate().format(DF_DATE),
            String.format("%s / %s", item.getSource(), item.getMedium()),
            item.getUserCount()))
        .toArray(SourceMeasureData[]::new);
    var sourceMedium = new SourceMeasure("source_medium", "Source / Medium", sourceMediumData);

    var referralsData = userSourceRepo.getReferralData(startDate, endDate).stream()
        .map(item -> new SourceMeasureData(
            item.getReportDate().format(DF_DATE),
            item.getReferral(),
            item.getUserCount()))
        .toArray(SourceMeasureData[]::new);
    var referrals = new SourceMeasure("referrals", "Referrals", referralsData);

    return new SourceData(new SourceMeasure[] { trafficChannel, sourceMedium, referrals });
  }

  public record SourceData(SourceMeasure[] measures) {}

  public record SourceMeasure(String name, String label, SourceMeasureData[] data) {}

  public record SourceMeasureData(String date, String key, Long value) {}
}
