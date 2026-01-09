package com.web.ers.servlets;

import java.io.IOException;
import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.web.ers.controllers.HomeController;
import com.web.ers.controllers.LoginController;
import com.web.ers.controllers.ManagerController;
import com.web.ers.controllers.RegistrationController;
import com.web.ers.controllers.SessionController;

public class Dispatcher {
	public static void process(HttpServletRequest req, HttpServletResponse res)
			throws IOException, JsonProcessingException {

		System.out.println(req.getPathInfo());
		
		invoker(req.getPathInfo(), req, res);
		
	}
	
	private static void invoker(String path, HttpServletRequest req, HttpServletResponse res) {
		path = path.substring(1);
		try {
			Method m = Dispatcher.class.getDeclaredMethod(path, HttpServletRequest.class, HttpServletResponse.class);
			m.invoke(m, req, res);	
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("method not found!");
		} 
	}
	

	private static void login(HttpServletRequest req,  HttpServletResponse res) throws IOException, JsonProcessingException {
		LoginController.login(req, res);
	}

	private static void logout(HttpServletRequest req,  HttpServletResponse res) throws IOException, JsonProcessingException {
		LoginController.logout(req, res);
	}	
	
	private static void register(HttpServletRequest req,  HttpServletResponse res) throws IOException, JsonProcessingException {
		RegistrationController.createUser(req, res);
	}
	
	private static void home(HttpServletRequest req, HttpServletResponse res) throws IOException, JsonProcessingException {
		HomeController.expenses(req, res);
	}
	
	private static void manager(HttpServletRequest req, HttpServletResponse res) throws IOException, JsonProcessingException {
		ManagerController.expenses(req, res);
	}
	
	private static void employees(HttpServletRequest req, HttpServletResponse res) throws IOException, JsonProcessingException {
		ManagerController.employees(req, res);
	}
	
	private static void ticket(HttpServletRequest req, HttpServletResponse res) throws IOException, JsonProcessingException {
		ManagerController.ticket(req, res);
	}
	
	private static void ticketbyname(HttpServletRequest req, HttpServletResponse res) throws IOException, JsonProcessingException {
		ManagerController.ticketByName(req, res);
	}
	
	private static void types(HttpServletRequest req, HttpServletResponse res) throws IOException, JsonProcessingException {
		HomeController.types(req, res);
	}
	
	private static void save(HttpServletRequest req, HttpServletResponse res) throws IOException, JsonProcessingException {
		HomeController.save(req, res);
	}
	
	private static void update(HttpServletRequest req, HttpServletResponse res) throws IOException, JsonProcessingException {
		ManagerController.update(req, res);
	}
}
