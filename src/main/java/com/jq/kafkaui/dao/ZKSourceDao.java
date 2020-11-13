package com.jq.kafkaui.dao;

import com.jq.kafkaui.domain.Source;
import com.jq.kafkaui.domain.ZKSource;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @program: kafkaUI
 * @description:
 * @author: jiangqiang
 * @create: 2020-11-13 15:28
 **/
@Mapper
public interface ZKSourceDao {

    @Results({@Result(property = "name", column = "name"),
            @Result(property = "address", column = "address"),
            @Result(property = "id", column = "id")})

    @Select("SELECT * FROM zookeeper_source")
    List<ZKSource> getAll();


    @Insert({"insert into zookeeper_source (name, address) values (#{name}, #{address})"})
    void insert(ZKSource source);

    @Delete("delete from zookeeper_source where id = #{id}")
    void delete(Integer id);
}
