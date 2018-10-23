# Balin

[![license](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](http://www.apache.org/licenses/LICENSE-2.0)

Balin is a browser automation library for Kotlin. It's basically a
Selenium-WebDriver wrapper inspired by Geb.


## Rationale

Geb is a wonderful library that allows its users exploit the power of the
Selenium-WebDriver API with incredible ease. As a software developer who spends
a lot of time working with browser automation, I've been lucky enough to work
with Geb and am now learning Kotlin. I'm taking this chance to learn this new
JVM-based language and to contribute to its ecosystem a tool that's very useful
to me.

## Usage

If you're curious about how to use this library, you can check out the
[following examples](src/test/kotlin/com/github/epadronu/balin/examples)


## Documentation

JavaDoc and KDoc versions of Balin's API documentation can be found in the
following locations:

- [Java API](https://epadronu.github.io/balin/java/api/index.html)
- [Kotlin API](https://epadronu.github.io/balin/kotlin/api/balin/index.html)


## Build system & framework integrations

### Maven

```xml
<dependencies>
  <dependency>
    <groupId>com.github.epadronu</groupId>
    <artifactId>balin</artifactId>
    <version>0.3.2</version>
    <type>pom</type>
  </dependency>
</dependencies>
```

### Gradle

```groovy
dependencies {
  compile 'com.github.epadronu:balin:0.3.2'
}

repositories {
  jcenter()
}
```


## Note

This project has been conceived for research purposes but I don't dismiss the
possibility of making it a production-ready library if it gets to that point.

## License

Like Kotlin, _Balin_ is released under version 2.0 of the [Apache License](LICENSE.md).
