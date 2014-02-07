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
import org.devel.jfxcontrols.scene.control.ImageCropper;
import org.devel.jfxcontrols.scene.control.ImageCropperScrollPane;
import org.devel.jfxcontrols.scene.image.SourceImageView;
import org.devel.jfxcontrols.scene.shape.CropperRectangle;

/**
 * @author stefan.illgen
 * 
 */
public class ImageCropperSkin extends SkinBase<ImageCropper> {

	// TODO stefan - import from i18n
	private static final String SOURCE_CHOOSE_LABEL = "Choose Source File";
	private static final String TARGET_CHOOSE_LABEL = "Choose Target File";
	private static final String SOURCE_LABEL = "Source";
	private static final String TARGET_LABEL = "Target";
	private static final String TARGET_BUTTON_TEXT = "Save Image";
	private static final String SOURCE_BUTTON_TEXT = "Load Image";
	
	// fixed width constants
	private static final double SOURCE_IMAGE_WIDTH = 200.0;
	private static final double SOURCE_IMAGE_HEIGHT = 266.0;
	private static final double SOURCE_SCROLL_PANE_MIN_WIDTH = 103.0;
	private static final double SOURCE_SCROLL_PANE_MIN_HEIGHT = 137.0;
	private static final double TARGET_IMAGE_WIDTH = 100.0;
	private static final double TARGET_IMAGE_HEIGHT = 133.0;
	private static final double TARGET_HBOX_WIDTH = 122.0;
	private static final double TARGET_HBOX_HEIGHT = 155.0;

	public ImageCropperSkin(ImageCropper control) {
		super(control);
		createNodes();
		initialize();
		getSkinnable().requestLayout();		
	}

	private GridPane imageCropperGridPane;
	private Button loadImageButton;
	private Button saveImageButton;
	private Label sourceLabel;
	private Label targetLabel;
	private ImageCropperScrollPane imageCropperScrollPane;
	private StackPane imageCropperStackPane;
	private SourceImageView sourceImageView;
	private CropperRectangle cropperRectangle;
	private ImageView targetImageView;

