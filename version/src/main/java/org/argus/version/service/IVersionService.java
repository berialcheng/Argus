package org.argus.version.service;

import java.util.Map;

import org.tmatesoft.svn.core.SVNException;

public interface IVersionService {

	public void configure(Map<String, String> parameter) throws SVNException;

	public long getLatestVersion();
}
