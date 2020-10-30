package com.jq.kafkaui.dao;

import com.jq.kafkaui.domain.Source;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SourceDao {

    @Select("SELECT * FROM source")
    @Results({@Result(property = "name", column = "name"),
            @Result(property = "broker", column = "broker"),
            @Result(property = "id", column = "id")})
    List<Source> getAll();

    @Insert({"insert into source (name, broker) values (#{name}, #{broker})"})
    void insert(Source source);

    @Delete("")
    void delete(Integer id);
}
