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
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ZoomEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;

import javax.swing.JOptionPane;

import org.devel.jfxcontrols.concurrent.LoadImageTask;
import org.devel.jfxcontrols.concurrent.SaveImageTask;
import org.devel.jfxcontrols.scene.layout.ImageCropperPane;

/**
 * @author stefan.illgen
 * 
 */
public class ImageCropper extends Control {

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

	private StringProperty outputText;

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
	private Label outputTextLabel;

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
	private ScrollPane originalPicturePane;

	@FXML
	private Slider rectangleSlider;

	private double referencePointX;
	private double referencePointY;

	// the image cropper pane used for loading FXML
	private ImageCropperPane imageCropperPane;

	// @FXML
	// private TextField imageNameTextField;

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

	@FXML
	void onZoom(final ZoomEvent event) {
		if (event.getZoomFactor() > 1) {
			rectangleSlider.setValue(rectangleSlider.getValue() * 1.01);
		} else {
			rectangleSlider.setValue(rectangleSlider.getValue() * 0.99);
		}
	}

	// TODO stefan - What for?
	@FXML
	void removeFilter(final MouseEvent event) {
		// originalPicturePane.removeEventFilter(MouseEvent.ANY, draggKiller);
	}

	/**
	 * 
	 * @param event
	 *            {@link MouseEvent} with the new coordinates for the
	 *            {@link Rectangle}
	 */
	@FXML
	void moveRectangle(final MouseEvent event) {

		/**
		 * rectangle is moving to the left edge
		 */
		if (cropperRectangle.getTranslateX() - (referencePointX - event.getX())
				+ cropperRectangle.getLayoutX() < 0) {

			cropperRectangle
					.setTranslateX(cropperRectangle.getLayoutX() * (-1));

			/**
			 * move reference point, maximum is the left edge of the rectangle
			 */
			if (event.getX() > 0) {
				referencePointX = event.getX();
			}

			/**
			 * rectangle is moving to the right edge
			 */
		} else if (cropperRectangle.getTranslateX()
				- (referencePointX - event.getX())
				+ cropperRectangle.getLayoutX() > (sourceImageView
				.getFitWidth() - cropperRectangle.getWidth())) {
			cropperRectangle
					.setTranslateX((sourceImageView.getFitWidth() - cropperRectangle
							.getWidth()) / 2);

			/**
			 * move reference point, maximum is the right edge of the rectangle
			 */
			if (event.getX() < cropperRectangle.getWidth()) {
				referencePointX = event.getX();
			}

			/**
			 * rectangle is moving to the upper edge
			 */
		} else {
			cropperRectangle.setTranslateX(cropperRectangle.getTranslateX()
					- (referencePointX - event.getX()));
		}

		if (cropperRectangle.getTranslateY() - (referencePointY - event.getY())
				+ cropperRectangle.getLayoutY() < 0) {
			cropperRectangle
					.setTranslateY(cropperRectangle.getLayoutY() * (-1));

			/**
			 * move reference point, maximum is the upper edge of the rectangle
			 */
			if (event.getY() > 0) {
				referencePointY = event.getY();
			}

			/**
			 * rectangle is moving to the lower edge
			 */
		} else if (cropperRectangle.getTranslateY()
				- (referencePointY - event.getY())
				+ cropperRectangle.getLayoutY() > (sourceImageView
				.getFitHeight() - cropperRectangle.getHeight())) {

			/**
			 * move reference point, maximum is the lower edge of the rectangle
			 */
			if (event.getY() < cropperRectangle.getHeight()) {
				referencePointY = event.getY();
			}

		} else {
			cropperRectangle.setTranslateY(cropperRectangle.getTranslateY()
					- (referencePointY - event.getY()));
		}

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
			LoadImageTask task = new LoadImageTask(imageFile,
					outputTextLabel.textProperty());

			task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
				@Override
				public void handle(WorkerStateEvent event) {
					// if load image was successful
					if (task.getValue()) {
						setSourceImage(task.getImage());
						lazySliderBinding();
					}
				}
			});
			new Thread(task).start();
		}

	}

	/**
	 * {@link Slider} min and max value needs to be binded after image is set,
	 * otherwise the {@link Slider} thumb is invisible until the mouse moves
	 * over the thumb or the slider was clicked.
	 */
	private void lazySliderBinding() {

		if (rectangleSlider.isDisabled()) {
			rectangleSlider.setDisable(false);
			// imageNameTextField.setDisable(false);
			rectangleSlider.minProperty().bindBidirectional(
					resizeFactorProperty());
			// TODO stefan - What 4? Stays fix!
//			rectangleSlider.maxProperty().bindBidirectional(
//					rectangleSlider.maxProperty());
			
			cropperRectangle.setWidth(cropperRectangle.getWidth()+1);
			cropperRectangle.setWidth(cropperRectangle.getWidth()-1);
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
		File imageFile = fileChooser.showOpenDialog(getScene().getWindow());

		// TODO stefan - remove swing dialog
		// if file exists, show override request
		if (imageFile != null
				&& imageFile.exists()
				&& JOptionPane.showConfirmDialog(null,
						TXT_save_target_override_question,
						TXT_save_target_override_title,
						JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {

			// save target image
			new Thread(new SaveImageTask(imageFile, getTargetImage(),
					outputTextLabel.textProperty())).start();
		}
	}

	// ######## Layout API ############

	/**
	 * 
	 */
	public ImageCropper() {
		setupSkin();
		// add ImageCropperPane
		imageCropperPane = new ImageCropperPane(this);
		getChildren().add(imageCropperPane);
		initialize();
	}

	private void initialize() {
		rectangleSlider.setDisable(true);
		// imageNameTextField.setDisable(true);

		// TODO stefan - TargetImage ViewPort
		bindSource2TargetImageView();

		bindSourceImageSize();

		bindRectangleResizes();

		// TODO stefan - rectangle slider bindings

		// rectangleSlider.valueProperty().addListener(new
		// ChangeListener<Number>() {
		//
		// @Override
		// public void changed(final ObservableValue<? extends Number> arg0,
		// final Number arg1,
		// final Number arg2) {
		// adjust();
		//
		// }
		// });
		//
		// cropperRectangle.widthProperty().bind(new DoubleBinding() {
		// {
		// super.bind(rectangleSlider.valueProperty(),
		// imageView.imageProperty());
		// }
		//
		// @Override
		// protected double computeValue() {
		// return rectangleSlider.valueProperty().get() *
		// (ImageCropperModel.WIDTH);
		// }
		// });
		//
		// cropperRectangle.heightProperty().bind(new DoubleBinding() {
		//
		// {
		// super.bind(rectangleSlider.valueProperty(),
		// imageView.imageProperty());
		// }
		//
		// @Override
		// protected double computeValue() {
		//
		// return rectangleSlider.valueProperty().get() *
		// (ImageCropperModel.HEIGHT);
		// }
		// });
		//
		// cropperRectangle.widthProperty().addListener(new
		// ChangeListener<Number>() {
		//
		// @Override
		// public void changed(final ObservableValue<? extends Number> arg0,
		// final Number arg1,
		// final Number arg2) {
		// adjust();
		// }
		//
		// });
		//
		// cropperRectangle.layoutXProperty().addListener(new
		// ChangeListener<Number>() {
		//
		// @Override
		// public void changed(final ObservableValue<? extends Number> arg0,
		// final Number arg1,
		// final Number arg2) {
		// adjust();
		// }
		// });
		//
		// cropperRectangle.layoutYProperty().addListener(new
		// ChangeListener<Number>() {
		//
		// @Override
		// public void changed(final ObservableValue<? extends Number> arg0,
		// final Number arg1,
		// final Number arg2) {
		// adjust();
		// }
		// });

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

						double min = rectangleSlider.getMin();
						
						double convertBack = 1 / rectangleSlider.getMin();

						double minX = cropperTransLayoutXProperty()
								* convertBack;
						double minY = cropperTransLayoutYProperty()
								* convertBack;
						double width = cropperRectangle.getWidth()
								* convertBack;
						double height = cropperRectangle.getHeight()
								* convertBack;

						return new Rectangle2D(minX, minY, width, height);

					}

					private double cropperTransLayoutYProperty() {
						return cropperRectangle.translateYProperty()
								.add(cropperRectangle.layoutYProperty()).get();
					}

					private double cropperTransLayoutXProperty() {
						return cropperRectangle.translateXProperty()
								.add(cropperRectangle.layoutXProperty()).get();
					}
				});

	}

	/**
     * 
     */
	private void bindRectangleResizes() {

		// TODO stefan - ViewPort Error: Bind this initially leads to NullPointerException
		/*
		 * Calculate the resizeFactor for the originalImage to fit them into the
		 * imageView the resizeFactor is also needed to recalculate the cropped
		 * area of the image.
		 */
		resizeFactorProperty().bind(new DoubleBinding() {

			{
				super.bind(sourceImageProperty());
			}

			@Override
			protected double computeValue() {
				if (getSourceImage() == null)
					return 0;
				// the resizeFactor is dependent on whether the width or the
				// height needs to be increased
				if (getSourceImage().getWidth() / 3 > getSourceImage()
						.getHeight() / 4) {
					return DEFAULT_HEIGHT / getSourceImage().getHeight();
				} else {
					return DEFAULT_WIDTH / getSourceImage().getWidth();
				}
			}
		});
	}

	/*
	 * Source Image Size bindings.
	 */
	private void bindSourceImageSize() {

		sourceImageView.fitHeightProperty().bind(new DoubleBinding() {
			{
				super.bind(sourceImageProperty());
			}

			@Override
			protected double computeValue() {
				// TODO stefan - make this a strategy ???
				if (getSourceImage() == null)
					return 0;
				// increase height of the imageView if needed
				// TODO stefan - dynamic size > remove fix values
				if (getSourceImage().getWidth() / 3 < getSourceImage()
						.getHeight() / 4) {
					return getSourceImage().getHeight()
							* (DEFAULT_WIDTH / getSourceImage().getWidth());
				} else {
					return DEFAULT_HEIGHT;
				}
			}
		});

		sourceImageView.fitWidthProperty().bind(new DoubleBinding() {
			{
				super.bind(sourceImageProperty());
			}

			@Override
			protected double computeValue() {
				// TODO stefan - make this a strategy ???
				if (getSourceImage() == null)
					return 0;
				// increase width of the imageView if needed
				// TODO stefan - dynamic size > remove fix values
				if (getSourceImage().getWidth() / 3 > getSourceImage()
						.getHeight() / 4) {
					return getSourceImage().getWidth()
							* (DEFAULT_HEIGHT / getSourceImage().getHeight());
				} else {
					return DEFAULT_WIDTH;
				}
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

	public StringProperty outputTextProperty() {
		if (outputText == null)
			outputText = new SimpleStringProperty();
		return outputText;
	}

	public String getOutputText() {
		return outputText.get();
	}

	public void setOutputText(String outputText) {
		this.outputText.set(outputText);
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
			resizeFactor = new SimpleDoubleProperty();
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
		getStyleClass().add("image-cropper");
	}

	/**
	 * Integrate skin.
	 */
	@Override
	protected String getUserAgentStylesheet() {
		return getClass().getResource("image-cropper.css").toExternalForm();
	}

}
