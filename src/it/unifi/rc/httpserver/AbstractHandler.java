package it.unifi.rc.httpserver;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractHandler implements HTTPHandler{
	private File pathFile;
	private List<String> methods = new ArrayList<String>();
	
	public AbstractHandler(File pathFile){
		this.pathFile = pathFile;
		methods.add("GET");
		methods.add("POST");
		methods.add("HEAD");
	}
	
	public abstract boolean hostControl(HTTPRequest request);
	public abstract String getCurrentVersion();
	public abstract HTTPReply chooseMethod(HTTPRequest request);

	public HTTPReply handle(HTTPRequest request) {
		HTTPReply reply = null;
		
		if(hostControl(request) == false){
			return null;
		}
		
		if(!request.getVersion().equals(getCurrentVersion())){
			return null;
		}
		
		if(!methods.contains(request.getMethod())) {
			return new HTTPReplyClass(request.getVersion(), "400", "Bad Request", new HashMap<String,String>(), "");
		}
		
		reply = chooseMethod(request);			
		return reply;
	}
	
	public HTTPReplyClass replyToGet(HTTPRequest request) {
		File f = new File(pathFile.getAbsolutePath() + request.getPath());
		List<String> content = new ArrayList<String>();
		
		try {
			content = Files.readAllLines(Paths.get(f.getAbsolutePath()));
		} catch (IOException e) {
			return new HTTPReplyClass(request.getVersion(), "404", "Not Found", null, null);
		}
		
		String text = contentToString(content); 
		
		return new HTTPReplyClass(request.getVersion(), "200", "OK", makeParameters(f), text);

	}
	
	public HTTPReplyClass replyToHead(HTTPRequest request) {
		File f = new File(pathFile.getAbsolutePath() + request.getPath()); 
		
		if(f.exists()){
			return new HTTPReplyClass(request.getVersion(), "200", "OK", makeParameters(f), null);
		}else{
			return new HTTPReplyClass(request.getVersion(), "404", "Not Found", null, null);
		}
	}
	
	public HTTPReplyClass replyToPost(HTTPRequest request) {
		File f = new File(pathFile.getAbsolutePath() + request.getPath());
		
		if(request.getEntityBody().isEmpty()) {
			return new HTTPReplyClass(request.getVersion(), "204", "No Content", null, null); //non sicuro dello status code e message
		}
		
		try {
			if(f.exists()){
				FileOutputStream fstream = new FileOutputStream(f, true);
				byte[] content = request.getEntityBody().getBytes();
				fstream.write(content);
				fstream.close();
			}else {
				f.getParentFile().mkdirs();
                f.createNewFile();
                FileOutputStream fstream = new FileOutputStream(f, true);
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

	public String contentToString(List<String> content) {
		String s = "";
		for(int i = 0; i < content.size(); i++) {
			s = s + content.get(i);
		}
		return s;
	}
	
	public Map<String,String> makeParameters(File f){
		Map<String,String> parameters = new HashMap<>();
		parameters.put("Content-Length", String.valueOf(f.length()));
		parameters.put("Date", new Date().toString());
		return parameters;
	}
	
	public List<String> getMethods() {
		return methods;
	}
	
	public String getPathFile(){
		return pathFile.getAbsolutePath();
	}

}
