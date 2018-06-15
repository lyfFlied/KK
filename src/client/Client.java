package client;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import javax.swing.JLabel;

import java.awt.Font;

import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextArea;

import java.awt.Color;

import javax.swing.border.BevelBorder;

import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JButton;

import message.Message.FieldType;
import message.Message.Commands;
import message.Message;
import message.MessageDaolmpl;
import User.User;
import User.UserDaoImpl;
import javax.swing.UIManager;
import java.awt.Toolkit;
/**
 * 
 * @Title: Client.java 
 * @Package client 
 * @Description: TODO(客户端)    
 * @date 2018年6月11日 上午11:20:25 
 * @version V1.0   
 * @author 李云飞
 */
public class Client extends JFrame {
	private static final long serialVersionUID = 1L;
	private ClientTool tool;
	private JFrame frame;
	private JPanel contentPane;
	JLabel lblNickName;//昵称显示
	JLabel lblSchoolNumber ;//账号显示
	private JTextField PrivateChatNumberText;//私聊账号输入框
	ArrayList<JButton> JBUserList;//用户列表
	JPanel panelOnlineUsers;//在线用户列表
	JTextArea textAreaNews;//消息展示框
	JTextArea SendText;//消息发送框
	private MessageThread messageThread;// 负责接收消息的线程
	private String myUserId;
	private static int port = 6666;//端口号
	private static String hostIp = "127.0.0.1";//IP地址
	private  User user;
	Socket socket;
	PrintWriter writer;
	BufferedReader reader;
	OutputStream outputStream;//字节输入流
	ObjectOutputStream objectOutputStream;
	InputStream inputStream;
	ObjectInputStream objectInputStream;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					User user = new User();
					user.setNickname("Admin");
					user.setSchoolnumber("6665");
					Client frame = new Client(user);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * 创建界面的构造方法
	 */
	public Client(final User user) {
		setIconImage(Toolkit.getDefaultToolkit().getImage(Client.class.getResource("/User/login.jpg")));
		this.user=user;
		setTitle("实时聊天客户端");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 854, 741);
		contentPane = new JPanel();
		contentPane.setBackground(SystemColor.info);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JPanel panel = new JPanel();
		panel.setForeground(Color.RED);
		panel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panel.setBackground(Color.LIGHT_GRAY);
		panel.setBounds(0, 35, 109, 659);
		contentPane.add(panel);
		panel.setLayout(null);

		JLabel lblNewLabel = new JLabel("在线用户");
		lblNewLabel.setBackground(Color.LIGHT_GRAY);
		lblNewLabel.setBounds(10, 5, 80, 24);
		lblNewLabel.setFont(new Font("宋体", Font.PLAIN, 20));
		panel.add(lblNewLabel);

		panelOnlineUsers = new JPanel();
		panelOnlineUsers.setBounds(0, 30, 109, 629);
		panel.add(panelOnlineUsers);

		JPanel panel_1 = new JPanel();
		panel_1.setBackground(UIManager.getColor("Button.background"));
		panel_1.setBounds(0, 0, 840, 36);
		contentPane.add(panel_1);
		panel_1.setLayout(null);

		JLabel lblNewLabel_1 = new JLabel("昵称：");
		lblNewLabel_1.setFont(new Font("宋体", Font.PLAIN, 18));
		lblNewLabel_1.setBounds(14, 13, 56, 18);
		panel_1.add(lblNewLabel_1);

		JLabel lblNewLabel_2 = new JLabel("学号：");
		lblNewLabel_2.setFont(new Font("宋体", Font.PLAIN, 18));
		lblNewLabel_2.setBounds(188, 15, 63, 18);
		panel_1.add(lblNewLabel_2);

		lblNickName = new JLabel("");
		//根据用户显示昵称
		lblNickName.setText(user.getNickname());
		lblNickName.setBounds(84, 15, 72, 18);
		panel_1.add(lblNickName);

		lblSchoolNumber = new JLabel("");
		//根据用户显示学号
		lblSchoolNumber.setText(user.getSchoolnumber());
		lblSchoolNumber.setBounds(252, 15, 72, 18);
		panel_1.add(lblSchoolNumber);

