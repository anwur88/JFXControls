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
import javafx.scene.control.TreeTableView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import org.devel.jfxcontrols.conf.Properties;
import org.devel.jfxcontrols.resource.ImageRegistry;
import org.devel.jfxcontrols.scene.control.TTTVCell;
import org.devel.jfxcontrols.scene.control.skin.FlingableTreeTableViewSkin;

/**
 * @author stefan.illgen
 *
 */
public class Main extends Application {

	/**
	 * 
	 */
	public Main() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void start(Stage stage) throws Exception {
		setup(stage);

		Scene scene = new Scene(createNodes());
		// scene.getStylesheets().add(getClass().getResource(".").toExternalForm()
		// + getClass().getSimpleName() + ".css");
		stage.setScene(scene);
		stage.setTitle(getClass().getName());

		stage.show();

		stage.setMinWidth(600);
		stage.setMinHeight(300);
	}

	private Parent createNodes() {

		// column
		TreeTableColumn<String, String> firstCol = new TreeTableColumn<String, String>();
		firstCol.setMinWidth(150);
		firstCol.setPrefWidth(150);
		firstCol.setMaxWidth(150);
		firstCol.setCellValueFactory((item) -> (new ReadOnlyObjectWrapper<String>(item.getValue()
																					  .getValue())));
		firstCol.setCellFactory((column) -> {
			return new TTTVCell();
		});

		// column
		TreeTableColumn<String, String> secondCol = new TreeTableColumn<String, String>();
		secondCol.setCellValueFactory((item) -> (new ReadOnlyObjectWrapper<String>(item.getValue()
																					   .getValue())));
		secondCol.setCellFactory((column) -> {
			return new TTTVCell();
		});

		// tree table view
		TreeTableView<String> ttv = new TreeTableView<String>();
		secondCol.prefWidthProperty().bind(ttv.widthProperty()
											  .subtract(firstCol.widthProperty()));
		ttv.setFixedCellSize(50.0);
		// ttv.setColumnResizePolicy(TreeTableView.CONSTRAINED_RESIZE_POLICY);
		ttv.setShowRoot(false);
		ttv.getColumns().addAll(new ArrayList<TreeTableColumn<String, String>>() {
			private static final long serialVersionUID = 2504499281867308788L;
			{
				add(firstCol);
				add(secondCol);
			}
		});
		ttv.setSkin(new FlingableTreeTableViewSkin<String>(ttv));

		// items
		TreeItem<String> treeRootItem = new TreeItem<String>("root");
		treeRootItem.setExpanded(false);

		for (int i = 0; i < 50; i++) {
			TreeItem<String> childTreeItem = new TreeItem<String>("Child: " + i);
			for (int j = 0; j < 50; j++) {
				childTreeItem.getChildren().add(new TreeItem<String>("Child of Child: "
					+ j));
			}
			treeRootItem.getChildren().add(childTreeItem);
		}

		ttv.setRoot(treeRootItem);

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
