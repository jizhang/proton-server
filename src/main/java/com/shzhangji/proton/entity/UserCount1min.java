package com.shzhangji.proton.entity;

import java.time.LocalDateTime;
import lombok.Data;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("da_user_count_1min")
public class UserCount1min {
  private LocalDateTime reportMinute;
  private long userCount;
  private LocalDateTime updatedAt;
}
