package main;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import java.util.Vector;

/** 
 * Connect to TravelAgency Database 
 * Execute Query
 * 
 * @version 1.01 11/29/15
 * @author Hyunjeong Shim, ����, ������
 */

public class Database{
	private final String URL = "jdbc:mysql://localhost:3306/travelagency";
	private final String USER = "root";
	private final String PASSWORD = "1234";
	private Connection conn = null;
	private static String sql = "";
	private static Statement st = null;
	private static ResultSet rs = null;
	private Scanner in = new Scanner(System.in);
	private static int result;
	
	/** Database Constructor: Connect to DB */
	public Database(){
		try {
			conn = DriverManager.getConnection(URL,USER,PASSWORD);
			st = conn.createStatement();
			System.out.println("DB is connected.");
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}	
	
	/** Execute Query */
	public static void ExecuteQuery(String sql, int num){
		try {
			if(num==0)
				result = st.executeUpdate(sql);
			else
				rs = st.executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public Vector<Vector> Table_Initialize(int TabNum, Vector<Vector> data) throws SQLException{
		Vector<String> in;
		
		switch(TabNum){
			case 1: 	//Airline Table Initialize
				sql = "select * from Airline";
				ExecuteQuery(sql,1);
				
				while(rs.next()){
					in = new Vector<String>();
					//in.add("");
					in.add(rs.getString("aId"));
					in.add(rs.getString("name"));
					in.add(rs.getString("country"));
					in.add(rs.getString("address"));
					in.add(rs.getString("phone"));
					
					data.add(in);
				}
				break;
			case 2: 	//Customer Table Initialize
				break;
			case 3: 	//Airplane Table Initialize
				break;
			case 4: 	//Flight Table Initialize
				break;
			case 5: 	//Reservation Table Initialize
				break;
		}
		
		return data;
	}
	
	public static void AirlineSearch(int SearchMode) throws SQLException{
		switch(SearchMode){
			case 0: break;
			case 1: break;
			case 2: break;
			case 3: break;
		}
	}
	
	/*public void insert(){
		
		while(true){
			System.out.print("������ ������ �Է��ϼ���: ");
			b_name = in.next();
			System.out.print("������ ���ڸ� �Է��ϼ���: ");
			author = in.next();
			System.out.print("������ ����� �Է��ϼ���(Ex.20151015): ");
			releaseDate = in.next();
			System.out.print("������ ���ǻ縦 �Է��ϼ���: ");
			publisher = in.next();
			System.out.print("������ �뿩�Ḧ �Է��ϼ���: ");
			b_price = in.nextInt();
			
			sql = "insert book(name,author,releaseDate,publisher,b_price) valuse('"
					+ b_name + "','" + author + "','" + releaseDate + "','"
							+ publisher + "'," + b_price + ");";
			
			ExecuteQuery(sql,0);
			
			if(result > 0) System.out.println("������ ��ϵǾ����ϴ�.");
			else System.out.println("���� ��Ͽ� �����Ͽ����ϴ�.");
			
			System.out.print("������ �� ����Ͻðڽ��ϱ�? (y/n): ");
			flag = in.next();
			
			if (flag.equals("y"))
				continue;
			else
				break;
		}
	}
	
	public void delete(){
		while(true){
			System.out.print("������ �������� �Է��ϼ���: ");
			b_name = in.next();
			
			sql = "delete from book where b_name='"+b_name+"';";
			ExecuteQuery(sql,0);
			
			if(result > 0) System.out.println("������ �����Ǿ����ϴ�.");
			else System.out.println("���� ������ �����Ͽ����ϴ�.");
			
			System.out.print("������ �� �����Ͻðڽ��ϱ�? (y/n): ");
			flag = in.next();
			
			if (flag.equals("y"))
				continue;
			else
				break;
		}
	}
	
	public void lend() throws SQLException{
		Calendar cal = new GregorianCalendar(Locale.KOREA);
		nYear = cal.get(Calendar.YEAR);
		nMonth = cal.get(Calendar.MONTH)+1;
		nDate = cal.get(Calendar.DATE);
		
		while(true){
			System.out.print("�뿩�Ͻô� ������ �Է��ϼ���: ");
			c_name = in.next();
			System.out.print("�뿩�� �������� �Է��ϼ���: ");
			b_name = in.next();
			
			sql = "select bID from book where b_name='"+b_name+"';";
			ExecuteQuery(sql, 1);
			bID = rs.getInt(1);
			
			sql = "select cID from customer where c_name='"+c_name+"';";
			ExecuteQuery(sql, 1);
			cID = rs.getInt(1);
			
			lentDate = Integer.toString(nYear)
					+Integer.toString(nMonth)
					+Integer.toString(nDate);
			System.out.println("LentDate: "+lentDate);
			
			nDate += 7;
			dueDate = Integer.toString(nYear)
					+Integer.toString(nMonth)
					+Integer.toString(nDate);
			System.out.println("DueDate: "+dueDate);
			
			
			if(result > 0) System.out.println("������ �뿩�Ǿ����ϴ�.");
			else System.out.println("���� �뿩�� �����Ͽ����ϴ�.");
			
			System.out.print("������ �� �뿩�Ͻðڽ��ϱ�? (y/n): ");
			flag = in.next();
			
			if (flag.equals("y"))
				continue;
			else
				break;
		}		
	}
	
	public void returnBook() throws SQLException{
		while(true){
			System.out.print("�ݳ��Ͻô� ������ �Է��ϼ���: ");
			c_name = in.next();
			System.out.print("�ݳ��� �������� �Է��ϼ���: ");
			b_name = in.next();
			
			sql = "select bID from lentbook where b_name='"+b_name+"';";
			ExecuteQuery(sql, 1);
			bID = rs.getInt(1);
			
			sql = "select cID from customer where c_name='"+c_name+"';";
			ExecuteQuery(sql, 1);
			cID = rs.getInt(1);
			
			sql = "delete from lentbook where bID="+bID+" and cID="+cID+";";
			ExecuteQuery(sql,0);
			
			if(result > 0) System.out.println("������ �ݳ��Ǿ����ϴ�.");
			else System.out.println("���� �ݳ��� �����Ͽ����ϴ�.");
			
			System.out.print("������ �� �ݳ��Ͻðڽ��ϱ�? (y/n): ");
			flag = in.next();
			
			if (flag.equals("y"))
				continue;
			else
				break;
		}		
	}
	
	public void extend() throws SQLException{
		while(true){
			System.out.print("�����Ͻô� ������ �Է��ϼ���: ");
			c_name = in.next();
			
			sql = "select cID from customer where c_name='"+c_name+"';";
			ExecuteQuery(sql, 1);
			cID = rs.getInt(1);
			
			sql = "select bID from lentbook where cID="+cID+";";
			ExecuteQuery(sql, 1);
			bID = rs.getInt(1);
			
			sql = "select * from book where bID="+cID+";";
			ExecuteQuery(sql,1);
			while(rs.next()){
				System.out.println("");
			}
			
			System.out.print("�ݳ��� �������� �Է��ϼ���: ");
			b_name = in.next();
			
			sql = "select bID from lentbook where b_name='"+b_name+"';";
			ExecuteQuery(sql, 1);
			bID = rs.getInt(1);
			
			
			
			sql = "delete from lentbook where bID="+bID+" and cID="+cID+";";
			ExecuteQuery(sql,0);
			
			if(result > 0) System.out.println("������ �ݳ��Ǿ����ϴ�.");
			else System.out.println("���� �ݳ��� �����Ͽ����ϴ�.");
			
			System.out.print("������ �� �ݳ��Ͻðڽ��ϱ�? (y/n): ");
			flag = in.next();
			
			if (flag.equals("y"))
				continue;
			else
				break;
		}		
	}*/
}