package ztq3;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import dat.User;

public class LoginServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//��ȡ����Ĳ���ֵ
		String userName = request.getParameter("username");
		String userPass = request.getParameter("userpass");
		
//		System.out.println("��û��ת��֮ǰ��"+userName+"---"+userPass);
		//GET��ʽ���������봦��
		userName = new String(userName.getBytes("ISO8859-1"),"UTF-8");
		userPass = new String(userPass.getBytes("ISO8859-1"),"UTF-8");

//		System.out.println("��ת��֮��---"+userName+"---"+userPass);
		
		String xmlString = "<?xml version='1.0' encoding='UTF-8'?>"  
                + "<Users>"  
				
                + "<user id='1'>"  
                + "<name>aa</name >"
                +"<password>123<password>"
                + "</user>"  
                
                + "<user id='2'>"  
                + "<name>bb</name >"
                +"<password>234<password>"
                + "</user>"  
                
                + "</Users>"; 
		
		User user=new User();
			try {
				if(user.check(userName, userPass))
				{   
					response.getOutputStream().write("1".getBytes()); 
				}  
				else{
					//��Ӧ��½ʧ�ܵ���Ϣ
					response.getOutputStream().write(xmlString.getBytes());					
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doGet(request, response);
	}

}
