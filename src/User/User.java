package User;

import java.util.Date;


/**
 * 用户
 * DB名：  用户表 - user
 * 设计者：李云飞
 * 日期：  2018/6/1
 */
public class User {

	/**
	 * 昵称
	 */
	private String nickname;

	/**
	 * 昵称
	 */
	private String ip;

	/**
	 * 爱好
	 */
	private String hobby;

	/**
	 * 专业
	 */
	private String major;

	/**
	 * 学号
	 */
	private String schoolnumber;

	/**
	 * 密码
	 */
	private String password;

	/**
	 * 姓名
	 */
	private String name;

	/**
	 * 状态
	 */
	private Integer state;

	/**
	 * 创建时间
	 */
	private Date createdAt;

	/**
	 * 更新时间
	 */
	private Date updatedAt;


	/**
	 * 获取 昵称
	 */
	public String getNickname() {
		return nickname;
	}



	/**
	 * 设置 昵称
	 * @param nickname 昵称
	 */
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	/**
	 * 获取 IP
	 */
	public String getIp() {
		return ip;
	}

	/**
	 * 设置 IP
	 * @param nickname 昵称
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}

	/**
	 * 获取 爱好
	 */
	public String getHobby() {
		return hobby;
	}

	/**
	 * 设置 爱好
	 * @param hobby 爱好
	 */
	public void setHobby(String hobby) {
		this.hobby = hobby;
	}

	/**
	 * 获取 专业
	 */
	public String getMajor() {
		return major;
	}

	/**
	 * 设置 专业
	 * @param major 专业
	 */
	public void setMajor(String major) {
		this.major = major;
	}

	/**
	 * 获取 学号
	 */
	public String getSchoolnumber() {
		return schoolnumber;
	}

	/**
	 * 设置 学号
	 * @param schoolnumber 学号
	 */
	public void setSchoolnumber(String schoolnumber) {
		this.schoolnumber = schoolnumber;
	}

	/**
	 * 获取 密码
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * 设置 密码
	 * @param password 密码
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * 获取 姓名
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置 姓名
	 * @param name 姓名
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获取 状态
	 */
	public Integer getState() {
		return state;
	}

	/**
	 * 设置 状态
	 * @param state 状态
	 */
	public void setState(Integer state) {
		this.state = state;
	}

	/**
	 * 获取 创建时间
	 */
	public Date getCreatedAt() {
		return createdAt;
	}

	/**
	 * 设置 创建时间
	 * @param createdAt 创建时间
	 */
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	/**
	 * 获取 更新时间
	 */
	public Date getUpdatedAt() {
		return updatedAt;
	}

	/**
	 * 设置 更新时间
	 * @param updatedAt 更新时间
	 */
	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public User() {
		super();
	}

	public User(String username,String userIp){
		this.nickname=username;
		this.ip = userIp;
	}
}
