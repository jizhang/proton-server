package com.shzhangji.proton.repository;

import com.shzhangji.proton.entity.UserPrimaryDaily;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserPrimaryRepository {
  private final NamedParameterJdbcTemplate jdbcTemplate;

  public List<UserPrimaryDaily> getUserPrimaryData(LocalDate startDate, LocalDate endDate) {
    var dfDate = DateTimeFormatter.ofPattern("yyyyMMdd");
    var params = new HashMap<String, String>();
    params.put("start_date", startDate.format(dfDate));
    params.put("end_date", endDate.format(dfDate));
    return jdbcTemplate.query("""
        SELECT
            DATE(report_date) AS report_date
            ,user_count
            ,session_count
            ,single_page_user_count / user_count AS bounce_rate
            ,ROUND(total_session_seconds / user_count) AS session_duration
        FROM da_user_primary_daily
        WHERE report_date BETWEEN :start_date AND :end_date
        """,
        params, new BeanPropertyRowMapper<>(UserPrimaryDaily.class));
  }
}
