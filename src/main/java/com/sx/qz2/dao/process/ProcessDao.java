package com.sx.qz2.dao.process;

import com.sx.qz2.entity.res.LoadRateEntity;
import com.sx.qz2.entity.result.LineResultEntity;
import com.sx.qz2.entity.result.NodeResultEntity;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: ZhangQi
 * @Date: 2023/7/31 10:38
 * @Description:
 */
@Mapper
@Repository
public interface ProcessDao {

    @Select("SELECT table_name FROM tables_name")
    String[] getTablesName();

    @Select("SELECT node_num FROM (SELECT * FROM node_info ORDER BY order_num) AS temp")
    String[] getNodeNum();
    //查询线路和节点的额定容量
    @Select("SELECT  rated_capacity FROM  dev1_capacity_line ORDER BY id asc")
    Float[] getLineRatedCurrentDev1();

    @Select("SELECT  rated_capacity FROM  dev2_capacity_line ORDER BY id asc")
    Float[] getLineRatedCurrentDev2();

    @Select("SELECT  rated_capacity FROM  dev1_capacity_node ORDER BY id asc")
    Float[] getNodeRatedCapacityDev1();

    // @Select("SELECT  rated_capacity FROM  dev2_capacity_node ORDER BY id asc")
    // Float[] getNodeRatedCapacityDev2();

    int updateLineLoadRatioDev1( List<LoadRateEntity> updateList);

    int updateNodeLoadRatioDev1(List<LoadRateEntity>updateList);

    int updateLineLoadRatioDev2( List<LoadRateEntity> updateList);

    // int updateNodeLoadRatioDev2(List<LoadRateEntity> updateList);

    LineResultEntity selectLineData(String tableName);

    void insertLineResult(@Param("tableName") String name, @Param("line") LineResultEntity lineResultEntity);

    void insertNodeResult(@Param("tableName") String name, @Param("node")NodeResultEntity nodeResultEntity);

    @Options(useGeneratedKeys=true)
    @Insert("INSERT INTO time_axis SET time = #{time}")
    void insertTime(String time);
}