	private void createNodes() {

		imageCropperGridPane = new GridPane();
		imageCropperGridPane.setHgap(10.0);
		imageCropperGridPane.setVgap(10.0);
		// imageCropperGridPane.setMinHeight(250.0);
		// imageCropperGridPane.setMinWidth(360.0);
		imageCropperGridPane.setStyle("-fx-background-color: white;");
		this.getChildren().addAll(imageCropperGridPane);

		imageCropperGridPane.getColumnConstraints().addAll(
				new ColumnConstraints(Region.USE_COMPUTED_SIZE,
						Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE,
						Priority.ALWAYS, HPos.CENTER, true),
				new ColumnConstraints(Region.USE_COMPUTED_SIZE,
						Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE,
						Priority.NEVER, HPos.CENTER, true));
		imageCropperGridPane.setPadding(new Insets(10.0, 10.0, 10.0, 10.0));

		// label row

		sourceLabel = new Label(SOURCE_LABEL);
		sourceLabel.setAlignment(Pos.CENTER);
		sourceLabel.setContentDisplay(ContentDisplay.CENTER);
		sourceLabel.setStyle("-fx-font-weight: bold;");
		sourceLabel.setTextAlignment(TextAlignment.CENTER);
		GridPane.setHgrow(sourceLabel, Priority.ALWAYS);
		GridPane.setVgrow(sourceLabel, Priority.ALWAYS);
		imageCropperGridPane.add(sourceLabel, 0, 0);

		targetLabel = new Label(TARGET_LABEL);
		targetLabel.setAlignment(Pos.CENTER);
		targetLabel.setContentDisplay(ContentDisplay.CENTER);
		targetLabel.setStyle("-fx-font-weight: bold;");
		targetLabel.setTextAlignment(TextAlignment.CENTER);
		GridPane.setHgrow(targetLabel, Priority.ALWAYS);
		GridPane.setVgrow(targetLabel, Priority.ALWAYS);
		imageCropperGridPane.add(targetLabel, 1, 0);

		// cropper row

		imageCropperScrollPane = new ImageCropperScrollPane();
		imageCropperScrollPane.setMinHeight(SOURCE_SCROLL_PANE_MIN_HEIGHT);
		imageCropperScrollPane.setMinWidth(SOURCE_SCROLL_PANE_MIN_WIDTH);
		GridPane.setHgrow(imageCropperScrollPane, Priority.ALWAYS);
		GridPane.setVgrow(imageCropperScrollPane, Priority.ALWAYS);
		GridPane.setValignment(imageCropperScrollPane, VPos.CENTER);
		GridPane.setHalignment(imageCropperScrollPane, HPos.CENTER);
		imageCropperGridPane.add(imageCropperScrollPane, 0, 1);

		imageCropperStackPane = new StackPane();
		imageCropperStackPane.setAlignment(Pos.TOP_LEFT);

		sourceImageView = new SourceImageView();
		sourceImageView.setPickOnBounds(true);
		sourceImageView.setPreserveRatio(true);
		sourceImageView.setFitHeight(SOURCE_IMAGE_HEIGHT);
		sourceImageView.setFitWidth(SOURCE_IMAGE_WIDTH);

		cropperRectangle = new CropperRectangle();
		cropperRectangle.setFill(new Color(1.0, 1.0, 1.0, 0.5));
		cropperRectangle.setHeight(133.33333);
		cropperRectangle.setWidth(100.0);
		StackPane.setAlignment(cropperRectangle, Pos.TOP_LEFT);
		cropperRectangle.setStroke(new Color(0.8666666746139526,
				0.8666666746139526, 0.8666666746139526, 1.0));

		imageCropperStackPane.getChildren().addAll(sourceImageView,
				cropperRectangle);

		imageCropperScrollPane.setContent(imageCropperStackPane);
		
		HBox targetHBox = new HBox();
		targetHBox.setAlignment(Pos.CENTER);
		targetHBox.setMaxHeight(TARGET_HBOX_HEIGHT);
		targetHBox.setMaxWidth(TARGET_HBOX_WIDTH);
		targetHBox.setMinHeight(TARGET_HBOX_HEIGHT);
		targetHBox.setMinWidth(TARGET_HBOX_WIDTH);
		targetHBox
				.setStyle("-fx-border-color: lightgrey; -fx-background-color: #fefefe;");
		GridPane.setHgrow(targetHBox, Priority.ALWAYS);
		GridPane.setVgrow(targetHBox, Priority.ALWAYS);
		targetHBox.setPadding(new Insets(10.0, 10.0, 10.0, 10.0));
		imageCropperGridPane.add(targetHBox, 1, 1);

		targetImageView = new ImageView();
		targetImageView.setFitHeight(TARGET_IMAGE_HEIGHT);
		targetImageView.setFitWidth(TARGET_IMAGE_WIDTH);
		targetImageView.setPickOnBounds(true);
		targetImageView.setPreserveRatio(true);
		GridPane.setHgrow(targetImageView, Priority.NEVER);
		GridPane.setVgrow(targetImageView, Priority.NEVER);
		targetHBox.getChildren().add(targetImageView);

		// button row

		loadImageButton = new Button(SOURCE_BUTTON_TEXT);
		loadImageButton.setAlignment(Pos.CENTER);
		loadImageButton.setMnemonicParsing(false);
		imageCropperGridPane.add(loadImageButton, 0, 2);
		loadImageButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				reset();

				// choose the name
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle(SOURCE_CHOOSE_LABEL);
				fileChooser.getExtensionFilters().addAll(
						new FileChooser.ExtensionFilter("All", "*.jpg",
								"*.jpeg", "*.png"),
						new FileChooser.ExtensionFilter("png", "*.png"),
						new FileChooser.ExtensionFilter("jpg", "*.jpg",
								"*.jpeg"));

				File imageFile = fileChooser
						.showOpenDialog(imageCropperGridPane.getScene()
								.getWindow());

				// load source image
				loadSourceImage(imageFile);
			}
		});

		saveImageButton = new Button(TARGET_BUTTON_TEXT);
		saveImageButton.setAlignment(Pos.CENTER);
		saveImageButton.setMnemonicParsing(false);
		imageCropperGridPane.add(saveImageButton, 1, 2);
		saveImageButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				// open file chooser dialog
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle(TARGET_CHOOSE_LABEL);
				fileChooser.getExtensionFilters().addAll(
						new FileChooser.ExtensionFilter("All", "*.jpg",
								"*.jpeg", "*.png"),
						new FileChooser.ExtensionFilter("png", "*.png"),
						new FileChooser.ExtensionFilter("jpg", "*.jpg",
								"*.jpeg"));
				File imageFile = fileChooser
						.showSaveDialog(imageCropperGridPane.getScene()
								.getWindow());

				saveImage(imageFile);
			}
		});

	}
	
	private void initialize() {
		// TODO stefan - Set SubControl Property Test
		getSkinnable().setSourceImageView(sourceImageView);
		getSkinnable().setTargetImageView(targetImageView);
		// TODO stefan - SubControl Initialization > Better move to SubControl?
		sourceImageView.setCropperRectangle(cropperRectangle);
		sourceImageView.initialize();
		cropperRectangle.initialize();
		bind();
	}
	
	private void bind() {

		// bind image properties
		getSkinnable().targetImageProperty().bind(
				sourceImageView.imageProperty());

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

						if (observable == null)
							sourceImageView.imageProperty()
									.removeListener(this);

						if (newValue != null) {
							// width
							imageCropperScrollPane
									.maxWidthObservablesProperty().clear();
							imageCropperScrollPane
									.maxWidthObservablesProperty().add(
											cropperRectangle.widthProperty());
							imageCropperScrollPane
									.maxWidthObservablesProperty().add(
											sourceImageView.fitWidthProperty());
							// height
							imageCropperScrollPane
									.maxHeightObservablesProperty().clear();
							imageCropperScrollPane
									.maxHeightObservablesProperty().add(
											cropperRectangle.heightProperty());
							imageCropperScrollPane
									.maxHeightObservablesProperty()
									.add(sourceImageView.fitHeightProperty());
						}

					}
				});

	}

	private void reset() {
		 sourceImageView.reset();
		 cropperRectangle.reset();
	}

	private void loadSourceImage(File image) {
		if (image != null) {
			LoadImageTask task = new LoadImageTask(image);
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

	private void saveImage(File image) {
		 Thread cropSaveThread = new Thread(new CropWriteImageTask(image,
		 getSkinnable().getTargetImage(), cropperRectangle.getX(),
		 cropperRectangle.getY(), cropperRectangle.getWidth(),
		 cropperRectangle.getHeight()));
		 if (image != null)
		 cropSaveThread.start();
	}

	// ###### SkinBase API ######

	@Override
	protected void layoutChildren(double contentX, double contentY,
			double contentWidth, double contentHeight) {

		// dynamic
		super.layoutChildren(contentX, contentY, contentWidth, contentHeight);
		// static
		// layoutInArea(imageCropperGridPane, 0, 0, contentWidth, contentHeight,
		// Control.USE_PREF_SIZE, HPos.LEFT, VPos.TOP);
	}

	@Override
	protected double computeMinWidth(double height, double topInset,
			double rightInset, double bottomInset, double leftInset) {

		// dynamic
		return super.computeMinWidth(height, topInset, rightInset, bottomInset,
				leftInset);
		// static
		// return leftInset + imageCropperGridPane.minWidth(height) +
		// rightInset;

	}

	@Override
	protected double computeMinHeight(double width, double topInset,
			double rightInset, double bottomInset, double leftInset) {

		// dynamic
		return super.computeMinHeight(width, topInset, rightInset, bottomInset,
				leftInset);
		// static
		// return topInset + imageCropperGridPane.minHeight(width) +
		// bottomInset;

	}

	@Override
	protected double computePrefWidth(double height, double topInset,
			double rightInset, double bottomInset, double leftInset) {

		// dynamic
		return super.computePrefWidth(height, topInset, rightInset,
				bottomInset, leftInset);
		// static
		// return leftInset + imageCropperGridPane.prefWidth(height) +
		// rightInset;
	}

	@Override
	protected double computePrefHeight(double width, double topInset,
			double rightInset, double bottomInset, double leftInset) {

		// dynamic
		return super.computePrefHeight(width, topInset, rightInset,
				bottomInset, leftInset);
		// static
		// return topInset + imageCropperGridPane.prefHeight(width) +
		// bottomInset;
	}

	@Override
	protected double computeMaxWidth(double height, double topInset,
			double rightInset, double bottomInset, double leftInset) {
		return leftInset + imageCropperGridPane.maxWidth(height) + rightInset;
	}

	@Override
	protected double computeMaxHeight(double width, double topInset,
			double rightInset, double bottomInset, double leftInset) {
		return topInset + imageCropperGridPane.maxHeight(width) + bottomInset;
	}

}
