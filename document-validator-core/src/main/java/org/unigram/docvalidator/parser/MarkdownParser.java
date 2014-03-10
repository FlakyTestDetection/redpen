/**
 * redpen: a text inspection tool
 * Copyright (C) 2014 Recruit Technologies Co., Ltd. and contributors
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
package org.unigram.docvalidator.parser;

import org.apache.commons.io.IOUtils;
import org.pegdown.Extensions;
import org.pegdown.ParsingTimeoutException;
import org.pegdown.PegDownProcessor;
import org.pegdown.ast.RootNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.unigram.docvalidator.parser.markdown.ToFileContentSerializer;
import org.unigram.docvalidator.store.FileContent;
import org.unigram.docvalidator.store.Section;
import org.unigram.docvalidator.store.Sentence;
import org.unigram.docvalidator.util.DocumentValidatorException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Parser for Markdown format.<br/>
 * <p/>
 * Markdown Syntax @see http://daringfireball.net/projects/markdown/
 */
public class MarkdownParser extends BasicDocumentParser {

  private PegDownProcessor pegDownProcessor = new PegDownProcessor(
      Extensions.HARDWRAPS
          + Extensions.AUTOLINKS
          + Extensions.FENCED_CODE_BLOCKS);


  @Override
  public FileContent generateDocument(String fileName)
      throws DocumentValidatorException {
    InputStream inputStream = this.loadStream(fileName);
    FileContent fileContent = this.generateDocument(inputStream);
    if (fileContent != null) {
      fileContent.setFileName(fileName);
    }
    return fileContent;
  }

  @Override
  public FileContent generateDocument(InputStream inputStream)
      throws DocumentValidatorException {
    FileContent fileContent = null;

    StringBuilder sb = new StringBuilder();
    String line;
    int charCount = 0;
    List<Integer> lineList = new ArrayList<Integer>();
    BufferedReader br = null;

    try {
      br = createReader(inputStream);
      while ((line = br.readLine()) != null) {
        sb.append(line);
        sb.append("\n");
        // TODO surrogate pair ?
        charCount += line.length() + 1;
        lineList.add(charCount);
      }

      fileContent = new FileContent();
      List<Sentence> headers = new ArrayList<Sentence>();
      headers.add(new Sentence("", 0));
      Section currentSection = new Section(0, headers);
      fileContent.appendSection(currentSection);

      // TODO create fileContent after parsing... overhead...
      RootNode rootNode =
          pegDownProcessor.parseMarkdown(sb.toString().toCharArray());
      ToFileContentSerializer serializer =
          new ToFileContentSerializer(fileContent,
              lineList, this.getSentenceExtractor());
      fileContent = serializer.toFileContent(rootNode);
    } catch (ParsingTimeoutException e) {
      throw new DocumentValidatorException("Failed to parse timeout");
    } catch (IOException e) {
      throw new DocumentValidatorException("Failed to read lines");
    } finally {
      IOUtils.closeQuietly(br);
    }
    return fileContent;
  }


  private static final Logger LOG =
      LoggerFactory.getLogger(MarkdownParser.class);
}
