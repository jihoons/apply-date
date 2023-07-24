package kr.overtake.apply.mapper.dto;

import java.time.LocalDateTime;

public interface ApplyVersioning {
    long getId();
    String getType();
    LocalDateTime getApplyStartDt();
    LocalDateTime getApplyEndDt();
    void setApplyEndDt(LocalDateTime applyEndDt);
}
