package org.openspg.idea.conceptRule.parser;

import com.intellij.lang.ASTNode;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;
import org.openspg.idea.conceptRule.ConceptRuleLanguage;
import org.openspg.idea.conceptRule.lexer.ConceptRuleLexerAdapter;
import org.openspg.idea.conceptRule.psi.ConceptRuleFile;
import org.openspg.idea.conceptRule.psi.ConceptRuleTypes;

public final class ConceptRuleParserDefinition implements ParserDefinition {

    public static final IFileElementType FILE = new IFileElementType(ConceptRuleLanguage.INSTANCE);

    @NotNull
    @Override
    public Lexer createLexer(Project project) {
        return new ConceptRuleLexerAdapter();
    }

    @NotNull
    @Override
    public TokenSet getCommentTokens() {
        return TokenSet.create(ConceptRuleTypes.LINE_COMMENT, ConceptRuleTypes.BLOCK_COMMENT);
    }

    @NotNull
    @Override
    public TokenSet getWhitespaceTokens() {
        return TokenSet.create(TokenType.WHITE_SPACE, TokenType.NEW_LINE_INDENT, TokenType.BAD_CHARACTER);
    }

    @NotNull
    @Override
    public TokenSet getStringLiteralElements() {
        return TokenSet.EMPTY;
    }

    @NotNull
    @Override
    public PsiParser createParser(final Project project) {
        return new ConceptRuleParser();
    }

    @NotNull
    @Override
    public IFileElementType getFileNodeType() {
        return FILE;
    }

    @NotNull
    @Override
    public PsiFile createFile(@NotNull FileViewProvider viewProvider) {
        return new ConceptRuleFile(viewProvider);
    }

    @NotNull
    @Override
    public PsiElement createElement(ASTNode node) {
        return ConceptRuleTypes.Factory.createElement(node);
    }

}
