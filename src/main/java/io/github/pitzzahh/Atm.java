package io.github.pitzzahh;

import java.util.*;
import java.time.LocalDate;
import java.util.stream.Collectors;
import java.util.concurrent.TimeUnit;
import io.github.pitzzahh.entity.Client;
import io.github.pitzzahh.entity.LockedAccount;
import io.github.pitzzahh.service.ClientService;
import com.github.pitzzahh.utilities.SecurityUtil;
import com.github.pitzzahh.utilities.classes.Person;
import com.github.pitzzahh.utilities.classes.enums.Role;
import com.github.pitzzahh.utilities.classes.enums.Gender;
import com.github.pitzzahh.utilities.classes.enums.Status;
import com.github.pitzzahh.utilities.validation.Validator;
import static com.github.pitzzahh.utilities.classes.TextColors.*;
import static com.github.pitzzahh.utilities.classes.enums.Status.*;
import static com.github.pitzzahh.utilities.validation.Validator.isString;
import static io.github.pitzzahh.Atm.Machine.AdminAcc.getAllLockedAccounts;
import static com.github.pitzzahh.utilities.validation.Validator.isDecimalNumber;

/**
 * Automated Teller Machine.
 * Details are encrypted to avoid data leak.
 * @author pitzzahh
 */
public class Atm {
    private static final ClientService CLIENT_SERVICE = new ClientService();
    private static Hashtable<String, Client> clients = new Hashtable<>();

    /**
     * main method.
     * @param args arguments.
     */
    @SuppressWarnings("InfiniteLoopStatement")
    public static void main(String[] args)  {
        final var scanner = new Scanner(System.in);
        CLIENT_SERVICE.setDataSource().accept(ClientService.getDataSource());
        Machine.loadClients();
        for (;;)  {
            try {
                switch (home(scanner)) {
                    case ADMIN -> Machine.AdminAcc.admin(scanner);
                    case CLIENT -> Machine.ClientAcc.client(scanner);
                }
            } catch (RuntimeException | IllegalAccessException runtimeException) {
                System.out.println(RED_BOLD_BRIGHT +  runtimeException.getMessage() + RESET);
                System.out.print(YELLOW_BOLD_BRIGHT + "LOADING");
                dotLoading();
            }
        }
    }

    /**
     * The automated teller machine that handles most of the process.
     */
    static class Machine {
        private static final String $adm = "QGRtMW4xJHRyNHQwcg==";
        private static String $an;
        /**
         * Searches the {@code Hashtable<String, Client>}, checks if the account number exists.
         * @param n the account number to search.
         * @return {@code true} if the account number exists.
         */
        private static boolean searchAccount(String n) {
            return clients
                    .keySet()
                    .stream()
                    .anyMatch(number -> number.equals(n));
        }

        /**
         * Searches the {@code searchLockedAccount()} method, checks if the account number exists.
         * @param n the account number to search.
         * @return {@code true} if the account number exists.
         */
        private static boolean searchLockedAccount(String n) {
            return getAllLockedAccounts()
                    .stream()
                    .map(LockedAccount::accountNumber)
                    .anyMatch(an -> an.equals(n));
        }

        /**
         * Loads all the clients from the database.
         */
        private static void loadClients() {
            clients.putAll(CLIENT_SERVICE.getAllClients().get());
        }

        /**
         * class for Admin account.
         */
        protected static class AdminAcc {

