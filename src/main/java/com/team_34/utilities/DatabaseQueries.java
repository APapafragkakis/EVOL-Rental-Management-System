package com.team_34.utilities;

public class DatabaseQueries {
	/*
	 * SELECT r.RegisterationNumber,
	 * v.Brand,
	 * v.Model,
	 * c.CarType,
	 * v.IsRented,
	 * v.needMaintenance,
	 * v.needRepair
	 * FROM Vehicle v
	 * JOIN Car c ON v.VehicleID = c.VehicleID
	 * JOIN Registered_Vehicle r ON v.VehicleID = r.VehicleID
	 */
	private static String rentedCars = "SELECT" +
			" r.RegisterationNumber," +
			"v.Brand," +
			" v.Model," +
			" c.CarType," +
			" v.IsRented," +
			"v.needMaintenance," +
			"v.needRepair" +
			" FROM Vehicle v" +
			" JOIN Car c ON v.VehicleID = c.VehicleID" +
			" JOIN Registered_Vehicle r ON c.VehicleID = r.VehicleID";
	/*
	 * SELECT c.RegisterationNumber,
	 * vtbm.Brand,
	 * vtbm.Model,
	 * v.IsRented,
	 * v.needMaintenance,
	 * v.needRepair
	 * FROM Vehicle v
	 * JOIN Vehicle_Type_Brand_Model vtbm ON v.VehicleID = vtbm.VehicleID
	 * WHERE vtbm.VehicleType = 'Motorbike';
	 */
	private static String rentedMotorbikes = "SELECT c.RegisterationNumber," +
			"v.Brand," +
			"vtbm.Model," +
			"v.IsRented," +
			"v.needMaintenance," +
			"v.needRepair" +
			" FROM Vehicle v" +
			" JOIN Vehicle_Type_Brand_Model vtbm ON v.VehicleID = vtbm.VehicleID" +
			" WHERE vtbm.VehicleType = 'Motorbike'";

	/*
	 * --Scooter
	 * SELECT c.UniqueueNumber,
	 * vtbm.Brand,
	 * vtbm.Model,
	 * v.IsRented,
	 * v.needMaintenance,
	 * v.needRepair
	 * FROM Vehicle v
	 * JOIN Vehicle_Type_Brand_Model vtbm ON v.VehicleID = vtbm.VehicleID
	 * WHERE vtbm.VehicleType = 'Scooter';
	 */
	private static String rentedScouters = "SELECT c.UniqueueNumber," +
			"v.Brand," +
			" v.Model," +
			" v.IsRented," +
			"v.needMaintenance," +
			"v.needRepair" +
			" FROM Vehicle v" +
			" JOIN Vehicle_Type_Brand_Model vON v.VehicleID = v.VehicleID" +
			" WHERE v.VehicleType = 'Scooter'";

	/*
	 * --Select all customers
	 *
	 * SELECT FirstName,
	 * LastName,
	 * City,
	 * BirthDate
	 * FROM Customer;
	 */
	private static String allCustomers = "SELECT FirstName," +
			" LastName," +
			" TIN" +
			" FROM Customer";

	// getters
	public static String getAllCustomers() {
		return allCustomers;
	}

	public static String queryRentedVehicles(String TIN) {
		return "SELECT" +
				" v.VehicleID," +
				" v.Brand," +
				" v.Model," +
				" v.Color" +
				" FROM Vehicle v" +
				" JOIN Rental r ON v.VehicleID = r.VehicleID" +
				" JOIN Customer c ON c.TIN = r.TIN" +
				" WHERE c.TIN = '" + TIN + "'" + "AND v.IsRented = 1 AND r.hasReturned = 0";
	}

	/*
	 * --Bicycle
	 * SELECT c.UniqueueNumber,
	 * vtbm.Brand,
	 * vtbm.Model,
	 * v.IsRented,
	 * v.needMaintenance,
	 * v.needRepair
	 * FROM Vehicle v
	 * JOIN Vehicle_Type_Brand_Model vtbm ON v.VehicleID = vtbm.VehicleID
	 * WHERE vtbm.VehicleType = 'Bicycle';
	 */
	private static String rentedBicycles = "SELECT c.UniqueueNumber," +
			" vtbm.Brand," +
			" vtbm.Model," +
			" v.IsRented," +
			" v.needMaintenance," +
			" v.needRepair" +
			" FROM Vehicle v" +
			" JOIN Vehicle_Type_Brand_Model vtbm ON v.VehicleID = vtbm.VehicleID" +
			" WHERE v.VehicleType = 'Bicycle'";

