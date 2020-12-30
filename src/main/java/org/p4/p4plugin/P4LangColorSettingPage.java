package org.p4.p4plugin;

import com.google.common.io.Resources;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.options.colors.AttributesDescriptor;
import com.intellij.openapi.options.colors.ColorDescriptor;
import com.intellij.openapi.options.colors.ColorSettingsPage;
import kotlin.text.Charsets;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.p4.p4plugin.icon.P4LangIcon;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.util.Map;

public class P4LangColorSettingPage implements ColorSettingsPage {

    private static final AttributesDescriptor[] DESCRIPTORS = new AttributesDescriptor[]{
            new AttributesDescriptor("Keywords", P4LangSyntaxHighlighter.P4LANG_KEYWORD),
            new AttributesDescriptor("String", P4LangSyntaxHighlighter.P4LANG_STRING),
            new AttributesDescriptor("Match Kind", P4LangSyntaxHighlighter.P4LANG_MATCH_KIND),
            new AttributesDescriptor("Value", P4LangSyntaxHighlighter.P4LANG_INTEGER),
            new AttributesDescriptor("Type", P4LangSyntaxHighlighter.P4LANG_TYPE),
            new AttributesDescriptor("Annontation", P4LangSyntaxHighlighter.P4LANG_ANNONTATION),
            new AttributesDescriptor("Preprocessor", P4LangSyntaxHighlighter.P4LANG_PRE_PROCESS),
            new AttributesDescriptor("Comment", P4LangSyntaxHighlighter.P4LANG_COMMENT),
            new AttributesDescriptor("Unknown token", P4LangSyntaxHighlighter.P4LANG_UNKNOWN_TOKEN),
    };

    @Nullable
    @Override
    public Icon getIcon() {
        return P4LangIcon.ICON;
    }

    @NotNull
    @Override
    public SyntaxHighlighter getHighlighter() {
        return new P4LangSyntaxHighlighter();
    }

    @NotNull
    @Override
    public String getDemoText() {
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
    public Map<String, TextAttributesKey> getAdditionalHighlightingTagToDescriptorMap() {
        return null;
    }

    @NotNull
    @Override
    public AttributesDescriptor[] getAttributeDescriptors() {
        return DESCRIPTORS;
    }

    @NotNull
    @Override
    public ColorDescriptor[] getColorDescriptors() {
        return ColorDescriptor.EMPTY_ARRAY;
    }

    @NotNull
    @Override
    public String getDisplayName() {
        return "P4 Language";
    }
}
