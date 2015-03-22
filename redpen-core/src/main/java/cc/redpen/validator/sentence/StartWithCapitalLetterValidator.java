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

import cc.redpen.RedPenException;
import cc.redpen.model.Sentence;
import cc.redpen.tokenizer.TokenElement;
import cc.redpen.util.WordListExtractor;
import cc.redpen.validator.ValidationError;
import cc.redpen.validator.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

/**
 * Check if the input sentence start with a capital letter.
 */
final public class StartWithCapitalLetterValidator extends Validator {
    private static final String DEFAULT_RESOURCE_PATH = "default-resources/capital-letter-exception-list";
    private static final Logger LOG =
            LoggerFactory.getLogger(SpellingValidator.class);
    private Set<String> whiteList;
    private Set<String> customWhiteList = new HashSet<>();

    @Override
    public List<String> getSupportedLanguages() {
        return Arrays.asList(Locale.ENGLISH.getLanguage());
    }

    @Override
    public void validate(List<ValidationError> errors, Sentence sentence) {
        String content = sentence.getContent();
        List<TokenElement> tokens = sentence.getTokens();
        String headWord = "";
        for (TokenElement token : tokens) {
            if (!token.getSurface().equals("")) { // skip white space
                headWord = token.getSurface();
                break;
            }
        }

        if (tokens.size() == 0 || this.whiteList.contains(headWord) || this.customWhiteList.contains(headWord)) {
            return;
        }

        char headChar = '≡';
        for (char ch : content.toCharArray()) {
            if (ch != ' ') {
                headChar = ch;
                break;
            }
        }

        if (headChar == '≡') {
            return;
        }

        if (Character.isLowerCase(headChar)) {
            errors.add(createValidationError(sentence, headChar));
        }
    }

    @Override
    protected void init() throws RedPenException {

        String defaultDictionaryFile = DEFAULT_RESOURCE_PATH
                + "/default-capital-case-exception-list.dat";
        whiteList = loadWordListFromResource(defaultDictionaryFile, "capital letter exception dictionary", false);

        WordListExtractor extractor = new WordListExtractor();
        Optional<String> confFile = getConfigAttribute("dict");
        if(confFile.isPresent()){
            try {
                LOG.info("user dictionary file is " + confFile.get());
                extractor.load(new FileInputStream(confFile.get()));
                customWhiteList = extractor.get();
                LOG.info("Succeeded to load specified user dictionary.");
            } catch (IOException e) {
                throw new RedPenException("Failed to load user dictionary.", e);
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StartWithCapitalLetterValidator that = (StartWithCapitalLetterValidator) o;

        if (whiteList != null ? !whiteList.equals(that.whiteList) : that.whiteList != null) return false;
        return !(customWhiteList != null ? !customWhiteList.equals(that.customWhiteList) : that.customWhiteList != null);

    }

    @Override
    public int hashCode() {
        int result = whiteList != null ? whiteList.hashCode() : 0;
        result = 31 * result + (customWhiteList != null ? customWhiteList.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "StartWithCapitalLetterValidator{" +
                "whiteList=" + whiteList +
                ", customWhiteList=" + customWhiteList +
                '}';
    }
}
