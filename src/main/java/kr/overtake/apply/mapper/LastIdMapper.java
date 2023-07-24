package kr.overtake.apply.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface LastIdMapper {
    @Select("""
            select last_insert_id()
            """)
    long selectLastInsertId();
}
