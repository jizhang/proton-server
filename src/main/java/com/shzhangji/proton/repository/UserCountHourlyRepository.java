package com.shzhangji.proton.repository;

import com.shzhangji.proton.entity.UserCountHourly;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface UserCountHourlyRepository extends CrudRepository<UserCountHourly, LocalDateTime> {
  List<UserCountHourly> findByReportHourBetween(LocalDateTime start, LocalDateTime end);
}
