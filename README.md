# Technical Exercise Sibs

Instructions on how to run the project and how to call the routes.

1. Step: Clone the project [TechnicalExerciseSibs](https://github.com/AdilsonRTB/TechnicalExerciseSibs.git). in your localhost.
2. Step: Use editor Sql postgresql as `PgAdmin or DBeaver` ou another editor to run the next script to create a new database.
```
CREATE DATABASE SIBS_1
	WITH 
		OWNER = postgres
		CONNECTION LIMIT = -1;
 ```  
3. Step: Access a database and create a extentions to use UUID.
```
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
```

4. Step: Edit file `application-dev.properties` and change the password created in step 2.
	
`spring.datasource.password=<password database>`
	
5. Step: Create app end generate the password in your google account 
	
- google account>security>devices password
	
* add new app, generate password and copy it.
* in the file `application.properties` change the parameters
	
`spring.mail.username=<add your email>`
	
`spring.mail.password=<password generate in your google account>`

6. Step: Run the application 
7. Access the Swagger Api documentation in browser, it has all the information to call the APIs 
``` http://localhost:8082/swagger-ui.html ```
8. Step: Put the username and password defenid in `application-dev.properties` file
```
username=SIBS
password=SIBSDEV#MS#23
```
9. Step: After access a swagger API, create a users and items only after create a order or movement stock	
