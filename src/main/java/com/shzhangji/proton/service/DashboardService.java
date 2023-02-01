package com.shzhangji.proton.service;

import com.shzhangji.proton.entity.UserCountRt;
import com.shzhangji.proton.entity.UserPrimaryDaily;
import com.shzhangji.proton.repository.UserCount1minRepository;
import com.shzhangji.proton.repository.UserCountRtRepository;
import com.shzhangji.proton.repository.UserPrimaryRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardService {
  private final UserCountRtRepository userCountRtRepo;
  private final UserCount1minRepository userCount1minRepo;
  private final UserPrimaryRepository userPrimaryRepo;

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

  public PrimaryData getPrimaryData() {
    var endDate = LocalDate.now().minusDays(1);
    var startDate = endDate.minusDays(27);
    var data = userPrimaryRepo.getUserPrimaryData(startDate, endDate);
    var dataMap = new HashMap<LocalDate, UserPrimaryDaily>();
    for (var item : data) {
      dataMap.put(item.getReportDate(), item);
    }

    var currentDate = endDate.minusDays(13);
    var dataArr = new PrimaryMeasureData[14];
    var dfDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    for (int i = 0; i < 14; ++i) {
      long current = 0;
      long previous = 0;

      var currentItem = dataMap.get(currentDate);
      if (currentItem != null) {
        current = currentItem.getUserCount();
      }

      var previousItem = dataMap.get(currentDate.minusDays(14));
      if (previousItem != null) {
        previous = previousItem.getUserCount();
      }

      dataArr[i] = new PrimaryMeasureData(currentDate.format(dfDate), current, previous);

      currentDate = currentDate.plusDays(1);
    }

    var users = new PrimaryMeasure("users", "Users", "integer", dataArr);
    return new PrimaryData(new PrimaryMeasure[] { users });
  }

  public record ActiveUserData(long count, long[] minutes) {}

  public record PrimaryData(PrimaryMeasure[] measures) {}

  public record PrimaryMeasure(
      String name,
      String label,
      String format,
      PrimaryMeasureData[] data
  ) {}

  public record PrimaryMeasureData(String date, Number current, Number previous) {}
}
