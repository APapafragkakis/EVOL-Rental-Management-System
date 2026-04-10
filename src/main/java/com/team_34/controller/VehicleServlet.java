package com.team_34.controller;

import com.team_34.model.DatabaseModel;

import javax.servlet.annotation.WebServlet;
import java.io.PrintWriter;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import java.math.BigDecimal;

@WebServlet("/VehicleServlet")
public class VehicleServlet extends HttpServlet {

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
		// Get parameters from the form
		String vehicleType = request.getParameter("vehicleType");
		String vehicleColor = request.getParameter("vehicleColor");
		String vehicleModel = request.getParameter("vehicleModel");
		String vehicleBrand = request.getParameter("vehicleBrand");
		BigDecimal dailyRentalCost = BigDecimal.valueOf(Double.parseDouble(request.getParameter("dailyRentalCost")));
		BigDecimal dailyInsuranceCost = BigDecimal
				.valueOf(Double.parseDouble(request.getParameter("dailyInsuranceCost")));
		BigDecimal hourlyDelayedCost = BigDecimal
				.valueOf(Double.parseDouble(request.getParameter("hourlyDelayedCost")));
		Integer rangeInKilometers = Integer.parseInt(request.getParameter("rangeInKilometers"));
		Integer numberOfPassengers = Integer.parseInt(request.getParameter("numberOfPassengers"));
		String carType = request.getParameter("carType");
		String registrationNumber = request.getParameter("registrationNumber");
		String uniqueNumber = request.getParameter("uniqueNumber");

		out.println("<!DOCTYPE html>");
		out.println("<html lang=\"en\">");
		out.println("<head>");
		out.println("    <meta charset=\"UTF-8\">");
		out.println("    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">");
		out.println("    <title>Register New Customer</title>");
		out.println(
				"    <link rel=\"stylesheet\" href=\"https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css\">");
		out.println("    <link rel=\"stylesheet\" href=\"style.css\">");
		out.println("</head>");
		out.println("<body>");
		out.println("<div class=\"container\">");

		// Output content using Bootstrap styles
		out.println("<h1 class=\"mt-5\">Vehicle Supplied</h1>");
		out.println("<p class=\"lead\">Vehicly Type: " + vehicleType + "</p>");
		out.println("<p class=\"lead\">Brand: " + vehicleBrand + "</p>");
		out.println("<p class=\"lead\">Model: " + vehicleModel + "</p>");
		out.println("<p class=\"lead\">Color: " + vehicleColor + "</p>");
		out.println("<p class=\"lead\">Cost of daily rental: " + dailyRentalCost + "</p>");
		out.println("<p class=\"lead\">Cost per delayed hour: " + hourlyDelayedCost + "</p>");
		out.println("<p class=\"lead\">Cost for insurance per day: " + dailyInsuranceCost + "</p>");
		// Add more output for other parameters as needed

		out.println("</div>");
		out.println("</body>");
		out.println("</html>");

		Object[] typeValues = {
				vehicleType,
				vehicleBrand,
				vehicleModel,
				dailyRentalCost,
				dailyInsuranceCost,
				hourlyDelayedCost,
				rangeInKilometers
		};
		Object[] vehicleVal = { null, vehicleType, vehicleBrand, vehicleModel, vehicleColor };
		Object[] carValues = { null, carType, numberOfPassengers };
		Object[] registeredVals = { null, registrationNumber };
		Object[] unregisteredVals = { null, uniqueNumber };
		handleModel(new DatabaseModel(), typeValues, vehicleVal, registeredVals, unregisteredVals, carValues);
		out.close();
	}

	private void handleModel(DatabaseModel databaseModel, Object[] typeValues, Object[] vehicleVal,
			Object[] registeredVals, Object[] unregisteredVals, Object[] carValues) {
		databaseModel.addVehicle(typeValues, vehicleVal, registeredVals, unregisteredVals, carValues);
		databaseModel.closeConnection();
	}
}
