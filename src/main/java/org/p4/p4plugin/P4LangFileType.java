package org.p4.p4plugin;

import com.intellij.openapi.fileTypes.LanguageFileType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.p4.p4plugin.icon.P4LangIcon;

import javax.swing.*;


/**
 * P4 Language type
 */
public class P4LangFileType extends LanguageFileType {

    public static P4LangFileType INSTANCE = new P4LangFileType();

    P4LangFileType() {
        super(P4Lang.INSTANCE);
    }

    @NotNull
    @Override
    public String getName() {
        return "P4 Language";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Programming Protocol-independent Packet Processor";
    }

    @NotNull
    @Override
    public String getDefaultExtension() {
        return "p4";
    }

    @Nullable
    @Override
    public Icon getIcon() {
        return P4LangIcon.ICON;
    }
}
