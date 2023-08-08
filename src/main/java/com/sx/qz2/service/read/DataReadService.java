package com.sx.qz2.service.read;

import com.sx.qz2.entity.req.NodeInfoReqEntity;
import com.sx.qz2.entity.res.ListsResEntity;

/**
 * @Author: ZhangQi
 * @Date: 2023/7/21 10:48
 * @Description:
 */
public interface DataReadService {

    ListsResEntity ssVoltageRead(NodeInfoReqEntity nodeInfoReqEntity);

    ListsResEntity lineCtAndPRead(NodeInfoReqEntity nodeInfoReqEntity);

    ListsResEntity installedCapRead();

    ListsResEntity loadInfoRead();

    ListsResEntity gridScaleRead();

    ListsResEntity loadRateRead();

}
