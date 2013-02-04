package org.argus.core;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.argus.core.route.RouteUtils;
import org.argus.core.route.Rule;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/*")
public class CoreController {

	@RequestMapping
	public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String releativePath = request.getPathInfo();
		Rule rule = RouteUtils.resolveRoute(releativePath);

		String clzName = rule.getHandler();

		Class handlerClz = Class.forName(clzName);
		Object target = handlerClz.newInstance();
		String methodStr = request.getMethod();

		for (Method method : handlerClz.getDeclaredMethods()) {
			if (method.getName().equalsIgnoreCase(methodStr)) {
				method.invoke(target, request, response);
				break;
			}
		}
	}
}
