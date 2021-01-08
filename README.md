Coverage: 81%
# IMS

Inventory Management System for a shop, it keeps track of customer details, purchasable items and their details as well as creating orders using the previous details.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

Java Runtime Environment 1.8

An instance of MySQL server 5.7

### Installing

You can download a working jar from [here](https://github.com/sforsteracademytrainee/IMS/raw/developer/documentation/ims-1.0.0-with-dependencies.jar).
The provided jar build only works with a localhost database.

If you wish to build the project yourself here are the instructions.

Import this project from github as a Maven project.

You will need to edit the contents of the `DBUtils` class with your own database host url in the `connect` function.

```
public static DBUtils connect(String username, String password) {
	instance = new DBUtils("yourhost", username, password);
	return instance;
}
```

You will also have to enter your database login details for the tests in the `Details` class in the test tree.


```
private static final String DB_USER = "youruser";

private static final String DB_PASS = "yourpass";
```

Once this is done you should test the program using the provided tests. If it all goes by you can build the project with the command `mvn clean package`. The working jar will be called `ims-x.x.x-jar-with-dependencies.jar`.

## Running the tests

You can run all all the tests by using `mvn test` in a command prompt at the project folder. Make sure you have entered database details in the `Details` class as described above in the test packages.

All the tests are located in the `src/test` folder.

The tests make sure that the database connection is functional and that the user input is handled correctly.

Currently we have an 81% test coverage.
![text](https://i.imgur.com/O9waSOw.png)

### Unit Tests 

The unit tests make sure that the interaction with the database is correct.

They are located in the `persistence.dao` package of the test folder. There is also a test for the `Order` object in the `domain` package. They are performed using JUnit 4.12.

### Integration Tests 

The integration tests are here to check if the correct actions are done when giving input to the controllers. These tests do not require a database connection as database results are mocked.

They are located in the `controllers` package. We used Mockito 3.2.4 for this project.

### And coding style tests

We are currently not using coding style tests.

## Built With

* [Maven](https://maven.apache.org/) - Dependency Management


## Authors

* **J Harry** - *Initial work* - [JHarry444](https://github.com/JHarry444)
* **Simon Forster** - *Continued work* - [sforsteracademytrainee](https://github.com/sforsteracademytrainee)

## License

This project is licensed under the MIT license - see the [LICENSE.md](LICENSE.md) file for details 

*For help in [Choosing a license](https://choosealicense.com/)*
