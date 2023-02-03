package com.shzhangji.proton.entity;

import java.time.LocalDate;
import lombok.Data;

@Data
public class UserChannelDaily {
  private LocalDate reportDate;
  private String channel;
  private long userCount;
}
