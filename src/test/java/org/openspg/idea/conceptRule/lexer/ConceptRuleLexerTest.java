package org.openspg.idea.conceptRule.lexer;

import org.openspg.idea.common.AbstractLexerTestCase;

import java.util.List;

import static org.openspg.idea.conceptRule.psi.ConceptRuleTypes.*;

public class ConceptRuleLexerTest extends AbstractLexerTestCase {

    public ConceptRuleLexerTest() {
        super(new ConceptRuleLexerAdapter());
    }

    /**
     * Scenario: single-line namespace declaration
     * Focus: keyword and identifier tokenization
     * Assert: token sequence and content match
     */
    public void testNamespace() {
        String text = """
                namespace Sample
                """;

        assertTokens(text, List.of(
                new CommonToken(NAMESPACE_KEYWORD, "namespace"),
                new CommonToken(UNESCAPED_SYMBOLIC_NAME, "Sample")
        ));
    }

    /**
     * Scenario: rule wrapper with define and graph structure
     * Focus: wrapper head, rule block, define head, and structure tokens
     * Assert: token sequence matches the expected order
     */
    public void testRuleWrapper() {
        String text = """
                namespace Sample
                
                `TaxEvent`/`Foo`:
                    rule: [[
                        Define (s:`TaxEvent`/`Foo`)-[p:leadTo]->(o:`TaxEvent`/`Foo`) {
                            Structure {
                            }
                        }
                    ]]
                """;

        assertTokens(text, List.of(
                // namespace Sample
                new CommonToken(NAMESPACE_KEYWORD, "namespace"),
                new CommonToken(UNESCAPED_SYMBOLIC_NAME, "Sample"),
                // `TaxEvent`/`Foo`:
                new CommonToken(ESCAPED_SYMBOLIC_NAME, "`TaxEvent`"),
                new CommonToken(DIV, "/"),
                new CommonToken(ESCAPED_SYMBOLIC_NAME, "`Foo`"),
                new CommonToken(COLON, ":"),
                // rule: [[
                new CommonToken(WRAPPER_RULE_KEYWORD, "rule"),
                new CommonToken(COLON, ":"),
                new CommonToken(OPEN_RULE_BLOCK, "[["),
                // Define (s:`TaxEvent`/`Foo`)-[p:leadTo]->(o:`TaxEvent`/`Foo`) {
                new CommonToken(DEFINE_KEYWORD, "Define"),
                new CommonToken(LPARENTH, "("),
                new CommonToken(UNESCAPED_SYMBOLIC_NAME, "s"),
                new CommonToken(COLON, ":"),
                new CommonToken(ESCAPED_SYMBOLIC_NAME, "`TaxEvent`"),
                new CommonToken(DIV, "/"),
                new CommonToken(ESCAPED_SYMBOLIC_NAME, "`Foo`"),
                new CommonToken(RPARENTH, ")"),
                new CommonToken(MINUS, "-"),
                new CommonToken(LBRACKET, "["),
                new CommonToken(UNESCAPED_SYMBOLIC_NAME, "p"),
                new CommonToken(COLON, ":"),
                new CommonToken(UNESCAPED_SYMBOLIC_NAME, "leadTo"),
                new CommonToken(RBRACKET, "]"),
                new CommonToken(RIGHT_ARROW, "->"),
                new CommonToken(LPARENTH, "("),
                new CommonToken(UNESCAPED_SYMBOLIC_NAME, "o"),
                new CommonToken(COLON, ":"),
                new CommonToken(ESCAPED_SYMBOLIC_NAME, "`TaxEvent`"),
                new CommonToken(DIV, "/"),
                new CommonToken(ESCAPED_SYMBOLIC_NAME, "`Foo`"),
                new CommonToken(RPARENTH, ")"),
                new CommonToken(LBRACE, "{"),
                // Structure { }
                new CommonToken(STRUCTURE_KEYWORD, "Structure"),
                new CommonToken(LBRACE, "{"),
                new CommonToken(RBRACE, "}"),
                new CommonToken(RBRACE, "}"),
                // ]]
                new CommonToken(CLOSE_RULE_BLOCK, "]]")
        ));
    }

