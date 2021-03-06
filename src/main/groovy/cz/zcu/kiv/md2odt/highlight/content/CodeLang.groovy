package cz.zcu.kiv.md2odt.highlight.content

/**
 * Enumerator of supported langs
 *
 * @version 2017-04-06
 * @author Vít Mazín
 */
enum CodeLang {
    NONE("TextLexer", "withoutname"),   //could use default if it is possible
    ADA("AdaLexer", "ada", "ada95", "ada2005"),
    ASYMPTOTE("AsymptoteLexer", "asymptote", "asy"),
    ANTTLR_PERL("AntlrPerlLexer", "antlrperl", "antlr-perl"),
    ANTLR_RUBY("AntlrRubyLexer", "antlr-ruby", "antlr-rb", "antlrruby", "antlrrb"),
    AWK("AwkLexer", "awk", "gawk", "mawk", "nawk"),
    BASH("BashLexer", "bash", "sh", "zsh", "shell"),
    BASH_SESSION("BashSessionLexer", "shell-session", "console", "shellsession"),
    BATCH("BatchLexer", "batch", "bat", "dosbatch", "winbatch"),
    BEFUNGE("BefungeLexer", "befunge"),
    BOO("BooLexer", "boo"),
    BRAINFUCK("BrainfuckLexer", "brainfuck", "bf"),
    C("CLexer", "c"),
    C_PLUS_PLUS("CppLexer", "cpp", "c++"),
    C_SHARP("CSharpLexer", "csharp", "c#"),
    CLOJURE("ClojureLexer", "clojure", "clj"),
    COFFEESCRIPT ("CoffeeScriptLexer", "coffee-script", "coffeescript", "coffee"),
    COLDFUSION("ColdfusionLexer", "coldfusion", "cfs", "cfc", "cfm"),
    LISP("CommonLispLexer", "common-lisp", "cl", "lisp"),
    COQ("CoqLexer", "coq"),
    CRYPTOL("LiterateCryptolLexer", "literate-cryptol", "lcry", "lcryptol", "cryptol"),
    CRYSTAL("CrystalLexer", "crystal", "cr"),
    CYTHON("CythonLexer", "cython", "pyx", "pyrex"),
    D("DLexer", "d"),
    DG("DgLexer", "dg"),
    DART("DartLexer", "dart"),
    DELPHI("DelphiLexer", "delphi", "pas", "pascal", "objectpascal"),
    DYLAN("DylanLexer", "dylan"),
    DYLAN_CONSOLE("DylanConsoleLexer", "dylan-console", "dylanconsole", "dylan-repl"),
    DYLAN_LID("DylanLidLexer", "dylan-lid", "dylanlid", "lid"),
    ELM("ElmLexer", "elm"),
    ELIXIR_CONSOLE("ElixirConsoleLexer", "elixirconsole", "iex"),
    ELIXIR("ElixirLexer", "elixir", "ex", "exs"),
    ERB("ErbLexer", "erb"),
    ERLANG("ErlangLexer", "erlang"),
    ERLANG_SHELL("ErlangShellLexer", "erl", "erl-sh"),
    EZHIL("EzhilLexer", "ezhil"),
    FACTOR("FactorLexer", "factor"),
    FANCY("FancyLexer", "fancy", "fy"),
    FISH_SHELL("FishShellLexer", "fish", "fishshell"),
    FORTRAN("FortranLexer", "fortran"),
    FORTRAN_FIXED("FortranFixedLexer", "fortranfixed"),
    F_SHARP("FSharpLexer", "fsharp", "f#"),
    GAP("GAPLexer", "gap"),
    GHERKIN("GherkinLexer", "cucumber", "gherkin"),
    GO("GoLexer", "go"),
    GLSL("GLShaderLexer", "glsl", "glshader", "gl-shader"),
    GROOVY("GroovyLexer", "groovy"),
    HASKELL("HaskellLexer", "haskell", "hs"),
    IDL("IDLLexer", "idl"),
    IO("IoLexer", "io"),
    JAVA("JavaLexer", "java"),
    JAVASCRIPT("JavascriptLexer", "javascript", "js"),
    LASSO("LassoLexer", "lasso", "lassoscript"),
    LLVM("LlvmLexer", "llvm"),
    LOGTALK("LogtalkLexer", "logtalk"),
    LUA("LuaLexer", "lua"),
    MATLAB("MatlabLexer", "matlab"),
    MATLAB_SESSION("MatlabSessionLexer", "matlabsession"),
    MINID("MiniDLexer", "minid"),
    MODELICA("ModelicaLexer", "modelica"),
    MODULA2("Modula2Lexer", "modula2", "m2"),
    MSDOS("MSDOSSessionLexer", "doscon"),
    MUPAD("MuPADLexer", "mupad"),
    NEMERLE("NemerleLexer", "nemerle"),
    NEWSPEAK("NewspeakLexer", "newspeak"),
    NIM("NimrodLexer", "nim", "nimrod"),
    NUMPY("NumPyLexer", "numpy"),
    OBJECTIVEC("ObjectiveCLexer", "objective-c", "objectivec", "obj-c", "objc"),
    OBJECTIVECPP("ObjectiveCppLexer", "objective-c++", "objectivec++", "obj-c++", "objc++"),
    OBJECTIVEJ("ObjectiveJLexer", "objective-j", "objectivej", "obj-j", "objj"),
    OCTAVE("OctaveLexer", "octave"),
    OCAML("OcamlLexer", "ocaml"),
    PERL6("Perl6Lexer", "perl6", "pl6"),
    PERL("PerlLexer", "perl", "pl"),
    PHP("PhpLexer", "php"),
    POWRAY("PovrayLexer", "povray", "pov"),
    POSTSCRIPT("PostScriptLexer", "postscript", "postscr"),
    POWERSHELL("PowerShellLexer", "powershell", "posh", "ps1", "psm1"),
    PROLOG("PrologLexer", "prolog"),
    PY3TB("Python3TracebackLexer", "py3tb"),
    PYTHON("PythonLexer", "python", "py", "python2", "py2", "sage"),
    PYTHON3("Python3Lexer", "python3", "py3"),
    PYTHONCONSOLE("PythonConsoleLexer", "pycon", "pythonconsole"),
    PYTHONTRACEBACK("PythonTracebackLexer", "pytb", "pythontraceback"),
    RAGELRUBY("RagelRubyLexer", "ragel-ruby", "ragel-rb", "ragelruby", "ragelrb"),
    REBOL("RebolLexer", "rebol"),
    RED("RedLexer", "red", "red/system"),
    REDCODE("RedcodeLexer", "redcode"),
    RUBY("RubyLexer", "ruby", "rb", "duby"),
    RUBYCONSOLE("RubyConsoleLexer", "rbcon", "irb"),
    RUST("RustLexer", "rust", "rs"),
    SAS("SASLexer", "sas"),
    S("SLexer", "splus", "s", "r"),
    SCALA("ScalaLexer", "scala"),
    SCHEME("SchemeLexer", "scheme", "scm"),
    SCILAB("ScilabLexer", "scilab"),
    SMALLTALK("SmalltalkLexer", "smalltalk", "squeak", "st"),
    SNOBOL("SnobolLexer", "snobol"),
    SYSTEM("SystemVerilogLexer", "systemverilog", "sv"),
    TCL("TclLexer", "tcl"),
    VALA("ValaLexer", "vala", "vapi"),
    VERILOG("VerilogLexer", "verilog", "v"),
    VBASPX("VbNetAspxLexer", "aspx-vb", "aspxvb", "vbaspx", "vb-aspx"),
    VBNET("VbNetLexer", "vbnet", "vb.net", "vb", "visualbasic", "visual-basic"),
    FOXPRO("FoxProLexer", "foxpro", "vfp", "clipper", "xbase"),
    XQUERY("XQueryLexer", "xquery", "xqy", "xq", "xql", "xqm"),
    ZEPHIR("ZephirLexer", "zephir"),
    VHDL("VhdlLexer", "vhdl"),
    SWIFT("SwiftLexer", "swift"),


