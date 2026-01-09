package org.openspg.idea.schema.ui.editor.jcef;

import org.cef.network.CefRequest;
import org.jetbrains.annotations.NotNull;
import org.openspg.idea.schema.ui.editor.ColorThemeUtils;
import org.openspg.idea.schema.util.UIUtils;

import java.nio.charset.StandardCharsets;

public class FetchThemeCssSupplier extends SchemaResourceSupplier {

    public static final String RESOURCE_SCHEMA_THEME = "schema-theme.css";

    private byte[] resourceBytes;
    private boolean isDarkTheme;

    public FetchThemeCssSupplier() {
        this.isDarkTheme = UIUtils.isDarkTheme();
    }

    @Override
    public boolean isSupported(@NotNull CefRequest request) {
        return request.getURL().endsWith(RESOURCE_SCHEMA_THEME);
    }

    @Override
    public Resource getResource(@NotNull CefRequest request) {
        return new Resource() {
            @Override
            public @NotNull String getContentType() {
                return "text/css";
            }

            @Override
            public byte[] getContent() {
                if (resourceBytes != null && UIUtils.isDarkTheme() == isDarkTheme) {
                    return resourceBytes;
                }
                isDarkTheme = UIUtils.isDarkTheme();
                String css = ColorThemeUtils.getCssStyle();
                resourceBytes = css.getBytes(StandardCharsets.UTF_8);
                return resourceBytes;
            }
        };
    }

}
