package Server;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;


import message.Message.FieldType;
import message.Message.Commands;
import message.Message;
import message.MessageDaolmpl;
import ChatRoom.ChatRoom;
import ChatRoom.ChatRoomDaolmpl;
import User.User;
import User.UserDaoImpl;


public class Server extends JFrame{

	/**
	 * 服务器端
	 */
	private static final long serialVersionUID = 1L;
	private JFrame frame;
	private JTextArea contentArea;  //文本域
	private JTextField txt_message;  //用于显示文本信息
	private JTextField txt_max;     //设置最大连接人数
	private JTextField txt_port;    //设置端口号
	private JButton btn_start;      //开始按钮
	private JButton btn_stop;       //断开按钮
	private JButton btn_send;       //发送按钮
	private JPanel northPanel;      //北方面板
	private JPanel southPanel;      //南方面板
	private JScrollPane rightPanel; //左边滚动条
	private JScrollPane leftPanel;  //右边滚动条
	private JSplitPane centerSplit; //分割线
	private JList userList;         //列表组件
	private DefaultListModel listModel;//在线用户列表

	private ServerSocket serverSocket;
	private ServerThread serverThread;
	private ArrayList<ClientThread> clients;//客户端线程
	private Message msg;//消息对象
	private ArrayList<ChatRoom> chatRoomList = new ArrayList<ChatRoom>();//群聊室列表
	InputStream inputStream;
	ObjectInputStream objectInputStream;//反序列化流,利用输入流从文件中读取对象
	OutputStream outputStream;//字节输入流
	ObjectOutputStream objectOutputStream;//序列化流,利用输出流向文件中写入对象
	private boolean isStart = false;
	/**
	 * 聊天室的数据库操作
	 */
	private ChatRoomDaolmpl chatRoomDaolmpl = new ChatRoomDaolmpl();

	// 主方法,程序执行入口
	public static void main(String[] args) {
		new Server();
	}

	// 执行消息发送
	public void send() {
		if (!isStart) {
			JOptionPane.showMessageDialog(frame, "服务器还未启动,不能发送消息！", "错误",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		if (clients.size() == 0) {
			JOptionPane.showMessageDialog(frame, "没有用户在线,不能发送消息！", "错误",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		String message = txt_message.getText().trim();
		if (message == null || message.equals("")) {
			JOptionPane.showMessageDialog(frame, "消息不能为空！", "错误",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		//    sendServerMessage(message);// 群发服务器消息
		Date day=new Date();   
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		String time = df.format(day); 
		Message messageAll = new Message(Commands.MSG_ALL);
		messageAll.set(FieldType.MSG_TXT, txt_message.getText());
		messageAll.set(FieldType.USER_ID, "服务器");
		messageAll.set(FieldType.TIME, time);
		for (int i = clients.size() - 1; i >= 0; i--) {
			//转发群发
			ObjectOutputStream	objectOutputStream =clients.get(i).getObjectOutputStream();
			sendMessage(objectOutputStream, messageAll);
		}		

		contentArea.append("服务器：" + txt_message.getText() + "\r\n");
		txt_message.setText(null);

	}

	/**
	 * 服务器构造方法
	 */
	public Server() {
		frame = new JFrame("实时聊天系统服务器");
		// 更改JFrame的图标：
		//frame.setIconImage(Toolkit.getDefaultToolkit().createImage(Client.class.getResource("qq.png")));
		//frame.setIconImage(Toolkit.getDefaultToolkit().createImage(Server.class.getResource("qq.png")));
		contentArea = new JTextArea();
		contentArea.setEditable(false);
		contentArea.setForeground(Color.blue);
		txt_message = new JTextField();
		txt_max = new JTextField("30");
		txt_port = new JTextField("6666");
		btn_start = new JButton("启动");
		btn_stop = new JButton("停止");
		btn_send = new JButton("发送");
		btn_stop.setEnabled(false);
		listModel = new DefaultListModel();
		userList = new JList(listModel);

		southPanel = new JPanel(new BorderLayout());
		southPanel.setBorder(new TitledBorder("写消息"));
		southPanel.add(txt_message, "Center");
		southPanel.add(btn_send, "East");
		leftPanel = new JScrollPane(userList);
		leftPanel.setBorder(new TitledBorder("在线用户"));

		rightPanel = new JScrollPane(contentArea);
		rightPanel.setBorder(new TitledBorder("消息显示区"));

		centerSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel,
				rightPanel);
		centerSplit.setDividerLocation(100);
		northPanel = new JPanel();
		northPanel.setLayout(new GridLayout(1, 6));
		northPanel.add(new JLabel("人数上限"));
		northPanel.add(txt_max);
		northPanel.add(new JLabel("端口"));
		northPanel.add(txt_port);
		northPanel.add(btn_start);
		northPanel.add(btn_stop);
		northPanel.setBorder(new TitledBorder("配置信息"));

		frame.setLayout(new BorderLayout());
		frame.add(northPanel, "North");
		frame.add(centerSplit, "Center");
		frame.add(southPanel, "South");
		frame.setSize(600, 400);
		//frame.setSize(Toolkit.getDefaultToolkit().getScreenSize());//设置全屏
		int screen_width = Toolkit.getDefaultToolkit().getScreenSize().width;
		int screen_height = Toolkit.getDefaultToolkit().getScreenSize().height;
		frame.setLocation((screen_width - frame.getWidth()) / 2,
				(screen_height - frame.getHeight()) / 2);
		frame.setVisible(true);

		// 关闭窗口时事件
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				if (isStart) {
					//      closeServer();// 关闭服务器
				}
				System.exit(0);// 退出程序
			}
		});

		// 文本框按回车键时事件
		txt_message.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				send();
			}
		});

