package com.sx.qz2.dao.read;

import com.sx.qz2.entity.req.NodeInfoEntity;
import com.sx.qz2.entity.res.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: ZhangQi
 * @Date: 2023/7/13 12:32
 * @Description:
 */
@Mapper
@Repository
public interface DataReadDao {

    @Select("SELECT * FROM node_info WHERE node_num = #{nodeNum} ")
    NodeInfoEntity getNodeInfo(String nodeNum);

    List<SsVolResEntity> getSsVoltage(NodeInfoEntity nodeInfoEntity);

    List<LineCtAndPResEntity> getLineCtAndP(NodeInfoEntity nodeInfoEntity);

    @Select("SELECT * FROM installed_capacity")
    List<InstalledCapEntity> getInstalledCap();

    @Select("SELECT * FROM load_info")
    List<LoadInfoEntity> getLoadInfo();

    @Select("SELECT * FROM grid_scale")
    List<GridScaleEntity> getGridScale();

    @Select("SELECT * FROM dev1_load_rate_line")
    List<LoadRateEntity> getDev1LoadRateLine();
    @Select("SELECT * FROM dev2_load_rate_line")
    List<LoadRateEntity> getDev2LoadRateLine();
    @Select("SELECT * FROM dev1_load_rate_node")
    List<LoadRateEntity> getDev1LoadRateNode();
    @Select("SELECT * FROM dev2_load_rate_node")
    List<LoadRateEntity> getDev2LoadRateNode();
    @Select("SELECT * FROM line_load_rate_info")
    List<LoadRateEntity> getLineLoadRate();

    @Select("SELECT time FROM (SELECT * FROM time_axis ORDER BY id LIMIT 40) AS TEMP")
    String[] getTimeAxis();
}
