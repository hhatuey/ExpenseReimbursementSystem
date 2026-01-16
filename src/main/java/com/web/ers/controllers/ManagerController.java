package com.web.ers.controllers;

import java.io.IOException;
import java.sql.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.app.dao.ExpensesDao;
import com.app.dao.ReimbursementDao;
import com.app.dao.UserDao;
import com.app.logging.Logging;
import com.app.models.Expenses;
import com.app.models.Reimbursement;
import com.app.models.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ManagerController {
	public static void expenses(HttpServletRequest req, HttpServletResponse res) throws IOException, JsonProcessingException {
		ObjectMapper om = new ObjectMapper();
		
		List<Expenses> list = new ExpensesDao().getAll();
		res.getWriter().write(om.writeValueAsString(list));
	}
	
	public static void employees(HttpServletRequest req, HttpServletResponse res) throws IOException, JsonProcessingException {
		ObjectMapper om = new ObjectMapper();
		
		List<User> list = new UserDao().getAll();
		res.getWriter().write(om.writeValueAsString(list));
	}
	
	public static void ticket(HttpServletRequest req, HttpServletResponse res) throws JsonProcessingException, IOException {
		ObjectMapper om = new ObjectMapper();
		String field = req.getParameter("field");
		int id = Integer.parseInt(req.getParameter("id"));
		List<Expenses> list = new ExpensesDao().getAllWhere(field+ "=" +id);
		res.getWriter().write(om.writeValueAsString(list));
	}
	
	public static void ticketByName(HttpServletRequest req, HttpServletResponse res) throws JsonProcessingException, IOException {
		ObjectMapper om = new ObjectMapper();
		String field = req.getParameter("field");
		String name = req.getParameter("name");
		List<Expenses> list = new ExpensesDao().getAllWhere(field+ "='" +name+ "'");
		res.getWriter().write(om.writeValueAsString(list));
	}
	
	public static void update(HttpServletRequest req, HttpServletResponse res) throws IOException, JsonProcessingException {
		ObjectMapper om = new ObjectMapper();
		ReimbursementDao rd = new ReimbursementDao();
		JsonNode jn = om.readTree(req.getReader());
		Reimbursement r = rd.getByID(jn.get("id").asInt());
		r.setResolved(new Date(jn.get("resolved").asLong()));
		r.setResolverID(jn.get("resolver").asInt());
		r.setStatusID(jn.get("status").asInt());
		if(rd.update(r)) {
			res.getWriter().write(om.writeValueAsString(jn));
			Logging.logger.info("Reimbursement was updated.");
			return;
		}
		res.getWriter().write("{\"msg\":\"Failed\"}");
	}
	
	
}
