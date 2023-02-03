package com.shzhangji.proton.entity;

import java.time.LocalDate;
import lombok.Data;

@Data
public class UserReferralDaily {
  private LocalDate reportDate;
  private String referral;
  private long userCount;
}
