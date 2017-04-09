package cz.zcu.kiv.md2odt.highlight.content

/**
 * Created by pepe on 6. 4. 2017.
 */
enum CodeLang {
    NONE("withoutname", "TextLexer"),   //could use default if it is possible
    JAVA("java", "JavaLexer"),
    GROOVY("groovy", "GroovyLexer"),
    C("c", "CLexer"),
    C_PLUS_PLUS("cpp", "CppLexer"),
    PHP("php", "PhpLexer"),
    C_SHARP("csharp", "CSharpLexer")


    private String name
    private String lexer

    CodeLang(String name, String lexer) {
        this.name = name
        this.lexer = lexer
    }

    String getLangName() {
        return name
    }

    String getLexer() {
        return lexer
    }
}