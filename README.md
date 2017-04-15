# MD2odt

[![Build Status](https://travis-ci.org/abcBHM/MD2odt.svg?branch=master)](https://travis-ci.org/abcBHM/MD2odt)
[![codecov](https://codecov.io/gh/abcBHM/MD2odt/branch/master/graph/badge.svg)](https://codecov.io/gh/abcBHM/MD2odt)
[![](https://jitpack.io/v/abcBHM/MD2odt.svg)](https://jitpack.io/#abcBHM/MD2odt)

**WORK IN PROGRESS**


MD2odt is a library for converting Markdown to OpenDocument (*.odt*).

* Web application: [MD2odt-web](https://github.com/abcBHM/MD2odt-web)
    * Online: https://md2odt.herokuapp.com/

## Example

```java
import cz.zcu.kiv.md2odt.MD2odt;
```

```java
InputStream zip = ...  // zip with Markdown file and images
InputStream template = ...  // .odt or .ott template, optional
OutputStream out = ...

MD2odt.converter()
        .setInputZip(zip)
        .setTemplate(template)
        .setOutput(out)
        .enableAllExtensions()
        .convert();
```

## License
This project is licensed under the MIT License - see the [LICENSE.txt](LICENSE.txt)
file for details.
