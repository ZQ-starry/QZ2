package com.sx.qz2.controller.ads;

import com.sx.qz2.entity.result.ResultListsEntity;
import com.sx.qz2.service.process.ProcessService;
import com.sx.qz2.util.AdsReadUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

/**
 * @Author: ZhangQi
 * @Date: 2023/7/31 10:30
 * @Description:
 */
@RestController
@Slf4j
public class ResultAdsController {

    private static int countDev1 = 0;

    private static int countDev2 = 0;

    @Autowired
    private ProcessService processService;

    @PostConstruct
    public void getTablesName(){
        processService.getTablesName();
    }

    @RequestMapping("/names")
    // @Scheduled(fixedDelay = 500) //上一次执行完毕时间点向后每隔20秒执行一次
    // @Scheduled(initialDelay=3000, fixedDelay = 20000)
    public void dev1ResultRead(){
        log.info("第"+ countDev1 +"次执行");
        if (countDev1 == 35040){
            // 读取完一年的数据就停止
            System.out.println(countDev1);
        }else {
            // 调用设备2数据读取方法
            this.dev2ResultRead();
            countDev1++;
            ResultListsEntity resultListsEntity;
            resultListsEntity = new AdsReadUtil().dev1StructRead();
            processService.dev1DataProcess(resultListsEntity);
        }
}

    // @RequestMapping("/names")
    @Async("task2")  //异步，与设备一的读取同时进行
    public void dev2ResultRead(){
        log.info("第"+ countDev2+1 +"次执行");
        countDev2++;
        ResultListsEntity resultListsEntity;
        resultListsEntity = new AdsReadUtil().dev2StructRead();
        processService.dev2DataProcess(resultListsEntity);
    }
}
