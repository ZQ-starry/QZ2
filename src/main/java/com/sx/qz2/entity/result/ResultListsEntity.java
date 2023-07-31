package com.sx.qz2.entity.result;

import lombok.Data;

import java.util.List;

/**
 * @Author: ZhangQi
 * @Date: 2023/7/31 10:28
 * @Description:
 */
@Data
public class ResultListsEntity {
    private List<LineResultEntity> lineResultEntityList;
    private List<NodeResultEntity> nodeResultEntityList;
}
