package servletPackage;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketIoServer extends Thread{

	@Override
	public void run() {
		try {
			ServerSocket serversocket=new ServerSocket(8884);	
			Socket socket=serversocket.accept();
			
			InputStream is=socket.getInputStream();
			BufferedInputStream bis=new BufferedInputStream(is);
			DataInputStream dis=new DataInputStream(is);
			String filename = null;
			String filetype = null;
			
			byte[] buf=new byte[1024];
			int bytes=0;
			int whilenumber=1;
			
			String fileinformation = dis.readUTF();
			System.out.println(fileinformation);
			String filePath = Filechoose.setfilepath()+"\\";
			filename = fileinformation.substring(0, fileinformation.indexOf("_"));
			filetype = fileinformation.substring(fileinformation.indexOf("_")+1);
			System.out.println("filename:"+filename+"filetype:"+filetype);
            System.out.println("filePath"+filePath);
			FileOutputStream fo=new FileOutputStream(filePath+filename);
			BufferedOutputStream bfo=new BufferedOutputStream(fo);
			DataOutputStream dos=new DataOutputStream(bfo);
			
			System.out.println("开始接收");
			while((bytes=dis.read(buf))!=-1){
				dos.write(buf,0,bytes);  	
				whilenumber++;
			}
			dos.flush();
			System.out.println("传输完毕");
			if (bytes==-1) {
				socket.shutdownInput();
			}
			dos.close();
			fo.close();
			dis.close();
			is.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
