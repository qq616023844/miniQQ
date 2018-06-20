package serverServletPackage;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.sf.json.JSON;
import net.sf.json.JSONException;
import utilPackage.CommunicateMySQL;
import utilPackage.ConnectionMySQL;

public class MainServerSocketChannel implements Runnable{
	Selector selector;
	SelectionKey selectionKey;
	Connection conn;
	CommunicateMySQL communicateMySQL;
	final String prefixion="RMuHLAS9BOk7GDPaZQN3_";
	public MainServerSocketChannel(Connection conn,CommunicateMySQL communicateMySQL) {
		this.conn = conn;
		this.communicateMySQL = communicateMySQL;
	}
	private void init() throws IOException {
		selector = Selector.open();
		ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
		ServerSocket serverSocket = serverSocketChannel.socket();
		serverSocket.bind(new InetSocketAddress(8888));
		serverSocketChannel.configureBlocking(false);
	    selectionKey = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
	}
	SelectionKey key;
	@Override
	public void run() {
		try {
			while(true){
				int count = selector.select();
				if (count > 0) {
					Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
					while(iterator.hasNext()){
						key = iterator.next();
						if (key.isAcceptable()) {
							ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
							SocketChannel socketChannel = serverSocketChannel.accept();
							socketChannel.configureBlocking(false);
							socketChannel.register(selector, SelectionKey.OP_READ);
							iterator.remove();
							System.out.println("成功收到连接请求");
						}else if (key.isValid()&&key.isReadable()) {							
									try {
										readMessage(key);
									} catch (SQLException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (JSONException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}						
						}else if (key.isValid()&&key.isWritable()) {
							writeMessage(key);
						}
					}
				}
			}
		} catch (IOException e) {			
			e.printStackTrace();
		}
	}
	private void readMessage(SelectionKey key) throws SQLException, net.sf.json.JSONException, IOException {
		SocketChannel socketChannel = null;
		socketChannel = (SocketChannel) key.channel();
		ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
		StringBuffer stringBuffer = new StringBuffer();
		try {
			int count = socketChannel.read(byteBuffer);
			if(count>0){
				byteBuffer.flip();
		    	stringBuffer.append(new String(byteBuffer.array(),0,count));
			}
			String message = stringBuffer.toString();
			if (message!=null) {
				System.out.println(message);
			}
			if (message.indexOf(prefixion+"sendmessage_")!=-1) {
				try {
					System.out.println("消息发送中");
					String temp = message.substring(message.indexOf(prefixion+"sendmessage_")+(prefixion+"sendmessage_").length());
					net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject.fromObject(temp);
					List<String> list = new ArrayList<>();
					list = (List<String>) jsonObject.get("recieveusername");
					String sendmessage = (String) jsonObject.get("sendmessage");
					String sendusername = (String) key.attachment();
					for (int i = 0; i < list.size(); i++) {
						Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
						while(iterator.hasNext()){
							SelectionKey skey = iterator.next();					
							if (skey.attachment().equals(list.get(i))) {
								System.out.println("消息已发送给"+skey.attachment());
								socketChannel = (SocketChannel) skey.channel();
								socketChannel.write(ByteBuffer.wrap((prefixion+"recievemessage_"+sendusername+"_"+sendmessage).getBytes()));
								byteBuffer.clear();
							}
						}
					}
				} catch (net.sf.json.JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else if (message.indexOf(prefixion+"register_")!=-1) {
				String username = message.substring(message.indexOf(prefixion+"register_")+(prefixion+"register_").length(),message.lastIndexOf("_"));
				String password = message.substring(message.lastIndexOf("_")+1);
				boolean judgement = communicateMySQL.register(conn, username, password);
				if (judgement) {				
					socketChannel.write(ByteBuffer.wrap((prefixion+"register_true").getBytes()));
				}else{
					socketChannel.write(ByteBuffer.wrap((prefixion+"register_false").getBytes()));
				}
				byteBuffer.clear();
			}else if (message.indexOf(prefixion+"addfriend_")!=-1) {
				System.out.println("message:"+message);
				String addfriend = message.substring(message.indexOf(prefixion+"addfriend_")+(prefixion+"addfriend_").length());
				String username = (String) key.attachment();
			    boolean judgement = communicateMySQL.addfriend(conn, username, addfriend);
			    if (judgement) {
					socketChannel.write(ByteBuffer.wrap((prefixion+"addfriend_true").getBytes()));
				}else{
					socketChannel.write(ByteBuffer.wrap((prefixion+"addfriend_false").getBytes()));
				}
			    byteBuffer.clear();

			}else if ((message.indexOf(prefixion+"login_"))!=-1) {
				String username = message.substring(message.indexOf(prefixion+"login_")+(prefixion+"login_").length(),message.lastIndexOf("_"));
				String password = message.substring(message.lastIndexOf("_")+1);
				boolean judgement = communicateMySQL.login(conn, username, password);
				if (judgement) {				
					socketChannel.write(ByteBuffer.wrap((prefixion+"login_true").getBytes()));
					key.attach(username);
					
				}else{
					socketChannel.write(ByteBuffer.wrap((prefixion+"login_false").getBytes()));
				}
				byteBuffer.clear();

			}else if (message.indexOf(prefixion+"exit_")!=-1) {
				
			}else if (message.indexOf(prefixion+"getfriendlist_")!=-1) {
				String username = (String) key.attachment();
				ResultSet resultSet = communicateMySQL.getfriendlist(conn, username);
				List list = new ArrayList();
				while(resultSet.next()){
					list.add(resultSet.getString("friendname"));
				}
				net.sf.json.JSONArray jsonArray = net.sf.json.JSONArray.fromObject(list);
				socketChannel.write(ByteBuffer.wrap((prefixion+"friendlist_"+jsonArray).getBytes()));
				byteBuffer.clear();				
			}else if(message.indexOf(prefixion+"isReadyToRecievefile_")!=-1){
				String temp = message.substring(message.indexOf(prefixion+"isReadyToRecievefile_")+(prefixion+"isReadyToRecievefile_").length());
				net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject.fromObject(temp);
				List<String> list = new ArrayList<>();
				list = (List<String>) jsonObject.get("recieveusername");
				String sendusername = (String) key.attachment();
				for (int i = 0; i < list.size(); i++) {
					Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
					while(iterator.hasNext()){
						SelectionKey skey = iterator.next();						
						if (skey.attachment().equals(list.get(i))) {
							socketChannel = (SocketChannel) skey.channel();
							socketChannel.write(ByteBuffer.wrap((prefixion+"isReadyToRecievefile_"+sendusername).getBytes()));
							byteBuffer.clear();
						}
					}
				}
			}else if(message.indexOf(prefixion+"isReadygo_")!=-1){
				String sendusername = message.substring(message.indexOf(prefixion+"isReadygo_")+(prefixion+"isReadygo_").length(),message.lastIndexOf("_"));
				String ip = message.substring(message.lastIndexOf("_")+1);
				Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
				while(iterator.hasNext()){
					SelectionKey skey = iterator.next();
					if (skey.attachment().equals(sendusername)) {
						socketChannel = (SocketChannel) skey.channel();
						
						socketChannel.write(ByteBuffer.wrap((prefixion+"isReadygo_"+ip).getBytes()));
						byteBuffer.clear();
					}
				}
			}else if(message.indexOf(prefixion+"isReadyprintandguess_")!=-1){
				String temp = message.substring(message.indexOf(prefixion+"isReadyprintandguess_")+(prefixion+"isReadyprintandguess_").length());
				net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject.fromObject(temp);
				List<String> list = new ArrayList<>();
				list = (List<String>) jsonObject.get("recieveusername");
				String sendusername = (String) key.attachment();
				for (int i = 0; i < list.size(); i++) {
					Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
					while(iterator.hasNext()){
						SelectionKey skey = iterator.next();						
						if (skey.attachment().equals(list.get(i))) {
							socketChannel = (SocketChannel) skey.channel();
							socketChannel.write(ByteBuffer.wrap((prefixion+"isReadyprintandguess_"+sendusername).getBytes()));
							byteBuffer.clear();
						}
					}
				}
			}else if(message.indexOf(prefixion+"printandguess_")!=-1){
				String ip = message.substring(message.lastIndexOf("_")+1);
				String sendusername = message.substring(message.indexOf(prefixion+"printandguess_")+(prefixion+"printandguess_").length(),message.lastIndexOf("_"));
				System.out.println("sendusername"+sendusername);
				Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
				while(iterator.hasNext()){
					SelectionKey skey = iterator.next();						
					if (skey.attachment().equals(sendusername)) {
						socketChannel = (SocketChannel) skey.channel();
						socketChannel.write(ByteBuffer.wrap((prefixion+"printandguess_"+ip).getBytes()));
						System.out.println("printandguess已发送");
						byteBuffer.clear();
					}
				}
			}else{
				System.out.println("存在未知消息");
			}
			
			Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
			while(iterator.hasNext()){
				SelectionKey skey = iterator.next();
				if (skey != selectionKey) {
					skey.interestOps(skey.interestOps()|SelectionKey.OP_WRITE);
				}
			}
		} catch (IOException e) {
			//socketChannel.close();
			e.printStackTrace();
		}
	}
	private void writeMessage(SelectionKey key) {
		// TODO Auto-generated method stub

		Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
		while(iterator.hasNext()){
			SelectionKey skey = iterator.next();
			if (skey != selectionKey) {
				skey.interestOps(skey.interestOps()|SelectionKey.OP_READ);
			}
		}
	}
	public static void main(String[] args) throws IOException {
		ConnectionMySQL connectionMySQL = new ConnectionMySQL();
		Connection conn = connectionMySQL.ComMySQL();
		CommunicateMySQL communicateMySQL = new CommunicateMySQL();
		MainServerSocketChannel mainServerSocketChannel = new MainServerSocketChannel(conn,communicateMySQL);
		mainServerSocketChannel.init();
		new Thread(mainServerSocketChannel).start();
	}
}
