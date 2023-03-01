package com.shzhangji.proton.repository;

import com.shzhangji.proton.entity.UserRetentionWeekly;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRetentionWeeklyRepository
    extends CrudRepository<UserRetentionWeekly, Integer> {
  List<UserRetentionWeekly> findByReportWeekBetweenOrderByReportWeek(int start, int end);
}
