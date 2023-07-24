package kr.overtake.apply.mapper.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OverviewApply implements ApplyVersioning {
    private String type;
    private long overviewTblSeq;
    private LocalDateTime applyStartDt;
    private LocalDateTime applyEndDt;

    @Override
    public long getId() {
        return overviewTblSeq;
    }
}
