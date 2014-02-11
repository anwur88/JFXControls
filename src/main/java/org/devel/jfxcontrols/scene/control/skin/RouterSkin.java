/**
 * 
 */
package org.devel.jfxcontrols.scene.control.skin;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SkinBase;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;

import org.devel.jfxcontrols.scene.control.RouteMapView;
import org.devel.jfxcontrols.scene.layout.Router;

/**
 * @author stefan.illgen
 *
 */
public class RouterSkin extends SkinBase<Router> {
	
	private static final String DEFAULT_START_POSITION = "51.02681 13.70878";
	private static final String DEFAULT_FINISH_POSITION = "51.05041 13.73726";

	private AnchorPane searchRoutePane;
	private RouteMapView routeMapView;
	private Label finishLbl;
	private TextField finishTf;
	private Button searchBtn;
	private Label startLbl;
	private TextField startTf;

	public RouterSkin(Router control) {
		super(control);
		
		createNodes();
		bind();
		getSkinnable().requestLayout();
	}
	
	private void createNodes() {

		searchRoutePane = new AnchorPane();
		searchRoutePane.setStyle("-fx-background-color: white;");
		getChildren().add(searchRoutePane);

		HBox mapViewHBox = new HBox();
		searchRoutePane.getChildren().add(mapViewHBox);
		AnchorPane.setTopAnchor(mapViewHBox, 0.0);
		AnchorPane.setRightAnchor(mapViewHBox, 0.0);
		AnchorPane.setBottomAnchor(mapViewHBox, 125.0);
		AnchorPane.setLeftAnchor(mapViewHBox, 0.0);

		routeMapView = new RouteMapView();
		routeMapView.setId("routeMapView");
		mapViewHBox.getChildren().add(routeMapView);
		HBox.setHgrow(routeMapView, Priority.ALWAYS);

		VBox vBox = new VBox();
		searchRoutePane.getChildren().add(vBox);
		
		AnchorPane.setRightAnchor(vBox, 0.0);
		AnchorPane.setBottomAnchor(vBox, 0.0);
		AnchorPane.setLeftAnchor(vBox, 0.0);

		GridPane gridPane = new GridPane();
		gridPane.setStyle("-fx-background-color: white;");
		gridPane.setHgap(10.0);
		gridPane.setVgap(10.0);
		vBox.getChildren().add(gridPane);

		gridPane.getColumnConstraints().addAll(
				new ColumnConstraints(20.0, Region.USE_COMPUTED_SIZE,
						Region.USE_COMPUTED_SIZE, Priority.NEVER, HPos.LEFT,
						true),
				new ColumnConstraints(50.0, Region.USE_COMPUTED_SIZE,
						Region.USE_COMPUTED_SIZE, Priority.SOMETIMES,
						HPos.LEFT, true));
		gridPane.setPadding(new Insets(10.0, 10.0, 10.0, 10.0));
		gridPane.getRowConstraints().addAll(
				new RowConstraints(30.0, 30.0, Region.USE_COMPUTED_SIZE,
						Priority.NEVER, VPos.CENTER, false),
				new RowConstraints(30.0, 30.0, Region.USE_COMPUTED_SIZE,
						Priority.NEVER, VPos.CENTER, true),
				new RowConstraints());

		startLbl = new Label("Start (A):");
		startLbl.setId("startLbl");
		GridPane.setConstraints(startLbl, 0, 0);

		finishLbl = new Label("Finish (B):");
		finishLbl.setId("finishLbl");
		GridPane.setConstraints(finishLbl, 0, 1);

		startTf = new TextField();
		startTf.setId("startTf");
		startTf.setPrefWidth(200.0);
		startTf.setText(DEFAULT_START_POSITION);
		GridPane.setConstraints(startTf, 1, 0);

		finishTf = new TextField();
		finishTf.setId("finishTf");
		finishTf.setPrefWidth(200.0);
		finishTf.setText(DEFAULT_FINISH_POSITION);
		GridPane.setConstraints(finishTf, 1, 1);

		searchBtn = new Button("Search");
		searchBtn.setId("searchBtn");
		searchBtn.setMnemonicParsing(false);
		GridPane.setConstraints(searchBtn, 0, 2, 2, 1);

		gridPane.getChildren().addAll(startLbl, finishLbl, startTf, finishTf,
				searchBtn);

	}
	
	private void bind() {
		
		// init
		routeMapView.setupThreadPool();
		routeMapView.loadEngine();
		// bind
		routeMapView.startPositionProperty().bindBidirectional(
				startTf.textProperty());
		routeMapView.finishPositionProperty().bindBidirectional(
				finishTf.textProperty());
		// propagate
		getSkinnable().setRouteMapView(routeMapView);
		
		// expensive bindings
		getSkinnable().startLabelProperty().bindBidirectional(startLbl.textProperty());
		getSkinnable().startTextProperty().bindBidirectional(startTf.textProperty());
		getSkinnable().finishLabelProperty().bindBidirectional(finishLbl.textProperty());
		getSkinnable().finishTextProperty().bindBidirectional(finishTf.textProperty());
		getSkinnable().buttonProperty().bindBidirectional(searchBtn.textProperty());
		
		// lambda example
		searchBtn.setOnMouseReleased(e -> {
			routeMapView.computeRoute();
		});
		
	}

}
