package org.devel.jfxcontrols.scene.control.treetableview;

import javafx.beans.property.ObjectProperty;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableCell;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;

import org.devel.jfxcontrols.scene.control.treetableview.command.Expand;
import org.devel.jfxcontrols.scene.control.treetableview.command.Expandable;

public class ExpandableCell extends TreeTableCell<String, String>
	implements
		Expandable<String> {

	private BorderPane rootContainer;
	private Label label;
	private Callback<Expandable<String>, Expand<String>> transitionFactory;

	public ExpandableCell() {
		super();
		setGraphic(createNodes());
		setStyle("-fx-border-color: red; -fx-border-width: 1px;");
		requestLayout();
	}

	private Node createNodes() {
		rootContainer = new BorderPane();
		label = new Label("");
		BorderPane.setAlignment(label, Pos.CENTER_LEFT);
		return rootContainer;
	}

	@Override
	protected void updateItem(String item, boolean empty) {
		super.updateItem(item, empty);

		// pre
		if (item == null || empty) {
			return;
		}

		label.setText(item);
		rootContainer.setLeft(label);
	}

	@Override
	public void expand() {
		// TODO Auto-generated method stub

	}

	@Override
	public void collpase() {
		// TODO Auto-generated method stub

	}

	@Override
	public ObjectProperty<TreeItem<String>> expandedItemProperty() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TreeItem<String> getExpandedItem() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setExpandedItem(TreeItem<String> treeItem) {
		// TODO Auto-generated method stub

	}

	public void
		setCommandFactory(Callback<Expandable<String>, Expand<String>> cmdFactory) {
		this.transitionFactory = cmdFactory;
	}

	// private void expandTreeItem() {
	// // access tree item
	// TreeTableRow<String> row = (TreeTableRow<String>) getParent();
	// TreeItem<String> treeItem = row.getTreeItem();
	// treeItem.setExpanded(!treeItem.isExpanded());
	// }

}
