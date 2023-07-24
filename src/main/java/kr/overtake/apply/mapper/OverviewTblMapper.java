package kr.overtake.apply.mapper;

import kr.overtake.apply.mapper.dto.OverviewApply;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface OverviewTblMapper {
    @Select("""
            select type, overview_tbl_seq, apply_start_dt, apply_end_dt
            from (
            select 'lt' type, overview_tbl_seq, apply_start_dt, apply_end_dt
            from overview_apply
            where product_seq = #{productSeq}
              and apply_start_dt < #{applyStartDt}
              and apply_end_dt > #{applyStartDt}
            order by apply_start_dt desc
            limit 1) lt
            union all
            select type, overview_tbl_seq, apply_start_dt, apply_end_dt
            from (
            select 'gt' type, overview_tbl_seq, apply_start_dt, apply_end_dt
            from overview_apply
            where product_seq = #{productSeq}
              and apply_start_dt > #{applyStartDt}
            order by apply_start_dt asc
            limit 1) gt
            """)
    List<OverviewApply> selectOverviewClosest(@Param("productSeq") long productSeq, @Param("applyStartDt") LocalDateTime applyStartDt);

    @Insert("""
            insert into overview_apply (product_seq, name, apply_start_dt, apply_end_dt)
            values (#{productSeq}, #{name}, #{applyStartDt}, #{applyEndDt})
            """)
    int addOverview(@Param("productSeq") long productSeq, @Param("name") String name, @Param("applyStartDt") LocalDateTime applyStartdt, @Param("applyEndDt") LocalDateTime applyEndDt);

    @Update("""
            update overview_apply
            set apply_end_dt = #{applyEndDt}
            where overview_tbl_seq = #{overviewTblSeq}
            """)
    int modifyOverview(@Param("overviewTblSeq") long overviewTabSeq, @Param("applyEndDt") LocalDateTime applyEndDt);
}
