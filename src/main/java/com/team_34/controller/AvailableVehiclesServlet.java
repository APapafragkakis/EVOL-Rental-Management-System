package com.team_34.controller;

import com.team_34.model.DatabaseModel;
import com.team_34.utilities.DatabaseQueries;

import javax.servlet.annotation.WebServlet;
import java.io.PrintWriter;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import java.math.BigDecimal;

@WebServlet("/AvailableVehiclesServlet")
public class AvailableVehiclesServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		handleRequest(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		handleRequest(request, response);
	}

	private void handleRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		String vehicleType = request.getParameter("vehicleType");

		if (vehicleType.equals("Car")) {

		} else if (vehicleType.equals("Motorbike")) {

		} else if (vehicleType.equals("Scooter")) {

		} else if (vehicleType.equals("Bicycle")) {

		}
	}
}
