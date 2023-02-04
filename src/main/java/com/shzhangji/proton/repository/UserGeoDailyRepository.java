package com.shzhangji.proton.repository;

import com.shzhangji.proton.entity.UserGeoDaily;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserGeoDailyRepository {
  private final JdbcTemplate jdbcTemplate;

  public List<UserGeoDaily> getUserGeoDaily(LocalDate date) {
    return jdbcTemplate.query("""
        SELECT
            DATE(report_date) AS report_date
            ,province
            ,user_count
        FROM da_user_geo_daily
        WHERE report_date = ?
        """,
        new BeanPropertyRowMapper<>(UserGeoDaily.class),
        date.format(DateTimeFormatter.BASIC_ISO_DATE));
  }
}
