package com.cy.pj.common.vo;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
/**
 * 在控制层封装数据的一个对象
 * */
@Setter
@Getter
public class JsonResult implements Serializable{
	private static final long serialVersionUID = 7081912909210011540L;
	//状态码
	private int state=1;//1表示success 0表示error
	//状态信息
	private String message="ok";
	/**借助此属性封装业务信息*/
	private Object data;
	//private JsonResult() {}
	public JsonResult() {}
	public JsonResult(String message){
		this.message=message;
	}
	/**一般查询时调用，封装查询结果*/
	public JsonResult(Object data) {
		this.data=data;
	}
	/**出现异常时时调用*/
	public JsonResult(Throwable t){
		this.state=0;
		this.message=t.getMessage();
	}
}
