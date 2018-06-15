package User;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import java.awt.Toolkit;  
/**
 * 
 * 注册
 *
 */
public class Register extends JFrame implements ActionListener {  

	User user = new User();
	//定义组件   
	JButton jbLogin,jbClear,jb3=null;  
	private JButton jbClose;
	JPanel jp1,jp2,jp3,jp4=null;  
	JTextField jtf=null;  
	private JTextField NickNameText;
	JLabel jlb1,jlb2=null;  
	JPasswordField jpf=null;  
	private JPasswordField PassWordText;
	ButtonGroup bg=null;  

	private JButton jbRegister;
	private JPanel panel;
	private JPanel panel_1;
	private JPanel panel_2;
	private JLabel lblNewLabel;
	private JLabel lblNewLabel_1;
	private JLabel lblNewLabel_2;
	private JLabel lblNewLabel_3;
	private JTextField MajorText;
	private JTextField SchoolNumberText;
	private JTextField HobbyText;
	private JTextField NameText;

	public static void main(String[] args) {  

		Register Register=new Register();  
	}  
	/**
	 * 构造方法
	 */
	public Register()  
	{
		setIconImage(Toolkit.getDefaultToolkit().getImage("E:\\IMG\\233130261198.jpg"));  
		//创建组件  
		jbLogin=new JButton("登录");  
		jbClear=new JButton("重置"); 
		jbClose=new JButton("退出");

		//设置监听  
		jbClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clear();
			}
		});  
		jbClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose(); 
			}
		});  
		jbLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ToLogin();
			}
		});  
		jp1=new JPanel();  
		jp2=new JPanel();  
		jp4=new JPanel();                 

		jlb1=new JLabel("昵称:");  
		jlb2=new JLabel("爱好：");  

		NickNameText=new JTextField(10);  
		//加入到JPanel中  
		jp1.add(jlb1);  
		jp1.add(NickNameText);  

		jp2.add(jlb2);

		jp4.add(jbLogin);       //添加按钮

		jbRegister = new JButton("注册");
		jp4.add(jbRegister);
		jp4.add(jbClear);  
		jp4.add(jbClose);
		jbRegister.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				register();

			}
		});
		//加入JFrame中  
		getContentPane().add(jp1);  

		panel = new JPanel();
		getContentPane().add(panel);

		lblNewLabel_1 = new JLabel("学号:");
		panel.add(lblNewLabel_1);

		SchoolNumberText = new JTextField();
		panel.add(SchoolNumberText);
		SchoolNumberText.setColumns(10);

		panel_2 = new JPanel();
		getContentPane().add(panel_2);

		lblNewLabel_3 = new JLabel("姓名:");
		panel_2.add(lblNewLabel_3);

		NameText = new JTextField();
		panel_2.add(NameText);
		NameText.setColumns(10);

		panel_1 = new JPanel();
		getContentPane().add(panel_1);

		lblNewLabel_2 = new JLabel("密码:");
		panel_1.add(lblNewLabel_2);
		PassWordText=new JPasswordField(10);  
		panel_1.add(PassWordText);
		jp3=new JPanel();  
		getContentPane().add(jp3);  

		lblNewLabel = new JLabel("专业：");
		jp3.add(lblNewLabel);

		MajorText = new JTextField();
		jp3.add(MajorText);
		MajorText.setColumns(10);
		getContentPane().add(jp2);  

		HobbyText = new JTextField();
		jp2.add(HobbyText);
		HobbyText.setColumns(10);
		getContentPane().add(jp4);  

		getContentPane().setLayout(new GridLayout(8,1));            //选择GridLayout布局管理器        
		this.setTitle("实时聊天系统");          
		this.setSize(600,500);         
		this.setLocation(400, 200);           
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);    //设置当关闭窗口时，保证JVM也退出 
		this.setVisible(true);  
		this.setResizable(true);  

	}  

	//清空文本框和密码框  
	public  void clear()  
	{  
		NickNameText.setText("");  
		PassWordText.setText("");  
		HobbyText.setText("");  
		MajorText.setText("");  
		SchoolNumberText.setText("");  
		NameText.setText("");  
	}
	//跳转到登陆界面
	public void ToLogin(){
		Login login = new Login();
		dispose();
	}
	//注册用户信息
	public void register(){
		UserDaoImpl userDaoImpl = new UserDaoImpl();
		User user = userDaoImpl.registration(NickNameText.getText(), PassWordText.getText(), MajorText.getText(), SchoolNumberText.getText(), HobbyText.getText(), NameText.getText());
		if(user==null){
			System.out.println("注册失败");
		}
		else {
			System.out.println("注册成功");
		}
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}  

} 