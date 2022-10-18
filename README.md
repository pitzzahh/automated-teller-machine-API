# automated-teller-machine-API
API used for making atm applications (none-web-app)

![GitHub Issues](https://img.shields.io/github/issues/pitzzahh/automated-teller-machine-console)
![Forks](https://img.shields.io/github/forks/pitzzahh/automated-teller-machine-console)
![Stars](https://img.shields.io/github/stars/pitzzahh/automated-teller-machine-console)
![License](https://img.shields.io/github/license/pitzzahh/automated-teller-machine-console)
________________________________________
## Quickstart
### How to use the API
* With Database (can use any DBMS)

```java
import io.github.pitzzahh.atm.database.DatabaseConnection;
import io.github.pitzzahh.atm.dao.InDatabase;
import io.github.pitzzahh.atm.service.AtmService;
import io.github.pitzzahh.atm.dao.AtmDAO;

public class App {
    private static final AtmDAO ATM_DAO = new InDatabase();
    private static final DatabaseConnection DATABASE_CONNECTION = new DatabaseConnection();

    public static void main(String[] args) {
        AtmService atmService = new AtmService(ATM_DAO);
        atmService.setDataSource().accept(
                DATABASE_CONNECTION
                        .setDriverClassName("org.postgresql.Driver")
                        .setUrl("jdbc:postgresql://localhost/{database_name}")
                        .setUsername("{username}")
                        .setPassword("{password}")
                        .getDataSource()
        );
    }
}
 ```

---

* Without Database (in-memory)

```java
import io.github.pitzzahh.atm.service.AtmService;
import io.github.pitzzahh.atm.dao.InMemory;

public class App {

    public static void main(String[] args) {
        AtmService atmService = new AtmService(new InMemory()); 
    }
}
 ```

## Saving clients


To save a client object, a method called `saveClient()` in `AtmService` is used. It is a Function that accepts a Client Object.
```java
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
// getting the client, returns a Client object, throws IllegalArgumentException if account number does not belong to any client.
Client client = atmService.getClientByAccountNumber().apply("123123123");
// prints the client (using Print class from util-classes-API)
println(client);
// removes the client by account number
atmService.removeClientByAccountNumber().apply("123123123");

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
![maven-central](https://img.shields.io/maven-central/v/io.github.pitzzahh/automated-teller-machine-API?color=blue)

If you use Maven, add the following configuration to your project's `pom.xml`

Be sure to replace the **VERSION** key below with the one of the versions shown above

```maven
<dependencies>

    <!-- other dependencies are there -->
    <dependency>
        <groupId>io.github.pitzzahh</groupId>
        <artifactId>automated-teller-machine-API</artifactId>
        <version>VERSION</version>
    </dependency>
    <!-- other dependencies are there -->

</dependencies>
```
### Others
Dependencies
- [util-classes](https://github.com/pitzzahh/util-classes)
