package com.sx.qz2.entity.data;

import lombok.Data;

/**
 * @Author: ZhangQi
 * @Date: 2023/7/13 12:27
 * @Description:
 */
@Data
public class NodeDataEntity {
    private  Integer id;
    private float voltage1A;
    private float voltage1B;
    private float voltage1C;
    private float current1A;
    private float current1B;
    private float current1C;
}
