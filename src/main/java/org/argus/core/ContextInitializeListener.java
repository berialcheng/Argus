package org.argus.core;
import java.io.File;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.argus.core.route.RouteUtils;

public class ContextInitializeListener implements ServletContextListener{

	private static String WORKINGDIR = ".argus";
	
	public void contextInitialized(ServletContextEvent sce) {
		
		String homePath = System.getProperty("user.home") + File.separator + WORKINGDIR;
		File homeDir = new File(homePath);
		if(!homeDir.exists())
		{
			homeDir.mkdirs();
		}
		
		//initialize route configuration
		RouteUtils.initializeRouteConf();
	}

	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub
	}
}
