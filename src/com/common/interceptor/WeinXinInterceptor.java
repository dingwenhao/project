package com.common.interceptor;

import java.io.UnsupportedEncodingException;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.kit.PropKit;
import com.weixin.WeixinCommonService;

public class WeinXinInterceptor implements Interceptor{


	@SuppressWarnings("static-access")
	@Override
	public void intercept(Invocation inv) {
        String domain=PropKit.get("domain");
	    String url=domain+inv.getActionKey();
		try {
			inv.getController().setAttr("signature",WeixinCommonService.weixinsevice.getsignature(inv.getController(),url));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		inv.getController().setAttr("appId", WeixinCommonService.weixinsevice.getAccessToken(inv.getController()));
		
		inv.getController().setAttr("nonceStr", WeixinCommonService.weixinsevice.create_nonce_str());

	}

}
