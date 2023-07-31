package com.sx.qz2.controller.ads;

import com.sx.qz2.component.SpringUtil;
import com.sx.qz2.entity.result.ResultListsEntity;
import com.sx.qz2.service.process.ProcessService;
import com.sx.qz2.util.AdsReadUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

/**
 * @Author: ZhangQi
 * @Date: 2023/7/31 10:30
 * @Description:
 */
@RestController
public class ResultAdsController {

    private ProcessService processService = SpringUtil.getBean(ProcessService.class);


    @PostConstruct
    public void getTablesName(){
        processService.getTablesName();
    }

    @RequestMapping("/names")
    // 加个定时器
    public void dev1ResultRead(){
       ResultListsEntity resultListsEntity;
       resultListsEntity = new AdsReadUtil().dev1StructRead();
       processService.dev1DataProcess(resultListsEntity);
    }
}
