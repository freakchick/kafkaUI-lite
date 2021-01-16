package com.jq.kafkaui.dao;

import com.jq.kafkaui.domain.Auth;
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
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void insert(RedisSource source);

    @Delete("delete from redis_source where id = #{id}")
    void delete(Integer id);


    @Delete("delete from redis_auth where source_id = #{sourceId}")
    int deleteAuth(Integer sourceId);

    @Insert("insert into redis_auth (source_id,add_auth,update_auth,remove_auth) values(#{sourceId},#{add},#{update}, #{remove})")
    void insertAuth(Integer sourceId, Integer add, Integer update, Integer remove);

    @Select("select add_auth,update_auth,remove_auth from redis_auth where source_id = #{sourceId}")
    Auth getAuthBySource(Integer sourceId);

    @Update({"update redis_auth set add_auth=#{add}, update_auth=#{update}, remove_auth=#{remove} where source_id = #{id}"})
    int updateAuth(int id, int add, int update, int remove);

}
