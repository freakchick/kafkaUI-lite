package com.jq.kafkaui.dao;

import com.jq.kafkaui.domain.RedisSource;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @program: kafkaUI
 * @description:
 * @author: jiangqiang
 * @create: 2020-11-12 17:42
 **/
@Mapper
public interface RedisSourceDao {

    @Select("SELECT * FROM redis_source")
    @Results({@Result(property = "name", column = "name"),
            @Result(property = "ip", column = "ip"),
            @Result(property = "port", column = "port"),
            @Result(property = "password", column = "password"),
            @Result(property = "id", column = "id")})
    List<RedisSource> getAll();

    @Select("SELECT * FROM redis_source where id = #{id}")
    RedisSource selectById(Integer id);

    @Insert({"insert into redis_source (name, ip,port,password) values (#{name}, #{ip}, #{port}, #{password})"})
    void insert(RedisSource source);

    @Delete("delete from redis_source where id = #{id}")
    void delete(Integer id);
}
