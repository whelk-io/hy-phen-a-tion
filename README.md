# hy-phen-a-tion
Java OSS library for calculating syllables and hyphenation using Frank Liang's doctoral thesis.

[![CodeFactor](https://www.codefactor.io/repository/github/whelk-io/hy-phen-a-tion/badge)](https://www.codefactor.io/repository/github/whelk-io/hy-phen-a-tion) ![](https://github.com/whelk-io/hy-phen-a-tion/workflows/deploy/badge.svg) [![Dependabot Status](https://api.dependabot.com/badges/status?host=github&repo=whelk-io/hy-phen-a-tion)](https://dependabot.com)

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
