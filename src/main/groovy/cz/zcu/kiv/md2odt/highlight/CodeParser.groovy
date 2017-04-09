package cz.zcu.kiv.md2odt.highlight

import cz.zcu.kiv.md2odt.highlight.content.CodeLang
import cz.zcu.kiv.md2odt.highlight.content.CodeSection
import cz.zcu.kiv.md2odt.highlight.content.CodeSectionImpl
import cz.zcu.kiv.md2odt.highlight.content.CodeSectionType
import org.apache.log4j.Logger
import org.python.core.PyList
import org.python.core.PyTuple
import org.python.core.PyTupleDerived
import org.python.util.PythonInterpreter

/**
 * Created by pepe on 6. 4. 2017.
 */
class CodeParser implements Parser {
    private static final Logger LOGGER = Logger.getLogger(CodeParser)

    @Override
    /** Returns a list of CodeSection, where is specified how to style a text segment.
     *
     * @param code  Code to parse.
     * @param lang  Language used.
     * @return list of CodeSection
     */
    List<CodeSection> parse(String code, String lang) {
        List<CodeSection> list = new ArrayList<>()

        CodeLang codeLang = switchLang(lang)

        String lexer = codeLang.getLexer()

        PythonInterpreter interpreter = new PythonInterpreter()

        // Set a variable with the content you want to work with
        interpreter.set("code", code)

        interpreter.exec("""
        from pygments import highlight
        from pygments.lexers import ${lexer}
        from pygments.formatter import Formatter
        ret = None
        class OwnFormatter(Formatter):
                
            def format(self, tokensource, outfile):
                enc = self.encoding
                global ret
                ret = list(tokensource)
        
        highlight(code, ${lexer}(), OwnFormatter())""".stripIndent())

        PyList result = interpreter.get("ret", PyList.class)
        for(int i = 0; i < result.size(); i++) {
            PyTuple tuple = (PyTuple)result.get(i)
            PyTupleDerived tuple2 = (PyTupleDerived)tuple.get(0)

            String value = tuple.get(1)
            String token = tuple2.get(0)
            CodeSectionType type = switchType(token)

            list.add(new CodeSectionImpl(value, type))
        }

        return list
    }

    static CodeLang switchLang(String lang) {
        CodeLang codeLang = null
        String lowerLang = lang.toLowerCase()

        for(int i = 0; i < CodeLang.values().size(); i++) {
            CodeLang pickedLang = CodeLang.values()[i]
            String codeLangName = pickedLang.getLangName()
            Integer distance = levenshteinDistance(lowerLang, codeLangName)

            if(distance == 0) {
                codeLang = pickedLang
                break
            } else if(distance < 3) {
                codeLang = pickedLang
            }
        }

        if(codeLang == null) {
            codeLang = CodeLang.NONE
            LOGGER.info("unknown code lang " + lang)
        }

        return codeLang
    }

    static CodeSectionType switchType(String type) {
        CodeSectionType codeSectionType = null

        for(int i = 0; i < CodeSectionType.values().size(); i++) {
            CodeSectionType picked = CodeSectionType.values()[i]

            if(picked.getName() == type) {
                codeSectionType = picked
            }
        }

        if(codeSectionType == null) {
            codeSectionType = CodeSectionType.NONE
            LOGGER.info("unknown CodeSectionType " + type)
        }

        return codeSectionType
    }

    private static int levenshteinDistance (CharSequence lhs, CharSequence rhs) {
        int len0 = lhs.length() + 1
        int len1 = rhs.length() + 1

        // the array of distances
        int[] cost = new int[len0]
        int[] newcost = new int[len0]

        // initial cost of skipping prefix in String s0
        for (int i = 0; i < len0; i++) cost[i] = i

        // dynamically computing the array of distances

        // transformation cost for each letter in s1
        for (int j = 1; j < len1; j++) {
            // initial cost of skipping prefix in String s1
            newcost[0] = j

            // transformation cost for each letter in s0
            for(int i = 1; i < len0; i++) {
                // matching current letters in both strings
                int match = (lhs.charAt(i - 1) == rhs.charAt(j - 1)) ? 0 : 1

                // computing cost for each transformation
                int cost_replace = cost[i - 1] + match
                int cost_insert  = cost[i] + 1
                int cost_delete  = newcost[i - 1] + 1

                // keep minimum cost
                newcost[i] = Math.min(Math.min(cost_insert, cost_delete), cost_replace)
            }

            // swap cost/newcost arrays
            int[] swap = cost; cost = newcost; newcost = swap
        }

        // the distance is the cost for transforming all letters in both strings
        return cost[len0 - 1]
    }
}
