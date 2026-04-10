package com.team_34.controller;

import com.team_34.model.DatabaseModel;

import javax.servlet.annotation.WebServlet;
import java.io.PrintWriter;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;

@WebServlet("/CustomerServelet")
public class CustomerServelet extends HttpServlet {

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
		// Retrieve parameters using getParameter
		String clientIdentification = request.getParameter("clientIdentification");
		String clientFirstName = request.getParameter("clientFirstName");
		String clientLastName = request.getParameter("clientLastName");
		String clientCity = request.getParameter("clientCity");
		String clientStreet = request.getParameter("clientStreet");
		String clientHouseNumber = request.getParameter("clientHouseNumber");
		String clientPostalCode = request.getParameter("clientPostalCode");
		Date clientBirthDate = java.sql.Date.valueOf(request.getParameter("clientBirthDate"));
		String clientCreditCardExpDate = request.getParameter("clientCreditCardExpDate");
		String clientCreditCardNumber = request.getParameter("clientCreditCardNumber");
		String clientCreditCardCCV = request.getParameter("clientCreditCardCCV");
		// Retrieve parameters using getParameter

		// Use the retrieved parameters as needed
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
		out.println("<h1 class=\"mt-5\">Customer Registrered</h1>");
		out.println("<p class=\"lead\">Client Identification: " + clientIdentification + "</p>");
		out.println("<p class=\"lead\">First Name: " + clientFirstName + "</p>");
		out.println("<p class=\"lead\">Last Name: " + clientLastName + "</p>");
		out.println("<p class=\"lead\">City: " + clientCity + "</p>");
		out.println("<p class=\"lead\">Street: " + clientCity + "</p>");
		// Add more output for other parameters as needed

		out.println("</div>");
		out.println("</body>");
		out.println("</html>");

		Object[] customerValues = { clientIdentification, clientFirstName, clientLastName, clientCity, clientStreet,
				clientHouseNumber, clientPostalCode, clientBirthDate };
		Object[] CreditCardValues = { clientCreditCardNumber, clientCreditCardExpDate, clientCreditCardCCV,
				clientIdentification };
		handleModel(new DatabaseModel(), customerValues, CreditCardValues);
		out.close();

	}

	private void handleModel(DatabaseModel databaseModel, Object[] customerValues, Object[] CreditCardValues) {
		databaseModel.addCustomer(customerValues);
		databaseModel.addCreditCard(CreditCardValues);
		databaseModel.closeConnection();
	}
}
