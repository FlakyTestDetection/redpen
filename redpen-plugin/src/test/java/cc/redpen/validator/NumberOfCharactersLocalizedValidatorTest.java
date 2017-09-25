package cc.redpen.validator;

import cc.redpen.RedPenException;
import cc.redpen.model.Sentence;
import cc.redpen.validator.sentence.NumberOfCharactersLocalizedValidator;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;


class NumberOfCharactersLocalizedValidatorTest extends Validator {
    @Test
    void testValidationErrorCreation() throws RedPenException {
        Validator validator = new NumberOfCharactersLocalizedValidator();
        validator.setLocale(Locale.ENGLISH);
        List<ValidationError> validationErrors = new ArrayList<>();
        validator.setErrorList(validationErrors);
        Sentence sentence = new Sentence("sentence", 1);
        StringBuilder longString = new StringBuilder("long sentence.");
        for (int i = 0; i < 7; i++) {
            longString.append(longString.toString());
        }
        // longString is 1792 characters long.
        Sentence longSentence = new Sentence(longString.toString() , 1);

        validator.validate(sentence);
        validator.validate(longSentence);
        assertEquals(2, validationErrors.size());
        assertEquals("Sentence is shorter than 100 characters long.", validationErrors.get(0).getMessage());
        assertEquals("Sentence is longer than 1,000 characters long.", validationErrors.get(1).getMessage());


        validator.setLocale(Locale.JAPANESE);
        validationErrors = new ArrayList<>();
        validator.setErrorList(validationErrors);
        validator.validate(sentence);
        validator.validate(longSentence);
        assertEquals(2, validationErrors.size());
        assertEquals("文が100文字より短いです。", validationErrors.get(0).getMessage());
        assertEquals("文が1,000文字より長いです。", validationErrors.get(1).getMessage());

    }
}
