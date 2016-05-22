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
        list.add(notNullCheck); list.add(numberCheck);
        String data = "lol";
        numberCheck.performValidation(data);
        list.get(1).performValidation(data);
        ValidityChecker checker = new ValidityChecker(list, data);
        checker.validate();
    }

    public ValidityChecker(List<ValidityCheck> validityCheckList, Object candidateData) {
        this.candidateData = candidateData;
        this.validityCheckList = validityCheckList;
    }

    public static void validate(List<ValidityCheck> validityCheckList, Object candidateData) {
        for (ValidityCheck validityCheck : validityCheckList) {
            if (!validityCheck.performValidation(candidateData))
                validity = false;
        }

        validity = true;
    }

    public void validate() {
        for (ValidityCheck validityCheck : validityCheckList) {
            if (!validityCheck.performValidation(candidateData))
                validity = false;
        }

        validity = true;
    }
}

abstract class ValidityCheck {
    public abstract boolean performValidation(Object candidateData);
    //Empty
}

class ValidityCheckNotNull extends ValidityCheck {

    private static boolean validity;

    public boolean performValidation(Object inputData) {
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
    private static String personalRegistrationNumber;

    private static boolean validity;

    public boolean performValidation(Object inputData) {
        System.err.println("validating civic number");
        try {
            personalRegistrationNumber = inputData.toString();
        } catch (Exception exception) {
            validity = false;
        }

        return validity;
    }
}