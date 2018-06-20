package show;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import net.sf.json.JSON;
import net.sf.json.JSONObject;
import servletPackage.DrawIoClient;
import servletPackage.DrawIoServer;
import servletPackage.MainSocketChannel;
import servletPackage.SocketIoClient;
import servletPackage.SocketIoServer;

public class Dialogshow {

	String prefixion = "RMuHLAS9BOk7GDPaZQN3_";
	String username;
	String recieveusername;
	MainSocketChannel mainSocketChannel;
	Dialogshow dialogshow;
	JTextArea jTextArea;
	JTextArea tempText;
	String sendmessaged;
	Vector<String> listData;
	List<String> vector;
	int xOld = 0;
	int yOld = 0;
	public Dialogshow(MainSocketChannel mainSocketChannel,String username,String recieveusername,List<String> vector) {
		this.username = username;
		this.recieveusername = recieveusername;
		this.mainSocketChannel = mainSocketChannel;
		this.vector = vector;
		listData = new Vector<>();
		listData.add(recieveusername);
	}
	public void temp(Dialogshow dialogshow){
		this.dialogshow = dialogshow;
	}
	public void show() {
		dialogThread dThread = dialogshow.new dialogThread();
		dThread.start();
		JFrame jFrame = new JFrame();
		ImageIcon dialogImage = new ImageIcon("src/image/dialogImage.png");
		ImageIcon sendMesageImage = new ImageIcon("src/image/sendImage.png");
		ImageIcon addImage = new ImageIcon("src/image/addImage.png");
		ImageIcon IoImage = new ImageIcon("src/image/IoImage.png");
		ImageIcon guessImage = new ImageIcon("src/image/guessImage.png");
		ImageIcon dialogcloseImage = new ImageIcon("src/image/dialogcloseImage.png");
		ImageIcon dialogminImage = new ImageIcon("src/image/dialogminImage.png");
		JLabel background = new JLabel();
		jTextArea = new JTextArea();
		
		JTextArea TextInput = new JTextArea();
	//	tempText = new JTextArea();
		DefaultMutableTreeNode myfriend1 = new DefaultMutableTreeNode("我的好友");
		myfriend1.add(new DefaultMutableTreeNode(recieveusername));
		JTree jTree1 = new JTree(myfriend1);
		
		//JList<String> jList = new JList<>(listData);
		JButton Buttonclose = new JButton();
		JButton Buttonmin = new JButton();
		JButton ButtonIo = new JButton();
		JButton Buttonguess = new JButton();
		JButton Buttonsend = new JButton();
		JButton Buttonadd = new JButton();
		//jFrame.setLayout(null);
		jFrame.setResizable(false);
		jFrame.setUndecorated(true);
		//jPanel.setBounds(30, 30, 400, 300);
		jFrame.setBounds(400, 100, 600, 500);
		Buttonclose.setBounds(570, 0, 30, 30);
		Buttonmin.setBounds(540, 0, 30, 30);
		Buttonsend.setBounds(390, 460, 40, 20);
		Buttonadd.setBounds(450, 465, 130, 30);
		ButtonIo.setBounds(475, 50, 80, 80);
		Buttonguess.setBounds(475, 150, 80, 80);
		jTree1.setBounds(450, 250, 130, 200);
		//LableShow.setBounds(30, 30, 400, 300);
		jTextArea.setBounds(30, 30, 400, 300);
		TextInput.setBounds(30, 350, 400,130 );
		background.setIcon(dialogImage);
		ButtonIo.setIcon(IoImage);
		Buttonguess.setIcon(guessImage);
		Buttonsend.setIcon(sendMesageImage);
		Buttonadd.setIcon(addImage);
		Buttonclose.setIcon(dialogcloseImage);
		Buttonmin.setIcon(dialogminImage);
		jFrame.add(jTextArea);
		jFrame.add(Buttonclose);
		jFrame.add(Buttonmin);
		jFrame.add(Buttonsend);
		jFrame.add(Buttonadd);
		jFrame.add(ButtonIo);
		jFrame.add(Buttonguess);
		jFrame.add(jTree1);
		jFrame.add(TextInput);
		jFrame.add(background);
		Buttonguess.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				synchronized(this){
					JSONObject jsonObject = new JSONObject();
					List<String> list = new ArrayList<>();
					for(int i=0;i<listData.size();i++){
						list.add(listData.get(i));
					}
					jsonObject.put("recieveusername", list);
					mainSocketChannel.sendMessage(prefixion+"isReadyprintandguess_"+jsonObject);
					
				}
			}
		});
		ButtonIo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				synchronized(this){
					if ((sendmessaged = TextInput.getText()) != null) {
						JSONObject jsonObject = new JSONObject();
						List<String> list = new ArrayList<>();
						for(int i=0;i<listData.size();i++){
							list.add(listData.get(i));
						}
						jsonObject.put("recieveusername", list);
						mainSocketChannel.sendMessage(prefixion+"isReadyToRecievefile_"+jsonObject);
					}
				}
				
			}
		});
		Buttonclose.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				jFrame.setVisible(false);
				jFrame.dispose();
			}
		});
		Buttonmin.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				jFrame.setExtendedState(JFrame.ICONIFIED);
			}
		});
		Buttonadd.addActionListener(new ActionListener() {		
			@Override
			public void actionPerformed(ActionEvent e) {
				JFrame jFrameadd = new JFrame();
				//JList listadd = new JList(vector);
				DefaultMutableTreeNode myfriend = new DefaultMutableTreeNode("我的好友");
         		JTree jTree = new JTree(myfriend);
         		for(int i=0;i<vector.size();i++){
         			myfriend.add(new DefaultMutableTreeNode(vector.get(i)));
         		}
                jTree.addMouseListener(new MouseListener() {

					@Override
					public void mouseClicked(MouseEvent arg0) {
						// TODO Auto-generated method stub
						DefaultMutableTreeNode note = (DefaultMutableTreeNode) jTree.getLastSelectedPathComponent();
					    if (note.toString().equals("我的好友")) {
							
						}else{
							listData.add(note.toString());
							myfriend1.add(new DefaultMutableTreeNode(note.toString()));
							System.out.println("已添加");
							jTree1.updateUI();
							jFrameadd.setVisible(false);
							jFrameadd.dispose();
						}
					}

					@Override
					public void mouseEntered(MouseEvent arg0) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void mouseExited(MouseEvent arg0) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void mousePressed(MouseEvent arg0) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void mouseReleased(MouseEvent arg0) {
						// TODO Auto-generated method stub
	
					}
                
                });
			
				
				jFrameadd.setBounds(300, 300, 200, 400);
				jTree.setBounds(20, 20, 160, 360);
				/*jTree.addListSelectionListener(new ListSelectionListener() {
					@Override
					public void valueChanged(ListSelectionEvent e) {
						listData.add((String) .getSelectedValue());
						jFrameadd.setVisible(false);
						jFrameadd.dispose();
					}
				});*/
				jFrameadd.add(jTree);
				//jFrameadd.pack();
				jFrameadd.setVisible(true);
			}
		});
		Buttonsend.addActionListener(new ActionListener() {		
			@Override
			public void actionPerformed(ActionEvent e) {
				synchronized(this){
					if ((sendmessaged = TextInput.getText()) != null) {
						JSONObject jsonObject = new JSONObject();
						List<String> list = new ArrayList<>();
						for(int i=0;i<listData.size();i++){
							list.add(listData.get(i));
						}
						jTextArea.append("用户名"+username+"\r\n"+"  "+TextInput.getText()+"\r\n");
						TextInput.setText("");
						jsonObject.put("recieveusername", list);
						jsonObject.put("sendmessage", sendmessaged);
						mainSocketChannel.sendMessage(prefixion+"sendmessage_"+jsonObject);
					}
				}
				
			}
		});
		jFrame.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {				
				xOld = e.getX();
				yOld = e.getY();
			}			
		});		
		jFrame.addMouseMotionListener(new MouseMotionAdapter() {
			  @Override
			    public void mouseDragged(MouseEvent e) {
			    int xOnScreen = e.getXOnScreen();
			    int yOnScreen = e.getYOnScreen();
			    int xx = xOnScreen - xOld;
			    int yy = yOnScreen - yOld;
			    jFrame.setLocation(xx, yy);//设置拖拽后，窗口的位置
			    }
		});
		jFrame.setVisible(true);
		jTextArea.append("\r\n");

	}
	private class dialogThread extends Thread{
		String message = null;
		boolean judge = true;
		int ss = 1;
		@Override
		public void run() {
			while(true){
				if(ss == 1){
					System.out.println("dialog线程开启");
					ss++;
				}
				message = mainSocketChannel.recieveMessage();
				if (message !=null) {
					if (message.indexOf(prefixion+"recievemessage_")!=-1) {
						String sendusername = message.substring(message.indexOf(prefixion+"receievemessage_")+(prefixion+"receievemessage_").length(),message.lastIndexOf("_"));
						String getmessage = message.substring(message.lastIndexOf("_")+1);
					    synchronized(this){
					    	jTextArea.append("用户名:"+sendusername+"\r\n"+"  "+getmessage+"\r\n");
					    }					    
					}else if(message.indexOf(prefixion+"isReadyToRecievefile_")!=-1){
						String sendusername = message.substring(message.lastIndexOf("_")+1);
						JFrame jFrame = new JFrame();
						jFrame.setLayout(null);
						JLabel jLabel = new JLabel("是否准备好接收来自"+sendusername+"的文件");
						JButton jButton1 = new JButton("是");
						JButton jButton2 = new JButton("否");
						
						jFrame.setBounds(0, 0, 400, 200);
						jButton1.setBounds(10, 72, 50, 30);
						jButton2.setBounds(60, 72, 50, 30);
						jLabel.setBounds(0, 0, 400, 60);
						
						jButton1.addActionListener(new ActionListener() {
							String ip = null;
							@Override
							public void actionPerformed(ActionEvent e) {
								// TODO Auto-generated method stub
								try {
									ip =InetAddress.getLocalHost().getHostAddress();
								} catch (UnknownHostException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
							
								SocketIoServer socketIoServer = new SocketIoServer();
      							mainSocketChannel.sendMessage(prefixion+"isReadygo_"+sendusername+"_"+ip);
      							socketIoServer.run();
							}
						});
						jFrame.add(jLabel);
						jFrame.add(jButton1);
						jFrame.add(jButton2);
						jFrame.setVisible(true);
					}else if(message.indexOf(prefixion+"isReadygo_")!=-1){
						String ip = message.substring(message.lastIndexOf("_")+1);
						SocketIoClient socketIoClient = new SocketIoClient(ip);
						socketIoClient.run();
						System.out.println("IO运行了");
					}else if(message.indexOf(prefixion+"printandguess_")!=-1){
						String ip = message.substring(message.lastIndexOf("_")+1);
						DrawIoClient drawIoClient = new DrawIoClient(ip);
					    try {
							drawIoClient.client();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}else if(message.indexOf(prefixion+"isReadyprintandguess")!=-1){
						String sendusername = message.substring(message.lastIndexOf("_")+1);
						JFrame jFrame = new JFrame();
						jFrame.setLayout(null);
						JLabel jLabel = new JLabel("是否准备进行来自"+sendusername+"你画我猜游戏");
						JButton jButton1 = new JButton("是");
						JButton jButton2 = new JButton("否");
						
						jFrame.setBounds(0, 0, 400, 200);
						jButton1.setBounds(10, 72, 50, 30);
						jButton2.setBounds(60, 72, 50, 30);
						jLabel.setBounds(0, 0, 400, 60);
						
						jButton1.addActionListener(new ActionListener() {
							String ip = null;
							@Override
							public void actionPerformed(ActionEvent e) {
								// TODO Auto-generated method stub
								try {
									ip =InetAddress.getLocalHost().getHostAddress();
									System.out.println(ip);
								} catch (UnknownHostException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
								mainSocketChannel.sendMessage(prefixion+"printandguess_"+sendusername+"_"+ip);
								DrawIoServer drawIoServer = new DrawIoServer();
								try {
									drawIoServer.server();
								} catch (IOException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
							}
						});
						jFrame.add(jLabel);
						jFrame.add(jButton1);
						jFrame.add(jButton2);
						jFrame.setVisible(true);
					}
			    }
		    }
	    }
    } 
}
