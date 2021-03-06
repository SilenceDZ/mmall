package com.mmall.common;

import java.util.Set;

import com.google.common.collect.Sets;

public class Const {
	public static final String CURRENT_USER="currentUser";
	
	public static final String EMAIL="email";
	
	public static final String USERNAME="username";
	
	public interface Role{
		/**
		 * @Fields ROLE_CUSTOMER:普通用户
		 */
		int ROLE_CUSTOMER = 0;
		/**
		 * @Fields ROLE_ADMIN:管理员
		 */
		int ROLE_ADMIN =1;
	}
	public enum ProductStatusEnum{
		ON_SALE(1,"在线");
		private String value;
		private int code;
		ProductStatusEnum(int code,String value){
			this.code=code;
			this.value=value;
		}
		public String getValue(){
			return value;
		}
		public int getCode(){
			return code;
		}
	}
	public interface Cart{
		int CHECKED=1;//购物车选中状态
		int UN_CHECKED=0;//购物车未选中
		//限制购买数量
		String LIMIT_NUM_FAIL="LIMIT_NUM_FAIL";
		String LIMIT_NUM_SUCCESS="LIMIT_NUM_SUCCESS";
	}
	public interface ProductListOrderBy{
		Set<String> PRICE_ASC_DES=Sets.newHashSet("price_desc","price_asc");
	}
	
	public enum OrderStatusEnum{
		CANCELED(0,"已取消"),
		NO_PAY(10,"未支付"),
		PAID(20,"已付款"),
		SHIPPED(40,"已发货"),
		ORDER_SUCCESS(50,"订单完成"),
		ORDER_CLOSE(60,"订单关闭");
		OrderStatusEnum(int code,String value){
			this.code=code;
			this.value=value;
		}
		private String value;
		private int code;
		public String getValue(){
			return value;
		}
		public int getCode(){
			return code;
		}
	}
	public interface AlipayCallback{
		String TRADE_STATUS_WAIT_BUYER_PAY="WAIT_BUYER_PAY";
		String TRADE_STATUS_TRADE_SUCCESS="TRADE_SUCCESS";
		
		String RESPONSE_SUCCESS="success";
		String RESPONSE_FALIED="failed";
	}
	
	public enum PayPlatformEnum{
		ALIPAY(1,"支付宝");
		PayPlatformEnum(int code,String value){
			this.code=code;
			this.value=value;
		}
		private String value;
		private int code;
		public String getValue(){
			return value;
		}
		public int getCode(){
			return code;
		}
	}
	
}
