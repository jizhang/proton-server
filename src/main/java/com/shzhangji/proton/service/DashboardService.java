package com.shzhangji.proton.service;

import com.shzhangji.proton.entity.UserCountRt;
import com.shzhangji.proton.repository.UserCount1minRepository;
import com.shzhangji.proton.repository.UserCountRtRepository;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardService {
  private final UserCountRtRepository userCountRtRepo;
  private final UserCount1minRepository userCount1minRepo;

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
}
