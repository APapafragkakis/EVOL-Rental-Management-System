package com.team_34.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

import com.team_34.utilities.DatabaseQueries;
import com.team_34.utilities.DatabaseUtilities;
import com.team_34.utilities.DatabaseQueries;

public class DatabaseModel {
	private Connection connection;
	private static int vehicleID = 0;
	private static int RentalID = 0;

	public DatabaseModel() {
		// Initialize the database connection
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://localhost:3306/EVOL";
			String username = "root";
			String password = "";
			connection = DriverManager.getConnection(url, username, password);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

	public void addCustomer(Object[] customerValues) {
		this.addTuple("Customer", customerValues);
	}

	public void addVehicle(Object[] typevalues, Object[] vehiclevalues, Object[] registeredvalues,
			Object[] unregisteredvalues, Object[] carValues) {
		vehicleID++;
		String vehicleTable = "Vehicle";
		String typeTable = "Vehicle_Type_Brand_Model";
		String carTable = "Car";
		String registeredTable = "Registered_Vehicle";
		String unregisteredTable = "Unregistered_Vehicle";
		this.addTuple(typeTable, typevalues);

		vehiclevalues[0] = (vehicleID);
		this.addTuple(vehicleTable, vehiclevalues);

		if (typevalues[0].equals("Car") || typevalues[0].equals("Motorbike")) {
			if (typevalues[0].equals("Car")) {
				carValues[0] = (vehicleID);
				this.addTuple(carTable, carValues);
			}
			registeredvalues[0] = (vehicleID);
			this.addTuple(registeredTable, registeredvalues);
		} else {
			unregisteredvalues[0] = (vehicleID);
			this.addTuple(unregisteredTable, unregisteredvalues);
		}

	}

	public void addRental(Object[] rentalValues, Object[] carMotorbikeRentalValues) {
		RentalID++;
		rentalValues[0] = (RentalID);
		this.addTuple("Rental", rentalValues);

		if (DatabaseUtilities.checkCarRentalInsertion(connection, RentalID)) {
			carMotorbikeRentalValues[0] = (RentalID);
			this.addTuple("Car_Motorbike_Rental", carMotorbikeRentalValues);
		}
	}

	public void addDriver(Object[] driverValues) {
		addTuple("Driver", driverValues);
		if (!DatabaseUtilities.checkDriver(connection, (Date) driverValues[1], (Integer) driverValues[2])) {
			// OUTPUT
			System.out.println("Driver is not old enough to drive this vehicle");
			deleteTuple("Driver", "RentalID = " + driverValues[2].toString());
		}
	}

	public void addPayment(Object[] paymentValues) {
		this.addTuple("Payment", paymentValues);
	}

	public void addCreditCard(Object[] creditCardValues) {
		this.addTuple("Credit_Card", creditCardValues);
	}

	public void addService(Object[] serviceValues) {
		this.addTuple("Service", serviceValues);
	}

	private void addTuple(String tableName, Object[] values) {
		List<String> columnNames = getTableColumns(tableName);

		try {
			// Construct the SQL query dynamically based on the table and columns
			String insertQuery = DatabaseUtilities.createInsertQuery(tableName,
					columnNames.toArray(new String[0]),
					values);
			// Create a PreparedStatement and set parameters based on values
			PreparedStatement statement = connection.prepareStatement(insertQuery);

			// Set values dynamically based on the column types
			for (int i = 0; i < columnNames.size(); i++) {
				int columnType = DatabaseUtilities.getColumnType(connection, tableName, columnNames.get(i));
				DatabaseUtilities.setObjectByType(statement, i + 1, columnType, values[i]);
			}

			// Execute the query
			statement.executeUpdate();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void updateTuple(String tableName, String[] columnNames, String[] values, String condition) {
		try {
			// Construct the SQL query dynamically based on the table and columns
			String updateQuery = DatabaseUtilities.createUpdateQuery(tableName, columnNames, values, condition);
			// Create a PreparedStatement and set parameters based on values
			PreparedStatement statement = connection.prepareStatement(updateQuery);

			for (int i = 0; i < values.length; i++) {
				statement.setString(i + 1, values[i]);
			}

			// Execute the query
			statement.executeUpdate();

			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public List<List<String>> readTuple(String tableName, String[] columnNames, String condition) {
		List<List<String>> results = new ArrayList<>();

		try {
			String readQuery = DatabaseUtilities.createSelectQuery(tableName, columnNames, condition);
			// Create a PreparedStatement and execute the query
			PreparedStatement statement = connection.prepareStatement(readQuery);
			ResultSet resultSet = statement.executeQuery();

			// Retrieve data from the result set
			while (resultSet.next()) {
				List<String> resultRow = new ArrayList<>();
				for (String columnName : columnNames) {
					resultRow.add(resultSet.getString(columnName));
				}
				results.add(resultRow);
			}

			statement.executeQuery();

			// Close resources
			resultSet.close();
		} catch (SQLException e) {
			e.printStackTrace();
			// Handle exceptions appropriately
		}

		return results;
	}

	// Generic method to delete a record from any table
	public void deleteTuple(String tableName, String condition) {
		try {
			// Construct the SQL query dynamically based on the table and condition
			String query = DatabaseUtilities.createDeleteQuery(tableName, condition);

			// Create a PreparedStatement and execute the query
			PreparedStatement statement = connection.prepareStatement(query);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public int getRentalID() {
		return RentalID;
	}

	private List<String> getTableColumns(String tableName) {
		// Somehow return column names for each table
		List<String> columnNames = new ArrayList<>();
		try {
			DatabaseMetaData metaData = connection.getMetaData();
			ResultSet columnsResultSet = metaData.getColumns(null, null, tableName, "%");

			while (columnsResultSet.next()) {
				String columnName = columnsResultSet.getString("COLUMN_NAME");
				String isAutoIncrement = columnsResultSet.getString("IS_AUTOINCREMENT");
				String defaultValue = columnsResultSet.getString("COLUMN_DEF");

				// Check if the column has the AUTO_INCREMENT property or if it has a default
				// value
				if (!"YES".equalsIgnoreCase(isAutoIncrement) && defaultValue == null) {
					columnNames.add(columnName);
				}
			}

			columnsResultSet.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return columnNames;
	}

	public void closeConnection() {
		try {
			if (connection != null && !connection.isClosed()) {
				connection.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void printTuples(List<List<String>> tuples) {
		if (tuples.size() == 0) {
			System.out.println("No tuples found");
			return;
		} else {
			for (List<String> tuple : tuples) {
				System.out.println(tuple);
			}
		}
	}

	public void initializeCustomers() {
		// Adding 5 Customers

		String[] TIN = { "000000000", "000000001", "000000002", "000000003", "000000004" };
		String[] FirstNames = { "John", "Jane", "Jack", "Jill", "James" };
		String[] LastNames = { "Doe", "Doe", "Black", "Black", "Bond" };
		String[] City = { "New York", "New York", "Los Angeles", "Los Angeles", "London" };
		String[] Street = { "1st Street", "2nd Street", "3rd Street", "4th Street", "5th Street" };
		String[] PostalCode = { "10001", "10002", "90001", "90002", "90031" };
		String[] HouseNumber = { "001", "002", "003", "004", "005" };
		Date[] BirthDate = { Date.valueOf("1990-01-01"), Date.valueOf("1990-01-02"), Date.valueOf("1990-01-03"),
				Date.valueOf("1990-01-04"), Date.valueOf("1990-01-05") };
		for (int i = 0; i < 5; i++) {
			Object[] values = {
					TIN[i],
					FirstNames[i],
					LastNames[i],
					City[i],
					Street[i],
					HouseNumber[i],
					PostalCode[i],
					BirthDate[i]
			};

			addCustomer(values);
		}
	}

	public void initializeVehicles() {
		// Adding 7 Vehicles
		// Vehicle addition
		String[] Color = { "Red", "Blue", "Green", "Yellow", "Black", "Grey", "Brown" };
		// VehicleTypeBrandModel additions
		String[] VehicleType = { "Car", "Motorbike", "Car", "Scooter", "Bicycle", "Car", "Bicycle" };
		String[] Model = { "Model 3", "ninja", "M3", "100", "Turbo Levo", "Roadster", "Lola" };
		String[] Brand = { "Tesla", "Kawasaki", "BMW", "Lime", "S-Works", "Tesla", "Mountain" };
		BigDecimal[] DailyInsuranceCost = { new BigDecimal(10), new BigDecimal(5), new BigDecimal(10),
				new BigDecimal(5.20), new BigDecimal(2.12), new BigDecimal(10.99), new BigDecimal(20.01) };
		BigDecimal[] DailyRentalCost = { new BigDecimal(100), new BigDecimal(50), new BigDecimal(100),
				new BigDecimal(50), new BigDecimal(12), new BigDecimal(100), new BigDecimal(20) };
		BigDecimal[] HourlyDelayedCost = { new BigDecimal(10), new BigDecimal(5), new BigDecimal(10),
				new BigDecimal(5.20), new BigDecimal(2.12), new BigDecimal(10.99), new BigDecimal(20.01) };
		Integer[] RangeInKilometers = { 500, 300, 500, 100, 50, 500, 50 };
		// RegisteredVehicle addition
		String[] RegistrationNumber = { "ABC-0001", "ACD-0001", "CDB-0021", "", "", "ATC-5435", "" };
		// UnregisteredVehicle addition
		String[] UniqueNumber = { "", "", "", "00001", "00002", "", "00003" };
		// Car Values
		Integer[] NumberOfPassengers = { 5, -1, 5, -1, -1, 5, -1 };
		String[] CarType = { "Sedan", "", "Sedan", "", "", "Truck", "" };

		// Add 7 Vehicles to corresponding Tables
		for (int i = 0; i < 7; i++) {
			Object[] typeValues = {
					VehicleType[i],
					Brand[i],
					Model[i],
					DailyRentalCost[i],
					DailyInsuranceCost[i],
					HourlyDelayedCost[i],
					RangeInKilometers[i]
			};
			Object[] vehicleVal = { "", VehicleType[i], Brand[i], Model[i], Color[i] };
			Object[] carValues = { "", CarType[i], NumberOfPassengers[i] };
			Object[] registeredVals = { "", RegistrationNumber[i] };
			Object[] unregisteredVals = { "", UniqueNumber[i] };

			addVehicle(typeValues, vehicleVal, registeredVals, unregisteredVals, carValues);
		}
	}

	public void initializeTables() {
		this.initializeCustomers();
		this.initializeVehicles();
		this.initializeRentals();
		this.initializePayment();
		this.initializeDrivers();
		this.initializeCreditCards();
	}

	public void initializeRentals() {
		// Adding 5 Rentals
		Timestamp[] deliveryDateTime = {
				Timestamp.valueOf("2019-02-09 10:20:30"),
				Timestamp.valueOf("2024-01-22 00:00:00"),
				Timestamp.valueOf("2023-01-05 11:00:00"),
				Timestamp.valueOf("2023-02-03 02:01:00"),
				Timestamp.valueOf("2023-01-11 00:00:00")
		};
		Timestamp[] returnDateTime = {
				Timestamp.valueOf("2019-02-11 10:20:30"),
				Timestamp.valueOf("2024-01-20 00:00:00"),
				Timestamp.valueOf("2023-01-01 11:00:00"),
				Timestamp.valueOf("2023-02-01 02:01:00"),
				Timestamp.valueOf("2023-01-10 00:00:00")
		};
		Boolean[] hasInsurance = { true, true, true, true, true };
		String[] TIN = { "000000000", "000000001", "000000002", "000000003", "000000004" };
		Integer[] vehicleID = { 1, 2, 3, 4, 5 };
		// Car_Motorbike_Rental addition
		String[] DriverLicenceNumber = { "12345", "12346", "12347", "12350", "" };
		for (int i = 0; i < 5; i++) {
			Object[] values = { "",
					returnDateTime[i],
					null,
					deliveryDateTime[i],
					hasInsurance[i],
					TIN[i],
					vehicleID[i]
			};
			addRental(values, new Object[] { "", DriverLicenceNumber[i] });
			updateTuple("Vehicle", new String[] { "IsRented" }, new String[] { "1" }, "VehicleID = " + vehicleID[i]);
		}
	}

	public void initializePayment() {
		Integer[] RentalID = { 1, 2, 3, 4, 5 };
		BigDecimal[] Amount = {
				new BigDecimal(200),
				new BigDecimal(100),
				new BigDecimal(400),
				new BigDecimal(100),
				new BigDecimal(20)
		};
		for (int i = 0; i < 5; i++) {
			Object[] values = {
					Amount[i],
					RentalID[i]
			};
			addPayment(values);
		}
	}

	public void initializeDrivers() {
		Integer[] RentalID = { 1, 2, 4 };
		Date[] BirthDate = {
				Date.valueOf("1990-01-01"),
				Date.valueOf("2007-01-04"),
				Date.valueOf("1990-01-02")
		};
		String[] TIN = { "001010000", "100000001", "000010003" };
		Object[] values = {
				TIN[0],
				BirthDate[0],
				RentalID[0]
		};
		for (int i = 0; i < RentalID.length; i++) {
			values[0] = TIN[i];
			values[1] = BirthDate[i];
			values[2] = RentalID[i];
			addDriver(values);
		}
	}

	public int displayAvailableCustomers() {
		int customerNumber = 0;
		try (PreparedStatement preparedStatement = connection
				.prepareStatement(DatabaseQueries.getAllCustomers())) {
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				while (resultSet.next()) {
					customerNumber++;
					// Retrieve data from the current row
					String TIN = resultSet.getString("TIN");
					String firstName = resultSet.getString("FirstName");
					String LastName = resultSet.getString("LastName");

					// Print the data
					System.out.println("TIN: " + TIN);
					System.out.println("FirstName: " + firstName);
					System.out.println("LastName: " + LastName);
					System.out.println("--------------------");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (customerNumber == 0) {
			System.out.println("No customers found");
			System.out.println("--------------------");
		}
		return customerNumber;
	}

	public int displayRentedVehicles(String TIN) {
		int vehicleNumber = 0;
		try (PreparedStatement preparedStatement = connection
				.prepareStatement(DatabaseQueries.queryRentedVehicles(TIN))) {
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				while (resultSet.next()) {
					vehicleNumber++;
					// Retrieve data from the current row
					String vehicleID = resultSet.getString("VehicleID");
					String brand = resultSet.getString("Brand");
					String model = resultSet.getString("Model");
					String color = resultSet.getString("Color");

					// Print the data
					System.out.println("VehicleID: " + vehicleID);
					System.out.println("Brand: " + brand);
					System.out.println("Model: " + model);
					System.out.println("Color: " + color);
					System.out.println("--------------------");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (vehicleNumber == 0) {
			System.out.println("No vehicles found");
			System.out.println("--------------------");
		}
		return vehicleNumber;
	}

	public void initializeCreditCards() {
		String[] CreditCardNumber = { "1234567890123456", "1234567890123457", "1234567890123458", "1234567890123459",
				"1234567890123460" };
		String[] ExpirationDate = { "02/29", "02/30", "02/27", "04/28", "02/23" };
		String[] CVV = { "022", "183", "523", "121", "113" };
		String[] TIN = { "000000000", "000000001", "000000002", "000000003", "000000004" };
		for (int i = 0; i < 5; i++) {
			Object[] values = {
					CreditCardNumber[i],
					ExpirationDate[i],
					CVV[i],
					TIN[i]
			};
			addCreditCard(values);
		}
	}

	public int displayAvailableCars() {
		int count = 0;
		try (PreparedStatement preparedStatement = connection
				.prepareStatement(DatabaseQueries.getAvailableCars())) {
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				while (resultSet.next()) {
					count++;
					// Retrieve data from the current row
					String VehicleID = resultSet.getString("VehicleID");
					String brand = resultSet.getString("Brand");
					String model = resultSet.getString("Model");
					String color = resultSet.getString("Color");
					String carType = resultSet.getString("CarType");
					String numberOfPassengers = resultSet.getString("NumberOfPassengers");
					String DailyRentalCost = resultSet.getString("DailyRentalCost");
					String DailyInsuranceCost = resultSet.getString("DailyInsuranceCost");

					// Print the data
					System.out.println("VehicleID: " + VehicleID);
					System.out.println("Brand: " + brand);
					System.out.println("Model: " + model);
					System.out.println("Color: " + color);
					System.out.println("Car Type: " + carType);
					System.out.println("Number of Passengers: " + numberOfPassengers);
					System.out.println("Daily Rental Cost: " + DailyRentalCost);
					System.out.println("Daily Insurance Cost: " + DailyInsuranceCost);
					System.out.println("--------------------");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (count == 0) {
			System.out.println("No cars found");
			System.out.println("--------------------");
		}
		return count;
	}

	public int displayAvailableMotorbikes() {
		int count = 0;
		try (PreparedStatement preparedStatement = connection
				.prepareStatement(DatabaseQueries.getAvailableMotorbikes())) {
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				while (resultSet.next()) {
					count++;
					// Retrieve data from the current row
					String VehicleID = resultSet.getString("VehicleID");
					String brand = resultSet.getString("Brand");
					String model = resultSet.getString("Model");
					String color = resultSet.getString("Color");
					String DailyRentalCost = resultSet.getString("DailyRentalCost");
					String DailyInsuranceCost = resultSet.getString("DailyInsuranceCost");

					// Print the data
					System.out.println("VehicleID: " + VehicleID);
					System.out.println("Brand: " + brand);
					System.out.println("Model: " + model);
					System.out.println("Color: " + color);
					System.out.println("Daily Rental Cost: " + DailyRentalCost);
					System.out.println("Daily Insurance Cost: " + DailyInsuranceCost);
					System.out.println("--------------------");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (count == 0) {
			System.out.println("No Motorbikes found");
			System.out.println("--------------------");
		}
		return count;
	}

	public int displayAvailableScooters() {
		int count = 0;
		try (PreparedStatement preparedStatement = connection
				.prepareStatement(DatabaseQueries.getAvailableScooters())) {
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				while (resultSet.next()) {
					// Retrieve data from the current row
					String VehicleID = resultSet.getString("VehicleID");
					String brand = resultSet.getString("Brand");
					String model = resultSet.getString("Model");
					String color = resultSet.getString("Color");
					String DailyRentalCost = resultSet.getString("DailyRentalCost");
					String DailyInsuranceCost = resultSet.getString("DailyInsuranceCost");

					// Print the data
					System.out.println("VehicleID: " + VehicleID);
					System.out.println("Brand: " + brand);
					System.out.println("Model: " + model);
					System.out.println("Color: " + color);
					System.out.println("Daily Rental Cost: " + DailyRentalCost);
					System.out.println("Daily Insurance Cost: " + DailyInsuranceCost);
					System.out.println("--------------------");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (count == 0) {
			System.out.println("No Scooters found");
			System.out.println("--------------------");
		}
		return count;
	}

	public int displayAvailableBicycles() {
		int count = 0;
		try (PreparedStatement preparedStatement = connection
				.prepareStatement(DatabaseQueries.getAvailableBicycles())) {
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				while (resultSet.next()) {
					// Retrieve data from the current row
					String VehicleID = resultSet.getString("VehicleID");
					String brand = resultSet.getString("Brand");
					String model = resultSet.getString("Model");
					String color = resultSet.getString("Color");
					String DailyRentalCost = resultSet.getString("DailyRentalCost");
					String DailyInsuranceCost = resultSet.getString("DailyInsuranceCost");

					// Print the data
					System.out.println("VehicleID: " + VehicleID);
					System.out.println("Brand: " + brand);
					System.out.println("Model: " + model);
					System.out.println("Color: " + color);
					System.out.println("Daily Rental Cost: " + DailyRentalCost);
					System.out.println("Daily Insurance Cost: " + DailyInsuranceCost);
					System.out.println("--------------------");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (count == 0) {
			System.out.println("No Bicycles found");
			System.out.println("--------------------");
		}
		return count;
	}

	public Double returnAccidentCost(String vID) {
		Double cost = -1.;
		String sql = "SELECT VTBN.DailyRentalCost " +
				"FROM Vehicle_Type_Brand_Model VTBN " +
				" JOIN Vehicle V ON V.VehicleType = VTBN.VehicleType" +
				" AND V.Brand = VTBN.Brand " + "AND V.Model = VTBN.Model " +
				" AND V.VehicleID = " + vID;
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			System.out.println("Executing query" + preparedStatement.toString());

			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				if (resultSet.next()) {
					String costStr = resultSet.getString("DailyRentalCost");
					System.out.println("Cost: " + Double.valueOf(costStr));
					cost = Double.valueOf(costStr);
				} else {
					// throw exception
					throw new SQLException("No cost found for the specified vehicleID.");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (cost == -1) {
			System.out.println("No cost found");
			System.out.println("--------------------");
		}
		return cost;
	}

	public Double returnReturnCost(String vID) {
		Double cost = -1.;
		String sql = "SELECT VTBN.HourlyDelayedCost " +
				"FROM Vehicle_Type_Brand_Model VTBN " +
				" JOIN Vehicle V ON V.VehicleType = VTBN.VehicleType" +
				" AND V.Brand = VTBN.Brand " + "AND V.Model = VTBN.Model " +
				" AND V.VehicleID = " + vID;
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			System.out.println("Executing query" + preparedStatement.toString());

			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				if (resultSet.next()) {
					String costStr = resultSet.getString("HourlyDelayedCost");
					System.out.println("Cost: " + Double.valueOf(costStr));
					cost = Double.valueOf(costStr);
				} else {
					// throw exception
					throw new SQLException("No cost found for the specified vehicleID.");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (cost == -1.) {
			System.out.println("No cost found");
			System.out.println("--------------------");
		}
		return cost;
	}

	public void displayStatusCars() {
		try (PreparedStatement preparedStatement = connection
				.prepareStatement(DatabaseQueries.getStatusCar())) {
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				while (resultSet.next()) {
					// Retrieve data from the current row
					String RegisterationNumber = resultSet.getString("RegisterationNumber");
					String brand = resultSet.getString("Brand");
					String model = resultSet.getString("Model");
					String carType = resultSet.getString("CarType");
					String isRented = resultSet.getString("IsRented");
					String needMaintenance = resultSet.getString("needMaintenance");
					String needRepair = resultSet.getString("needRepair");

					// Print the data
					System.out.println("RegisterationNumber: " + RegisterationNumber);
					System.out.println("Brand: " + brand);
					System.out.println("Model: " + model);
					System.out.println("CarType: " + carType);
					System.out.println("IsRented: " + isRented);
					System.out.println("needMaintenance: " + needMaintenance);
					System.out.println("needRepair: " + needRepair);
					System.out.println("--------------------");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void displayStatusMotorbikes() {
		try (PreparedStatement preparedStatement = connection
				.prepareStatement(DatabaseQueries.getStatusMotorbike())) {
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				while (resultSet.next()) {
					// Retrieve data from the current row
					String RegisterationNumber = resultSet.getString("RegisterationNumber");
					String brand = resultSet.getString("Brand");
					String model = resultSet.getString("Model");
					String isRented = resultSet.getString("IsRented");
					String needMaintenance = resultSet.getString("needMaintenance");
					String needRepair = resultSet.getString("needRepair");

					// Print the data
					System.out.println("RegisterationNumber: " + RegisterationNumber);
					System.out.println("Brand: " + brand);
					System.out.println("Model: " + model);
					System.out.println("IsRented: " + isRented);
					System.out.println("needMaintenance: " + needMaintenance);
					System.out.println("needRepair: " + needRepair);
					System.out.println("--------------------");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void displayStatusScooters() {
		try (PreparedStatement preparedStatement = connection
				.prepareStatement(DatabaseQueries.getStatusScooter())) {
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				while (resultSet.next()) {
					// Retrieve data from the current row
					String UniqueNumber = resultSet.getString("UniqueNumber");
					String brand = resultSet.getString("Brand");
					String model = resultSet.getString("Model");
					String isRented = resultSet.getString("IsRented");
					String needMaintenance = resultSet.getString("needMaintenance");
					String needRepair = resultSet.getString("needRepair");

					// Print the data
					System.out.println("UniqueNumber: " + UniqueNumber);
					System.out.println("Brand: " + brand);
					System.out.println("Model: " + model);
					System.out.println("IsRented: " + isRented);
					System.out.println("needMaintenance: " + needMaintenance);
					System.out.println("needRepair: " + needRepair);
					System.out.println("--------------------");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void displayStatusBicycles() {
		try (PreparedStatement preparedStatement = connection
				.prepareStatement(DatabaseQueries.getStatusBicycle())) {
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				while (resultSet.next()) {
					// Retrieve data from the current row
					String UniqueNumber = resultSet.getString("UniqueNumber");
					String brand = resultSet.getString("Brand");
					String model = resultSet.getString("Model");
					String isRented = resultSet.getString("IsRented");
					String needMaintenance = resultSet.getString("needMaintenance");
					String needRepair = resultSet.getString("needRepair");

					// Print the data
					System.out.println("UniqueNumber: " + UniqueNumber);
					System.out.println("Brand: " + brand);
					System.out.println("Model: " + model);
					System.out.println("IsRented: " + isRented);
					System.out.println("needMaintenance: " + needMaintenance);
					System.out.println("needRepair: " + needRepair);
					System.out.println("--------------------");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void displayRentalsStatus(String start_t, String end_t) {
		try (PreparedStatement preparedStatement = connection
				.prepareStatement(DatabaseQueries.rentalsStatus(start_t, end_t))) {
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				while (resultSet.next()) {
					// Retrieve data from the current row
					String RentalID = resultSet.getString("RentalID");
					String ReturnDateTime = resultSet.getString("ReturnDateTime");
					String DeliveryDateTime = resultSet.getString("DeliveryDateTime");
					String RentalStatus = resultSet.getString("RentalStatus");

					// Print the data
					System.out.println("RentalID: " + RentalID);
					System.out.println("ReturnDateTime: " + ReturnDateTime);
					System.out.println("DeliveryDateTime: " + DeliveryDateTime);
					System.out.println("RentalStatus: " + RentalStatus);
					System.out.println("--------------------");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void displayMaxMinAvgRentalDuration() {
		try (PreparedStatement preparedStatement = connection
				.prepareStatement(DatabaseQueries.getMaxMinAvgRentalDuration())) {
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				while (resultSet.next()) {
					// Retrieve data from the current row
					String VehicleType = resultSet.getString("VehicleType");
					String MaxDuration = resultSet.getString("MaxDuration");
					String MinDuration = resultSet.getString("MinDuration");
					String AvgDuration = resultSet.getString("AvgDuration");

					// Print the data
					System.out.println("VehicleType: " + VehicleType);
					System.out.println("MaxDuration: " + MaxDuration);
					System.out.println("MinDuration: " + MinDuration);
					System.out.println("AvgDuration: " + AvgDuration);
					System.out.println("--------------------");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void displayRevenueCategoryPeriod(String VehicleType) {
		try (PreparedStatement preparedStatement = connection
				.prepareStatement(DatabaseQueries.RevenueCategoryPeriod(VehicleType))) {
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				while (resultSet.next()) {
					// Retrieve data from the current row
					String vehicleType = resultSet.getString("VehicleType");
					String TimePeriod = resultSet.getString("TimePeriod");
					String RentalIncome = resultSet.getString("RentalIncome");

					// Print the data
					System.out.println("VehicleType: " + vehicleType);
					System.out.println("TimePeriod: " + TimePeriod);
					System.out.println("RentalIncome: " + RentalIncome);
					System.out.println("--------------------");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void displayTotalServiceCosts(String VehicleType) {
		try (PreparedStatement preparedStatement = connection
				.prepareStatement(DatabaseQueries.TotalServiceCosts(VehicleType))) {
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				while (resultSet.next()) {
					// Retrieve data from the current row
					String TimePeriod = resultSet.getString("TimePeriod");
					String TotalCost = resultSet.getString("TotalCost");

					// Print the data
					System.out.println("TimePeriod: " + TimePeriod);
					System.out.println("TotalCost: " + TotalCost);
					System.out.println("--------------------");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void displayMostPopularVehicle() {
		try (PreparedStatement preparedStatement = connection
				.prepareStatement(DatabaseQueries.getMostPopularVehicle())) {
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				while (resultSet.next()) {
					// Retrieve data from the current row
					String VehicleType = resultSet.getString("VehicleType");
					String Brand = resultSet.getString("Brand");
					String Model = resultSet.getString("Model");
					String NumberOfRentals = resultSet.getString("NumberOfRentals");

					// Print the data
					System.out.println("VehicleType: " + VehicleType);
					System.out.println("Brand: " + Brand);
					System.out.println("Model: " + Model);
					System.out.println("NumberOfRentals: " + NumberOfRentals);
					System.out.println("--------------------");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void displayCustomQuery(String query) {
		try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				ResultSetMetaData metaData = resultSet.getMetaData();
				int columnCount = metaData.getColumnCount();

				while (resultSet.next()) {
					for (int i = 1; i <= columnCount; i++) {
						String columnName = metaData.getColumnName(i);
						String columnValue = resultSet.getString(i);

						System.out.println(columnName + ": " + columnValue);
					}
					System.out.println("--------------------");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void completeServices(String CurrentDate) {
		try (PreparedStatement preparedStatement = connection
				.prepareStatement(DatabaseQueries.checkServiceComplete(CurrentDate))) {
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				while (resultSet.next()) {
					// Retrieve data from the current row
					String ServiceID = resultSet.getString("ServiceID");
					String VehicleID = resultSet.getString("VehicleID");
					String ServiceType = resultSet.getString("ServiceType");

					updateTuple("Service", new String[] { "IsComplete" }, new String[] { "1" },
							"ServiceID = " + ServiceID);

					if (ServiceType.equals("Maintenance")) {
						updateTuple("Vehicle", new String[] { "needMaintenance" }, new String[] { "0" },
								"VehicleID = " + VehicleID);
						System.out.println("Maintenance of Vehicle " + VehicleID + " has been completed");
					} else {
						updateTuple("Vehicle", new String[] { "needRepair" }, new String[] { "0" },
								"VehicleID = " + VehicleID);
						System.out.println("Repair of Vehicle " + VehicleID + " has been completed");
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
