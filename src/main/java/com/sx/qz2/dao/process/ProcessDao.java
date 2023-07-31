package com.sx.qz2.dao.process;

import com.sx.qz2.entity.result.LineResultEntity;
import com.sx.qz2.entity.result.NodeResultEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

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

    void insertLineResult(@Param("tableName") String name,LineResultEntity lineResultEntity);

    void insertNodeResult(@Param("tableName") String name, NodeResultEntity nodeResultEntity);

}
