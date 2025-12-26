package org.openspg.idea.conceptRule.psi;

import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.openspg.idea.conceptRule.ConceptRuleLanguage;

public class ConceptRuleTokenType extends IElementType {

    public ConceptRuleTokenType(@NotNull @NonNls String debugName) {
        super(debugName, ConceptRuleLanguage.INSTANCE);
    }

    @Override
    public String toString() {
        return "ConceptRuleTokenType." + super.toString();
    }

}