		JLabel lblNewLabel_7 = new JLabel("个人资料");
		lblNewLabel_7.setBounds(675, 15, 72, 18);
		panel_1.add(lblNewLabel_7);

		JLabel lblNewLabel_8 = new JLabel("修改密码");
		lblNewLabel_8.setBounds(754, 15, 72, 18);
		panel_1.add(lblNewLabel_8);

		JPanel panel_2 = new JPanel();
		panel_2.setBackground(SystemColor.inactiveCaption);
		panel_2.setBounds(112, 35, 728, 522);
		contentPane.add(panel_2);
		panel_2.setLayout(null);

		JLabel lblNewLabel_3 = new JLabel("消息显示");
		lblNewLabel_3.setBounds(328, 5, 72, 18);
		panel_2.add(lblNewLabel_3);

		textAreaNews = new JTextArea();
		textAreaNews.setBounds(91, 82, 573, 367);
		//添加滚动条

		panel_2.add(textAreaNews);

		JScrollPane scrollPane_1 = new JScrollPane(textAreaNews);
		scrollPane_1.setBounds(14, 37, 700, 472);
		panel_2.add(scrollPane_1);

		JPanel panel_3 = new JPanel();
		panel_3.setBounds(112, 556, 628, 138);
		contentPane.add(panel_3);
		panel_3.setLayout(null);

		JLabel lblNewLabel_4 = new JLabel("写消息");
		lblNewLabel_4.setFont(new Font("黑体", Font.PLAIN, 20));
		lblNewLabel_4.setBounds(334, 0, 72, 18);
		panel_3.add(lblNewLabel_4);

		JLabel lblNewLabel_5 = new JLabel("账号：");
		lblNewLabel_5.setBounds(49, 23, 45, 18);
		panel_3.add(lblNewLabel_5);

		PrivateChatNumberText = new JTextField();
		PrivateChatNumberText.setText("ALL");
		PrivateChatNumberText.setBounds(108, 20, 203, 24);
		panel_3.add(PrivateChatNumberText);
		PrivateChatNumberText.setColumns(10);

		SendText = new JTextArea();
		SendText.setBounds(10, 51, 479, 61);
		panel_3.add(SendText);

