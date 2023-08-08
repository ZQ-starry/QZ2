package com.sx.qz2.service.process;

import com.sx.qz2.entity.result.ResultListsEntity;

/**
 * @Author: ZhangQi
 * @Date: 2023/7/31 10:35
 * @Description:
 */
public interface ProcessService {

    void getTablesName();

    void dev1DataProcess(ResultListsEntity resultListsEntity);

    void dev2DataProcess(ResultListsEntity resultListsEntity);
}
