import java.util.ArrayList;
import java.util.List;

/**
 * Created by martinpettersson on 22/05/16.
 */
public class ValidityChecker {
    private static boolean validity;
    private static Object candidateData;
    private static List<ValidityCheck> validityCheckList;
    private static final String LOG_FILE_LOCATION = "/error_log/";

    public static void main(String[] args) {
        ArrayList<ValidityCheck> list = new ArrayList<ValidityCheck>();
        ValidityCheckNotNull notNullCheck = new ValidityCheckNotNull();
        ValidityCheckIsPersonalRegistratinonNumber numberCheck =
                new ValidityCheckIsPersonalRegistratinonNumber();
        ValidityCheckNotNull notNullCheck2 = new ValidityCheckNotNull();
        String data = "19910127-3416";
        list.add(notNullCheck); list.add(numberCheck); list.add(notNullCheck2);
        ValidityChecker checker = new ValidityChecker(list, data);
        checker.performValidation();
    }

    public ValidityChecker(List<ValidityCheck> validityCheckList, Object candidateData) {
        this.candidateData = candidateData;
        this.validityCheckList = validityCheckList;
    }

    public static void validate(List<ValidityCheck> validityCheckList, Object candidateData) {
        for (ValidityCheck validityCheck : validityCheckList) {
            if (!validityCheck.validate(candidateData))
                validity = false;
        }

        validity = true;
    }

    public void performValidation() {
        for (ValidityCheck validityCheck : validityCheckList)
            validityCheck.validate(candidateData);
    }
}

abstract class ValidityCheck {
    public abstract boolean validate(Object candidateData);
}

class ValidityCheckNotNull extends ValidityCheck {
    private static boolean validity;

    public boolean validate(Object inputData) {
        System.err.println("validating null");
        try {
            validity = (inputData == null);
        } catch (Exception exception) {
            validity = false;
        } finally {
            return validity;
        }
    }
}

class ValidityCheckIsPersonalRegistratinonNumber extends ValidityCheck {
    private static int civicNumber;
    private static int controlDigit;
    private static boolean validity;

    public boolean validate(Object inputData) {
        System.err.println("validating civic number");

        try {
            String inputString = inputData.toString();
            inputString = inputString.replaceAll("-", "");

            if (inputString.length() < 10 || inputString.length() > 12)
                throw new Exception();

            if (inputString.length() == 12)
                inputString = inputString.substring(2);

            char lastDigit = inputString.charAt(9);
            inputString = inputString.substring(0, 9);

            civicNumber = Integer.parseInt(inputString);
            controlDigit = Integer.parseInt(String.valueOf(lastDigit));


        } catch (Exception exception) {
            System.err.println("Civic number exception");
            validity = false;
            return validity;
        }

        System.err.println("Civic number: " + civicNumber);
        System.err.println("control digit: " + controlDigit);

        // If we get here, then we should have a nine digit integer
        // and a control digit.

        validity = true;
        return validity;
    }
}