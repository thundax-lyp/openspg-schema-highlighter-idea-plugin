package org.openspg.idea.conceptRule.annotator;

import com.intellij.lang.annotation.HighlightSeverity;
import org.openspg.idea.common.AbstractAnnotatorTestCase;
import org.openspg.idea.conceptRule.ConceptRuleFileType;
import org.openspg.idea.conceptRule.highlighter.ConceptRuleHighlightingColors;

import java.util.List;

public class ConceptRuleAnnotatorTest extends AbstractAnnotatorTestCase {

    /**
     * Scenario: basic wrapper rule and function call
     * Focus: wrapper label pattern and function name highlighting
     * Assert: label and function are highlighted with annotator keys
     */
    public void testBasicHighlight() {
        String text = """
                namespace SupplyChain

                `TaxOfProdEvent`/`价格上涨`:TaxOfCompanyEvent/`成本上涨`
                    rule: [[
                        Define (s:`TaxOfProdEvent`/`价格上涨`)-[p:leadTo]->(o:`TaxOfCompanyEvent`/`成本上涨`) {
                            Structure {
                                (s)-[:subject]->(prod:Product)-[:hasSupplyChain]->(down:Product)<-[:product]-(c:Company)
                            }
                            Constraint {
                            eventName = concat(c.name, "成本上升事件")
                            }
                            Action {
                                downEvent = createNodeInstance(
                                    type=CompanyEvent,
                                    value = {
                                        subject=c.id
                                        name=eventName
                                        trend="上涨"
                                        index="成本"
                                    }
                                )
                                createEdgeInstance(
                                    src=s,
                                    dst=downEvent,
                                    type=leadTo,
                                    value={}
                                )
                            }
                        }
                    ]]
                """;

        myFixture.configureByText(ConceptRuleFileType.INSTANCE, text);

        assertHighlights(myFixture.doHighlighting(), List.of(
                HighlightData.highlight(text, "`TaxOfProdEvent`/`价格上涨`", 1, ConceptRuleHighlightingColors.WRAPPER_PATTERN),
                HighlightData.highlight(text, "TaxOfCompanyEvent/`成本上涨`", 1, ConceptRuleHighlightingColors.WRAPPER_PATTERN),
                HighlightData.highlight(text, "`TaxOfProdEvent`/`价格上涨`", 2, ConceptRuleHighlightingColors.WRAPPER_PATTERN),
                HighlightData.highlight(text, "leadTo", 1, ConceptRuleHighlightingColors.WRAPPER_PATTERN),
                HighlightData.highlight(text, "`TaxOfCompanyEvent`/`成本上涨`", 1, ConceptRuleHighlightingColors.WRAPPER_PATTERN),
                HighlightData.highlight(text, "subject", 1, ConceptRuleHighlightingColors.WRAPPER_PATTERN),
                HighlightData.highlight(text, "Product", 1, ConceptRuleHighlightingColors.WRAPPER_PATTERN),
                HighlightData.highlight(text, "hasSupplyChain", 1, ConceptRuleHighlightingColors.WRAPPER_PATTERN),
                HighlightData.highlight(text, "Product", 2, ConceptRuleHighlightingColors.WRAPPER_PATTERN),
                HighlightData.highlight(text, "product", 1, ConceptRuleHighlightingColors.WRAPPER_PATTERN),
                HighlightData.highlight(text, "Company", 3, ConceptRuleHighlightingColors.WRAPPER_PATTERN),
                HighlightData.highlight(text, "concat", 1, ConceptRuleHighlightingColors.FUNCTION),
                HighlightData.highlight(text, "CompanyEvent", 3, ConceptRuleHighlightingColors.WRAPPER_PATTERN),
                HighlightData.highlight(text, "leadTo", 2, ConceptRuleHighlightingColors.WRAPPER_PATTERN)
        ));
    }

    /**
     * Scenario: label property list with bad order
     * Focus: label name appears after property expression
     * Assert: error annotation is emitted for the misplaced label
     */
    public void testBadLabelPropertyOrder() {
        String text = """
                namespace Sample

                `TaxEvent`/`Foo`:
                    rule: [[
                        Define (s:`TaxEvent`/`Foo`)-[p:leadTo]->(o:`TaxEvent`/`Foo`) {
                            Structure {
                                a -> b [foo=1, Bar]
                            }
                        }
                    ]]
                """;

        myFixture.configureByText(ConceptRuleFileType.INSTANCE, text);

        assertHighlights(
                myFixture.doHighlighting()
                        .stream()
                        .filter(info -> info.getSeverity() == HighlightSeverity.ERROR)
                        .toList(),
                List.of(
                        HighlightData.error(
                                text,
                                "Bar",
                                1,
                                HighlightSeverity.ERROR,
                                "Label name must be before all property expressions"
                        )
                )
        );
    }

    /**
     * Scenario: redeclared concept rules in the same file
     * Focus: duplicate rule head detection
     * Assert: both heads are reported as errors
     */
    public void testRedeclaredConceptRule() {
        String text = """
                Define (s:`TaxEvent`/`Foo`)-[p:leadTo]->(o:`TaxEvent`/`Foo`) {
                    Structure {
                        a -> b
                    }
                }
                Define (s:`TaxEvent`/`Foo`)-[p:leadTo]->(o:`TaxEvent`/`Foo`) {
                    Structure {
                        a -> b
                    }
                }
                """;

        myFixture.configureByText(ConceptRuleFileType.INSTANCE, text);

        assertHighlights(
                myFixture.doHighlighting()
                        .stream()
                        .filter(info -> info.getSeverity() == HighlightSeverity.ERROR)
                        .toList(),
                List.of(
                        HighlightData.error(
                                text,
                                "Define (s:`TaxEvent`/`Foo`)-[p:leadTo]->(o:`TaxEvent`/`Foo`)",
                                1,
                                HighlightSeverity.ERROR,
                                "Redeclare block-scoped concept rule"
                        ),
                        HighlightData.error(
                                text,
                                "Define (s:`TaxEvent`/`Foo`)-[p:leadTo]->(o:`TaxEvent`/`Foo`)",
                                2,
                                HighlightSeverity.ERROR,
                                "Redeclare block-scoped concept rule"
                        )
                )
        );
    }

}
