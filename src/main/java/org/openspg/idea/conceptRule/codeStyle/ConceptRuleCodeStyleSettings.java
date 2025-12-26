package org.openspg.idea.conceptRule.codeStyle;


import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.codeStyle.CustomCodeStyleSettings;


public class ConceptRuleCodeStyleSettings extends CustomCodeStyleSettings {

    public static final int DEFAULT_INDENT_SIZE = 4;

    public static final boolean DEFAULT_SPACE_BEFORE_COMMA = false;
    public static final boolean DEFAULT_SPACE_AFTER_COMMA = false;
    public static final boolean DEFAULT_SPACE_BEFORE_COLON = false;
    public static final boolean DEFAULT_SPACE_AFTER_COLON = false;
    public static final boolean DEFAULT_SPACE_WITHIN_BRACKETS = false;
    public static final boolean DEFAULT_SPACE_WITHIN_PARENTHESES = false;
    public static final boolean DEFAULT_SPACE_AROUND_ASSIGNMENT_OPERATORS = true;
    public static final boolean DEFAULT_SPACE_AROUND_EQUALITY_OPERATORS = true;


    public ConceptRuleCodeStyleSettings(CodeStyleSettings container) {
        super("ConceptRuleCodeStyleSettings", container);
    }

}
