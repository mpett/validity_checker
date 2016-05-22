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
        String data = "9101273414";
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
    private static final int NUMBER_SIZE = 9;
    private static String civicNumberString;
    private static int controlDigit;
    private static boolean validity;

    public boolean validate(Object inputData) {
        try {
            String inputString = inputData.toString();
            inputString = inputString.replaceAll("-", "");

            if (inputString.length() < NUMBER_SIZE + 1 || inputString.length() > NUMBER_SIZE + 3)
                throw new Exception();

            if (inputString.length() == 12)
                inputString = inputString.substring(2);

            char lastDigit = inputString.charAt(NUMBER_SIZE);
            inputString = inputString.substring(0, NUMBER_SIZE);

            civicNumberString = inputString;
            controlDigit = Integer.parseInt(String.valueOf(lastDigit));

        } catch (Exception exception) {
            System.err.println("Civic number exception");
            validity = false;
            return validity;
        }

        validity = validateControlNumber();
        return validity;
    }

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

    private int sumOfDigits(int n) {
        String digits = new Integer(n).toString();
        int sum = 0;
        for (char c: digits.toCharArray())
            sum += c - '0';
        return sum;
    }
}