	private static String availableCars = "SELECT" +
			" v.VehicleID," +
			" v.Brand," +
			" v.Model," +
			" v.Color," +
			" c.NumberOfPassengers," +
			" c.CarType," +
			" vtbm.DailyRentalCost," +
			" vtbm.DailyInsuranceCost" +
			" FROM Vehicle v" +
			" JOIN Registered_Vehicle r ON v.VehicleID = r.VehicleID" +
			" JOIN Vehicle_Type_Brand_Model vtbm ON v.VehicleType = vtbm.VehicleType" +
			" AND v.Brand = vtbm.Brand " + "AND v.Model = vtbm.Model" +
			" JOIN Car c ON c.VehicleID = r.VehicleID" +
			" WHERE v.IsRented = 0 AND v.needRepair = 0 AND v.needMaintenance = 0";

	private static String availableMotorbikes = "SELECT" +
			" v.VehicleID," +
			" v.Brand," +
			" v.Model," +
			" v.Color," +
			" vtbm.DailyRentalCost," +
			" vtbm.DailyInsuranceCost" +
			" FROM Vehicle v" +
			" JOIN Vehicle_Type_Brand_Model vtbm ON v.VehicleType = vtbm.VehicleType" +
			" AND v.Brand = vtbm.Brand " + "AND v.Model = vtbm.Model" +
			" WHERE v.VehicleType = 'Motorbike'" +
			" AND v.IsRented = 0 AND v.needRepair = 0 AND v.needMaintenance = 0";

	private static String availableScooters = "SELECT" +
			" v.VehicleID," +
			" v.Brand," +
			" v.Model," +
			" v.Color," +
			" vtbm.DailyRentalCost," +
			" vtbm.DailyInsuranceCost" +
			" FROM Vehicle v" +
			" JOIN Vehicle_Type_Brand_Model vtbm ON v.VehicleType = vtbm.VehicleType" +
			" AND v.Brand = vtbm.Brand " + "AND v.Model = vtbm.Model" +
			" WHERE vtbm.VehicleType = 'Scooter'" +
			" AND v.IsRented = 0 AND v.needRepair = 0 AND v.needMaintenance = 0";

	private static String availableBicycles = "SELECT" +
			" v.VehicleID," +
			" v.Brand," +
			" v.Model," +
			" v.Color," +
			" vtbm DailyRentalCost," +
			" vtbm DailyInsuranceCost" +
			" FROM Vehicle v" +
			" JOIN Vehicle_Type_Brand_Model vtbm ON v.VehicleType = vtbm.VehicleType" +
			" AND v.Brand = vtbm.Brand " + "AND v.Model = vtbm.Model" +
			" WHERE vtbm.VehicleType = 'Bicycle'" +
			" AND v.IsRented = 0 AND v.needRepair = 0 AND v.needMaintenance = 0";

	public static String queryDATETIME_Diff(String column1, String column2) {
		return "SELECT TIMESTAMPDIFF(HOUR, " + "'" + column1 + "'" + "," + "'" + column1 + "'" +
				") AS time_difference_hours";
	}

	// getters rented
	public static String getRentedCars() {
		return rentedCars;
	}

	public static String getRentedMotorbikes() {
		return rentedMotorbikes;
	}

	public static String getRentedScouters() {
		return rentedScouters;
	}

	public static String getRentedBicycles() {
		return rentedBicycles;
	}

	// getters available
	public static String getAvailableCars() {
		return availableCars;
	}

	public static String getAvailableMotorbikes() {
		return availableMotorbikes;
	}

	public static String getAvailableScooters() {
		return availableScooters;
	}

	public static String getAvailableBicycles() {
		return availableBicycles;
	}

	// Status of vehicles per category
	// Car
	private static String statusCar = "SELECT r.RegisterationNumber," +
			" v.Brand," +
			" v.Model," +
			" c.CarType," +
			" v.IsRented," +
			" v.needMaintenance," +
			" v.needRepair" +
			"	FROM Vehicle v" +
			"	JOIN Car c ON v.VehicleID = c.VehicleID" +
			" JOIN Registered_Vehicle r ON v.VehicleID = r.VehicleID";

	// Motorbike
	private static String statusMotorbike = "SELECT r.RegisterationNumber," +
			" v.Brand," +
			" v.Model," +
			" v.IsRented," +
			" v.needMaintenance," +
			" v.needRepair" +
			" FROM Vehicle v" +
			" JOIN Registered_Vehicle r ON v.VehicleID = r.VehicleID" +
			" WHERE v.VehicleType = 'Motorbike'";

	// Scooter
	private static String statusScooter = "SELECT u.UniqueNumber," +
			" v.Brand," +
			" v.Model," +
			" v.IsRented," +
			" v.needMaintenance," +
			" v.needRepair" +
			" FROM Vehicle v" +
			" JOIN Unregistered_Vehicle u ON v.VehicleID = u.VehicleID" +
			" WHERE v.VehicleType = 'Scooter'";

