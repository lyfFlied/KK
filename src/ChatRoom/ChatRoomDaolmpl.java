package ChatRoom;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import message.Message;
import message.Message.FieldType;
import User.DBUtil;
import User.User;

public class ChatRoomDaolmpl implements ChatRommDao{

	@Override
	public boolean creatChatRoom(String UserId, String ChatRoomId) {
		// TODO Auto-generated method stub
		return false;
	}
/**
 * 加入聊天室
 */
	@Override
	public boolean addChatRoom(String UserId, String ChatRoomId) {

		Connection conn = DBUtil.open();
		String sql = "insert into User_Room ( UserId , ChatRoomId ) values ( ?,?)";
		try {
			PreparedStatement pstmt = 	conn.prepareStatement(sql);
			pstmt.setString(1, UserId);
			pstmt.setString(2, ChatRoomId);
			pstmt.executeUpdate();  

			return true;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		return false;
	}
	/**
	 * 离开聊天室
	 */
	@Override
	public boolean removeChatRoom(String UserId, String ChatRoomId) {

		Connection conn = DBUtil.open();
		String sql = "DELETE FROM User_Room where UserId = ? and ChatRoomId = ?";
		try {
			PreparedStatement pstmt = 	conn.prepareStatement(sql);
			pstmt.setString(1, UserId);
			pstmt.setString(2, ChatRoomId);
			pstmt.executeUpdate();  

			return true;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		return false;
	}
/**
 * 获取聊天室列表
 */
	@Override
	public ArrayList<ChatRoom> selectChatRoom(String UserId) {
		// TODO Auto-generated method stub
		ArrayList<ChatRoom> list  = new ArrayList<ChatRoom>();
		Connection conn = DBUtil.open();
		//聊天记录升序查询
		String sql = "select * from User_Room where UserId= ? ";
		try {
			PreparedStatement pstmt = 	conn.prepareStatement(sql);
			pstmt.setString(1, UserId);
			ResultSet rs= pstmt.executeQuery();
			while(rs.next()){
				ChatRoom chatRoom = new ChatRoom(rs.getString("ChatRoomId"));//群聊室
				ArrayList<User> userList =  selectUser(chatRoom.getChatRoomId());//获取聊天室的成员
				for (User user : userList) {//将成员加入聊天室
					chatRoom.setUser(user);
				}

				list.add(chatRoom);
			}
			return list;



		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		return null;
	}
/**
 * 获取聊天室的成员
 */
	@Override
	public ArrayList<User> selectUser(String ChatRoomId) {
		// TODO Auto-generated method stub
		ArrayList<User> list  = new ArrayList<User>();
		Connection conn = DBUtil.open();
		//聊天记录升序查询
		String sql = "select * from User_Room where ChatRoomId= ? ";
		try {
			PreparedStatement pstmt = 	conn.prepareStatement(sql);
			pstmt.setString(1, ChatRoomId);
			ResultSet rs= pstmt.executeQuery();
			while(rs.next()){
				User user = new User();//群聊室
				user.setSchoolnumber((rs.getString("userId")));
				list.add(user);
			}
			return list;



		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		return null;
	}

}
