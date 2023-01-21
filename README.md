# Technical Exercise Sibs

Instructions on how to run the project and how to call the routes.

1. Step: Clone the project [TechnicalExerciseSibs](https://pages.github.com/](https://github.com/AdilsonRTB/TechnicalExerciseSibs.git). in your localhost.
2. Step: Use editor Sql postgresql as PgAdmin or DBeaver ou another editor to run the next script to create a new database

Create database SIBS_1
	with 
		OWNER = postgres
		CONNECTION LIMIT = -1;
    
3. Step: create a extentions to use UUID

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
