package io.whelk.hy.phen;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import java.util.Arrays;
import java.util.Collection;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import lombok.AllArgsConstructor;

@RunWith(Parameterized.class)
@AllArgsConstructor
public class HyphenatorTest {

  private String wordToTest;
  private String expectedHyphenation;
  private String expectedTrieWord;
  private int expectedSyllableCount;
  
  @Parameters
  public static Collection<Object[]> data() {
    return Arrays.asList(new Object[][] {
        { "computer", "com-put-er", "co4m5pu2t3er", 3 },
        { "associate", "as-so-ciate", null, 3 },
        { "associates", "as-so-ciates", null, 3 },
        { "declination", "dec-li-na-tion", null, 4 },
        { "obligatory", "oblig-a-tory", null, 3 },
        { "philanthropic", "phil-an-thropic", null, 3 },
        { "present", "present", null, 1 },
        { "presents", "presents", null, 1 },
        { "project", "project", null, 1 },
        { "projects", "projects", null, 1 },
        { "reciprocity", "reci-procity", null, 2 },
        { "recognizance", "re-cog-ni-zance", null, 4 },
        { "reformation", "ref-or-ma-tion", null, 4 },
        { "retribution", "ret-ri-bu-tion", null, 4 },
        { "table", "ta-ble", null, 2 },
        { "supercalifragilisticexpialidocious", 
          "su-per-cal-ifrag-ilis-tic-ex-pi-ali-do-cious", 
          "su1per1cal1ifrag1il4is1t2ic1ex3p2i3al2i1do1c2io2u2s", 11 }
    });
  }

  @Test
  public void testHyphen() {
    var result = Hyphenator.hyphen(wordToTest);
    assertThat(result.originalWord(), is(wordToTest));
    assertThat(result.hyphenWord(), is(expectedHyphenation));
    assertThat(result.trieWord(), is(expectedTrieWord));
    assertThat(result.syllables().size(), is(expectedSyllableCount));
  }

}
