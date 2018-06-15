package message;

import java.util.ArrayList;

public interface MessageDao {
	//查询历史消息记录
	public ArrayList<Message> selectMessageRecord(String userId,String contactsId);//用户ID和联系人ID
	public boolean addRecord(Message message);
	//获取聊天室记录
	public ArrayList<Message> selectMessageRecordByR(String userId,String ChatRoomId);//用户ID和联系人ID
	//添加聊天室记录
	public boolean addRecordToR(Message message);
	//查询接收记录
	ArrayList<Message> selectReceiveRecord(String receiveId);
	//查询发送记录
	ArrayList<Message> selectSendRecord(String sendId);



}
