# Markdown

## Formatting

*single asterisks* or _single underscores_

**double asterisks** or __double underscores__

Inline `code`

Escaping: \*literal asterisks\*

Diacritics: Příliš žluťoučký kůň úpěl ďábelské ódy.


## Links

Email link: <address@example.com>

Autolink: <http://google.com/>

http://google.com/ (disabled by default)

Normal link 1: [Google](http://google.com/ "Google")

Normal link 2: [Yahoo](http://search.yahoo.com/)

Normal link 3: [http://search.msn.com/]()


### References

Ref. link 1: [Google][1]

Ref. link 2: [1]

Ref. link 3: [Yahoo][yahoo]

[1]:      http://google.com/       "Google"
[yahoo]:  http://search.yahoo.com/ "Yahoo Search"


## Lists

1. First ordered list item
2. Another item
   * Unordered sub-list. 
1. Actual numbers don't matter, just that it's a number
   1. Ordered sub-list
4. And another item.

   You can have properly indented paragraphs within list items.

   To have a line break without a paragraph, you will need to use two trailing spaces.
   Note that this line is separate, but within the same paragraph.

* Unordered list can use asterisks
- Or minuses
+ Or pluses


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


## Quote Blocks

> Lorem ipsum dolor sit amet, consectetur adipiscing elit

Some regular text.

> **Lorem ipsum dolor sit amet**, consectetur adipiscing elit,
> sed do `eiusmod tempor` incididunt ut labore et dolore magna aliqua.
> 
> Ut enim ad minim veniam, quis nostrud *exercitation ullamco*.


## Images

![Dog](https://www.seznam.cz/media/img/dogs/krasty_04.png)
![Dog](https://www.seznam.cz/media/img/dogs/krasty_06.png)

Reference-style:

![alt text][dog]

[dog]: https://www.seznam.cz/media/img/dogs/krasty_07.png


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

### HTML snippets (TODO)
<div>
  <h4>London</h4>
  <p><strong>London</strong> is the capital city of England.</p>
</div>


## Horizontal Rules

---

***
___
