package org.p4.p4plugin.action;

import com.google.common.collect.Streams;
import com.intellij.ide.fileTemplates.FileTemplate;
import com.intellij.ide.fileTemplates.FileTemplateManager;
import com.intellij.openapi.project.Project;

import org.p4.p4plugin.P4LangFileType;


import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class P4TemplateFileUtil {
    public static List<FileTemplate> getApplicableTemplates(Project project) {
        FileTemplateManager fileTemplateManager = FileTemplateManager.getInstance(project);
        String extension = P4LangFileType.INSTANCE.getDefaultExtension();

        return Streams.concat(Arrays.stream(fileTemplateManager.getInternalTemplates()),
                       Arrays.stream(fileTemplateManager.getAllTemplates()))
                .filter(ft -> ft.getExtension().equals(extension))
                .collect(Collectors.toList());
    }
}
