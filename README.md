# EVOL - Vehicle Rental Database System

Team: Alexandros Papafragakis, Ioannis Trikalopoulos, Giorgos Syngelakis

---

## About

Database system for managing a vehicle rental company. Supports cars, motorbikes,
scooters and bicycles. Built as part of the HY360 course on Files and Databases.

---

## What it does

- Register customers and their credit cards
- Add vehicles to the fleet (cars, motorbikes, scooters, bicycles)
- Rent vehicles to customers
- Track returns, accidents and damage reports
- Manage vehicle repair and maintenance scheduling
- Handle payments

---

## Database Design

The schema is based on an Entity-Relationship diagram with 3 core entities:

- **Rental** — tracks each rental instance, insurance, damage, accident status
- **Vehicle** — all vehicle types with cost, availability and maintenance info
- **Customer** — personal info, address, credit cards

Additional entities: Driver, Payment, Service, CreditCard

The schema was normalized to **Third Normal Form (3NF)** by decomposing the
Vehicle relation to remove a transitive dependency on (VehicleType, Brand, Model).

---

## Schema Overview

```
Vehicle (VehicleID, Color, Model, Brand, VehicleType, IsRented, needMaintenance, needRepair)
VehicleTypeBrandModel (VehicleType, Brand, Model, DailyRentalCost, DailyInsuranceCost, HourlyDelayedCost, RangeInKilometers)
RegisteredVehicle (VehicleID, RegistrationNumber)
UnregisteredVehicle (VehicleID, UniqueNumber)
Car (VehicleID, CarType, NumberOfPassengers)
Customer (TIN, FirstName, LastName, City, Street, HouseNumber, PostalCode, BirthDay, BirthMonth, BirthYear)
CreditCard (CreditCardNumber, ExpirationMonth, ExpirationYear, CCV, TIN)
Rental (RentalID, TIN, VehicleID, DeliveryDateTime, ReturnDateTime, hasInsurance, hasDamage, hadAccident, hasReturned, IsCustomerDriver)
Car/MotorbikeRental (RentalID, DriverLicenseNumber)
Driver (TIN, BirthDay, BirthMonth, BirthYear, RentalID)
Payment (PaymentID, Amount, RentalID)
Service (ServiceID, Cost, EnterDate, IsComplete, ServiceType, VehicleID)
```

---

## How to run

The system runs as a CLI application. On startup it presents a menu:

```
1. Register New Customer
2. Supply Vehicles
3. Rent Vehicle
4. Return Vehicle
5. Report Vehicle Damage
6. Report Accident
7. Vehicle Repair
```

---

## Notes

- Unregistered vehicles (scooters, bicycles) use a UniqueNumber instead of a registration plate
- A rental requires a driver's license only for cars and motorbikes
- If a vehicle is involved in an accident without insurance, the customer is charged 3x the rental cost
- Repairs take 3 days, maintenance takes 1 day — vehicle is unavailable during this time

---
