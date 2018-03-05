package dk.eazyit.appcode.nscoding;

import com.intellij.ide.util.DefaultPsiElementCellRenderer;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.LabeledComponent;
import com.intellij.psi.PsiElement;
import com.intellij.ui.CollectionListModel;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.components.JBList;
import com.jetbrains.swift.psi.SwiftClassDeclaration;
import com.jetbrains.swift.psi.SwiftVariable;
import com.jetbrains.swift.psi.SwiftVariableDeclaration;
import com.jetbrains.swift.psi.impl.SwiftVariableDeclarationGenImpl;
import org.antlr.v4.runtime.misc.Nullable;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class SelectMembersDialog extends DialogWrapper {

    private final CollectionListModel<SwiftVariableDeclaration> myFields;
    private final LabeledComponent<JPanel> myComponent;

    public SelectMembersDialog(SwiftClassDeclaration swiftClass, String title, String labelText) {
        super(swiftClass.getProject());
        setTitle(title);

        myFields = new CollectionListModel<SwiftVariableDeclaration>(fieldsFrom(swiftClass));
        JList fieldList = new JBList(myFields);
        fieldList.setCellRenderer(new DefaultPsiElementCellRenderer());
        ToolbarDecorator decorator = ToolbarDecorator.createDecorator(fieldList);
        decorator.disableAddAction();
        JPanel panel = decorator.createPanel();
        myComponent = LabeledComponent.create(panel, labelText);

        init();
    }

    private SwiftVariableDeclaration[] fieldsFrom(final SwiftClassDeclaration psiClass) {
        final List<SwiftVariableDeclaration> filteredFields = new ArrayList<>();
        for (PsiElement element : psiClass.getChildren()) {

            if (element instanceof SwiftVariableDeclarationGenImpl) {
                SwiftVariableDeclarationGenImpl impl = (SwiftVariableDeclarationGenImpl) element;
                if (impl.isStatic() ||
                        (impl.getPatternInitializerList().size() > 0 && impl.getPatternInitializerList().get(0).isComputed())) {
                    continue;
                } else {
                    filteredFields.add(impl);
                }
            }
        }

        return filteredFields.toArray(new SwiftVariableDeclaration[filteredFields.size()]);
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return myComponent;
    }

    public List<SwiftVariableDeclaration> getFields() {
        return myFields.getItems();
    }
}
