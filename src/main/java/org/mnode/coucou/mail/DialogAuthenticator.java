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
package org.mnode.coucou.mail;

import java.awt.Component;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

public class DialogAuthenticator extends Authenticator {

	private final Component parent;
	
	public DialogAuthenticator(Component parent) {
		this.parent = parent;
	}

	@Override
	protected PasswordAuthentication getPasswordAuthentication() {
		final JPasswordField passwordField = new JPasswordField(20);
		
		final String message = String.format("%s: %s", getRequestingSite(), getRequestingPrompt());
		if (JOptionPane.showConfirmDialog(parent, passwordField,
				message, JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
			
			return new PasswordAuthentication(getDefaultUserName(), new String(passwordField.getPassword()));
		}
//		final String password = JOptionPane.showInputDialog(parent,
//				String.format("%s: %s", getRequestingSite(), getRequestingPrompt()));
//		
//		if (password != null) {
//			return new PasswordAuthentication(getDefaultUserName(), password);
//		}
		else {
			return super.getPasswordAuthentication();
		}
	}
}
