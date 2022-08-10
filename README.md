# automated-teller-machine-API
An API for an atm application

![GitHub Issues](https://img.shields.io/github/issues/pitzzahh/automated-teller-machine-console)
![Forks](https://img.shields.io/github/forks/pitzzahh/automated-teller-machine-console)
![Stars](https://img.shields.io/github/stars/pitzzahh/automated-teller-machine-console)
![License](https://img.shields.io/github/license/pitzzahh/automated-teller-machine-console)
![Stable Release](https://img.shields.io/badge/version-1.0.0-blue)
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
        atmService = new AtmService(atmDAO);
        atmService.setDataSource().accept(
                databaseConnection.setDriverClassName("org.postgresql.Driver")
                        .setUrl("jdbc:postgresql://localhost/postgres")
                        .setUsername("postgres")
                        .setPassword("password")
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
        <artifactId>util-classes</artifactId>
        <version>1.0.5</version>
    </dependency>
    <!-- other dependencies are there -->

</dependencies>
```
