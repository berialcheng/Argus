package org.argus.core.route;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


public class Rule {
	
	private String path;

	private String method;
	
	private String handler;
	
	private int priority;

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getHandler() {
		return handler;
	}

	public void setHandler(String handler) {
		this.handler = handler;
	}
	
	
	
	/*public static void main(String args[])
	{
		Gson gson = new Gson();
		List<Rule> rules = new ArrayList<Rule>();
		for (int i = 0; i < 10; i++) {
			Rule p = new Rule();
			p.setMethod("method"+i);
			p.setHandler("abc"+i);
			p.setArtifact("artifact"+i);
			rules.add(p);
		}
		String str = gson.toJson(rules);
		System.out.println(str);
		
	}*/
}
