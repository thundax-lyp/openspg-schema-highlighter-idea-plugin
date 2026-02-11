package org.openspg.idea.conceptRule.reference;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.openspg.idea.conceptRule.ConceptRuleFileType;
import org.openspg.idea.conceptRule.psi.*;
import org.openspg.idea.schema.SchemaFileType;
import org.openspg.idea.schema.SchemaIcons;
import org.openspg.idea.schema.psi.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class ConceptRuleSchemaNameReference extends PsiPolyVariantReferenceBase<ConceptRuleSchemaName> {

    private final String myNamespace;
    private final List<PathPattern> myPathPatterns;

    public ConceptRuleSchemaNameReference(@NotNull ConceptRuleSchemaName element) {
        super(element, new TextRange(0, element.getTextLength()));
        myNamespace = findNamespace(element.getContainingFile());
        myPathPatterns = loadPathPatterns(element);
    }

    @Override
    public ResolveResult @NotNull [] multiResolve(boolean incompleteCode) {
        return streamOfStructureNameDeclaration()
                .filter(element -> {
                    List<String> pathSegments = findEntityPath(element);
                    for (PathPattern pattern : myPathPatterns) {
                        if (pattern.matches(pathSegments)) {
                            return true;
                        }
                    }
                    return false;
                })
                .map(PsiElementResolveResult::new)
                .toArray(ResolveResult[]::new);
    }

    @Override
    public Object @NotNull [] getVariants() {
        return streamOfStructureNameDeclaration()
                .map(x -> LookupElementBuilder
                        .create(x)
                        .withIcon(SchemaIcons.FILE)
                        .withTypeText(x.getStructureName().getFullName())
                )
                .toArray(LookupElement[]::new);
    }

    private String findNamespace(PsiFile file) {
        if (file.getFileType() == SchemaFileType.INSTANCE) {
            SchemaNamespace namespace = PsiTreeUtil.findChildOfType(file, SchemaNamespace.class);
            if (namespace != null) {
                return namespace.getNamespace();
            }

        } else if (file.getFileType() == ConceptRuleFileType.INSTANCE) {
            ConceptRuleNamespaceDeclaration namespace = PsiTreeUtil.findChildOfType(file, ConceptRuleNamespaceDeclaration.class);
            if (namespace != null) {
                return namespace.getValue();
            }
        }
        return null;
    }

    private Stream<SchemaStructureNameDeclaration> streamOfStructureNameDeclaration() {
        return streamOfFiles()
                .filter(x -> myNamespace == null || myNamespace.equals(findNamespace(x)))
                .flatMap(x -> PsiTreeUtil.findChildrenOfType(x, SchemaBasicStructureDeclaration.class).stream())
                .map(SchemaBasicStructureDeclaration::getStructureNameDeclaration);
    }

    private List<PathPattern> loadPathPatterns(ConceptRuleSchemaName element) {
        List<PathPattern> pathPatterns = new ArrayList<>();

        List<String> pathTail = new ArrayList<>();
        pathTail.add(0, element.getLabel());

        if (!element.isMajor()) {
            ConceptRuleSchemaName majorName = PsiTreeUtil.getPrevSiblingOfType(element, ConceptRuleSchemaName.class);
            if (majorName == null) {
                throw new IllegalStateException("minor name must have a major name");
            }
            pathTail.add(0, majorName.getLabel());
        }

        PsiElement nodePatternOrRuleWrapperHead = PsiTreeUtil.getParentOfType(element, ConceptRuleNodePattern.class, ConceptRuleRuleWrapperHead.class);
        if (nodePatternOrRuleWrapperHead != null) {
            pathPatterns.add(new PathPattern(new ArrayList<>(pathTail)));
            return pathPatterns;
        }

        ConceptRuleElementLookup elementLookup = findElementLookup(element);
        if (elementLookup == null) {
            throw new IllegalStateException("schemaName must be under nodePattern or ruleDeclaration or elementPattern");
        }

        PsiTreeUtil.findChildrenOfType(elementLookup, ConceptRuleConceptType.class).forEach(conceptType -> {
            List<String> pathSegment = new ArrayList<>(
                    conceptType.getSchemaNameList()
                            .stream()
                            .map(ConceptRuleSchemaName::getLabel)
                            .toList()
            );
            pathSegment.addAll(pathTail);
            pathPatterns.add(new PathPattern(pathSegment));
        });

        return pathPatterns;
    }

    private ConceptRuleElementLookup findElementLookup(ConceptRuleSchemaName element) {
        ConceptRuleConceptRuleDeclaration ruleDeclaration = PsiTreeUtil.getParentOfType(element, ConceptRuleConceptRuleDeclaration.class);
        if (ruleDeclaration != null) {
            return ruleDeclaration
                    .getConceptRuleHead()
                    .getNodePatternList().get(0)
                    .getElementPatternDeclarationAndFiller()
                    .getElementLookup();
        }
        ConceptRuleElementPattern elementPattern = PsiTreeUtil.getParentOfType(element, ConceptRuleElementPattern.class);
        if (elementPattern != null) {
            return elementPattern
                    .getNodePattern()
                    .getElementPatternDeclarationAndFiller()
                    .getElementLookup();
        }
        return null;
    }

    private Stream<PsiFile> streamOfFiles() {
        Collection<VirtualFile> files = FileTypeIndex.getFiles(
                SchemaFileType.INSTANCE,
                GlobalSearchScope.projectScope(myElement.getProject())
        );
        PsiManager psiManager = PsiManager.getInstance(myElement.getProject());
        return files.stream()
                .map(psiManager::findFile)
                .filter(Objects::nonNull);
    }

    private List<String> findEntityPath(SchemaStructureNameDeclaration declaration) {
        List<String> path = new ArrayList<>();
        PsiTreeUtil.collectParents(declaration, SchemaEntity.class, false, x -> false)
                .forEach(x -> path.add(0, x.getRealName()));
        return path;
    }

    public static class PathPattern {
        private final List<String> segments;

        public PathPattern(List<String> segments) {
            this.segments = segments;
        }

        public boolean matches(List<String> thatSegments) {
            if (this.segments.size() != thatSegments.size()) {
                return false;
            }
            for (int i = 0; i < this.segments.size(); i++) {
                String thisSegment = this.segments.get(i);
                String thatSegment = thatSegments.get(i);
                if (thisSegment != null && !thisSegment.isEmpty() && !thisSegment.equals(thatSegment)) {
                    return false;
                }
            }
            return true;
        }
    }
}
