package message;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;


public class Message implements Serializable
{
	//枚举类型的用于表明命令类型的command成员
	public enum Commands{
		LOG_IN,//登陆
		LOG_OUT,//退出
		QUERY_USERS,//获取用户列表
		QUERY_ALL_CHAT_ROOMS, //查询所有群聊列表       
		QUERY_MY_CHAT_ROOMS,//查询加入的群聊
		QUERY_ROOM_MEMBERS,//查询群聊成员
		HEART_BEAT,
		MSG_P2P,//个人对个人的消息
		MSG_ALL,//对所有人发消息
		MSG_P2R,//聊天室消息
		CREATE_CHAT_ROOM,//创建群聊
		JOIN_CHAT_ROOM,//加入群聊
		LEAVE_CHAT_ROOM,//离开群聊
		SET_USER_NAME; 
	};
	//指名携带数据的类型的FieldType枚举
	public enum FieldType{
		USER_ID,//用户ID
		USER_NAME,//用户名
		PASS_WD,//密码
		PEER_ID,//单聊对象的ID
		ROOM_ID,//聊天室ID
		USER_LIST,//用户列表
		ROOM_LIST_ALL,//所有房间列表
		ROOM_LIST_ME,//我的聊天室列表
		ROOM_MEMBERS,//用户列表
		MSG_TXT,//消息
		RESPONSE_STATUS,//响应状态
		ENCODING,//编码
		TIME;//时间
	};
	private static final long serialVersionUID = 1L;
	private Map<FieldType,String> fields=new HashMap<>();//TODO：泛型支持，任意消息类型，包括文本，图片，语音，视频，文件等
	private Commands command;
	public Message()
	{
	}
	public Message(Commands command)
	{
		this.command=command;
	}
	public Commands getCommand()
	{
		return this.command;
	}
	public Message set(FieldType key,String value)
	{
		if(key!=null&&value!=null)
		{
			fields.put(key,value);
		}
		return this;
	}
	public String get(FieldType key)
	{
		return fields.get(key);
	}

	/*public byte[] toBytes()
	{
		return SerializeHelper.serialize(this);
	}

	public ByteBuffer wrap()
	{
		byte[] frame=toBytes();
		return ByteBuffer.wrap(frame);
	}*/

}