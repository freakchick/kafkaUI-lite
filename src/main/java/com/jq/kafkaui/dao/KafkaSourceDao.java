package com.jq.kafkaui.dao;

import com.jq.kafkaui.domain.Auth;
import com.jq.kafkaui.domain.KafkaSource;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface KafkaSourceDao {

    @Select("SELECT * FROM source")
    @Results({@Result(property = "name", column = "name"),
            @Result(property = "source", column = "source"),
            @Result(property = "id", column = "id")})
    List<KafkaSource> getAll();

    @Insert("insert into source (name, source) values (#{name}, #{source})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void insert(KafkaSource source);

    @Delete("delete from source where id = #{id}")
    void delete(Integer id);

    @Delete("delete from kafka_auth where source_id = #{sourceId}")
    int deleteAuth(Integer sourceId);

    @Insert("insert into kafka_auth (source_id,add_auth,update_auth,remove_auth) values(#{sourceId},#{add},#{update}, #{remove})")
    void insertAuth(Integer sourceId, Integer add, Integer update, Integer remove);

    @Select("select add_auth,update_auth,remove_auth from kafka_auth where source_id = #{sourceId}")
    Auth getAuthBySource(Integer sourceId);

    @Update({"update kafka_auth set add_auth=#{add}, update_auth=#{update}, remove_auth=#{remove} where source_id = #{id}"})
    int updateAuth(int id, int add, int update, int remove);

    @Select("SELECT broker FROM source where id=#{sourceId}")
    String selectById(Integer sourceId);
}
