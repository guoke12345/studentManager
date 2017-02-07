package com.stuManager.config;
import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.core.JFinal;
import com.jfinal.ext.handler.ContextPathHandler;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.c3p0.C3p0Plugin;
import com.jfinal.render.ViewType;
import com.stuManager.Models.Basicinfo;
import com.stuManager.Models.Classes;
import com.stuManager.Models.Major;
import com.stuManager.Models.Pro_info;
import com.stuManager.Models.Project;
import com.stuManager.Models.Student;
import com.stuManager.Models.User;
import com.stuManager.controllers.IndexController;
import com.stuManager.controllers.Pro_infoController;
import com.stuManager.controllers.ProjectController;
import com.stuManager.controllers.StuController;
import com.stuManager.controllers.UploadExcelController;
import com.stuManager.controllers.UserController;
import com.stuManager.interceptor.LoginInterceptor;

public class StuManagerConfig extends JFinalConfig {

	@Override
	/*
	 * 配置常量
	 */
	public void configConstant(Constants arg0) {
		// TODO Auto-generated method stub
//		loadPropertyFile("a_little_config.txt");
		arg0.setDevMode(true);
		arg0.setViewType(ViewType.FREE_MARKER);
		arg0.setError404View("/404.html");
	}

	@Override
	public void configHandler(Handlers arg0) {
		// TODO Auto-generated method stub
		arg0.add(new ContextPathHandler("contextPath"));//设置上下文路径
	}

	@Override
	public void configInterceptor(Interceptors arg0) {
		// TODO Auto-generated method stub
		//配置登录的全局拦截器
		arg0.add(new LoginInterceptor());
	}

	@Override
	public void configPlugin(Plugins arg0) {
		Prop p  = PropKit.use("config.txt");
		// TODO Auto-generated method stub
//		C3p0Plugin cp=new C3p0Plugin("jdbc:mysql://121.42.26.121:3306/stu_manager?useUnicode=true&characterEncoding=utf-8", "zhang", "q12582");
		C3p0Plugin cp=new C3p0Plugin(p.get("jdbcUrl"), p.get("user"), p.get("password"));
		arg0.add(cp);
		ActiveRecordPlugin arp=new ActiveRecordPlugin(cp);
		arg0.add(arp);
		arp.setShowSql(true);//调试阶段在控制台打印sql语句
		arp.addMapping("student","Id", Student.class);
		arp.addMapping("user","Id", User.class);
		arp.addMapping("classes","Id", Classes.class);
		arp.addMapping("major","Id", Major.class);
		arp.addMapping("project","Id", Project.class);
		arp.addMapping("pro_info","Id", Pro_info.class);
		arp.addMapping("basicinfo","Id", Basicinfo.class);
		
	}

	@Override
	public void configRoute(Routes arg0) {
		// TODO Auto-generated method stub
		//分别控制路由
		arg0.add("/stu", StuController.class);
		arg0.add("/user",UserController.class);
		arg0.add("/pro",ProjectController.class);
		arg0.add("/info",Pro_infoController.class);
		arg0.add("/upload",UploadExcelController.class);
		arg0.add("/",IndexController.class);
	}
	
	//配置端口号
	public static void main(String[] args) {
		JFinal.start("webapp", 80, "/", 5);
	}

}
