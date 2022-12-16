# Library application

This application will create a tables:

* Author
* Book
* Reader
* Child
* Parent
* Loans

create few example records and automatically shut down.

### To run docker container you need to use following command

````
docker-compose up -d
````

### To build jar you nedd to use following commang

````
gradle build
````

### To start this application you need to use following command

````
java -jar build/libs/LibraryApplication-1.0.jar
````

### To do rollback you need to use following command , as x u need to write number of changeset to rollback

````
gradle rollbackCount -PliquibaseCommandValue=x
````

#### you can do rollback by date

````
gradle rollbackToDate -PliquibaseCommandValue=YYYY-MM-DDTHH:mm:ss
````

#### If you want to do rollback with specific name or password or url do one of following command

````
gradle rollbackCount -PliquibaseCommandValue=x -PurDbl=url -PusernameDb=username -PpasswordDb=password   
````

### To use swagger follow this link

````
http://localhost:8080/swagger-ui/index.html
````

I assumed that the configuration data as the minimum age to rent a book, etc. are in the configuration table in the
record with id = 1
Quantity in book table is all books in this library

Technologies :  Java, SpringBoot, Liquibase, PostgreSQL, Docker

Default values of configuration:
min age to borrow a book: 13
max number of days to borrow a book: 10
max number of borrowed books in the same time: 2
