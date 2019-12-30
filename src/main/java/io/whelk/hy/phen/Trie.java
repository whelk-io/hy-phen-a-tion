package io.whelk.hy.phen;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Data
@RequiredArgsConstructor
@Accessors(fluent = true)
public class Trie {

  private final String value;
  private final String matcher;
  private final char[] arr;
  private final boolean isLeadingMatch;
  private final boolean isTrailingMatch;

}
