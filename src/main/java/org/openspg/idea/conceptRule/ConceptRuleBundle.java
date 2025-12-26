package org.openspg.idea.conceptRule;

import com.intellij.DynamicBundle;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.PropertyKey;

import java.util.function.Supplier;

public class ConceptRuleBundle {

    private static final @NonNls String BUNDLE = "messages.ConceptRuleBundle";
    private static final DynamicBundle INSTANCE = new DynamicBundle(ConceptRuleBundle.class, BUNDLE);

    private ConceptRuleBundle() {
    }

    public static @NotNull @Nls String message(@NotNull @PropertyKey(resourceBundle = BUNDLE) String key, Object @NotNull ... params) {
        return INSTANCE.getMessage(key, params);
    }

    public static @NotNull Supplier<@Nls String> messagePointer(@NotNull @PropertyKey(resourceBundle = BUNDLE) String key, Object @NotNull ... params) {
        return INSTANCE.getLazyMessage(key, params);
    }

}
