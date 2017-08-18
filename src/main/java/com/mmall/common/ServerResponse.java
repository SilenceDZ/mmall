package com.mmall.common;

import java.io.Serializable;

public class ServerResponse<T> implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private int status;
	private String msg;
	private T data;
	
}
