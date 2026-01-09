package com.web.ers.controllers;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.app.dao.UserDao;
import com.app.logging.Logging;
import com.app.models.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class LoginController {
	public static void login(HttpServletRequest req, HttpServletResponse res)
			throws IOException, JsonProcessingException {
		ObjectMapper om = new ObjectMapper();
		JsonNode jn = om.readTree(req.getReader());

		UserDao ud = new UserDao();
		User u = ud.getByEmail(jn.get("email").asText());
		if (u != null) {
			if (u.getPassword().equals(jn.get("password").asText())) {
				req.getSession().setAttribute("id", u.getID());
				req.getSession().setAttribute("user", u.getEmail());
				res.setStatus(200);
				res.getWriter().write(om.writeValueAsString(u));
				Logging.logger.info(u.getEmail() + " logged in.");
				return;
			}
			u = null;
		}
		res.getWriter().write(om.writeValueAsString(u));

//		res.setStatus(200);
//		int c = 0;
//		if(req.getSession().getAttribute("counter") != null)
//			c = (int)req.getSession().getAttribute("counter");
//		req.getSession().setAttribute("counter", ++c);
//		res.getWriter().println("Welcome!++ "+req.getSession().getAttribute("counter"));
	}

	public static void logout(HttpServletRequest req, HttpServletResponse res)
			throws IOException, JsonProcessingException {
		ObjectMapper om = new ObjectMapper();
		String user = req.getSession().getAttribute("user").toString();
		req.getSession().invalidate();
		Logging.logger.info(user + " logged out.");
		res.getWriter().write(om.writeValueAsString("{\"msg\":\"User logged out\"}"));

	}
}
