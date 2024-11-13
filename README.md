# jResult
Inspired by :crab: [Rust](https://github.com/rust-lang/rust)

[![Build Status](https://github.com/dcadea/jresult/actions/workflows/maven.yml/badge.svg?branch=main)](https://github.com/dcadea/jresult/actions/workflows/maven.yml)

## Result pattern
This library implements a design pattern called `Result`. 
It provides a convenient representation of an operation outcome.
By wrapping return types into `Result` developers can gracefully react to both success - `Ok` and failure - `Err`.
Errors as values force programmers to deal with undesirable behavior at the earliest stage which results in a more reliable, resilient and robust system.

## Quick start

### Dependency

Maven:
```xml
<dependency>
  <groupId>io.github.dcadea</groupId>
  <artifactId>jresult</artifactId>
  <version>${version}</version>
</dependency>
```

Gradle:
```groovy
dependencies {
    implementation 'io.github.dcadea:jresult:${version}'
}
```
