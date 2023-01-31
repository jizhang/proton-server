package com.shzhangji.proton.entity;

import java.time.LocalDateTime;
import lombok.Data;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("da_user_count_rt")
public class UserCountRt {
  private LocalDateTime reportMinute;
  @Column("user_count_1min")
  private long userCount1min;
  @Column("user_count_5min")
  private long userCount5min;
  @Column("user_count_15min")
  private long userCount15min;
  private LocalDateTime updatedAt;
}
