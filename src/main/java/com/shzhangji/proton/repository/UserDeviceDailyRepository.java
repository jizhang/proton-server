package com.shzhangji.proton.repository;

import com.shzhangji.proton.entity.UserDeviceDaily;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserDeviceDailyRepository {
  private final NamedParameterJdbcTemplate jdbcTemplate;

  public List<UserDeviceDaily> findByDates(Collection<LocalDate> dates) {
    var strDates = dates.stream()
        .map(DateTimeFormatter.BASIC_ISO_DATE::format)
        .toList();

    return jdbcTemplate.query("""
        SELECT
            DATE(report_date) AS report_date
            ,device_name
            ,user_count
        FROM da_user_device_daily
        WHERE report_date IN (:dates)
        """,
        new MapSqlParameterSource("dates", strDates),
        new BeanPropertyRowMapper<>(UserDeviceDaily.class));
  }
}
