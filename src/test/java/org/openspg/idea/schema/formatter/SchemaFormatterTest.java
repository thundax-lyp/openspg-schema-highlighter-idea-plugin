package org.openspg.idea.schema.formatter;

import com.intellij.psi.codeStyle.CommonCodeStyleSettings;
import org.openspg.idea.common.AbstractFormatterTestCase;
import org.openspg.idea.schema.SchemaFileType;
import org.openspg.idea.schema.SchemaLanguage;

public class SchemaFormatterTest extends AbstractFormatterTestCase {

    public SchemaFormatterTest() {
        super(SchemaLanguage.INSTANCE, SchemaFileType.INSTANCE);
    }

    /**
     * Scenario: namespace and entities with inconsistent spacing
     * Focus: space after namespace/colon and blank lines between top-level entities
     */
    public void testBasicFormat() {
        String input = """
                namespace  Sample
                Person(人物) :  EntityType
                    desc:  "ok"
                Child(孩子)  ->  Person:
                    desc:     "ok"
                """;

        String expected = """
                namespace Sample
                Person(人物):EntityType
                  desc:"ok"
                Child(孩子)->Person:
                  desc:"ok"
                """;

        withCommonSettings(settings -> {
            settings.SPACE_AFTER_COLON = false;
            settings.SPACE_AFTER_COMMA = true;
            settings.SPACE_BEFORE_COMMA = false;
            settings.BLANK_LINES_AFTER_PACKAGE = 0;
            settings.BLANK_LINES_AFTER_IMPORTS = 0;

            CommonCodeStyleSettings.IndentOptions indentOptions = settings.getIndentOptions();
            if (indentOptions != null) {
                indentOptions.INDENT_SIZE = 2;
                indentOptions.TAB_SIZE = 2;
            }
        }, () -> assertReformat(input, expected));
    }

    /**
     * Scenario: inherited types list with commas
     * Focus: space after arrow and commas in inherited declarations
     */
    public void testFormatInheritedTypes() {
        String input = """
                namespace Sample
                Single(单):TypeA
                    desc: "x"
                Multi(多)->TypeA,TypeB:
                    desc:"x"
                """;

        String expected = """
                namespace Sample
                
                Single(单): TypeA
                    desc: "x"

                Multi(多)-> TypeA, TypeB:
                    desc: "x"
                """;

        withCommonSettings(settings -> {
            settings.SPACE_AFTER_COLON = true;
            settings.SPACE_AFTER_COMMA = true;
            settings.SPACE_BEFORE_COMMA = false;
            settings.BLANK_LINES_AFTER_PACKAGE = 1;
            settings.BLANK_LINES_AFTER_IMPORTS = 1;

            CommonCodeStyleSettings.IndentOptions indentOptions = settings.getIndentOptions();
            if (indentOptions != null) {
                indentOptions.INDENT_SIZE = 4;
                indentOptions.TAB_SIZE = 4;
            }
        }, () -> assertReformat(input, expected));
    }

}
