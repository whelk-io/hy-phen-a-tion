package io.whelk.hy.phen;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;
import java.util.List;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class HyphenatorTest {

  static Collection<Object[]> data() {
    return List.of( //
        new Object[][] { //
            { "computer", "com-put-er", "co4m5pu2t3er", 3 }, //
            { "supercalifragilisticexpialidocious", //
                "su-per-cal-ifrag-ilis-tic-ex-pi-ali-do-cious", //
                "su1per1cal1ifrag1il4is1t2ic1ex3p2i3al2i1do1c2io2u2s", 11 } //
        });
  }

  static Collection<Object[]> edge() {
    return List.of( //
        new Object[][] { //
            { "associate", "as-so-ciate", 3 }, //
            { "associates", "as-so-ciates", 3 }, //
            { "declination", "dec-li-na-tion", 4 }, //
            { "obligatory", "oblig-a-tory", 3 }, //
            { "philanthropic", "phil-an-thropic", 3 }, //
            { "present", "present", 1 }, //
            { "presents", "presents", 1 }, //
            { "project", "project", 1 }, //
            { "projects", "projects", 1 }, //
            { "reciprocity", "reci-procity", 2 }, //
            { "recognizance", "re-cog-ni-zance", 4 }, //
            { "reformation", "ref-or-ma-tion", 4 }, //
            { "retribution", "ret-ri-bu-tion", 4 }, //
            { "table", "ta-ble", 2 } //
        });
  }

  @ParameterizedTest
  @MethodSource("data")
  void when_hyphen(String wordToTest, String expectedHyphenation, String expectedTriWord, int expectedSyllableCount) {
    var result = Hyphenator.hyphen(wordToTest);

    assertNotNull(result);
    assertTrue(wordToTest.equals(result.originalWord()));
    assertTrue(expectedHyphenation.equals(result.hyphenWord()));
    assertTrue(expectedTriWord.equals(result.trieWord()));
    assertNotNull(result.syllables());
    assertEquals(expectedSyllableCount, result.syllables().size());
  }

  @ParameterizedTest
  @MethodSource("edge")
  void when_hyphen_with_edge_case(String wordToTest, String expectedHyphenation, int expectedSyllableCount) {
    var result = Hyphenator.hyphen(wordToTest);

    assertNotNull(result);
    assertTrue(wordToTest.equals(result.originalWord()));
    assertTrue(expectedHyphenation.equals(result.hyphenWord()));
    assertNull(result.trieWord());
    assertNotNull(result.syllables());
    assertEquals(expectedSyllableCount, result.syllables().size());
  }

}
