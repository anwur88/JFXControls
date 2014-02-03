/**
 * 
 */
package org.devel.jfxcontrols.scene.control;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;

import javax.swing.JOptionPane;

import org.devel.jfxcontrols.concurrent.LoadImageTask;
import org.devel.jfxcontrols.concurrent.SaveImageTask;
import org.devel.jfxcontrols.scene.image.SourceImageView;
import org.devel.jfxcontrols.scene.layout.ImageCropperGridPane;
import org.devel.jfxcontrols.scene.shape.CropperRectangle;

/**
 * TODO stefan - separate concerns > build sub controls
 * 
 * @author stefan.illgen
 * 
 */
public class ImageCropperGrid extends Control implements Initializable {

	private static final String TXT_save_target_override_title = "Warning";
	private static final String TXT_save_target_override_question = "Die Datei existiert bereits. Überschreiben?";
	private static final String TXT_choose_source_label = "Quelle";
	private static final String TXT_choose_target_label = "Ziel";

	// // size properties for the target image to be set via CSS
	// private IntegerProperty targetWidth;
	// private IntegerProperty targetHeight;
	//
	// // necessary due to restriction for getting the path to the image
	// private StringProperty targetPath;
	//
	// // factor 4 translating source cropper rectangle size 2 target view port
	// private DoubleProperty resizeFactor;

	/**
	 * 
	 */
	public ImageCropperGrid() {
		setupSkin();

		// add ImageCropperPane
		imageCropperPane = new ImageCropperGridPane(this);
		getChildren().add(imageCropperPane);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		DoubleProperty fitHeightProperty = sourceImageView.fitHeightProperty();

		// imageCropperScrollPane.maxHeightObservablesProperty().add(cropperRectangle.heightProperty());
		// imageCropperScrollPane.maxHeightObservablesProperty().add(sourceImageView.fitHeightProperty());
		// imageCropperScrollPane.maxWidthObservablesProperty().add(cropperRectangle.widthProperty());
		// imageCropperScrollPane.maxWidthObservablesProperty().add(sourceImageView.fitWidthProperty());

		sourceImageView.initialize(location, resources);
		cropperRectangle.initialize(location, resources);
		imageCropperScrollPane.initialize(location, resources);
		bind();

	}

	// ############ Controller APIs ##############

	// ### ImageCropperGridPane ###

	@FXML
	private GridPane imageCropperView;

	// the image cropper pane used for loading FXML
	private ImageCropperGridPane imageCropperPane;

	@FXML
	private StackPane test;

	@FXML
	private ImageCropperScrollPane imageCropperScrollPane;

	// ### TargetImageView ###

	@FXML
	private SourceImageView sourceImageView;

	@FXML
	private CropperRectangle cropperRectangle;

	@FXML
	private ImageView targetImageView;

	@FXML
	private Button saveImageButton;

