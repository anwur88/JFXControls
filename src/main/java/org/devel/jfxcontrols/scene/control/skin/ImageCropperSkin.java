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
import javafx.scene.layout.RowConstraints;
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

	private static final double GRID_PADDING = 10.0;
	private static final double GRID_GAP = 10.0;

	private static final Color CROPPER_COLOR = new Color(1.0, 1.0, 1.0, 0.5);
	private static final Color CROPPER_STROKE_COLOR = new Color(
			0.8666666746139526, 0.8666666746139526, 0.8666666746139526, 1.0);
	private static final double CROPPER_WIDTH = 100.0;
	private static final double CROPPER_HEIGHT = 133.33333;

	public ImageCropperSkin(ImageCropper control) {
		super(control);
		createNodes();
		bind();
		getSkinnable().requestLayout();
	}

	private GridPane imageCropperGridPane;
	private Button loadImageButton;
	private Button saveImageButton;
	private Label sourceLabel;
	private Label targetLabel;
	private ImageCropperScrollPane imageCropperScrollPane;
	private StackPane imageCropperStackPane;
	private CropperRectangle cropperRectangle;
	private SourceImageView sourceImageView;
	private ImageView targetImageView;

	private void createNodes() {

		imageCropperGridPane = new GridPane();
		imageCropperGridPane.setHgap(GRID_GAP);
		imageCropperGridPane.setVgap(GRID_GAP);
		imageCropperGridPane.setStyle("-fx-background-color: white;");
		this.getChildren().addAll(imageCropperGridPane);

		imageCropperGridPane.getColumnConstraints().addAll(
				new ColumnConstraints(Region.USE_COMPUTED_SIZE,
						Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE,
						Priority.ALWAYS, HPos.CENTER, true),
				new ColumnConstraints(Region.USE_COMPUTED_SIZE,
						Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE,
						Priority.NEVER, HPos.CENTER, true));
//		imageCropperGridPane.getRowConstraints().addAll(
//				new RowConstraints(Region.USE_COMPUTED_SIZE,
//						Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE,
//						Priority.NEVER, VPos.CENTER, false),
//				new RowConstraints(Region.USE_COMPUTED_SIZE,
//						Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE,
//						Priority.ALWAYS, VPos.CENTER, true),
//				new RowConstraints(Region.USE_COMPUTED_SIZE,
//						Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE,
//						Priority.NEVER, VPos.CENTER, true));
		imageCropperGridPane.setPadding(new Insets(GRID_PADDING, GRID_PADDING,
				GRID_PADDING, GRID_PADDING));

		// label row

		sourceLabel = new Label(SOURCE_LABEL);
		sourceLabel.setAlignment(Pos.CENTER);
		sourceLabel.setContentDisplay(ContentDisplay.CENTER);
		sourceLabel.setStyle("-fx-font-weight: bold;");
		sourceLabel.setTextAlignment(TextAlignment.CENTER);
		GridPane.setHgrow(sourceLabel, Priority.ALWAYS);
		GridPane.setVgrow(sourceLabel, Priority.NEVER);
		imageCropperGridPane.add(sourceLabel, 0, 0);

		targetLabel = new Label(TARGET_LABEL);
		targetLabel.setAlignment(Pos.CENTER);
		targetLabel.setContentDisplay(ContentDisplay.CENTER);
		targetLabel.setStyle("-fx-font-weight: bold;");
		targetLabel.setTextAlignment(TextAlignment.CENTER);
		GridPane.setHgrow(targetLabel, Priority.ALWAYS);
		GridPane.setVgrow(targetLabel, Priority.NEVER);
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
		cropperRectangle.setFill(CROPPER_COLOR);
		cropperRectangle.setHeight(CROPPER_HEIGHT);
		cropperRectangle.setWidth(CROPPER_WIDTH);
		StackPane.setAlignment(cropperRectangle, Pos.TOP_LEFT);
		cropperRectangle.setStroke(CROPPER_STROKE_COLOR);

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

	private void bind() {

		// ### external / public / skinnable bindings ###

		getSkinnable().sourceImageProperty().bindBidirectional(
				sourceImageView.imageProperty());
		getSkinnable().targetImageProperty().bindBidirectional(
				targetImageView.imageProperty());

		getSkinnable().cropperFillColorProperty().bindBidirectional(
				cropperRectangle.fillProperty());
		getSkinnable().cropperStrokeColorProperty().bindBidirectional(
				cropperRectangle.strokeProperty());
		getSkinnable().cropperWidthProperty().bindBidirectional(
				cropperRectangle.widthProperty());
		getSkinnable().cropperHeightProperty().bindBidirectional(
				cropperRectangle.heightProperty());

		// ### internal initialization ###

		sourceImageView.setCropperRectangle(cropperRectangle);
		sourceImageView.initialize();
		cropperRectangle.initialize();

		// ### internal bindings ###

		// bind image properties
		targetImageView.imageProperty().bind(sourceImageView.imageProperty());

		// bind view port of target image view
		targetImageView.viewportProperty().bind(
				new ObjectBinding<Rectangle2D>() {
					{
						super.bind(cropperRectangle.widthProperty(),
								cropperRectangle.heightProperty(),
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
				targetImageView.getImage(), cropperRectangle.getTranslateX(),
				cropperRectangle.getTranslateY(), cropperRectangle.getWidth(),
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
