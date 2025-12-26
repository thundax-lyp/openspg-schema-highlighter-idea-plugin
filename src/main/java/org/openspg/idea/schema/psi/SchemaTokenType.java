package org.openspg.idea.schema.psi;

import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.openspg.idea.schema.SchemaLanguage;

public class SchemaTokenType extends IElementType {

    public SchemaTokenType(@NotNull @NonNls String debugName) {
        super(debugName, SchemaLanguage.INSTANCE);
    }

    @Override
    public String toString() {
        return "SchemaTokenType." + super.toString();
    }

}
