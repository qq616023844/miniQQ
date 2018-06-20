package servletPackage;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import net.sf.json.JSON;
import net.sf.json.JSONObject;

public class SocketIoClient extends Thread{

	String ip = null;
	public SocketIoClient(String ip) {
		this.ip = ip;
	}
	@Override
	public void run() {
		try {
			Socket socket=new Socket(ip,8884);
			OutputStream os=socket.getOutputStream();
			BufferedOutputStream bfo=new BufferedOutputStream(os);
			DataOutputStream dos=new DataOutputStream(os);
			
			JSONObject fileinformation = Filechoose.getfilepath();
		//	JSONObject jsonfile = new JSONObject().fromObject(fileinformation);
			/*String filename = jsonfile.getString("filename");
			String filetype = jsonfile.getString("filetype");
			String filePath = jsonfile.getString("filePath");*/
			String filename = fileinformation.getString("filename");
			String filetype = fileinformation.getString("filetype");
			String filePath = fileinformation.getString("filePath");
			System.out.println("filename:"+filename+"filetype:"+filetype+"filepath:"+filePath);
			byte[] buf = new byte[1024];
			dos.writeUTF(filename+"_"+filetype);
			dos.flush();	
			FileInputStream fis=new FileInputStream(filePath);
			BufferedInputStream bis=new BufferedInputStream(fis);
			DataInputStream dis=new DataInputStream(bis);
			int bytes;
			int whilenumber=0;
			System.out.println("ÕýÔÚ·¢ËÍ");
			while((bytes=dis.read(buf))!=-1){
				dos.write(buf,0,bytes);
				dos.flush();
				whilenumber++;
			}
			System.out.println("send over");

			if (bytes==-1) {
				socket.shutdownOutput();
			}
			dis.close();
			fis.close();
			dos.close();
			os.close();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}