    BBCODE("BBCodeLexer", "bbcode"),
    CMAKE("CMakeLexer", "cmake"),
    CSS("CssLexer", "css"),
    CSSDJANGO("DjangoLexer", "css+django", "css+jinja"),
    DJANGO("DjangoLexer", "django", "jinja"),
    HTML("HtmlLexer", "html"),
    MAKEFILE("MakefileLexer", "make", "makefile", "mf", "bsdmake"),
    MYSQL("MySqlLexer", "mysql"),
    TEA("TeaTemplateLexer", "tea"),
    TWIG("TwigLexer", "twig"),
    TWIGHTML("TwigHtmlLexer", "twightml", "html+twig", "htmltwig"),
    POSTGRES("PostgresLexer", "postgresql", "postgres"),
    SQL("SqlLexer", "sql"),
    SQLITE("SqliteConsoleLexer", "sqlite3"),
    TEX("TexLexer", "tex", "latex"),
    XML("XmlLexer", "xml"),
    YAML("YamlLexer", "yaml")



    private String[] names
    private String lexer

    /**
     * Enum constructor
     *
     * @param lexer name of lexer
     * @param names posible names of lang
     */
    CodeLang(String lexer, String... names) {
        this.names = names
        this.lexer = lexer
    }

    /**
     * Returns array of names
     *
     * @return array of names
     */
    String[] getLangNames() {
        return names
    }

    /**
     * Returns lexer name
     *
     * @return lexer name
     */
    String getLexer() {
        return lexer
    }
}