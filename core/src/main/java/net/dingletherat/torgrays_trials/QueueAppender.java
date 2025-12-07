// Copyright (c) 2025 DingleTheRat. All Rights Reserved.
package net.dingletherat.torgrays_trials;

import org.apache.logging.log4j.core.*;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.PatternLayout;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Plugin(name = "Queue", category = Core.CATEGORY_NAME, elementType = Appender.ELEMENT_TYPE)
public class QueueAppender extends AbstractAppender {
	private PrintWriter fileWriter;
	static private QueueAppender self;

	protected QueueAppender(String name, Filter filter, Layout<? extends Serializable> layout, boolean ignoreExceptions) {
		super(name, filter, layout, ignoreExceptions, Property.EMPTY_ARRAY);
		try {
			File file = new File("logs/latest.log");
			if (new File("logs/").mkdirs()) { LOGGER.info("Created logs directory"); }
			if (file.createNewFile()) { LOGGER.info("Created latest.log file"); }
			fileWriter = new PrintWriter(new FileWriter(file, true), true);
			String dateTime =   LocalDateTime.now().format(DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss"));
			fileWriter.println(" --- New Session - " + dateTime + " --- ");
		} catch (IOException e) {
			Main.handleException(e);
		}
		self = this;
	}

	@PluginFactory
	public static QueueAppender createAppender(
			@PluginAttribute("name") String name,
			@PluginElement("Layout") Layout<? extends Serializable> layout,
			@PluginElement("Filter") Filter filter) {
		if (layout == null) {
			layout = PatternLayout.createDefaultLayout();
		}
		return new QueueAppender(name, filter, layout, true);
	}

	public static void printError(Exception exception) {
		System.err.println("\u001B[31mAn exception occurred: " + exception.getMessage());
		if (self.fileWriter != null) {
			self.fileWriter.print("An exception occurred: ");
			self.fileWriter.flush();
		}
		for (StackTraceElement element : exception.getStackTrace()) {
			String message = "\t" + element.toString();
			System.err.println(message);
			if (self.fileWriter != null) {
				self.fileWriter.println(message.replace("\u001B[34m", "")
						.replace("\u001B[32m", "")
						.replace("\u001B[36m", "")
						.replace("\u001B[m", ""));
				self.fileWriter.flush();
			}
		}
	}

	@Override
	public void append(LogEvent event) {
		String message = new String(getLayout().toByteArray(event));

		System.out.print(message);
		if (fileWriter != null) {
			fileWriter.print(message.replace("\u001B[34m", "")
							.replace("\u001B[32m", "")
							.replace("\u001B[36m", "")
							.replace("\u001B[m", ""));
			fileWriter.flush();
		}
	}
}