		JButton btSend = new JButton("发送");
		btSend.setBackground(SystemColor.activeCaption);
		btSend.setBounds(503, 98, 113, 27);
		//添加发送功能
		btSend.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent e) {
				String time;
				String receiveId = PrivateChatNumberText.getText();
				if(receiveId.equals("ALL")){
					time= tool.send(SendText.getText().trim(),"ALL");//不设置私法账号，则群发ALL
				}
				else {
					time= tool.send(SendText.getText().trim(),PrivateChatNumberText.getText());//获取去掉空格后的文本		
				}

				textAreaNews.append(time+"\r\n");
				if(PrivateChatNumberText.getText()==null){
					textAreaNews.append("你说："+SendText.getText().trim()+"(群发)\r\n");
				}

				else {
					textAreaNews.append("你对"+PrivateChatNumberText.getText()+"说："+SendText.getText().trim()+"\r\n");
				}
				//清空消息框
				SendText.setText("");
			}			
		});  
		panel_3.add(btSend);

		JButton btAddChatRoom = new JButton("加入聊天室");
		btAddChatRoom.setBounds(503, -2, 113, 27);
		panel_3.add(btAddChatRoom);

		JButton btnSendToRoom = new JButton("聊天室发言");
		btnSendToRoom.setBounds(503, 58, 113, 27);
		btnSendToRoom.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String time;
				String receiveId = PrivateChatNumberText.getText();
				time= tool.sentMsgToRoom(SendText.getText().trim(),receiveId);//获取去掉空格后的文本		
				textAreaNews.append(time+"\r\n");
				textAreaNews.append("你在聊天室："+receiveId+"说："+SendText.getText().trim()+"(群发)\r\n");

				//清空消息框
				SendText.setText("");
			}
		});
		panel_3.add(btnSendToRoom);

		JButton btnQuitChatRoom = new JButton("退出聊天室");
		btnQuitChatRoom.setBounds(503, 30, 113, 27);
		panel_3.add(btnQuitChatRoom);
		//退出聊天室功能

		//加聊天室功能
		btAddChatRoom.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String time;
				final String ChatRoomId = PrivateChatNumberText.getText();
				time= tool.addChatRoom(ChatRoomId);//执行方法	


				textAreaNews.append(time+"\r\n");
				textAreaNews.append("你请求加入聊天室："+ChatRoomId+"\r\n");
				//添加聊天室按钮

				JButton btChatRoom = new JButton(ChatRoomId+"(群聊)");
				btChatRoom.setBackground(SystemColor.activeCaption);
				btChatRoom.setBounds(503, 85, 113, 27);
				//设置监听,添加所选的用户的账号到私聊框
				btChatRoom.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						PrivateChatNumberText.setText(ChatRoomId);
					}			
				}); 
				//添加到列表
				panelOnlineUsers.add(btChatRoom);
			}			
		});  

		JPanel panel_4 = new JPanel();
		panel_4.setBounds(740, 556, 100, 138);
		contentPane.add(panel_4);
		panel_4.setLayout(null);

		JLabel lblNewLabel_6 = new JLabel("记录查询");
		lblNewLabel_6.setBounds(20, 5, 60, 18);
		panel_4.add(lblNewLabel_6);

		JButton btMessageRecord = new JButton("查询记录");
		btMessageRecord.setBounds(0, 36, 100, 27);
		panel_4.add(btMessageRecord);

		//添加获取聊天记录功能
		btMessageRecord.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent e) {
				String id =user.getSchoolnumber();
				textAreaNews.append("消息记录\r\n");
				MessageDaolmpl messageDaolmpl =new MessageDaolmpl();
				//根据双方ID，查询记录
				ArrayList<Message> list = messageDaolmpl.selectMessageRecord(id,PrivateChatNumberText.getText());
				for (Message message : list) {
					textAreaNews.append(message.get(FieldType.TIME)+"\r\n");
					textAreaNews.append(message.get(FieldType.USER_ID)+"发送消息给用户: "+message.get(FieldType.PEER_ID)+"  :"+message.get(FieldType.MSG_TXT)+"\r\n");
				}
			}			
		});

		JButton btClose = new JButton("退出");
		btClose.setBounds(0, 111, 100, 27);
		panel_4.add(btClose);
		//添加退出功能
		btClose.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent e) {
				tool.logout(user);
				messageThread.stop();
				dispose();
			}			
		});  
		//获取在线用户列表
		updateUserList(selectUser());
		//  连接服务器
		boolean loginBoolean = connectServer(port,hostIp,user);
		if(loginBoolean){
			textAreaNews.append("登陆成功"+"\r\n");
		}
		else {
			textAreaNews.append("登陆失败"+"\r\n");
		}

	}
	/**
	 * 
	 * @Title: selectUser()
	 * @Package client 
	 * @Description: TODO(在线用户查询)    
	 * @date 2018年6月11日 上午11:27:21 
	 * @version V1.0   
	 * @author 李云飞
	 */
	public ArrayList<User> selectUser(){
		ArrayList<User> list  = new ArrayList<User>();
		UserDaoImpl userDaoImpl = new UserDaoImpl();
		list = userDaoImpl.selectUserList();
		return list;

	}
	/**
	 * 
	 * @Title: updateUserList()
	 * @Package client 
	 * @Description: TODO(更新在线用户列表)    
	 * @date 2018年6月11日 上午11:27:53 
	 * @version V1.0   
	 * @author 李云飞
	 */
	public void updateUserList(ArrayList<User> list){
		//为每一个用户创建按钮显示
		JBUserList = new ArrayList<JButton>();
		for (final User user : list) {
			JButton btUser = new JButton(user.getNickname()+","+user.getSchoolnumber());
			btUser.setBackground(SystemColor.activeCaption);
			btUser.setBounds(503, 85, 113, 27);
			//设置监听,添加所选的用户的账号到私聊框
			btUser.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					PrivateChatNumberText.setText(user.getSchoolnumber());
				}			
			});  
			JBUserList.add(btUser);
		}
		//将列表添加到显示区域
		for (JButton jButton : JBUserList) {
			panelOnlineUsers.add(jButton);
		}

	}

	/**
	 * 连接服务器
	 * 
	 * @param port
	 * @param hostIp
	 * @param name
	 */
	public boolean connectServer(int port, String hostIp,User user) {
		// 连接服务器
		try {

			socket = new Socket(hostIp, port);// 根据端口号和服务器ip建立连接
			//writer = new PrintWriter(socket.getOutputStream());

			outputStream=socket.getOutputStream();//字节输入流
			/*           objectInputStream=new ObjectInputStream(inputStream);
			 */
			inputStream = socket.getInputStream();
			//构造工具类
			tool = new ClientTool(user, outputStream, inputStream);
			Message message=new Message(Commands.MSG_P2R);
			message.set(FieldType.USER_ID, lblSchoolNumber.getText());//获取学号
			message.set(FieldType.USER_NAME, lblNickName.getText());//获取昵称
			tool.sendMessage(message);
			// 发送客户端用户基本信息(用户名和ip地址)
			tool.login(user);
			// 开启接收消息的线程
			messageThread = new MessageThread(inputStream,textAreaNews);
			messageThread.start();
			System.out.println("连接服务器成功");

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 
	 * 不断接收消息的线程
	 *
	 */
	class MessageThread extends Thread {
		private InputStream inputStream;
		private ObjectInputStream objectInputStream;
		private JTextArea textArea;

		// 接收消息线程的构造方法
		public MessageThread(InputStream inputStream, JTextArea textArea) {
			this.inputStream = inputStream;
			this.textArea = textArea;

		}


		public void run() {
			Message msg = null;
			try {
				objectInputStream = new ObjectInputStream(inputStream) ;
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			while (true) {
				if(inputStream!=null)
					try {
						//获取发送来的信息对象

						msg=(Message)objectInputStream.readObject();

						String userId=msg.get(FieldType.USER_ID);
						switch (msg.getCommand()) {
						//私聊
						case MSG_P2P:
						{
							textAreaNews.append(msg.get(FieldType.TIME)+"\r\n");
							textAreaNews.append(msg.get(FieldType.USER_ID)+"(私聊)对你说："+msg.get(FieldType.MSG_TXT)+"\r\n");
							break;
						}	
						//聊天室聊天
						case MSG_P2R:
						{
							if(!msg.get(FieldType.USER_ID).equals(user.getSchoolnumber())){
								textAreaNews.append(msg.get(FieldType.TIME)+"\r\n");
								textAreaNews.append(msg.get(FieldType.USER_ID)+"在聊天室"+msg.get(FieldType.ROOM_ID)+"对你说："+msg.get(FieldType.MSG_TXT)+"\r\n");
							}

							break;
						}
						//群发消息
						case MSG_ALL:
						{
							textAreaNews.append(msg.get(FieldType.TIME)+"\r\n");
							textAreaNews.append(msg.get(FieldType.USER_ID)+"(群发消息)对你说："+msg.get(FieldType.MSG_TXT)+"\r\n");
							break;
						}
						//创建聊天室成功
						case CREATE_CHAT_ROOM:
						{
							textAreaNews.append(msg.get(FieldType.TIME)+"\r\n");
							textAreaNews.append("加入群聊室:"+msg.get(FieldType.ROOM_ID)+msg.get(FieldType.MSG_TXT)+"\r\n");
							break;
						}
						//更新在线用户列表
						case QUERY_USERS:
						{
							textAreaNews.append("刷新用户列表\r\n");
							for (JButton jButton : JBUserList) {
								panelOnlineUsers.remove(jButton);
							}
							updateUserList(selectUser());
						}
						default:
							break;
						}
					} catch (IOException e) {
						//		e.printStackTrace();
					} catch (Exception e) {
						//		e.printStackTrace();
					}
			}
		}
	}

	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}  
}
