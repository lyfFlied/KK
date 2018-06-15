package User;

import javax.swing.*;  

import client.Client;

import java.awt.*;  
import java.awt.event.ActionEvent;  
import java.awt.event.ActionListener;  
/**
 * 
 * 登陆
 *
 */
public class Login extends JFrame implements ActionListener {  

	//定义组件   
	JButton jbLogin,jbClear,jb3=null;  
	private JButton jbClose;
	JRadioButton jrb1,jrb2=null;  
	JPanel jp1,jp2,jp3,jp4=null;  
	JTextField jtf=null;  
	private JTextField SchoolNumberText;
	JLabel jlb1,jlb2=null;  
	JPasswordField jpf=null;  
	private JPasswordField PassWordText;
	ButtonGroup bg=null;  
	User user = new User();

	private JButton jbRegister;
	private JLabel lblTip;

	public static void main(String[] args) {  

		Login login=new Login();  
	}
	/**
	 * 构造方法
	 */
	public Login()  
	{
		setIconImage(Toolkit.getDefaultToolkit().getImage("E:\\IMG\\233130261198.jpg"));  
		//创建组件  
		jbLogin=new JButton("登录");  
		jbClear=new JButton("重置"); 
		jbClose=new JButton("退出");

		//设置监听  
		jbLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				login();
			}
		});  
		jbClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clear();

			}
		});  

		jbClose.addActionListener(this);  

		jp1=new JPanel();  
		jp2=new JPanel();  
		jp3=new JPanel();  
		jp4=new JPanel();                 

		jlb1=new JLabel("学  号：");  
		jlb2=new JLabel("密  码：");  

		SchoolNumberText=new JTextField(10);  
		PassWordText=new JPasswordField(10);  
		//加入到JPanel中  
		jp1.add(jlb1);  
		jp1.add(SchoolNumberText);  

		jp2.add(jlb2);  
		jp2.add(PassWordText);  

		jp4.add(jbLogin);       //添加按钮

		jbRegister = new JButton("注册");
		jbRegister.setToolTipText("");
		jbRegister.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ToRegister();
				dispose();
			}
		});
		jp4.add(jbRegister);
		jp4.add(jbClear);  
		jp4.add(jbClose);

		//加入JFrame中  
		getContentPane().add(jp1);  
		getContentPane().add(jp2);  
		getContentPane().add(jp3);  
		jp3.setLayout(null);

		lblTip = new JLabel("");
		lblTip.setForeground(Color.RED);
		lblTip.setBounds(97, 13, 207, 18);
		jp3.add(lblTip);
		getContentPane().add(jp4);  

		getContentPane().setLayout(new GridLayout(4,1));            //选择GridLayout布局管理器        
		this.setTitle("实时聊天系统");          
		this.setSize(379,306);         
		this.setLocation(400, 200);           
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);    //设置当关闭窗口时，保证JVM也退出 
		this.setVisible(true);  
		this.setResizable(true);  

	}  


	//登录方法  
	public void login()  
	{
		UserDaoImpl userDaoImpl = new UserDaoImpl();
		User user = new User();
		user= userDaoImpl.login( SchoolNumberText.getText(),PassWordText.getText());
		if(user==null){
			lblTip.setText("登陆失败");
			System.out.println("登陆失败");
		}
		else {
			lblTip.setText("登陆成功");
			System.out.println("登陆成功");
			dispose();
			Client frame = new Client(user);
			frame.setVisible(true);
		}
	}   
	//清空文本框和密码框功能
	public  void clear()  
	{   lblTip.setText("");
	SchoolNumberText.setText("");  
	PassWordText.setText("");  
	}
	//跳往注册界面
	public  void ToRegister()  
	{  
		Register register = new Register();
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}  

} 