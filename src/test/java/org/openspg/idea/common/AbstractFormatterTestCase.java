package org.openspg.idea.common;

import com.intellij.application.options.CodeStyle;
import com.intellij.lang.Language;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.codeStyle.CommonCodeStyleSettings;

import java.util.List;
import java.util.function.Consumer;

public abstract class AbstractFormatterTestCase extends AbstractPlatformTestCase {

    private final Language myLanguage;
    private final FileType myFileType;

    public AbstractFormatterTestCase(Language language, FileType fileType) {
        super();
        myLanguage = language;
        myFileType = fileType;
    }

    protected void withCommonSettings(Consumer<CommonCodeStyleSettings> consumer, Runnable action) {
        CodeStyleSettings baseSettings = CodeStyle.getSettings(getProject());
        CodeStyle.doWithTemporarySettings(getProject(), baseSettings, (x) -> {
            consumer.accept(x.getCommonSettings(myLanguage));
            action.run();
        });
    }

    protected void assertReformat(String input, String expected) {
        myFixture.configureByText(myFileType, input);
        WriteCommandAction.runWriteCommandAction(getProject(), () -> {
            CodeStyleManager manager = CodeStyleManager.getInstance(getProject());
            manager.reformatText(
                    myFixture.getFile(),
                    List.of(myFixture.getFile().getTextRange())
            );
        });
        myFixture.checkResult(expected);
    }

}
