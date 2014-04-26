/**
 * 
 */
package org.devel.jfxcontrols.scene.control.treetableview;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableRow;

import org.devel.jfxcontrols.scene.control.treetableview.command.Adjustable;
import org.devel.jfxcontrols.scene.control.treetableview.command.Expand;
import org.devel.jfxcontrols.scene.control.treetableview.command.RowAdjust;

/**
 * @author stefan.illgen
 *
 */
public class TreeTableView<T, A extends Adjustable<T, TreeTableRow<T>>>
	extends
		javafx.scene.control.TreeTableView<T> {

	public TreeTableView() {
		this(null);
	}

	public TreeTableView(TreeItem<T> root) {
		super(root);

	}

	private ObjectProperty<RowAdjust<T, TreeTableRow<T>>> rowAdjust;

	public ObjectProperty<RowAdjust<T, TreeTableRow<T>>> rowAdjustProperty() {
		if (rowAdjust == null) {
			rowAdjust = new SimpleObjectProperty<RowAdjust<T, TreeTableRow<T>>>(null);
		}
		return rowAdjust;
	}

	public RowAdjust<T, TreeTableRow<T>> getRowAdjust() {
		return rowAdjustProperty().get();
	}

	public void setRowAdjust(RowAdjust<T, TreeTableRow<T>> rowAdjust) {
		this.rowAdjustProperty().set(rowAdjust);
	}

	private ObjectProperty<Expand<T, TreeTableRow<T>>> expand;

	public ObjectProperty<Expand<T, TreeTableRow<T>>> expandProperty() {
		if (expand == null) {
			expand = new SimpleObjectProperty<Expand<T, TreeTableRow<T>>>(null);
		}
		return expand;
	}

	public Expand<T, TreeTableRow<T>> getExpand() {
		return expandProperty().get();
	}

	public void setExpand(Expand<T, TreeTableRow<T>> expand) {
		this.expandProperty().set(expand);
	}

	private ObjectProperty<InputMode> inputMode;

	public ObjectProperty<InputMode> inputModeProperty() {
		if (inputMode == null) {
			inputMode = new SimpleObjectProperty<InputMode>(InputMode.MOUSE_KEYBOARD);
		}
		return inputMode;
	}

	public InputMode getInputMode() {
		return inputModeProperty().get();
	}

	public void setInputMode(InputMode commandFactory) {
		this.inputModeProperty().set(commandFactory);
	}

	private ObjectProperty<TreeItem<T>> expandedTreeItem;

	public ObjectProperty<TreeItem<T>> expandedTreeItemProperty() {
		if (expandedTreeItem == null) {
			expandedTreeItem = new SimpleObjectProperty<TreeItem<T>>(null);
		}
		return expandedTreeItem;
	}

	public TreeItem<T> getExpandedTreeItem() {
		return expandedTreeItemProperty().get();
	}

	public void setExpandedTreeItem(TreeItem<T> expandedTreeItem) {
		this.expandedTreeItemProperty().set(expandedTreeItem);
	}

}