            /**
             * called then an admin is logged in.
             * @param scanner the scanner needed for keyboard input.
             * @throws IllegalArgumentException if any of the input is not valid from the detail(scanner) method.
             * @throws IllegalStateException if there are no clients available.
             */
            private static void admin(Scanner scanner) throws IllegalArgumentException, IllegalStateException {
                var choice = "";
                do {
                    try {
                        System.out.println(RED_BOLD  + ": " + BLUE_BOLD_BRIGHT   + "1" + RED_BOLD + " : " + BLUE_BOLD_BRIGHT + "ADD CLIENT");
                        System.out.println(RED_BOLD  + ": " + YELLOW_BOLD_BRIGHT + "2" + RED_BOLD + " : " + YELLOW_BOLD_BRIGHT + "REMOVE CLIENT");
                        System.out.println(RED_BOLD  + ": " + GREEN_BOLD_BRIGHT  + "3" + RED_BOLD + " : " + GREEN_BOLD_BRIGHT + "VIEW CLIENTS");
                        System.out.println(RED_BOLD  + ": " + PURPLE_BOLD_BRIGHT + "4" + RED_BOLD + " : " + PURPLE_BOLD_BRIGHT + "MANAGE LOCKED ACCOUNTS");
                        System.out.println(RED_BOLD  + ": " + RED                + "5" + RED_BOLD + " : " + RED + "LOGOUT");
                        System.out.print(PURPLE_BOLD + ">>>: " + RESET);
                        choice = scanner.nextLine().trim();
                        switch (choice) {
                            case "1" -> {
                                System.out.println(BLUE_BOLD_BRIGHT + "CREATE CLIENT" + RESET);
                                var status = createClient(scanner);
                                System.out.println(
                                        status == SUCCESS ? BLUE_BOLD_BRIGHT + "\nCLIENT ADDED SUCCESSFULLY\n" :
                                                RED_BOLD_BRIGHT + "\nERROR ADDING CLIENT\n" + RESET
                                );
                            }
                            case "2" -> {
                                System.out.println(YELLOW_BOLD_BRIGHT + "REMOVE CLIENT" + RESET);
                                var status = removeClient(scanner);
                                System.out.println(
                                        status == SUCCESS ? BLUE_BOLD_BRIGHT + "\nCLIENT REMOVED SUCCESSFULLY\n" :
                                                RED_BOLD_BRIGHT + "\nERROR REMOVING CLIENT\n" + RESET
                                );
                            }
                            case "3" -> viewAllClients();
                            case "4" -> manageLockedAccounts(scanner);
                            case "5" -> {
                                System.out.print(RED_BOLD_BRIGHT + "LOGGING OUT");
                                dotLoading();
                            }
                            default -> throw new IllegalStateException(String.format("\nINVALID INPUT: %s\n", choice));
                        }
                        clients.clear();
                        loadClients();
                    } catch (RuntimeException runtimeException) {
                        System.out.println(RED_BOLD_BRIGHT +  runtimeException.getMessage() + RESET);
                        System.out.print(YELLOW_BOLD_BRIGHT + "LOADING");
                        dotLoading();
                    }
                } while (!choice.equals("5"));
            }

            /**
             * Creates a new {@code Client} and adds it to the {@code Hashtable<String, Client>}
             * @param scanner the scanner needed for keyboard input.
             * @throws IllegalArgumentException if any of the input is not valid from the detail(scanner) method.
             */
            private static Status createClient(Scanner scanner) throws IllegalArgumentException {
                var client = details(scanner);
                return CLIENT_SERVICE.saveClient().apply(
                        new Client(
                                client.accountNumber(),
                                client.pin(),
                                new Person(
                                        client.details().getFirstName(),
                                        client.details().getLastName(),
                                        client.details().getGender(),
                                        client.details().getAddress(),
                                        client.details().getBirthDate()
                                ),
                                client.savings(),
                                client.attempts()
                        )
                );
            }

