package com.mmall.controller.portal;

import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.demo.trade.config.Configs;
import com.google.common.collect.Maps;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IOrderService;
import com.mmall.util.PropertiesUtil;

@Controller
@RequestMapping("/order/")
public class OrderController {
	
	private static Logger logger=LoggerFactory.getLogger(OrderController.class);
	@Autowired
	private IOrderService iOrderService;
	
	/**
	 *Title:pay
	 *Description:前端付款请求
	 *@param session
	 *@param orderNo 订单号
	 *@param request
	 *@return
	 *Throws
	 */
	@RequestMapping("pay.action")
	@ResponseBody
	public ServerResponse pay(HttpSession session,Long orderNo,HttpServletRequest request){
		User user =(User)session.getAttribute(Const.CURRENT_USER);
		if(user==null){
			return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),
					ResponseCode.NEED_LOGIN.getDesc());
		}
		String path=request.getSession().getServletContext().getRealPath("upload");
		return iOrderService.pay(user.getId(), orderNo, path);
	}
	
	/**
	 *Title:alipayCallback
	 *Description:支付宝回调函数
	 *@param request
	 *@return
	 *Throws
	 */
	@RequestMapping("alipay_callback.action")
	@ResponseBody
	public Object alipayCallback(HttpServletRequest request){
		Map<String, String[]> requestParams = request.getParameterMap();
		Map<String ,String> params=Maps.newHashMap();
		for(Iterator iter=requestParams.keySet().iterator();iter.hasNext();){
			String name=(String) iter.next();
			String[] values=requestParams.get(name);
			String valueStr="";
			for(int i=0;i<values.length;i++){
				valueStr=(i==values.length-1)?valueStr+values[i]:valueStr+values[i]+",";
			}
			params.put(name, valueStr);
		}
		logger.info("支付包回调,sing:{},trade_status:{},参数:{}"
				,params.get("sign")
				,params.get("trade_status")
				,params.toString());
		//非常重要，验证回调的正确性，是不是支付宝发的，并且还要避免重复通知
//		AlipaySignature
		//验证要求去掉signhe sign_type这两个参数，alipay没有remove这个参数，要自己remove
		params.remove("sign_type");
		try {
			boolean alipayRSACheckedV2 = AlipaySignature.rsaCheckV2(params, Configs.getAlipayPublicKey(), "utf-8", Configs.getSignType());
			if(!alipayRSACheckedV2){
				return ServerResponse.createByErrorMessage("非法请求，验证不通过，再试就报警");
			}
		} catch (AlipayApiException e) {
			logger.error("支付宝回调验证失败",e);
		}
		//TODO 验证各种数据
		
		//业务逻辑
		ServerResponse serverResponse =iOrderService.aliCallback(params);
		if(serverResponse.isSuccess()){
			return Const.AlipayCallback.RESPONSE_SUCCESS;
		}
		return Const.AlipayCallback.RESPONSE_FALIED;
	}
	
	/**
	 *Title:queryOrderPayStatus
	 *Description:前台查询订单状态，查询用户是否已经付款
	 *@param session
	 *@param orderNo 订单号
	 *@param request
	 *@return
	 *Throws
	 */
	@RequestMapping("query_order_pay.action")
	@ResponseBody
	public ServerResponse queryOrderPayStatus(HttpSession session,Long orderNo,HttpServletRequest request){
		User user =(User)session.getAttribute(Const.CURRENT_USER);
		if(user==null){
			return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),
					ResponseCode.NEED_LOGIN.getDesc());
		}
		ServerResponse serverResponse = iOrderService.queryOrderPayStatus(user.getId(), orderNo);
		if(serverResponse.isSuccess()){
			return ServerResponse.createBySuccess(true);
		}
		return ServerResponse.createBySuccess(false);
	}
	
	
}
