package com.sx.qz2.entity.res;

import lombok.Data;

/**
 * @Author: ZhangQi
 * @Date: 2023/7/25 16:46
 * @Description: 负载率查询表
 */
@Data
public class LoadRateEntity {
    private Integer id;
    private String nodeNum;
    private float loadRate;
}
