package com.web.ers.controllers;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.app.dao.ExpensesDao;
import com.app.dao.ReimbursementDao;
import com.app.logging.Logging;
import com.app.models.Expenses;
import com.app.models.Reimbursement;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class HomeController {
	public static void expenses(HttpServletRequest req, HttpServletResponse res) throws IOException, JsonProcessingException {
		ObjectMapper om = new ObjectMapper();
//		JsonNode jn = om.readTree(req.getReader());
		int id = Integer.parseInt(req.getParameter("userid"));
		
		List<Expenses> list = new ExpensesDao().getAllWhere(" author_id ="+id);
		res.getWriter().write(om.writeValueAsString(list));
	}
	
	public static void types(HttpServletRequest req, HttpServletResponse res) throws IOException, JsonProcessingException {
		ObjectMapper om = new ObjectMapper();
		
		List<Reimbursement.Type> list = new ReimbursementDao().getTypes();
		res.getWriter().write(om.writeValueAsString(list));
	}
	
	public static void status(HttpServletRequest req, HttpServletResponse res) throws IOException, JsonProcessingException {
		ObjectMapper om = new ObjectMapper();
		
		List<Reimbursement.Status> list = new ReimbursementDao().getStatus();
		res.getWriter().write(om.writeValueAsString(list));
	}
	
	public static void save(HttpServletRequest req, HttpServletResponse res) throws IOException, JsonProcessingException {
		ObjectMapper om = new ObjectMapper();
		JsonNode jn = om.readTree(req.getReader());
		System.out.println(jn);
		Reimbursement r = om.readValue(jn.traverse(), Reimbursement.class);
		System.out.println(om.writeValueAsString(r));
		int id = new ReimbursementDao().create(r);
		System.out.println(id);
		res.getWriter().write(om.writeValueAsString(r));
		Logging.logger.info("Reimbursement was created.");
	}
}
