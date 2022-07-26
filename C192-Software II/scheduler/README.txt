Create a README.txt file that includes the following information:

•  title and purpose of the application
Title: Scheduler 
Purpose: I work for a software company that has been contracted to develop a 
	GUI-based scheduling desktop application. The contract is with a global consulting 
	organization that conducts business in multiple languages and has main offices in 
	Phoenix, Arizona; White Plains, New York; Montreal, Canada; and London, England. 
	The consulting organization has provided a MySQL database that the application must 
	pull data from. The database is used for other systems, so its structure cannot be 
	modified.

•  author, contact information, student application version, and date
Author: Trayer Lee Harvey
Contact Information: tharv91@wgu.edu
Student Application Verion: 2
Date: 5-23-2022

•  IDE including version number (e.g., IntelliJ Community 2020.01), full JDK of version used (e.g., Java SE 17.0.1), and JavaFX version compatible with JDK version (e.g. JavaFX-SDK-17.0.1)
IDE: IntelliJ Community 2022.1.1
JDK: Java SE 18.0.1.1
JavaFX: 18.0.1

•  directions for how to run the program
Setup packages, 
go to scheduler\src\main\java\view_controller\scheduler\databaseConnect.java 
	to setup MySQL credentials,
	Current MySQL Creds:
		username: sqlUser
		password: Passw0rd!
		db name: client_schedule
		url: jdbc:mysql://localhost:3306/
		class: com.mysql.cj.jdbc.Driver
	
run like normal (compile from main).
scheduler/src/main has all the important java and fxml files.
JAVADOCS is in scheduler/javadocs/index.html,
README (this file) is in scheduler
login_activity.txt is where the login results output goes


•  a description of the additional report of your choice you ran in part A3f
I created a method to get a schedule for each CUSTOMER in your organization that includes appointment ID, title, type and description, start date and time, end date and time, and customer ID
viewable on reports.java

•  the MySQL Connector driver version number, including the update number (e.g., mysql-connector-java-8.1.23)
mysql-connector-java-8.0.29