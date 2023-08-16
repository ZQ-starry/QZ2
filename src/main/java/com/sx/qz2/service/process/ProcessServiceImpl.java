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
import com.sx.qz2.service.read.DataReadServiceImpl;
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

    private DateUtils dateUtils = new DateUtils();
    // 用于计算时间轴
    private static int countDev1 = 0;

    private static int countDev2 = 0;
    // 设备1的线路数量
    private final static int Dev1LineNum = 102;
    // 设备1的节点数量
    private final static int Dev1NodeNum = 89;
    // 设备2的线路数量
    private final static int Dev2LineNum = 128;
    // 设备2的节点数量
    private final static int Dev2NodeNum = 100;
    // 设备1的线路结果表个数
    private final static int Dev1LineTableNum = 7;
    // 设备1的节点结果表个数
    private final static int Dev1NodeTableNum = 6;
    // 设备2的线路表个数
    private final static int Dev2LineTableNum = 9;
    // 设备2的节点结果表个数
    private final static int Dev2NodeTableNum = 7;
    // 所有表的表名
    private static String[] tableNames = new String[Dev1LineTableNum+Dev1NodeTableNum+Dev2LineTableNum+Dev2NodeTableNum];
    //设备1中线路编号的数组
    private static String[] Dev1LineNums = new String[Dev1LineNum];
    //设备1中节点编号的数组
    private static String[] Dev1NodeNums = new String[Dev1NodeNum];
    //设备2中线路编号的数组
    private static String[] Dev2LineNums = new String[Dev2LineNum];
    //设备2中节点编号的数组
    private static String[] Dev2NodeNums = new String[Dev2NodeNum];

    @Autowired
    private ProcessDao processDao;

    @Autowired
    private WebSocketService webSocketService;

    @Autowired
    private DataReadDao dataReadDao;

    @Autowired
    private DataReadServiceImpl dataReadServiceImpl;
    /**
     * 项目开始后执行一次
     */
    @Override
    public void getTablesName() {
        // 取结果要保存的表名
        tableNames = processDao.getTablesName();
        // 取线路与节点的编号，如zh001，zh002
        String[] nodeNums = processDao.getNodeNum();
        // 编号的分类
        for (int i=0; i<nodeNums.length; i++){
            // 设备1线路编号
            if (i<Dev1LineNum+Dev1NodeNum+Dev2LineNum){
                if (i<Dev1LineNum+Dev1NodeNum){
                    if (i<Dev1LineNum){
                        Dev1LineNums[i] = nodeNums[i];
                    }else {
                        Dev1NodeNums[i-Dev1LineNum] = nodeNums[i];
                    }
                }else {
                    Dev2LineNums[i-Dev1LineNum+Dev1NodeNum] = nodeNums[i];
                }
            }else {
                Dev2NodeNums[i-Dev1LineNum+Dev1NodeNum+Dev2LineNum] = nodeNums[i];
            }
        }
        // 截断所有结果表
//        for (String tableName:tableNames){
//            processDao.truncateTable(tableName);
//        }
        System.out.println(Arrays.toString(tableNames));
    }

    @Override
    @Transactional
    public void dev1DataProcess (ResultListsEntity resultListsEntity)  {
        // 时间轴,获取时间并入库
        String[] times = new String[1];
        times[0] = dateUtils.timelineUtil(countDev1+1);
        countDev1++;
        processDao.insertTime(times[0]);

        List<LineResultEntity> lineList = resultListsEntity.getLineResultEntityList();
        List<NodeResultEntity> nodeList = resultListsEntity.getNodeResultEntityList();
        /*
        发送给前端,对lineList和nodeList 做一下拆分，拆分为一条线路一个实体类
         */
        // 处理设备1的线路电流与功率数据
        for (int i=0; i<Dev1LineTableNum; i++){
            // 返回list放入线路的编号，如zh001
            if (i != Dev1LineTableNum-1){
                for (int j=0; j<15; j++){
                    ListsResEntity listsResEntity = new ListsResEntity();
                    listsResEntity.setNodeNum(Dev1LineNums[i*15+j]);
                    listsResEntity.setTimes(times);
                    lineDataProcessor(lineList, i, j, listsResEntity);
                }
            }else {
                // 最后一个表数据不满
                int end = Dev1LineNum%15;
                for (int j=0; j<end; j++){
                    ListsResEntity listsResEntity = new ListsResEntity();
                    listsResEntity.setNodeNum(Dev1LineNums[i*15+j]);
                    listsResEntity.setTimes(times);
                    lineDataProcessor(lineList, i, j, listsResEntity);
                }
            }

        }
        // 处理设备1的节点电压数据
        for (int i=0; i<Dev1NodeTableNum; i++){
            if (i != Dev1NodeTableNum-1){
                for(int j=0; j<15; j++){
                    // 返回list放入节点的编号，如zh001
                    ListsResEntity listsResEntity = new ListsResEntity();
                    // 编号位置从设备1的线路编号最后一个的位置开始
                    listsResEntity.setNodeNum(Dev1NodeNums[i*15+j]);
                    listsResEntity.setTimes(times);
                    nodeDataProcessor(nodeList, i, j, listsResEntity);
                }
            }else {
                int end = Dev1NodeNum%15;
                for(int j=0; j<end; j++){
                    // 返回list放入节点的编号，如zh001
                    ListsResEntity listsResEntity = new ListsResEntity();
                    // 编号位置从设备1的线路编号最后一个的位置开始
                    listsResEntity.setNodeNum(Dev1NodeNums[i*15+j]);
                    listsResEntity.setTimes(times);
                    nodeDataProcessor(nodeList, i, j, listsResEntity);
                }
            }
        }
        /**
         * 执行数据插入，线路数据插入result的line011-line017，节点数据插入node011-016
         */
        for (int i=0; i<Dev1LineTableNum+Dev1NodeTableNum; i++){
            if (i < Dev1LineTableNum){
                // 插入线路的数据,对应tableNames位置0-6
                processDao.insertLineResult(tableNames[i],lineList.get(i));
            }else {
                // 插入节点的数据，对应tableNames位置7-12
                processDao.insertNodeResult(tableNames[i], nodeList.get(i-Dev1LineTableNum));
            }
        }
        /*
           线路负载率=单相电流/额定电流
         */
        LoadRateUtil loadRateUtil = new LoadRateUtil();
        List<LineResultEntity> lineList1 = new ArrayList<>();
        // 取库中各线路的额定电流
        Float[] ratedCurrents = processDao.getLineRatedCurrentDev1();
        for (int i=0; i<Dev1LineTableNum; i++){
            // 从库中取出设备1的最新的一次线路的三相电流、有功无功等数据
            LineResultEntity lineResultEntity = processDao.selectLineData(tableNames[i]);
            lineList1.add(lineResultEntity);
        }
        // 执行线路负载率的计算,把节点的编号一起传过去处理
        List<LoadRateEntity>  lineLoadRatioDev1 = loadRateUtil.lineLoadRate(Dev1LineNums,lineList1, ratedCurrents);
        // 更新dev1中线路负载率
        processDao.updateLineLoadRatioDev1(lineLoadRatioDev1);
        /*
        变电站负载率=UI/额定容量
         */
        // 取读取数据中的UI值
        Float[] uis = resultListsEntity.getNodeUI();
        // 取库中的变电站的额定容量
        Float[] nodeRatedCapacity = processDao.getNodeRatedCapacityDev1();
        List<LoadRateEntity> nodeLoadRatioDev1 = loadRateUtil.nodeLoadRate(Dev1NodeNums,uis,nodeRatedCapacity);
        // 更新库中的变电站负载率
        processDao.updateNodeLoadRatioDev1(nodeLoadRatioDev1);
        /**
         * 查询一次所有的负载率，发送给前端
         */
        ListsResEntity listsResEntity1 = dataReadServiceImpl.loadRateRead();
        webSocketService.sendTextMsg("/response/getLoadRate",listsResEntity1);
    }

    @Override
    public void dev2DataProcess (ResultListsEntity resultListsEntity)  {
        // 时间轴,获取时间并插入表
        String[] times = new String[1];
        times[0] = dateUtils.timelineUtil(countDev2+1);
        countDev2++;
        // 执行数据插入，线路数据插入result的line021-line029，节点数据插入node021-027
        List<LineResultEntity> lineList = resultListsEntity.getLineResultEntityList();
        List<NodeResultEntity> nodeList = resultListsEntity.getNodeResultEntityList();
        /*
        发送给前端,对lineList做一下拆分，拆分为一条线路一个实体类
         */
        for (int i=0; i<Dev2LineTableNum; i++){
            if (i != Dev2LineTableNum-1){
                for (int j=0;j<15;j++){
                    ListsResEntity listsResEntity = new ListsResEntity();
                    // 编号从设备1节点的最后一个编号位置接续
                    listsResEntity.setNodeNum(Dev2LineNums[i*15+j]);
                    listsResEntity.setTimes(times);
                    lineDataProcessor(lineList, i, j, listsResEntity);
                }
            }else {
                int end = Dev2LineNum%15;
                for (int j=0;j<end;j++){
                    ListsResEntity listsResEntity = new ListsResEntity();
                    // 编号从设备1节点的最后一个编号位置接续
                    listsResEntity.setNodeNum(Dev2LineNums[i*15+j]);
                    listsResEntity.setTimes(times);
                    lineDataProcessor(lineList, i, j, listsResEntity);
                }
            }
        }

        for (int i=0; i<Dev2NodeTableNum; i++){
            if (i != Dev2NodeTableNum-1){
                for(int j=0;j<15;j++){
                    ListsResEntity listsResEntity = new ListsResEntity();
                    listsResEntity.setNodeNum(Dev2NodeNums[i*15+j]);
                    listsResEntity.setTimes(times);
                    nodeDataProcessor(nodeList, i, j, listsResEntity);
                }
            }else {
                int end = Dev2NodeNum%15;
                for(int j=0;j<end;j++){
                    ListsResEntity listsResEntity = new ListsResEntity();
                    listsResEntity.setNodeNum(Dev2NodeNums[i*15+j]);
                    listsResEntity.setTimes(times);
                    nodeDataProcessor(nodeList, i, j, listsResEntity);
                }
            }

        }
        // 数据入库中的结果表
        for (int i=0; i < Dev2LineTableNum+Dev2NodeTableNum; i++){
            if (i < Dev2LineTableNum){
                // 插入线路的数据,对应tableNames位置i+13
                processDao.insertLineResult(tableNames[i+Dev1LineTableNum+Dev1NodeTableNum], lineList.get(i));
            }else {
                // 插入节点的数据，对应tableNames位置i+13
                processDao.insertNodeResult(tableNames[i+Dev1LineTableNum+Dev1NodeTableNum], nodeList.get(i-Dev2LineTableNum));
            }
        }
        /*
           线路负载率=单相电流/额定电流
         */
        LoadRateUtil loadRateUtil = new LoadRateUtil();
        List<LineResultEntity> lineList2 = new ArrayList<>();
        Float[] lineRatedCapacity = processDao.getLineRatedCurrentDev2();
        // 取库中各线路的额定电流
        for (int i=0; i<Dev2LineTableNum; i++){
            // 从库中取出设备2的最新的一次线路的三相电流、有功无功等数据
            LineResultEntity lineResultEntity = processDao.selectLineData(tableNames[i+Dev1LineTableNum+Dev1NodeTableNum]);
            lineList2.add(lineResultEntity);
        }
        // 执行线路负载率的计算
        List<LoadRateEntity> lineLoadRatioDev2 = loadRateUtil.lineLoadRate(Dev2LineNums,lineList2,lineRatedCapacity);
        // 更新dev2中的线路负载率
        processDao.updateLineLoadRatioDev2(lineLoadRatioDev2);
        /*
        变电站负载率=UI/额定容量
         */
        // 取读取数据中的UI值
        Float[] uis = resultListsEntity.getNodeUI();
        // 取库中的变电站的额定容量
        Float[] nodeRatedCapacity = processDao.getNodeRatedCapacityDev2();
        List<LoadRateEntity> nodeLoadRatioDev1 = loadRateUtil.nodeLoadRate(Dev2NodeNums,uis,nodeRatedCapacity);
        // 更新库中的变电站负载率
        processDao.updateNodeLoadRatioDev1(nodeLoadRatioDev1);
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
