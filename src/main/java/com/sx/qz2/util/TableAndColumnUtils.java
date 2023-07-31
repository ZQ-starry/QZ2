package com.sx.qz2.util;


import com.sx.qz2.entity.res.NodeInfoEntity;

/**
 * @Author: ZhangQi
 * @Date: 2023/7/24 10:54
 * @Description: 数据的字段名设定
 */
public class TableAndColumnUtils {
    public NodeInfoEntity columnNames(NodeInfoEntity nodeInfo){
        String number = nodeInfo.getNodeColumnNum();
        String variable = nodeInfo.getNodeVariable();
        if (variable.equals("线路")){
            nodeInfo.setNodeColumnName1("current" + number + "_a");
            nodeInfo.setNodeColumnName2("current" + number + "_b");
            nodeInfo.setNodeColumnName3("current" + number + "_c");
            nodeInfo.setNodeColumnName4("power_active" + number);
            nodeInfo.setNodeColumnName5("power_reactive" + number);
        }else {
            nodeInfo.setNodeColumnName1("voltage"+ number + "_a");
            nodeInfo.setNodeColumnName2("voltage"+ number + "_b");
            nodeInfo.setNodeColumnName3("voltage"+ number + "_c");
        }
        return nodeInfo;
    }


    /**
     * voltage1A
     * current1A
     * @param key
     * @return
     */
    public int keyNames(String key){
        // 拆分key首字母
        int returnValue = 0;
        int number = key.charAt(7);
        if (key.charAt(0) == 'v'){
            // 说明是电压数据,判断其数字
            if (key.charAt(7)== 'A'){
                returnValue = returnValue + ((number - 1) * 6);
            }else if (key.charAt(7)== 'B'){
                returnValue = returnValue + ((number - 1) * 6) + 1;
            }else {
                returnValue = returnValue + ((number - 1) * 6) + 2;
            }
        } else {
            // 说明是电流数据，判断其数字
            if (key.charAt(7)== 'A'){
                returnValue = returnValue + ((number - 1) * 6) + 3;
            }else if (key.charAt(7)== 'B'){
                returnValue = returnValue + ((number - 1) * 6) + 4;
            }else {
                returnValue = returnValue + ((number - 1) * 6) + 5;
            }
        }
        return returnValue;
    }
}
