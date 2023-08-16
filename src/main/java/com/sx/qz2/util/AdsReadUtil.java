package com.sx.qz2.util;

import com.sx.qz2.entity.result.LineResultEntity;
import com.sx.qz2.entity.result.NodeResultEntity;
import com.sx.qz2.entity.result.ResultListsEntity;
import de.beckhoff.jni.Convert;
import de.beckhoff.jni.JNIByteBuffer;
import de.beckhoff.jni.tcads.AdsCallDllFunction;
import de.beckhoff.jni.tcads.AmsAddr;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: ZhangQi
 * @Date: 2023/7/31 9:45
 * @Description:
 */
public class AdsReadUtil {

    private static final String NetId1 = "169.254.118.233.1.1"; // 设备1 NetId

    private static final String NetId2 = "192.168.1.50.1.1"; // 设备2 NetId

    /**
     * 8.14拆分版本：设备1含25个T节点，变电站39，电厂22，牵引站3，线路102
     * @return
     */
    public ResultListsEntity dev1StructRead() {
        ResultListsEntity resultListsEntity = new ResultListsEntity();
        List<LineResultEntity> lineList = new ArrayList<>();
        List<NodeResultEntity> nodeList = new ArrayList<>();
        //用来存40个变电站的UI值
        Float[] nodeUIFloats = new Float[39];
        //该长度代表8张线路表 7张节点表 以及82个节点(实际容量) 需要的数据长度
        int length = (7*75+6*45+40)*4;
        long err;
        AmsAddr addr = new AmsAddr();
        try {
            AdsCallDllFunction.adsPortOpen();
            err = AdsCallDllFunction.getLocalAddress(addr);
            addr.setNetIdStringEx(NetId1);
            addr.setPort(851);
            if (err != 0) {
                System.out.println("Read Error PLC1: 0x"
                        + Long.toHexString(err));
            } else {
                System.out.println("Read Success: Open PLC1 communication!");
            }
            JNIByteBuffer hdlBuff = new JNIByteBuffer(4);
            JNIByteBuffer symBuff = new JNIByteBuffer(Convert.StringToByteArr("MAIN.PLCVar", false));
            JNIByteBuffer dataBuff = new JNIByteBuffer(length);
            err = AdsCallDllFunction.adsSyncReadWriteReq(
                    addr,
                    0xF003,//指令代码号，用于获取句柄
                    0x0,//指令号，固定
                    hdlBuff.getUsedBytesCount(),//取得句柄缓存长度
                    hdlBuff,
                    symBuff.getUsedBytesCount(),//取得变量名缓存长度
                    symBuff);
            if (err != 0) {
                System.out.println("Error: Get handle: 0x"
                        + Long.toHexString(err));
            }
            int hdlBuffToInt = Convert.ByteArrToInt(hdlBuff.getByteArray());
            Long dataLength = (long) length;
            err = AdsCallDllFunction.adsSyncReadReq(
                    addr,
                    0xF005,//指令代码号，用于获取数据
                    hdlBuffToInt,
                    dataLength,//数据长度
                    dataBuff);
            if (err != 0) {
                System.out.println("Error: Read by handle: 0x"
                        + Long.toHexString(err));
            } else {
                Field[] lines = LineResultEntity.class.getDeclaredFields();
                for (Field field : lines) {
                    field.setAccessible(true);
                }
                Field[] nodes = NodeResultEntity.class.getDeclaredFields();
                for (Field field : nodes) {
                    field.setAccessible(true);
                }
                ByteBuffer responseBuffer = ByteBuffer.wrap(dataBuff.getByteArray());
                responseBuffer.order(ByteOrder.LITTLE_ENDIAN);
                // dataBuff为读取到的总的结构体
                for (int i = 0; i < 14; i++) {
                    LineResultEntity lineResultEntity = new LineResultEntity();
                    NodeResultEntity nodeResultEntity = new NodeResultEntity();
                    if (i < 7) {
                        // 处理线路数据
                        for (int j = 0; j < 75; j++) {
                            lines[j].set(lineResultEntity, responseBuffer.getFloat(i*75*4 + j*4));
                        }
                        lineList.add(lineResultEntity);
                    } else if(i==13){
                        // 处理 UI数据
                        for (int j=0;j<40;j++){
                            nodeUIFloats[j]=responseBuffer.getFloat(3180+j*4); // 3180=(7*75+6*45)*4
                        }
                    }
                    else {
                        // 处理节点数据
                        for (int j = 0; j < 45; j++) {
                            nodes[j].set(nodeResultEntity, responseBuffer.getFloat(i*300+(i-7)*45*4 + j*4));
                        }
                        nodeList.add(nodeResultEntity);
                    }
                }

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            AdsCallDllFunction.adsPortClose();
        }
        resultListsEntity.setLineResultEntityList(lineList);
        resultListsEntity.setNodeResultEntityList(nodeList);
        resultListsEntity.setNodeUI(nodeUIFloats);
        return resultListsEntity;
    }


    /**、
     * 8.14拆分版本：设备2含T节点31，变电站43，电厂22，牵引站3，线路128，光伏电站1
     * @return
     */
    public ResultListsEntity dev2StructRead() {
        ResultListsEntity resultListsEntity = new ResultListsEntity();
        List<LineResultEntity> lineList = new ArrayList<>();
        List<NodeResultEntity> nodeList = new ArrayList<>();
        //用来存41个节点的实际容量
        Float[] nodeUIFloats = new Float[43];
        int length = (9*75+7*45+43)*4;
        long err;
        AmsAddr addr = new AmsAddr();
        try {
            AdsCallDllFunction.adsPortOpen();
            err = AdsCallDllFunction.getLocalAddress(addr);
            addr.setNetIdStringEx(NetId1);
            addr.setPort(851);
            if (err != 0) {
                System.out.println("Read Error PLC1: 0x"
                        + Long.toHexString(err));
            } else {
                System.out.println("Read Success: Open PLC1 communication!");
            }
            JNIByteBuffer hdlBuff = new JNIByteBuffer(4);
            JNIByteBuffer symBuff = new JNIByteBuffer(Convert.StringToByteArr("MAIN.PLCVar", false));
            JNIByteBuffer dataBuff = new JNIByteBuffer(length);
            err = AdsCallDllFunction.adsSyncReadWriteReq(
                    addr,
                    0xF003,//指令代码号，用于获取句柄
                    0x0,//指令号，固定
                    hdlBuff.getUsedBytesCount(),//取得句柄缓存长度
                    hdlBuff,
                    symBuff.getUsedBytesCount(),//取得变量名缓存长度
                    symBuff);
            if (err != 0) {
                System.out.println("Error: Get handle: 0x"
                        + Long.toHexString(err));
            }
            int hdlBuffToInt = Convert.ByteArrToInt(hdlBuff.getByteArray());
            Long dataLength = (long) length;
            err = AdsCallDllFunction.adsSyncReadReq(
                    addr,
                    0xF005,//指令代码号，用于获取数据
                    hdlBuffToInt,
                    dataLength,//数据长度
                    dataBuff);
            if (err != 0) {
                System.out.println("Error: Read by handle: 0x"
                        + Long.toHexString(err));
            } else {
                Field[] lines = LineResultEntity.class.getDeclaredFields();
                for (Field field : lines) {
                    field.setAccessible(true);
                }
                Field[] nodes = NodeResultEntity.class.getDeclaredFields();
                for (Field field : nodes) {
                    field.setAccessible(true);
                }
                ByteBuffer responseBuffer = ByteBuffer.wrap(dataBuff.getByteArray());
                responseBuffer.order(ByteOrder.LITTLE_ENDIAN);
                // dataBuff为读取到的总的结构体
                for (int i = 0; i < 17; i++) {
                    LineResultEntity lineResultEntity = new LineResultEntity();
                    NodeResultEntity nodeResultEntity = new NodeResultEntity();
                    if (i < 9) {
                        // 处理线路数据
                        for (int j = 0; j < 75; j++) {
                            lines[j].set(lineResultEntity, responseBuffer.getFloat(i*75*4 + j*4));
                        }
                        lineList.add(lineResultEntity);
                    } else if(i==16){
                        // 处理 UI数据
                        for (int j=0;j<43;j++){
                            nodeUIFloats[j]=responseBuffer.getFloat(3960+j*4); // 3960=(9*75+7*45)*4
                        }
                    } else {
                        // 处理节点数据
                        for (int j = 0; j < 45; j++) {
                            nodes[j].set(nodeResultEntity, responseBuffer.getFloat(i*300+(i-9)*45*4 + j*4));
                        }
                        nodeList.add(nodeResultEntity);
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            AdsCallDllFunction.adsPortClose();
        }
        resultListsEntity.setLineResultEntityList(lineList);
        resultListsEntity.setNodeResultEntityList(nodeList);
        return resultListsEntity;
    }
}
