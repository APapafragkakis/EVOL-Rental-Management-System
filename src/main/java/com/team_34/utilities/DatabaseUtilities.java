// DatabaseUtilities.java
package com.team_34.utilities;

import java.sql.*;
import java.time.LocalDate;

public class DatabaseUtilities {
	public static String createInsertQuery(String tableName, String[] columnNames, Object[] values) {
		String insertQuery = "INSERT INTO " + tableName + " (";

		for (int i = 0; i < columnNames.length; i++) {
			insertQuery += columnNames[i];
			if (i < columnNames.length - 1) {
				insertQuery += ", ";
			}
		}

		insertQuery += ") VALUES (";

		for (int i = 0; i < values.length; i++) {
			insertQuery += "?";
			if (i < values.length - 1) {
				insertQuery += ", ";
			}
		}

		insertQuery += ")";

		return insertQuery;
	}

	public static String createUpdateQuery(String tableName, String[] columnNames, String[] values, String condition) {
		String updateQuery = "UPDATE " + tableName + " SET ";

		for (int i = 0; i < columnNames.length; i++) {
			updateQuery += columnNames[i] + " = ?";
			if (i < columnNames.length - 1) {
				updateQuery += ", ";
			}
		}

		updateQuery += " WHERE " + condition;

		return updateQuery;
	}

	public static String createSelectQuery(String tableName, String[] columnNames, String condition) {
		String selectQuery = "SELECT ";

		for (int i = 0; i < columnNames.length; i++) {
			selectQuery += columnNames[i];
			if (i < columnNames.length - 1) {
				selectQuery += ", ";
			}
		}

		selectQuery += " FROM " + tableName + " WHERE " + condition;

		return selectQuery;
	}

	public static String createDeleteQuery(String tableName, String condition) {
		String deleteQuery = "DELETE FROM " + tableName + " WHERE " + condition;
		return deleteQuery;
	}

	public static void setObjectByType(PreparedStatement preparedStatement, int index, int columnType, Object value)
			throws SQLException {
		switch (columnType) {
			case java.sql.Types.INTEGER:
				preparedStatement.setInt(index, (Integer) value);
				break;
			case java.sql.Types.VARCHAR:
			case java.sql.Types.CHAR:
				preparedStatement.setString(index, (String) value);
				break;
			case java.sql.Types.DATE:
				preparedStatement.setDate(index, (Date) value);
				break;
			case java.sql.Types.BOOLEAN:
			case java.sql.Types.BIT:
				preparedStatement.setBoolean(index, (Boolean) value);
				break;
			case java.sql.Types.TIMESTAMP:
				preparedStatement.setTimestamp(index, (Timestamp) value);
				break;
			case java.sql.Types.NULL:
				preparedStatement.setNull(index, java.sql.Types.NULL);
				break;
			case java.sql.Types.DECIMAL:
				preparedStatement.setBigDecimal(index, (java.math.BigDecimal) value);
				break;
			default:
				// Handle other data types or throw an exception
				throw new SQLException("Unsupported column type: " + columnType);
		}
	}

	public static int getColumnType(Connection connection, String tableName, String columnName) throws SQLException {
		DatabaseMetaData metaData = connection.getMetaData();
		ResultSet columnsResultSet = metaData.getColumns(null, null, tableName, columnName);

		if (columnsResultSet.next()) {
			return columnsResultSet.getInt("DATA_TYPE");
		}

		throw new SQLException("Column type not found for " + tableName + "." + columnName);
	}

	public static Boolean checkDriver(Connection connection, Date birthDate, int RentalID) {
		String sql = "SELECT VTBN.VehicleType " +
				"FROM Driver D " +
				"JOIN Rental R ON " + "R.RentalID = " + "?" +
				" JOIN Vehicle V ON R.VehicleID = V.VehicleID" +
				" JOIN Vehicle_Type_Brand_Model VTBN ON V.VehicleType = VTBN.VehicleType" +
				" AND V.Brand = VTBN.Brand " + "AND V.Model = VTBN.Model ";

		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setInt(1, RentalID);

			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				if (resultSet.next()) {
					String vehicleType = resultSet.getString("VehicleType");

					int ageThresh;
					LocalDate currentDate = LocalDate.now();
					LocalDate birthLocalDate = birthDate.toLocalDate();

					if (vehicleType.equals("Car") || vehicleType.equals("Motorbike"))
						ageThresh = 18;
					else
						ageThresh = 16;

					if (birthLocalDate.plusYears(ageThresh).isAfter(currentDate))
						return false;
				} else {
					// throw exception
					throw new SQLException("No Vehicle found for the specified RentalID.");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return true;
	}

	public static Boolean checkCarRentalInsertion(Connection connection, int RentalID) {
		String sql = "SELECT VTBN.VehicleType " +
				"FROM Rental R " +
				"JOIN Vehicle V ON R.VehicleID = V.VehicleID " +
				"JOIN Vehicle_Type_Brand_Model VTBN ON V.VehicleType = VTBN.VehicleType " +
				"AND V.Brand = VTBN.Brand " + "AND V.Model = VTBN.Model " +
				"WHERE R.RentalID = " + RentalID;

		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				if (resultSet.next()) {
					String vehicleType = resultSet.getString("VehicleType");

					if (vehicleType.equals("Car") || vehicleType.equals("Motorbike")) {
						System.out.println("Car Rental Insertion is allowed.");
						return true;
					} else {

						return false;
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return true;
	}
}
