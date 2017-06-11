package com.weixin;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.Map;
import java.util.UUID;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.core.Controller;
import com.jfinal.kit.HttpKit;



public class WeixinCommonService {
      public static  WeixinCommonService weixinsevice=new WeixinCommonService();
     static final String  app_key="wx5cced67fa48f8f66";
     static final String  appSecret="62665042c23ba3909bff256f5b88fda1";
	
	/**
	 * 获取access_token
	 */
	public static  String  getAccessToken(Controller c){
		String access_token=c.getCookie("access_token");
		if(StringUtils.isEmpty("access_token")){
		String weixinUrl="https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+app_key+"&secret="+appSecret+"";
		String backString=HttpKit.get(weixinUrl);
		@SuppressWarnings("unchecked")
		Map<String,String> map= JSONObject.parseObject(backString, Map.class);
	    c.setCookie("access_token",map.get("access_token"), 7000);
		return  map.get("access_token");
		}else{
			return access_token;
		}
	}
	
	public static  String  getJSApiTicket(Controller c){
		String access_token=getAccessToken(c);
		String weixinUrl = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token="+access_token+"&type=jsapi"; 
		String backString=HttpKit.get(weixinUrl);
		@SuppressWarnings("unchecked")
		Map<String, String> map=JSONObject.parseObject(backString,Map.class);
		return map.get("ticket");
		
	}
	public static String gettimestamp(){
		return Long.toString(System.currentTimeMillis());
	}
	
	public static String create_nonce_str() {  
        return UUID.randomUUID().toString();  
    } 
	
	public static String getsignature(Controller c,String url) throws UnsupportedEncodingException{
		String jsapi_ticket=getJSApiTicket(c);
		String noncestr=create_nonce_str();
		String timestamp=gettimestamp();
		StringBuilder builder=new StringBuilder("jsapi_ticket=");
		builder.append(jsapi_ticket)
		.append("&noncestr=").append(noncestr)
		.append("&timestamp=").append(timestamp)
		.append("&url=").append(url);
		String string1=builder.toString();
		MessageDigest digest = null;
		try {
			digest = MessageDigest.getInstance("SHA-1");
			digest.reset();
			digest.update(string1.getBytes("UTF-8"));  
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
        
        //获取字节数组  
        byte messageDigest[] = digest.digest(); 
       String signture= byteToHex(messageDigest);
       return  signture;
	}
	
	 private static String byteToHex(final byte[] hash) {  
	        Formatter formatter = new Formatter();  
	        for (byte b : hash)  
	        {  
	            formatter.format("%02x", b);  
	        }  
	        String result = formatter.toString();  
	        formatter.close();  
	        return result;  
	    } 
	public static void main(String[] args) throws UnsupportedEncodingException {
	}

}
