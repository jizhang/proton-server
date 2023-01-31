package com.shzhangji.proton.repository;

import com.shzhangji.proton.entity.UserCountRt;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCountRtRepository extends CrudRepository<UserCountRt, LocalDateTime> {
  Optional<UserCountRt> findFirstByReportMinuteAfterOrderByReportMinuteDesc(LocalDateTime date);
}
