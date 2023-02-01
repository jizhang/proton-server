package com.shzhangji.proton.entity;

import java.time.LocalDate;
import lombok.Data;

@Data
public class UserPrimaryDaily {
  private LocalDate reportDate;
  private long userCount;
  private long sessionCount;
  private Double bounceRate;
  private Integer sessionDuration;
}
