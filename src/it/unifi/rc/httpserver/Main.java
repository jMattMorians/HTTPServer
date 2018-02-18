package it.unifi.rc.httpserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Main {	// ora ci sono i test

	public static void main(String[] args) throws IOException {
		String result = "";
		ServerSocket ssocket = new ServerSocket(4323);	
		new Thread(
				  new Runnable(){
					public void run(){
				      try {
						Socket acc = ssocket.accept();
						Map<String,String> parameters = new HashMap<>();
						parameters.put("aa", "Aaaaaaaaaaaaaaaaaaaaaaaaaa");
						OutputStream os = acc.getOutputStream();
						HTTPOutputStreamClass httpsOS = new HTTPOutputStreamClass(os);	
						httpsOS.writeHttpReply(new HTTPReplyClass("version", "statusCode", "statusMessage", parameters, "data"));
						acc.close();
				      } catch (IOException e) {
						e.printStackTrace();
				      }
				    }
				  }
				).start();
		Socket socket = new Socket("localhost", 4323);
		byte [] mybytearray  = new byte [1000];
	    InputStream is = socket.getInputStream();
	    int a = is.read();
	    while(a != -1) {
	    	result = result + (char)a;
	    	System.out.print((char)a);
	    	a = is.read();
	    }
	    System.out.println(result);
	    ssocket.close();
	}

}