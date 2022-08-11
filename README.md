# automated-teller-machine-API
An API for an atm application

![GitHub Issues](https://img.shields.io/github/issues/pitzzahh/automated-teller-machine-console)
![Forks](https://img.shields.io/github/forks/pitzzahh/automated-teller-machine-console)
![Stars](https://img.shields.io/github/stars/pitzzahh/automated-teller-machine-console)
![License](https://img.shields.io/github/license/pitzzahh/automated-teller-machine-console)
![Stable Release](https://img.shields.io/badge/version-1.0.1-blue)
________________________________________
## Quickstart
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

### Add Maven Dependency

If you use Maven, add the following configuration to your project's `pom.xml`

```maven
<dependencies>

    <!-- other dependencies are there -->
    <dependency>
        <groupId>io.github.pitzzahh</groupId>
        <artifactId>automated-teller-machine-API</artifactId>
        <version>1.0.1</version>
    </dependency>
    <!-- other dependencies are there -->

</dependencies>
```
