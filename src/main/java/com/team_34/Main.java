package com.team_34;

import java.util.Scanner;
import com.team_34.model.DatabaseModel;
import com.team_34.controller.DatabaseController;
import com.team_34.utilities.DatabaseQueries;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.time.Duration;
import java.time.Instant;

public class Main {
	private static Scanner scanner = new Scanner(System.in);

	public static void main(String args[]) {
		DatabaseModel databaseModel = new DatabaseModel();
		DatabaseController databaseController = new DatabaseController(databaseModel);

		while (true) {
			displayOptions();
			System.out.print("Enter the number of your choice: ");
			int selectedOption = getUserInt();
			System.out.println();

			switch (selectedOption) {
				case 1:
					registerNewCustomer(databaseModel);
					break;
				case 2:
					supplyVehicles(databaseModel);
					break;
				case 3:
					rentVehicle(databaseModel);
					break;
				case 4:
					returnVehicle(databaseModel);
					break;
				case 5:
					reportVehicleDamage(databaseModel);
					break;
				case 6:
					reportAccident(databaseModel);
					break;
				case 7:
					checkService(databaseModel);
					break;
				case 8:
					selectQueries(databaseModel);
					break;
				case 9:
					System.out.println("Exiting...");
					System.exit(0);
					break;
				default:
					System.out.println("Invalid option selected. Exiting.");
			}
		}
	}

	private static int getUserInt() {
		int result = scanner.nextInt();
		scanner.nextLine();

		return result;
	}

	private static void displayOptions() {
		System.out.println("Select an option by entering the corresponding number:");
		System.out.println("1. Register New Customer");
		System.out.println("2. Supply Vehicles");
		System.out.println("3. Rent Vehicle");
		System.out.println("4. Return Vehicle");
		System.out.println("5. Report Vehicle Damage");
		System.out.println("6. Report Accident");
		System.out.println("7. Check Service");
		System.out.println("8. Queries");
		System.out.println("9. Exit");
	}

	private static void displayQueries() {
		System.out.println("Select an option by entering the corresponding number:");
		System.out.println("1. Status of vehicles per category");
		System.out.println("2. Rentals status per time period");
		System.out.println("3. Max, Min and Average rental duration per time period");
		System.out.println("4. Reveune per time period and vehicle category");
		System.out.println("5. Total service costs per time period");
		System.out.println("6. Most popular vehicle per category");
		System.out.println("7. Custom query");
	}

	private static void supplyVehicles(DatabaseModel databasemodel) {
		System.out.println("Supply Vehicles...");

		System.out.print("Enter VehicleType (Car,Motorbike,Bicycle,Scooter):");
		String vehicleType = scanner.nextLine();

		System.out.print("Enter Brand Name: ");
		String brand = scanner.nextLine();

		System.out.print("Enter Model Name: ");
		String model = scanner.nextLine();

		System.out.print("Enter Color: ");
		String color = scanner.nextLine();

		System.out.print("Enter Daily Rental Cost (Decimal): ");
		String dailyRentalCost = scanner.nextLine();

		System.out.print("Enter Daily Insurance Cost (Decimal): ");
		String dailyInsuranceCost = scanner.nextLine();

		System.out.print("Enter Cost per delayed hour: ");
		String hourlyDelayedCost = scanner.nextLine();

		String registrationNumber = "";
		String uniqueNumber = "";
		Integer numberOfPassengers = -1;
		String carType = "";

		System.out.print("Enter Range: ");
		String rangeInKilometers = scanner.nextLine();

		if (vehicleType.equals("Car") || vehicleType.equals("Motorbike")) {
			System.out.print("Enter Registration Number (8 characters ^[A-Z]{3}-[0-9]{4}):");
			registrationNumber = scanner.nextLine();
			if (vehicleType.equals("Car")) {
				System.out.print("Enter Car Type:");
				carType = scanner.nextLine();

				System.out.print("Enter number of seats:");
				numberOfPassengers = scanner.nextInt();
				scanner.nextLine();
			}
		} else {
			System.out.print("Enter uniqueNumber Number (5 characters):");
			uniqueNumber = scanner.nextLine();
		}

		Object[] typeValues = {
				vehicleType,
				brand,
				model,
				BigDecimal.valueOf(Double.parseDouble(dailyRentalCost)),
				BigDecimal.valueOf(Double.parseDouble(dailyInsuranceCost)),
				BigDecimal.valueOf(Double.parseDouble(hourlyDelayedCost)),
				Integer.parseInt(rangeInKilometers)
		};
		Object[] vehicleVal = { "", vehicleType, brand, model, color };
		Object[] carValues = { "", carType, numberOfPassengers };
		Object[] registeredVals = { "", registrationNumber };
		Object[] unregisteredVals = { "", uniqueNumber };

		databasemodel.addVehicle(typeValues, vehicleVal, registeredVals, unregisteredVals, carValues);
	}

