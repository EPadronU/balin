# Balin

[![license](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](http://www.apache.org/licenses/LICENSE-2.0)

Balin is a browser automation library for Kotlin. It's basically a
Selenium-WebDriver wrapper library inspired by Geb.


## Rationale

Geb is a wonderful library that allows its users exploit the power of the
Selenium-WebDriver API with incredible ease. The author, as a software
developer working a lot with browser automation, has been lucky enough to work
with Geb and he is now learning Kotlin. This is the author's chance to learn
the new JVM-based language and to bring to its ecosystem a tool that is so
heavily used by him.


## Note

This project has been conceived for research purposes but the author doesn't
dismiss the possibility of making it a production-ready library if it gets to
that point.


## Build system & framework integrations

### Maven

```xml
<dependencies>
  <dependency>
    <groupId>com.github.epadronu</groupId>
    <artifactId>balin</artifactId>
    <version>0.2.1</version>
    <type>pom</type>
  </dependency>
</dependencies>
```

### Gradle

```groovy
dependencies {
  compile 'com.github.epadronu:balin:0.2.1'
}

repositories {
  jcenter()
}
```

## License

Like Kotlin, _Balin_ is released under version 2.0 of the [Apache License](LICENSE.md).
