package kr.overtake.apply.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import kr.overtake.apply.mapper.dto.ApplyVersioning;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class HouseGrade implements ApplyVersioning {
    private long houseGradeSeq;
    private long productSeq;
    private String name;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime applyStartDt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime applyEndDt;

    private List<HouseDongGrade> gradeList;

    @Override
    public long getId() {
        return houseGradeSeq;
    }

    @Override
    public String getType() {
        return null;
    }

    @Data
    public static class HouseDongGrade {
        private String dongCd;
        private String grade;
    }
}
