package show;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import servletPackage.MainSocketChannel;

public class FriendListPage {
	static boolean FriendListjudge = true;
	final String prefixion="RMuHLAS9BOk7GDPaZQN3_";
	MainSocketChannel mainSocketChannel;
	FriendListPage friendListPage;
	String username;
	int xOld = 0;
	int yOld = 0;
	JFrame jFramefriendlist;
	JLabel background;
	public FriendListPage(MainSocketChannel mainSocketChannel,String username) {
		this.mainSocketChannel = mainSocketChannel;
		this.username = username;
	}
	public void temp(FriendListPage friendListPage){
		this.friendListPage = friendListPage;
	}
	public void show() {
		friendlistThread fThread = friendListPage.new friendlistThread();
		fThread.start();
		
		jFramefriendlist = new JFrame();
		JLabel jLabelusername = new JLabel("你好:"+username);
		background = new JLabel();
		JButton Buttonclose = new JButton();
		JButton Buttonflush = new JButton("刷新");
		JButton Buttonaddfriend = new JButton("添加好友");
		ImageIcon closeImage = new ImageIcon("src/image/closeImage1.png");
		ImageIcon friendlistImage = new ImageIcon("src/image/friendlistImage.png");
		
		jFramefriendlist.setBounds(1060,0,300,700);
		jFramefriendlist.setLayout(null);
		jFramefriendlist.setResizable(false);
		jFramefriendlist.setUndecorated(true);
		
		Buttonaddfriend.setBounds(100, 640, 200, 50);
		Buttonflush.setBounds(0, 0, 60, 40);
		Buttonclose.setIcon(closeImage);
		Buttonclose.setBounds(270,0,30,30);
		background.setIcon(friendlistImage);
		background.setSize(300,700);
		
		Buttonaddfriend.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				JFrame jFrame2 = new JFrame();
				jFrame2.setLayout(null);
				JTextArea jTextArea2 = new JTextArea();
				JLabel jLabel2 = new JLabel("请输入添加的好友账号");
				JButton jButton3 = new JButton("添加");
				
				jTextArea2.setBounds(20, 150, 200, 40);
				jLabel2.setBounds(20, 20, 340, 40);
				jFrame2.setBounds(300, 300, 400, 400);
				jButton3.setBounds(20, 210, 340, 40);
				
				jButton3.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						String addfriendname = jTextArea2.getText();
						String addfriendname1 = prefixion+"addfriend_"+addfriendname;
						mainSocketChannel.sendMessage(addfriendname1);
					}
				});
				
				jFrame2.add(jLabel2);
				jFrame2.add(jTextArea2);
				jFrame2.add(jButton3);
				jFrame2.setVisible(true);
			}
		});
		Buttonflush.addActionListener(new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent e) {
				FriendListjudge = true;
				mainSocketChannel.sendMessage(prefixion+"getfriendlist_");
				
			}
		});
		Buttonclose.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				try {
				    mainSocketChannel.sendMessage(prefixion+"exit_");
					wait(1000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}finally {
					System.exit(0);
				}
			}
		});
		jFramefriendlist.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {				
				xOld = e.getX();
				yOld = e.getY();
			}			
		});		
		jFramefriendlist.addMouseMotionListener(new MouseMotionAdapter() {
			  @Override
			    public void mouseDragged(MouseEvent e) {
			    int xOnScreen = e.getXOnScreen();
			    int yOnScreen = e.getYOnScreen();
			    int xx = xOnScreen - xOld;
			    int yy = yOnScreen - yOld;
			    jFramefriendlist.setLocation(xx, yy);//设置拖拽后，窗口的位置
			    }
		});
		jFramefriendlist.add(Buttonclose);
		jFramefriendlist.add(Buttonflush);
		jFramefriendlist.add(Buttonaddfriend);
		mainSocketChannel.sendMessage(prefixion+"getfriendlist_");
	//	FriendListjudge = true;
	}
	
	private class friendlistThread extends Thread{
		Vector<String> vector;
		String message = null;
		@Override
		public void run() {
			//while(true){
			while(FriendListjudge){
				message = mainSocketChannel.recieveMessage();
				if (message !=null) {
					if (message.indexOf(prefixion+"friendlist_")!=-1) {
						
						System.out.println("已接到好友列表");
						String friendstring = message.substring(message.indexOf(prefixion+"friendlist_")+(prefixion+"friendlist_").length());
                        List<String> friendlist = new ArrayList<>();
						try {
                            String name;                          
                            friendstring = friendstring.replaceAll("\\[\"","");
                            friendstring = friendstring.replaceAll("\"\\]","");
                            String[] friendlisttemp = friendstring.split("\",\"");                            
                            for(int a=0;a<friendlisttemp.length;a++){
                            	friendlist.add(friendlisttemp[a]);                            	
                            }                                               					
                            DefaultMutableTreeNode myfriend = new DefaultMutableTreeNode("我的好友");
                    		JTree jTree = new JTree(myfriend);
                            jTree.addMouseListener(new MouseListener() {
								
								@Override
								public void mouseReleased(MouseEvent e) {
									// TODO Auto-generated method stub
									
								}
								
								@Override
								public void mousePressed(MouseEvent e) {
									// TODO Auto-generated method stub
									
								}
								
								@Override
								public void mouseExited(MouseEvent e) {
									// TODO Auto-generated method stub
									
								}
								
								@Override
								public void mouseEntered(MouseEvent e) {
									// TODO Auto-generated method stub
									
								}
								
								@Override
								public void mouseClicked(MouseEvent e) {
									// TODO Auto-generated method stub
									DefaultMutableTreeNode note = (DefaultMutableTreeNode) jTree.getLastSelectedPathComponent();
									if (note.toString().equals("我的好友")) {
										
									}else{
										FriendListjudge = false;
										String recieveusername = note.toString();
										Dialogshow dialogshow = new Dialogshow(mainSocketChannel,username,recieveusername,friendlist);
										dialogshow.temp(dialogshow);
										dialogshow.show();
										jFramefriendlist.repaint();
										FriendListjudge = false;
										System.out.println("dialogshow类已开启");
										
									}
								}
							});

							for(int i=0;i<friendlist.size();i++){
								myfriend.add(new DefaultMutableTreeNode(friendlist.get(i)));
							}

							jTree.setBounds(50, 100, 100, 500);
							jFramefriendlist.add(jTree);
							jFramefriendlist.add(background);
							jFramefriendlist.setVisible(true);
						} catch (net.sf.json.JSONException e) {
							e.printStackTrace();
						}
                        
					}
				/*	if (message.indexOf(prefixion+"addfriend_true")!=-1) {
						JFrame jFrame4 = new JFrame();
						JLabel jLabel4 = new JLabel("添加成功");
						jFrame4.setBounds(300, 300, 300, 300);
						jLabel4.setBounds(0, 0, 200, 200);
						jFrame4.add(jLabel4);
						jFrame4.setVisible(true);
						mainSocketChannel.sendMessage(prefixion+"getfriendlist_");
						FriendListjudge = true;
					}
					if (message.indexOf(prefixion+"addfriend_false")!=-1) {
						JFrame jFrame4 = new JFrame();
						JLabel jLabel4 = new JLabel("添加失败");
						jFrame4.setBounds(300, 300, 300, 300);
						jLabel4.setBounds(0, 0, 200, 200);
						jFrame4.add(jLabel4);
						jFrame4.setVisible(true);
						mainSocketChannel.sendMessage(prefixion+"getfriendlist_");
						FriendListjudge = true;
					}*/
				}			
			}
		}
		//}
	}
}
