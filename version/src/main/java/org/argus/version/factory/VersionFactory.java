package org.argus.version.factory;

import java.util.HashMap;
import java.util.Map;

import org.argus.version.VersionSystem;
import org.argus.version.service.IVersionService;
import org.argus.version.service.svn.VersionService;
import org.tmatesoft.svn.core.SVNException;

public class VersionFactory {

	public static IVersionService getSVNInstance(VersionSystem type, Map<String, String> parameters)
			throws SVNException {

		IVersionService service = null;
		switch (type) {
		case Subversion:
			service = new VersionService();
			service.configure(parameters);
			break;
		case Git:
			;
		default:
			throw new IllegalArgumentException();
		}
		return service;
	}

	/**
	 * @param args
	 * @throws SVNException
	 */
	public static void main(String[] args) throws SVNException {
		Map<String, String> parameters = new HashMap<String, String>();
		// https://quality-one.googlecode.com/svn/trunk/
		parameters.put("url", "https://quality-one.googlecode.com/svn/trunk/");
		parameters.put("usingPrivateKey", "false");
		IVersionService service = VersionFactory.getSVNInstance(VersionSystem.Subversion, parameters);
		System.out.println(service.getLatestVersion());
	}
}
