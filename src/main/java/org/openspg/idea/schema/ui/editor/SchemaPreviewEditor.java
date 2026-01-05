package org.openspg.idea.schema.ui.editor;

import com.alibaba.fastjson.JSONObject;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.colors.EditorColorsListener;
import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.editor.event.DocumentEvent;
import com.intellij.openapi.editor.event.DocumentListener;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorState;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.util.UserDataHolderBase;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.ui.components.JBLabel;
import com.intellij.util.ui.JBUI;
import com.intellij.util.ui.components.BorderLayoutPanel;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.openspg.idea.schema.psi.SchemaEntity;
import org.openspg.idea.schema.psi.SchemaNamespace;
import org.openspg.idea.schema.psi.SchemaRootEntity;
import org.openspg.idea.schema.ui.editor.model.SchemaEntityModel;
import org.openspg.idea.schema.ui.editor.model.SchemaNamespaceModel;
import org.openspg.idea.schema.util.EditorUtils;

import javax.swing.*;
import java.beans.PropertyChangeListener;
import java.util.*;
import java.util.stream.Collectors;


public class SchemaPreviewEditor extends UserDataHolderBase implements FileEditor, DocumentListener {

    private static final Logger logger = Logger.getInstance(SchemaPreviewEditor.class);

    private final Project myProject;
    private final VirtualFile myFile;
    private final Document myDocument;

    private BorderLayoutPanel myHtmlPanelWrapper;
    private SchemaHtmlPanel myPanel;

    private final List<SchemaEntityModel> myEntityModels = new ArrayList<>();
    private SchemaNamespaceModel myNamespaceModel = new SchemaNamespaceModel();

    public SchemaPreviewEditor(Project project, VirtualFile file) {
        myProject = project;
        myFile = file;

        myDocument = FileDocumentManager.getInstance().getDocument(myFile);
        assert myDocument != null;
        myDocument.addDocumentListener(this);

        this.initModels();

        ApplicationManager.getApplication()
                .getMessageBus()
                .connect(this)
                .subscribe(EditorColorsManager.TOPIC, (EditorColorsListener) scheme -> {
                    if (myPanel != null) {
                        myPanel.updateStyle();
                    }
                });

        logger.info("Schema preview editor initialized");
    }

    private void initModels() {
        PsiFile psiFile = PsiManager.getInstance(myProject).findFile(myFile);

        SchemaNamespace namespace = PsiTreeUtil.getChildOfType(psiFile, SchemaNamespace.class);
        myNamespaceModel = elementToModel(namespace);

        myEntityModels.clear();
        PsiTreeUtil.getChildrenOfTypeAsList(psiFile, SchemaEntity.class)
                .stream()
                .map(this::elementToModel)
                .forEach(myEntityModels::add);
    }

    @Override
    public @NotNull JComponent getComponent() {
        if (myHtmlPanelWrapper == null) {
            myHtmlPanelWrapper = JBUI.Panels.simplePanel();

            JBLabel loadingLabel = new JBLabel("Loading......");
            myHtmlPanelWrapper.addToCenter(loadingLabel);

            SchemaHtmlPanel tempPanel = null;
            try {
                tempPanel = new SchemaHtmlPanel();
                tempPanel.updateSchema(buildSchemaString());
                myHtmlPanelWrapper.addToCenter(tempPanel.getComponent());

            } catch (Throwable e) {
                myHtmlPanelWrapper.addToCenter(new JBLabel("<html><body>Your environment does not support JCEF.<br>Check the Registry 'ide.browser.jcef.enabled'.<br>" + e.getMessage() + "<body></html>"));

            } finally {
                myPanel = tempPanel;
                myHtmlPanelWrapper.remove(loadingLabel);
                myHtmlPanelWrapper.repaint();
            }
        }

        return myHtmlPanelWrapper;
    }

    @Override
    public @Nullable JComponent getPreferredFocusedComponent() {
        return null;
    }

    @Override
    public @Nls(capitalization = Nls.Capitalization.Title) @NotNull String getName() {
        return "SchemaPreview";
    }

    @Override
    public void setState(@NotNull FileEditorState state) {

    }

