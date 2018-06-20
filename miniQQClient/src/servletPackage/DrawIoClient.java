package servletPackage;

import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JFrame;
public class DrawIoClient {
	int xOld = 0;
	int yOld = 0;
	int i = 0;
	byte[] buf;
	OutputStream os;
	StringBuffer sb;
	String ip = null;
	public DrawIoClient(String ip) {
		this.ip = ip;
	}
	public void client() throws IOException {
		Socket socket = new Socket(ip, 8889);
		os = socket.getOutputStream();
		buf = new byte[12];
		sb = new StringBuffer();
		JFrame jFrame = new JFrame();
		jFrame.setBounds(300, 300, 300, 300);
		jFrame.setVisible(true);
		Graphics g = jFrame.getGraphics();
		
		jFrame.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {				
				xOld = e.getX();
				yOld = e.getY();
				if((i=(xOld+""+yOld).getBytes().length)<6){
					sb.append(xOld+","+yOld);
					for(int j=0;j<6-i;j++){
						sb.append(",");
					}
					buf = ("pres,"+sb).toString().getBytes();
				}else{
					buf = ("pres,"+(xOld+","+yOld)).toString().getBytes();
				}
				try {
					sb.setLength(0);
					os.write(buf);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}			
		});
		jFrame.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				buf = "releaseddddd".toString().getBytes();
				try {
					os.write(buf);
				}catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		jFrame.addMouseMotionListener(new MouseMotionAdapter() {
			  @Override
			    public void mouseDragged(MouseEvent e) {
			    int xx=e.getX();
			    int yy=e.getY();
			    System.out.println("xx:"+xx+",yy:"+yy);
			    g.drawLine(xOld, yOld, xx, yy);
			    xOld = xx;
			    yOld = yy;
				if((i=(xx+""+yy).getBytes().length)<6){
					sb.append(xx+","+yy);
					for(int j=0;j<6-i;j++){
						sb.append(",");
					}
					buf = ("move,"+sb).toString().getBytes();
				}else{
					buf = ("move,"+(xx+","+yy)).toString().getBytes();
				}
				try {
					sb.setLength(0);
					os.write(buf);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			    }
		});
	    jFrame.addWindowListener(new WindowListener() {
			
			@Override
			public void windowOpened(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowIconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowDeiconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowDeactivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				buf = "close1111111".toString().getBytes();
				try {
					os.write(buf);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			
			@Override
			public void windowClosed(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowActivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
	}
}
