package ChatRoom;

import java.util.ArrayList;

import User.User;

public class ChatRoom {
	private String chatRoomId;//群聊室ID
private User user;//用户
private ArrayList<User> userList = new ArrayList<User>();//用户列表
public String getChatRoomId() {
	return chatRoomId;
}
public void setChatRoomId(String chatRoomId) {
	this.chatRoomId = chatRoomId;
}
public User getUser() {
	return user;
}
public void setUser(User user) {
	this.user = user;
	userList.add(user);
}
public ArrayList<User> getUserList() {
	return userList;
}
public void setUserList(ArrayList<User> userList) {
	this.userList = userList;
}
public ChatRoom(String chatRoomId) {
	super();
	this.chatRoomId = chatRoomId;

}
public ChatRoom() {
	super();
}

}
