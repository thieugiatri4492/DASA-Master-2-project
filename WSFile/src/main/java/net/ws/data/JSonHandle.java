package net.ws.data;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;



public class JSonHandle {

	
	public JSonHandle(){
		
	}
	
	public void writeJson( String content,String name_PathFile){
		File file = new File(name_PathFile);
		System.out.println(file.getAbsolutePath());
		try (FileOutputStream fop = new FileOutputStream(file)) {
			 
			// if file doesn't exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
 
			// get the content in bytes
			byte[] contentInBytes = content.getBytes();
 
			fop.write(contentInBytes);
			fop.flush();
			fop.close();
 
			System.out.println("Done");
 
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@SuppressWarnings("unchecked")
	public String readJsonToken (String Filename){
		 JSONParser parser = new JSONParser();
		 String token=null;
		 try {
			  
	            Object obj = parser.parse(new FileReader(Filename));
	            JSONObject jsonObject = (JSONObject) obj;
	            token = (String) jsonObject.get("access_token");
	 
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
		 return token;
		 
	}
}
