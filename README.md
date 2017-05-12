# MD2odt

[![Build Status](https://travis-ci.org/abcBHM/MD2odt.svg?branch=master)](https://travis-ci.org/abcBHM/MD2odt)
[![codecov](https://codecov.io/gh/abcBHM/MD2odt/branch/master/graph/badge.svg)](https://codecov.io/gh/abcBHM/MD2odt)
[![](https://jitpack.io/v/abcBHM/MD2odt.svg)](https://jitpack.io/#abcBHM/MD2odt)

**WORK IN PROGRESS**


MD2odt is a easy to use library for converting Markdown to OpenDocument (*.odt*).

* Web application: [MD2odt-web](https://github.com/abcBHM/MD2odt-web)
    * Online: https://md2odt.herokuapp.com/

* Command line interface: [MD2odt-cli](https://github.com/abcBHM/MD2odt-cli)

## Features
* Supports templates

* Supports various Markdown extensions
    * Autolinks
    * Emoji
    * Strikethrough
    * Subscript
    * Superscript
    * Tables
    * Table of contents

* Syntax highlighting

## Example

### Maven

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

Dependency:
```xml
<dependency>
    <groupId>com.github.abcBHM</groupId>
    <artifactId>MD2odt</artifactId>
    <version>master-SNAPSHOT</version>
</dependency>
```

### Source code

```java
import cz.zcu.kiv.md2odt.MD2odt;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

public class Example {

    public static void main(String... args) throws IOException {
        MD2odt.converter()
                .setInput(Paths.get("example.md"), StandardCharsets.UTF_8)
                .setTemplate(Paths.get("template.odt"))
                .setOutput(Paths.get("result.odt"))
                .enableAllExtensions()
                .convert();
    }
}
```

## License
This project is licensed under the MIT License - see the [LICENSE.txt](LICENSE.txt)
file for details.
