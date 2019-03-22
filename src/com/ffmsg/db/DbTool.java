package com.ffmsg.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import org.json.JSONObject;
import com.ffmsg.tools.Tools;

public class DbTool {
	public static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");

	
	//更新数据库(app_count)第三方短信余量
	public static boolean upOtherBlance(String other_balance,String username,String password){
		boolean b=false;
		Connection con=null;
		PreparedStatement ps=null;
		System.out.println("upOtherBlance:");
		try {
			con=DB.getInstance().getConnection();
			String sql="update app_account set other_balance= ?where username=? and password=?";
			ps=con.prepareStatement(sql);
			ps.setString(1, other_balance);
			ps.setString(2,username);
			ps.setString(3, password);
			ps.execute();
			b=true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		
		
		return b;
	}
	
	// 更新数据库(app_count)短信余量
	public static boolean upbalance_ob(String last_balance, String username) {
		Connection connection = null;
		PreparedStatement ps = null;
		boolean up_balance_status = false;
 
		try {
			connection = DB.getInstance().getConnection();
			String sqlString = "update app_account set  balance=?where username=?";
			ps = connection.prepareStatement(sqlString);
			ps.setString(1, last_balance);
			ps.setString(2, username);
			int i = ps.executeUpdate();
			if (i > 0) {
				up_balance_status = true;
			}
		} catch (Exception e) {
			Tools.SaveErrorLog(e);
			e.printStackTrace();
			if(connection!=null){
				try {
					connection.close();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					Tools.SaveErrorLog(e1);
					e1.printStackTrace();
				}
			}
		} 
		return up_balance_status;
	}

	// 插入修改记录(operater_balance)
	public static boolean insetob(Map<String, String> map, String id,int sum) {
		Connection connection = null;
		PreparedStatement ps = null;
		boolean up_insetob_status = false;
		try {
			connection = DB.getInstance().getConnection();
			String sqlString = "insert  into operater_balance(operator,username,operator_time,first_balance,last_balance,remarks) values(?,?,?,?,?,?)";

			ps = connection.prepareStatement(sqlString);
			ps.setString(1, id);
			ps.setString(2, map.get("username"));
			ps.setString(3, dateFormat.format(new Date()).toString());
			ps.setString(4, map.get("first_balance"));//修改前
			ps.setString(5, map.get("last_balance"));//充值的条数
			ps.setString(6, map.get("remarks"));
			ps.execute();
			up_insetob_status = true;
		} catch (Exception e) {
			Tools.SaveErrorLog(e);
			e.printStackTrace();
			if(connection!=null){
				try {
					connection.close();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					Tools.SaveErrorLog(e1);
					e1.printStackTrace();
				}
			}
		} 
		return up_insetob_status;

	}

	// 更新数据库(app_count)密码(root)
	public static boolean isuppassword(String newpwd, String username, String oldpwd) {
		Connection connection = null;
		PreparedStatement ps = null;
		boolean up_pwd_status = false;

		try {
			connection = DB.getInstance().getConnection();
			String sql = "update app_account   set  password=?,update_time = ? where username=? and password=?";

			ps = connection.prepareStatement(sql);
			ps.setString(1, newpwd);
			ps.setString(2, dateFormat.format(new Date()).toString());
			ps.setString(3, username);
			ps.setString(4, oldpwd);
			int i = ps.executeUpdate();
			if (i > 0) {
				up_pwd_status = true;
			}
		} catch (Exception e) {
			Tools.SaveErrorLog(e);
			e.printStackTrace();
			if(connection!=null){
				try {
					connection.close();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					Tools.SaveErrorLog(e1);
					e1.printStackTrace();
				}
			}
		} 
		return true;
	}

	// 插入app用户(app_account)
	// 向数据库(app_count)插入账号信息
	public static boolean insertsql(Map<String, String> map, String pwd, int systen_balance) {
		Connection connection = null;
		PreparedStatement ps = null;
		boolean insert_acc_status = false;

		try {
			connection = DB.getInstance().getConnection();
			String sqlString = "insert  into app_account(username,password,balance,update_time,add_time ,role) values(?,?,?,?,?,?)";
			ps = connection.prepareStatement(sqlString);
			ps.setString(1, map.get("username"));
			ps.setString(2, pwd);
			ps.setInt(3, systen_balance);
			ps.setString(4, dateFormat.format(new Date()).toString());
			ps.setString(5, dateFormat.format(new Date()).toString());
			ps.setString(6, "-1");		
			ps.execute();
			insert_acc_status = true;
		} catch (Exception e) {
			Tools.SaveErrorLog(e);
			e.printStackTrace();
			if(connection!=null){
				try {
					connection.close();
				} catch (SQLException e1) {		
					Tools.SaveErrorLog(e1);
					e1.printStackTrace();
				}
			}
		} 
		return insert_acc_status;
	}

	// 插入发送短信记录(send_count)
	// 向数据库(send_count)插入发送信息
	public static boolean insersendsql(Map<String, String> map, int status, String back_info, String id) {
		Connection connection = null;
		PreparedStatement ps = null;
		boolean insert_send_status = false;

		try {
			String sqlString = "insert into send_count(username,content,phone,send_time,status,back_info,app_id) value(?,?,?,?,?,?,?)";
			connection = DB.getInstance().getConnection();
			ps = connection.prepareStatement(sqlString);
			ps.setString(1,map.get("username") );
			ps.setString(2, map.get("msg"));
			ps.setString(3, map.get("p"));
			ps.setString(4, dateFormat.format(new Date()).toString());
			ps.setInt(5, status);
			ps.setString(6, back_info);
			ps.setString(7, id);
			ps.execute();
			insert_send_status = true;
		} catch (Exception e) {
			Tools.SaveErrorLog(e);
			e.printStackTrace();
			if(connection!=null){
				try {
					connection.close();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					Tools.SaveErrorLog(e1);
					e1.printStackTrace();
				}
			}
		} 
		return true;
	}

	// 查找是否有该app
	// 寻找app信息
	public static JSONObject findapp(String username, String password) {
		JSONObject back_find_info = new JSONObject();
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = DB.getInstance().getConnection();
			String sqlString = "select balance from app_account where username= ?and password=?";
			ps = connection.prepareStatement(sqlString);
			ps.setString(1, username);
			ps.setString(2, password);
			System.out.println("sqlString"+sqlString);
			rs = ps.executeQuery();
			while (rs.next()) {
				back_find_info.put("balance", rs.getInt(1));				
			}
		} catch (Exception e) {
			Tools.SaveErrorLog(e);
			e.printStackTrace();
			if(connection!=null){
				try {
					connection.close();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					Tools.SaveErrorLog(e1);
					e1.printStackTrace();
				}
			}
		} 

		return back_find_info;
	}

