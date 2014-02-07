/**
 * 
 */
package org.devel.jfxcontrols.scene.control.skin;

import java.io.File;

import javafx.beans.binding.ObjectBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.SkinBase;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;

import org.devel.jfxcontrols.concurrent.CropWriteImageTask;
import org.devel.jfxcontrols.concurrent.LoadImageTask;
import org.devel.jfxcontrols.scene.control.Aggregator;
import org.devel.jfxcontrols.scene.control.ImageCropper;
import org.devel.jfxcontrols.scene.control.ImageCropperScrollPane;
import org.devel.jfxcontrols.scene.image.SourceImageView;
import org.devel.jfxcontrols.scene.layout.ImageCropperGridPane;
import org.devel.jfxcontrols.scene.shape.CropperRectangle;

/**
 * @author stefan.illgen
 * 
 */
public class ImageCropperSkin extends SkinBase<ImageCropper> {

	public ImageCropperSkin(ImageCropper control) {
		super(control);
		createNodes();
		getSkinnable().requestLayout();
	}

	/**
	 * TODO stefan - Check if superfluous!
	 * 
	 * @return
	 */
	public ImageCropper getControl() {
		return getSkinnable();
	}
	
	private GridPane imageCropperGridPane;
	private Button loadImageButton;
	

	private void createNodes() {
		
		imageCropperGridPane = new GridPane();
		imageCropperGridPane.setHgap(10.0);
		imageCropperGridPane.setVgap(10.0);
		imageCropperGridPane.setMinHeight(250.0);
		imageCropperGridPane.setMinWidth(360.0);
		imageCropperGridPane.setStyle("-fx-background-color: white;");
		this.getChildren().addAll(imageCropperGridPane);
		
//		imageCropperView.getColumnConstraints().addAll(
//				new ColumnConstraints(Region.USE_COMPUTED_SIZE,
//						Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE,
//						Priority.ALWAYS, HPos.CENTER, true),
//				new ColumnConstraints(Region.USE_COMPUTED_SIZE,
//						Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE,
//						Priority.NEVER, HPos.CENTER, true));
		imageCropperGridPane.setPadding(new Insets(10.0, 10.0, 10.0, 10.0));
		
		loadImageButton = new Button("Load Image");
		loadImageButton.setAlignment(Pos.CENTER);
		loadImageButton.setMnemonicParsing(false);
		imageCropperGridPane.add(loadImageButton, 0, 0);

		

	}

	@Override
	protected void layoutChildren(double contentX, double contentY,
			double contentWidth, double contentHeight) {
		
		// this is only for further children added to this control
//		super.layoutChildren(contentX, contentY, contentWidth, contentHeight);
		
		layoutInArea(imageCropperGridPane, 0, 0, contentWidth, contentHeight, Control.USE_PREF_SIZE, HPos.LEFT, VPos.TOP);
	}

	@Override
	protected double computeMinWidth(double height, double topInset,
			double rightInset, double bottomInset, double leftInset) {
		
		// this is only for further children added to this control
//		return super.computeMinWidth(height, topInset, rightInset, bottomInset,
//				leftInset);
		return leftInset + imageCropperGridPane.minWidth(height) + rightInset;
		
	}

	@Override
	protected double computeMinHeight(double width, double topInset,
			double rightInset, double bottomInset, double leftInset) {
		
		// this is only for further children added to this control
//		return super.computeMinHeight(width, topInset, rightInset, bottomInset,
//				leftInset);
		return topInset + imageCropperGridPane.minHeight(width) + bottomInset;
		
	}

	@Override
	protected double computePrefWidth(double height, double topInset,
			double rightInset, double bottomInset, double leftInset) {
		
		// this is only for further children added to this control
//		return super.computePrefWidth(height, topInset, rightInset, bottomInset,
//				leftInset);		
		return leftInset + imageCropperGridPane.prefWidth(height) + rightInset;
	}

	@Override
	protected double computePrefHeight(double width, double topInset,
			double rightInset, double bottomInset, double leftInset) {
		
		// this is only for further children added to this control
//		return super.computePrefHeight(width, topInset, rightInset, bottomInset,
//				leftInset);		
		return topInset + imageCropperGridPane.prefHeight(width) + bottomInset;
	}

	@Override
	protected double computeMaxWidth(double height, double topInset,
			double rightInset, double bottomInset, double leftInset) {
		return leftInset + imageCropperGridPane.maxWidth(height)+ rightInset;
	}

	@Override
	protected double computeMaxHeight(double width, double topInset,
			double rightInset, double bottomInset, double leftInset) {
		return topInset + imageCropperGridPane.maxHeight(width) + bottomInset;
	}
	
	
	
}
