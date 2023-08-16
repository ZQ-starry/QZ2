package com.sx.qz2.controller.read;


import com.sx.qz2.entity.req.NodeInfoReqEntity;
import com.sx.qz2.entity.res.ListsResEntity;
import com.sx.qz2.service.read.DataReadService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

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
    // @GetMapping("/getSsVol")
    // @ResponseBody
    // @ApiImplicitParam(required = true)
    // @ApiOperation(value = "变电站的各相的等效电压查询")
    @MessageMapping("/getSsVol")
    @SendToUser("/response/getSsVol")
    public ListsResEntity ssVoltageRead(NodeInfoReqEntity nodeInfoReqEntity){
        return dataReadService.ssVoltageRead(nodeInfoReqEntity);
    }

    /**
     * 线路的各相的等效电流查询及功率查询
     * @param nodeInfoReqEntity
     * @return
     */
    // @GetMapping("/getLineCtAndP")
    //     // @ResponseBody
    //     // @ApiImplicitParam(required = true)
    //     // @ApiOperation(value = "线路的各相的电流查询及功率查询")
    @MessageMapping("/getLineCtAndP")
    @SendToUser("/response/getLineCtAndP")
    public ListsResEntity lineCtAndPRead(NodeInfoReqEntity nodeInfoReqEntity){
        return dataReadService.lineCtAndPRead(nodeInfoReqEntity);
    }

    /**
     * 装机规模查询
     * @return
     */
    // @GetMapping("/getInstalledCap")
    // @ResponseBody
    // @ApiOperation(value = "主网装机规模查询")
    @MessageMapping("/getInstalledCap")
    @SendToUser("/response/getInstalledCap")
    public ListsResEntity installedCapRead(){
        return dataReadService.installedCapRead();
    }

    /**
     * 负荷情况查询
     * @return
     */
    // @GetMapping("/getLoadInfo")
    // @ResponseBody
    // @ApiOperation(value = "主网负荷情况查询")
    @MessageMapping("/getLoadInfo")
    @SendToUser("/response/getLoadInfo")
    public ListsResEntity loadInfoRead(){
        return dataReadService.loadInfoRead();
    }

    /**
     * 电网规模查询
     * @return
     */
    // @GetMapping("/getGridScale")
    // @ResponseBody
    // @ApiOperation(value = "主网电网规模查询")
    @MessageMapping("/getGridScale")
    @SendToUser("/response/getGridScale")
    public ListsResEntity gridScaleRead(){
        return dataReadService.gridScaleRead();
    }

    /**
     * 主网负载率查询
     * @return
     */
    // @GetMapping("/getLoadRate")
    // @ResponseBody
    // @ApiOperation(value = "主网负载率查询")
    @MessageMapping("/getLoadRate")
    @SendToUser("/response/getLoadRate")
    public ListsResEntity loadRateRead(){
        return dataReadService.loadRateRead();
    }

}
