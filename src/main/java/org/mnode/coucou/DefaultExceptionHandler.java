/**
 * This file is part of Coucou.
 *
 * Copyright (c) 2011, Ben Fortuna [fortuna@micronode.com]
 *
 * Coucou is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Coucou is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Coucou.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.mnode.coucou;

import java.awt.Component;
import java.lang.Thread.UncaughtExceptionHandler;

import javax.swing.SwingUtilities;

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
		
		final String title = "Error";
		final String basicErrorMessage = exception.getMessage();
		final String detailedErrorMessage = String.format("<html><body>Unexpected error in thead <em>%s</em>: %s</body></html>", thread, exception);
		final String category = thread.getName();
		
		final ErrorInfo error = new ErrorInfo(title, basicErrorMessage, detailedErrorMessage,
				category, exception, null, null);
		
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				JXErrorPane.showDialog(dialogOwner, error);
			}
		});
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
