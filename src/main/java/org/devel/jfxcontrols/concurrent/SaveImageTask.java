/**
 * 
 */
package org.devel.jfxcontrols.concurrent;

import java.io.File;
import java.io.IOException;

import javafx.beans.property.StringProperty;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
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

	public SaveImageTask(final File imageFile, final Image image,
			final StringProperty output) {
		this.imageFile = imageFile;
		this.image = image;
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
