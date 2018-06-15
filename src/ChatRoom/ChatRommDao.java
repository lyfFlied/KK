package ChatRoom;

import java.util.ArrayList;

import User.User;

public interface ChatRommDao {
	//创建聊天室
	public boolean creatChatRoom(String UserId,String ChatRoomId);
	//加入聊天室
	public boolean addChatRoom(String UserId,String ChatRoomId);
	//退出聊天室
	public boolean removeChatRoom(String UserId,String ChatRoomId);
	//查询聊天室
	public ArrayList<ChatRoom> selectChatRoom(String UserId);
	//查询聊天室的成员
	public ArrayList<User> selectUser(String ChatRoomId);

}
