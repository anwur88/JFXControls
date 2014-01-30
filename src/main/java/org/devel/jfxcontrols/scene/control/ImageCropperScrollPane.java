/**
 * 
 */
package org.devel.jfxcontrols.scene.control;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

/**
 * @author stefan.illgen
 * 
 */
public class ImageCropperScrollPane extends ScrollPane implements Initializable {

	// ### Controller API ###

	private double referencePointX;
	private double referencePointY;

	private ImageCropperScrollPane imageCropperScrollPane = this;

	@FXML
	private StackPane imageStackPane;

	@FXML
	private ImageView sourceImageView;

	public ObjectProperty<Image> sourceImageProperty() {
		return sourceImageView.imageProperty();
	}

	public Image getSourceImage() {
		return sourceImageView.imageProperty().get();
	}

	public void setSourceImage(Image sourceImage) {
		sourceImageView.imageProperty().set(sourceImage);
	}

	@FXML
	private Rectangle cropperRectangle;

	/*
	 * Property 4 handling custom rectangles.
	 */
	private ObjectProperty<Rectangle> _cropperRectangle;

	public ObjectProperty<Rectangle> cropperRectangleProperty() {
		if (_cropperRectangle == null)
			_cropperRectangle = new SimpleObjectProperty<Rectangle>(
					cropperRectangle);
		return _cropperRectangle;
	}

	public Rectangle getCropperRectangle() {
		return cropperRectangleProperty().get();
	}

	public void setCropperRectangle(Rectangle cropperRectangle) {
		cropperRectangleProperty().set(cropperRectangle);
	}

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
		// offset inside cropperRectangle
		double offsetX = referencePointX - getCropperRectangle().getX();
		// distance to translate the rectangle to
		double translateX = getCropperRectangle().getTranslateX()
				+ event.getX() - offsetX;
		// new x position of the left upper corner for the cropperRectangle
		double newXLeft = getCropperRectangle().getX() + translateX;
		// new x position of the downer edge for the cropperRectangle
		double newXRight = newXLeft + getCropperRectangle().getWidth();
		if (newXLeft >= 0 && newXRight <= sourceImageView.getFitWidth())
			getCropperRectangle().setTranslateX(translateX);

		// ### Y axis ###
		// offset inside cropperRectangle
		double offsetY = referencePointY - getCropperRectangle().getY();
		// distance to translate the rectangle to
		double translateY = getCropperRectangle().getTranslateY()
				+ event.getY() - offsetY;
		// new y position of the upper edge for the cropperRectangle
		double newYUp = getCropperRectangle().getY() + translateY;
		// new y position of the downer edge for the cropperRectangle
		double newYDown = newYUp + getCropperRectangle().getHeight();
		if (newYUp >= 0 && newYDown <= sourceImageView.getFitHeight())
			getCropperRectangle().setTranslateY(translateY);

	}

	// ### private API ###

	private void bind() {
		bindSourceScrollMaxBounds();
		bindSourceImageSize();
	}

	private void bindSourceScrollMaxBounds() {

		this.maxWidthProperty().bind(new DoubleBinding() {
			{
				super.bind(sourceImageProperty(), getCropperRectangle()
						.widthProperty());
			}

			@Override
			protected double computeValue() {
				if (getSourceImage() != null) {
					double siWidth = getSourceImage().getWidth();
					double cropperWidth = getCropperRectangle().getWidth();
					if (siWidth >= cropperWidth)
						return siWidth + calculateVerticalDimDiff();
					else
						return cropperWidth + calculateVerticalDimDiff();
				}
				// else use computed value
				return -1;
			}

			private double calculateVerticalDimDiff() {
				return imageCropperScrollPane.getWidth()
						- imageCropperScrollPane.getViewportBounds().getWidth();
			}
		});

		this.maxHeightProperty().bind(new DoubleBinding() {
			{
				super.bind(sourceImageProperty(), getCropperRectangle()
						.heightProperty());
			}

			@Override
			protected double computeValue() {
				if (getSourceImage() != null) {
					double siHeight = getSourceImage().getHeight();
					double cropperHeight = getCropperRectangle().getHeight();
					if (siHeight >= cropperHeight)
						return siHeight + calculateHorizontalDimDiff();
					else
						return cropperHeight + calculateHorizontalDimDiff();
				}
				// else use computed value
				return -1;
			}

			private double calculateHorizontalDimDiff() {
				return imageCropperScrollPane.getBoundsInParent().getHeight()
						- imageCropperScrollPane.getViewportBounds()
								.getHeight();
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

	// ### init ###

	public ImageCropperScrollPane(Node content) {
		this();
		setContent(content);
	}

	public ImageCropperScrollPane() {
		super();
		setupSkin();
		loadFXML();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		bind();
	}

	private void loadFXML() {

		// load FXML
		URL url = getClass().getResource("ImageCropperScrollPane.fxml");
		FXMLLoader fxmlLoader = new FXMLLoader(url);

		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);

		try {
			fxmlLoader.load();
			layout();
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}

	}

	private void setupSkin() {
		getStyleClass().add("image-cropper-scroll-pane");
	}

	@Override
	protected String getUserAgentStylesheet() {
		return getClass().getResource("image-cropper.css").toExternalForm();
	}

	public void reset() {
		setSourceImage(null);
		// TODO stefan - solve via bindings
		getCropperRectangle().setTranslateX(0.0);
		getCropperRectangle().setTranslateY(0.0);
	}

}
