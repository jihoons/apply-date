package kr.overtake.apply.mapper.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class HouseGradeApply implements ApplyVersioning {
    private String type;
    private long houseGradeSeq;
    private LocalDateTime applyStartDt;
    private LocalDateTime applyEndDt;

    @Override
    public long getId() {
        return houseGradeSeq;
    }
}
