package com.sx.qz2.controller.read;


import com.sx.qz2.entity.req.NodeInfoReqEntity;
import com.sx.qz2.entity.res.ListsResEntity;
import com.sx.qz2.service.read.DataReadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author: ZhangQi
 * @Date: 2023/7/18 13:35
 * @Description: 数据查询控制层
 */
@Controller
@Api(tags = "主网数据查询模块")
public class DataReadController {

    @Autowired
    private DataReadService dataReadService;


    /**
     * 变电站的各相的等效电压查询
     * @param nodeInfoReqEntity
     * @return
     */
    @GetMapping("/getSsVol")
    @ResponseBody
    @ApiImplicitParam(required = true)
    @ApiOperation(value = "变电站的各相的等效电压查询")
    public ListsResEntity ssVoltageRead(NodeInfoReqEntity nodeInfoReqEntity){
        return dataReadService.ssVoltageRead(nodeInfoReqEntity);
    }

    /**
     * 线路的各相的等效电流查询及功率查询
     * @param nodeInfoReqEntity
     * @return
     */
    @GetMapping("/getLineCtAndP")
    @ResponseBody
    @ApiImplicitParam(required = true)
    @ApiOperation(value = "线路的各相的电流查询及功率查询")
    public ListsResEntity lineCtAndPRead(NodeInfoReqEntity nodeInfoReqEntity){
        return dataReadService.lineCtAndPRead(nodeInfoReqEntity);
    }

    /**
     * 装机规模查询
     * @return
     */
    @GetMapping("/getInstalledCap")
    @ResponseBody
    @ApiOperation(value = "主网装机规模查询")
    public ListsResEntity installedCapRead(){
        return dataReadService.installedCapRead();
    }

    /**
     * 负荷情况查询
     * @return
     */
    @GetMapping("/getLoadInfo")
    @ResponseBody
    @ApiOperation(value = "主网负荷情况查询")
    public ListsResEntity loadInfoRead(){
        return dataReadService.loadInfoRead();
    }

    /**
     * 电网规模查询
     * @return
     */
    @GetMapping("/getGridScale")
    @ResponseBody
    @ApiOperation(value = "主网电网规模查询")
    public ListsResEntity gridScaleRead(){
        return dataReadService.gridScaleRead();
    }

    /**
     * 主网线路负载率查询
     * @return
     */
    @GetMapping("/getLineLoadRate")
    @ResponseBody
    @ApiOperation(value = "主网线路负载率查询")
    public ListsResEntity lineLoadRateRead(){
        return dataReadService.lineLoadRateRead();
    }

    /**
     * 主网其他节点负载率查询
     * @return
     */
    @GetMapping("/getLoadRate")
    @ResponseBody
    @ApiOperation(value = "主网其他节点负载率查询")
    public ListsResEntity loadRateRead(){
        return dataReadService.loadRateRead();
    }


}
