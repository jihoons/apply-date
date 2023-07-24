package kr.overtake.apply.mapper.dto;

import lombok.Data;

@Data
public class HouseGradeDetail {
    private long houseGradeDetailSeq;
    private long houseGradeSeq;
    private String dongCd;
    private String grade;
}
