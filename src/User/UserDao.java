package User;

import java.util.ArrayList;

public interface UserDao {
	//登陆
	public User login(String SchoolNumber,String passWord);
	//注册
	public User registration(String nickName,String hobby,String major,String SchoolNumber,String passWord,String name);
	//修改用户状态
	public int updateStatic(User user);
	//查询
	public ArrayList<User> selectUserList();
	//登录时间
	public boolean updateInTime(User user);
	//退出时间
	public boolean updateOutTime(User user);
}
