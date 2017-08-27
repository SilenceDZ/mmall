package com.mmall.controller.portal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IOrderService;

@Controller
@RequestMapping("/order/")
public class OrderController {
	
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
	
	
	
	
	
	
}
