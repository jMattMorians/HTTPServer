package it.unifi.rc.httpserver;

import java.io.File;
import java.util.*;

public class ProxyHandler implements HTTPHandler {
	private List<HTTPRequest> requests = new ArrayList<HTTPRequest>();
	private List<HTTPReply> replies = new ArrayList<HTTPReply>();
	private HTTPHandler alternative = new HandlerGenericHost1_0(new File(""));	//default
	private int lastIndex = 0;
	private int maxIndex = 9;	//default

	
	public HTTPReply handle(HTTPRequest request) {
		HTTPReply reply = null;
		for (int i=0; i<requests.size() ; i++) {
			if(requests.get(i).equals(request)){
				reply = replies.get(i);
				return reply;
			}
		}
		reply = alternative.handle(request);
		return reply;
	}
	
	public void add(HTTPRequest request, HTTPReply reply) {
		requests.add(lastIndex, request);
		replies.add(lastIndex, reply);
		lastIndex = lastIndex+1;
		if(lastIndex==maxIndex) {
			lastIndex = 0;
		}
	}
	
	public List<HTTPRequest> getRequests() {
		return requests;
	}
	
	public List<HTTPReply> getReplies() {
		return replies;
	}
	
	public void setMaxIndex(int maxIndex) {
		this.maxIndex = maxIndex;
	}
	
	public void setAltHandler(HTTPHandler alternative) {
		this.alternative = alternative;
	}
	
}
