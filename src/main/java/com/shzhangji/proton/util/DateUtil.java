package com.shzhangji.proton.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtil {
  public static int toInt(LocalDate dt) {
    return Integer.parseInt(dt.format(DateTimeFormatter.BASIC_ISO_DATE));
  }

  public static LocalDate fromInt(int i) {
    return LocalDate.parse(String.valueOf(i), DateTimeFormatter.BASIC_ISO_DATE);
  }
}
