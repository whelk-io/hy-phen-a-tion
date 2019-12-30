package io.whelk.hy.phen;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import lombok.SneakyThrows;
import lombok.var;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Hyphenator {
  
  private static Map<Integer, Map<String, List<Trie>>> tries;
  private static Map<String, String> edges;
  
  private static int maxTrie;
  private static int minTrie;

  static {
    loadHyphens();
    loadEdges();
  }
  
  @SneakyThrows
  private static void loadHyphens() {
    try (Stream<String> stream = Files.lines(Paths.get(ClassLoader.getSystemResource("hyphen-en-us.tex").toURI()))) {
      tries = stream
              .sorted()
              .distinct()
              .map(Hyphenator::mapPattern)
              .collect(Collectors.groupingBy(t -> t.matcher().length(), Collectors.groupingBy(t -> t.matcher())));
      
      var stats = tries.keySet().stream().mapToInt(i -> i).summaryStatistics();
      maxTrie = stats.getMax();
      minTrie = stats.getMin();
    }
  }
  
  @SneakyThrows
  private static void loadEdges() {
    try (Stream<String> stream = Files.lines(Paths.get(ClassLoader.getSystemResource("edge-en-us.tex").toURI()))) {
      edges = stream
              .sorted()
              .distinct()
              .collect(Collectors.toMap(v -> v.replaceAll("-", ""), v -> v));
    }
  }
  
  private static Trie mapPattern(String value) {
    var plainText = value.replaceAll("[\\.0-9]", "");
    var isLeadingMatch = value.startsWith(".");
    var isTrailingMatch = value.endsWith(".");
    var isTrailingWildcard = value.substring(value.length() - 1).matches("[\\.0-9]");
    var trieArr = toTrieArray(value, plainText, isTrailingWildcard);
    return new Trie(value, plainText, trieArr, isLeadingMatch, isTrailingMatch);
  }
  
  private static char[] toTrieArray(String value, String plainText, boolean isTrailingWildcard) { 
    var trimmed = value.replaceAll("\\.", "");
    var len = plainText.length();
    if (isTrailingWildcard)
      len++;
    char[] arr = new char[len * 2];
    Arrays.fill(arr, '0');
    
    int i = 0;
    for (char c : trimmed.toCharArray()) { 
      if (c >= '0' && c <= '9') {
        if (i % 2 == 0)
          arr[i] = c;
        else
          arr[++i] = c;
      } else {
        if (i %2 == 0)
          arr[++i] = c;
        else 
          arr[i] = c;
      }
      i++;
    }
    return arr;
  }

  /**
   * Try to calculate where hyphens should be placed (which in turn is in between syllables) using
   * TEX82 hyphenation patterns. This is largely based on Frank Liang's dictoral thesis from 1983
   * titled 'Word Hy-phen-a-tion by Com-put-er'.
   * 
   * @param word
   * @return HyphenResult
   */
  public static HyphenResult hyphen(String word) {
    validate(word);
    
    // convert word
    word = word.toLowerCase();
    var result = new HyphenResult().originalWord(word);
    
    // check if edge case
    if (edges.containsKey(word)) {
      result.isEdgeCase(true);
      result.hyphenWord(edges.get(word));
      return result;
    }

    // start building trie array
    char[] wordTrieArr = new char[word.length() * 2];
    Arrays.fill(wordTrieArr, '0');
    char[] wordArr = word.toCharArray();
    for (int i = 0; i < wordArr.length; i++)
      wordTrieArr[(i * 2) + 1] = wordArr[i];

    // match tries to word
    List<Trie> matchedTrie = new LinkedList<>();
    for (int i = minTrie; i <= maxTrie; i++) {
      Map<String, List<Trie>> trieMap = tries.getOrDefault(i, new HashMap<>());
      boolean isLeadingMatchOnly = false;
      boolean isTrailingMatchOnly = false;
      for (int j = 0; j <= word.length() - i; j++) {
        isLeadingMatchOnly = j == 0;
        isTrailingMatchOnly = j == word.length() - i;
        var sub = word.substring(j, j + i);
        if (trieMap.containsKey(sub)) {
          List<Trie> tries = trieMap.get(sub);
          for (Trie trie : tries) { 
            if (isLeadingMatchOnly && !trie.isLeadingMatch())
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
    return syllables
            .stream()
            .collect(Collectors.joining("-"));
  }
  
  /**
   * Join wordTrieArr into final trie model with scores.
   * 
   * @param wordTrieArr
   * @return
   */
  private static String groupByToken(char[] wordTrieArr) { 
    return IntStream
            .range(0, wordTrieArr.length)
            .mapToObj(i -> wordTrieArr[i])
            .map(c -> c.toString())
            .filter(c -> !"0".equals(c))
            .collect(Collectors.joining());
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
    return IntStream
            .range(start, end)
            .filter(j -> j % 2 != 0)
            .mapToObj(j -> wordTrieArr[j])
            .map(c -> c.toString())
            .collect(Collectors.joining());
  }

  /**
   * Update wordTrie score if trie score is higher.
   * 
   * @param wordTrieArr
   * @param index
   * @param trie
   */
  private static void updateWordArr(char[] wordTrieArr, int index, Trie trie) {
    char[] trieArr = trie.arr();
    for (int i = 0; i < trieArr.length && i + index < wordTrieArr.length; i += 2)
      if (trieArr[i] > wordTrieArr[i + index])
        wordTrieArr[i + index] = trieArr[i];
  }

}
