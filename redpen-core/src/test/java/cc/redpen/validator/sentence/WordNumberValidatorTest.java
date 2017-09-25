/**
 * redpen: a text inspection tool
 * Copyright (c) 2014-2015 Recruit Technologies Co., Ltd. and contributors
 * (see CONTRIBUTORS.md)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cc.redpen.validator.sentence;

import cc.redpen.RedPen;
import cc.redpen.RedPenException;
import cc.redpen.model.Document;
import cc.redpen.model.Sentence;
import cc.redpen.validator.BaseValidatorTest;
import cc.redpen.validator.ValidationError;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class WordNumberValidatorTest extends BaseValidatorTest {

    WordNumberValidatorTest() {
        super("WordNumber");
    }

    @Test
    void testWithShortSentence() {
        WordNumberValidator maxWordNumberValidator = new WordNumberValidator();
        Sentence str = new Sentence(
                "this sentence is short.", 0);
        List<ValidationError> errors = new ArrayList<>();
        maxWordNumberValidator.setErrorList(errors);
        maxWordNumberValidator.validate(str);
        assertNotNull(errors);
        assertEquals(0, errors.size());
    }

    @Test
    void testWithLongSentence() {
        WordNumberValidator maxWordNumberValidator = new WordNumberValidator();
        Sentence str = new Sentence(
                "this sentence is very very very very very very very very very very" +
                        " very very very very very very very very very very very very very very long", 0);
        List<ValidationError> errors = new ArrayList<>();
        maxWordNumberValidator.setErrorList(errors);
        maxWordNumberValidator.validate(str);
        assertNotNull(errors);
        assertEquals(0, errors.size());
    }

    @Test
    void testWithZeroLengthSentence() {
        WordNumberValidator maxWordNumberValidator = new WordNumberValidator();
        Sentence str = new Sentence("", 0);
        List<ValidationError> errors = new ArrayList<>();
        maxWordNumberValidator.setErrorList(errors);
        maxWordNumberValidator.validate(str);
        assertNotNull(errors);
        assertEquals(0, errors.size());
    }

    @Test
    void testSentenceWithCommas() throws RedPenException {
        // NOTE: the following sentence contains 29 words.
        Document document = prepareSimpleDocument("There is no real path, so first follow the line of the foot of the rocks past Kawa, then cut straight up to the next level of slabs.");
        RedPen redPen = new RedPen(config);
        Map<Document, List<ValidationError>> errors = redPen.validate(singletonList(document));
        assertEquals(0, errors.get(document).size());
    }



}
