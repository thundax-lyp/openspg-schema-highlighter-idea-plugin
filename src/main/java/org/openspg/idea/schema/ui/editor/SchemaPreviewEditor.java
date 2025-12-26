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
        this.myProject = project;
        this.myFile = file;

        this.myDocument = FileDocumentManager.getInstance().getDocument(myFile);
        assert this.myDocument != null;
        this.myDocument.addDocumentListener(this);

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
        PsiFile psiFile = PsiManager.getInstance(this.myProject).findFile(this.myFile);

        SchemaNamespace namespace = PsiTreeUtil.getChildOfType(psiFile, SchemaNamespace.class);
        this.myNamespaceModel = elementToModel(namespace);

        this.myEntityModels.clear();
        PsiTreeUtil.getChildrenOfTypeAsList(psiFile, SchemaEntity.class)
                .stream()
                .map(this::elementToModel)
                .forEach(this.myEntityModels::add);
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
        PsiDocumentManager.getInstance(myProject).commitDocument(event.getDocument());

        PsiFile psiFile = PsiManager.getInstance(this.myProject).findFile(this.myFile);

        SchemaNamespace namespace = PsiTreeUtil.getChildOfType(psiFile, SchemaNamespace.class);
        this.myNamespaceModel = elementToModel(namespace);

        List<SchemaEntityModel> lastEntityModels = this.myEntityModels
                .stream()
                .filter(model -> {
                    // remove model if model.start between event.start and event.old-end
                    int eventStart = event.getOffset();
                    int eventEnd = event.getOffset() + event.getOldLength();
                    if (eventStart <= model.getTextOffset() && model.getTextOffset() <= eventEnd) {
                        logger.debug("remove model. name:" + model.getText());
                        return false;
                    }
                    return true;
                })
                .peek(model -> {
                    // update model.version to 0 if event.start between model.start and model.end
                    // it will be modified if x.version is 0 during next step
                    int modelStart = model.getTextOffset();
                    int modelEnd = modelStart + model.getTextLength();
                    if (modelStart < event.getOffset() && event.getOffset() <= modelEnd) {
                        model.setVersion(0);
                    }
                })
                .peek(model -> {
                    // alignment model.start if model.start is larger than event.start
                    if (model.getTextOffset() >= event.getOffset()) {
                        model.setTextOffset(model.getTextOffset() + event.getNewLength() - event.getOldLength());
                    }
                })
                .collect(Collectors.toList());

        List<SchemaEntity> elements = PsiTreeUtil.getChildrenOfTypeAsList(psiFile, SchemaEntity.class);
        Set<Integer> validElementOffsets = elements.stream()
                .map(SchemaEntity::getTextOffset)
                .collect(Collectors.toSet());
        lastEntityModels.removeIf(model -> !validElementOffsets.contains(model.getTextOffset()));

        Set<Integer> validModelOffsets = lastEntityModels.stream()
                .map(SchemaEntityModel::getTextOffset)
                .collect(Collectors.toSet());
        elements.forEach(element -> {
            // add new model if element.start between event.start and event.end
            if (!validModelOffsets.contains(element.getTextOffset())) {
                lastEntityModels.add(SchemaPreviewEditor.this.elementToModel(element));
            }
        });

        lastEntityModels.forEach(model -> {
            // update model if model.version is 0
            if (model.getVersion() == 0) {
                SchemaEntity entity = elements
                        .stream()
                        .filter(element -> element.getTextOffset() == model.getTextOffset())
                        .findFirst()
                        .orElse(null);
                assert entity != null;
                elementToModel(entity, model);
            }
        });

        // sort models by x.start
        lastEntityModels.sort(Comparator.comparing(SchemaEntityModel::getTextOffset));

        this.myEntityModels.clear();
        this.myEntityModels.addAll(lastEntityModels);

        if (myPanel != null) {
            myPanel.updateSchema(this.buildSchemaString());
        }
    }

    private SchemaNamespaceModel elementToModel(SchemaNamespace element) {
        SchemaNamespaceModel model = new SchemaNamespaceModel();
        if (element != null) {
            model.setJsonValue(element.toJson());
        }
        return model;
    }

    private SchemaEntityModel elementToModel(SchemaEntity element, SchemaEntityModel model) {
        model.setVersion(System.currentTimeMillis());
        model.setTextOffset(element.getTextOffset());
        model.setTextLength(element.getTextLength());
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
        map.put("namespace", this.myNamespaceModel.getJsonValue());
        map.put("entities", this.myEntityModels
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