            /**
             * method for asking for user details.
             * @param scanner the scanner needed for keyboard input.
             * @return a {@code Client} object.
             * @throws IllegalArgumentException if any of the input is not valid.
             */
            protected static Client details(Scanner scanner) throws IllegalArgumentException {

                var an = "";
                var pin = "";
                var firstName = "";
                var lastName = "";
                var gender = "";
                var address = "";
                var birthDate = "";

                do {
                    System.out.print(BLUE_BOLD_BRIGHT + "Enter account number: ");
                    an = scanner.nextLine().trim();
                    checkAccountNumberInput(an);
                    if (searchAccount(an)) System.out.println(RED_BOLD_BRIGHT + String.format("\nCLIENT WITH ACCOUNT NUMBER : %s ALREADY EXISTS\n", an) + RESET);
                } while (Validator.isIdValid().negate().test(an) || an.length() < 9 || searchAccount(an));

                do {
                    System.out.print(PURPLE_BOLD_BRIGHT + "Enter account pin   : ");
                    pin = scanner.nextLine().trim();
                    if (Validator.isWholeNumber().negate().test(pin) || pin.length() < 6) System.out.println(RED_BOLD_BRIGHT + "\nPIN SHOULD BE 6 DIGITS\n" + RESET);
                } while (Validator.isWholeNumber().negate().test(pin) || pin.length() < 6);

                System.out.print(YELLOW_BOLD_BRIGHT + "Enter client first name: ");
                firstName = scanner.nextLine().toUpperCase().trim();

                System.out.print(YELLOW_BOLD_BRIGHT + "Enter client last name : ");
                lastName = scanner.nextLine().toUpperCase().trim();

                do {
                    System.out.print(GREEN_BOLD_BRIGHT + "Enter client gender    : ");
                    gender = scanner.nextLine().toUpperCase().trim();
                    if (Validator.isGenderValid().negate().test(gender)) System.out.println(RED_BOLD_BRIGHT + "\nUnknown Gender, please select from the list: " + Arrays.toString(Arrays.stream(Gender.values()).map(Gender::name).toArray()) + "\n" + RESET);
                } while (Validator.isGenderValid().negate().test(gender));

                System.out.print(BLUE_BOLD_BRIGHT + "Enter client address   : ");
                address = scanner.nextLine().toUpperCase().trim();
                List<String> b = new ArrayList<>();
                var year = 2000;
                var month = 1;
                var day = 1;
                do {
                    System.out.print(PURPLE_BOLD_BRIGHT + "Enter client birthdate : ");
                    birthDate = scanner.nextLine().trim();
                    if (Validator.isWholeNumber().or(isDecimalNumber()).or(isString()).test(birthDate)) throw new IllegalArgumentException("\nINVALID BIRTHDATE FORMAT, VALID FORMAT: (YYYY-MM-dd)\n");
                    b = Arrays.stream(birthDate.split("-")).toList();
                    year = Integer.parseInt(b.get(0));
                    month = Integer.parseInt(b.get(1));
                    day = Integer.parseInt(b.get(2));
                    if (year < 1850 || year > 2029) System.out.println(RED_BOLD_BRIGHT + String.format("\nINVALID YEAR: %s\n", String.valueOf(year)) + RESET);
                    if (month <= 0 || month > 12) System.out.println(RED_BOLD_BRIGHT + String.format("\nINVALID MONTH: %s\n", String.valueOf(month)) + RESET);
                    if (day <= 0 || (day > 31 && !LocalDate.EPOCH.isLeapYear())) System.out.println(RED_BOLD_BRIGHT + String.format("\nINVALID DAY: %s\n", String.valueOf(day)) + RESET);
                } while (Validator.isBirthDateValid().negate().test(birthDate));
                return new Client(
                        an,
                        pin,
                        new Person(
                                firstName,
                                lastName,
                                Gender.valueOf(gender),
                                address,
                                LocalDate.of(year, month, day)
                        ),
                        5000.00,
                        5
                );
            }

            /**
             * Method for removing clients, used by admin.
             * @param scanner the scanner needed for keyboard input.
             * @return a {@code Status} of removal.
             * @throws IllegalStateException if there are no clients available.
             */
            protected static Status removeClient(Scanner scanner) throws IllegalStateException {
                checkIfThereAreClientsAvailable();
                System.out.println(RED_BOLD_BRIGHT + ":ENTER ACCOUNT NUMBER TO REMOVE:");
                System.out.print(PURPLE_BOLD + ">>>: " + RESET);
                $an = scanner.nextLine().trim();
                checkAccountNumberInput($an);
                if (!searchAccount($an)) throw new IllegalStateException(String.format("\nACCOUNT: %s DOES NOT EXIST\n", $an));
                clients.remove($an);
                return CLIENT_SERVICE.removeClientByAccountNumber().apply($an);
            }

            /**
             * Prints all the clients details.
             * @throws IllegalStateException if there are no clients available.
             */
            protected static void viewAllClients() throws IllegalStateException {
                checkIfThereAreClientsAvailable();
                clients.entrySet()
                        .stream()
                        .map(Map.Entry::getValue)
                        .forEach(System.out::println);
            }

