/**
 * 
 */
package org.devel.jfxcontrols.concurrent;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javafx.beans.property.StringProperty;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.image.Image;

import org.apache.commons.io.FilenameUtils;

/**
 * @author stefan.illgen
 * 
 */
public class LoadImageTask extends Task<Boolean> {

	private static final String TXT_load_image_error_0 = "Fehler beim Speichern von ";
	private static final String TXT_load_image_success = "Image was successfully loaded";
	private File imageFile;
	private Image image;

	public LoadImageTask(final File imageFile, final StringProperty output) {
		this.imageFile = imageFile;
		output.bind(messageProperty());
		setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent event) {
				// TODO stefan - unbind the property after the fade out was
				// successful
				// output.unbind();
			}
		});
	}

	@Override
	protected Boolean call() throws Exception {

		updateMessage("");
		try {
			image = new Image(new FileInputStream(imageFile));

			// TODO stefan - must the image size be restricted like that?
			// if (image.getWidth() < WIDTH || image.getHeight() < HEIGHT) {
			// updateMessage("Bild ist zu klein. Minimum sind 200*150 Pixel.");
			// return false;
			// }

			updateMessage(TXT_load_image_success);

			return true;

		} catch (IOException e) {
			updateMessage(TXT_load_image_error_0
					+ FilenameUtils.getName(imageFile.toURI().getPath()));
		}

		return false;
	}

	public Image getImage() {
		return image;
	}

}
