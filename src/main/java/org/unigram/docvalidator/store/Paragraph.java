/**
 * DocumentValidator
 * Copyright (c) 2013-, Takahiko Ito, All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library.
 */
package org.unigram.docvalidator.store;

import java.util.Iterator;
import java.util.Vector;
/**
 * Represent a paragraph of text.
 */
public final class Paragraph implements Block {
  /**
   * constructor.
   */
  public Paragraph() {
    super();
    sentences = new Vector<Sentence>();
  }

  /**
   * get the iterator of sentences.
   * @return Iterator of Sentence in the paragraph
   */
  public Iterator<Sentence> getSentences() {
    return sentences.iterator();
  }

  /**
   * get the sentence of specified number.
   * @param lineNumber sentence number
   * @return sentence
   */
  public Sentence getLine(int lineNumber) {
    return sentences.get(lineNumber);
  }

  public void appendSentence(String content, int lineNum) {
    sentences.add(new Sentence(content, lineNum));
  }

  public int getNumverOfSentences() {
    return sentences.size();
  }

  public int getBlockID() {
    return BlockTypes.PARAGRAPH;
  }

  public int extractSummary() {
    return 0;
  }

  Vector<Sentence> sentences;
}