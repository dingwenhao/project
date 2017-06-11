package com.weixin;


import com.common.interceptor.WeinXinInterceptor;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;

@Before(WeinXinInterceptor.class)
public class WeiXinController extends Controller {
    //测试域名
	public  void  index(){
		render("/weixin/index.html");
	}
}
