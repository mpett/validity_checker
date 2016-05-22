import java.util.List;

/**
 * Created by martinpettersson on 22/05/16.
 */
public class ValidityChecker {
    private static boolean validity;
    private static Object candidateData;
    private static List<ValidityCheck> validityCheckList;
    private static final String LOG_FILE_LOCATION = "/error_log/";

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

class ValidityCheck {
    public static boolean validity;

    public ValidityCheck() {
        validity = true;
    }

    public static boolean performValidation(Object inputData) {
        return validity;
    }
}

class ValidityCheckNotNull extends ValidityCheck {
    public static boolean performValidation(Object inputData) {
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

    public static boolean performValidation(Object inputData) {
        try {
            personalRegistrationNumber = inputData.toString();
        } catch (Exception exception) {
            validity = false;
        }

        return validity;
    }
}