package org.openspg.idea.conceptRule.codeStyle;

import com.intellij.application.options.IndentOptionsEditor;
import com.intellij.lang.Language;
import com.intellij.psi.codeStyle.CodeStyleSettingsCustomizable;
import com.intellij.psi.codeStyle.CommonCodeStyleSettings;
import com.intellij.psi.codeStyle.LanguageCodeStyleSettingsProvider;
import org.jetbrains.annotations.NotNull;
import org.openspg.idea.conceptRule.ConceptRuleBundle;
import org.openspg.idea.conceptRule.ConceptRuleLanguage;
import org.openspg.idea.conceptRule.demo.ConceptRuleDemo;

import static org.openspg.idea.conceptRule.codeStyle.ConceptRuleCodeStyleSettings.*;

public final class ConceptRuleLanguageCodeStyleSettingsProvider extends LanguageCodeStyleSettingsProvider {

    @NotNull
    @Override
    public Language getLanguage() {
        return ConceptRuleLanguage.INSTANCE;
    }

    @Override
    public void customizeSettings(@NotNull CodeStyleSettingsCustomizable consumer, @NotNull SettingsType settingsType) {
        if (settingsType == SettingsType.INDENT_SETTINGS) {
            consumer.showStandardOptions("INDENT_SIZE");

        } else if (settingsType == SettingsType.SPACING_SETTINGS) {
            consumer.showStandardOptions(
                    "SPACE_AFTER_COMMA",
                    "SPACE_BEFORE_COMMA",
                    "SPACE_AFTER_COLON",
                    "SPACE_BEFORE_COLON",
                    "SPACE_WITHIN_BRACKETS",
                    "SPACE_WITHIN_PARENTHESES",
                    "SPACE_AROUND_EQUALITY_OPERATORS",
                    "SPACE_AROUND_ASSIGNMENT_OPERATORS"
            );
            consumer.renameStandardOption(
                    "SPACE_AROUND_EQUALITY_OPERATORS",
                    ConceptRuleBundle.message("ConceptRuleCodeStyleSettings.space.around.binary.operators")
            );
            consumer.renameStandardOption(
                    "SPACE_AROUND_ASSIGNMENT_OPERATORS",
                    ConceptRuleBundle.message("ConceptRuleCodeStyleSettings.space.around.assigment.operators")
            );

        } else if (settingsType == SettingsType.BLANK_LINES_SETTINGS) {
            consumer.showStandardOptions(
                    "BLANK_LINES_AFTER_PACKAGE",
                    "BLANK_LINES_AFTER_IMPORTS"
            );

            consumer.renameStandardOption(
                    "BLANK_LINES_AFTER_PACKAGE",
                    ConceptRuleBundle.message("ConceptRuleCodeStyleSettings.blankLines.after.namespace")
            );
            consumer.renameStandardOption(
                    "BLANK_LINES_AFTER_IMPORTS",
                    ConceptRuleBundle.message("ConceptRuleCodeStyleSettings.blankLines.after.schema")
            );
        }
    }

    @Override
    public @NotNull IndentOptionsEditor getIndentOptionsEditor() {
        return new IndentOptionsEditor(this);
    }

    @Override
    public String getCodeSample(@NotNull SettingsType settingsType) {
        return ConceptRuleDemo.getCodeStyleText();
    }

    @Override
    protected void customizeDefaults(@NotNull CommonCodeStyleSettings commonSettings, @NotNull CommonCodeStyleSettings.IndentOptions indentOptions) {
        super.customizeDefaults(commonSettings, indentOptions);

        commonSettings.SPACE_BEFORE_COMMA = DEFAULT_SPACE_BEFORE_COMMA;
        commonSettings.SPACE_AFTER_COMMA = DEFAULT_SPACE_AFTER_COMMA;
        commonSettings.SPACE_BEFORE_COLON = DEFAULT_SPACE_BEFORE_COLON;
        commonSettings.SPACE_AFTER_COLON = DEFAULT_SPACE_AFTER_COLON;
        commonSettings.SPACE_WITHIN_BRACKETS = DEFAULT_SPACE_WITHIN_BRACKETS;
        commonSettings.SPACE_WITHIN_PARENTHESES = DEFAULT_SPACE_WITHIN_PARENTHESES;
        commonSettings.SPACE_AROUND_ASSIGNMENT_OPERATORS = DEFAULT_SPACE_AROUND_ASSIGNMENT_OPERATORS;
        commonSettings.SPACE_AROUND_EQUALITY_OPERATORS = DEFAULT_SPACE_AROUND_EQUALITY_OPERATORS;

        indentOptions.INDENT_SIZE = DEFAULT_INDENT_SIZE;
    }

}