	// Bicycle
	private static String statusBicycle = "SELECT u.UniqueNumber," +
			" v.Brand," +
			" v.Model," +
			" v.IsRented," +
			" v.needMaintenance," +
			" v.needRepair" +
			" FROM Vehicle v" +
			" JOIN Unregistered_Vehicle u ON v.VehicleID = u.VehicleID" +
			" WHERE v.VehicleType = 'Bicycle'";

	// rentals status per time period
	public static String rentalsStatus(String strat_t, String finish_t) {
		return "SELECT" +
				" r.RentalID," +
				" r.ReturnDateTime," +
				" r.DeliveryDateTime," +
				" CASE" +
				" WHEN r.hasReturned = TRUE THEN 'Returned'" +
				" ELSE 'Not Returned'" +
				" END AS RentalStatus" +
				" FROM Rental r" +
				" WHERE r.DeliveryDateTime BETWEEN" + "'" + strat_t + "'" + "AND" + "'" + finish_t + "'";
	}

	// Max, Min and Average rental duration per time period
	private static String MaxMinAvgRentalDuration = "SELECT" +
			" v.VehicleType," +
			" MAX(TIMESTAMPDIFF(HOUR, r.DeliveryDateTime, r.ReturnDateTime)) AS MaxDuration," +
			" MIN(TIMESTAMPDIFF(HOUR, r.DeliveryDateTime, r.ReturnDateTime)) AS MinDuration," +
			" AVG(TIMESTAMPDIFF(HOUR, r.DeliveryDateTime, r.ReturnDateTime)) AS AvgDuration" +
			" FROM Rental r" +
			" JOIN Vehicle v ON r.VehicleID = v.VehicleID" +
			" GROUP BY v.VehicleType";

	// Reveune per time period and vehicle category
	public static String RevenueCategoryPeriod(String VehicleType) {
		return "SELECT" +
				" v.VehicleType," +
				" DATE_FORMAT(r.ReturnDateTime, '%Y-%m') AS TimePeriod," +
				" SUM(p.Amount) AS RentalIncome" +
				" FROM Rental r" +
				" JOIN Payment p ON r.RentalID = p.RentalID" +
				" JOIN Vehicle v ON r.VehicleID = v.VehicleID" +
				" AND v.VehicleType = " + "'" + VehicleType + "'" +
				" GROUP BY v.VehicleType, TimePeriod";
	}

	// Total service costs per time period
	public static String TotalServiceCosts(String VehicleType) {
		return "SELECT" +
				" DATE_FORMAT(s.EnterDate, '%Y-%m') AS TimePeriod," +
				" SUM(s.Cost) AS TotalCost" +
				" FROM Service s" +
				" JOIN Vehicle v ON s.VehicleID = v.VehicleID" +
				" WHERE s.EnterDate BETWEEN :start_date AND :end_date" +
				" AND v.VehicleType = " + "'" + VehicleType + "'" +
				" GROUP BY v.VehicleType";
	}

	// Most popular vehicle per category
	private static String MostPopularVehicle = "SELECT" +
			" v.VehicleType," +
			" v.Brand," +
			" v.Model," +
			" COUNT(r.RentalID) AS NumberOfRentals" +
			" FROM Rental r" +
			" JOIN Vehicle v ON r.VehicleID = v.VehicleID" +
			" GROUP BY v.VehicleType, v.Brand, v.Model" +
			" ORDER BY NumberOfRentals DESC" +
			" LIMIT 1";

	public static String getStatusCar() {
		return statusCar;
	}

	public static String getStatusMotorbike() {
		return statusMotorbike;
	}

	public static String getStatusScooter() {
		return statusScooter;
	}

	public static String getStatusBicycle() {
		return statusBicycle;
	}

	public static String getMostPopularVehicle() {
		return MostPopularVehicle;
	}

	public static String getMaxMinAvgRentalDuration() {
		return MaxMinAvgRentalDuration;
	}

	public static String checkServiceComplete(String CurrentDate) {
		return "SELECT" +
				" s.ServiceID," +
				" v.VehicleID," +
				" s.ServiceType" +
				" FROM Service s JOIN Vehicle v" +
				" ON s.VehicleID = v.VehicleID" +
				" WHERE (s.ServiceType = 'Repair' AND DATEDIFF(" + "'" + CurrentDate + "'" + ", s.EnterDate) > 3)" +
				" OR (s.ServiceType = 'Maintenance' AND DATEDIFF(" + "'" + CurrentDate + "'" + ", s.EnterDate) > 1)";

	}
}
