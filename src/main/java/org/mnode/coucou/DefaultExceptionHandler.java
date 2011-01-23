package org.mnode.coucou;

import java.awt.Component;
import java.lang.Thread.UncaughtExceptionHandler;

import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;
import org.mnode.base.log.LogAdapter;
import org.mnode.base.log.adapter.Slf4jAdapter;
import org.slf4j.LoggerFactory;

public class DefaultExceptionHandler implements UncaughtExceptionHandler {
	
	private static final LogAdapter LOG = new Slf4jAdapter(LoggerFactory.getLogger(UncaughtExceptionHandler.class));

	private Component dialogOwner;
	
	@Override
	public void uncaughtException(Thread thread, Throwable exception) {
		LOG.log(LogEntries.UNEXPECTED_ERROR, exception);
		
		final ErrorInfo error = new ErrorInfo("Error", exception.getMessage(),
				String.format("<html><body>Unexpected error in thead <em>%s</em>: %s</body></html>", thread, exception),
				null, null, null, null);
		
		JXErrorPane.showDialog(dialogOwner, error);
	}

	/**
	 * @return the dialogOwner
	 */
	public Component getDialogOwner() {
		return dialogOwner;
	}

	/**
	 * @param dialogOwner the dialogOwner to set
	 */
	public void setDialogOwner(Component dialogOwner) {
		this.dialogOwner = dialogOwner;
	}

}
