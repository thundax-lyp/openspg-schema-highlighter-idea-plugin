package org.openspg.idea.conceptRule.formatter;

import com.intellij.psi.codeStyle.CommonCodeStyleSettings;
import org.openspg.idea.common.AbstractFormatterTestCase;
import org.openspg.idea.conceptRule.ConceptRuleFileType;
import org.openspg.idea.conceptRule.ConceptRuleLanguage;

public class ConceptRuleFormatterTest extends AbstractFormatterTestCase {

    public ConceptRuleFormatterTest() {
        super(ConceptRuleLanguage.INSTANCE, ConceptRuleFileType.INSTANCE);
    }

    /**
     * Scenario: wrapper rule with mixed spacing
     * Focus: namespace spacing, blank lines, braces, and binary operators
     */
    public void testBasicFormat() {
        String input = """
                namespace  Sample
                `TaxEvent`/`Foo`:`TaxEvent`/`Bar`
                    rule: [[
                        Define (s:`TaxEvent`/`Foo`)-[p:leadTo]->(o:`TaxEvent`/`Bar`){
                            Structure{
                                s->o
                            }
                            Rule{
                                R1:s==o
                            }
                            Action{}
                        }
                    ]]
                """;

        String expected = """
                namespace Sample

                `TaxEvent`/`Foo`:`TaxEvent`/`Bar`
                  rule: [[
                    Define (s:`TaxEvent`/`Foo`)-[p:leadTo]->(o:`TaxEvent`/`Bar`) {
                      Structure {
                        s->o
                      }
                      Rule {
                        R1:s == o
                      }
                      Action {
                      }
                    }
                  ]]
                """;

        withCommonSettings(settings -> {
            settings.SPACE_AFTER_COLON = false;
            settings.SPACE_BEFORE_COLON = false;
            settings.SPACE_AFTER_COMMA = true;
            settings.SPACE_BEFORE_COMMA = false;
            settings.SPACE_AROUND_ASSIGNMENT_OPERATORS = true;
            settings.SPACE_AROUND_EQUALITY_OPERATORS = true;
            settings.SPACE_WITHIN_BRACKETS = false;
            settings.SPACE_WITHIN_PARENTHESES = false;
            settings.BLANK_LINES_AFTER_PACKAGE = 1;
            settings.BLANK_LINES_AFTER_IMPORTS = 0;

            CommonCodeStyleSettings.IndentOptions indentOptions = settings.getIndentOptions();
            if (indentOptions != null) {
                indentOptions.INDENT_SIZE = 2;
                indentOptions.TAB_SIZE = 2;
            }
        }, () -> assertReformat(input, expected));
    }

    /**
     * Scenario: spacing inside parentheses/brackets and comma/assignment rules
     * Focus: spaces within delimiters and around commas/equals
     */
    public void testSpacesInDelimiters() {
        String input = """
                namespace Sample
                `TaxEvent`/`Foo`:`TaxEvent`/`Bar`
                    rule: [[
                        Define (s:`TaxEvent`/`Foo`)-[p:leadTo|belongTo]->(o:`TaxEvent`/`Bar`){
                            Structure{
                                (s)-[p:leadTo|belongTo]->(o)
                            }
                        }
                    ]]
                """;

        String expected = """
                namespace Sample

                `TaxEvent`/`Foo`:`TaxEvent`/`Bar`
                    rule: [[
                        Define ( s:`TaxEvent`/`Foo` )-[ p:leadTo|belongTo ]->( o:`TaxEvent`/`Bar` ) {
                            Structure {
                                ( s )-[ p:leadTo|belongTo ]->( o )
                            }
                        }
                    ]]
                """;

        withCommonSettings(settings -> {
            settings.SPACE_AFTER_COLON = false;
            settings.SPACE_BEFORE_COLON = false;
            settings.SPACE_AFTER_COMMA = true;
            settings.SPACE_BEFORE_COMMA = false;
            settings.SPACE_AROUND_ASSIGNMENT_OPERATORS = true;
            settings.SPACE_AROUND_EQUALITY_OPERATORS = true;
            settings.SPACE_WITHIN_BRACKETS = true;
            settings.SPACE_WITHIN_PARENTHESES = true;
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
