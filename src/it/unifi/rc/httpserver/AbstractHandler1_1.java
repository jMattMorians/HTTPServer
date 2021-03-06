package it.unifi.rc.httpserver;

import java.io.File;
import java.io.FileOutputStream;

public abstract class AbstractHandler1_1 extends AbstractHandler{

	public AbstractHandler1_1(File pathFile) {
		super(pathFile);
		getMethods().add("PUT");
		getMethods().add("DELETE");
	}

	public String getCurrentVersion() {
		return "HTTP/1.1";
	}
	
	public HTTPReplyClass replyToPut(HTTPRequest request) {
		File f = new File(getPathFile() + request.getPath());
		
		if(request.getEntityBody().isEmpty()) {
			return new HTTPReplyClass(request.getVersion(), "204", "No Content", null, null);
		}
		
		try {
			if(f.exists()){
				FileOutputStream fstream = new FileOutputStream(f, false);
				byte[] content = request.getEntityBody().getBytes();
				fstream.write(content);
				fstream.close();
			}else {
				f.getParentFile().mkdirs();
                f.createNewFile();
                FileOutputStream fstream = new FileOutputStream(f, false);
				byte[] content = request.getEntityBody().getBytes();
				fstream.write(content);
				fstream.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new HTTPReplyClass(request.getVersion(), "400", "Bad Request", null, null);
		}
		
		return new HTTPReplyClass(request.getVersion(), "200", "OK", null, null);
	}
	
	public HTTPReplyClass replyToDelete(HTTPRequest request) {
		File f = new File(getPathFile() + request.getPath());
		if(f.exists()){
			f.delete();
			return new HTTPReplyClass(request.getVersion(), "200", "OK", null, null);
		}else {
			return new HTTPReplyClass(request.getVersion(), "404", "Not Found", null, null);
		}
		
	}
	
	public HTTPReplyClass chooseMethod(HTTPRequest request) {
		HTTPReplyClass reply = null;
		
		switch(request.getMethod()) {
			case("GET"):
				reply = replyToGet(request);
			case("POST"):
				reply = replyToPost(request);
			case("HEAD"):
				reply = replyToHead(request);
			case("PUT"):
				reply = replyToPut(request);
			case("DELETE"):
				reply = replyToDelete(request);
		}
		return reply;
	}

}
