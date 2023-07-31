package com.sx.qz2.entity.res;

import lombok.Data;

/**
 * @Author: ZhangQi
 * @Date: 2023/7/25 14:35
 * @Description: 负荷情况实体
 */
@Data
public class LoadInfoEntity {
    private Integer id;
    private String year;
    // 网供负荷（兆瓦）
    private float gridLoad;
    // 全社会负荷（兆瓦）
    private float socialLoad;
    // 网供电量（亿千瓦时）
    private float gridElectricity;
    // 全社会电量(亿千瓦时)
    private float socialElectricity;
}
