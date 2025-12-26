package org.openspg.idea.schema.parser;

import com.intellij.lang.ASTNode;
import com.intellij.lang.LightPsiParser;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiParser;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

public class SchemaParser implements PsiParser, LightPsiParser {

    private final _SchemaParser myDelegate;

    public SchemaParser() {
        myDelegate = new _SchemaParser();
    }

    @Override
    public @NotNull ASTNode parse(@NotNull IElementType root, @NotNull PsiBuilder builder) {
        return myDelegate.parse(root, builder);
    }

    @Override
    public void parseLight(IElementType root, PsiBuilder builder) {
        myDelegate.parseLight(root, builder);
    }

}
