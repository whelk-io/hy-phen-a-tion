# hy-phen-a-tion
Java OSS library for calculating syllables and hyphenation based on Frank Liang's doctoral thesis.

[![CodeFactor](https://www.codefactor.io/repository/github/whelk-io/hy-phen-a-tion/badge)](https://www.codefactor.io/repository/github/whelk-io/hy-phen-a-tion) ![release](https://github.com/whelk-io/hy-phen-a-tion/workflows/release/badge.svg)

## Usage

````java
public static void main(String...args) { 
  
  var result = Hyphenator.hyphen("computer");
  
  System.out.println(result); 
  // HyphenResult(
  //    originalWord=computer, 
  //    trieWord=co4m5pu2t3e4r, 
  //    hyphenWord=com-put-er, 
  //    syllables=[com, put, er])
  
}
````

## System Requirements

* Java 11


## Maven Integration

[Maven Central](https://search.maven.org/artifact/io.whelk.hy.phen/whelk-hy-phen-a-tion)

````xml
<dependency>
  <groupId>io.whelk.hy.phen</groupId>
  <artifactId>whelk-hy-phen-a-tion</artifactId>
  <version>${hy-phen-a-tion-version}</version>
</dependency>
````
