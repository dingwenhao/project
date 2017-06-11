package com.demo.blog;

import com.demo.common.model.Blog;
import com.jfinal.aop.Before;
import com.jfinal.core.Const;
import com.jfinal.core.Controller;
import com.jfinal.token.TokenManager;

/**
 * 本 demo 仅表达最为粗浅的 jfinal 用法，更为有价值的实用的企业级用法
 * 详见 JFinal 俱乐部: http://jfinal.com/club
 * 
 * BlogController
 * 所有 sql 与业务逻辑写在 Model 或 Service 中，不要写在 Controller 中，养成好习惯，有利于大型项目的开发与维护
 */
@Before(BlogInterceptor.class)
public class BlogController extends Controller {
	public void index() {
		setAttr("blogPage", Blog.me.paginate(getParaToInt(0, 1), 10));
		render("blog.html");
	}
	
	public void add() {
		TokenManager.createToken(this, "token", Const.MIN_SECONDS_OF_TOKEN_TIME_OUT);
	}
	
	@Before(BlogValidator.class)
	public void save() {
		
		getModel(Blog.class).save();
		redirect("/blog");
	}
	
	public void edit() {
		TokenManager.createToken(this, "token", Const.MIN_SECONDS_OF_TOKEN_TIME_OUT);
		setAttr("blog", Blog.me.findById(getParaToInt()));
	}
	
	@Before(BlogValidator.class)
	public void update() {
		System.out.println(TokenManager.validateToken(this, "token"));
		getModel(Blog.class).update();
		redirect("/blog");
	}
	
	public void delete() {
		Blog.me.deleteById(getParaToInt());
		redirect("/blog");
	}
}


