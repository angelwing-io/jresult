# jResult
Inspired by :crab: [Rust](https://github.com/rust-lang/rust)

[![Build Status](https://github.com/dcadea/jresult/actions/workflows/build.yml/badge.svg?branch=main)](https://github.com/dcadea/jresult/actions/workflows/build.yml)

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

### Usage
```java
Result<String, Exception> readFile(String path) {
    try {
        return Result.ok(Files.readString(Paths.get(path)));
    } catch (IOException e) {
        return Result.err(e);
    }
}

<T> Result<T, Exception> parseJson(String json, Class<T> clazz) {
    try {
        return Result.ok(objectMapper.readValue(json, clazz));
    } catch (IOException e) {
        return Result.err(e);
    }
}

void main() {
    var result = readFile("some/path/awesome.json")
            .andThen(content -> parseJson(content, Model.class));

    switch (result) {
        case Ok(Model model) -> /* handle ok value */;
        case Err(Exception e) -> /* react to error */;
    }
}
```

#### Creating a Result
To create a `Result`, you can use the static methods `ok` and `err`:
```java
import io.github.dcadea.jresult.Result;

var ok = Result.ok("success");
var err = Result.err("failure");
```

#### Checking the Result
```java
var result = Result.ok("success");

if (result.isOk()) { /* is an Ok variant holding a value */ }
if (result.isErr()) { /* is an Err variant holding an error value */ }
if (result.isEmpty()) { /* is an Ok variant with null as value */ }
```
With Java 21+ pattern matching you automatically benefit from type safe cast:
```java
if (result instanceof Ok(String value)) { /* use deconstructed value here */ }
if (result instanceof Err(Kaboom boom)) { /* use deconstructed error value here */ }

// or walk through all variants using switch
switch (result) {
    case Ok(String v) -> System.out.println("ok with value: %s".formatted(v));
    case Err(Kaboom boom) -> System.err.println(boom);
}
```

### License
This project is licensed under [Apache](LICENSE-APACHE) and [MIT](LICENSE-APACHE) Licenses - see the corresponding file for details.
