package User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
/**
 * 
 * @Title: UserDaoImpl.java
 * @Package User 
 * @Description: TODO(用户的数据库操作类)    
 * @date 2018年3月11日 上午9:31:21 
 * @version V1.0   
 * @author 李云飞
 */
public class UserDaoImpl implements UserDao{
	/**
	 * 登陆
	 */
	@Override
	public User login(String SchoolNumber, String password) {
		// TODO Auto-generated method stub
		Connection conn = DBUtil.open();
		String sql = "select * from user where SchoolNumber=? and passWord=? ";
		try {
			PreparedStatement pstmt = 	conn.prepareStatement(sql);
			pstmt.setString(1, SchoolNumber);
			pstmt.setString(2, password);
			ResultSet rs= pstmt.executeQuery();

			if(rs.next()){
				User u = new User();
				u.setNickname(rs.getString("nickname"));
				u.setHobby(rs.getString("hobby"));
				u.setMajor(rs.getString("major"));
				u.setSchoolnumber(rs.getString("schoolnumber"));
				u.setPassword(rs.getString("password"));
				u.setName(rs.getString("name"));
				u.setState(rs.getInt("state"));
				if(rs.getInt("state")==1){
					return null;
				}
				//更新用户状态，上线
				updateStatic(u);
				//更新上线时间
				updateInTime(u);
				return u;
			}


		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		return null;
	}
	/**
	 * 注册
	 */
	public User registration(String nickName,String passWord,String major,String SchoolNumber,String hobby,String name){

		Connection conn = DBUtil.open();
		String sql = "insert into user ( nickName , passWord ,major, SchoolNumber,hobby,name) values ( ?,?,?,?,?,?)";
		try {
			PreparedStatement pstmt = 	conn.prepareStatement(sql);
			pstmt.setString(1, nickName);
			pstmt.setString(2, passWord);
			pstmt.setString(3, major);
			pstmt.setString(4, SchoolNumber);
			pstmt.setString(5, hobby);
			pstmt.setString(6, name);
			pstmt.executeUpdate();  
			User u = new User();		
			u.setSchoolnumber(SchoolNumber);
			u.setPassword(passWord);
			return u;




		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		return null;
	}

	/**
	 * 查询用户列表
	 */
	@Override
	public ArrayList<User> selectUserList() {
		// TODO Auto-generated method stub
		ArrayList<User> list  = new ArrayList<User>();
		Connection conn = DBUtil.open();
		String sql = "select * from user where state= ? ";
		try {
			PreparedStatement pstmt = 	conn.prepareStatement(sql);
			pstmt.setInt(1, 1);
			ResultSet rs= pstmt.executeQuery();
			while(rs.next()){
				User u = new User();
				u.setNickname(rs.getString("nickname"));
				u.setHobby(rs.getString("hobby"));
				u.setMajor(rs.getString("major"));
				u.setSchoolnumber(rs.getString("schoolnumber"));
				u.setPassword(rs.getString("password"));
				u.setName(rs.getString("name"));
				u.setState(rs.getInt("state"));
				list.add(u);
			}
			return list;



		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		return null;
	}
	/**
	 * 修改用户状态
	 */
	@Override
	public int updateStatic(User user) {
		// TODO Auto-generated method stub
		int state = 0;
		ArrayList<User> list  = new ArrayList<User>();
		Connection conn = DBUtil.open();
		String sql = "update user set state=? where schoolnumber = ?";
		if(user.getState()==0){
			state = 1;
		}
		try {
			PreparedStatement pstmt = 	conn.prepareStatement(sql);
			pstmt.setInt(1, state);
			pstmt.setString(2, user.getSchoolnumber());
			pstmt.executeUpdate();  

			return 1;


		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		return 0;
	}
	/**
	 * 更新登录时间
	 */
	@Override
	public boolean updateInTime(User user) {
		// TODO Auto-generated method stub
		String time = null;
		Date day=new Date();   
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		time = df.format(day); 
		Connection conn = DBUtil.open();
		String sql = "update user set inTime=? where schoolnumber = ?";
		try {
			PreparedStatement pstmt = 	conn.prepareStatement(sql);
			pstmt.setString(1, time);
			pstmt.setString(2, user.getSchoolnumber());
			pstmt.executeUpdate();  

			return true;


		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		return false;
	}
	/**
	 * 更新退出时间
	 */
	@Override
	public boolean updateOutTime(User user) {
		// TODO Auto-generated method stub
		String time = null;
		Date day=new Date();   
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		time = df.format(day); 
		Connection conn = DBUtil.open();
		String sql = "update user set outTime=? where schoolnumber = ?";
		try {
			PreparedStatement pstmt = 	conn.prepareStatement(sql);
			pstmt.setString(1, time);
			pstmt.setString(2, user.getSchoolnumber());
			pstmt.executeUpdate();  

			return true;


		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		return false;
	}

}
