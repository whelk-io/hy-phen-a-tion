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
  
  @Parameters
  public static Collection<Object[]> data() {
    return Arrays.asList(new Object[][] {
        { "computer", "com-put-er" },
        { "associate", "as-so-ciate" },
        { "associates", "as-so-ciates" },
        { "declination", "dec-li-na-tion" },
        { "obligatory", "oblig-a-tory" },
        { "philanthropic", "phil-an-thropic" },
        { "present", "present" },
        { "presents", "presents" },
        { "project", "project" },
        { "projects", "projects" },
        { "reciprocity", "reci-procity" },
        { "recognizance", "re-cog-ni-zance" },
        { "reformation", "ref-or-ma-tion" },
        { "retribution", "ret-ri-bu-tion" },
        { "table", "ta-ble" },
        { "supercalifragilisticexpialidocious", "su-per-cal-ifrag-ilis-tic-ex-pi-ali-do-cious" }
    });
  }

  @Test
  public void test() {
    var result = Hyphenator.hyphen(wordToTest);
    assertThat(result.originalWord(), is(wordToTest));
    assertThat(result.hyphenWord(), is(expectedHyphenation));
  }

}
