package com.shzhangji.proton.entity;

import java.time.LocalDate;
import lombok.Data;

@Data
public class UserSourceMediumDaily {
  private LocalDate reportDate;
  private String source;
  private String medium;
  private long userCount;
}
