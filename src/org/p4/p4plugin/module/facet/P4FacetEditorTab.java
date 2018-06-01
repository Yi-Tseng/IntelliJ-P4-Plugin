package org.p4.p4plugin.module.facet;

import com.intellij.facet.ui.FacetEditorTab;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.p4.p4plugin.module.P4SdkPanel;

import javax.swing.*;

public class P4FacetEditorTab extends FacetEditorTab {

    private final P4ModuleSettings p4ModuleSettings;
    private final P4SdkPanel p4SdkPanel;

    public P4FacetEditorTab(P4ModuleSettings p4ModuleSettings) {
        this.p4ModuleSettings = p4ModuleSettings;
        this.p4SdkPanel = new P4SdkPanel();
    }

    @NotNull
    @Override
    public JComponent createComponent() {
        return p4SdkPanel;
    }

    @Override
    public boolean isModified() {
        return false;
    }

    @Nls
    @Override
    public String getDisplayName() {
        return P4ModuleSettings.FACET_NAME;
    }
}