            /**
             * Manages locked accounts.
             * @param scanner the scanner needed for keyboard input.
             */
            private static void manageLockedAccounts(Scanner scanner) throws IllegalStateException {
                final var LOCKED_ACCOUNTS = getAllLockedAccounts();
                if (LOCKED_ACCOUNTS.size() == 0) throw new IllegalStateException("\nTHERE ARE NO LOCKED ACCOUNTS\n");
                String choice = "";
                do {
                    System.out.println(YELLOW_BOLD_BRIGHT + "\n: MANAGE LOCKED ACCOUNTS :\n");
                    System.out.println(RED_BOLD_BRIGHT + "LOCKED " + (LOCKED_ACCOUNTS.size() > 1 ? "ACCOUNTS" : "ACCOUNT") +"\n");
                    LOCKED_ACCOUNTS.forEach(System.out::println);
                    System.out.println(PURPLE_BOLD + ":" + BLUE_BOLD_BRIGHT + " 1 " + PURPLE_BOLD_BRIGHT + ": " + BLUE_BOLD_BRIGHT + "REOPEN LOCKED ACCOUNT");
                    System.out.println(PURPLE_BOLD + ":" + RED_BOLD_BRIGHT + " 2 " + PURPLE_BOLD_BRIGHT + ": " + RED_BOLD_BRIGHT + "DELETE LOCKED ACCOUNT");
                    System.out.println(PURPLE_BOLD + ":" + GREEN_BOLD_BRIGHT + " 3 " + PURPLE_BOLD_BRIGHT + ": " + GREEN_BOLD_BRIGHT + "BACK");
                    System.out.print(YELLOW_BOLD + ">>>: " + RESET);
                    choice = scanner.nextLine().trim();
                    switch (choice) {
                        case "1" -> {
                            System.out.println(BLUE_BOLD_BRIGHT + "REOPEN ACCOUNT" + RESET);
                            var status = reopenAccount(scanner);
                            System.out.println(
                                    status == SUCCESS ? BLUE_BOLD_BRIGHT + "\nACCOUNT REOPENED SUCCESSFULLY\n" :
                                            RED_BOLD_BRIGHT + "\nERROR REOPENING ACCOUNT\n" + RESET
                            );
                        }
                        case "2" -> {
                            System.out.println(YELLOW_BOLD_BRIGHT + "REMOVE ACCOUNT" + RESET);
                            var status = CANNOT_PERFORM_OPERATION;
                            System.out.println(
                                    status == SUCCESS ? BLUE_BOLD_BRIGHT + "\nACCOUNT REMOVED SUCCESSFULLY\n" :
                                            RED_BOLD_BRIGHT + "\nERROR REMOVING ACCOUNT\n" + RESET
                            );
                        }
                        case "3" -> {
                            System.out.print(RED_BOLD_BRIGHT + "PLEASE WAIT");
                            dotLoading();
                        }
                        default -> throw new IllegalStateException(String.format("\nINVALID INPUT: %s\n", choice));
                    }
                    LOCKED_ACCOUNTS.removeIf(c -> c.accountNumber().equals($an));
                    clients.clear();
                    loadClients();
                    if (LOCKED_ACCOUNTS.isEmpty()) break;
                } while (!choice.equals("3"));
            }

            /**
             * Reopens a locked account.
             * @param scanner the scanner needed for keyboard input.
             * @return a {@code Status} of the operation.
             */
            private static Status reopenAccount(Scanner scanner) {
                System.out.println(RED_BOLD_BRIGHT + ":ENTER ACCOUNT NUMBER TO REOPEN:");
                System.out.print(PURPLE_BOLD + ">>>: " + RESET);
                $an = scanner.nextLine().trim();
                checkAccountNumberInput($an);
                if (!searchLockedAccount($an)) throw new IllegalStateException(String.format("\nACCOUNT: %s DOES NOT EXIST\n", $an));
                return CLIENT_SERVICE.updateClientAttemptsByAccountNumber().apply($an, "5");
            }

