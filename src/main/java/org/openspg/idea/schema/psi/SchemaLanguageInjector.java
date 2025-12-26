package org.openspg.idea.schema.psi;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.InjectedLanguagePlaces;
import com.intellij.psi.LanguageInjector;
import com.intellij.psi.PsiLanguageInjectionHost;
import org.jetbrains.annotations.NotNull;
import org.openspg.idea.conceptRule.ConceptRuleLanguage;

public final class SchemaLanguageInjector implements LanguageInjector {

    @Override
    public void getLanguagesToInject(@NotNull PsiLanguageInjectionHost host, @NotNull InjectedLanguagePlaces injectionPlacesRegistrar) {
        if (host instanceof SchemaPlainTextContent content) {
            injectionPlacesRegistrar.addPlace(
                    ConceptRuleLanguage.INSTANCE,
                    TextRange.from(0, content.getTextLength()),
                    "",
                    ""
            );
        }
    }
}
