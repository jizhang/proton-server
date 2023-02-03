package com.shzhangji.proton.repository;

import com.shzhangji.proton.entity.UserChannelDaily;
import com.shzhangji.proton.entity.UserReferralDaily;
import com.shzhangji.proton.entity.UserSourceMediumDaily;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserSourceDailyRepository {
  private static final DateTimeFormatter DF_DATE = DateTimeFormatter.ofPattern("yyyyMMdd");

  private final JdbcTemplate jdbcTemplate;

  public List<UserChannelDaily> getChannelData(LocalDate startDate, LocalDate endDate) {
    return jdbcTemplate.query("""
        SELECT
            DATE(report_date) AS report_date
            ,channel
            ,SUM(user_count) AS user_count
        FROM da_user_source_daily
        WHERE report_date BETWEEN ? AND ?
        GROUP BY report_date, channel
        """,
        new BeanPropertyRowMapper<>(UserChannelDaily.class),
        startDate.format(DF_DATE),
        endDate.format(DF_DATE));
  }

  public List<UserSourceMediumDaily> getSourceMediumData(LocalDate startDate, LocalDate endDate) {
    return jdbcTemplate.query("""
        SELECT
            DATE(report_date) AS report_date
            ,source
            ,medium
            ,user_count
        FROM da_user_source_daily
        WHERE report_date BETWEEN ? AND ?
        """,
        new BeanPropertyRowMapper<>(UserSourceMediumDaily.class),
        startDate.format(DF_DATE),
        endDate.format(DF_DATE));
  }

  public List<UserReferralDaily> getReferralData(LocalDate startDate, LocalDate endDate) {
    return jdbcTemplate.query("""
        SELECT
            DATE(report_date) AS report_date
            ,source AS referral
            ,user_count
        FROM da_user_source_daily
        WHERE report_date BETWEEN ? AND ?
        AND medium = 'referral'
        """,
        new BeanPropertyRowMapper<>(UserReferralDaily.class),
        startDate.format(DF_DATE),
        endDate.format(DF_DATE));
  }
}
