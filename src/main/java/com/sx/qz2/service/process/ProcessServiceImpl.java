package com.sx.qz2.service.process;

import com.sx.qz2.component.WebSocketService;
import com.sx.qz2.dao.process.ProcessDao;
import com.sx.qz2.entity.common.ResStatus;
import com.sx.qz2.entity.res.LineCtAndPResEntity;
import com.sx.qz2.entity.res.ListsResEntity;
import com.sx.qz2.entity.result.LineResultEntity;
import com.sx.qz2.entity.result.NodeResultEntity;
import com.sx.qz2.entity.result.ResultListsEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * @Author: ZhangQi
 * @Date: 2023/7/31 10:35
 * @Description:
 */
@Service
public class ProcessServiceImpl implements ProcessService{

    private static String[] tableNames = new String[30];
    private static String[] numbers = new String[419];

    @Autowired
    private ProcessDao processDao;

    @Autowired
    private WebSocketService webSocketService;


    /**
     * 项目开始后执行一次
     */
    @Override
    public void getTablesName() {
        // 取表名
        tableNames = processDao.getTablesName();
        numbers = processDao.getNodeNum();
        System.out.println(Arrays.toString(tableNames));
    }

    @Override
    @Async("taskExecutor1")
    public void dev1DataProcess(ResultListsEntity resultListsEntity) {
        // 执行数据插入，线路数据插入result的01101-01108，节点数据插入01202-01208
        List<LineResultEntity> lineList = resultListsEntity.getLineResultEntityList();
        List<NodeResultEntity> nodeList = resultListsEntity.getNodeResultEntityList();
        /*
        发送给前端,对lineList做一下拆分，拆分为一条线路一个实体类
        先查线路对应的node_num
         */
        ListsResEntity listsResEntity = new ListsResEntity();
        LineCtAndPResEntity lineCtAndPResEntity = new LineCtAndPResEntity();
        for (int i=0; i<lineList.size(); i++){
            // 返回list放入节点的编号，如zh001
            listsResEntity.setNodeNum(numbers[i]);
            if (lineList != null){
                listsResEntity.setStatus(ResStatus.SUCCESS);
                // 以下内容可以改为映射，循环赋值
                lineCtAndPResEntity.setCurrentA(lineList.get(i).getCurrent1A());
                lineCtAndPResEntity.setCurrentB(lineList.get(i).getCurrent1B());
                lineCtAndPResEntity.setCurrentC(lineList.get(i).getCurrent1C());
                lineCtAndPResEntity.setPowerActive(lineList.get(i).getPowerActive1());
                lineCtAndPResEntity.setPowerReactive(lineList.get(i).getPowerReactive1());
                webSocketService.sendTextMsg("/response/lineResult",listsResEntity);
            }else {
                listsResEntity.setStatus(ResStatus.FAILED);
            }
        }
        for (int i=0; i<nodeList.size(); i++){
            // 返回list放入节点的编号，如zh001
            listsResEntity.setNodeNum(numbers[i+229]);
            if (lineList != null){
                listsResEntity.setStatus(ResStatus.SUCCESS);
                // 以下内容可以改为映射，循环赋值
                lineCtAndPResEntity.setCurrentA(nodeList.get(i).getVoltage1A());
                lineCtAndPResEntity.setCurrentB(nodeList.get(i).getVoltage1B());
                lineCtAndPResEntity.setCurrentC(nodeList.get(i).getVoltage1C());
                webSocketService.sendTextMsg("/response/nodeResult",listsResEntity);
            }else {
                listsResEntity.setStatus(ResStatus.FAILED);
            }
        }
        for (int i=0; i<tableNames.length; i++){
            if (i<8){
                // 插入线路的数据,设备一的线路结果数据表从01101-01108，对应names位置0-7
                processDao.insertLineResult(tableNames[i],lineList.get(i));
            }else {
                // 插入节点的数据，设备一的节点结果数据表从01202-01208，对应names位置16-22
                processDao.insertNodeResult(tableNames[i+9], nodeList.get(i));
            }
        }

    }

}
