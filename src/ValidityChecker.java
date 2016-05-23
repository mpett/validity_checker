import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * A ValidityChecker that can be used to validate candidate data along
 * with one or several ValidityChecks in no particular order.
 *
 * @author Martin Pettersson
 */
public class ValidityChecker {
    private static boolean validity;
    private static Object candidateData;
    private static List<ValidityCheck> validityCheckList;
    private static Logger errorLogger;
    private static final String LOG_FILE_LOCATION = "ErrorLog.log";


    public static void main(String[] args) {
        ArrayList<ValidityCheck> list = new ArrayList<ValidityCheck>();
        ValidityCheckNotNull notNullCheck = new ValidityCheckNotNull();
        ValidityCheckIsPersonalRegistrationNumber numberCheck =
                new ValidityCheckIsPersonalRegistrationNumber();
        ValidityCheckNotNull notNullCheck2 = new ValidityCheckNotNull();
        String data = "lol";
        ArrayList<String> strings = new ArrayList<String>();
        strings.add(data);
        list.add(notNullCheck); list.add(numberCheck); list.add(notNullCheck2);
        ValidityChecker checker = new ValidityChecker(list, strings);
        checker.performValidation();
    }

    /**
     * Static validation
     * @param validityCheckList
     * @param candidateData
     */
    public static void validate(List<ValidityCheck> validityCheckList, Object candidateData) {
        for (ValidityCheck validityCheck : validityCheckList) {
            if (!validityCheck.validate(candidateData)) {
                validity = false;
                errorLogger.info(validityCheck.logMessage());
            }
        }
        validity = true;
    }

    /**
     * ValidityChecker constructor
     * @param validityCheckList
     * @param candidateData
     */
    public ValidityChecker(List<ValidityCheck> validityCheckList, Object candidateData) {
        this.candidateData = candidateData;
        this.validityCheckList = validityCheckList;
        prepareErrorLog();
    }

    public void performValidation() {
        for (ValidityCheck validityCheck : validityCheckList) {
            if (!validityCheck.validate(candidateData)) {
                validity = false;
                errorLogger.info(validityCheck.logMessage());
            }
        }
        validity = true;
    }

    private void prepareErrorLog() {
        errorLogger = Logger.getLogger("MyLog");
        FileHandler fh;
        try {
            fh = new FileHandler(LOG_FILE_LOCATION);
            errorLogger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

/**
 * Abstract base class for ValidityChecks. You can combine
 * several of these for use in a ValidityChecker.
 */
abstract class ValidityCheck {
    public abstract boolean validate(Object candidateData);

    public abstract String logMessage();
}

/**
 * A ValidityCheck to control if passed candidate data of a
 * ValidityChecker is not null.
 */
class ValidityCheckNotNull extends ValidityCheck {
    private boolean validity;
    private String dataType;

    /**
     * Checks if inputData is not null.
     * @param inputData
     * @return
     */
    public boolean validate(Object inputData) {
        try {
            dataType = inputData.getClass().getTypeName();
        } catch (Exception exception) {
            dataType = "NON RESOLVABLE";
        }

        try {
            if (inputData != null) validity = true;
        } catch (Exception exception) {
            validity = false;
        } finally {
            return validity;
        }
    }

    public String logMessage() {
        return "Candidate data of type "
                + dataType + " failed ValidityCheckNotNull";
    }
}

/**
 * A ValidityCheck to control if passed candidate data of a
 * ValidityChecker is a valid Swedish personal registration
 * number.
 */
class ValidityCheckIsPersonalRegistrationNumber extends ValidityCheck {
    private final int NUMBER_SIZE = 9;
    private String civicNumberString;
    private int controlDigit;
    private boolean validity;
    private String dataType;

    /**
     * Checks if inputData is a valid civic number.
     * @param inputData
     * @return
     */
    public boolean validate(Object inputData) {
        try {
            dataType = inputData.getClass().getTypeName();
        } catch (Exception exception) {
            dataType = "NON RESOLVABLE";
        }

        try {
            String inputString = inputData.toString();
            inputString = inputString.replaceAll("-", "");

            if (inputString.length() < NUMBER_SIZE + 1 || inputString.length() > NUMBER_SIZE + 3)
                throw new Exception();

            if (inputString.length() == NUMBER_SIZE + 3)
                inputString = inputString.substring(2);

            char lastDigit = inputString.charAt(NUMBER_SIZE);
            inputString = inputString.substring(0, NUMBER_SIZE);
            civicNumberString = inputString;
            controlDigit = Integer.parseInt(String.valueOf(lastDigit));
        } catch (Exception exception) {
            validity = false;
            return validity;
        }

        validity = validateControlNumber();
        return validity;
    }

    public String logMessage() {
        return "Candidate data of type " + dataType +
                " failed ValidityCheckIsPersonalRegistrationNumber";
    }

    /**
     * An algorithm that checks if the final digit of a civic number is valid.
     * @return
     */
    private boolean validateControlNumber() {
        int controlSum = 0;

        for (int characterIndex = 0; characterIndex < NUMBER_SIZE; ++characterIndex) {
            char digitCharacter = civicNumberString.charAt(characterIndex);
            int digit = Integer.parseInt(String.valueOf(digitCharacter));
            if ((characterIndex + 1) % 2 != 0) digit *= 2;
            if (characterIndex == 0 || characterIndex == NUMBER_SIZE - 1)
                digit = sumOfDigits(digit);
            controlSum += digit;
        }

        controlSum %= 10;
        controlSum = 10 - controlSum;
        controlSum %= 10;

        return (controlSum == controlDigit);
    }

    /**
     * Computes the digit sum of the integer parameter n.
     * @param n
     * @return
     */
    private int sumOfDigits(int n) {
        String digits = new Integer(n).toString();
        int sum = 0;
        for (char c : digits.toCharArray())
            sum += c - '0';
        return sum;
    }
}