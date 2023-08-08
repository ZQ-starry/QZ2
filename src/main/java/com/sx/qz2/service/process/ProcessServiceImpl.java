package com.sx.qz2.service.process;

import com.sx.qz2.component.WebSocketService;
import com.sx.qz2.dao.process.ProcessDao;
import com.sx.qz2.dao.read.DataReadDao;
import com.sx.qz2.entity.common.ResStatus;
import com.sx.qz2.entity.res.LineCtAndPResEntity;
import com.sx.qz2.entity.res.ListsResEntity;
import com.sx.qz2.entity.res.LoadRateEntity;
import com.sx.qz2.entity.res.SsVolResEntity;
import com.sx.qz2.entity.result.LineResultEntity;
import com.sx.qz2.entity.result.NodeResultEntity;
import com.sx.qz2.entity.result.ResultListsEntity;
import com.sx.qz2.util.DateUtils;
import com.sx.qz2.util.LoadRateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.util.ArrayList;
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

    private DateUtils dateUtils = new DateUtils();
    // 用于计算时间轴
    private static int countDev1 = 0;

    private static int countDev2 = 0;

    @Autowired
    private ProcessDao processDao;

    @Autowired
    private WebSocketService webSocketService;

    @Autowired
    private DataReadDao dataReadDao;


    /**
     * 项目开始后执行一次
     */
    @Override
    public void getTablesName() {
        // 取结果要保存的表名
        tableNames = processDao.getTablesName();
        // 取线路与节点的编号，如zh001，zh002
        numbers = processDao.getNodeNum();
        // 截断所有结果表
//        for (String tableName:tableNames){
//            processDao.truncateTable(tableName);
//        }
        System.out.println(Arrays.toString(tableNames));
    }

    @Override
    @Transactional
    public void dev1DataProcess (ResultListsEntity resultListsEntity)  {
        // 时间轴,获取时间并插入表
        String[] times = new String[1];
        times[0] = dateUtils.timelineUtil(countDev1+1);
        countDev1++;
        processDao.insertTime(times[0]);

        // 执行数据插入，线路数据插入result的01101-01108，节点数据插入01202-01208
        List<LineResultEntity> lineList = resultListsEntity.getLineResultEntityList();
        List<NodeResultEntity> nodeList = resultListsEntity.getNodeResultEntityList();
        /*
        发送给前端,对lineList和nodeList 做一下拆分，拆分为一条线路一个实体类
         */
        // 处理线路的电流与功率数据
        for (int i=0; i<lineList.size(); i++){
            // 返回list放入节点的编号，如zh001
            for (int j=0;j<15;j++){
                ListsResEntity listsResEntity = new ListsResEntity();
                listsResEntity.setNodeNum(numbers[i*15+j]);
                listsResEntity.setTimes(times);
                lineDataProcessor(lineList, i, j, listsResEntity);
            }
        }
        // 处理节点的电压数据
        for (int i=0; i<nodeList.size(); i++){
            for(int j=0;j<15;j++){
                // 返回list放入节点的编号，如zh001
                //每次需要传只含有一个点的信息的集合给前端 所以每次循环都需要新建一个集合和一个实体类。
                ListsResEntity listsResEntity = new ListsResEntity();
                listsResEntity.setNodeNum(numbers[i*15+j+229]);
                listsResEntity.setTimes(times);
                nodeDataProcessor(nodeList, i, j, listsResEntity);
            }
        }
        /**
         * 线路结果数据和节点结果数据插入数据库
         */
        for (int i=0; i<15; i++){
            if (i<8){
                // 插入线路的数据,设备一的线路结果数据表从01101-01108，对应names位置0-7
                processDao.insertLineResult(tableNames[i],lineList.get(i));
            }else {
                // 插入节点的数据，设备一的节点结果数据表从01202-01208，对应names位置16-22
                processDao.insertNodeResult(tableNames[i+8], nodeList.get(i-8));
            }
        }
        /*
           线路与节点（变电站）负载率计算
         */
        LoadRateUtil loadRateUtil = new LoadRateUtil();
        List<LineResultEntity> lineList1 = new ArrayList<>();
        // 获取线路的额定电流
        Float[] ratedCurrents = processDao.getLineRatedCurrentDev1();
        //取出Dev1中所有线路的信息
        for (int i=0; i<8; i++){
            LineResultEntity lineResultEntity = processDao.selectLineData(tableNames[i]);
            lineList1.add(lineResultEntity);
        }
        List<LoadRateEntity>  lineLoadRatioDev1 = loadRateUtil.lineLoadRateDev1(lineList1, ratedCurrents);
        // 更新dev1中线路负载率
        processDao.updateLineLoadRatioDev1(lineLoadRatioDev1);
        // 变电站负载率计算
        Float[] uis = resultListsEntity.getNodeUI();
        // 取库中的变电站的额定容量
        Float[] nodeRatedCapacity = processDao.getNodeRatedCapacityDev1();
        List<LoadRateEntity> nodeLoadRatioDev1 = loadRateUtil.nodeLoadRateDev1(uis,nodeRatedCapacity);
        // 更新库中的变压器负载率
        processDao.updateNodeLoadRatioDev1(nodeLoadRatioDev1);
        /**
         * 查询一次所有的负载率，发送给前端
         */
        ListsResEntity listsResEntity1 = new ListsResEntity();
        List<LoadRateEntity> list1 = dataReadDao.getDev1LoadRateLine();
        List<LoadRateEntity> list2 = dataReadDao.getDev2LoadRateLine();
        List<LoadRateEntity> list3 = dataReadDao.getDev1LoadRateNode();
        List<LoadRateEntity> list4 = dataReadDao.getDev2LoadRateNode();
        list1.addAll(list2);
        list1.addAll(list3);
        list1.addAll(list4);
        if (list1 == null){
            listsResEntity1.setStatus(ResStatus.FAILED);
        }else {
            listsResEntity1.setUser("QZ");
            listsResEntity1.setStatus(ResStatus.SUCCESS);
            listsResEntity1.setLoadRateEntityList(list1);
        }
        webSocketService.sendTextMsg("/response/getLoadRate",listsResEntity1);
    }

    @Override
    public void dev2DataProcess (ResultListsEntity resultListsEntity)  {
        // 取表名
        String[] tableNames = processDao.getTablesName();
        // 执行数据插入，线路数据插入result的01109-011015 +02201，节点数据插入01209-012014
        List<LineResultEntity> lineList = resultListsEntity.getLineResultEntityList();
        List<NodeResultEntity> nodeList = resultListsEntity.getNodeResultEntityList();
        /*
        发送给前端,对lineList做一下拆分，拆分为一条线路一个实体类
        先查线路对应的node_num
         */
        String[] numbers = processDao.getNodeNum();
        for (int i=0; i<lineList.size(); i++){
            // 返回list放入节点的编号，如zh001
            for (int j=0;j<15;j++){
                ListsResEntity listsResEntity = new ListsResEntity();
                listsResEntity.setNodeNum(numbers[120+i*15+j]);
                lineDataProcessor(lineList, i, j, listsResEntity);
            }
        }

        for (int i=0; i<nodeList.size(); i++){
            for(int j=0;j<15;j++){
                // 返回list放入节点的编号，如zh001
                //每次需要传只含有一个点的信息的集合给前端 所以每次循环都需要新建一个集合和一个实体类。
                ListsResEntity listsResEntity = new ListsResEntity();
                listsResEntity.setNodeNum(numbers[i*15+j+335]);
                nodeDataProcessor(nodeList, i, j, listsResEntity);
            }

        }
        for (int i=0; i<14; i++){
            if (i<8){
                // 插入线路的数据,设备一的线路结果数据表从01101-01108，对应names位置0-7
                processDao.insertLineResult(tableNames[i+8],lineList.get(i));
            }else {
                // 插入节点的数据，设备一的节点结果数据表从01202-01208，对应names位置16-22
                processDao.insertNodeResult(tableNames[i+15], nodeList.get(i-8));
            }
        }


        //Dev2中线路负载率的结果
        LoadRateUtil loadRateUtil=new LoadRateUtil();
        List<LineResultEntity> lineList1=new ArrayList<>();
        Float[] lineRatedCapacity = processDao.getLineRatedCurrentDev2();
        //取出Dev2中所有线路的信息
        for (int i=0; i<8;i++){
            LineResultEntity lineResultEntity = processDao.selectLineData(tableNames[i+8]);
            lineList1.add(lineResultEntity);
        }
        List<LoadRateEntity> lineLoadRatioDev2 = loadRateUtil.lineLoadRatioDev2(lineList1,lineRatedCapacity);
        //更新dev2中的线路负载率
        processDao.updateLineLoadRatioDev2(lineLoadRatioDev2);


//        //Dev2中端点负载率的结果
//        Float[] nodeCapacity = resultListsEntity.getNodeCapacity();
//        Float[] nodeRatedCapacity = processDao.getNodeRatedCapacityDev2();
//        List<Float> nodeLoadRatio = loadRateUtil.nodeLoadRatioDev2(nodeCapacity,nodeRatedCapacity);
    }

    /**
     * 节点的数据处理与解析方法，每一个节点的数据分别传给前端
     * @param nodeList
     * @param i
     * @param j
     * @param listsResEntity
     */
    private void nodeDataProcessor(List<NodeResultEntity> nodeList, int i, int j, ListsResEntity listsResEntity) {
        if (nodeList != null){
            listsResEntity.setStatus(ResStatus.SUCCESS);
            List<SsVolResEntity> ssVolResEntityList=new ArrayList<>();
            SsVolResEntity ssVolResEntity=new SsVolResEntity();
            try {
                Field[] fields = NodeResultEntity.class.getDeclaredFields();
                for (Field field:fields){
                    field.setAccessible(true);
                }
                ssVolResEntity.setVoltageA((Float) fields[j*3].get(nodeList.get(i)));
                ssVolResEntity.setVoltageB((Float) fields[j*3+1].get(nodeList.get(i)));
                ssVolResEntity.setVoltageC((Float) fields[j*3+2].get(nodeList.get(i)));
                ssVolResEntityList.add(ssVolResEntity);
                listsResEntity.setSsVolResEntityList(ssVolResEntityList);
                webSocketService.sendTextMsg("/response/nodeResult",listsResEntity);
            }catch (Exception e){
                e.printStackTrace();
            }
            // 以下内容可以改为映射，循环赋值
        }else {
            listsResEntity.setStatus(ResStatus.FAILED);
        }
    }

    /**
     * 线路的数据处理与解析方法，每一个线路的数据分别传给前端
     * @param lineList
     * @param i
     * @param j
     * @param listsResEntity
     */
    private void lineDataProcessor(List<LineResultEntity> lineList, int i, int j, ListsResEntity listsResEntity) {
        if (lineList != null){
            listsResEntity.setStatus(ResStatus.SUCCESS);
            List<LineCtAndPResEntity> lineCtAndPResEntityList = new ArrayList<>();
            LineCtAndPResEntity lineCtAndPResEntity = new LineCtAndPResEntity();
            // 通过反射机制取到LineResultEntity中所有属性的值，并且赋值给lineCtAndPResEntity
            try {
                Field[] fields = LineResultEntity.class.getDeclaredFields();
                for (Field field:fields){
                    field.setAccessible(true);
                }
                lineCtAndPResEntity.setCurrentA((Float) fields[j*5].get(lineList.get(i)));
                lineCtAndPResEntity.setCurrentB((Float) fields[j*5+1].get(lineList.get(i)));
                lineCtAndPResEntity.setCurrentC((Float) fields[j*5+2].get(lineList.get(i)));
                lineCtAndPResEntity.setPowerActive((Float) fields[j*5+3].get(lineList.get(i)));
                lineCtAndPResEntity.setPowerReactive((Float) fields[j+5+4].get(lineList.get(i)));
                lineCtAndPResEntityList.add(lineCtAndPResEntity);
                listsResEntity.setLineCtAndPResEntityList(lineCtAndPResEntityList);
                webSocketService.sendTextMsg("/response/lineResult",listsResEntity);
            }catch (Exception e){
                e.printStackTrace();
            }
        }else {
            listsResEntity.setStatus(ResStatus.FAILED);
        }
    }
}
