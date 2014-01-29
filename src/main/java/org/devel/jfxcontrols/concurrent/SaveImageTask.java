/**
 * 
 */
package org.devel.jfxcontrols.concurrent;

import java.io.File;
import java.io.IOException;

import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import javax.imageio.ImageIO;

import org.apache.commons.io.FilenameUtils;

/**
 * @author stefan.illgen
 * 
 */
public class SaveImageTask extends Task<Boolean> {

	private File imageFile;
	private Image image;

	public SaveImageTask(final File imageFile, final Image image) {
		this.imageFile = imageFile;
		this.image = image;
	}

	@Override
	protected Boolean call() throws Exception {

		updateMessage("");
		try {
			ImageIO.write(SwingFXUtils.fromFXImage(image, null),
					FilenameUtils.getExtension(imageFile.toURI().getPath()),
					imageFile);
			updateMessage(FilenameUtils.getName(imageFile.toURI().getPath())
					+ " erfolgreich gespeichert.");
		} catch (IOException e) {
			updateMessage("Fehler beim Speichern von "
					+ FilenameUtils.getName(imageFile.toURI().getPath()));
			return false;
		}

		return true;
	}

}
