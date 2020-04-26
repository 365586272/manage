package com.cy.pj.sys.vo;

import java.io.Serializable;
import java.util.Date;

import com.cy.pj.sys.entity.SysDept;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class SysUserDeptVo implements Serializable {

	private static final long serialVersionUID = -4970626414864210089L;
	private Integer id;
	private String username;
	private String password;//md5
	private String salt;
	private String email;
	private String mobile;
	private Integer valid;
	private Date createdTime;
	private Date modifiedTime;
	private String createdUser;
	private String modifiedUser;
	private SysDept sysDept; //private Integer deptId;
}
