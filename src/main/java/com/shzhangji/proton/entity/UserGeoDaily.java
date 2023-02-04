package com.shzhangji.proton.entity;

import java.time.LocalDate;
import lombok.Data;

@Data
public class UserGeoDaily {
  private LocalDate reportDate;
  private String province;
  private long userCount;
}
