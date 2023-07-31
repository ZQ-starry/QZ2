package com.sx.qz2.entity.res;

import lombok.Data;

/**
 * @Author: ZhangQi
 * @Date: 2023/7/25 14:01
 * @Description: 装机容量实体
 */
@Data
public class InstalledCapEntity{
    private Integer id;
    private String year;
    private String classify;
    private String category;
    private float capacity;
    private Integer station_number;
}
