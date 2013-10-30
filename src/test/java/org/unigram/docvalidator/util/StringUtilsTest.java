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
package org.unigram.docvalidator.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class StringUtilsTest {

  @Test
  public void testEndPosition() {
    String str = new String ("this is a pen.");
    assertEquals(13, StringUtils.getSentenceEndPosition(str, "."));
  }

  @Test
  public void testEndPositionWithSpace() {
    String str = new String ("this is a pen. ");
    assertEquals(13, StringUtils.getSentenceEndPosition(str, "."));
  }

  @Test
  public void testEndPositionInMultipleSentence() {
    String str = new String ("this is a pen. that is not pen.");
    assertEquals(13, StringUtils.getSentenceEndPosition(str, "."));
  }

  @Test
  public void testEndPositionInJapanese() {
    String str = new String ("私はペンではない。私は人間です。");
    assertEquals(8, StringUtils.getSentenceEndPosition(str, "。"));
  }

  @Test
  public void testEndPositionInJapaneseWithSpace() {
    String str = new String ("私はペンではない。 私は人間です。");
    assertEquals(8, StringUtils.getSentenceEndPosition(str, "。"));
  }

}