		// 单击发送按钮时事件
		btn_send.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				send();
			}
		});

		// 单击启动服务器按钮时事件
		btn_start.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (isStart) {
					JOptionPane.showMessageDialog(frame, "服务器已处于启动状态，不要重复启动！",
							"错误", JOptionPane.ERROR_MESSAGE);
					return;
				}
				int max;
				int port;
				try {
					try {
						max = Integer.parseInt(txt_max.getText());
					} catch (Exception e1) {
						throw new Exception("人数上限为正整数！");
					}
					if (max <= 0) {
						throw new Exception("人数上限为正整数！");
					}
					try {
						port = Integer.parseInt(txt_port.getText());
					} catch (Exception e1) {
						throw new Exception("端口号为正整数！");
					}
					if (port <= 0) {
						throw new Exception("端口号 为正整数！");
					}
					serverStart(max, port);
					contentArea.append("服务器已成功启动!人数上限：" + max + ",端口：" + port
							+ "\r\n");
					JOptionPane.showMessageDialog(frame, "服务器成功启动!");
					btn_start.setEnabled(false);
					txt_max.setEnabled(false);
					txt_port.setEnabled(false);
					btn_stop.setEnabled(true);
				} catch (Exception exc) {
					JOptionPane.showMessageDialog(frame, exc.getMessage(),
							"错误", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		// 单击停止服务器按钮时事件
		btn_stop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!isStart) {
					JOptionPane.showMessageDialog(frame, "服务器还未启动，无需停止！", "错误",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				try {
					//   closeServer();
					btn_start.setEnabled(true);
					txt_max.setEnabled(true);
					txt_port.setEnabled(true);
					btn_stop.setEnabled(false);
					contentArea.append("服务器成功停止!\r\n");
					JOptionPane.showMessageDialog(frame, "服务器成功停止！");
				} catch (Exception exc) {
					JOptionPane.showMessageDialog(frame, "停止服务器发生异常！", "错误",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	}

	/**
	 * 
	 * @Title: serverStart(int max, int port)
	 * @Package Server 
	 * @Description: TODO( 启动服务器)    
	 * @date 2018年6月11日 上午11:17:02 
	 * @version V1.0   
	 * @author 李云飞
	 */
	public void serverStart(int max, int port) throws java.net.BindException {
		try {
			clients = new ArrayList<ClientThread>();
			serverSocket = new ServerSocket(port);
			serverThread = new ServerThread(serverSocket, max);
			serverThread.start();
			isStart = true;
		} catch (BindException e) {
			isStart = false;
			throw new BindException("端口号已被占用，请换一个！");
		} catch (Exception e1) {
			e1.printStackTrace();
			isStart = false;
			throw new BindException("启动服务器异常！");
		}
	}
	/**
	 *  服务器线程
	 *
	 */
	class ServerThread extends Thread {
		private ServerSocket serverSocket;
		private int max;// 人数上限

		// 服务器线程的构造方法
		public ServerThread(ServerSocket serverSocket, int max) {
			this.serverSocket = serverSocket;
			this.max = max;
		}

		public void run() {
			Message message = null;
			while (true) {// 不停的等待客户端的链接
				try {
					Socket socket = serverSocket.accept();
					if (clients.size() == max) {// 如果已达人数上限
						outputStream = socket.getOutputStream();
						ObjectOutputStream objectOutputStream=  new ObjectOutputStream(outputStream);
						Message reMessage  = new Message(Commands.MSG_P2P);

						reMessage.set(FieldType.USER_ID, "服务器");
						reMessage.set(FieldType.MSG_TXT, "服务器已达人数上限，请稍后重试");
						socket.close();
						continue;
					}
					inputStream = socket.getInputStream();
					ObjectInputStream objectInputStream=  new ObjectInputStream(inputStream);
					try {
						message = (Message)objectInputStream.readObject();
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					User user = new User();
					user.setSchoolnumber(message.get(FieldType.USER_ID));
					user.setNickname(message.get(FieldType.USER_NAME));
					ClientThread client = new ClientThread(socket,user);
					client.start();// 开启对此客户端服务的线程
					//加入线程组中
					clients.add(client);
					//显示用户上线信息
					listModel.addElement(client.getUser().getSchoolnumber()+" "+client.getUser().getNickname());// 更新在线列表
					contentArea.append(client.getUser().getNickname()
							+ client.getUser().getSchoolnumber() + "上线!\r\n");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 
	 * @Title: sendMessage() 
	 * @Package Server 
	 * @Description: TODO(发送封装好的消息对象)    
	 * @date 2018年6月11日 上午11:13:50 
	 * @version V1.0   
	 * @author 李云飞
	 */
	public void sendMessage(ObjectOutputStream objectOutputStream ,Message message) {

		try {
			objectOutputStream.writeObject(message);
			objectOutputStream.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}

	/**
	 * 
	 * @Title: ClientThread.java 
	 * @Package Server 
	 * @Description: TODO(客户端线程)    
	 * @date 2018年6月11日 上午11:13:50 
	 * @version V1.0   
	 * @author 李云飞
	 */
	class ClientThread extends Thread {
		private Socket socket;
		InputStream inputStream;
		ObjectInputStream objectInputStream;
		OutputStream outputStream;
		ObjectOutputStream objectOutputStream;
		private User user;

		public InputStream getInputStream() {
			return inputStream;
		}

		public ObjectInputStream getObjectInputStream() {
			return objectInputStream;
		}

		public OutputStream getOutputStream() {
			return outputStream;
		}

		public ObjectOutputStream getObjectOutputStream() {
			return objectOutputStream;
		}
		public User getUser() {
			return user;
		}

		// 客户端线程的构造方法
		public ClientThread(Socket socket,User user) {
			try {
				this.socket = socket;
				this.user=user;
				//接收，发送
				inputStream = socket.getInputStream();

				outputStream = socket.getOutputStream();
				objectOutputStream=new ObjectOutputStream(outputStream);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}


		public void run() {// 不断接收客户端的消息，进行处理。
			String message = null;
			while (true) {
				try {try {
					if(inputStream!=null){
						//接收传过来的消息对象
						objectInputStream=new ObjectInputStream(inputStream);
						msg=(Message)objectInputStream.readObject();
					}

				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if(msg!=null){
					//处理请求
					ProcessingMessages(msg ,objectOutputStream);

				}} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	/**
	 * 
	 * @Title: ProcessingMessages()
	 * @Package Server 
	 * @Description: TODO(处理客户端发来的请求)    
	 * @date 2018年6月6日 上午10:54:12 
	 * @version V1.0   
	 * @author 李云飞
	 */
	public void ProcessingMessages(Message msg ,ObjectOutputStream ReObjectOutputStream){//消息和返回流
		String userId=msg.get(FieldType.USER_ID);
		switch (msg.getCommand()) {
		case LOG_IN:
		{
			/**
			 * 返回消息给请求者
			 */
			Message ReMessage =new Message();
			ReMessage.set(FieldType.MSG_TXT, "登陆成功");
			ReMessage.set(FieldType.USER_ID, "服务器");
			sendMessage(ReObjectOutputStream, ReMessage);
			Message updateMessage = new Message(Commands.QUERY_USERS);
			updateMessage.set(FieldType.USER_ID, "服务器");
			updateMessage.set(FieldType.MSG_TXT, "更新用户列表");
			for (int i = clients.size() - 1; i >= 0; i--) {
				//群发告知有新用户上线
				ObjectOutputStream	objectOutputStream =clients.get(i).getObjectOutputStream();
				sendMessage(objectOutputStream, updateMessage);
			}
			break;
		}
		case LOG_OUT:
		{		
			User user = new User();
			user.setSchoolnumber(msg.get(FieldType.USER_ID));
			user.setState(1);
			contentArea.append(msg.get(FieldType.TIME));
			contentArea.append("用户"+user.getSchoolnumber()+"请求退出...\r\n");
			//更新状态，下线
			UserDaoImpl userDaoImpl = new UserDaoImpl();
			int state = userDaoImpl.updateStatic(user);
			//更新下线时间
			userDaoImpl.updateOutTime(user);
			if(state ==1){
				for (int i = clients.size() - 1; i >= 0; i--) {
					if(clients.get(i).getUser().getSchoolnumber().toString().equals(user.getSchoolnumber())){
						listModel.remove(i);// 更新在线列表
						clients.remove(i);
					}
				}
				contentArea.append(msg.get(FieldType.TIME));
				contentArea.append("用户"+user.getSchoolnumber()+"退出成功\r\n");
				/**
				 * 发送用户退出消息
				 */
				Message updateMessage = new Message(Commands.QUERY_USERS);
				updateMessage.set(FieldType.USER_ID, "服务器");
				updateMessage.set(FieldType.MSG_TXT, "更新用户列表");
				for (int i = clients.size() - 1; i >= 0; i--) {
					//群发告知有用户下线
					ObjectOutputStream	objectOutputStream =clients.get(i).getObjectOutputStream();
					sendMessage(objectOutputStream, updateMessage);
				}
			}
			else {
				System.out.println("退出失败");
			}
			break;

		}
		//私发信息
		case MSG_P2P:
		{
			//将消息存入数据库
			MessageDaolmpl messageDaolmpl = new MessageDaolmpl();
			boolean state= messageDaolmpl.addRecord(msg);
			String toId=msg.get(FieldType.PEER_ID);
			String txt=msg.get(FieldType.MSG_TXT);
			contentArea.append(msg.get(FieldType.TIME)+"\r\n");
			contentArea.append("用户"+userId+"发送消息给用户: "+toId+" :"+msg.get(FieldType.MSG_TXT)+"\r\n");
			Message message1=new Message(Commands.MSG_P2P);

			for (int i = clients.size() - 1; i >= 0; i--) {
				//匹配对象ID,转发私聊
				if(clients.get(i).getUser().getSchoolnumber().toString().equals(toId)){
					ObjectOutputStream	objectOutputStream =clients.get(i).getObjectOutputStream();
					sendMessage(objectOutputStream, msg);
				}
			}
			break;
		}	
		//聊天室消息
		case MSG_P2R:
		{
			ArrayList<User> listUser = new ArrayList<User>();
			//将消息存入数据库
			//	msg.set(FieldType.PEER_ID, msg.get(FieldType.ROOM_ID));
			MessageDaolmpl messageDaolmpl = new MessageDaolmpl();
			boolean state= messageDaolmpl.addRecord(msg);
			String ChatRoomId=msg.get(FieldType.ROOM_ID);
			String txt=msg.get(FieldType.MSG_TXT);
			contentArea.append(msg.get(FieldType.TIME)+"\r\n");
			contentArea.append("用户"+userId+"发送消息给聊天室: "+ChatRoomId+" :"+msg.get(FieldType.MSG_TXT)+"\r\n");
			for (ChatRoom chatRoom : chatRoomList) {
				//匹配群聊室ID,获取用户列表
				if(chatRoom.getChatRoomId().equals(ChatRoomId)){
					listUser = chatRoom.getUserList();
					break;
				}
			}
			//给聊天室每一个成员发消息
			for (User user : listUser) {
				String toId = user.getSchoolnumber();
				for (int i = clients.size() - 1; i >= 0; i--) {
					//匹配对象ID,转发私聊
					if(clients.get(i).getUser().getSchoolnumber().toString().equals(toId)){
						ObjectOutputStream	objectOutputStream =clients.get(i).getObjectOutputStream();
						sendMessage(objectOutputStream, msg);
					}
				}
			}
			break;
		}	
		case MSG_ALL:
		{
			//将消息存入数据库
			MessageDaolmpl messageDaolmpl = new MessageDaolmpl();
			boolean state= messageDaolmpl.addRecord(msg);
			String toId=msg.get(FieldType.PEER_ID);
			String txt=msg.get(FieldType.MSG_TXT);
			contentArea.append(msg.get(FieldType.TIME)+"\r\n");
			contentArea.append("用户"+userId+"（群发消息）:"+msg.get(FieldType.MSG_TXT)+"\r\n");
			Message message1=new Message(Commands.MSG_ALL);

			for (int i = clients.size() - 1; i >= 0; i--) {
				//转发群发
				if(!clients.get(i).getUser().getSchoolnumber().toString().equals(userId)){//不发给自己
					ObjectOutputStream	objectOutputStream =clients.get(i).getObjectOutputStream();
					sendMessage(objectOutputStream, msg);
				}
			}		
			break;
		}
		//加入群聊室
		case CREATE_CHAT_ROOM:
		{
			boolean RoomIsExistence=false;//房间是否存在
			String ChatRoomId=msg.get(FieldType.ROOM_ID);//群聊室ID
			String UserId=msg.get(FieldType.USER_ID);//用户ID
			User user = new User();
			user.setSchoolnumber(UserId);
			contentArea.append(msg.get(FieldType.TIME)+"\r\n");
			contentArea.append("用户"+UserId+"请求加入群聊室: "+ChatRoomId+"\r\n");
			if(chatRoomList!=null){
				for (ChatRoom chatRoom : chatRoomList) {
					//加入群聊室
					if(chatRoom.getChatRoomId().equals(ChatRoomId)){
						chatRoomDaolmpl.addChatRoom(UserId, ChatRoomId);
						chatRoom.setUser(user);
						RoomIsExistence=true;
						break;
					}

				}
			}

			if(!RoomIsExistence){//不存在则新建
				ChatRoom chatRoom = new ChatRoom(ChatRoomId);
				chatRoom.setUser(user);
				chatRoomList.add(chatRoom);
				chatRoomDaolmpl.addChatRoom(UserId, ChatRoomId);
			}
			/**
			 * 返回消息给请求者
			 */
			//返回给请求者
			Date day=new Date();   
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
			String time = df.format(day); 
			Message ReMessage =new Message(Commands.CREATE_CHAT_ROOM);
			ReMessage.set(FieldType.MSG_TXT, "成功");
			ReMessage.set(FieldType.USER_ID, "服务器");
			ReMessage.set(FieldType.ROOM_ID, ChatRoomId);
			ReMessage.set(FieldType.TIME, time);
			sendMessage(ReObjectOutputStream, ReMessage);

			break;
		}
		default:
			//	System.out.println("未识别的指令："+msg.getCommand().toString());
			break;
		}


	}
}