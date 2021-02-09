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

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toMap;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;
import lombok.SneakyThrows;

/**
 * @author Zack Teater
 * @since 0.1.9
 */
public class Hyphenator {

  private static Map<Integer, Map<String, List<Trie>>> tries;
  private static Map<String, Edge> edges;

  private static int maxTrie;
  private static int minTrie;

  static {
    loadHyphens();
    loadEdges();
  }

  private static void loadHyphens() {
    tries = Hyphenator //
        .readFromFile("/hyphen-en-us.txt") //
        .stream() //
        .sorted() //
        .distinct() //
        .map(Hyphenator::mapPattern) //
        .collect(groupingBy(t -> t.matcher().length(), groupingBy(t -> t.matcher())));

    var stats = tries.keySet().stream().mapToInt(i -> i).summaryStatistics();
    maxTrie = stats.getMax();
    minTrie = stats.getMin();
  }

  @SneakyThrows
  private static void loadEdges() {
    edges = Hyphenator //
        .readFromFile("/edge-en-us.txt") //
        .stream() //
        .sorted() //
        .distinct() //
        .map(Hyphenator::mapEdge) //
        .collect(toMap(Edge::value, v -> v));
  }

  @SneakyThrows
  private static List<String> readFromFile(String filename) {
    var lines = new LinkedList<String>();
    try (var inputStream = Hyphenator.class.getResourceAsStream(filename);
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
      String line;
      while ((line = br.readLine()) != null) {
        lines.add(line);
      }
    }
    return lines;
  }

  private static Trie mapPattern(String value) {
    var plainText = value.replaceAll("[\\.0-9]", "");
    var isLeadingMatch = value.startsWith(".");
    var isTrailingMatch = value.endsWith(".");
    var isTrailingWildcard = value.substring(value.length() - 1).matches("[\\.0-9]");
    var trieArr = toTrieArray(value, plainText, isTrailingWildcard);
    return new Trie(value, plainText, trieArr, isLeadingMatch, isTrailingMatch);
  }

  private static Edge mapEdge(String value) {
    return new Edge(value.replaceAll("-", ""), value, Arrays.asList(value.split("-")));
  }

  private static char[] toTrieArray(String value, String plainText, boolean isTrailingWildcard) {
    var trimmed = value.replaceAll("\\.", "");
    var len = plainText.length();
    if (isTrailingWildcard)
      len++;
    var arr = new char[len * 2];
    Arrays.fill(arr, '0');

    int i = 0;
    for (char c : trimmed.toCharArray()) {
      if (c >= '0' && c <= '9') {
        if (i % 2 == 0)
          arr[i] = c;
        else
          arr[++i] = c;
      } else {
        if (i % 2 == 0)
          arr[++i] = c;
        else
          arr[i] = c;
      }
      i++;
    }
    return arr;
  }

  /**
   * Try to calculate where hyphens should be placed (which in turn is in between
   * syllables) using TEX82 hyphenation patterns. This is largely based on Frank
   * Liang's dictoral thesis from 1983 titled 'Word Hy-phen-a-tion by Com-put-er'.
   * 
   * @param word to split into hyphens using syllables
   * @return HyphenResult
   */
  public static HyphenResult hyphen(String word) {
    validate(word);

    // convert word
    word = word.toLowerCase();
    var result = new HyphenResult().originalWord(word);

    // check if edge case
    if (edges.containsKey(word)) {
      var edge = edges.get(word);
      result.isEdgeCase(true);
      result.hyphenWord(edge.hypen());
      result.syllables(edge.syllables());
      return result;
    }

    // start building trie array
    var wordTrieArr = new char[word.length() * 2];
    Arrays.fill(wordTrieArr, '0');
    var wordArr = word.toCharArray();
    for (int i = 0; i < wordArr.length; i++)
      wordTrieArr[(i * 2) + 1] = wordArr[i];

    // match tries to word
    List<Trie> matchedTrie = new LinkedList<>();
    for (int i = minTrie; i <= maxTrie; i++) {
      var trieMap = tries.getOrDefault(i, new HashMap<>());
      var isLeadingMatchOnly = false;
      var isTrailingMatchOnly = false;
      for (int j = 0; j <= word.length() - i; j++) {
        isLeadingMatchOnly = j == 0;
        isTrailingMatchOnly = j == word.length() - i;
        var sub = word.substring(j, j + i);
        if (trieMap.containsKey(sub)) {
          var tries = trieMap.get(sub);
          for (Trie trie : tries) {
            if (trie.isLeadingMatch() && !isLeadingMatchOnly)
              continue;
            if (trie.isTrailingMatch() && !isTrailingMatchOnly)
              continue;
            // update word array if trie value is greater
            updateWordArr(wordTrieArr, j * 2, trie);
            matchedTrie.add(trie);
          }
        }
      }
    }

    // update result
    result.tries(matchedTrie);
    result.trieWord(groupByToken(wordTrieArr));
    var syllables = groupBySyllable(wordTrieArr);
    result.syllables(syllables);
    result.hyphenWord(hyphenWord(syllables));

    return result;
  }

  private static void validate(String word) {
    if (word == null || word.trim().isEmpty())
      throw new IllegalArgumentException("word cannot be null or blank");

    if (!word.matches("^[a-zA-Z]+$"))
      throw new IllegalArgumentException("word must contain alpha characters only");
  }

  /**
   * Join syllables by '-' to re-form word.
   * 
   * @param syllables
   * @return
   */
  private static String hyphenWord(List<String> syllables) {
    return syllables.stream().collect(joining("-"));
  }

  /**
   * Join wordTrieArr into final trie model with scores.
   * 
   * @param wordTrieArr
   * @return
   */
  private static String groupByToken(char[] wordTrieArr) {
    return IntStream //
        .range(1, wordTrieArr.length) //
        .mapToObj(i -> wordTrieArr[i]) //
        .map(c -> c.toString()) //
        .filter(c -> !"0".equals(c)) //
        .collect(joining());
  }

  /**
   * Break wordTrie by odd-value scores
   * 
   * @param wordTrieArr
   * @return
   */
  private static List<String> groupBySyllable(char[] wordTrieArr) {
    List<String> syllables = new LinkedList<>();
    int start = 1;
    for (int i = 2; i < wordTrieArr.length; i += 2) {
      if (wordTrieArr[i] % 2 != 0) {
        syllables.add(joinWord(wordTrieArr, start, i));
        start = i;
      }
    }
    syllables.add(joinWord(wordTrieArr, start, wordTrieArr.length));
    return syllables;
  }

  /**
   * Join char arr into String.
   * 
   * @param wordTrieArr
   * @param start
   * @param end
   * @return
   */
  private static String joinWord(char[] wordTrieArr, int start, int end) {
    return IntStream //
        .range(start, end) //
        .filter(j -> j % 2 != 0) //
        .mapToObj(j -> wordTrieArr[j]) //
        .map(c -> c.toString()) //
        .collect(joining());
  }

  /**
   * Update wordTrie score if trie score is higher.
   * 
   * @param wordTrieArr
   * @param index
   * @param trie
   */
  private static void updateWordArr(char[] wordTrieArr, int index, Trie trie) {
    var trieArr = trie.arr();
    for (int i = 0; i < trieArr.length && i + index < wordTrieArr.length; i += 2)
      if (trieArr[i] > wordTrieArr[i + index])
        wordTrieArr[i + index] = trieArr[i];
  }

}
