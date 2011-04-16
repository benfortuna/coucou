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
