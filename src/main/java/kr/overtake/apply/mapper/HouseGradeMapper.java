package kr.overtake.apply.mapper;

import kr.overtake.apply.mapper.dto.HouseGradeApply;
import kr.overtake.apply.mapper.dto.HouseGradeDetail;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface HouseGradeMapper {

    @Select("""
            select type, house_grade_seq houseGrade_seq, apply_start_dt, apply_end_dt
            from (
            select 'lt' type, house_grade_seq, apply_start_dt, apply_end_dt
            from house_grade_apply
            where product_seq = #{productSeq}
              and apply_start_dt < #{applyStartDt}
              and apply_end_dt > #{applyStartDt}
            order by apply_start_dt desc
            limit 1) lt
            union all
            select type, house_grade_seq, apply_start_dt, apply_end_dt
            from (
            select 'gt' type, house_grade_seq, apply_start_dt, apply_end_dt
            from house_grade_apply
            where product_seq = #{productSeq}
              and apply_start_dt > #{applyStartDt}
            order by apply_start_dt asc
            limit 1) gt
            """)
    List<HouseGradeApply> selectHouseGradeClosest(@Param("productSeq") long productSeq, @Param("applyStartDt") LocalDateTime applyStartDt);

    @Insert("""
            insert into house_grade_apply (product_seq, name, apply_start_dt, apply_end_dt)\s
            values (#{productSeq}, #{name}, #{applyStartDt}, #{applyEndDt})
            """)
    int addHouseGrade(@Param("productSeq") long productSeq, @Param("name") String name, @Param("applyStartDt") LocalDateTime applyStartdt, @Param("applyEndDt") LocalDateTime applyEndDt);

    @Update("""
            update house_grade_apply
            set apply_end_dt = #{applyEndDt}
            where house_grade_seq = #{houseGradeSeq}
            """)
    int modifyHouseGrade(@Param("houseGradeSeq") long houseGradeSeq, @Param("applyEndDt") LocalDateTime applyEndDt);

    @Insert("""
            insert into house_grade_apply_detail (house_grade_seq, dong_cd, grade)
            values (#{houseGradeSeq}, #{dongCd}, #{grade})
            """)
    int addHouseGradeDetail(@Param("houseGradeSeq") long houseGradeSeq, @Param("dongCd") String dongCd, @Param("grade") String grade);

    @Update("""
            update house_grade_apply_detail 
            set grade = #{grade}
            where house_grade_detail_seq = #{houseGradeDetailSeq}
            """)
    int modifyHouseGradeDetail(@Param("houseGradeDetailSeq") long houseGradeDetailSeq, @Param("grade") String grade);

    @Update("""
            update house_grade_apply_detail 
            set status = 'deleted'
            where house_grade_detail_seq = #{houseGradeDetailSeq}
            """)
    int removeHouseGradeDetail(@Param("houseGradeDetailSeq") long houseGradeDetailSeq);

    @Select("""
            select house_grade_detail_seq, house_grade_seq, dong_cd, grade
            from house_grade_apply_detail
            where house_grade_seq = #{houseGradeSeq}
              and status = 'active'
            """)
    List<HouseGradeDetail> selectHouseGradeDetailList(@Param("houseGradeSeq") long houseGradeSeq);
}
