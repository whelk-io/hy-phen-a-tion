/*
 * Copyright 2021 Whelk Contributors (https://whelk.io)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.whelk.hy.phen;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @author Zack Teater
 * @since 0.1.9
 */
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
