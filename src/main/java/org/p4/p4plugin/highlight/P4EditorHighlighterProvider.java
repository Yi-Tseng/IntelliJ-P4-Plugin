package org.p4.p4plugin.highlight;

import com.intellij.openapi.editor.colors.EditorColorsScheme;
import com.intellij.openapi.editor.ex.util.LayeredLexerEditorHighlighter;
import com.intellij.openapi.editor.highlighter.EditorHighlighter;
import com.intellij.openapi.fileTypes.EditorHighlighterProvider;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class P4EditorHighlighterProvider implements EditorHighlighterProvider {

    @Override
    public EditorHighlighter getEditorHighlighter(@Nullable Project project,
                                                  @NotNull FileType fileType,
                                                  @Nullable VirtualFile virtualFile,
                                                  @NotNull EditorColorsScheme colors) {
        return new P4EditorHighlighter(project, fileType, virtualFile, colors);
    }
}

class P4EditorHighlighter extends LayeredLexerEditorHighlighter {


    public P4EditorHighlighter(@Nullable Project project,
                               @NotNull FileType fileType,
                               @Nullable VirtualFile virtualFile,
                               @NotNull EditorColorsScheme colors) {
        super(new P4SyntaxHighlighter(), colors);
    }
}
