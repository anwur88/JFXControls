/**
 * 
 */
package org.devel.jfxcontrols.scene.control;

import java.util.ArrayList;

import org.devel.jfxcontrols.scene.control.skin.ImageCropperScrollPaneSkin;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;

/**
 * This class represents a scroll pane whose maximum bounds depend on the
 * maximum of certain width and height properties encapsulated via the
 * {@link #maxWidthObservablesProperty()} and the
 * {@link #maxHeightObservablesProperty()}. The calculation for maximum properties takes the
 * maximum value of each list (@see {@link ImageCropperScrollPaneSkin}).
 * 
 * @author stefan.illgen
 * 
 */
public class ImageCropperScrollPane extends ScrollPane {

	private ObservableList<ReadOnlyDoubleProperty> maxWidthObservables;
	private ObservableList<ReadOnlyDoubleProperty> maxHeightObservables;

	public ImageCropperScrollPane(Node content) {
		super(content);
		setupSkin();
	}

	public ImageCropperScrollPane() {
		super();
		setupSkin();
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

	private void setupSkin() {
		getStyleClass().add("image-cropper-scroll-pane");
	}

	@Override
	protected String getUserAgentStylesheet() {
		return getClass().getResource("image-cropper.css").toExternalForm();
	}

}
