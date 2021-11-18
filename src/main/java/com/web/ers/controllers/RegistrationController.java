package com.web.ers.controllers;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.app.dao.UserDao;
import com.app.logging.Logging;
import com.app.models.User;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RegistrationController {
	public static void createUser(HttpServletRequest req, HttpServletResponse res) throws IOException {
		ObjectMapper om = new ObjectMapper();
		JsonNode jn = om.readTree(req.getReader());
		
		User u = new User();
		u.setID(0);
		u.setUsername(jn.get("username").asText());
		u.setPassword(jn.get("password").asText());
		u.setFirstName(jn.get("firstName").asText());
		u.setLastName(jn.get("lastName").asText());
		u.setEmail(jn.get("email").asText());
		u.setRoleID(jn.get("roleID").asInt());
		UserDao ud = new UserDao();
		int id =ud.create(u);
		System.out.println(ud.getByID(id));
		res.getWriter().write(om.writeValueAsString(u));
		Logging.logger.info("User was registered.");
	}
}
