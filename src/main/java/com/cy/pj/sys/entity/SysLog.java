package com.cy.pj.sys.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
/**
 * 1)po持久化对象(特点:与标志段由一一映射关系)
 * 2)vo普通值对象(用户进行值得封装和传递,可以不予表中字段有一一对应)
 * 在java所哟有存储数据的对象实现Serializable接口,并手动填加一个序列化id
 * */

@Data
public class SysLog implements Serializable {
	private static final long serialVersionUID = 8924387722922123121L;
	private Integer id;
	//用户名
	private String username;
	//用户操作
	private String operation;
	//请求方法
	private String method;
	//请求参数
	private String params;
	//执行时长(毫秒)
	private Long time;
	//IP地址
	private String ip;
	//创建时间
	private Date createdTime;
}
