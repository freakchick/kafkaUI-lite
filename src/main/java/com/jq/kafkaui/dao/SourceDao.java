package com.jq.kafkaui.dao;

import com.jq.kafkaui.domain.Source;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SourceDao {

    @Select("SELECT * FROM source")
    @Results({@Result(property = "name", column = "name"),
            @Result(property = "broker", column = "broker")})
    List<Source> getAll();
}
