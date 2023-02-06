package com.shzhangji.proton.entity;

import java.time.LocalDate;
import lombok.Data;

@Data
public class UserDeviceDaily {
  private LocalDate reportDate;
  private String deviceName;
  private long userCount;
}
