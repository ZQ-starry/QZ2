package com.sx.qz2.entity.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class BaseRes {

	// websocket用户名
	protected String user;
	// 节点编号
	protected String nodeNum;
	// 消息状态
	protected ResStatus status;

}
