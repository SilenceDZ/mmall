package com.mmall.common;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
//保证序列化json的时候，如果是null的对象，key也会消失
public class ServerResponse<T> implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private int status;
	private String msg;
	private T data;
	
	private ServerResponse(int status){
		this.status=status;
	}
	//如果两个方法都是public，第二个参数是String是就会调用第二个方法
	private ServerResponse(int status,T data){
		this.status=status;
		this.data=data;
	}
	
	private ServerResponse(int status,String msg){
		this.status=status;
		this.msg=msg;
	}
	
	private ServerResponse(int status,String msg,T data){
		this.status=status;
		this.data=data;
		this.msg=msg;
	}
	
	@JsonIgnore//不序列化这个方法
	public boolean isSuccess(){
		return this.status==ResponseCode.SUCCESS.getCode();
	}
	
	public int getStatus(){
		return this.status;
	}
	
	public T getData(){
		return this.data;
	}
	
	public static <T> ServerResponse<T> createBySuccess(){
		return new ServerResponse<T>(ResponseCode.SUCCESS.getCode());
	}
	
	public static <T> ServerResponse<T> createBySuccessMessage(String msg){
		return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(),msg);
	}
	
	public static <T> ServerResponse<T> createBySuccess(T data){
		return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(),data);
	}
	//避免了T是String时调用构造器错误的问题
	public static <T> ServerResponse<T> createBySuccess(String msg,T data){
		return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(),msg,data);
	}
	
	public static <T> ServerResponse<T> createByError(){
		return new ServerResponse<T>(ResponseCode.ERROR.getCode(),ResponseCode.ERROR.getDesc());
	}
	
	public static <T> ServerResponse<T> createByErrorMessage(String errorMessage){
		return new ServerResponse<T>(ResponseCode.ERROR.getCode(),errorMessage);
	}
	
	public static <T> ServerResponse<T> createByErrorCodeMessage(int errorCode,String errorMessage){
		return new ServerResponse<T>(errorCode,errorMessage);
	}
}
