# automated-teller-machine-API
API used for making atm applications (none-web-app)

![GitHub Issues](https://img.shields.io/github/issues/pitzzahh/automated-teller-machine-console)
![Forks](https://img.shields.io/github/forks/pitzzahh/automated-teller-machine-console)
![Stars](https://img.shields.io/github/stars/pitzzahh/automated-teller-machine-console)
![License](https://img.shields.io/github/license/pitzzahh/automated-teller-machine-console)
![Stable Release](https://img.shields.io/badge/version-1.0.2-blue)
________________________________________
## Quickstart
### How to use the API
* connecting to a database

```java
import io.github.pitzzahh.atm.dao.AtmDAO;
import io.github.pitzzahh.atm.service.AtmService;
import io.github.pitzzahh.atm.database.DatabaseConnection;

public class App {

    private static AtmDAO atmDAO;
    private static AtmService atmService;
    private static DatabaseConnection databaseConnection;

    public static void main(String[] args) {
        atmService = new AtmService(atmDAO, databaseConnection);
        atmService.setDataSource().accept(atmService
                .connection
                .setDriverClassName("org.postgresql.Driver")
                .setUrl("jdbc:postgresql://localhost/postgres")
                .setUsername("usernameHere")
                .setPassword("passwordHere")
                .getDataSource()
        );
    }
}
 ```
* saving clients to the database
```java
import io.github.pitzzahh.atm.database.DatabaseConnection;
import com.github.pitzzahh.utilities.classes.enums.Gender;
import com.github.pitzzahh.utilities.classes.Person;
import io.github.pitzzahh.atm.service.AtmService;
import io.github.pitzzahh.atm.entity.Client;
import com.github.pitzzahh.utilities.Print;
import io.github.pitzzahh.atm.dao.AtmDAO;
import java.time.LocalDate;
import java.util.Optional;
import java.time.Month;

public class App {

    private static AtmDAO atmDAO;
    private static AtmService atmService;
    private static DatabaseConnection databaseConnection;

    public static void main(String[] args) {
        atmService = new AtmService(atmDAO, databaseConnection);
        atmService.setDataSource().accept(atmService
                .connection
                .setDriverClassName("org.postgresql.Driver")
                .setUrl("jdbc:postgresql://localhost/atm")
                .setUsername("postgres")
                .setPassword("!Password123")
                .getDataSource()
        );
        // saving a client object to the clients table...
        atmService.saveClient().apply(
                new Client(
                        "123123123",
                        "123123",
                        new Person(
                                "Mark",
                                "Silent",
                                Gender.PREFER_NOT_TO_SAY,
                                "Earth",
                                LocalDate.of(2018, Month.AUGUST, 10)
                        ),
                        5555,
                        false
                )
        );
        // getting the client, returns an Optional<Client> because Client Object might be null.
        Client client = atmService.getClientByAccountNumber().apply("123123123");
        // prints the client (using Print class from util-classes-API) or else throws an exception
        Print.println(client);
        // removes the client by account number
        atmService.removeClientByAccountNumber().apply("123123123");
    }
}
```
To save a client object, a method called `saveClient()` in `AtmService` is used. It is a Function that accepts a Client Object.
```java
atmService.saveClient().apply(
        new Client(
                "123123123", // the account number
                "123123", // the account pin
                new Person(
                      "Mark", // the first name
                      "Silent", // the last name
                      Gender.PREFER_NOT_TO_SAY, // the gender
                      "Earth", // the address
                      LocalDate.of(2018, Month.AUGUST, 10) // the birth date
                 ),
                 5555, // the initial account balance
                 false // false means the account is not locked, otherwise true if locked
        )
);
```
To get the client from the database, there are two methods that can be used, first is `getClientByAccountNumber()` a method that accepts a
`String` containing an account number, second is `getAllClients()` a method that get all the client
as a `Supplier<Map<String, Client>>`.
Below shows the two ways on how to get a client/clients.
```java
// getting client by account number
Client client = atmService.getClientByAccountNumber().apply("123123123");
```
```java
// getting all the clients.
Supplier<Map<String, Client> > clients = atmService.getAllClients();
```
To remove a client, there are also two methods that can be used, first is removing client by account number,
second is removing all the clients.
Below shows the two ways on how to remove a client/clients.
```java
// removes the client by account number
atmService.removeClientByAccountNumber().apply("123123123");
```
```java
// removes all the clients
atmService.removeAllClients();
```
### Add Maven Dependency

If you use Maven, add the following configuration to your project's `pom.xml`

```maven
<dependencies>

    <!-- other dependencies are there -->
    <dependency>
        <groupId>io.github.pitzzahh</groupId>
        <artifactId>automated-teller-machine-API</artifactId>
        <version>1.0.2</version>
    </dependency>
    <!-- other dependencies are there -->

</dependencies>
```
### Others
Dependencies
- [util-classes](https://github.com/pitzzahh/util-classes)
