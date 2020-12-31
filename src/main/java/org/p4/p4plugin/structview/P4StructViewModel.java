package org.p4.p4plugin.structview;

import com.intellij.ide.structureView.StructureViewModel;
import com.intellij.ide.structureView.StructureViewModelBase;
import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.util.treeView.smartTree.Sorter;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import org.p4.p4plugin.psi.P4LangFile;

public class P4StructViewModel extends StructureViewModelBase implements StructureViewModel.ElementInfoProvider {
    public P4StructViewModel(@NotNull PsiFile psiFile) {
        super(psiFile, new P4StructureViewElement(psiFile));
    }

    @Override
    public boolean isAlwaysShowsPlus(StructureViewTreeElement element) {
        return false;
    }

    @Override
    public boolean isAlwaysLeaf(StructureViewTreeElement element) {
        return element instanceof P4LangFile;
    }

    @Override
    public @NotNull Sorter[] getSorters() {
        return new Sorter[]{Sorter.ALPHA_SORTER};
    }
}
