# TowGo Towing Service

Layered Java console app (dao / model / ui) using SQLite via Maven.
Course: PF201 OOP - IT2G

## Requirements
- Java 17+
- Maven 3.6+

## Run
```
mvn compile exec:java
```
The database file `towgo.db` is created automatically on first run.
Default admin account: `admin` / `admin123`

## Structure
```
src/main/java
├── Main.java                  entry point
├── dao/DatabaseConnection.java   connection + table creation
├── dao/UserDao.java              SQL (PreparedStatement)
├── model/User.java               record
└── ui/MainMenu.java, DashboardMenu.java
```

## Features
- Register (role STAFF or DRIVER, status defaults to PENDING)
- Login (checked against the database)
- Logout / Exit
