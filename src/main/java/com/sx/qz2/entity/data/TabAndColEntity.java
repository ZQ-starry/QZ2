package com.sx.qz2.entity.data;

import lombok.Data;

/**
 * @Author: ZhangQi
 * @Date: 2023/7/24 11:04
 * @Description:
 */
@Data
public class TabAndColEntity {
    // 节点计算结果所在表的表名
    protected String nodeTableName;
    // 节点计算结果在表中的字段编号
    protected String nodeColumnNum;
    // 节点计算结果在表中的字段名
    protected String nodeColumnName1;
    // 节点计算结果在表中的字段名
    protected String nodeColumnName2;
    // 节点计算结果在表中的字段名
    protected String nodeColumnName3;
    // 节点计算结果在表中的字段名
    protected String nodeColumnName4;
    // 节点计算结果在表中的字段名
    protected String nodeColumnName5;
    protected Integer orderNum;
}
