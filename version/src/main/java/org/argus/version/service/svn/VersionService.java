package org.argus.version.service.svn;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.argus.version.service.IVersionService;
import org.tmatesoft.svn.core.SVNDirEntry;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNLogEntry;
import org.tmatesoft.svn.core.SVNLogEntryPath;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.io.SVNFileRevision;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

public class VersionService implements IVersionService {

	private SVNRepository repository;

	private final long QUERY_VERSION_THRESHOLD = 100;

	private ISVNAuthenticationManager authenticate(String userName, String password, boolean usingPrivateKey,
			String privateKeyFilePath, String passphrase) {
		ISVNAuthenticationManager authManager = null;
		if (usingPrivateKey) {
			if (userName == null || password == null) {
				authManager = SVNWCUtil.createDefaultAuthenticationManager(null, null, null, new File(
						privateKeyFilePath), passphrase, false);
			} else {
				authManager = SVNWCUtil.createDefaultAuthenticationManager(null, userName, password, new File(
						privateKeyFilePath), passphrase, false);
			}
		} else {
			authManager = SVNWCUtil.createDefaultAuthenticationManager(userName, password);
		}
		return authManager;
	}

	private long getLatestRepositoryVersionNum() {
		long latest = -1;
		try {
			SVNDirEntry dir = repository.getDir("", -1, true, new HashSet());
			latest = dir.getRevision();
		} catch (SVNException e) {
			// TOOD got error what to do?
			e.printStackTrace();
		}
		return latest;
	}

	public void configure(Map<String, String> parameter) throws SVNException {

		String url = parameter.get("url");
		String userName = parameter.get("userName");
		String password = parameter.get("password");
		boolean usingPrivateKey = Boolean.valueOf(parameter.get("usingPrivateKey"));
		String privateKeyFilePath = parameter.get("privateKeyFilePath");
		String passphrase = parameter.get("passphrase");

		if ("".equalsIgnoreCase(userName) || "".equalsIgnoreCase(password)) {
			userName = null;
			password = null;
		}
		FSRepositoryFactory.setup();
		DAVRepositoryFactory.setup();
		SVNRepositoryFactoryImpl.setup();
		SVNURL svnUrl = SVNURL.parseURIEncoded(url);
		repository = SVNRepositoryFactoryImpl.create(svnUrl);
		repository.setAuthenticationManager(authenticate(userName, password, usingPrivateKey, privateKeyFilePath,
				passphrase));
	}

	public void listLogEntries(int startVersion, int endVersion) throws SVNException {
		Collection logEntries = null;
		/* do use the content to match the file */
		logEntries = repository.log(new String[] { "" }, null, startVersion, endVersion, true, true);
		for (Iterator entries = logEntries.iterator(); entries.hasNext();) {
			SVNLogEntry logEntry = (SVNLogEntry) entries.next();
			System.out.println("---------------------------------------------");
			System.out.println("revision: " + logEntry.getRevision());
			System.out.println("author: " + logEntry.getAuthor());
			System.out.println("date: " + logEntry.getDate());
			System.out.println("log message: " + logEntry.getMessage());
			if (logEntry.getChangedPaths().size() > 0) {
				System.out.println();
				System.out.println("changed paths:");
				Set changedPathsSet = logEntry.getChangedPaths().keySet();

				for (Iterator changedPaths = changedPathsSet.iterator(); changedPaths.hasNext();) {
					SVNLogEntryPath entryPath = (SVNLogEntryPath) logEntry.getChangedPaths().get(changedPaths.next());
					System.out.println(" "
							+ entryPath.getType()
							+ " "
							+ entryPath.getPath()
							+ ((entryPath.getCopyPath() != null) ? " (from " + entryPath.getCopyPath() + " revision "
									+ entryPath.getCopyRevision() + ")" : ""));
				}
			}
		}
	}

	public long getLatestVersion() {
		long version = -1;
		try {
			test();
			version = this.repository.getLatestRevision();
		} catch (SVNException e) {
			e.printStackTrace();
		}
		return version;
	}

	public void test() {
		String path = "quality-one/server/src/main/java/com/hp/it/server/servlet/SonarViolationChangeReportServlet.java";
		try {
			Collection<SVNFileRevision> col = this.repository.getFileRevisions(path, null, 1, 63);
			for (SVNFileRevision revision : col) {
				this.repository.getFile(path, revision.getRevision(), null, new FileOutputStream(new File(
						"/tmp/version/" + "pom.xml" + "-v" + revision.getRevision())));
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SVNException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
