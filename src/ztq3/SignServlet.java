package ztq3;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import dat.User;

public class SignServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//获取请求的参数值
		String gdname = request.getParameter("gdname");
		String gdpass = request.getParameter("gdpass");
		String gdemail = request.getParameter("gdemail");		
				
		User user=new User();
	//	System.out.println(gdname+gdpass + gdemail);
		try {
			user.sentdata(gdname,gdpass,gdemail);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

}
}
