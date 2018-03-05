package dk.eazyit.appcode.nscoding;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import com.jetbrains.swift.psi.SwiftClassDeclaration;
import com.jetbrains.swift.psi.SwiftTypeInheritanceClause;
import com.jetbrains.swift.psi.SwiftVariableDeclaration;
import com.jetbrains.swift.psi.types.SwiftType;

import java.util.List;
import java.util.Optional;

public class NSCodingGenerationAction extends AnAction {

    private static final String TO_STRING_TITLE = "Select fields for NSCoding";
    private static final String TO_STRING_LABEL_TEXT = "Fields to include in NSCoding:";

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        PsiFile psiFile = anActionEvent.getData(CommonDataKeys.PSI_FILE);
        if (psiFile == null) { return; }

        SwiftClassDeclaration classDeclaration = PsiTreeUtil.getChildOfAnyType(psiFile, SwiftClassDeclaration.class);
        if (classDeclaration == null) { return; }

        SelectMembersDialog dlg = new SelectMembersDialog(classDeclaration, TO_STRING_TITLE, TO_STRING_LABEL_TEXT);
        dlg.show();
        if (dlg.isOK()) {
            System.out.println("TEST");
        }
    }

    public void update(AnActionEvent anActionEvent) {

        anActionEvent.getPresentation().setEnabledAndVisible(false);

        PsiFile psiFile = anActionEvent.getData(CommonDataKeys.PSI_FILE);
        if (psiFile == null) { return; }

        SwiftClassDeclaration classDeclaration = PsiTreeUtil.getChildOfAnyType(psiFile, SwiftClassDeclaration.class);
        if (classDeclaration == null) { return; }

        SwiftTypeInheritanceClause inheritanceClause = classDeclaration.getTypeInheritanceClause();
        if (inheritanceClause == null) { return; }

        List<SwiftType> types = inheritanceClause.getTypes();
        if (types.size() == 0) { return; }

        Optional<SwiftType> nscoding = types.stream().filter(type -> type.getPresentableText().equals("NSCoding")).findFirst();
        if (!nscoding.isPresent()) { return; }

        anActionEvent.getPresentation().setEnabledAndVisible(true);
    }

    public void generateInitializer(PsiFile psiFile, final List<SwiftVariableDeclaration> fields) {

        new WriteCommandAction.Simple(psiFile.getProject(), psiFile.getContainingFile()) {

            @Override
            protected void run() throws Throwable {
//                PsiElementFactory elementFactory = JavaPsiFacade.getElementFactory(psiClass.getProject());
//                final JavaCodeStyleManager javaCodeStyleManager = JavaCodeStyleManager.getInstance(psiClass.getProject());
//
//                final String equalsMethodAsString = equalsBuilder.generateEqualsMethod(psiClass, fields);
//                PsiMethod equalsMethod = elementFactory.createMethodFromText(equalsMethodAsString, psiClass);
//                PsiElement equalsElement = psiClass.add(equalsMethod);
//                javaCodeStyleManager.shortenClassReferences(equalsElement);
//
//                final String hashCodeMethodAsString = hashCodeBuilder.generateHashCodeMethod(psiClass, fields);
//                PsiMethod hashCodeMethod = elementFactory.createMethodFromText(hashCodeMethodAsString, psiClass);
//                PsiElement hashCodeElement = psiClass.add(hashCodeMethod);
//                javaCodeStyleManager.shortenClassReferences(hashCodeElement);

            }

        }.execute();
    }

}
