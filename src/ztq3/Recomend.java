package ztq3;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import dat.User;

public class Recomend extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//获取请求的参数值
		String purpose = request.getParameter("purpose");
		String timeBegin = request.getParameter("timeBegin");
		String timeEnd = request.getParameter("timeEnd");
		String location = request.getParameter("location");		
		

		
		
		User user=new User();
		
		String xml;
		
	//	System.out.println(gdname+gdpass + gdemail);
		try {  
			xml = user.sort(purpose, timeBegin, timeEnd, location);
			
			System.out.println(xml);

			
			response.getOutputStream().write(xml.getBytes());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
}
}