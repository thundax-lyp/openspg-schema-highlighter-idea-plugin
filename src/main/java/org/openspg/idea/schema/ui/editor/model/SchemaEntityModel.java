package org.openspg.idea.schema.ui.editor.model;

import java.io.Serializable;
import java.util.Map;

public class SchemaEntityModel implements Serializable {

    private String id;
    private int start;
    private String text;

    private Map<String, Object> jsonValue;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Map<String, Object> getJsonValue() {
        return jsonValue;
    }

    public void setJsonValue(Map<String, Object> jsonValue) {
        this.jsonValue = jsonValue;
    }
}
