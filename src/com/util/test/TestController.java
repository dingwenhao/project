package com.util.test;

import java.net.URISyntaxException;
import java.util.List;

import org.eclipse.jetty.server.Authentication.User;

import com.jfinal.core.Controller;
import com.jfinal.kit.JsonKit;
import com.jfinal.kit.PathKit;
import com.util.model.Company;
import com.util.test.enumtest.User_type;

/**
 * 测试类
 * @author huiyi
 *
 */
public class TestController extends Controller {

	
	public void ztree(){
		render("/util/test/ztree/index.html");
		
	}
	
	public void ztreeData(){
		List<Company> companys=Company.dao.find("select id ,hos_name as name from company where  parent_id is null ");
		for(Company company:companys){
			List<Company>	list=Company.dao.find(" select id ,hos_name as name from company where  parent_id =? ",company.get("id"));
			if(list!=null){
				company.put("children", list);
			}
		}
		renderJson(companys);
	}
	
	public static void enumTest(){
		System.out.println(User_type.WORKER.getName());
	   System.out.println(User_type.values().length);
	   System.out.println(User_type.valueOf(User_type.class, "COMMON"));
	   Class clzz=User_type.class;
	   Class clazzSuper = clzz.getSuperclass();  
	   System.out.println(clazzSuper.getName());
	}
	
	public static void main(String[] args) {
		
		
		enumTest();

	}

}
