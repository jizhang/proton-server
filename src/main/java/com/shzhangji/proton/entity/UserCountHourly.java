package com.shzhangji.proton.entity;

import java.time.LocalDateTime;
import lombok.Data;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("da_user_count_hourly")
public class UserCountHourly {
  private LocalDateTime reportHour;
  private long userCount;
}