	/**
	 * method calls an dialog to choose an image from the drive and sets them to
	 * the ImageCropperModel.
	 * 
	 * @param event
	 *            is unused in this method
	 */
	@FXML
	void loadImage(final ActionEvent event) {

		// imageCropperScrollPane.layout();

		// reset image cropper
		reset();

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
						sourceImageView.setImage(task.getImage());
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

	private void bind() {

		// bind image properties
		targetImageProperty().bind(sourceImageView.imageProperty());

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

		// bind image cropper scroll pane width'n'height

		sourceImageView.imageProperty().addListener(
				new ChangeListener<Image>() {

					@Override
					public void changed(
							ObservableValue<? extends Image> observable,
							Image oldValue, Image newValue) {
						
						if(observable == null)
							sourceImageView.imageProperty().removeListener(this);

						if(newValue != null) {
							// width
							imageCropperScrollPane.maxWidthObservablesProperty().clear();
							imageCropperScrollPane.maxWidthObservablesProperty().add(cropperRectangle.widthProperty());
							imageCropperScrollPane.maxWidthObservablesProperty().add(sourceImageView.fitWidthProperty());
							// height
							imageCropperScrollPane.maxHeightObservablesProperty().clear();
							imageCropperScrollPane.maxHeightObservablesProperty().add(cropperRectangle.heightProperty());
							imageCropperScrollPane.maxHeightObservablesProperty().add(sourceImageView.fitHeightProperty());
						}

					}
				});

		// ### does not work > think that low level bindings aren't allowed for ListBindings ###
//		
//		Bindings.bindContent(
//				imageCropperScrollPane.maxWidthObservablesProperty(),
//				new ListBinding<ReadOnlyDoubleProperty>() {
//					{
//						super.bind(sourceImageView.imageProperty());
//						// ,
//						// cropperRectangle.widthProperty()
//
//					}
//
//					@Override
//					protected ObservableList<ReadOnlyDoubleProperty> computeValue() {
//
//						ObservableList<ReadOnlyDoubleProperty> result = FXCollections
//								.observableArrayList(new ArrayList<ReadOnlyDoubleProperty>() {
//									private static final long serialVersionUID = -1049640758537268871L;
//									{
//										add(cropperRectangle.widthProperty());
//									}
//								});
//
//						if (sourceImageView.imageProperty().get() != null)
//							result.add(sourceImageView.getImage()
//									.widthProperty());
//						return result;
//					}
//				});
//
//		Bindings.bindContent(
//				imageCropperScrollPane.maxHeightObservablesProperty(),
//				new ListBinding<ReadOnlyDoubleProperty>() {
//					{
//						super.bind(sourceImageView.imageProperty());
//						// ,
//						// cropperRectangle.heightProperty()
//					}
//
//					@Override
//					protected ObservableList<ReadOnlyDoubleProperty> computeValue() {
//
//						ObservableList<ReadOnlyDoubleProperty> result = FXCollections
//								.observableArrayList(new ArrayList<ReadOnlyDoubleProperty>() {
//									private static final long serialVersionUID = 7172416090874066799L;
//									{
//										add(cropperRectangle.heightProperty());
//									}
//								});
//
//						if (sourceImageView.imageProperty().get() != null)
//							result.add(sourceImageView.getImage()
//									.heightProperty());
//						return result;
//					}
//				});
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

	private void reset() {
		sourceImageView.reset();
		cropperRectangle.reset();
	}

	// ### some more possible properties ###

	// public IntegerProperty targetWidthProperty() {
	// if (targetWidth == null)
	// targetWidth = new SimpleIntegerProperty(DEFAULT_WIDTH);
	// return targetWidth;
	// }
	//
	// public int getTargetWidth() {
	// return targetWidth.get();
	// }
	//
	// public void setTargetWidth(int targetWidth) {
	// this.targetWidth.set(targetWidth);
	// }
	//
	// public IntegerProperty targetHeightProperty() {
	// if (targetHeight == null)
	// targetHeight = new SimpleIntegerProperty(DEFAULT_HEIGHT);
	// return targetHeight;
	// }
	//
	// public int getTargetHeight() {
	// return targetHeight.get();
	// }
	//
	// public void setTargetHeight(int targetHeight) {
	// this.targetHeight.set(targetHeight);
	// }
	//
	// public String getTargetPath() {
	// return targetPath.get();
	// }
	//
	// public StringProperty targetPathProperty() {
	// if (targetPath == null)
	// targetPath = new SimpleStringProperty();
	// return targetPath;
	// }
	//
	// public void setTargetPath(String targetPath) {
	// this.targetPath.set(targetPath);
	// }

	// /**
	// * Every source image has its resize factor.
	// *
	// * @return
	// */
	// public DoubleProperty resizeFactorProperty() {
	// if (resizeFactor == null)
	// resizeFactor = new SimpleDoubleProperty(0.2754820936639118);
	// return resizeFactor;
	// }
	//
	// public double getResizeFactor() {
	// return resizeFactor.get();
	// }
	//
	// public void setResizeFactor(double resizeFactor) {
	// this.resizeFactor.set(resizeFactor);
	// }

}
