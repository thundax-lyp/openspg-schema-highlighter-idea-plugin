package org.openspg.idea.schema.reference;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.ResolveResult;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.testFramework.fixtures.BasePlatformTestCase;
import org.openspg.idea.schema.SchemaFileType;
import org.openspg.idea.schema.psi.*;

import java.util.List;

public class SchemaReferenceTest extends BasePlatformTestCase {

    /**
     * Scenario: variable structure types reference declared entities
     * Focus: variable type references resolve within the same file
     * Assert: resolved targets match variable type text
     */
    public void testVariableStructureTypeResolve() {
        String text = """
                Parent(父): EntityType
                Sibling(兄): EntityType
                Child(子): Parent
                Adopted(养) -> Parent, Sibling:
                """;

        SchemaFile file = parse(text);
        List<SchemaVariableStructureType> types =
                PsiTreeUtil.findChildrenOfType(file, SchemaVariableStructureType.class)
                        .stream()
                        .toList();

        assertEquals(3, types.size());

        for (SchemaVariableStructureType type : types) {
            PsiReference reference = type.getReference();
            assertNotNull(reference);

            PsiElement resolved = reference.resolve();
            assertNotNull(resolved);
            assertEquals(type.getText(), resolved.getText());
        }
    }

    /**
     * Scenario: variable structure type has no matching entity
     * Focus: unresolved reference handling
     * Assert: resolve returns null for unknown types
     */
    public void testVariableStructureTypeUnresolved() {
        String text = """
                Parent(父): EntityType
                Child(子): UnknownType
                """;

        SchemaFile file = parse(text);
        List<SchemaVariableStructureType> types =
                PsiTreeUtil.findChildrenOfType(file, SchemaVariableStructureType.class)
                        .stream()
                        .toList();

        assertEquals(1, types.size());

        PsiReference reference = types.get(0).getReference();
        assertNotNull(reference);
        assertNull(reference.resolve());
    }

    /**
     * Scenario: multiple entities share the same name
     * Focus: multiResolve returns all matching targets
     * Assert: multiResolve includes every matching declaration
     */
    public void testVariableStructureTypeMultiResolve() {
        String text = """
                Duplicate(一): EntityType
                Duplicate(二): EntityType
                Child(子): Duplicate
                """;

        SchemaFile file = parse(text);
        List<SchemaVariableStructureType> types =
                PsiTreeUtil.findChildrenOfType(file, SchemaVariableStructureType.class)
                        .stream()
                        .toList();

        assertEquals(1, types.size());

        PsiReference reference = types.get(0).getReference();
        assertNotNull(reference);
        assertTrue(reference instanceof SchemaVariableStructureTypeReference);

        ResolveResult[] results = ((SchemaVariableStructureTypeReference) reference).multiResolve(false);
        assertEquals(2, results.length);

        String firstText = results[0].getElement() == null ? null : results[0].getElement().getText();
        String secondText = results[1].getElement() == null ? null : results[1].getElement().getText();

        assertEquals("Duplicate", firstText);
        assertEquals("Duplicate", secondText);
    }

    private SchemaFile parse(String text) {
        return (SchemaFile) myFixture.configureByText(SchemaFileType.INSTANCE, text);
    }
}
