package org.p4.p4plugin.style;

import com.intellij.application.options.TabbedLanguageCodeStylePanel;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import org.p4.p4plugin.P4Lang;

public class P4LangTabbedCodeStylePanel extends TabbedLanguageCodeStylePanel {

    public P4LangTabbedCodeStylePanel(CodeStyleSettings currentSettings, CodeStyleSettings settings) {
        super(P4Lang.INSTANCE, currentSettings, settings);
    }

    @Override
    protected void initTabs(CodeStyleSettings codeStyleSettings) {
        addIndentOptionsTab(codeStyleSettings);
    }
}
