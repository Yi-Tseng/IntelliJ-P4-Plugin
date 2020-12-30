package org.p4.p4plugin.module;

import com.intellij.openapi.module.ModuleConfigurationEditor;
import com.intellij.openapi.roots.ui.configuration.ModuleConfigurationEditorProvider;
import com.intellij.openapi.roots.ui.configuration.ModuleConfigurationState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class P4ModuleConfigurationEditorProvider implements ModuleConfigurationEditorProvider {
    private static final Logger log = LoggerFactory.getLogger(P4ModuleConfigurationEditorProvider.class);

    @Override
    public ModuleConfigurationEditor[] createEditors(ModuleConfigurationState moduleConfigurationState) {
        P4ModuleConfigEditor editor = new P4ModuleConfigEditor(moduleConfigurationState);
        return new ModuleConfigurationEditor[]{editor};
    }

}
