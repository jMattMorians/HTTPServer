package it.unifi.rc.httpserver;

import java.util.HashMap;
import java.util.Map;

public class HTTPRequestClass implements HTTPRequest {
	
	private String method = "";
	private String path = "";
	private String version = "";
	private Map<String,String> parameters = new HashMap<>();
	private String entityBody = "";
	
	public HTTPRequestClass(String method, String path, String version, Map<String,String> parameters, String entityBody) {
		this.method = method;
		this.path = path;
		this.version = version;
		this.parameters = parameters;
		this.entityBody = entityBody;
	}

	public String getVersion() {
		return version;
	}

	public String getMethod() {
		return method;
	}

	public String getPath() {
		return path;
	}

	public String getEntityBody() {
		return entityBody;
	}

	public Map<String, String> getParameters() {
		return parameters;
	}
	
	public boolean equals(Object o) {
		boolean result = false;
		if (o instanceof HTTPRequest) {
			HTTPRequest req = (HTTPRequest) o;
			if ((method.equals(req.getMethod())) && (path.equals(req.getPath())) 
					&& (version.equals(req.getVersion())) && (parameters.equals(req.getParameters())) 
					&& (entityBody.equals(req.getEntityBody()))) {
				return true;
			}
		}
		return result;
	}

}