    /**
     * Scenario: full concept rule file
     * Focus: lexer can tokenize without bad characters
     * Assert: no BAD_CHARACTER tokens are emitted
     */
    public void testRiskMining() {
        String text = """
                namespace RiskMining
                
                `TaxOfRiskApp`/`赌博应用`:
                    rule: [[
                        Define (s:App)-[p:belongTo]->(o:`TaxOfRiskApp`/`赌博应用`) {
                            Structure {
                                (s)
                            }
                            Constraint {
                                R1("风险标记为赌博"): s.riskMark like "%赌博%"
                            }
                        }
                    ]]
                """;

        assertTokens(text, List.of(
                // namespace RiskMining
                new CommonToken(NAMESPACE_KEYWORD, "namespace"),
                new CommonToken(UNESCAPED_SYMBOLIC_NAME, "RiskMining"),
                // `TaxOfRiskApp`/`赌博应用`:
                new CommonToken(ESCAPED_SYMBOLIC_NAME, "`TaxOfRiskApp`"),
                new CommonToken(DIV, "/"),
                new CommonToken(ESCAPED_SYMBOLIC_NAME, "`赌博应用`"),
                new CommonToken(COLON, ":"),
                //    rule: [[
                new CommonToken(WRAPPER_RULE_KEYWORD, "rule"),
                new CommonToken(COLON, ":"),
                new CommonToken(OPEN_RULE_BLOCK, "[["),
                //        Define (s:App)-[p:belongTo]->(o:`TaxOfRiskApp`/`赌博应用`) {
                new CommonToken(DEFINE_KEYWORD, "Define"),
                new CommonToken(LPARENTH, "("),
                new CommonToken(UNESCAPED_SYMBOLIC_NAME, "s"),
                new CommonToken(COLON, ":"),
                new CommonToken(UNESCAPED_SYMBOLIC_NAME, "App"),
                new CommonToken(RPARENTH, ")"),
                new CommonToken(MINUS, "-"),
                new CommonToken(LBRACKET, "["),
                new CommonToken(UNESCAPED_SYMBOLIC_NAME, "p"),
                new CommonToken(COLON, ":"),
                new CommonToken(UNESCAPED_SYMBOLIC_NAME, "belongTo"),
                new CommonToken(RBRACKET, "]"),
                new CommonToken(RIGHT_ARROW, "->"),
                new CommonToken(LPARENTH, "("),
                new CommonToken(UNESCAPED_SYMBOLIC_NAME, "o"),
                new CommonToken(COLON, ":"),
                new CommonToken(ESCAPED_SYMBOLIC_NAME, "`TaxOfRiskApp`"),
                new CommonToken(DIV, "/"),
                new CommonToken(ESCAPED_SYMBOLIC_NAME, "`赌博应用`"),
                new CommonToken(RPARENTH, ")"),
                new CommonToken(LBRACE, "{"),
                //            Structure {
                new CommonToken(STRUCTURE_KEYWORD, "Structure"),
                new CommonToken(LBRACE, "{"),
                //                (s)
                new CommonToken(LPARENTH, "("),
                new CommonToken(UNESCAPED_SYMBOLIC_NAME, "s"),
                new CommonToken(RPARENTH, ")"),
                //            }
                new CommonToken(RBRACE, "}"),
                //            Constraint {
                new CommonToken(CONSTRAINT_KEYWORD, "Constraint"),
                new CommonToken(LBRACE, "{"),
                //               R1("风险标记为赌博"): s.riskMark like "%赌博%"
                new CommonToken(UNESCAPED_SYMBOLIC_NAME, "R1"),
                new CommonToken(LPARENTH, "("),
                new CommonToken(STRING_LITERAL, "\"风险标记为赌博\""),
                new CommonToken(RPARENTH, ")"),
                new CommonToken(COLON, ":"),
                new CommonToken(UNESCAPED_SYMBOLIC_NAME, "s"),
                new CommonToken(DOT, "."),
                new CommonToken(UNESCAPED_SYMBOLIC_NAME, "riskMark"),
                new CommonToken(LIKE_KEYWORD, "like"),
                new CommonToken(STRING_LITERAL, "\"%赌博%\""),
                //            }
                new CommonToken(RBRACE, "}"),
                //        }
                new CommonToken(RBRACE, "}"),
                //    ]]
                new CommonToken(CLOSE_RULE_BLOCK, "]]")
        ));
    }


}
