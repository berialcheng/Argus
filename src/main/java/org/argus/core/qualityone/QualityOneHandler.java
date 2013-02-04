package org.argus.core.qualityone;

import java.net.URL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.argus.core.route.CoordinateUtils;

public class QualityOneHandler {

	public void get(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String urlStr = request.getServerName()
				+ ":"
				+ request.getServerPort()
				+ "/"
				+ QualityOneAdapter.translate(CoordinateUtils
						.resloveCoordinate(request.getPathInfo()));
		URL url = new URL(urlStr);
		System.out.println(url);
	}
}
