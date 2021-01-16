package com.jq.kafkaui.dao;

import com.jq.kafkaui.domain.Auth;
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
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void insert(ZKSource source);

    @Delete("delete from zookeeper_source where id = #{id}")
    void delete(Integer id);

    @Select("SELECT address FROM zookeeper_source where id = #{id}")
    String getAddress(Integer id);

    @Delete("delete from zookeeper_auth where source_id = #{sourceId}")
    int deleteAuth(Integer sourceId);

    @Insert("insert into zookeeper_auth (source_id,add_auth,update_auth,remove_auth) values(#{sourceId},#{add},#{update}, #{remove})")
    void insertAuth(Integer sourceId, Integer add, Integer update, Integer remove);

    @Select("select add_auth,update_auth,remove_auth from zookeeper_auth where source_id = #{sourceId}")
    Auth getAuthBySource(Integer sourceId);

    @Update({"update zookeeper_auth set add_auth=#{add}, update_auth=#{update}, remove_auth=#{remove} where source_id = #{id}"})
    int updateAuth(int id, int add, int update, int remove);

//    @Select("SELECT broker FROM source where id=#{sourceId}")
//    String selectById(Integer sourceId);
}
