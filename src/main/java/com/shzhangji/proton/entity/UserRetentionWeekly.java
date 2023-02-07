package com.shzhangji.proton.entity;

import lombok.Data;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("da_user_retention_weekly")
public class UserRetentionWeekly {
  private int reportWeek;
  private long userCount;
  @Column("user_count_1w")
  private long userCount1w;
  @Column("user_count_2w")
  private long userCount2w;
  @Column("user_count_3w")
  private long userCount3w;
  @Column("user_count_4w")
  private long userCount4w;
  @Column("user_count_5w")
  private long userCount5w;
}
