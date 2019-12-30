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
import lombok.var;

@RunWith(Parameterized.class)
@AllArgsConstructor
public class HyphenatorTest {

  private String wordToTest;
  private String expectedHyphenation;
  private String expectedTrieWord;
  
  @Parameters
  public static Collection<Object[]> data() {
    return Arrays.asList(new Object[][] {
        { "computer", "com-put-er", "co4m5pu2t3er" },
        { "associate", "as-so-ciate", null },
        { "associates", "as-so-ciates", null },
        { "declination", "dec-li-na-tion", null },
        { "obligatory", "oblig-a-tory", null },
        { "philanthropic", "phil-an-thropic", null },
        { "present", "present", null },
        { "presents", "presents", null },
        { "project", "project", null },
        { "projects", "projects", null },
        { "reciprocity", "reci-procity", null },
        { "recognizance", "re-cog-ni-zance", null },
        { "reformation", "ref-or-ma-tion", null },
        { "retribution", "ret-ri-bu-tion", null },
        { "table", "ta-ble", null },
        { "supercalifragilisticexpialidocious", 
          "su-per-cal-ifrag-ilis-tic-ex-pi-ali-do-cious", 
          "su1per1cal1ifrag1il4is1t2ic1ex3p2i3al2i1do1c2io2u2s" }
    });
  }

  @Test
  public void test() {
    var result = Hyphenator.hyphen(wordToTest);
    assertThat(result.originalWord(), is(wordToTest));
    assertThat(result.hyphenWord(), is(expectedHyphenation));
    assertThat(result.trieWord(), is(expectedTrieWord));
  }

}
