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
import org.devel.jfxcontrols.scene.control.FixedTreeTableView;
import org.devel.jfxcontrols.scene.control.TTTVCell;
import org.devel.jfxcontrols.scene.control.skin.ExtandableTreeTableViewSkin;
import org.devel.jfxcontrols.scene.control.skin.ExtendableFlow;
import org.devel.jfxcontrols.scene.control.skin.FlowAdjuster;
import org.devel.jfxcontrols.scene.control.skin.FlowExtension;
import org.devel.jfxcontrols.scene.control.skin.FlowTreeExpander;

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

		FixedTreeTableView<String> ttv = createTreeTableView();
		createSetSkin(ttv);
		createTreeItems(ttv);

		// layout container
		BorderPane root = new BorderPane();
		root.setCenter(ttv);

		return root;
	}

	public void createTreeItems(FixedTreeTableView<String> ttv) {
		TreeItem<String> treeRootItem = new TreeItem<String>("root");
		treeRootItem.setExpanded(false);

		for (int i = 0; i < 100; i++) {
			TreeItem<String> childTreeItem = new TreeItem<String>("Child: " + i);
			for (int j = 0; j < 20; j++) {
				childTreeItem.getChildren().add(
						new TreeItem<String>("Child of Child: " + j));
			}
			treeRootItem.getChildren().add(childTreeItem);
		}

		ttv.setRoot(treeRootItem);
	}

	public FixedTreeTableView<String> createTreeTableView() {

		// column
		TreeTableColumn<String, String> firstCol = new TreeTableColumn<String, String>();
		firstCol.setMinWidth(150);
		firstCol.setPrefWidth(150);
		firstCol.setMaxWidth(150);
		firstCol.setCellValueFactory((item) -> (new ReadOnlyObjectWrapper<String>(
				item.getValue().getValue())));
		firstCol.setCellFactory((column) -> {
			return new TTTVCell();
		});

		// column
		TreeTableColumn<String, String> secondCol = new TreeTableColumn<String, String>();
		secondCol
				.setCellValueFactory((item) -> (new ReadOnlyObjectWrapper<String>(
						item.getValue().getValue())));
		secondCol.setCellFactory((column) -> {
			return new TTTVCell();
		});

		FixedTreeTableView<String> ttv = new FixedTreeTableView<String>();
		secondCol.prefWidthProperty().bind(
				ttv.widthProperty().subtract(firstCol.widthProperty()));
		ttv.setFixedCellSize(50.0);
		// ttv.setColumnResizePolicy(TreeTableView.CONSTRAINED_RESIZE_POLICY);
		ttv.setShowRoot(false);
		ttv.getColumns().addAll(
				new ArrayList<TreeTableColumn<String, String>>() {
					private static final long serialVersionUID = 2504499281867308788L;
					{
						add(firstCol);
						add(secondCol);
					}
				});
		return ttv;
	}

	public void createSetSkin(FixedTreeTableView<String> ttv) {
		ExtandableTreeTableViewSkin<String> skin = new ExtandableTreeTableViewSkin<String>(
				ttv);
		skin.addExtensions(new ArrayList<FlowExtension<String, TreeTableRow<String>>>() {
			private static final long serialVersionUID = -3874572374641334976L;
			{
				// obligatory entire ro adjustment
				FlowAdjuster<String, TreeTableRow<String>> adjuster = new FlowAdjuster<String, TreeTableRow<String>>(
						skin.getExtendableFlow());
				// adjuster.selectionModelProperty().bind(
				// skin.getExtendableFlow().selectionModelProperty());
				adjuster.fixedCellSizeProperty().bindBidirectional(
						skin.getExtendableFlow().fixedCellLengthProperty());

				// obligatory expansion
				FlowTreeExpander<String, TreeTableRow<String>> flowTreeExpander = new FlowTreeExpander<String, TreeTableRow<String>>(
						skin.getExtendableFlow(), skin, adjuster);
				//
				// flowTreeExpander.singularProperty().bindBidirectional(
				// singularProperty());
				flowTreeExpander.rootProperty().bindBidirectional(
						ttv.rootProperty());
				flowTreeExpander.expandedItemCountProperty().bind(
						ttv.expandedItemCountProperty());
				flowTreeExpander.visibleSizeProperty().bind(
						ttv.fixedCellSizeProperty().multiply(
								ttv.visibleCellCountProperty()));
				//
				// if (isFlingable()) {
				// FlowFlinger<S, TreeTableRow<S>> flowFlinger = new
				// FlowFlinger<S, TreeTableRow<S>>(extensibleFlow,
				// adjuster);
				// flowFlinger.setEventHandling(getSkinnable());
				// }

				adjuster.setEventHandling(ttv);
			}
		});

		ExtendableFlow<String, TreeTableRow<String>> extendableFlow = skin
				.getExtendableFlow();
		extendableFlow.fixedCellLengthProperty().bind(
				ttv.fixedCellSizeProperty()
						.multiply(ttv.visibleCellCountProperty())
						.add(extendableFlow.hBarHeight()));

		ttv.setSkin(skin);
	}

	private void setup(Stage stage) {

		// conf
		Properties.init();

		// image registry
		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				if (event.getEventType().equals(
						WindowEvent.WINDOW_CLOSE_REQUEST)
						&& event.getTarget().equals(stage)) {
					stage.removeEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST,
							this);
					ImageRegistry.instance().dispose();
				}

			}
		});
	}

	public static void main(String[] args) {
		launch(args);
	}

}
