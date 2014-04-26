package org.devel.jfxcontrols.scene.control.treetableview;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TreeTableCell;
import javafx.scene.layout.BorderPane;

/**
 * 
 * @author stefan.illgen
 *
 * @param <S>
 *            type in tree item
 * @param <T>
 */
public class ExpandableTreeTableCell<S, T> extends TreeTableCell<S, T> {

	private BorderPane rootContainer;
	private Label label;

	public ExpandableTreeTableCell() {
		super();
		setGraphic(createNodes());
		setStyle("-fx-border-color: red; -fx-border-width: 1px;");
		bind();
		requestLayout();
	}

	private void bind() {

	}

	private Node createNodes() {
		rootContainer = new BorderPane();
		label = new Label("");
		BorderPane.setAlignment(label, Pos.CENTER_LEFT);
		return rootContainer;
	}

	@Override
	protected void updateItem(T item, boolean empty) {
		super.updateItem(item, empty);

		// pre
		if (item == null || empty) {
			return;
		}

		label.setText(item.toString());
		rootContainer.setLeft(label);
	}

}