	// 发送短信后更新余量
	// 短信发送成功更新app_account balance
	public static boolean upbalance(String username, String password, int balance ) {
		Connection connection = null;
		PreparedStatement ps = null;
		boolean up_balance_status = false;
		try {
			connection = DB.getInstance().getConnection();
			String sqlString = "update app_account set balance=? where username=? and password=?";
			ps = connection.prepareStatement(sqlString);
			ps.setInt(1, balance);
			ps.setString(2, username);
			ps.setString(3, password);
			ps.executeUpdate();
		   up_balance_status = true;
		} catch (Exception e) {
			e.printStackTrace();
			Tools.SaveErrorLog(e);
		}
		return up_balance_status;

	}

	// 更改密码app
	public static boolean isuppassword_app(String username, String pwd) {
		Connection connection = null;
		PreparedStatement ps = null;
		boolean up_pwd_status = false;

		try {
			connection = DB.getInstance().getConnection();
			String sql = "update app_account   set  password=?,update_time = ? where username=? ";

			ps = connection.prepareStatement(sql);
			ps.setString(1, pwd);
			ps.setString(2, dateFormat.format(new Date()).toString());
			ps.setString(3, username);
			int i = ps.executeUpdate();
			if (i > 0) {
				up_pwd_status = true;
			}
		} catch (Exception e) {
			Tools.SaveErrorLog(e);
			e.printStackTrace();
			if(connection!=null){
				try {
					connection.close();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					Tools.SaveErrorLog(e1);
					e1.printStackTrace();
				}
			}
		} 
		return true;
	}

