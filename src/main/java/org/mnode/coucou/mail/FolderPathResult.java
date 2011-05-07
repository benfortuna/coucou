package org.mnode.coucou.mail;

import java.util.ArrayList;
import java.util.List;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;

import org.mnode.coucou.PathResult;
import org.mnode.coucou.PathResultException;

import edu.emory.mathcs.backport.java.util.Arrays;

public class FolderPathResult implements PathResult<Folder, Message> {

	private final Folder folder;

	FolderPathResult(Folder folder) {
		this.folder = folder;
	}
	
	@Override
	public String getName() throws PathResultException {
		return folder.getName();
	}

	@Override
	public Folder getElement() {
		return folder;
	}

	@Override
	public boolean isLeaf() {
		return false;
	}

	@Override
	public List<PathResult<?, ?>> getChildren() throws PathResultException {
		final List<PathResult<?, ?>> children = new ArrayList<PathResult<?, ?>>();
		return children;
	}

	@Override
	public PathResult<?, Message> getChild(Message result) throws PathResultException {
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Message> getResults() throws PathResultException {
		try {
			return Arrays.asList(folder.getMessages());
		} catch (MessagingException e) {
			throw new PathResultException(e);
		}
	}

}
