package io.whelk.hy.phen;

import java.util.Collections;
import java.util.List;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true, chain = true)
public class HyphenResult {

  private String originalWord;
  private String trieWord;
  private String hyphenWord;
  private List<Trie> tries = Collections.emptyList();
  private List<String> syllables = Collections.emptyList();
  private boolean isEdgeCase = false;

}
