package org.openspg.idea.schema.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import org.openspg.idea.schema.psi.*;
import org.openspg.idea.schema.reference.SchemaVariableStructureTypeReference;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SchemaPsiImplUtil {

    // ============================================
    // SchemaNamespace methods
    //
    public static String getNamespace(SchemaNamespace element) {
        ASTNode node = element.getNode().findChildByType(SchemaTypes.IDENTIFIER);
        if (node != null) {
            return node.getText();
        }
        return null;
    }

    public static Map<String, Object> toJson(SchemaNamespace element) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("value", getNamespace(element));
        return result;
    }

    // ============================================
    // SchemaBasicStructureDeclaration methods
    //
    public static String getName(SchemaBasicStructureDeclaration element) {
        return element.getStructureNameDeclaration().getText();
    }

    public static Map<String, Object> toJson(SchemaBasicStructureDeclaration element) {
        Map<String, Object> result = new LinkedHashMap<>();

        result.put("name", getName(element));
        result.put("aliasName", unwrapText(element.getStructureAliasDeclaration().getText()));
        result.put("types", element.getStructureTypeDeclaration().getTypes());

        return result;
    }

    // ============================================
    // SchemaBasicStructureDeclaration methods
    //
    public static List<String> getTypes(SchemaStructureTypeDeclaration element) {
        List<String> types = new ArrayList<>();

        SchemaBasicStructureTypeDeclaration basicType = element.getBasicStructureTypeDeclaration();
        if (basicType != null) {
            SchemaBasicStructureTypeVariable variable = basicType.getBasicStructureTypeVariable();
            types.add(variable.getText());
        }

        SchemaInheritedStructureTypeDeclaration inheritedType = element.getInheritedStructureTypeDeclaration();
        if (inheritedType != null) {
            for (SchemaInheritedStructureTypeVariable variable : inheritedType.getInheritedStructureTypeVariableList()) {
                types.add(variable.getText());
            }
        }

        return types;
    }

    // ============================================
    // SchemaVariableStructureType methods
    //
    public static PsiReference getReference(SchemaVariableStructureType element) {
        return new SchemaVariableStructureTypeReference(element);
    }

    // ============================================
    // SchemaBasicPropertyDeclaration methods
    //
    public static String getName(SchemaBasicPropertyDeclaration element) {
        return element.getPropertyNameDeclaration().getPropertyNameVariable().getText();
    }

    public static String getValue(SchemaBasicPropertyDeclaration element) {
        SchemaPropertyValueDeclaration declaration = element.getPropertyValueDeclaration();
        if (declaration == null) {
            return null;
        }
        return declaration.getText().trim();
    }

    public static Map<String, Object> toJson(SchemaBasicPropertyDeclaration element) {
        Map<String, Object> result = new LinkedHashMap<>();

        result.put("name", getName(element));
        result.put("value", unwrapText(getValue(element)));

        return result;
    }

    // ============================================
    // SchemaEntity methods
    //
    public static String getName(SchemaEntity element) {
        return element.getEntityHead().getName();
    }

    public static String getAliasName(SchemaEntity element) {
        return element
                .getEntityHead()
                .getBasicStructureDeclaration()
                .getStructureAliasDeclaration()
                .getText();
    }

    public static List<String> getTypes(SchemaEntity element) {
        return element
                .getEntityHead()
                .getBasicStructureDeclaration()
                .getStructureTypeDeclaration()
                .getTypes();
    }

    public static int getLevel(SchemaEntity element) {
        return getEntityLevel(element);
    }

    public static List<SchemaProperty> getProperties(SchemaEntity element) {
        if (element.getEntityBody() == null) {
            return new ArrayList<>();
        }
        return element.getEntityBody().getPropertyList();
    }

    public static boolean isBodyEmpty(SchemaEntity element) {
        return element.getEntityBody() == null || element.getEntityBody().getPropertyList().isEmpty();
    }

    public static Map<String, Object> toJson(SchemaEntity element) {
        Map<String, Object> result = element.getEntityHead().getBasicStructureDeclaration().toJson();
        if (element.getEntityBody() != null) {
            result.put("properties", element.getEntityBody()
                    .getPropertyList()
                    .stream()
                    .map(SchemaPsiImplUtil::toJson)
                    .toList()
            );
        }
        return result;
    }

    // ============================================
    // SchemaEntityHead methods
    //
    public static String getName(SchemaEntityHead element) {
        return element.getBasicStructureDeclaration().getStructureNameDeclaration().getText();
    }

    public static PsiElement setName(SchemaEntityHead element, String newName) {
        throw new IllegalArgumentException("unsupported operation. setName(SchemaEntityHead element, String newName)");
    }

    public static PsiElement getNameIdentifier(SchemaEntityHead element) {
        return element.getBasicStructureDeclaration().getStructureNameDeclaration();
    }

    // ============================================
    // SchemaProperty methods
    //
    public static String getName(SchemaProperty element) {
        return element.getPropertyHead().getBasicPropertyDeclaration().getName();
    }

    public static String getValue(SchemaProperty element) {
        return element.getPropertyHead().getBasicPropertyDeclaration().getValue();
    }

    public static int getLevel(SchemaProperty element) {
        return getEntityLevel(element);
    }

    public static List<SchemaEntity> getEntities(SchemaProperty element) {
        if (element.getPropertyBody() == null) {
            return new ArrayList<>();
        }
        return element.getPropertyBody().getEntityList();
    }

    public static boolean isBodyEmpty(SchemaProperty element) {
        return element.getPropertyBody() == null || element.getPropertyBody().getEntityList().isEmpty();
    }

    public static Map<String, Object> toJson(SchemaProperty element) {
        Map<String, Object> result = element.getPropertyHead().getBasicPropertyDeclaration().toJson();
        if (element.getPropertyBody() != null) {
            result.put("children", element.getPropertyBody()
                    .getEntityList()
                    .stream()
                    .map(SchemaPsiImplUtil::toJson)
                    .toList()
            );
        }
        return result;
    }

    private static int getEntityLevel(PsiElement element) {
        int level = 0;
        PsiElement parent = element.getParent();
        while (parent != null) {
            if (parent instanceof SchemaEntity) {
                level++;
            }
            parent = parent.getParent();
        }
        return level;
    }


    public static String unwrapText(String text) {
        if (text == null || text.length() < 2) {
            return null;
        }

        text = text.trim();
        if (text.startsWith("'") && text.endsWith("'")) {
            return text.substring(1, text.length() - 1);
        }

        if (text.startsWith("\"") && text.endsWith("\"")) {
            return text.substring(1, text.length() - 1);
        }

        if (text.startsWith("`") && text.endsWith("`")) {
            return text.substring(1, text.length() - 1);
        }

        return text.isEmpty() ? null : text;
    }
}
