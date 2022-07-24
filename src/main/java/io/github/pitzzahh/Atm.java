package io.github.pitzzahh;

import java.util.List;
import java.util.Arrays;
import java.util.Optional;
import java.util.Scanner;
import java.time.LocalDate;
import java.util.concurrent.TimeUnit;
import io.github.pitzzahh.entity.Client;
import io.github.pitzzahh.service.ClientService;
import com.github.pitzzahh.utilities.SecurityUtil;
import com.github.pitzzahh.utilities.classes.Person;
import com.github.pitzzahh.utilities.classes.enums.Role;
import com.github.pitzzahh.utilities.classes.enums.Status;
import com.github.pitzzahh.utilities.validation.Validator;
import com.github.pitzzahh.utilities.classes.enums.Gender;
import static com.github.pitzzahh.utilities.classes.TextColors.*;

/**
 * Automated Teller Machine.
 * Details are encrypted to avoid data leak.
 * @author pitzzahh
 */
public class Atm {
    private static final ClientService CLIENT_SERVICE = new ClientService();

    /**
     * main method.
     * @param args arguments.
     */
    @SuppressWarnings("InfiniteLoopStatement")
    public static void main(String[] args)  {
        final Scanner scanner = new Scanner(System.in);
        CLIENT_SERVICE.setDataSource().accept(ClientService.getDataSource());
        for (;;)  {
            try {
                switch (home(scanner)) {
                    case ADMIN -> Machine.AdminAcc.admin(scanner);
                    case CLIENT -> Machine.ClientAcc.client(scanner);
                }
            } catch (RuntimeException | IllegalAccessException runtimeException) {
                System.out.println(RED_BOLD_BRIGHT +  runtimeException.getMessage() + RESET);
            }
        }
    }

    /**
     * The automated teller machine that handles most of the process.
     */
    static class Machine {
        private static final String $adm = "?cl0m0#sq3s/q";
        private static String $an;
        /**
         * Searches the {@code Hashtable<String, Client>}, checks if the account number exists.
         * @param n the account number to search.
         * @return {@code true} if the account number exists.
         */
        private static boolean searchAccount(String n) {
            return CLIENT_SERVICE.getAllClients().get().stream()
                                             .anyMatch(client -> client.isPresent() && client.get().accountNumber().equals(n));
        }

        /**
         * class for Admin account.
         */
        private static class AdminAcc {
            // TODO: add admin functions.
            /**
             * called then an admin is logged in.
             * @param scanner the scanner needed for keyboard input.
             * @throws IllegalArgumentException if any of the input is not valid from the detail(scanner) method.
             */
            private static void admin(Scanner scanner) throws IllegalArgumentException {
                String choice = "";
                do {
                    try {
                        System.out.println(RED_BOLD  + ": " + BLUE_BOLD_BRIGHT   + "1" + RED_BOLD + " : " + BLUE_BOLD_BRIGHT + "ADD CLIENT");
                        System.out.println(RED_BOLD  + ": " + YELLOW_BOLD_BRIGHT + "2" + RED_BOLD + " : " + YELLOW_BOLD_BRIGHT + "REMOVE CLIENT");
                        System.out.println(RED_BOLD  + ": " + GREEN_BOLD_BRIGHT  + "3" + RED_BOLD + " : " + GREEN_BOLD_BRIGHT + "VIEW CLIENTS");
                        System.out.println(RED_BOLD  + ": " + RED                + "4" + RED_BOLD + " : " + RED + "LOGOUT");
                        System.out.print(PURPLE_BOLD + ">>>: " + RESET);
                        choice = scanner.nextLine().trim();
                        switch (choice) {
                            case "1" -> {
                                System.out.println(BLUE_BOLD_BRIGHT + "CREATE CLIENT" + RESET);
                                createClient(scanner);
                            }
                            case "2" -> System.out.println(removeClient(scanner));
                            case "3" -> viewAllClients();
                            case "4" -> {
                                System.out.print(RED_BOLD_BRIGHT + "LOGGING OUT");
                                dotLoading();
                            }
                            default -> throw new IllegalStateException(String.format("\nINVALID INPUT: %s\n", choice));
                        }
                    } catch (RuntimeException runtimeException) {
                        System.out.println(RED_BOLD_BRIGHT +  runtimeException.getMessage() + RESET);
                        System.out.print(YELLOW_BOLD_BRIGHT + "LOADING");
                        dotLoading();
                    }
                } while (!choice.equals("4"));
            }

