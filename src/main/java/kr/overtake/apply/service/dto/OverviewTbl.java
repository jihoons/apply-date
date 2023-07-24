package kr.overtake.apply.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import kr.overtake.apply.mapper.dto.ApplyVersioning;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OverviewTbl implements ApplyVersioning {
    private long overviewTblSeq;
    private long productSeq;
    private String name;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime applyStartDt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime applyEndDt;

    @Override
    public long getId() {
        return overviewTblSeq;
    }

    @Override
    public String getType() {
        return null;
    }
}
