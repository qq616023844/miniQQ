package servletPackage;

import javax.swing.JFileChooser;

import net.sf.json.JSON;
import net.sf.json.JSONFunction;
import net.sf.json.JSONObject;

public class Filechoose {

	public static JSONObject getfilepath() {
		 JFileChooser fileChooser = new JFileChooser("D:\\");
		 int returnVal = fileChooser.showOpenDialog(fileChooser);
		 if(returnVal == JFileChooser.APPROVE_OPTION){   
			 String filePath= fileChooser.getSelectedFile().getAbsolutePath();
		     String filename = filePath.substring(filePath.lastIndexOf("\\")+1);
			 String filetype = filePath.substring(filePath.lastIndexOf(".")+1);
			 JSONObject jsonfile = new JSONObject();
             jsonfile.put("filePath", filePath);
        	 jsonfile.put("filename", filename);
        	 jsonfile.put("filetype", filetype);
        	 return jsonfile;
		 }
		 return null;
	}
	public static String setfilepath(){
		 JFileChooser fileChooser = new JFileChooser("D:\\");
		 fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		 int returnVal = fileChooser.showOpenDialog(fileChooser);
		 if(returnVal == JFileChooser.APPROVE_OPTION){       
			 String filePath= fileChooser.getSelectedFile().getAbsolutePath();
	         return filePath;
	     }
		 return null;
	}	 
}
