package com.cy.pj.common.vo;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * vo:基于此对象在业务层封装数据,进行分页计算
 * 
 * */

@Data
@NoArgsConstructor
public class PageObject<T> implements Serializable {
	private static final long serialVersionUID = 1284652098784613492L;
	private Integer pageCurrent;//当前页的页码
	private Integer pageSize;//页面大小
	private Integer rowCount;//总记录数(查询获得)
	private Integer pageCount;//总页数(计算得到)
	private List<T> records;//当前页记录
	public PageObject(Integer pageCurrent, Integer pageSize, Integer rowCount, List<T> records) {
		super();
		this.pageCurrent = pageCurrent;
		this.pageSize = pageSize;
		this.rowCount = rowCount;
		this.records = records;
		this.pageCount=(rowCount-1)/pageSize+1;
	}
	
}
