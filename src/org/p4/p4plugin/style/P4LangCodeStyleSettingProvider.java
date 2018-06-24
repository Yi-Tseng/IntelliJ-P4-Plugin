package org.p4.p4plugin.style;

import com.intellij.application.options.CodeStyleAbstractConfigurable;
import com.intellij.application.options.CodeStyleAbstractPanel;
import com.intellij.lang.Language;
import com.intellij.openapi.options.Configurable;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.codeStyle.CodeStyleSettingsProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.p4.p4plugin.P4Lang;

public class P4LangCodeStyleSettingProvider extends CodeStyleSettingsProvider {
    @NotNull
    @Override
    public Configurable createSettingsPage(CodeStyleSettings settings,
                                           CodeStyleSettings originalSettings) {
        return new CodeStyleAbstractConfigurable(settings, originalSettings, "P4") {
            @Override
            protected CodeStyleAbstractPanel createPanel(CodeStyleSettings codeStyleSettings) {
                return new P4LangTabbedCodeStylePanel(getCurrentSettings(), settings);
            }
        };
    }

    @Nullable
    @Override
    public Language getLanguage() {
        return P4Lang.INSTANCE;
    }

    @Nullable
    @Override
    public String getConfigurableDisplayName() {
        return "P4";
    }
}
