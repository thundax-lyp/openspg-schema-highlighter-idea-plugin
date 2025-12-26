package org.openspg.idea.conceptRule;

import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class ConceptRuleElementType extends IElementType {

    public ConceptRuleElementType(@NotNull @NonNls String debugName) {
        super(debugName, ConceptRuleLanguage.INSTANCE);
    }

}
