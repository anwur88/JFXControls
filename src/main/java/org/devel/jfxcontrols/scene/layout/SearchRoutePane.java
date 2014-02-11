package org.devel.jfxcontrols.scene.layout;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import org.devel.jfxcontrols.scene.control.RouteMapView;

/**
 * 
 * @author stefan.illgen
 * 
 */
public class SearchRoutePane extends AnchorPane implements Initializable {

	/**
	 * 
	 */
	private static final String DEFAULT_START_POSITION = "51.02681 13.70878";

	/**
	 * 
	 */
	private static final String DEFAULT_FINISH_POSITION = "51.05041 13.73726";

	@FXML
	private RouteMapView routeMapView;

	@FXML
	private Label finishLbl;

	@FXML
	private TextField finishTf;

	@FXML
	private Button searchBtn;

	@FXML
	private Label startLbl;

	@FXML
	private TextField startTf;

	/**
	 * 
	 */
	public SearchRoutePane() {
		this(DEFAULT_START_POSITION, DEFAULT_FINISH_POSITION);
	}

	/**
	 * 
	 * @param startPosition
	 * @param finishPosition
	 */
	public SearchRoutePane(String startPosition, String finishPosition) {
		loadFXML();
		setupDefaultPosition(startPosition, finishPosition);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		searchBtn.setOnMouseReleased(e -> {
			routeMapView.computeRoute();
		});
		
		routeMapView.setupThreadPool();
		routeMapView.loadEngine();
		
		routeMapView.startPositionProperty().bindBidirectional(
				startTf.textProperty());
		routeMapView.finishPositionProperty().bindBidirectional(
				finishTf.textProperty());
	}

	private void setupDefaultPosition(String startPosition,
			String finishPosition) {
		startTf.setText(startPosition);
		finishTf.setText(finishPosition);
	}

	private void loadFXML() {

		URL url = getClass().getResource(getClass().getSimpleName() + ".fxml");
		FXMLLoader fxmlLoader = new FXMLLoader(url);

		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);

		try {
			fxmlLoader.load();
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}

	}

}
