package org.openspg.idea.schema;

import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class SchemaElementType extends IElementType {

    public SchemaElementType(@NotNull @NonNls String debugName) {
        super(debugName, SchemaLanguage.INSTANCE);
    }

}
