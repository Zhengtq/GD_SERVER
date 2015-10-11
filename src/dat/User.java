package dat;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;

import com.sun.xml.internal.bind.v2.model.core.ID;

import dat.DB;
import dat.ZZ;

class ZZ
{
	public ZZ() {
		id = 0;
		score = 0.0f;
		// TODO Auto-generated constructor stub
	}
	int id;
	float score;
}

class MyComprator implements Comparator {
    public int compare(Object arg0, Object arg1) {
        ZZ t1=(ZZ)arg0;
        ZZ t2=(ZZ)arg1;
       return t1.score <= t2.score? 1:-1;

    }
}


public class User {
	public static  ZZ[] idscore = new ZZ[100];
	
	
	public User() {
        for(int i=0;i<100;i++)
            idscore[i]=new ZZ();
		
	}
	

	private static float[][] locationScore = new float[3][3];
	
	public static void fuzhi()
	{

		for(int i = 0; i < 100; i++)
		{
			idscore[i].id = 0;
			idscore[i].score = 0;
			
		}
		
		idscore[0].id = 0;
		locationScore[0][0] = (float) 0.8;
		locationScore[0][1] = (float) 0.4;
		locationScore[0][2] = (float) 0.4;
		
		locationScore[1][0] = (float) 0.4;
		locationScore[1][1] = (float) 0.8;
		locationScore[1][2] = (float) 0.4;
		
		locationScore[2][0] = (float) 0.4;
		locationScore[2][1] = (float) 0.4;
		locationScore[2][2] = (float) 0.8;
		
	}
	
	public static boolean check(String username, String password)
	 throws Exception {
     User u = null;
     Connection conn = DB.getConn();
     String sql = "select * from user2 where name = '" + username + "'";
     Statement stmt = DB.getStatement(conn);
     ResultSet rs = DB.getResultSet(stmt, sql);
    try {
	if(!rs.next()) {
		return false;
	  } else {
		if(!password.equals(rs.getString("password"))) {
			return false;

		}	    
	 }
	
    } catch (SQLException e) {
	  e.printStackTrace();
    } finally {
	  DB.close(rs);
	  DB.close(stmt);
	  DB.close(conn);
     }
    return true;
    }

	
	
	public static void sentdata(String gdname, String gdpass, String gdemail) throws SQLException
	{
	     Connection conn = DB.getConn();
	     String sql = "insert into user2 values('"+gdname+"','"+gdpass+"','"+gdemail+"')";
	     System.out.println(sql);
	     Statement stmt1 = DB.getStatement(conn);
	     stmt1.executeUpdate(sql);
	     
	}
	
	public static String zsort(String sql, Connection conn, int tb, int te, int loc, String xml) throws SQLException
	{
        for(int i=0;i<100;i++)
            idscore[i].score = 0;;
		
		 Statement stmt1 = DB.getStatement(conn);
	     ResultSet rs = null;
	     rs = stmt1.executeQuery(sql);
	      

	     float timescore = 0;
	     int id = 0;
	     while(rs.next()) 
	     {

	    	 
	    	 if(tb <= rs.getInt("timebegin") && te >= rs.getInt("timeend"))
	    		 timescore = (float) (rs.getInt("timeend") - rs.getInt("timebegin"))/(float) (te - tb + 1);	    	 
	    	 else if(tb <= rs.getInt("timebegin") && te <= rs.getInt("timeend"))
	    		 timescore = (float) (te - rs.getInt("timebegin"))/(float) (rs.getInt("timeend") - tb + 1);
	    	 else if(tb >= rs.getInt("timebegin") && te >= rs.getInt("timeend"))
	    		 timescore = (float) (rs.getInt("timeend") - tb)/(float) (te - rs.getInt("timebegin") + 1);
	    	 else if(tb >= rs.getInt("timebegin") && te <= rs.getInt("timeend"))
	    		 timescore = (float) (te - tb)/(float) (rs.getInt("timeend") - rs.getInt("timebegin") + 1);
	    	 else 
	    		 timescore = 0;
	    	 
	    	 idscore[id].score = locationScore[rs.getInt("location")][loc] + timescore;
	    	 idscore[id].id= rs.getInt("id");
	    	    	 
	    	 id++;    	 
	 		}	 
	     
	     Arrays.sort(idscore, new MyComprator()); 
		
	     Statement stmt2 = DB.getStatement(conn);
	     ResultSet rs1 = null;	      
	     for(int i=0;i<id;i++)
	     {
	    	 
	    	 String sql1 = "select * from datascheme1.`group` where id =" + idscore[i].id;
		     rs1 = stmt2.executeQuery(sql1);
		     rs1.next();
		     
	    	 xml = xml + "<user id= " + idscore[i].id +">"  
	                 + "<name>" + rs1.getString("name") + "</name>"
	                 +"<purpose>" + rs1.getInt("purpose") + "</purpose>"
	                 + "</user>" ;
	     }
	     
		return xml;
		
	}
	
	
	@SuppressWarnings("unchecked")
	public static String sort(String purpose, String timeBegin, String timeEnd,String location) throws SQLException
	{
		 String xml = "<?xml version='1.0' encoding='UTF-8'?>"  
	                + "<Users>"; 
	     fuzhi();	 
	     Connection conn = DB.getConn();
	     int tb, te, loc;
    	 tb =Integer.parseInt(timeBegin);
    	 te =Integer.parseInt(timeEnd);
    	 loc = Integer.parseInt(location);
	     
	     //初次筛选
	     String sql1 = "SELECT `group`.* FROM `group`,(SELECT `purpose` FROM `group` GROUP BY `purpose` HAVING COUNT(1) > 1 ) AS `t2` WHERE `group`.`purpose` ="
	     +purpose + " AND `group`.`timeend` >= " + timeBegin + " AND `group`.`timebegin` <= " + timeEnd;

	     xml = zsort(sql1,conn,tb,te,loc,xml);	     

	     
	     String sql2 = "SELECT `group`.* FROM `group`,(SELECT `purpose` FROM `group` GROUP BY `purpose` HAVING COUNT(1) > 1 ) AS `t2` WHERE `group`.`purpose` ="
	    	     +purpose + " AND( `group`.`timeend` < " + timeBegin + " OR `group`.`timebegin` > " + timeEnd +")";
	     
	     xml = zsort(sql2,conn,tb,te,loc,xml);

	     
	     String sql3 = "SELECT `group`.* FROM `group`,(SELECT `purpose` FROM `group` GROUP BY `purpose` HAVING COUNT(1) > 1 ) AS `t2` WHERE `group`.`purpose` !="
	    	     +purpose + " AND( `group`.`timeend` >= " + timeBegin + " AND `group`.`timebegin` <= " + timeEnd +")";
	     
	     xml = zsort(sql3,conn,tb,te,loc,xml);

	     
	     String sql4 = "SELECT `group`.* FROM `group`,(SELECT `purpose` FROM `group` GROUP BY `purpose` HAVING COUNT(1) > 1 ) AS `t2` WHERE `group`.`purpose` !="
	    	     +purpose + " AND( `group`.`timeend` < " + timeBegin + " OR `group`.`timebegin` > " + timeEnd +")";
	     
	     xml = zsort(sql4,conn,tb,te,loc,xml);

	     xml = xml +  "</Users>";
	     
	     
	     return xml;
	     

	      
	     
	}
	
	
}
