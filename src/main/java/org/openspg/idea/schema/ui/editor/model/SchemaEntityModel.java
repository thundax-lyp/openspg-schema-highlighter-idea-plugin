package org.openspg.idea.schema.ui.editor.model;

import java.io.Serializable;
import java.util.Map;

public class SchemaEntityModel implements Serializable {

    private String id;
    private long version;
    private int textOffset;
    private int textLength;
    private String text;

    private Map<String, Object> jsonValue;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public Map<String, Object> getJsonValue() {
        return jsonValue;
    }

    public void setJsonValue(Map<String, Object> jsonValue) {
        this.jsonValue = jsonValue;
    }

    public int getTextOffset() {
        return textOffset;
    }

    public void setTextOffset(int textOffset) {
        this.textOffset = textOffset;
    }

    public int getTextLength() {
        return textLength;
    }

    public void setTextLength(int textLength) {
        this.textLength = textLength;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
