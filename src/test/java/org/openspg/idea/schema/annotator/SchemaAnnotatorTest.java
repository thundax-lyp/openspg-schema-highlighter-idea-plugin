package org.openspg.idea.schema.annotator;

import com.intellij.lang.annotation.HighlightSeverity;
import org.openspg.idea.common.AbstractAnnotatorTestCase;
import org.openspg.idea.schema.SchemaFileType;
import org.openspg.idea.schema.highlighter.SchemaHighlightingColors;

import java.util.List;

public class SchemaAnnotatorTest extends AbstractAnnotatorTestCase {

    /**
     * Scenario: highlight a small schema sample
     * Focus: syntax + annotator highlights on key tokens
     * Assert: keyword/operator/entity name/alias/reference are highlighted
     */
    public void testBasic() {
        String text = """
                namespace Sample
                
                Person(人物): EntityType
                Child(孩子) -> Person:
                    desc: "ok"
                """;

        myFixture.configureByText(SchemaFileType.INSTANCE, text);

        assertHighlights(myFixture.doHighlighting(), List.of(
                HighlightData.highlight(text, "Person", 1, SchemaHighlightingColors.ENTITY_NAME),
                HighlightData.highlight(text, "人物", 1, SchemaHighlightingColors.ENTITY_ALIAS),
                HighlightData.highlight(text, "Child", 1, SchemaHighlightingColors.ENTITY_NAME),
                HighlightData.highlight(text, "孩子", 1, SchemaHighlightingColors.ENTITY_ALIAS),
                HighlightData.highlight(text, "Person", 2, SchemaHighlightingColors.ENTITY_REFERENCE)
        ));
    }

    /**
     * Scenario: invalid semantic name in structure declaration
     * Focus: error annotation from semantic name processor
     * Assert: error severity is emitted for the semantic name token
     */
    public void testInvalidSemanticNameError() {
        String text = """
                namespace Sample
                
                Person(人物): EntityType
                    properties:
                        BAD#belongTo(错误): Person
                """;

        myFixture.configureByText(SchemaFileType.INSTANCE, text);

        assertHighlights(
                myFixture.doHighlighting()
                        .stream()
                        .filter(info -> info.getSeverity() == HighlightSeverity.ERROR)
                        .toList(),
                List.of(
                        HighlightData.error(text, "BAD", 1, HighlightSeverity.ERROR, "Semantic name must be one of SYNANT, CAU, USE, SEQ, IND, INC")
                )
        );
    }

    /**
     * Scenario: invalid inherited structure type in structure declaration
     * Focus: error annotation from structure type processor
     * Assert: error severity is emitted for the structure type token
     */
    public void testInvalidInheritedStructureTypeError() {
        String text = """
                namespace Sample
                
                Person(人物): EntityType
                Work(作品) -> EntityType, UnknownType:
                """;

        myFixture.configureByText(SchemaFileType.INSTANCE, text);

        assertHighlights(
                myFixture.doHighlighting()
                        .stream()
                        .filter(info -> info.getSeverity() == HighlightSeverity.ERROR)
                        .toList(),
                List.of(
                        HighlightData.error(text, "UnknownType", 1, HighlightSeverity.ERROR, "Undefined type \"UnknownType\"")
                )
        );
    }

    /**
     * Scenario: namespace is declaration
     * Focus: error annotation from namespace processor
     * Assert: error severity is emitted for the first element of document
     */
    public void testMissingNamespaceError() {
        String text = """
                Person(人物): EntityType
                """;

        myFixture.configureByText(SchemaFileType.INSTANCE, text);

        assertHighlights(
                myFixture.doHighlighting()
                        .stream()
                        .filter(info -> info.getSeverity() == HighlightSeverity.ERROR)
                        .toList(),
                List.of(
                        HighlightData.error(text, "Person(人物): EntityType", 1, HighlightSeverity.ERROR, "First element of document must be a namespace")
                )
        );
    }

    /**
     * Scenario: namespace is singleton
     * Focus: error annotation from namespace processor
     * Assert: error severity is emitted for the redeclared namespace
     */
    public void testRedeclaredNamespaceError() {
        String text = """
                namespace First
                
                namespace Second
                
                Person(人物): EntityType
                """;

        myFixture.configureByText(SchemaFileType.INSTANCE, text);

        assertHighlights(
                myFixture.doHighlighting()
                        .stream()
                        .filter(info -> info.getSeverity() == HighlightSeverity.ERROR)
                        .toList(),
                List.of(
                        HighlightData.error(text, "namespace Second", 1, HighlightSeverity.ERROR, "Duplicate definition of namespace")
                )
        );
    }

    /**
     * Scenario: namespace is singleton
     * Focus: error annotation from namespace processor
     * Assert: error severity is emitted for the redeclared namespace
     */
    public void testRedeclaredEntityError() {
        String text = """
                namespace Sample
                
                Person(人物): EntityType
                
                Person(重名人物): ConceptType
                """;

        myFixture.configureByText(SchemaFileType.INSTANCE, text);

        assertHighlights(
                myFixture.doHighlighting()
                        .stream()
                        .filter(info -> info.getSeverity() == HighlightSeverity.ERROR)
                        .toList(),
                List.of(
                        HighlightData.error(text, "Person", 1, HighlightSeverity.ERROR, "Cannot redeclare block-scoped schema \"Person\""),
                        HighlightData.error(text, "Person", 2, HighlightSeverity.ERROR, "Cannot redeclare block-scoped schema \"Person\"")
                )
        );
    }


}
