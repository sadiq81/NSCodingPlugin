package dk.eazyit.appcode.nscoding;

import com.intellij.codeInsight.intention.IntentionAction;
import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import com.jetbrains.swift.psi.SwiftClassDeclaration;
import com.jetbrains.swift.psi.SwiftTypeInheritanceClause;
import com.jetbrains.swift.psi.impl.types.SwiftReferenceClassTypeImpl;
import com.jetbrains.swift.psi.types.SwiftType;
import org.antlr.v4.runtime.misc.NotNull;
import org.jetbrains.annotations.Nls;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.jetbrains.swift.psi.SwiftDeclarationKind.classDeclaration;

public class NSCodingGenerationAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {

    }

    public void update(AnActionEvent anActionEvent) {

        Boolean enabled = false;
        SwiftClassDeclaration classDeclaration = PsiTreeUtil.getParentOfType(psiElement, SwiftClassDeclaration.class);
        if (classDeclaration == null) {
            return false;
        }
        SwiftTypeInheritanceClause inheritanceClause = classDeclaration.getTypeInheritanceClause();
        if (inheritanceClause == null) {
            return false;
        }
        List<SwiftType> types = inheritanceClause.getTypes();
        if (types.size() == 0) {
            return false;
        }
        Optional<SwiftType> nscoding = types.stream().filter(type -> type.getPresentableText().equals("NSCoding")).findFirst();

        return nscoding.isPresent();

        final Project project = anActionEvent.getData(CommonDataKeys.PROJECT);
        if (project == null)
            return;
        Object navigatable = anActionEvent.getData(CommonDataKeys.NAVIGATABLE);
        anActionEvent.getPresentation().setEnabledAndVisible(navigatable != null);
    }


    //    @Override
//    public boolean isAvailable(@NotNull Project project, Editor editor, @NotNull PsiElement psiElement) {
//        SwiftClassDeclaration classDeclaration = PsiTreeUtil.getParentOfType(psiElement, SwiftClassDeclaration.class);
//        if (classDeclaration == null) {
//            return false;
//        }
//        SwiftTypeInheritanceClause inheritanceClause = classDeclaration.getTypeInheritanceClause();
//        if (inheritanceClause == null) {
//            return false;
//        }
//        List<SwiftType> types = inheritanceClause.getTypes();
//        if (types.size() == 0) {
//            return false;
//        }
//        Optional<SwiftType> nscoding = types.stream().filter(type -> type.getPresentableText().equals("NSCoding")).findFirst();
//
//        return nscoding.isPresent();
//    }
//
//    @Override
//    public void invoke(@NotNull Project project, Editor editor, @NotNull PsiElement psiElement) throws IncorrectOperationException {
//    }
//
//    @Nls
//    @NotNull
//    @Override
//    public String getFamilyName() {
//        return getText();
//    }
//
//    @NotNull
//    @Override
//    public String getText() {
//        return "NSCoding";
//    }
}
