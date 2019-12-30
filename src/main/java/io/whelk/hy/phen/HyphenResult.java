package io.whelk.hy.phen;

import java.util.Collections;
import java.util.List;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true, chain = true)
public class HyphenResult {

  private String originalWord;
  
  private String trieWord;
  
  private String hyphenWord;
  
  @ToString.Exclude
  private List<Trie> tries = Collections.emptyList();
  
  private List<String> syllables = Collections.emptyList();
  
  @ToString.Exclude
  private boolean isEdgeCase = false;

}
