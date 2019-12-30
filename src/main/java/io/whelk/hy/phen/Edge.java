package io.whelk.hy.phen;

import java.util.List;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Data
@RequiredArgsConstructor
@Accessors(fluent = true)
public class Edge {

  private final String value;
  private final String hypen;
  private final List<String> syllables;

}
