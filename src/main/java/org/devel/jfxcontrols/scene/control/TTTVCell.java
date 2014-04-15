package org.devel.jfxcontrols.scene.control;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TreeTableCell;
import javafx.scene.layout.BorderPane;

public class TTTVCell extends TreeTableCell<String, String> {

	private BorderPane rootContainer;
	private Label label;

	public TTTVCell() {
		super();
		// setOnMouseReleased(new EventHandler<MouseEvent>() {
		// @Override
		// public void handle(MouseEvent event) {
		// // TTTVCell.this.get
		// // expandTreeItem(treeItem, cell, !treeItem.isExpanded());
		// expandTreeItem();
		// }
		// });
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

	// private void expandTreeItem() {
	// // access tree item
	// TreeTableRow<String> row = (TreeTableRow<String>) getParent();
	// TreeItem<String> treeItem = row.getTreeItem();
	// treeItem.setExpanded(!treeItem.isExpanded());
	// }

}