            /**
             * Gets all the list of locked accounts {@code Client} object and creating {@code LockedAccount} objects.
             * @return a {@code List<LockedAccounts>}
             */
            protected static List<LockedAccount> getAllLockedAccounts() {
                return clients.entrySet()
                        .stream()
                        .map(Map.Entry::getValue)
                        .filter(an -> an.attempts() == 0)
                        .map(client -> {
                            return new LockedAccount(
                                    client.accountNumber(),
                                    client.details().getFirstName().concat(" " + client.details().getLastName()),
                                    client.details().getGender()
                            );
                        })
                        .collect(Collectors.toList());
            }


            /**
             * Checks if there are no clients available.
             * @throws IllegalStateException if there are no clients available.
             */
            private static void checkIfThereAreClientsAvailable() throws IllegalStateException {
                if (clients.isEmpty()) throw new IllegalStateException("\nTHERE ARE NO CLIENTS AVAILABLE\n");
            }
        }

        /**
         * class for Client account.
         */
        protected static class ClientAcc {
            // TODO: add client functions.
            // TODO: fix bug
            /**
             * Called when a client is logged in.
             */
            private static void client(Scanner scanner) {
                var choice = "";
                do {
                    try {
                        viewClientDetails($an);
                        System.out.println(BLUE_BOLD + ": " + BLUE_BOLD_BRIGHT   + "1" + BLUE_BOLD + " : " + BLUE_BOLD_BRIGHT   + "DEPOSIT");
                        System.out.println(BLUE_BOLD + ": " + YELLOW_BOLD_BRIGHT + "2" + BLUE_BOLD + " : " + YELLOW_BOLD_BRIGHT + "WITHDRAW");
                        System.out.println(BLUE_BOLD + ": " + RED                + "3" + BLUE_BOLD + " : " + RED + "LOAN");
                        System.out.println(BLUE_BOLD + ": " + RED_BOLD_BRIGHT    + "4" + BLUE_BOLD + " : " + RED_BOLD_BRIGHT + "LOGOUT");
                        System.out.print(PURPLE_BOLD + ">>>: " + RESET);
                        choice = scanner.nextLine().trim();
                        switch (choice) {
                            case "1" -> deposit(scanner);
                            case "2", "3" -> {}
                            case "4" -> {
                                System.out.print(RED_BOLD_BRIGHT + "LOGGING OUT");
                                dotLoading();
                            }
                            default -> throw new IllegalStateException(String.format("\nINVALID INPUT: %s\n", choice));
                        }
                        clients.clear();
                        loadClients();
                    } catch (RuntimeException | IllegalAccessException runtimeException) {
                        System.out.println(RED_BOLD_BRIGHT +  runtimeException.getMessage() + RESET);
                        System.out.print(YELLOW_BOLD_BRIGHT + "LOADING");
                        dotLoading();
                    }
                } while (!choice.equals("4"));
            }

