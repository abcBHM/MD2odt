# Markdown

## Basics

This is a paragraph.
Soft linebreak.

This is an another paragraph.  
Hard linebreak. (Because of two spaces.)

*single asterisks* or _single underscores_

**double asterisks** or __double underscores__

regular **bold *bold italic***

Inline `code`

Strikethrough: ~~something wrong~~ (extension)

Subscript: H~2~O (extension)

Superscript: x^2^, October 31^st^ (extension)

Escaping: \\ \` \* \_ \{\} \[\] \(\) \# \+ \- \. \!

Diacritics: Příliš žluťoučký kůň úpěl ďábelské ódy.

## Headers

### H3
#### H4
##### H5
###### H6
####### H7

### H3 - *for*`matt`*ed*

## Links

Email link: <address@example.com>

Autolink: <http://google.com/>

Autolink: http://google.com/ (extension, disabled by default)

Normal link 1: [Google](http://google.com/ "Google")

Normal link 2: [Yahoo](http://search.yahoo.com/)

Normal link 3: [http://search.msn.com/]()

Link with formatting 1: [**Google** `something`](http://google.com/)

Link with formatting 2: *[Google](http://google.com/)*

Image inside link: [Image inside ![smile](https://cloud.githubusercontent.com/assets/6131815/25194925/4afb48cc-253c-11e7-8ac9-cfb8d501eb61.png) link](http://google.com.au/)

### References

Ref. link 1: [Google][1]

Ref. link 2: [1]

Ref. link 3: [Yahoo][yahoo]

Ref. link broken: [Text][xxx]

[1]:      http://google.com/       "Google"
[yahoo]:  http://search.yahoo.com/ "Yahoo Search"


## Lists

1. First ordered list item
2. Another item
    * Unordered sub-list. 
1. Actual numbers don't matter, just that it's a number
    1. Ordered sub-list
4. And another item.

    This is a paragraph.
    Soft linebreak.

    Regular, `code`, *italic* **bold *bold italic***.

* Unordered list can use asterisks
- Or minuses
+ Or pluses


## Tables (extension)

| Command | Description |
| --- | --- |
| `git status` | List all *new or modified* files |
| `git diff` | Show file differences that **haven't been** staged |

| Left-aligned | Center-aligned | Right-aligned |
| :---         |     :---:      |          ---: |
| git status   | git status     | git status    |
| git diff     | git diff       | git diff      |


## Code Blocks

Indented code

    // Some comments
    line 1 of code
    line 2 of code
    line 3 of code


Block code "fences"

```
Sample text here...
```

Syntax highlighting

``` js
var foo = function (bar) {
  return bar++;
};

console.log(foo(5));
```

After each other...

```
Block #1
```
```
Block #2
```

## Quote Blocks

> Lorem ipsum dolor sit amet, consectetur adipiscing elit

Some regular text.

> **Lorem ipsum dolor sit amet**, consectetur adipiscing elit,
> sed do `eiusmod tempor` incididunt ut labore et dolore magna aliqua.
> 
> Ut enim ad minim veniam, quis nostrud *exercitation ullamco*.


## Images

![png](https://cloud.githubusercontent.com/assets/6131815/25194659/73406066-253b-11e7-83ba-5fb3c527c2d0.png)
![jpg](https://cloud.githubusercontent.com/assets/6131815/25194657/733d4eda-253b-11e7-8ec0-8f6aa46cfe16.jpg)

Reference-style:

![alt text][gif]

[gif]: https://cloud.githubusercontent.com/assets/6131815/25194658/733fb95e-253b-11e7-98e0-31aa33704799.gif

Broken reference:
![alt text][xxx]

### Emoji (extension)
Some animals: :octopus: :pig2: :crocodile:

Smiles: :confused: :cry: :weary:

Wrong: :rtrtrtrtrt:


## HTML

### Entities
A paragraph with some HTML entities: &copy; &para; &euro; &ncaron;.

Inside inline code: `&copy; &para; &euro; &ncaron;`.

Formatted: **&copy; &para; &euro; &ncaron;**

### HTML comments
<!-- comment (1 of 3) -->
<!--
 comment (2 of 3)
 -->
regular text (1 of 2) <!-- comment (3 of 3) --> regular text (2 of 2)

### HTML snippets (not supported)
<div>
  <h4>London</h4>
  <p><strong>London</strong> is the capital city of England.</p>
</div>

Some **paragraph** with <b>HTML</b>.


## Horizontal Rules

---

***
___
