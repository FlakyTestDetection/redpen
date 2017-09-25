/**
 * redpen: a text inspection tool
 * Copyright (c) 2014-2015 Recruit Technologies Co., Ltd. and contributors
 * (see CONTRIBUTORS.md)
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cc.redpen.validator.sentence;

import cc.redpen.RedPenException;
import cc.redpen.model.Document;
import cc.redpen.model.Sentence;
import cc.redpen.tokenizer.WhiteSpaceTokenizer;
import cc.redpen.validator.ValidationError;
import cc.redpen.validator.ValidatorFactory;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WeakExpressionValidatorTest {
    @Test
    void testSingleSentence() throws RedPenException {
        WeakExpressionValidator validator = (WeakExpressionValidator) ValidatorFactory.getInstance("WeakExpression");

        List<Document> documents = new ArrayList<>();
        documents.add(
                Document.builder(new WhiteSpaceTokenizer())
                        .addSection(1)
                        .addParagraph()
                        .addSentence(new Sentence("As a matter of fact, some things are very small.", 1))
                        .addSentence(new Sentence("Things are mentioned a lot.", 2))
                        .build());

        Sentence st = documents.get(0).getLastSection().getParagraph(0).getSentence(0);
        List<ValidationError> errors = new ArrayList<>();
        validator.setErrorList(errors);
        validator.validate(st);

        assertEquals(4, errors.size(), st.toString());
        st = documents.get(0).getLastSection().getParagraph(0).getSentence(1);
        errors = new ArrayList<>();
        validator.setErrorList(errors);
        validator.validate(st);
        assertEquals(2, errors.size(), st.toString());
    }
}
