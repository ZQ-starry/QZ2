package com.sx.qz2.util;
import com.sx.qz2.entity.res.LoadRateEntity;
import com.sx.qz2.entity.result.LineResultEntity;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LoadRateUtil {

    //假设8个表中数据都是满的
    public List<LoadRateEntity>  lineLoadRateDev1(List<LineResultEntity> lineList, Float[] ratedCurrents) {
        //查出全部线路的额定容量
        Float[] lineCurrent = new Float[120];
        Float[] lineLoadRatio = new Float[120];
        List<LoadRateEntity> loadRateList = new ArrayList<>();
        for (int i = 0; i < lineList.size(); i++) {
            LineResultEntity lineResultEntity = lineList.get(i);
            try {
                Field[] fields = LineResultEntity.class.getDeclaredFields();
                for (Field field : fields) {
                    field.setAccessible(true);
                }
                for (int j = 0; j < 15; j++) {
                    lineCurrent[i * 15 + j] = ((Float) fields[j * 5].get(lineResultEntity));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for (int k = 0; k < 120; k++) {
            LoadRateEntity loadRateEntity = new LoadRateEntity();
            loadRateEntity.setId(k+1);
            lineLoadRatio[k] = lineCurrent[k] / ratedCurrents[k];
            loadRateEntity.setLoadRate(lineLoadRatio[k]);
            loadRateList.add(loadRateEntity);
        }
        return loadRateList;
    }

        public List<LoadRateEntity> lineLoadRatioDev2(List<LineResultEntity> lineList, Float[] lineRatedCapacity) {
            //查出全部线路的额定容量
            Float[] lineCurrent = new Float[110];
            Float[] lineLoadRatio = new Float[110];
            List<LoadRateEntity>  loadRateList=new ArrayList<>();
            for (int i = 0; i < 8; i++) {
                LineResultEntity lineResultEntity = lineList.get(i);
                try {
                    Field[] fields = LineResultEntity.class.getDeclaredFields();
                    for (Field field : fields) {
                        field.setAccessible(true);
                    }
                    if (i<7){
                        for (int j = 0; j < 15; j++) {
                            lineCurrent[i * 15 + j] = ((Float) fields[j * 5].get(lineResultEntity));
                        }
                    }
                    else {
                        for (int j = 0; j < 5; j++) {
                            lineCurrent[i * 15 + j] = ((Float) fields[j * 5].get(lineResultEntity));
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            for (int k = 0; k < 110; k++) {
                lineLoadRatio[k] = lineCurrent[k] / lineRatedCapacity[k];
                LoadRateEntity loadRateEntity=new LoadRateEntity();
                loadRateEntity.setId(k+1);
                loadRateEntity.setLoadRate(lineLoadRatio[k]);
                loadRateList.add(loadRateEntity);
            }
            return loadRateList;
        }



    public List<LoadRateEntity>  nodeLoadRateDev1(Float[] uis, Float[] nodeRatedCapacity) {
        Float[] nodeLoadRatio = new Float[82];
        List<LoadRateEntity>  loadRateList=new ArrayList<>();
        for (int i = 0; i <82; i++) {
            nodeLoadRatio[i] = uis[i] / nodeRatedCapacity[i];
            LoadRateEntity loadRateEntity=new LoadRateEntity();
            loadRateEntity.setId(i+1);
            loadRateEntity.setLoadRate(nodeLoadRatio[i]);
            loadRateList.add(loadRateEntity);
        }
      return  loadRateList;
    }




            public List<Float> nodeLoadRatioDev2(Float[] nodeCapacity, Float[] nodeRatedCapacity) {
                Float[] nodeLoadRatio = new Float[41];
                for (int i=0;i<42;i++){
                    nodeLoadRatio[i]=nodeCapacity[i]/nodeRatedCapacity[i];
                }
                return Arrays.asList(nodeLoadRatio);
         }

}
