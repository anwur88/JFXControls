/**
 * 
 */
package org.devel.jfxcontrols.sample;

import java.util.ArrayList;
import java.util.HashMap;

import javafx.application.Application;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableRow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import org.devel.jfxcontrols.conf.Properties;
import org.devel.jfxcontrols.resource.ImageRegistry;
import org.devel.jfxcontrols.scene.control.treetableview.ExpandableCell;
import org.devel.jfxcontrols.scene.control.treetableview.command.Adjustable;
import org.devel.jfxcontrols.scene.control.treetableview.command.EventMapper;
import org.devel.jfxcontrols.scene.control.treetableview.command.Expand;
import org.devel.jfxcontrols.scene.control.treetableview.command.RowAdjust;
import org.devel.jfxcontrols.scene.control.treetableview.command.RowAdjust.Action;
import org.devel.jfxcontrols.scene.control.treetableview.skin.TreeTableViewSkin;

/**
 * @author stefan.illgen
 *
 */
public class Client1 extends Application {

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
			ExpandableCell eCell = new ExpandableCell();
			eCell.setCommandFactory((expandable) -> {
				return new Expand<>(expandable);
			});
			return eCell;
		});

		// column
		TreeTableColumn<String, String> secondCol = new TreeTableColumn<String, String>();
		secondCol.setCellValueFactory((item) -> (new ReadOnlyObjectWrapper<String>(item.getValue()
																					   .getValue())));
		secondCol.setCellFactory((column) -> {
			return new ExpandableCell();
		});

		org.devel.jfxcontrols.scene.control.treetableview.TreeTableView<String, Adjustable<String, TreeTableRow<String>>> ttv = new org.devel.jfxcontrols.scene.control.treetableview.TreeTableView<String, Adjustable<String, TreeTableRow<String>>>();
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

		// skin
		ttv.setSkin(new TreeTableViewSkin<String>(ttv));

		ttv.setCommandFactory((adjustable) -> {
			RowAdjust<String, TreeTableRow<String>> rowAdjust = new RowAdjust<String, TreeTableRow<String>>(adjustable);
			EventMapper<RowAdjust<String, TreeTableRow<String>>, Action, Adjustable<String, TreeTableRow<String>>> mapper = new EventMapper<>(ttv,
																																			  rowAdjust);
			mapper.addMouseFilters(new HashMap<EventType<MouseEvent>, RowAdjust.Action>() {
				private static final long serialVersionUID = -9005856313771120088L;
				{
					put(MouseEvent.MOUSE_PRESSED, RowAdjust.Action.PRESS.animate(true));
					put(MouseEvent.MOUSE_DRAGGED, RowAdjust.Action.DRAG);
					put(MouseEvent.MOUSE_RELEASED, RowAdjust.Action.RELEASE.animate(true));
					put(MouseEvent.MOUSE_CLICKED, RowAdjust.Action.CONSUME);
					put(MouseEvent.MOUSE_MOVED, RowAdjust.Action.CONSUME);
					put(MouseEvent.MOUSE_ENTERED, RowAdjust.Action.CONSUME);
					put(MouseEvent.MOUSE_EXITED, RowAdjust.Action.CONSUME);
					put(MouseEvent.MOUSE_ENTERED_TARGET, RowAdjust.Action.CONSUME);
					put(MouseEvent.MOUSE_EXITED_TARGET, RowAdjust.Action.CONSUME);
				}
			});
			return rowAdjust;
		});

		// tree items
		TreeItem<String> treeRootItem = new TreeItem<String>("root");
		treeRootItem.setExpanded(false);

		for (int i = 0; i < 100; i++) {
			TreeItem<String> childTreeItem = new TreeItem<String>("Child: " + i);
			for (int j = 0; j < 20; j++) {
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
