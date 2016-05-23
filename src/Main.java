import java.util.ArrayList;

/**
 * Main class for testing a ValidityChecker.
 *
 * Remember to add the -ea option in your JVM to
 * enable assertions.
 *
 * @author Martin Pettersson
 */
public class Main {
    public static void main(String[] args) {
        // ----------------------------------------------------------------------------------
        // Example usage of a ValidityChecker
        // ----------------------------------------------------------------------------------

        ArrayList<ValidityCheck> validityChecks = new ArrayList<ValidityCheck>();

        ValidityCheckNotNull notNullCheck = new ValidityCheckNotNull();
        ValidityCheckIsPersonalRegistrationNumber isPersonalRegistrationNumber =
                new ValidityCheckIsPersonalRegistrationNumber();

        validityChecks.add(notNullCheck);
        validityChecks.add(isPersonalRegistrationNumber);

        String candidateData = "19780202-2389";

        ValidityChecker validityChecker = new ValidityChecker(validityChecks, candidateData);

        // This statement will return true.
        validityChecker.performValidation();

        // ----------------------------------------------------------------------------------
        // Execute the test suite
        // ----------------------------------------------------------------------------------

        TestValidityChecker.executeTests();
    }
}
