package org.mnode.coucou.mail;

import java.util.ArrayList;
import java.util.List;

import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.Store;

import org.mnode.coucou.PathResult;
import org.mnode.coucou.PathResultException;

import edu.emory.mathcs.backport.java.util.Arrays;

public class StorePathResult implements PathResult<Store, Folder> {

	private final Store store;
	
	private final String name;
	
	StorePathResult(Store store, String name) {
		this.store = store;
		this.name = name;
	}
	
	@Override
	public String getName() throws PathResultException {
		return name;
	}

	@Override
	public Store getElement() {
		return store;
	}

	@Override
	public boolean isLeaf() {
		return false;
	}

	@Override
	public List<PathResult<?, ?>> getChildren() throws PathResultException {
		final List<PathResult<?, ?>> children = new ArrayList<PathResult<?, ?>>();
//		return store.getDefaultFolder().list();
		return children;
	}

	@Override
	public PathResult<?, Folder> getChild(Folder result) throws PathResultException {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Folder> getResults() throws PathResultException {
		try {
			if (!store.isConnected()) {
				store.connect();
			}
			return Arrays.asList(store.getDefaultFolder().list());
		} catch (MessagingException e) {
			throw new PathResultException(e);
		}
	}
	
}
