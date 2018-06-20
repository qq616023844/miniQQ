package show;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import servletPackage.MainSocketChannel;
public class LoginShow {
	int xOld = 0;
	int yOld = 0;
	JFrame jFrameLogin;
	boolean loginshowjudge = false;
	final String prefixion="RMuHLAS9BOk7GDPaZQN3_";
	MainSocketChannel mainSocketChannel;
	String usernameANDpassword;
	String username = null;
	public LoginShow(MainSocketChannel mainSocketChannel) {
		this.mainSocketChannel = mainSocketChannel;
	}
	public void show() {
		jFrameLogin = new JFrame();
		ImageIcon LoginImage = new ImageIcon("src/image/LoginImage.jpg");
		ImageIcon ButtonLoginImage = new ImageIcon("src/image/ButtonLogin.png");
		ImageIcon ButtonRegisterImage = new ImageIcon("src/image/ButtonRegister.png");
		ImageIcon closeImage = new ImageIcon("src/image/closeImage.png");
		JLabel background = new JLabel(LoginImage);
		JButton Buttonlogin = new JButton("µÇÂ½");
		JButton ButtonRegister = new JButton("×¢²á");
		JButton Buttonclose = new JButton("¹Ø±Õ");
		JLabel Lableusername = new JLabel("ÕËºÅ");
		JLabel Lablepassword = new JLabel("ÃÜÂë");		
		JTextArea Textusername = new JTextArea();
		JTextArea Textpassword = new JTextArea();
		
		background.setSize(435, 320);
		jFrameLogin.setLayout(null);
		jFrameLogin.setResizable(false);
		jFrameLogin.setUndecorated(true);
		jFrameLogin.setSize(435, 320);
		jFrameLogin.getContentPane().setBackground(Color.gray);
		Textusername.setFont(new Font(Font.DIALOG_INPUT, Font.BOLD, 20));
		Textpassword.setFont(new Font(Font.DIALOG_INPUT, Font.BOLD, 20));
		Textusername.setBounds(130,160,180,24);
		Textpassword.setBounds(130,210,180,24);
		Lableusername.setBounds(95, 160, 40, 30);
		Lablepassword.setBounds(95, 210, 40, 30);
		Lableusername.setForeground(Color.white);
		Lablepassword.setForeground(Color.white);
		Buttonlogin.setBounds(90,270,120, 30);
		Buttonlogin.setIcon(ButtonLoginImage);
		ButtonRegister.setBounds(220, 270, 120, 30);
		ButtonRegister.setIcon(ButtonRegisterImage);
		Buttonclose.setBounds(417, 0, 35, 28);
		Buttonclose.setIcon(closeImage);
		
		
        Buttonclose.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
				    mainSocketChannel.sendMessage(prefixion+"exit_");
					wait(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally {
					System.exit(0);					
				}
			}
		});
        Buttonlogin.addActionListener(new ActionListener() {		
			@Override
			public void actionPerformed(ActionEvent e) {
				username = Textusername.getText();
				usernameANDpassword = prefixion+"login_"+Textusername.getText()+"_"+Textpassword.getText();
			    mainSocketChannel.sendMessage(usernameANDpassword);
			    loginshowjudge = true;
			}
		});
        ButtonRegister.addActionListener(new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent e) {
				usernameANDpassword = prefixion+"register_"+Textusername.getText()+"_"+Textpassword.getText();
			    mainSocketChannel.sendMessage(usernameANDpassword);
			    loginshowjudge = true;
			}
		});
		jFrameLogin.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {				
				xOld = e.getX();
				yOld = e.getY();
			}			
		});		
		jFrameLogin.addMouseMotionListener(new MouseMotionAdapter() {
			  @Override
			    public void mouseDragged(MouseEvent e) {
			    int xOnScreen = e.getXOnScreen();
			    int yOnScreen = e.getYOnScreen();
			    int xx = xOnScreen - xOld;
			    int yy = yOnScreen - yOld;
			    jFrameLogin.setLocation(xx, yy);//ÉèÖÃÍÏ×§ºó£¬´°¿ÚµÄÎ»ÖÃ
			    }
		});

		jFrameLogin.add(Buttonlogin);
		jFrameLogin.add(ButtonRegister);
		jFrameLogin.add(Buttonclose);
		jFrameLogin.add(Lableusername);
		jFrameLogin.add(Lablepassword);
		jFrameLogin.add(Textusername);
		jFrameLogin.add(Textpassword);
		jFrameLogin.add(background);
		loginshowjudge = true;
		jFrameLogin.setVisible(true);
	}
	
	private class JudgeThread extends Thread{
		String message = null;
		boolean loginshowjudge = true;
		@Override
		public void run() {
			while(loginshowjudge){             
				message = mainSocketChannel.recieveMessage();
				if (message != null) {
					if (message.equals(prefixion+"login_true")) {
						FriendListPage friendListPage = new FriendListPage(mainSocketChannel,username);
						friendListPage.temp(friendListPage);
						friendListPage.show();			
						loginshowjudge = false;
						jFrameLogin.setVisible(false);
						jFrameLogin.dispose();
					}else if(message.equals(prefixion+"login_false")){
						JFrame jFrameLoginfalse = new JFrame();
						JLabel jLabel = new JLabel("µÇÂ½Ê§°Ü");
						jFrameLoginfalse.setLocationRelativeTo(null);
						jFrameLoginfalse.setSize(200, 200);
						jLabel.setSize(200,200);
						jLabel.setFont(new Font(Font.DIALOG_INPUT, Font.BOLD, 30));
						jFrameLoginfalse.add(jLabel);
						jFrameLoginfalse.setVisible(true);
					}else if(message.equals(prefixion+"register_true")){
						JFrame jFrameRegistertrue = new JFrame();
						JLabel jLabel = new JLabel("×¢²á³É¹¦");
						jFrameRegistertrue.setLocationRelativeTo(null);
						jFrameRegistertrue.setSize(200, 200);
						jLabel.setSize(200,200);
						jLabel.setFont(new Font(Font.DIALOG_INPUT, Font.BOLD, 30));
						jFrameRegistertrue.add(jLabel);
						jFrameRegistertrue.setVisible(true);
					}else if(message.equals(prefixion+"register_false")){
						JFrame jFrameRegisterfalse = new JFrame();
						JLabel jLabel = new JLabel("×¢²áÊ§°Ü");
						jFrameRegisterfalse.setSize(200, 200);
						jLabel.setSize(200,200);
						jLabel.setFont(new Font(Font.DIALOG_INPUT, Font.BOLD, 30));
						jFrameRegisterfalse.add(jLabel);
						jFrameRegisterfalse.setVisible(true);
					}
				}
			}
		}
	}
	public static void main(String[] args) {
		MainSocketChannel mainSocketChannel = MainSocketChannel.getIntance();
		LoginShow loginShow = new LoginShow(mainSocketChannel);
		JudgeThread judgeThread = loginShow.new JudgeThread();
		judgeThread.start();
		loginShow.show();
	}
}