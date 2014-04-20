package org.devel.jfxcontrols.animation;

import javafx.beans.property.ObjectProperty;
import javafx.scene.control.TreeItem;

/**
 * 
 * @author stefan.illgen
 *
 */
public interface Expandable<T> extends Receiver {

	void expand();

	void collpase();

	ObjectProperty<TreeItem<T>> expandedItemProperty();

	TreeItem<T> getExpandedItem();

	void setExpandedItem(TreeItem<T> treeItem);

}
