package servletPackage;

import java.awt.Graphics;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JFrame;

public class DrawIoServer {
	byte[] buf;
	int i ;
	public void server() throws IOException {
		System.out.println("DrawIoServer∆Ù∂Ø¡À");
		ServerSocket serverSocket = new ServerSocket(8889);
		Socket socket = serverSocket.accept();
		JFrame jFrame = new JFrame();
		jFrame.setBounds(300, 300, 300, 300);
		jFrame.setVisible(true);
		Graphics g = jFrame.getGraphics();
		InputStream is = socket.getInputStream();
		InputStreamReader isr = new InputStreamReader(is);
		buf = new byte[12];
		int xOld = 0;
		int yOld = 0;
		int xx = 0;
		int yy = 0;
        StringBuffer sb = new StringBuffer() ;
        String a;

        while((i=is.read(buf))!=-1){
        	a = new String(buf);
        	sb.append(a);
        	if (sb.length()==12) {
				String[] as = a.split(",");
				if (as[0].equals("pres")) {
					xOld = Integer.parseInt(as[1]);
					yOld = Integer.parseInt(as[2]);
					sb.setLength(0);
				}
				if (as[0].equals("move")) {
					xx = Integer.parseInt(as[1]);
					yy = Integer.parseInt(as[2]);
					sb.setLength(0);
					g.drawLine(xOld, yOld, xx, yy);
					xOld = xx;
					yOld = yy;
				}
				if (as[0].equals("releaseddddd")) {
					xOld = 0 ;
					yOld = 0 ;
					xx = 0;
					yy = 0;
					sb.setLength(0);
				}
				if (as[0].equals("close1111111")) {
					break;
				}
			}
        }
	}

}
