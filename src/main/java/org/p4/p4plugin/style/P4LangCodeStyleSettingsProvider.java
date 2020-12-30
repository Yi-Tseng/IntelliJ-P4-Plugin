package org.p4.p4plugin.style;

import com.google.common.io.Resources;
import com.intellij.lang.Language;
import com.intellij.psi.codeStyle.CommonCodeStyleSettings;
import com.intellij.psi.codeStyle.LanguageCodeStyleSettingsProvider;
import kotlin.text.Charsets;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.p4.p4plugin.P4Lang;

import java.io.IOException;
import java.net.URL;

public class P4LangCodeStyleSettingsProvider extends LanguageCodeStyleSettingsProvider {
    @NotNull
    @Override
    public Language getLanguage() {
        return P4Lang.INSTANCE;
    }

    @Override
    public String getCodeSample(@NotNull SettingsType settingsType) {
        URL url = getClass().getResource("/demo/demo.p4");
        try {
            return Resources.toString(url, Charsets.UTF_8);
        } catch (IOException e) {
            System.err.printf("Got error while reading the file %s: %s", url, e);
            return "";
        }
    }

    @Nullable
    @Override
    public CommonCodeStyleSettings getDefaultCommonSettings() {
        CommonCodeStyleSettings commonCodeStyleSettings = new CommonCodeStyleSettings(P4Lang.INSTANCE);
        CommonCodeStyleSettings.IndentOptions indentOptions = commonCodeStyleSettings.initIndentOptions();
        indentOptions.INDENT_SIZE = 4;
        indentOptions.TAB_SIZE = 8;
        return commonCodeStyleSettings;
    }
}