	private static void rentVehicle(DatabaseModel databaseModel) {
		System.out.println("Rent a vehicle:");

		if (databaseModel.displayAvailableCustomers() == 0)
			return;

		System.out.println("Select a TIN:");
		String TIN = scanner.nextLine();

		System.out.print("Select a number: \n 1.Car \n 2.Motorbike \n 3.Bicycle \n 4.Scooter \n");
		String select = scanner.nextLine();

		switch (select) {
			case "1":
				if (databaseModel.displayAvailableCars() == 0)
					return;

				break;
			case "2":
				if (databaseModel.displayAvailableMotorbikes() == 0)
					return;
				break;
			case "3":
				if (databaseModel.displayAvailableScooters() == 0)
					return;
				break;
			case "4":
				if (databaseModel.displayAvailableBicycles() == 0)
					return;
				databaseModel.displayAvailableBicycles();
				break;
			default:
				System.out.println("Invalid option selected. Exiting.");
				return;
		}

		System.out.print("Select Vehicle ID: ");
		int vehicleID = getUserInt();

		System.out.print("Is the customer the driver (yes/no): ");
		String customerDriverInput = scanner.nextLine();
		Boolean isCustomerDriver = customerDriverInput.equalsIgnoreCase("yes");

		String birthDate = "";
		String d_tin = "";
		if (!isCustomerDriver) {
			System.out.print("Enter Driver's TIN (9 digit) : ");
			d_tin = scanner.nextLine();

			System.out.print("Enter Driver's Birth Date (YYYY-MM-DD): ");
			birthDate = scanner.nextLine();
		}

		String driverLicenceNumber = "";
		if (select.equals("1") || select.equals("2")) {
			System.out.print("Enter Driver's Licence Number (5 digit): ");
			driverLicenceNumber = scanner.nextLine();
		}

		System.out.print("Enter Delivery Date (YYYY-MM-DD HH:MM:SS):");
		String deliveryDate = scanner.nextLine();

		System.out.print("Enter Return Date (YYYY-MM-DD HH:MM:SS):");
		String returnDate = scanner.nextLine();

		System.out.print("Buy insurance (yes/no): ");
		String insuranceInput = scanner.nextLine();
		boolean hasInsurance = insuranceInput.equalsIgnoreCase("yes");

		// Perform registration logic (e.g., database insertion)
		System.out.println("Rental is trying to enter database");
		Object[] values_r = { "", Timestamp.valueOf(returnDate), null, Timestamp.valueOf(deliveryDate), hasInsurance,
				TIN,
				vehicleID };

		databaseModel.addRental(values_r, new Object[] { "", driverLicenceNumber });
		databaseModel.updateTuple("Vehicle", new String[] { "IsRented" }, new String[] { "1" },
				"VehicleID = " + vehicleID);

		if (!isCustomerDriver) {
			Object[] values_d = { d_tin, Date.valueOf(birthDate), databaseModel.getRentalID() };
			databaseModel.addDriver(values_d);
		}

	}

	private static void returnVehicle(DatabaseModel databaseModel) {
		System.out.println("Return a vehicle:");

		if (databaseModel.displayAvailableCustomers() == 0)
			return;

		System.out.print("Select a customer TIN: ");
		String TIN = scanner.nextLine();

		if (databaseModel.displayRentedVehicles(TIN) == 0)
			return;

		System.out.print("Select a VehicleID: ");
		String vID = scanner.nextLine();

		System.out.print("Enter Returned Date (YYYY-MM-DD HH:MM:SS): ");
		String returnedD = scanner.nextLine();

		String[] columnsV = { "IsRented" };
		String[] valuesV = { "0" };
		databaseModel.updateTuple("Vehicle", columnsV, valuesV, "VehicleID = " + vID);

		String returnStr = databaseModel.readTuple("Rental", new String[] { "ReturnDateTime" }, "VehicleID = " + vID)
				.get(0).get(0);

		Double hourlyDelayedCost = databaseModel.returnReturnCost(vID);

		String[] columnsR = { "hasReturned", "ReturnedDateTime" };
		String[] valuesR = { "1", returnedD };
		databaseModel.updateTuple("Rental", columnsR, valuesR, "VehicleID = " + vID);

		Timestamp returnedTimestamp = Timestamp.valueOf(returnedD);
		System.out.println("return Timestamp: " + returnStr);
		Timestamp returnTs = Timestamp.valueOf(returnStr);
		// Calculate the difference in hours
		long hoursDifference = calculateHoursDifference(returnTs, returnedTimestamp);

		// Print the result
		System.out.println("Difference in hours: " + hoursDifference);

		if (hoursDifference <= 0)
			return;

		Double amount = hoursDifference * (hourlyDelayedCost);

		String rentalID = databaseModel.readTuple("Rental", new String[] { "RentalID" }, "VehicleID = " + vID)
				.get(0)
				.get(0);

		Object[] paymentVal = {
				BigDecimal.valueOf(amount),
				Integer.valueOf(rentalID)
		};

		System.out.println("Charged Amount = " + amount);
		databaseModel.addPayment(paymentVal);

	}

