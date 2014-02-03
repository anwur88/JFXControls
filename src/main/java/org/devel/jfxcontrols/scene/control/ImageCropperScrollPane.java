/**
 * 
 */
package org.devel.jfxcontrols.scene.control;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.beans.Observable;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;

/**
 * This class represents a scroll pane whose maximum bounds depend on the
 * maximum of certain width and height properties encapsulated via the
 * {@link #maxWidthObservablesProperty()} and the
 * {@link #maxHeightObservablesProperty()}. When initializing an instance of
 * this class, this' {@link #maxWidthProperty()} and
 * {@link #maxHeightProperty()} gets bounded to the related
 * {@link ObservableList}. The calculation for maximum properties takes the
 * maximum value of each list.
 * 
 * @author stefan.illgen
 * 
 */
public class ImageCropperScrollPane extends ScrollPane implements Initializable {

	private ObservableList<ReadOnlyDoubleProperty> maxWidthObservables;
	private ObservableList<ReadOnlyDoubleProperty> maxHeightObservables;

	public ImageCropperScrollPane(Node content) {
		this();
		setContent(content);
	}

	public ImageCropperScrollPane() {
		super();
		setupSkin();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		bind();
	}

	public ObservableList<ReadOnlyDoubleProperty> maxWidthObservablesProperty() {
		if (maxWidthObservables == null)
			maxWidthObservables = FXCollections
					.observableArrayList(new ArrayList<DoubleProperty>());
		return maxWidthObservables;
	}

	public ObservableList<ReadOnlyDoubleProperty> maxHeightObservablesProperty() {
		if (maxHeightObservables == null)
			maxHeightObservables = FXCollections
					.observableArrayList(new ArrayList<ReadOnlyDoubleProperty>());
		return maxHeightObservables;
	}

	// ### private API ###

	private void bind() {

		this.maxWidthProperty().bind(new DoubleBinding() {
			{
				super.bind(maxWidthObservablesProperty());
			}

			@Override
			protected double computeValue() {
				if (checkObservables(maxWidthObservablesProperty())) {
					return getMaxValue(maxWidthObservablesProperty());
				}
				// else use computed value
				return -1;
			}

		});

		this.maxHeightProperty().bind(new DoubleBinding() {
			{
				super.bind(maxHeightObservablesProperty());
			}

			@Override
			protected double computeValue() {
				if (checkObservables(maxHeightObservablesProperty())) {
					return getMaxValue(maxHeightObservablesProperty());
				}
				// else use computed value
				return -1;
			}
		});

	}

	private boolean checkObservables(Observable... observables) {
		for (int i = 0; i < observables.length; i++)
			// null check
			if (observables[i] == null)
				return false;
		return true;
	}

	private double getMaxValue(
			ObservableList<ReadOnlyDoubleProperty> observableList) {
		double result = 0.0;
		for (ReadOnlyDoubleProperty prop : observableList)
			result = (prop.get() > result) ? prop.get() : result;
		return result;
	}

	private void setupSkin() {
		getStyleClass().add("image-cropper-scroll-pane");
	}

	@Override
	protected String getUserAgentStylesheet() {
		return getClass().getResource("image-cropper.css").toExternalForm();
	}

}
