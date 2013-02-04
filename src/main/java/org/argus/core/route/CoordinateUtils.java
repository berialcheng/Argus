package org.argus.core.route;

import java.io.File;


public class CoordinateUtils {

	public static Artifact resloveCoordinate(String url) {

		if (url.startsWith("/")) {
			url = url.substring(1);
		}
		String[] items = url.split("/");
		Artifact artifact = new Artifact();

		String groupId = items[0];

		for (int i = 1; i < items.length; i++) {
			String fragment = items[i];
			/* reach the last one */
			if (i == items.length - 1) {
				artifact.setArtifactId(fragment);
				break;
			}
			groupId += "." + fragment;
		}
		artifact.setGroupId(groupId);
		return artifact;

	}

	public static String resloveRelativeArtifactPath(Artifact artifact) {
		String separator = File.separator;
		String location = artifact.getGroupId().replace('.',
				File.separator.charAt(0))
				+ separator + artifact.getArtifactId();
		return location;
	}

	public static boolean checkIfArtifactExists(String homeDir,
			Artifact artifact) {
		File file = new File(homeDir + File.separator
				+ resloveRelativeArtifactPath(artifact));
		return file.exists();
	}

	public static boolean createPathForArtifact(String homeDir,
			Artifact artifact) {
		File file = new File(homeDir + File.separator
				+ resloveRelativeArtifactPath(artifact));
		return file.mkdirs();
	}

	public static void main(String args[]) {
		Artifact artifact = CoordinateUtils.resloveCoordinate("/com/hp/abc");
		System.out.println(artifact);

		String home = System.getProperty("user.home") + File.separator
				+ ".quality-one";

		System.out.println(CoordinateUtils
				.checkIfArtifactExists(home, artifact));
		System.out.println(CoordinateUtils
				.createPathForArtifact(home, artifact));
	}
}
