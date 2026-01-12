package org.openspg.idea.schema.structureView;

import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.util.treeView.smartTree.TreeElement;
import com.intellij.navigation.ItemPresentation;
import com.intellij.testFramework.fixtures.BasePlatformTestCase;
import org.jetbrains.annotations.NotNull;
import org.openspg.idea.schema.SchemaFileType;
import org.openspg.idea.schema.psi.SchemaFile;

public class SchemaStructureViewTest extends BasePlatformTestCase {

    /**
     * Scenario: structure view with entities, properties, and child entities
     * Focus: node presentation text and hierarchy
     * Assert: entity/property/child nodes and aliases display correctly
     */
    public void testStructureViewTreeAndPresentation() {
        String text = """
                namespace Sample

                Person(人物): EntityType
                    desc: "a great man"
                    properties:
                        birth(生日): Text
                        locatedIn(所在地): EntityType

                Company(公司): EntityType
                """;

        SchemaFile file = parse(text);
        SchemaStructureViewModel model = new SchemaStructureViewModel(null, file);
        StructureViewTreeElement root = model.getRoot();

        StructureViewTreeElement person = findChildByText(root, "Person");
        assertNotNull(person);
        assertEquals("人物", presentationOf(person).getLocationString());

        StructureViewTreeElement company = findChildByText(root, "Company");
        assertNotNull(company);
        assertEquals("公司", presentationOf(company).getLocationString());

        StructureViewTreeElement desc = findChildByText(person, "desc");
        assertNotNull(desc);
        assertEquals("\"a great man\"", presentationOf(desc).getLocationString());

        StructureViewTreeElement properties = findChildByText(person, "properties");
        assertNotNull(properties);

        StructureViewTreeElement birth = findChildByText(properties, "birth");
        assertNotNull(birth);
        assertEquals("生日", presentationOf(birth).getLocationString());

        StructureViewTreeElement locatedIn = findChildByText(properties, "locatedIn");
        assertNotNull(locatedIn);
        assertEquals("所在地", presentationOf(locatedIn).getLocationString());
    }

    /**
     * Scenario: property value is a plain text block
     * Focus: structure view value truncation
     * Assert: locationString equals [[...]]
     */
    public void testPlainTextPropertyPresentation() {
        String text = """
                Person(人物): EntityType
                    detail: [[long text]]
                """;

        SchemaFile file = parse(text);
        SchemaStructureViewModel model = new SchemaStructureViewModel(null, file);
        StructureViewTreeElement root = model.getRoot();

        StructureViewTreeElement person = findChildByText(root, "Person");
        assertNotNull(person);

        StructureViewTreeElement detail = findChildByText(person, "detail");
        assertNotNull(detail);
        assertEquals("[[...]]", presentationOf(detail).getLocationString());
    }

    private SchemaFile parse(String text) {
        return (SchemaFile) myFixture.configureByText(SchemaFileType.INSTANCE, text);
    }

    private StructureViewTreeElement findChildByText(@NotNull StructureViewTreeElement parent, @NotNull String text) {
        for (TreeElement child : parent.getChildren()) {
            if (child instanceof StructureViewTreeElement element) {
                ItemPresentation presentation = presentationOf(element);
                if (text.equals(presentation.getPresentableText())) {
                    return element;
                }
            }
        }
        return null;
    }

    private ItemPresentation presentationOf(@NotNull StructureViewTreeElement element) {
        return element.getPresentation();
    }
}