	private static long calculateHoursDifference(Timestamp timestamp1, Timestamp timestamp2) {
		// Convert Timestamp to Instant
		Instant instant1 = timestamp1.toInstant();
		Instant instant2 = timestamp2.toInstant();

		// Calculate the duration between the two Instants
		Duration duration = Duration.between(instant1, instant2);

		// Convert duration to hours
		long hoursDifference = duration.toHours();

		return hoursDifference;
	}

	private static void reportVehicleDamage(DatabaseModel databaseModel) {

		System.out.println("Report Vehicle Damage...");

		if (databaseModel.displayAvailableCustomers() == 0)
			return;

		System.out.print("Select a customer TIN: ");
		String TIN = scanner.nextLine();

		if (databaseModel.displayRentedVehicles(TIN) == 0)
			return;

		System.out.print("Select a VehicleID: ");
		String vID = scanner.nextLine();

		databaseModel.updateTuple("Vehicle", new String[] { "isRented" }, new String[] { "0" },
				"VehicleID = " + vID);
		databaseModel.updateTuple("Rental", new String[] { "hasDamage" }, new String[] { "1" },
				"VehicleID = " + vID);
		databaseModel.updateTuple("Rental", new String[] { "hasReturned" }, new String[] { "1" },
				"VehicleID = " + vID);

		System.out.print("Select an enter date for maintenance: (YYYY-MM-DD) ");
		String enterDate = scanner.nextLine();

		databaseModel.updateTuple("Vehicle", new String[] { "needMaintenance" }, new String[] { "1" },
				"VehicleID = " + vID);

		Object[] serviceVals = {
				BigDecimal.valueOf(20.00),
				Date.valueOf(enterDate),
				false,
				"Maintenance",
				Integer.valueOf(vID)
		};
		databaseModel.addService(serviceVals);

		System.out.println("Vehicle with ID " + vID + " is substituted");
	}

	private static void reportAccident(DatabaseModel databaseModel) {

		System.out.println("Executing Report Accident...");

		if (databaseModel.displayAvailableCustomers() == 0)
			return;

		System.out.print("Select a customer TIN: ");
		String TIN = scanner.nextLine();

		if (databaseModel.displayRentedVehicles(TIN) == 0)
			return;

		System.out.print("Select a VehicleID: ");
		String vID = scanner.nextLine();

		String hasInsurance = databaseModel
				.readTuple("Rental", new String[] { "hasInsurance" }, "VehicleID = " + vID)
				.get(0).get(0);

		databaseModel.updateTuple("Rental", new String[] { "hadAccident" }, new String[] { "1" },
				"VehicleID = " + vID);
		databaseModel.updateTuple("Rental", new String[] { "hasReturned" }, new String[] { "1" },
				"VehicleID = " + vID);

		Double dailyRentalCost = databaseModel.returnAccidentCost(vID);

		Double cost = -1.;

		System.out.print("Select an enter date: (YYYY-MM-DD) ");
		String enterDate = scanner.nextLine();

		Object[] serviceVals = {
				BigDecimal.valueOf(50.00),
				Date.valueOf(enterDate),
				false,
				"Repair",
				Integer.valueOf(vID)
		};
		databaseModel.addService(serviceVals);

		if (hasInsurance.equals("1"))
			return;

		cost = (dailyRentalCost) * 3;
		System.out.println("Cost of vehicleID " + vID + "is " + cost);

		String rentalID = databaseModel.readTuple("Rental", new String[] { "RentalID" }, "VehicleID = " + vID)
				.get(0)
				.get(0);

		Object[] paymentVal = {
				BigDecimal.valueOf(cost),
				Integer.valueOf(rentalID),
		};
		databaseModel.addPayment(paymentVal);

		databaseModel.updateTuple("Vehicle", new String[] { "needRepair" }, new String[] { "1" },
				"VehicleID = " + vID);

	}

