package org.openspg.idea.schema.formatter;

import com.intellij.formatting.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.TokenType;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.codeStyle.CodeStyleSettingsManager;
import com.intellij.psi.formatter.common.AbstractBlock;
import com.intellij.psi.formatter.common.SettingsAwareBlock;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static org.openspg.idea.schema.psi.SchemaTypes.*;

public class SchemaBlock extends AbstractBlock implements SettingsAwareBlock {

    private static final Set<IElementType> NONBLOCK_ELEMENT_TYPES = Set.of(
            TokenType.WHITE_SPACE, TokenType.NEW_LINE_INDENT, TokenType.BAD_CHARACTER, INDENT, DEDENT
    );

    private static final Set<IElementType> INJECTED_ELEMENT_TYPES = Set.of(
            PLAIN_TEXT_CONTENT
    );

    private static final Set<IElementType> NORMAL_INDENT_BLOCK_ELEMENT_TYPES = Set.of(
            ENTITY, PROPERTY, PLAIN_TEXT_CONTENT
    );

    private final Indent myIndent;
    private final SpacingBuilder mySpacingBuilder;
    private final boolean myInjection;

    protected SchemaBlock(
            @NotNull ASTNode node,
            @Nullable Wrap wrap,
            @Nullable Alignment alignment,
            @Nullable SpacingBuilder spacingBuilder
    ) {
        this(node, wrap, alignment, spacingBuilder, null);
    }

    protected SchemaBlock(
            @NotNull ASTNode node,
            @Nullable Wrap wrap,
            @Nullable Alignment alignment,
            @Nullable SpacingBuilder spacingBuilder,
            @Nullable Indent indent
    ) {
        super(node, wrap, alignment);
        mySpacingBuilder = spacingBuilder;
        myIndent = indent;

        if (INJECTED_ELEMENT_TYPES.contains(node.getElementType())) {
            myInjection = true;
            setBuildIndentsOnly(false);
        } else {
            myInjection = false;
            setBuildIndentsOnly(true);
        }
    }

    @Override
    protected List<Block> buildChildren() {
        if (myInjection) {
            return new ArrayList<>();
        }

        Stream<ASTNode> children;
        if (myNode.getElementType() == BASIC_PROPERTY_DECLARATION) {
            children = extractNodes(
                    Stream.of(myNode.getChildren(null)).toList(),
                    x -> x.getElementType() != PLAIN_TEXT_CONTENT
            ).stream();
        } else {
            children = Stream.of(myNode.getChildren(null));
        }

        return children
                .map(this::buildBlock)
                .filter(Objects::nonNull)
                .toList();
    }

    private List<ASTNode> extractNodes(List<ASTNode> nodes, Predicate<ASTNode> predicate) {
        List<ASTNode> currNodes = nodes;
        while (true) {
            List<ASTNode> nextNodes = currNodes.stream()
                    .flatMap(x -> {
                        ASTNode[] children = x.getChildren(null);
                        if (children.length == 0 || !predicate.test(x)) {
                            return Stream.of(x);
                        }
                        return Stream.of(children);
                    })
                    .toList();
            if (nextNodes.equals(currNodes)) {
                return currNodes;
            }
            currNodes = nextNodes;
        }
    }

    private Block buildBlock(ASTNode node) {
        IElementType type = node.getElementType();
        if (NONBLOCK_ELEMENT_TYPES.contains(type)) {
            return null;
        }

        Indent indent = null;
        if (NORMAL_INDENT_BLOCK_ELEMENT_TYPES.contains(type)) {
            if (node.getTreeParent().getElementType() != ROOT_ENTITY) {
                indent = Indent.getNormalIndent();
            }
        }

        return new SchemaBlock(
                node,
                Wrap.createWrap(WrapType.NONE, true),
                Alignment.createAlignment(),
                mySpacingBuilder,
                indent
        );
    }

    @Override
    public Indent getIndent() {
        return myIndent == null ? Indent.getNoneIndent() : myIndent;
    }

    @Nullable
    @Override
    public Spacing getSpacing(@Nullable Block firstChild, @NotNull Block secondChild) {
        return mySpacingBuilder == null ? null : mySpacingBuilder.getSpacing(this, firstChild, secondChild);
    }

    @Override
    public boolean isLeaf() {
        return myNode.getFirstChildNode() == null;
    }

    @Override
    public @NotNull CodeStyleSettings getSettings() {
        return CodeStyleSettingsManager.getInstance().createSettings();
    }
}
