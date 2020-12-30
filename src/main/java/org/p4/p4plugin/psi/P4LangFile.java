package org.p4.p4plugin.psi;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import org.jetbrains.annotations.NotNull;
import org.p4.p4plugin.P4Lang;
import org.p4.p4plugin.P4LangFileType;

public class P4LangFile extends PsiFileBase {

    public P4LangFile(@NotNull FileViewProvider viewProvider) {
        super(viewProvider, P4Lang.INSTANCE);
    }

    @NotNull
    @Override
    public FileType getFileType() {
        return P4LangFileType.INSTANCE;
    }

    @Override
    public String toString() {
        return "P4 File";
    }
}
