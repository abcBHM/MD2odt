package cz.zcu.kiv.md2odt.highlight.content

/**
 * Created by pepe on 6. 4. 2017.
 */
enum CodeLang {
    NONE("TextLexer", "withoutname"),   //could use default if it is possible
    JAVA("JavaLexer", "java"),
    GROOVY("GroovyLexer", "groovy"),
    C("CLexer", "c"),
    C_PLUS_PLUS("CppLexer", "cpp", "c++"),
    PHP("PhpLexer", "php"),
    C_SHARP("CSharpLexer", "csharp", "c#")


    private String[] names
    private String lexer

    CodeLang(String lexer, String... names) {
        this.names = names
        this.lexer = lexer
    }

    String[] getLangNames() {
        return names
    }

    String getLexer() {
        return lexer
    }
}