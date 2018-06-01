package org.p4.p4plugin.module;

import com.intellij.openapi.module.ModuleType;
import org.jetbrains.annotations.NotNull;
import org.p4.p4plugin.icon.P4LangIcon;


import javax.swing.*;

public class P4ModuleType extends ModuleType<P4ModuleBuilder> {
    private static final String MODULE_ID = "P4_MODULE";

    public P4ModuleType() {
        super(MODULE_ID);
    }

    @NotNull
    @Override
    public P4ModuleBuilder createModuleBuilder() {
        return new P4ModuleBuilder();
    }

    @NotNull
    @Override
    public String getName() {
        return "P4 Language";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Add support for P4 language.";
    }

    @Override
    public Icon getNodeIcon(boolean b) {
        return P4LangIcon.ICON;
    }
}
