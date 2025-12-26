package org.openspg.idea.schema.spellchecker;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.project.DumbAware;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.tree.TreeUtil;
import com.intellij.psi.tree.TokenSet;
import com.intellij.spellchecker.tokenizer.SpellcheckingStrategy;
import com.intellij.spellchecker.tokenizer.Tokenizer;
import org.jetbrains.annotations.NotNull;
import org.openspg.idea.schema.psi.SchemaTypes;

public class SchemaSpellcheckingStrategy extends SpellcheckingStrategy implements DumbAware {

    public static final TokenSet NO_SPELLCHECKING_TYPES = TokenSet.create(
            SchemaTypes.PLAIN_TEXT_CONTENT
    );

    @Override
    public @NotNull Tokenizer<?> getTokenizer(PsiElement element) {
        final ASTNode node = element.getNode();

        if (TreeUtil.findParent(node, NO_SPELLCHECKING_TYPES) != null) {
            return SpellcheckingStrategy.EMPTY_TOKENIZER;
        }

        return SpellcheckingStrategy.TEXT_TOKENIZER;
    }
}
