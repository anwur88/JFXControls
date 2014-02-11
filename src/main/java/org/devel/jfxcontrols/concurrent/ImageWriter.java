/**
 * 
 */
package org.devel.jfxcontrols.concurrent;

import java.io.File;
import java.io.IOException;

import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.WritableImage;

import javax.imageio.ImageIO;

import org.apache.commons.io.FilenameUtils;

/**
 * @author stefan.illgen
 * 
 */
public class ImageWriter extends Task<Boolean> {

	private static final String SAVE_IMAGE_SUCCESS_MSG = " erfolgreich gespeichert.";
	private static final String SAVE_IMAGE_ERROR_MSG = "Fehler beim Speichern von ";
	private File imageFile;
	private Image image;
	private int x;
	private int y;
	private int width;
	private int height;

	public ImageWriter(final File imageFile, final Image image, int x,
			int y, int width, int height) {
		this.imageFile = imageFile;
		this.image = image;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	/**
	 * Values behind the comma are rounded down.
	 * 
	 * @param imageFile
	 * @param image
	 * @param x2
	 * @param y2
	 * @param width2
	 * @param height
	 */
	public ImageWriter(File imageFile, Image image, double x2,
			double y2, double width2, double height) {
		this(imageFile, image, (int) x2, (int) y2, (int) width2, (int) height);
	}

	@Override
	protected Boolean call() throws Exception {

		updateMessage("");

		// crop
		WritableImage croppedImage = new WritableImage(image.getPixelReader(), x, y, width,
				height);

		// write
		try {
			String ext = FilenameUtils.getExtension(imageFile.toURI().getPath());
			// TODO stefan - Remove Swing Deps 4 Writing Image 2 File
			// fix bad jpg color conversions
			if(ext.equals("jpg") || ext.equals("jpeg"))
				croppedImage = fixBadJPEG(croppedImage);
			if (!ImageIO.write(SwingFXUtils.fromFXImage(croppedImage, null),
					ext,
					imageFile))
				updateErrorMessage();
			updateMessage(FilenameUtils.getName(imageFile.toURI().getPath())
					+ SAVE_IMAGE_SUCCESS_MSG);
		} catch (IOException e) {
			updateErrorMessage();
			return false;
		}

		return true;
	}

	private void updateErrorMessage() {
		updateMessage(SAVE_IMAGE_ERROR_MSG
				+ FilenameUtils.getName(imageFile.toURI().getPath()));
	}

	/*
	 * FIXME stefan - colors gets converted in a wrong way
	 */
	private WritableImage fixBadJPEG(Image image) {
		
		int width = Double.valueOf(image.getWidth()).intValue();
		int height = Double.valueOf(image.getHeight()).intValue();
		int[] array = new int[width * height];
		image.getPixelReader().getPixels(0, 0, width, height,
				PixelFormat.getIntArgbInstance(), array, 0, width);
		
		for (int i = array.length - 1; i >= 0; i--) {
			int y = array[i] >> 16 & 0xFF; // Y
			int b = (array[i] >> 8 & 0xFF) - 128; // Pb
			int r = (array[i] & 0xFF) - 128; // Pr

			int g = (y << 8) + -88 * b + -183 * r >> 8; //
			b = (y << 8) + 454 * b >> 8;
			r = (y << 8) + 359 * r >> 8;

			if (r > 255)
				r = 255;
			else if (r < 0)
				r = 0;
			if (g > 255)
				g = 255;
			else if (g < 0)
				g = 0;
			if (b > 255)
				b = 255;
			else if (b < 0)
				b = 0;

			array[i] = 0xFF000000 | (r << 8 | g) << 8 | b;
		}
		
		WritableImage imgNew = new WritableImage(width, height);
		image.getPixelReader().getPixelFormat();
		imgNew.getPixelWriter().setPixels(0, 0, width, height,
				PixelFormat.getIntArgbInstance(), array, 0, width);
		
		return imgNew;
	}

}
