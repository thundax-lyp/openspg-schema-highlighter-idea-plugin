package org.openspg.idea.schema;

import com.intellij.openapi.util.IconLoader;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public final class SchemaIcons {

    public static final Icon FILE = IconLoader.getIcon("/icons/pluginIcon.svg", SchemaIcons.class);


    public static final class Nodes {
        /** 16x16 */
        public static final @NotNull Icon Entity = IconLoader.getIcon("/icons/nodes/entity.svg", SchemaIcons.class);
        /** 16x16 */
        public static final @NotNull Icon EntityMeta = IconLoader.getIcon("/icons/nodes/entityMeta.svg", SchemaIcons.class);
        /** 16x16 */
        public static final @NotNull Icon EmptyMeta = IconLoader.getIcon("/icons/nodes/emptyMeta.svg", SchemaIcons.class);
        /** 16x16 */
        public static final @NotNull Icon Property = IconLoader.getIcon("/icons/nodes/property.svg", SchemaIcons.class);
        /** 16x16 */
        public static final @NotNull Icon PropertyMeta = IconLoader.getIcon("/icons/nodes/propertyMeta.svg", SchemaIcons.class);
        /** 16x16 */
        public static final @NotNull Icon SubProperty = IconLoader.getIcon("/icons/nodes/subProperty.svg", SchemaIcons.class);
        /** 16x16 */
        public static final @NotNull Icon SubPropertyMeta = IconLoader.getIcon("/icons/nodes/subPropertyMeta.svg", SchemaIcons.class);

    }

}
