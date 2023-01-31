package com.shzhangji.proton.repository;

import com.shzhangji.proton.entity.UserCount1min;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCount1minRepository extends CrudRepository<UserCount1min, LocalDateTime> {
  List<UserCount1min> findByReportMinuteAfter(LocalDateTime date);
}
