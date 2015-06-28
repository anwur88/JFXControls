/**
 * 
 */
package org.devel.jfxcontrols.sample;

import java.util.ArrayList;

import javafx.application.Application;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableRow;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import org.devel.jfxcontrols.conf.Properties;
import org.devel.jfxcontrols.resource.ImageRegistry;
import org.devel.jfxcontrols.scene.control.treetableview.ExpandableTreeTableCell;
import org.devel.jfxcontrols.scene.control.treetableview.command.Adjustable;
import org.devel.jfxcontrols.scene.control.treetableview.command.Expand;
import org.devel.jfxcontrols.scene.control.treetableview.command.RowAdjust;

/**
 * @author stefan.illgen
 *
 */
public class Client1 extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		setup(stage);

		Scene scene = new Scene(createNodes());
		// scene.setS
		// scene.getStylesheets().add(getClass().getResource(".").toExternalForm()
		// + getClass().getSimpleName() + ".css");
		stage.setScene(scene);
		stage.setTitle(getClass().getName());

		stage.show();

		stage.setMinWidth(800);
		stage.setMinHeight(800);
		stage.setX(800);
		stage.setY(0);
	}

	private Parent createNodes() {

		final RowAdjust<String, TreeTableRow<String>> rowAdjust = new RowAdjust<String, TreeTableRow<String>>();

		org.devel.jfxcontrols.scene.control.treetableview.TreeTableView<String, Adjustable<String, TreeTableRow<String>>> ttv = new org.devel.jfxcontrols.scene.control.treetableview.TreeTableView<String, Adjustable<String, TreeTableRow<String>>>();
		ttv.setFixedCellSize(50.0);
		// ttv.setColumnResizePolicy(TreeTableView.CONSTRAINED_RESIZE_POLICY);
		ttv.setShowRoot(false);
		ttv.setRowAdjust(rowAdjust);
		ttv.setExpand(new Expand<String, TreeTableRow<String>>(rowAdjust));

		// column
		TreeTableColumn<String, String> firstCol = new TreeTableColumn<String, String>();
		firstCol.setMinWidth(137);
		firstCol.setPrefWidth(137);
		firstCol.setMaxWidth(137);
		firstCol.setCellValueFactory((item) -> (new ReadOnlyObjectWrapper<String>(item.getValue()
																					  .getValue())));

		firstCol.setCellFactory((column) -> {
			ExpandableTreeTableCell<String, String> eCell = new ExpandableTreeTableCell<String, String>();
			return eCell;
		});

		// column
		TreeTableColumn<String, String> secondCol = new TreeTableColumn<String, String>();
		secondCol.prefWidthProperty().bind(ttv.widthProperty()
											  .subtract(firstCol.widthProperty()));
		secondCol.setCellValueFactory((item) -> (new ReadOnlyObjectWrapper<String>(item.getValue()
																					   .getValue())));
		secondCol.setCellFactory((column) -> {
			ExpandableTreeTableCell<String, String> eCell = new ExpandableTreeTableCell<String, String>();
			return eCell;
		});

		ttv.getColumns().addAll(new ArrayList<TreeTableColumn<String, String>>() {
			private static final long serialVersionUID = 2504499281867308788L;
			{
				add(firstCol);
				add(secondCol);
			}
		});

		// skin
		ttv.setStyle("-fx-skin: \"org.devel.jfxcontrols.scene.control.treetableview.skin.TreeTableViewSkin\";");
		// ttv.setSkin(new TreeTableViewSkin<String>(ttv));

		// tree items
		TreeItem<String> treeRootItem = new TreeItem<String>("root");
		treeRootItem.setExpanded(false);

		for (int i = 0; i < 100; i++) {
			TreeItem<String> childTreeItem = new TreeItem<String>("Child: " + i);
			for (int j = 0; j < 5; j++) {
				childTreeItem.getChildren().add(new TreeItem<String>("Child of Child: "
					+ j));
			}
			treeRootItem.getChildren().add(childTreeItem);
		}

		ttv.setRoot(treeRootItem);

		// extended animation commands

		// layout container
		BorderPane root = new BorderPane();
		root.setCenter(ttv);

		return root;
	}

	private void setup(Stage stage) {

		// conf
		Properties.init();

		// image registry
		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				if (event.getEventType().equals(WindowEvent.WINDOW_CLOSE_REQUEST)
					&& event.getTarget().equals(stage)) {
					stage.removeEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, this);
					ImageRegistry.instance().dispose();
				}

			}
		});
	}

	public static void main(String[] args) {
		launch(args);
	}

}
