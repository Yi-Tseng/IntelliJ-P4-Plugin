package org.p4.p4plugin.structview;

import com.intellij.navigation.ItemPresentation;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.Nullable;
import org.p4.p4plugin.P4Lang;
import org.p4.p4plugin.icon.P4LangIcon;
import org.p4.p4plugin.parsing.P4Parser;
import org.p4.p4plugin.psi.P4LangFile;

import javax.swing.*;

import static org.p4.p4plugin.structview.P4StructureViewElement.isRule;

public class P4StructViewElementPresentation implements ItemPresentation {

    PsiElement element;

    public P4StructViewElementPresentation(PsiElement element) {
        this.element = element;
    }

    String findName(PsiElement e) {
        // find `name` node
        for (PsiElement c : e.getChildren()) {
            if (isRule(c, P4Parser.RULE_name)) {
                return c.getText();
            }
        }
        return null;
    }

    @Override
    public @Nullable String getPresentableText() {
        StringBuilder text = new StringBuilder();
        if (element instanceof P4LangFile) {
            P4LangFile f = (P4LangFile) element;
            return f.getOriginalFile().getName();
        } else if (isRule(element, P4Parser.RULE_controlDeclaration)) {
            text.append("Control: ");
            // control : controlTypeDeclaration optConstructorParameters.....
            PsiElement controlTypeDecl = element.getFirstChild();
            if (isRule(controlTypeDecl, P4Parser.RULE_controlTypeDeclaration)) {
                text.append(findName(controlTypeDecl));
            }
        } else if (isRule(element, P4Parser.RULE_actionDeclaration)) {
            // actionDeclaration : optAnnotations ACTION name '(' parameterList ')' blockStatement
            text.append("Action: ");
            text.append(findName(element));
        } else if (isRule(element, P4Parser.RULE_parserDeclaration)) {
            // parserDeclaration : parserTypeDeclaration optConstructorParameters
            // parserTypeDeclaration : optAnnotations PARSER name optTypeParameters '(' parameterList ')'
            text.append("Parser: ");
            PsiElement parserTypeDeclaration = element.getFirstChild();
            if (isRule(parserTypeDeclaration, P4Parser.RULE_parserTypeDeclaration)) {
                text.append(findName(parserTypeDeclaration));
            }
        } else if (isRule(element, P4Parser.RULE_instantiation)) {
            text.append(findName(element));
        } else if (isRule(element, P4Parser.RULE_typeDeclaration)) {
            // can be one of derived type, typedef, parser type, control type, and package type.
            PsiElement typeDeclDetail = element.getFirstChild();
            if (isRule(typeDeclDetail, P4Parser.RULE_derivedTypeDeclaration)) {
                PsiElement derivedType = typeDeclDetail.getFirstChild();
                if (derivedType == null) {
                    text.append("Derived type");
                } else {
                    if (isRule(derivedType, P4Parser.RULE_headerTypeDeclaration)) {
                        text.append("Header: ");
                    }
                    if (isRule(derivedType, P4Parser.RULE_headerUnionDeclaration)) {
                        text.append("Header Union: ");
                    }
                    if (isRule(derivedType, P4Parser.RULE_structTypeDeclaration)) {
                        text.append("Struct: ");
                    }
                    if (isRule(derivedType, P4Parser.RULE_enumDeclaration)) {
                        text.append("Enum: ");
                    }
                    text.append(findName(derivedType));
                }
            } else if (isRule(typeDeclDetail, P4Parser.RULE_typedefDeclaration)) {
                text.append("Typedef: ");
                text.append(findName(typeDeclDetail));
            }
            // TODO: parser type, control type, and package type
        } else if (isRule(element, P4Parser.RULE_constantDeclaration)) {
            text.append("Constant: ");
            text.append(findName(element));
        } else if (isRule(element, P4Parser.RULE_tableDeclaration)) {
            text.append("Table: ");
            text.append(findName(element));
        } else if (isRule(element, P4Parser.RULE_controlBody)) {
            text.append("Control body");
        } else {
            text.append(element.getNode().getElementType().toString());
        }
        return text.toString();
    }

    @Override
    public @Nullable String getLocationString() {
        return "";
    }

    @Override
    public @Nullable Icon getIcon(boolean unused) {
        if (isRule(element, P4Parser.RULE_actionDeclaration)) {
            return P4LangIcon.ACTION;
        }
        if (isRule(element, P4Parser.RULE_tableDeclaration)) {
            return P4LangIcon.TABLE;
        }
        if (isRule(element, P4Parser.RULE_controlDeclaration)) {
            return P4LangIcon.CONTROL;
        }
        if (isRule(element, P4Parser.RULE_typeDeclaration)) {
            PsiElement typeDeclDetail = element.getFirstChild();
            if (isRule(typeDeclDetail, P4Parser.RULE_derivedTypeDeclaration)) {
                PsiElement derivedType = typeDeclDetail.getFirstChild();
                if (derivedType != null) {
                    if (isRule(derivedType, P4Parser.RULE_headerTypeDeclaration) ||
                            isRule(derivedType, P4Parser.RULE_headerUnionDeclaration)) {
                        return P4LangIcon.HEADER;
                    }
                }
            }
        }
        return P4LangIcon.ICON;
    }
}
