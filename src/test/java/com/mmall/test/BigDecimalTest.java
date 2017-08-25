package com.mmall.test;

import java.math.BigDecimal;

import org.junit.Test;

public class BigDecimalTest {
	@Test
	public void test(){
		System.out.println(0.05+0.01);//0.060000000000000005
		System.out.println(1.0-0.42);//0.5800000000000001
		System.out.println(4.015*100);//401.49999999999994
		System.out.println(123.3/100);//1.2329999999999999
	}
	@Test
	public void test2(){
		BigDecimal b1=new BigDecimal(0.05);
		BigDecimal b2=new BigDecimal(0.01);
		//0.06000000000000000298372437868010820238851010799407958984375
		System.out.println(b1.add(b2));
	}
	
	@Test
	public void test3(){
		BigDecimal b1=new BigDecimal("0.05");
		BigDecimal b2=new BigDecimal("0.01");
		//0.06
		//商业计算一定要使用它的String构造器
		System.out.println(b1.add(b2));
	}
}
