package com.sx.qz2.service.read;


import com.sx.qz2.dao.read.DataReadDao;
import com.sx.qz2.entity.common.ResStatus;
import com.sx.qz2.entity.req.NodeInfoEntity;
import com.sx.qz2.entity.req.NodeInfoReqEntity;
import com.sx.qz2.entity.res.*;
import com.sx.qz2.util.TableAndColumnUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * @Author: ZhangQi
 * @Date: 2023/7/21 10:48
 * @Description:
 */
@Service
public class DataReadServiceImpl implements DataReadService{

    @Autowired
    private DataReadDao dataReadDao;


    @Override
    public ListsResEntity ssVoltageRead(NodeInfoReqEntity nodeInfoReqEntity) {
        ListsResEntity listsResEntity = new ListsResEntity();
        listsResEntity.setUser("QZ");
        try {
            listsResEntity.setNodeNum(nodeInfoReqEntity.getNodeNum());
            // 根据节点信息判断节点所在的表和字段编号
            NodeInfoEntity nodeInfoEntity = dataReadDao.getNodeInfo(nodeInfoReqEntity.getNodeNum());
            NodeInfoEntity nodeInfoAll = new TableAndColumnUtils().columnNames(nodeInfoEntity);
            // 时间轴查询
            String[] times = dataReadDao.getTimeAxis();
            if (nodeInfoEntity == null){
                listsResEntity.setStatus(ResStatus.FAILED);
            }else {
                List<SsVolResEntity> lists = dataReadDao.getSsVoltage(nodeInfoAll);
                Collections.reverse(lists);
                if (lists.isEmpty() | lists == null){
                    listsResEntity.setStatus(ResStatus.FAILED);
                }else {
                    listsResEntity.setSsVolResEntityList(lists);
                    listsResEntity.setTimes(times);
                    listsResEntity.setStatus(ResStatus.SUCCESS);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listsResEntity;
    }

    @Override
    public ListsResEntity lineCtAndPRead(NodeInfoReqEntity nodeInfoReqEntity) {
        ListsResEntity listsResEntity = new ListsResEntity();
        listsResEntity.setUser("QZ");
        try {
            listsResEntity.setNodeNum(nodeInfoReqEntity.getNodeNum());
            // 根据节点信息判断节点所在的表和字段编号
            NodeInfoEntity nodeInfoEntity = dataReadDao.getNodeInfo(nodeInfoReqEntity.getNodeNum());
            NodeInfoEntity nodeInfoAll = new TableAndColumnUtils().columnNames(nodeInfoEntity);
            // 时间轴查询
            String[] times = dataReadDao.getTimeAxis();
            if (nodeInfoEntity == null){
                listsResEntity.setStatus(ResStatus.FAILED);
            }else {
                List<LineCtAndPResEntity> lists = dataReadDao.getLineCtAndP(nodeInfoAll);
                Collections.reverse(lists);
                if (lists.isEmpty() | lists == null){
                    listsResEntity.setStatus(ResStatus.FAILED);
                }else {
                    listsResEntity.setLineCtAndPResEntityList(lists);
                    listsResEntity.setTimes(times);
                    listsResEntity.setStatus(ResStatus.SUCCESS);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listsResEntity;
    }

    @Override
    public ListsResEntity installedCapRead() {
        ListsResEntity listsResEntity = new ListsResEntity();
        try {
            List<InstalledCapEntity> list = dataReadDao.getInstalledCap();
            if (list == null){
                listsResEntity.setStatus(ResStatus.FAILED);
            }else {
                listsResEntity.setUser("QZ");
                listsResEntity.setStatus(ResStatus.SUCCESS);
                listsResEntity.setInstalledCapEntityList(list);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listsResEntity;
    }

    @Override
    public ListsResEntity loadInfoRead() {
        ListsResEntity listsResEntity = new ListsResEntity();
        try {
            List<LoadInfoEntity> list = dataReadDao.getLoadInfo();
            if (list == null){
                listsResEntity.setStatus(ResStatus.FAILED);
            }else {
                listsResEntity.setUser("QZ");
                listsResEntity.setStatus(ResStatus.SUCCESS);
                listsResEntity.setLoadInfoEntityList(list);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listsResEntity;
    }

    @Override
    public ListsResEntity gridScaleRead() {
        ListsResEntity listsResEntity = new ListsResEntity();
        try {
            List<GridScaleEntity> list = dataReadDao.getGridScale();
            if (list == null){
                listsResEntity.setStatus(ResStatus.FAILED);
            }else {
                listsResEntity.setUser("QZ");
                listsResEntity.setStatus(ResStatus.SUCCESS);
                listsResEntity.setGridScaleEntityList(list);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return listsResEntity;
    }

    @Override
    public ListsResEntity loadRateRead() {
        ListsResEntity listsResEntity = new ListsResEntity();
        try {
            List<LoadRateEntity> list1 = dataReadDao.getDev1LoadRateLine();
            List<LoadRateEntity> list2 = dataReadDao.getDev2LoadRateLine();
            List<LoadRateEntity> list3 = dataReadDao.getDev1LoadRateNode();
            List<LoadRateEntity> list4 = dataReadDao.getDev2LoadRateNode();
            list1.addAll(list2);
            list1.addAll(list3);
            list1.addAll(list4);
            if (list1 == null){
                listsResEntity.setStatus(ResStatus.FAILED);
            }else {
                listsResEntity.setUser("QZ");
                listsResEntity.setStatus(ResStatus.SUCCESS);
                listsResEntity.setLoadRateEntityList(list1);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return listsResEntity;
    }

}