            /**
             * Creates a new {@code Client} and adds it to the {@code Hashtable<String, Client>}
             * @param scanner the scanner needed for keyboard input.
             * @throws IllegalArgumentException if any of the input is not valid from the detail(scanner) method.
             */
            private static void createClient(Scanner scanner) throws IllegalArgumentException {
                Client client = details(scanner);
                CLIENT_SERVICE.saveClient().apply(
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

                String an = "";
                String pin = "";
                String firstName;
                String lastName;
                String gender = "";
                String address;
                String birthDate = "";

                do {
                    try {
                        System.out.print("Enter account number: ");
                        an = scanner.nextLine().trim();
                        if (Validator.isWholeNumber().negate().test(an) || an.length() < 9) throw new IllegalArgumentException("\nACCOUNT NUMBER SHOULD BE 9 DIGITS\n");
                        if (searchAccount(an)) throw new IllegalArgumentException(String.format("\nCLIENT WITH ACCOUNT NUMBER : %s ALREADY EXISTS\n", an));
                    } catch (RuntimeException runtimeException) {
                        System.out.println(RED_BOLD_BRIGHT +  runtimeException.getMessage() + RESET);
                    }
                } while (Validator.isIdValid().negate().test(an) || an.length() < 9 || searchAccount(an));

                do {
                    try {
                        System.out.print("Enter account pin   : ");
                        pin = scanner.nextLine().trim();
                        if (Validator.isWholeNumber().negate().test(pin) || pin.length() < 6) throw new IllegalArgumentException("\nPIN SHOULD BE 6 DIGITS\n");
                    } catch (RuntimeException runtimeException) {
                        System.out.println(RED_BOLD_BRIGHT +  runtimeException.getMessage() + RESET);
                    }
                } while (Validator.isWholeNumber().negate().test(pin) || pin.length() < 6);

                System.out.print("Enter client first name: ");
                firstName = scanner.nextLine().toUpperCase().trim();

                System.out.print("Enter client last name : ");
                lastName = scanner.nextLine().toUpperCase().trim();

                do {
                    try {
                        System.out.print("Enter client gender    : ");
                        gender = scanner.nextLine().toUpperCase().trim();
                        if (Validator.isGenderValid().negate().test(gender)) throw new IllegalArgumentException("\nUnknown Gender, please select from the list: " + Arrays.toString(Arrays.stream(Gender.values()).map(Gender::name).toArray()) + "\n");
                    } catch (RuntimeException runtimeException) {
                        System.out.println(RED_BOLD_BRIGHT +  runtimeException.getMessage() + RESET);
                    }
                } while (Validator.isGenderValid().negate().test(gender));

                System.out.print("Enter client address   : ");
                address = scanner.nextLine().toUpperCase().trim();

                do {
                    try {
                        System.out.print("Enter client birthdate : ");
                        birthDate = scanner.nextLine().trim();
                        if (Validator.isBirthDateValid().negate().test(birthDate)) throw new IllegalArgumentException("\nINVALID BIRTHDATE FORMAT, VALID FORMAT: (YYYY-MM-dd)\n");
                    } catch (RuntimeException runtimeException) {
                        System.out.println(RED_BOLD_BRIGHT +  runtimeException.getMessage() + RESET);
                    }
                } while (Validator.isBirthDateValid().negate().test(birthDate));

                List<String> b = Arrays.stream(birthDate.split("-")).toList();
                int day = Integer.parseInt(b.get(2));
                int month = Integer.parseInt(b.get(1));
                int year = Integer.parseInt(b.get(0));
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
             * @return a {@code Status} of removal.
             */
            public static Status removeClient(Scanner scanner) {
                System.out.println(RED_BOLD_BRIGHT + ":ENTER ACCOUNT NUMBER TO REMOVE:");
                System.out.print(PURPLE_BOLD + ">>>: " + RESET);
                $an = scanner.nextLine().trim();
                return CLIENT_SERVICE.removeClientByAccountNumber().apply($an);
            }

            /**
             * Prints all the clients details.
             */
            private static void viewAllClients() {
                CLIENT_SERVICE.getAllClients()
                        .get()
                        .forEach(client -> System.out.println(client.isPresent() ? client.get() : "ERROR 404"));
            }
        }

        /**
         * class for Client account.
         */
        private static class ClientAcc {
            // TODO: add client functions.
            /**
             * Called when a client is logged in.
             */
            private static void client(Scanner scanner) {
                String choice = "";
                do {
                    try {
                        viewClientDetails(Machine.$an);
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
                    } catch (RuntimeException | IllegalAccessException runtimeException) {
                        System.out.println(RED_BOLD_BRIGHT +  runtimeException.getMessage() + RESET);
                        System.out.print(YELLOW_BOLD_BRIGHT + "LOADING");
                        dotLoading();
                    }
                } while (!choice.equals("4"));
            }

            /**
             * Function for depositing.
             * @param scanner a {@code Scanner} object needed for user input.
             */
            public static void deposit(Scanner scanner) throws IllegalAccessException {
                Optional<Client> client = CLIENT_SERVICE.getClientByAccountNumber().apply(Machine.$an);
                int attempts = client.map(Client::attempts).orElse(0);
                if (attempts == 0) throw new IllegalAccessException("\nACCOUNT IS LOCKED\nCANNOT PROCEED TRANSACTION\n");
                boolean isLoggedIn = false;
                while (attempts != 0) {
                    System.out.print(BLUE_BOLD_BRIGHT + "ENTER PIN: ");
                    String pin = scanner.nextLine().trim();

                    if (!client.get().pin().equals(pin)) {
                        --attempts;
                        System.out.println(RED_BOLD_BRIGHT + "\nINVALID PIN\n" + RESET);
                    }
                    else {
                        isLoggedIn = true;
                        break;
                    }
                    if (attempts == 0) {
                        CLIENT_SERVICE.updateClientAttemptsByAccountNumber().apply(client.get().accountNumber(), "0");
                        throw new IllegalAccessException("\nACCOUNT LOCKED\nPLEASE CONTACT THE ADMINISTRATOR TO VERIFY YOUR IDENTITY AND UNLOCK YOUR ACCOUNT\n" + RESET);
                    }
                }

                if (isLoggedIn) {
                    String cash = "";
                    do {
                        try {
                            System.out.println(PURPLE_BOLD_BRIGHT + "\tDEPOSIT\t");
                            System.out.println(YELLOW_BOLD_BRIGHT + ": ENTER CASH AMOUNT : ");
                            System.out.print(RED_BOLD_BRIGHT + ">>>:  " + RESET);
                            cash = scanner.nextLine().trim();
                            if (Validator.isWholeNumber().or(Validator.isDecimalNumber()).negate().test(cash)) throw new IllegalStateException("\nCASH SHOULD BE A NUMBER\n");
                            double cashToDeposit = Double.parseDouble(cash);
                            if (cashToDeposit < 100) throw new IllegalStateException("\nCASH AMOUNT TO DEPOSIT SHOULD NOT BE LESS THAN 100\n");
                            Status status = CLIENT_SERVICE.updateClientSavingsByAccountNumber().apply(client.get().accountNumber(), client.get().savings() + cashToDeposit);
                            System.out.println(status == Status.SUCCESS ? BLUE_BOLD_BRIGHT + status.name() + RESET : RED_BOLD_BRIGHT + status.name() + RESET);
                            System.out.print(YELLOW_BOLD_BRIGHT + "LOADING");
                            dotLoading();
                        } catch (RuntimeException runtimeException) {
                            System.out.println(RED_BOLD_BRIGHT +  runtimeException.getMessage() + RESET);
                            System.out.print(YELLOW_BOLD_BRIGHT + "LOADING");
                            dotLoading();
                        }
                    } while (Validator.isWholeNumber().or(Validator.isDecimalNumber()).negate().test(cash));

                }
            }

            /**
             * Outputs the client details.
             */
            private static void viewClientDetails(String an) throws IllegalAccessException {
                System.out.println(CLIENT_SERVICE.getClientByAccountNumber().apply(an).orElseThrow(() -> new IllegalAccessException(String.format("CANNOT FIND ACCOUNT: %s", an))));
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
        if (Validator.isDecimalNumber().test(input)) throw new IllegalArgumentException("\nACCOUNT NUMBER IS A WHOLE NUMBER\n");
        if (Validator.isWholeNumber().negate().test(input)) throw new IllegalArgumentException("\nACCOUNT NUMBER IS A NUMBER\n");
        var result = Machine.searchAccount(input);
        if (!result) throw new IllegalAccessException("\nACCOUNT DOES NOT EXIST\n");
        else Machine.$an = input;
        return Role.CLIENT;
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



