import java.math.BigInteger;
import java.util.ArrayList;

/**
 * A sample test suite of the ValidityChecker.
 *
 * All of the following assertions should succeed and
 * four different error logs should be printed.
 *
 * Remember to add the -ea option in your JVM to
 * enable assertions.
 *
 * @author Martin Pettersson
 */
public class TestValidityChecker {
    public static void executeTests() {
        boolean result;

        // First we create a new ValidityChecker object with
        // three validity checks.
        ArrayList<ValidityCheck> list = new ArrayList<ValidityCheck>();

        // The three different checks.
        ValidityCheckNotNull notNullCheck = new ValidityCheckNotNull();
        ValidityCheckIsPersonalRegistrationNumber numberCheck =
                new ValidityCheckIsPersonalRegistrationNumber();
        ValidityCheckNotNull notNullCheck2 = new ValidityCheckNotNull();

        // Perform the validation on a valid personal registration number
        // in a String representation.
        String data = "19780202-2389";
        list.add(notNullCheck); list.add(numberCheck); list.add(notNullCheck2);
        ValidityChecker checker = new ValidityChecker(list, data);

        // The ValidityChecker should now be valid.
        // No error log is printed.
        result = checker.performValidation();
        assert result == true ;

        // We change the candidate data to another valid personal number of a
        // different format and as a BigInteger.
        BigInteger number = new BigInteger("9101273416");
        checker.setCandidateData(number);

        // The ValidityChecker should still be valid.
        // No error log is printed.
        result = checker.performValidation();
        assert result == true ;

        // We change the final control number, thus making the personal number invalid.
        number = new BigInteger("9101273417");
        checker.setCandidateData(number);

        // The ValidityChecker should now be invalid.
        // An error log with one message should be printed.
        result = checker.performValidation();
        assert result == false ;

        // We now remove ValidityCheckIsPersonalRegistrationNumber from
        // the set of available ValidityChecks.
        list.remove(numberCheck);
        checker.setValidityCheckList(list);

        // The ValidityChecker should now be valid.
        // No additional error log is printed.
        result = checker.performValidation();
        assert result == true;

        // The personal number ValidityCheck is added again.
        list.add(numberCheck);
        checker.setValidityCheckList(list);

        // The ValidityChecker should now be invalid.
        // An additional error log with one message should be printed.
        result = checker.performValidation();
        assert result == false ;

        // The candidate data is set to null.
        number = null;
        checker.setCandidateData(number);

        // The ValidityChecker should now be invalid.
        // An additional error log with three messages should be printed.
        result = checker.performValidation();
        assert result == false ;

        // This time, we enter another valid personal number
        // as a primitive type.
        long personalNumber = Long.parseLong("198204112380");
        checker.setCandidateData(personalNumber);

        // The ValidityChecker should now be valid.
        // No additional error log is printed.
        result = checker.performValidation();
        assert result == true ;

        // Make the personal number invalid,
        // but do not update the ValidityChecker object.
        personalNumber = -1;

        // We make a static call to ValidityChecker with the new
        // paramater. An additional log file with one entry should be written.
        boolean staticResult = ValidityChecker.validate(list, personalNumber);
        assert staticResult == false ;

        // The original object should still be valid,
        // no additional error log is written.
        result = checker.performValidation();
        assert result == true ;

        // Remove the personal number ValidityCheck from the list,
        // but do not update the original object.
        list.remove(numberCheck);

        // Make a static call with the new list of ValidityChecks.
        // It should be valid and no additional error log should be written.
        staticResult = ValidityChecker.validate(list, personalNumber);
        assert staticResult == true ;

        // The original object should still be valid,
        // no additional error log is written.
        result = checker.performValidation();
        assert result == true ;

        // A ValidityChecker with an empty list of ValidityChecks should be valid.
        list.remove(notNullCheck); list.remove(notNullCheck2);
        staticResult = ValidityChecker.validate(list, null);
        assert staticResult == true ;

        // A ValidityChecker with no list should throw an exception.
        try {
            ValidityChecker.validate(null, null);
        } catch (NullPointerException exception) {
            assert true;
        } finally {
            System.out.println("All ValidityChecker tests have passed.");
        }
    }
}
