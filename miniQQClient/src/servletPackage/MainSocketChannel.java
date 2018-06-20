package servletPackage;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class MainSocketChannel {
	SocketChannel socketChannel;
	private static MainSocketChannel mainSocketChannel;
	private static byte[] lock = new byte[1];
	public MainSocketChannel() throws IOException {
		socketChannel = SocketChannel.open();
		socketChannel.connect(new InetSocketAddress("127.0.0.1", 8888));
		socketChannel.configureBlocking(false);
	}
	public static MainSocketChannel getIntance(){
		synchronized (lock) {
			if (mainSocketChannel == null) {
				try{
					mainSocketChannel = new MainSocketChannel();
				}catch (IOException e) {
					e.printStackTrace();
				}
			}
			return mainSocketChannel;
		}
	}
    public void sendMessage(String message){
    	try {
			socketChannel.write(ByteBuffer.wrap(message.getBytes()));
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    public String recieveMessage(){
    	String message = null;
    	ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
		StringBuffer stringBuffer = new StringBuffer();
		int count = 0;
		try {
			while((count = socketChannel.read(byteBuffer))>0){
				stringBuffer.append(new String(byteBuffer.array(),0,count));
			}
			if (stringBuffer.length()>0) {
				if (stringBuffer.toString().equals("RMuHLAS9BOk7GDPaZQN3_close")) {
					socketChannel.socket().close();
					socketChannel.close();
				}else{
					message = stringBuffer.toString();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("服务器端出现了一个错误导致程序关闭");
			try {
				socketChannel.socket().close();
				socketChannel.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				System.out.println("客户端通道关闭失败");
			}
			
		}
		if (message!=null) {
			System.out.println(message);
		}
		return message;
    }
}
