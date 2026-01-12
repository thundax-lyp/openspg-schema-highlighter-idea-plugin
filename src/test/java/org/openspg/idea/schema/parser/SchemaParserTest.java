package org.openspg.idea.schema.parser;

import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.testFramework.fixtures.BasePlatformTestCase;
import org.openspg.idea.schema.SchemaFileType;
import org.openspg.idea.schema.psi.SchemaEntity;
import org.openspg.idea.schema.psi.SchemaFile;
import org.openspg.idea.schema.psi.SchemaNamespace;
import org.openspg.idea.schema.psi.SchemaProperty;

import java.io.File;
import java.util.List;

public class SchemaParserTest extends BasePlatformTestCase {

    @Override
    protected String getTestDataPath() {
        return new File("src/test/resources/testFixture").getAbsolutePath();
    }

    /**
     * Scenario: basic sample with namespace, entity, and property
     * Focus: namespace/entity head/property parsing coverage
     * Assert: namespace, entity name/alias/types, property name/value match
     */
    public void testNamespaceAndEntity() {
        String text = """
                namespace Sample

                Person(人物): EntityType
                    desc: "a great man"
                """;

        SchemaFile file = parse(text);
        SchemaNamespace namespace = PsiTreeUtil.findChildOfType(file, SchemaNamespace.class);
        assertNotNull(namespace);
        assertEquals("Sample", namespace.getNamespace());

        SchemaEntity entity = PsiTreeUtil.findChildOfType(file, SchemaEntity.class);
        assertNotNull(entity);
        assertEquals("Person", entity.getName());
        assertEquals("人物", entity.getAliasName());
        assertEquals(List.of("EntityType"), entity.getTypes());
        assertFalse(entity.getProperties().isEmpty());

        SchemaProperty property = entity.getProperties().get(0);
        assertEquals("desc", property.getName());
        assertEquals("\"a great man\"", property.getValue());
    }

    /**
     * Scenario: entity inheritance declaration
     * Focus: inherited type list and alias parsing
     * Assert: type order and entity name/alias match
     */
    public void testInheritedTypes() {
        String text = """
                Artist("艺术家") -> Person, Star:
                """;

        SchemaFile file = parse(text);
        SchemaEntity entity = PsiTreeUtil.findChildOfType(file, SchemaEntity.class);
        assertNotNull(entity);
        assertEquals("Artist", entity.getName());
        assertEquals("\"艺术家\"", entity.getAliasName());
        assertEquals(List.of("Person", "Star"), entity.getTypes());
    }

    /**
     * Scenario: properties block with child entities
     * Focus: properties block and child entity parsing
     * Assert: child entity count and names match
     */
    public void testPropertyBody() {
        String text = """
                Person(人物): EntityType
                    properties:
                        birth(生日): Text
                        locatedIn(所在地): EntityType
                """;

        SchemaFile file = parse(text);
        SchemaEntity entity = PsiTreeUtil.findChildOfType(file, SchemaEntity.class);
        assertNotNull(entity);
        assertFalse(entity.getProperties().isEmpty());

        SchemaProperty properties = entity.getProperties().get(0);
        assertEquals("properties", properties.getName());

        assertEquals(2, properties.getEntities().size());
        assertEquals("birth", properties.getEntities().get(0).getName());
        assertEquals("locatedIn", properties.getEntities().get(1).getName());
    }

    private SchemaFile parse(String text) {
        return (SchemaFile) myFixture.configureByText(SchemaFileType.INSTANCE, text);
    }
}
