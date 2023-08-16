package com.sx.qz2.util;
import com.sx.qz2.entity.res.LoadRateEntity;
import com.sx.qz2.entity.result.LineResultEntity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class LoadRateUtil {

    /**
     * 设备1中线路负载率计算方法
     * @param lineList 包含设备所有线路的最新一条数据，索引值对应表的个数，一个实体对应一个表的数据
     * @param ratedCurrents 设备中的线路的额定电流数组
     * @return
     */
    public List<LoadRateEntity>  lineLoadRate(String[] nodeNums, List<LineResultEntity> lineList, Float[] ratedCurrents) {
        Float[] lineCurrent = new Float[ratedCurrents.length];
        List<LoadRateEntity> loadRateList = new ArrayList<>();
        for (int i = 0; i < lineList.size(); i++) {
            LineResultEntity lineResultEntity = lineList.get(i);
            try {
                Field[] fields = LineResultEntity.class.getDeclaredFields();
                for (Field field : fields) {
                    field.setAccessible(true);
                }
                for (int j = 0; j < 15; j++) {
                    // 取A相电流有效值进行计算
                    lineCurrent[i * 15 + j] = ((Float) fields[j * 5].get(lineResultEntity));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for (int i=0; i<nodeNums.length; i++) {
            LoadRateEntity loadRateEntity = new LoadRateEntity();
            loadRateEntity.setId(i+1);
            // 放入节点编号
            loadRateEntity.setNodeNum(nodeNums[i]);
            loadRateEntity.setLoadRate(lineCurrent[i]/ratedCurrents[i]);
            loadRateList.add(loadRateEntity);
        }
        return loadRateList;
    }

    /**
     * 设备1节点的负载率计算方法
     * @param uis
     * @param nodeRatedCapacity
     * @return
     */
    public List<LoadRateEntity>  nodeLoadRate(String[] nodeNums,Float[] uis, Float[] nodeRatedCapacity) {
        List<LoadRateEntity>  loadRateList=new ArrayList<>();
        for (int i=0; i<uis.length; i++) {
            LoadRateEntity loadRateEntity = new LoadRateEntity();
            loadRateEntity.setId(i+1);
            loadRateEntity.setNodeNum(nodeNums[i]);
            loadRateEntity.setLoadRate(uis[i]/nodeRatedCapacity[i]);
            loadRateList.add(loadRateEntity);
        }
      return  loadRateList;
    }

}
