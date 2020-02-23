# hy-phen-a-tion
Java OSS library for calculating syllables and hyphenation based on Frank Liang's doctoral thesis.

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

## Maven Integration

**~/.m2/settings.xml**

````xml
<settings>

  <activeProfiles>
    <activeProfile>github</activeProfile>
  </activeProfiles>

  <profiles>
    <profile>
      <id>github</id>
      <repositories>
        <repository>
          <id>github-hy-phen-a-tion</id>
          <url>https://maven.pkg.github.com/whelk-io/hy-phen-a-tion</url>
        </repository>
      </repositories>
    </profile>
  </profiles>

  <servers>
    <server>
      <id>github-hy-phen-a-tion</id>
      <username>GITHUB_USERNAME</username>
      <password>PERSONAL_ACCESS_TOKEN</password>
    </server>
  </servers>

</settings>
````

**pom.xml**

````xml
<dependencies>
	<dependency>
		<groupId>io.whelk.hy.phen</groupId>
		<artifactId>whelk-hy-phen-a-tion</artifactId>
		<version>${whelk-hy-phen-a-tion-version}</version>
	</dependency>
</dependencies>

<repositories>
	<repository>
		<id>github-hy-phen-a-tion</id>
		<url>https://maven.pkg.github.com/whelk-io/hy-phen-a-tion</url>
	</repository>
</repositories>
````

More information on authenticating with GitHub packages: https://help.github.com/en/github/managing-packages-with-github-packages/configuring-apache-maven-for-use-with-github-packages#authenticating-to-github-packages

