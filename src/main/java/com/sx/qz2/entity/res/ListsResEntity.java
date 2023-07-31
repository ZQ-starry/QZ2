package com.sx.qz2.entity.res;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sx.qz2.entity.common.BaseRes;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author: ZhangQi
 * @Date: 2023/7/24 10:11
 * @Description: 给前端返回的所有数据实体
 */
@Data
public class ListsResEntity extends BaseRes {


    @JsonInclude(JsonInclude.Include.NON_NULL)
    @ApiModelProperty(name = "ssVolResEntityList", value = "查询电压时仅返回该集合,不返回其他集合")
    private List<SsVolResEntity> ssVolResEntityList;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @ApiModelProperty(name = "lineCtAndPResEntityList", value = "查询电流及功率时仅返回该集合,不返回其他集合")
    private List<LineCtAndPResEntity> lineCtAndPResEntityList;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @ApiModelProperty(name = "installedCapEntityList", value = "查询装机规模时仅返回该集合,不返回其他集合")
    private List<InstalledCapEntity> installedCapEntityList;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @ApiModelProperty(name = "loadInfoEntityList", value = "查询负荷情况时仅返回该集合,不返回其他集合")
    private List<LoadInfoEntity> loadInfoEntityList;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @ApiModelProperty(name = "gridScaleEntityList", value = "查询电网规模时仅返回该集合,不返回其他集合")
    private List<GridScaleEntity> gridScaleEntityList;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @ApiModelProperty(name = "loadRateEntityList", value = "查询负载率时仅返回该集合,不返回其他集合")
    private List<LoadRateEntity> loadRateEntityList;
}