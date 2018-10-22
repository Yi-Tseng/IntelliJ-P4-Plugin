package org.p4.p4plugin.module;

import com.intellij.ide.util.projectWizard.ModuleBuilder;
import com.intellij.ide.util.projectWizard.ModuleBuilderListener;
import com.intellij.ide.util.projectWizard.ModuleWizardStep;
import com.intellij.ide.util.projectWizard.SourcePathsBuilder;
import com.intellij.ide.util.projectWizard.WizardContext;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.roots.CompilerModuleExtension;
import com.intellij.openapi.roots.ContentEntry;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.roots.ui.configuration.ModulesProvider;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.p4.p4plugin.icon.P4LangIcon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class P4ModuleBuilder extends ModuleBuilder implements SourcePathsBuilder, ModuleBuilderListener {
    private static final String P4_MODULE_BUILDER_ID = "P4_MODULE_BUILDER";
    private final Logger log = LoggerFactory.getLogger(getClass());
    private List<Pair<String, String>> sourcePaths;

    public P4ModuleBuilder() {
        addListener(this);
    }

    @Nullable
    @Override
    public String getBuilderId() {
        return P4_MODULE_BUILDER_ID;
    }

    @Override
    public Icon getNodeIcon() {
        return P4LangIcon.ICON;
    }

    @Override
    public String getDescription() {
        return "P4 module.";
    }

    @Override
    public String getPresentableName() {
        return "P4 Language";
    }

    @Override
    public String getGroupName() {
        return "P4";
    }

    @Override
    public ModuleWizardStep[] createWizardSteps(@NotNull WizardContext wizardContext, @NotNull ModulesProvider modulesProvider) {
        return new ModuleWizardStep[]{};
    }

    @Override
    public void setupRootModel(ModifiableRootModel rootModel) throws ConfigurationException {
        final CompilerModuleExtension compilerModuleExtension = rootModel.getModuleExtension(CompilerModuleExtension.class);
        compilerModuleExtension.setExcludeOutput(true);
        rootModel.inheritSdk();

        ContentEntry contentEntry = doAddContentEntry(rootModel);
        if (contentEntry != null) {
            final List<Pair<String,String>> sourcePaths = getSourcePaths();

            if (sourcePaths != null) {
                for (final Pair<String, String> sourcePath : sourcePaths) {
                    String first = sourcePath.first;
                    new File(first).mkdirs();
                    final VirtualFile sourceRoot = LocalFileSystem.getInstance()
                            .refreshAndFindFileByPath(FileUtil.toSystemIndependentName(first));
                    if (sourceRoot != null) {
                        contentEntry.addSourceFolder(sourceRoot, false, sourcePath.second);
                    }
                }
            }
        }
    }

    @Override
    public ModuleType getModuleType() {
        return new P4ModuleType();
    }

    @Override
    public void moduleCreated(@NotNull Module module) {
    }

    @Override
    public List<Pair<String, String>> getSourcePaths() throws ConfigurationException {
        if (sourcePaths == null) {
            sourcePaths = new ArrayList<>();
            String path = getContentEntryPath() + File.separator + "src";
            new File(path).mkdirs();
            sourcePaths.add(Pair.create(path, ""));
        }
        return sourcePaths;
    }

    @Override
    public void setSourcePaths(List<Pair<String, String>> sourcePaths) {
        if (sourcePaths == null) {
            this.sourcePaths = null;
        } else {
            this.sourcePaths = new ArrayList<>(sourcePaths);
        }
    }

    @Override
    public void addSourcePath(Pair<String, String> path) {
        if (this.sourcePaths == null) {
            this.sourcePaths = new ArrayList<>();
        }
        this.sourcePaths.add(path);
    }
}
