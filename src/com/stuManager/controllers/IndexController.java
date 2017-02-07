package com.stuManager.controllers;

import com.jfinal.core.Controller;

public class IndexController extends Controller{
	public void index(){
		renderFreeMarker("/login.html");
	}
}
