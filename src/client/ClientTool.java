package client;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import message.Message.FieldType;
import message.Message.Commands;
import message.Message;
import User.User;

public class ClientTool {
	private User user;
	private Message message;
	private OutputStream outputStream;
	private InputStream inputStream;
	public ClientTool(User user,OutputStream outputStream, InputStream inputStream){
		this.user = user;
		this.outputStream = outputStream;
		this.inputStream = inputStream;
	}
	/**
	 * 
	 * @Title: send(String message , String receiveId)
	 * @Package client 
	 * @Description: TODO( 执行发送,返回发送时间)    
	 * @date 2018年6月11日 上午11:29:11 
	 * @version V1.0   
	 * @author 李云飞
	 */
	public String send(String message , String receiveId) {
		//如果私聊账号不为空
		if(receiveId!=null){
			//私法信息；对方ID，信息
			return sendMsgToUser(user,receiveId,message);
		}
		if(receiveId.equals("ALL")) {
			return sendMsgToUser(user,"ALL",message);
		}
		return null;
	}
	/**
	 * 
	 * @Title: login(User user)
	 * @Package client 
	 * @Description: TODO(登陆)    
	 * @date 2018年6月11日 上午11:29:47 
	 * @version V1.0   
	 * @author 李云飞
	 */
	public void login(User user)
	{

		String userid =user.getSchoolnumber();
		String passwd = user.getPassword();
		Message message=new Message(Commands.LOG_IN);
		message.set(FieldType.USER_ID, userid);
		message.set(FieldType.PASS_WD, passwd);
		sendMessage(message);
	}
	/**
	 * 
	 * @Title: logout(User user)
	 * @Package client 
	 * @Description: TODO(退出)    
	 * @date 2018年6月11日 上午11:30:07 
	 * @version V1.0   
	 * @author 李云飞
	 */
	public void logout(User user)
	{
		//获取当前ID
		String userId =user.getSchoolnumber();
		Message message=new Message(Commands.LOG_OUT);
		message.set(FieldType.USER_ID,userId );
		sendMessage(message);
	}

	/**
	 * 
	 * @Title: sentMsgToRoom(String text,String roomid)
	 * @Package client 
	 * @Description: TODO(发送信息给群聊室,并返回时间)    
	 * @date 2018年6月11日 上午11:30:23 
	 * @version V1.0   
	 * @author 李云飞
	 */
	public String sentMsgToRoom(String text,String roomid)
	{
		Date day=new Date();   
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		String time = df.format(day); 
		Message message=new Message(Commands.MSG_P2R);//聊天室消息
		message.set(FieldType.TIME, time);
		message.set(FieldType.USER_ID, user.getSchoolnumber());
		message.set(FieldType.ROOM_ID, roomid);
		message.set(FieldType.MSG_TXT, text);
		sendMessage(message);
		return time;
	}
	/**
	 * 
	 * @Title: sendMsgToUser(User user,String userid,String msg)
	 * @Package client 
	 * @Description: TODO(发送私聊，返回时间)    
	 * @date 2018年6月11日 上午11:30:44 
	 * @version V1.0   
	 * @author 李云飞
	 */
	public String sendMsgToUser(User user,String userid,String msg)
	{
		Date day=new Date();   
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		String time = null;
		time = df.format(day); 
		if(userid.equals("ALL")){
			Message message=new Message(Commands.MSG_ALL);	//群发
			message.set(FieldType.TIME, time);
			message.set(FieldType.USER_ID, user.getSchoolnumber());
			message.set(FieldType.PEER_ID, "群发");
			message.set(FieldType.MSG_TXT, msg);
			sendMessage(message);
			return time;
		}
		else{
			Message message=new Message(Commands.MSG_P2P);//私法
			message.set(FieldType.TIME, time);
			message.set(FieldType.USER_ID, user.getSchoolnumber());
			message.set(FieldType.PEER_ID, userid);
			message.set(FieldType.MSG_TXT, msg);
			sendMessage(message);
			return time;
		}
	}
	/**
	 * 
	 * @Title: sendMessage(Message message)
	 * @Package client 
	 * @Description: TODO(发送消息)    
	 * @date 2018年6月11日 上午11:31:35 
	 * @version V1.0   
	 * @author 李云飞
	 */
	public void sendMessage(Message message) {
		try {
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
			objectOutputStream.writeObject(message);
			objectOutputStream.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}

	/**
	 * 
	 * @Title: addChatRoom(String ChatRoomId)
	 * @Package client 
	 * @Description: TODO(加入聊天室)    
	 * @date 2018年6月11日 上午11:31:15 
	 * @version V1.0   
	 * @author 李云飞
	 */
	public String addChatRoom(String ChatRoomId) {
		//获取当前时间
		Date day=new Date();   
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		String time = null;
		time = df.format(day); 
		Message message=new Message(Commands.CREATE_CHAT_ROOM);	//加入聊天室
		message.set(FieldType.TIME, time);
		message.set(FieldType.ROOM_ID, ChatRoomId);
		message.set(FieldType.USER_ID, user.getSchoolnumber());
		sendMessage(message);
		return time;

	}


}