            /**
             * Method for depositing.
             * @param scanner a {@code Scanner} object needed for user input.
             */
            public static void deposit(Scanner scanner) throws IllegalAccessException {
                var client = clients.get($an);
                var attempts = client.attempts();
                if (attempts == 0) throw new IllegalAccessException("\nACCOUNT IS LOCKED\nCANNOT PROCEED TRANSACTION\n");
                var isLoggedIn = false;
                while (attempts != 0) {
                    System.out.print(BLUE_BOLD_BRIGHT + "ENTER PIN: ");
                    String pin = scanner.nextLine().trim();

                    if (!client.pin().equals(pin)) {
                        --attempts;
                        System.out.println(RED_BOLD_BRIGHT + "\nINVALID PIN\n" + RESET);
                    }
                    else {
                        isLoggedIn = true;
                        break;
                    }
                    System.out.println("attempts = " + attempts);
                    if (attempts == 0) {
                        CLIENT_SERVICE.updateClientAttemptsByAccountNumber().apply(client.accountNumber(), "0");
                        throw new IllegalAccessException("\nACCOUNT LOCKED\nPLEASE CONTACT THE ADMINISTRATOR TO VERIFY YOUR IDENTITY AND UNLOCK YOUR ACCOUNT\n" + RESET);
                    }
                }

                if (isLoggedIn) {
                    var cash = "";
                    do {
                        try {
                            System.out.println(PURPLE_BOLD_BRIGHT + "\tDEPOSIT\t");
                            System.out.println(YELLOW_BOLD_BRIGHT + ": ENTER CASH AMOUNT : ");
                            System.out.print(RED_BOLD_BRIGHT + ">>>:  " + RESET);
                            cash = scanner.nextLine().trim();
                            if (Validator.isWholeNumber().or(isDecimalNumber()).negate().test(cash)) throw new IllegalStateException("\nCASH SHOULD BE A NUMBER\n");
                            double cashToDeposit = Double.parseDouble(cash);
                            if (cashToDeposit < 100) throw new IllegalStateException("\nCASH AMOUNT TO DEPOSIT SHOULD NOT BE LESS THAN 100\n");
                            Status status = CLIENT_SERVICE.updateClientSavingsByAccountNumber().apply(client.accountNumber(), client.savings() + cashToDeposit);
                            System.out.println(status == Status.SUCCESS ? BLUE_BOLD_BRIGHT + status.name() + RESET : RED_BOLD_BRIGHT + status.name() + RESET);
                            System.out.print(YELLOW_BOLD_BRIGHT + "LOADING");
                            dotLoading();
                        } catch (RuntimeException runtimeException) {
                            System.out.println(RED_BOLD_BRIGHT +  runtimeException.getMessage() + RESET);
                            System.out.print(YELLOW_BOLD_BRIGHT + "LOADING");
                            dotLoading();
                        }
                    } while (Validator.isWholeNumber().or(isDecimalNumber()).negate().test(cash));

                }
            }

            /**
             * Outputs the client details.
             */
            private static void viewClientDetails(String an) throws IllegalAccessException {
                System.out.println(clients.get(an));
            }
        }
    }

    /**
     * The Main interface
     * @param scanner the scanner needed for keyboard input.
     * @return a {@code Role}.
     * @throws IllegalArgumentException if any of the input is not valid.
     * @throws IllegalAccessException if the account does not exist.
     */
    private static Role home(Scanner scanner) throws IllegalArgumentException, IllegalAccessException {
        System.out.println(BLUE_BOLD + "AUTOMATED " + YELLOW_BOLD + "TELLER " + PURPLE_BOLD + "MACHINE" + RESET);
        System.out.println(YELLOW_BRIGHT + "ENTER YOUR ACCOUNT NUMBER" + RESET);
        System.out.print(RED_BOLD_BRIGHT + ">>>: " + RESET);
        var input = scanner.nextLine().trim();
        if (input.equals(SecurityUtil.decrypt(Machine.$adm))) return Role.ADMIN;
        checkAccountNumberInput(input);
        var result = Machine.searchAccount(input);
        if (!result) throw new IllegalAccessException("\nACCOUNT DOES NOT EXIST\n");
        else Machine.$an = input;
        return Role.CLIENT;
    }

    /**
     * Cheks the account number that was inputted if valid ornot.
     * @param accountNumber the account number inputted.
     * @throws IllegalArgumentException if input is invalid.
     */
    private static void checkAccountNumberInput(String accountNumber) throws IllegalArgumentException {
        if (isDecimalNumber().test(accountNumber)) throw new IllegalArgumentException("\nACCOUNT NUMBER IS A WHOLE NUMBER\n");
        else if (Validator.isWholeNumber().negate().test(accountNumber)) throw new IllegalArgumentException("\nACCOUNT NUMBER IS A NUMBER\n");
        else if (Validator.isWholeNumber().negate().test(accountNumber) || accountNumber.length() < 9) throw new IllegalArgumentException("\nACCOUNT NUMBER SHOULD BE 9 DIGITS\n");
    }
    /**
     * Creates a dot loading.
     */
    private static void dotLoading() {
        try {
            for (int i = 1; i <= 3; i++) {
                System.out.print(".");
                TimeUnit.MILLISECONDS.sleep(500);
            }
            System.out.println(RESET + "\n");
        } catch (InterruptedException ignored) {}
    }
}



