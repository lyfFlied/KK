package message;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import message.Message.FieldType;
import User.DBUtil;
import User.User;
/**
 * 
 * 消息的数据库操作
 *
 */
public class MessageDaolmpl implements MessageDao{
	/**
	 * 查询聊天记录
	 */
	@Override
	public ArrayList<Message> selectSendRecord(String sendId) {
		// TODO Auto-generated method stub
		ArrayList<Message> list  = new ArrayList<Message>();
		Connection conn = DBUtil.open();
		String sql = "select * from message_record where send_id= ? ";
		try {
			PreparedStatement pstmt = 	conn.prepareStatement(sql);
			pstmt.setString(1, sendId);
			ResultSet rs= pstmt.executeQuery();
			while(rs.next()){
				Message message = new Message();
				message.set(FieldType.USER_ID, rs.getString("send_id"));//发送人ID
				message.set(FieldType.PEER_ID, rs.getString("receive_id"));//接收人ID
				message.set(FieldType.MSG_TXT, rs.getString("text"));//消息
				message.set(FieldType.TIME, rs.getString("time"));//时间

				list.add(message);
			}
			return list;



		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		return null;
	}

	@Override
	public ArrayList<Message> selectReceiveRecord(String receiveId) {
		// TODO Auto-generated method stub
		ArrayList<Message> list  = new ArrayList<Message>();
		Connection conn = DBUtil.open();
		String sql = "select * from message_record where receive_id= ? ";
		try {
			PreparedStatement pstmt = 	conn.prepareStatement(sql);
			pstmt.setString(1, receiveId);
			ResultSet rs= pstmt.executeQuery();
			while(rs.next()){
				Message message = new Message();
				message.set(FieldType.USER_ID, rs.getString("send_id"));//发送人ID
				message.set(FieldType.PEER_ID, rs.getString("receive_id"));//接收人ID
				message.set(FieldType.MSG_TXT, rs.getString("text"));//消息
				message.set(FieldType.TIME, rs.getString("time"));//时间

				list.add(message);
			}
			return list;



		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		return null;
	}
	/**
	 * 保存聊天记录
	 */
	@Override
	public boolean addRecord(Message message) {

		Connection conn = DBUtil.open();
		String sql = "insert into message_record ( send_id , receive_id ,text, time) values ( ?,?,?,?)";
		try {
			PreparedStatement pstmt = 	conn.prepareStatement(sql);
			pstmt.setString(1, message.get(FieldType.USER_ID));
			pstmt.setString(2, message.get(FieldType.PEER_ID));
			pstmt.setString(3, message.get(FieldType.MSG_TXT));
			pstmt.setString(4, message.get(FieldType.TIME));
			pstmt.executeUpdate();  

			return true;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		return false;
	}

	@Override
	public ArrayList<Message> selectMessageRecord(String userId,
			String contactsId) {
		// TODO Auto-generated method stub
		ArrayList<Message> list  = new ArrayList<Message>();
		Connection conn = DBUtil.open();
		//聊天记录升序查询
		String sql = "select * from message_record where receive_id= ? and send_id = ? or receive_id= ? and send_id = ? order by  time";
		try {
			PreparedStatement pstmt = 	conn.prepareStatement(sql);
			pstmt.setString(1, userId);
			pstmt.setString(2, contactsId);
			pstmt.setString(3, contactsId);
			pstmt.setString(4, userId);
			ResultSet rs= pstmt.executeQuery();
			while(rs.next()){
				Message message = new Message();
				message.set(FieldType.USER_ID, rs.getString("send_id"));//发送人ID
				message.set(FieldType.PEER_ID, rs.getString("receive_id"));//接收人ID
				message.set(FieldType.MSG_TXT, rs.getString("text"));//消息
				message.set(FieldType.TIME, rs.getString("time"));//时间

				list.add(message);
			}
			return list;



		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		return null;
	}

	@Override
	public ArrayList<Message> selectMessageRecordByR(String userId,
			String ChatRoomId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean addRecordToR(Message message) {
		// TODO Auto-generated method stub
		return false;
	}

}