	// 查找app用户id
	public static String findId(String username, String password) {
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String id = "";
		try {
			String sqlString = "select id from app_account where username=? and password =?";
			connection = DB.getInstance().getConnection();
			ps = connection.prepareStatement(sqlString);
			ps.setString(1, username);
			ps.setString(2, password);
			rs = ps.executeQuery();
			while (rs.next()) {
				id = rs.getString(1);
			}
		} catch (Exception e) {
			Tools.SaveErrorLog(e);
			e.printStackTrace();
			if(connection!=null){
				try {
					connection.close();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					Tools.SaveErrorLog(e1);
					e1.printStackTrace();
				}
			}
		} 

		return id;
	}

	// 查找app管理员id
	public static String findrootId(String username) {
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String id = "";
		try {
			String sqlString = "select id from app_account where username=?";
			connection = DB.getInstance().getConnection();
			ps = connection.prepareStatement(sqlString);
			ps.setString(1, username);
			rs = ps.executeQuery();
			while (rs.next()) {
				id = rs.getString(1);
			}
		} catch (Exception e) {
			Tools.SaveErrorLog(e);
			e.printStackTrace();
			if(connection!=null){
				try {
					connection.close();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					Tools.SaveErrorLog(e1);
					e1.printStackTrace();
				}
			}
		} 

		return id;
	}
	
	
	
	/**
	 * 修改用户余额
	 * 
	 * @param paramMap
	 * @return
	 */
	@SuppressWarnings("resource")
	public static boolean UpdateUserBalance(Map<String, Object> paramMap){
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			int userBalance = -1,user_id=0;
			//查询客户余额
			String sqlString = "select balance,id from app_account where username=?";
			connection = DB.getInstance().getConnection();
			connection.setAutoCommit(false);
			ps = connection.prepareStatement(sqlString);
			ps.setString(1, paramMap.get("username").toString());
			rs = ps.executeQuery();
			while(rs.next()){
				userBalance = rs.getInt("balance");
				user_id = rs.getInt("id");
			}
			
			if(userBalance != -1){
				sqlString = "insert into operater_balance(operator,username,operator_time,first_balance,last_balance,remarks,type)"
						+ "values(?,?,?,?,?,?,?,?)"; 
				ps = connection.prepareStatement(sqlString);
				ps.setString(1, user_id+"");
				ps.setString(2, paramMap.get("username").toString());
				ps.setString(3, dateFormat.format(new Date()).toString());
				ps.setInt(4, userBalance);
				ps.setInt(5, Integer.valueOf(paramMap.get("msgNum").toString()));
				ps.setString(6, paramMap.get("remarks").toString());
				ps.setInt(7, Integer.valueOf(paramMap.get("type").toString()));
				ps.execute();
				
				
				sqlString = "update app_account set balance=balance+? where id=?";
				ps = connection.prepareStatement(sqlString);
				ps.setInt(1, Integer.valueOf(paramMap.get("msgNum").toString()));
				ps.setInt(2, user_id);
				ps.execute();
				
				connection.commit();
			}else{
				connection.rollback();
			}
		} catch (Exception e) {
			e.printStackTrace();		
			Tools.SaveErrorLog(e);	
			if(connection != null){
				try {
					connection.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		}
		return false;
	}
	

}