    @Override
    public boolean isModified() {
        return false;
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public void addPropertyChangeListener(@NotNull PropertyChangeListener listener) {

    }

    @Override
    public void removePropertyChangeListener(@NotNull PropertyChangeListener listener) {

    }

    @Override
    public void dispose() {
        if (myDocument != null) {
            myDocument.removeDocumentListener(this);
        }

        if (myPanel != null) {
            Disposer.dispose(myPanel);
        }
    }

    @Override
    public @Nullable VirtualFile getFile() {
        return myFile;
    }

    @Override
    public void documentChanged(@NotNull DocumentEvent event) {
        PsiDocumentManager.getInstance(myProject).performLaterWhenAllCommitted(() -> {
            PsiFile psiFile = PsiDocumentManager.getInstance(myProject).getPsiFile(event.getDocument());
            if (psiFile == null) {
                return;
            }
            SchemaNamespace namespace = PsiTreeUtil.getChildOfType(psiFile, SchemaNamespace.class);
            myNamespaceModel = elementToModel(namespace);

            List<SchemaEntityModel> finalEntityModels = myEntityModels
                    .stream()
                    .filter(model -> {
                        /*
                         * Step 1: delete disappeared entity
                         *         if an entity's start between event-start and event-end, it is disappeared
                         */
                        int eventStart = event.getOffset();
                        int eventEnd = event.getOffset() + event.getOldLength();
                        return eventStart > model.getStart() || model.getStart() > eventEnd;
                    })
                    .peek(model -> {
                        /*
                         * Step 2: update entity start
                         *         alignment entity.start if entity.start is larger than event.start
                         * NOTICE: entity.start will be the key of entity
                         */
                        if (model.getStart() >= event.getOffset()) {
                            model.setStart(model.getStart() + event.getNewLength() - event.getOldLength());
                        }
                    })
                    .collect(Collectors.toList());

            /*
             * Step 3: delete invalid entity from finalEntityModels by entity-start
             */
            List<SchemaEntity> currentEntities = PsiTreeUtil.getChildrenOfTypeAsList(psiFile, SchemaRootEntity.class)
                    .stream()
                    .map(SchemaRootEntity::getEntity)
                    .toList();
            Set<Integer> validEntityStart = currentEntities.stream()
                    .map(SchemaEntity::getTextOffset)
                    .collect(Collectors.toSet());
            finalEntityModels.removeIf(model -> !validEntityStart.contains(model.getStart()));

            /*
             * Step 4: append new entities to finalEntityModels
             */
            Set<Integer> currentModelStarts = finalEntityModels.stream()
                    .map(SchemaEntityModel::getStart)
                    .collect(Collectors.toSet());
            currentEntities.forEach(element -> {
                // add new model if element.start between event.start and event.end
                if (!currentModelStarts.contains(element.getTextOffset())) {
                    finalEntityModels.add(SchemaPreviewEditor.this.elementToModel(element));
                }
            });

            /*
             * Step 5: update properties (expect id) of finalEntityModels
             */
            finalEntityModels.forEach(model -> {
                SchemaEntity entity = currentEntities
                        .stream()
                        .filter(element -> element.getTextOffset() == model.getStart())
                        .findFirst()
                        .orElse(null);
                assert entity != null;
                elementToModel(entity, model);
            });

            /*
             * Step 6: sort finalEntityModels and update to myEntityModels
             */
            finalEntityModels.sort(Comparator.comparing(SchemaEntityModel::getStart));

            myEntityModels.clear();
            myEntityModels.addAll(finalEntityModels);

            if (myPanel != null) {
                myPanel.updateSchema(this.buildSchemaString());
            }
        });
    }

    private SchemaNamespaceModel elementToModel(SchemaNamespace element) {
        SchemaNamespaceModel model = new SchemaNamespaceModel();
        if (element != null) {
            model.setJsonValue(element.toJson());
        }
        return model;
    }

    private SchemaEntityModel elementToModel(SchemaEntity element, SchemaEntityModel model) {
        model.setStart(element.getTextOffset());
        model.setText(element.getText());
        model.setJsonValue(element.toJson());
        return model;
    }

    private SchemaEntityModel elementToModel(SchemaEntity element) {
        SchemaEntityModel model = new SchemaEntityModel();
        model.setId(UUID.randomUUID().toString());
        return elementToModel(element, model);
    }

    private String buildSchemaString() {
        Map<String, Object> map = new HashMap<>();
        map.put("namespace", myNamespaceModel.getJsonValue());
        map.put("entities", myEntityModels
                .stream()
                .map(model -> {
                    Map<String, Object> data = model.getJsonValue();
                    data.put("id", model.getId());
                    return data;
                })
                .toList()
        );
        return JSONObject.toJSONString(EditorUtils.normalizeJsonMap(map));
    }

    public void activateEntity(@NotNull String name) {
        if (myPanel != null) {
            myPanel.activateEntity(name);
        }
    }

}
