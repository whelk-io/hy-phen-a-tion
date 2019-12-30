package io.whelk.hy.phen;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@RequiredArgsConstructor
@Accessors(fluent = true)
public class Trie {

  private final String value;
  
  @ToString.Exclude
  private final String matcher;
  
  @ToString.Exclude
  private final char[] arr;
  
  @ToString.Exclude
  private final boolean isLeadingMatch;
  
  @ToString.Exclude
  private final boolean isTrailingMatch;

}
