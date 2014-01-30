/**
 * 
 */
package org.devel.jfxcontrols.scene.control;

import java.io.File;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;

import javax.swing.JOptionPane;

import org.devel.jfxcontrols.concurrent.LoadImageTask;
import org.devel.jfxcontrols.concurrent.SaveImageTask;
import org.devel.jfxcontrols.scene.layout.ImageCropperGridPane;

/**
 * TODO stefan - separate concerns > build sub controls
 * 
 * @author stefan.illgen
 * 
 */
public class ImageCropperGrid extends Control {

	private static final String TXT_save_target_override_title = "Warning";
	private static final String TXT_save_target_override_question = "Die Datei existiert bereits. Überschreiben?";
	private static final String TXT_choose_source_label = "Quelle";
	private static final String TXT_choose_target_label = "Ziel";

	/**
	 * the required width for the target picture.
	 */
	public static final int DEFAULT_WIDTH = 150;
	/**
	 * the required height for the target picture.
	 */
	public static final int DEFAULT_HEIGHT = 200;

	// size properties for the target image to be set via CSS
	private IntegerProperty targetWidth;
	private IntegerProperty targetHeight;

	// necessary due to restriction for getting the path to the image
	private StringProperty targetPath;

	// was previously outText
	private StringProperty validationField;

	// factor 4 translating source cropper rectangle size 2 target view port
	private DoubleProperty resizeFactor;

	// ############ Controller API ##############

	@FXML
	private GridPane imageCropperView;

	@FXML
	private Button saveImageButton;

	@FXML
	private StackPane imageStackPane;

	@FXML
	private ImageView sourceImageView;

	@FXML
	private Rectangle cropperRectangle;

	@FXML
	private ImageView targetImageView;

	@FXML
	private ScrollPane sourceScrollPane;

	private double referencePointX;
	private double referencePointY;

	// the image cropper pane used for loading FXML
	private ImageCropperGridPane imageCropperPane;

	/**
	 * @param event
	 *            {@link MouseEvent} which needs to be set the clicked
	 *            coordinates
	 */
	@FXML
	void setReferencePoint(final MouseEvent event) {
		referencePointX = event.getX();
		referencePointY = event.getY();
	}

	/**
	 * 
	 * @param event
	 *            {@link MouseEvent} with the new coordinates for the
	 *            {@link Rectangle}
	 */
	@FXML
	void moveRectangle(final MouseEvent event) {

		// ### X axis ###
		// offset inside cropper rectangle
		double offsetX = referencePointX - cropperRectangle.getX();
		// distance to translate the rectangle to
		double translateX = cropperRectangle.getTranslateX() + event.getX()
				- offsetX;
		// new x position of the left upper corner for the cropper rectangle
		double newXLeft = cropperRectangle.getX() + translateX;
		// new x position of the downer edge for the cropper rectangle
		double newXRight = newXLeft + cropperRectangle.getWidth();
		if (newXLeft >= 0 && newXRight <= sourceImageView.getFitWidth())
			cropperRectangle.setTranslateX(translateX);

		// ### Y axis ###
		// offset inside cropper rectangle
		double offsetY = referencePointY - cropperRectangle.getY();
		// distance to translate the rectangle to
		double translateY = cropperRectangle.getTranslateY() + event.getY()
				- offsetY;
		// new y position of the upper edge for the cropper rectangle
		double newYUp = cropperRectangle.getY() + translateY;
		// new y position of the downer edge for the cropper rectangle
		double newYDown = newYUp + cropperRectangle.getHeight();
		if (newYUp >= 0 && newYDown <= sourceImageView.getFitHeight())
			cropperRectangle.setTranslateY(translateY);

	}

	/**
	 * method calls an dialog to choose an image from the drive and sets them to
	 * the ImageCropperModel.
	 * 
	 * @param event
	 *            is unused in this method
	 */
	@FXML
	void loadImage(final ActionEvent event) {

		// unload old image
		setSourceImage(null);

		// choose the name
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle(TXT_choose_source_label);
		fileChooser.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("All", "*.jpg", "*.jpeg",
						"*.png"),
				new FileChooser.ExtensionFilter("png", "*.png"),
				new FileChooser.ExtensionFilter("jpg", "*.jpg", "*.jpeg"));

