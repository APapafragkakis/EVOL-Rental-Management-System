package com.team_34.controller;

import com.team_34.model.DatabaseModel;

import javax.servlet.annotation.WebListener;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletContextEvent;

public class DatabaseController {
	public DatabaseController(DatabaseModel databaseModel) {
		databaseModel.initializeTables();
	}
}
