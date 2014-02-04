/**
 * 
 */
package org.devel.jfxcontrols.concurrent;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javafx.concurrent.Task;
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

	public LoadImageTask(File imageFile) {
		this.imageFile = imageFile;
	}

	@Override
	protected Boolean call() throws Exception {

		updateMessage("");
		try {
			image = new Image(new FileInputStream(imageFile));
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