		File imageFile = fileChooser.showOpenDialog(getScene().getWindow());

		// load source image
		if (imageFile != null) {
			LoadImageTask task = new LoadImageTask(imageFile);

			task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
				@Override
				public void handle(WorkerStateEvent event) {
					// if load image was successful
					if (task.getValue()) {
						setSourceImage(task.getImage());
					}
				}
			});
			new Thread(task).start();
		}

	}

	/**
	 * Saves the cropped image under the filename in the imageNameTextField.
	 * 
	 * @param event
	 *            is unused in this method
	 */
	@FXML
	void saveImage(final ActionEvent event) {

		// open file chooser dialog
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle(TXT_choose_target_label);
		fileChooser.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("All", "*.jpg", "*.jpeg",
						"*.png"),
				new FileChooser.ExtensionFilter("png", "*.png"),
				new FileChooser.ExtensionFilter("jpg", "*.jpg", "*.jpeg"));
		File imageFile = fileChooser.showSaveDialog(getScene().getWindow());

		// TODO stefan - remove swing dialog
		// if file exists, show override request
		if (imageFile != null
				&& imageFile.exists()
				&& JOptionPane.showConfirmDialog(null,
						TXT_save_target_override_question,
						TXT_save_target_override_title,
						JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {

			// save target image
			new Thread(new SaveImageTask(imageFile, getTargetImage())).start();
		}
	}

	// ######## Layout API ############

	/**
	 * 
	 */
	public ImageCropperGrid() {
		setupSkin();
		// add ImageCropperPane
		imageCropperPane = new ImageCropperGridPane(this);
		getChildren().add(imageCropperPane);
		initialize();
	}

	private void initialize() {
		bindSource2TargetImageView();
		bindSourceImageSize();
		bindSourceScrollMaxBounds();
	}

	private void bindSource2TargetImageView() {

		// bind image properties
		targetImageProperty().bind(sourceImageProperty());

		// bind view port of target image view
		targetImageView.viewportProperty().bind(
				new ObjectBinding<Rectangle2D>() {
					{
						super.bind(cropperRectangle.widthProperty(),
								cropperRectangle.translateXProperty(),
								cropperRectangle.translateYProperty());
					}

					@Override
					protected Rectangle2D computeValue() {

						double minX = cropperRectangle.translateXProperty()
								.add(cropperRectangle.layoutXProperty()).get();
						double minY = cropperRectangle.translateYProperty()
								.add(cropperRectangle.layoutYProperty()).get();
						double width = cropperRectangle.getWidth();
						double height = cropperRectangle.getHeight();

						return new Rectangle2D(minX, minY, width, height);

					}
				});
	}

	/*
	 * Source Image Size bindings.
	 */
	private void bindSourceImageSize() {

		// calculate fit height in relation to source image size and source
		// scroll pane size
		sourceImageView.fitHeightProperty().bind(new DoubleBinding() {
			{
				super.bind(sourceImageProperty());
			}

			@Override
			protected double computeValue() {
				if (getSourceImage() == null)
					return 0;				
				return getSourceImage().getHeight();
			}
		});

		// calculate fit width in relation to source image size and source
		// scroll pane size
		sourceImageView.fitWidthProperty().bind(new DoubleBinding() {
			{
				super.bind(sourceImageProperty());
			}

			@Override
			protected double computeValue() {
				if (getSourceImage() == null)
					return 0;				
				return getSourceImage().getWidth();
			}
		});
	}

	private void bindSourceScrollMaxBounds() {

		sourceScrollPane.maxWidthProperty().bind(new DoubleBinding() {
			{
				super.bind(sourceImageProperty(),
						cropperRectangle.widthProperty());
			}

			@Override
			protected double computeValue() {
				if (getSourceImage() != null) {
					double siWidth = getSourceImage().getWidth();
					double cropperWidth = cropperRectangle.getWidth();
					if (siWidth >= cropperWidth)
						return siWidth + calculateVerticalDimDiff();
					else
						return cropperWidth + calculateVerticalDimDiff();
				}
				// else use computed value
				return -1;
			}

			private double calculateVerticalDimDiff() {
				return sourceScrollPane.getWidth()
						- sourceScrollPane.getViewportBounds().getWidth();
			}
		});

		sourceScrollPane.maxHeightProperty().bind(new DoubleBinding() {
			{
				super.bind(sourceImageProperty(),
						cropperRectangle.heightProperty());
			}

			@Override
			protected double computeValue() {
				if (getSourceImage() != null) {
					double siHeight = getSourceImage().getHeight();
					double cropperHeight = cropperRectangle.getHeight();
					if (siHeight >= cropperHeight)
						return siHeight + calculateHorizontalDimDiff();
					else
						return cropperHeight + calculateHorizontalDimDiff();
				}
				// else use computed value
				return -1;
			}

			private double calculateHorizontalDimDiff() {
				return sourceScrollPane.getBoundsInParent().getHeight()
						- sourceScrollPane.getViewportBounds().getHeight();
			}
		});
	}

	public IntegerProperty targetWidthProperty() {
		if (targetWidth == null)
			targetWidth = new SimpleIntegerProperty(DEFAULT_WIDTH);
		return targetWidth;
	}

	public int getTargetWidth() {
		return targetWidth.get();
	}

	public void setTargetWidth(int targetWidth) {
		this.targetWidth.set(targetWidth);
	}

	public IntegerProperty targetHeightProperty() {
		if (targetHeight == null)
			targetHeight = new SimpleIntegerProperty(DEFAULT_HEIGHT);
		return targetHeight;
	}

	public int getTargetHeight() {
		return targetHeight.get();
	}

	public void setTargetHeight(int targetHeight) {
		this.targetHeight.set(targetHeight);
	}

	public String getTargetPath() {
		return targetPath.get();
	}

	public StringProperty targetPathProperty() {
		if (targetPath == null)
			targetPath = new SimpleStringProperty();
		return targetPath;
	}

	public void setTargetPath(String targetPath) {
		this.targetPath.set(targetPath);
	}

	public ObjectProperty<Image> sourceImageProperty() {
		return sourceImageView.imageProperty();
	}

	public Image getSourceImage() {
		return sourceImageView.imageProperty().get();
	}

	public void setSourceImage(Image sourceImage) {
		sourceImageView.imageProperty().set(sourceImage);
	}

	public ObjectProperty<Image> targetImageProperty() {
		return targetImageView.imageProperty();
	}

	public Image getTargetImage() {
		return targetImageView.imageProperty().get();
	}

	public void setTargetImage(Image targetImage) {
		targetImageView.imageProperty().set(targetImage);
	}

	public StringProperty getValidationFieldProperty() {
		return validationField;
	}

	public StringProperty getValidationField() {
		return validationField;
	}

	public void setValidationField(String validationField) {
		this.validationField.set(validationField);
	}

	/**
	 * Every source image has its resize factor.
	 * 
	 * @return
	 */
	public DoubleProperty resizeFactorProperty() {
		if (resizeFactor == null)
			resizeFactor = new SimpleDoubleProperty(0.2754820936639118);
		return resizeFactor;
	}

	public double getResizeFactor() {
		return resizeFactor.get();
	}

	public void setResizeFactor(double resizeFactor) {
		this.resizeFactor.set(resizeFactor);
	}

	// ### private API ###

	private void setupSkin() {
		getStyleClass().add("image-cropper-grid");
	}

	/**
	 * Integrate skin.
	 */
	@Override
	protected String getUserAgentStylesheet() {
		return getClass().getResource("image-cropper.css").toExternalForm();
	}

}