	private static void checkService(DatabaseModel databaseModel) {
		System.out.print("Enter Current Date (YYYY-MM-DD): ");
		String date = scanner.nextLine();

		databaseModel.completeServices(date);
	}

	private static void registerNewCustomer(DatabaseModel databaseModel) {
		System.out.println("Register New Customer:");

		System.out.print("Enter TIN (9 digit) : ");
		String tin = scanner.nextLine();

		System.out.print("Enter First Name: ");
		String firstName = scanner.nextLine();

		System.out.print("Enter Last Name: ");
		String lastName = scanner.nextLine();

		System.out.print("Enter City: ");
		String city = scanner.nextLine();

		System.out.print("Enter Street: ");
		String street = scanner.nextLine();

		System.out.print("Enter House Number (3 digits): ");
		String houseNumber = scanner.nextLine();

		System.out.print("Enter Postal Code (5 digits): ");
		String postalCode = scanner.nextLine();

		System.out.print("Enter Birth Date (YYYY-MM-DD): ");
		String birthDate = scanner.nextLine();

		System.out.println("Customer is trying to enter database");
		Object[] values_c = { tin, firstName, lastName, city, street, houseNumber, postalCode,
				Date.valueOf(birthDate) };

		databaseModel.addCustomer(values_c);

		do {
			System.out.println("Enter Credit Card Number (16 digits): ");
			String creditCardNumber = scanner.nextLine();

			System.out.println("Enter Card's Expiration date (MM/YY): ");
			String expirationDate = scanner.nextLine();

			System.out.println("Enter Card's CCV (3 digits): ");
			String CCV = scanner.nextLine();

			System.out.println("Credit Card is trying to enter database");
			Object[] values_cc = { creditCardNumber, expirationDate, CCV, tin };
			databaseModel.addCreditCard(values_cc);

			System.out.print("Do you want to add another Credit Card (yes/no): ");
			String anotherCC = scanner.nextLine();
			boolean hasAnotherCC = anotherCC.equalsIgnoreCase("yes");
			if (!hasAnotherCC)
				break;
		} while (true);
	}

	public static void selectQueries(DatabaseModel databaseModel) {
		displayQueries();
		System.out.print("Enter the number of your choice: ");
		int selectedOption = getUserInt();
		System.out.println();

		switch (selectedOption) {
			case 1:
				selectStatusType(databaseModel);
				break;
			case 2:
				System.out.println("Choose start time (YYYY-MM-DD HH:MM:SS): ");
				String start_time = scanner.nextLine();
				System.out.println("Choose end time (YYYY-MM-DD HH:MM:SS): ");
				String end_time = scanner.nextLine();
				databaseModel.displayRentalsStatus(start_time, end_time);
				break;
			case 3:
				databaseModel.displayMaxMinAvgRentalDuration();
				break;
			case 4:
				System.out.println("Choose a Vehicle type (Car, Motorbike, Scooter, Bicycle): ");
				String VehicleType1 = scanner.nextLine();
				databaseModel.displayRevenueCategoryPeriod(VehicleType1);
				break;
			case 5:
				System.out.println("Choose a Vehicle type (Car, Motorbike, Scooter, Bicycle): ");
				String VehicleType2 = scanner.nextLine();
				databaseModel.displayTotalServiceCosts(VehicleType2);
				break;
			case 6:
				databaseModel.displayMostPopularVehicle();
				break;
			case 7:
				System.out.println("Input a query ");
				String query = scanner.nextLine();
				databaseModel.displayCustomQuery(query);
				break;
			default:
				System.out.println("Invalid option selected. Exiting.");
				break;
		}
	}

	public static void selectStatusType(DatabaseModel databaseModel) {
		System.out.println("Select Status Type...");
		System.out.println("1. Cars");
		System.out.println("2. Motorbikes");
		System.out.println("3. Scooters");
		System.out.println("4. Bicycles");

		System.out.print("Enter the number of your choice: ");
		int selectedOption = getUserInt();
		System.out.println();

		switch (selectedOption) {
			case 1:
				databaseModel.displayStatusCars();
				break;
			case 2:
				databaseModel.displayAvailableMotorbikes();
				break;
			case 3:
				databaseModel.displayAvailableScooters();
				break;
			case 4:
				databaseModel.displayAvailableBicycles();
				break;
			default:
				System.out.println("Invalid option selected. Exiting.");
				break;
		}
	}
}
