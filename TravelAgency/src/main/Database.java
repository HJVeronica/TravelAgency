package main;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import java.util.StringTokenizer;
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
	private String sql = "";
	private Statement st = null;
	private ResultSet rs = null;
	private Scanner in = new Scanner(System.in);
	private int result;
	private Vector<String> line;
	private Vector<Vector> select;
	
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
	public void ExecuteQuery(String sql, int num){
		try {
			if(num==0)
				result = st.executeUpdate(sql);
			else
				rs = st.executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void Table_Initialize(int TabNum){
		switch(TabNum){
			case 1: 	//Airline Table Initialize
				sql = "select * from Airline";
				ExecuteQuery(sql,1);
				
				try {
					while(rs.next()){
						line = new Vector<String>();
						//in.add("");
						line.add(rs.getString("aId"));
						line.add(rs.getString("name"));
						line.add(rs.getString("country"));
						line.add(rs.getString("address"));
						line.add(rs.getString("phone"));
						
						swing.Airline.model.addRow(line);
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
				break;
			case 2: 	//Customer Table Initialize
				sql = "select * from customer";
				ExecuteQuery(sql,1);
				String member = null;
				
				try {
					while(rs.next()){
						line = new Vector<String>();
						//in.add("");
						line.add(rs.getString("cId"));
						line.add(rs.getString("name"));
						line.add(rs.getString("phone"));
						line.add(rs.getString("address"));
						if(rs.getString("membership").equals("0"))
							member = "�̺���";
						else member = "����";
						line.add(member);
						line.add(rs.getString("payment_preference"));
						line.add(rs.getString("seat"));
						
						swing.Customer.model.addRow(line);
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
				break;
			case 3: 	//Airplane Table Initialize
				sql = "select * from airplane";
				ExecuteQuery(sql,1);
				
				try {
					while(rs.next()){
						line = new Vector<String>();
	
						line.add(rs.getString("pId"));
						line.add(rs.getString("aId"));
						line.add(rs.getString("aircraft"));
						line.add(rs.getString("type"));
						line.add(rs.getString("firstclass")+"��");
						line.add(rs.getString("business")+"��");
						line.add(rs.getString("economy")+"��");
						line.add(rs.getString("length")+"m");
						line.add(rs.getString("plane_size"));
						
						swing.Airplane.model.addRow(line);
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
				sql = "select name "
						+ "from airline, airplane"
						+ "where airline.aId=airplane.aId";
				break;
			case 4: 	//Flight Table Initialize
				break;
			case 5: 	//Reservation Table Initialize
				sql = "select * from reservation";
				ExecuteQuery(sql,1);
				
				try {
					while(rs.next()){
						line = new Vector<String>();
	
						line.add(rs.getString("cId"));
						line.add(rs.getString("flight_name"));
						line.add(rs.getString("book_date"));
						line.add(rs.getString("staff"));
						line.add(rs.getString("payment"));
						line.add(rs.getString("state"));
						line.add(rs.getString("route"));
						
						swing.Reservation.model.addRow(line);
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
				break;
		}
	}
	
	public void AirlineSearch(int SearchMode) throws SQLException{
		switch(SearchMode){
			case 0: break;
			case 1: break;
			case 2: break;
			case 3: break;
		}
	}
	
	public Vector<String> AirlineComboNames(Vector<String> comboNames){
		sql = "select name from airline;";
		ExecuteQuery(sql, 1);
		comboNames.add("��ü");
		try {
			while(rs.next()){
				comboNames.add(rs.getString(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return comboNames;
		//System.out.println(comboNames);
	}
	
	/** Show the Tuples That Includes the Selected Airline Name */
	public void AirlineSelectName(String name){
		if(name.equals("��ü")){
			sql = "select * from Airline";
			ExecuteQuery(sql,1);

			try {
				while(rs.next()){
					line = new Vector<String>();
					line.add(rs.getString("aId"));
					line.add(rs.getString("name"));
					line.add(rs.getString("country"));
					line.add(rs.getString("address"));
					line.add(rs.getString("phone"));
					
					swing.Airline.model.addRow(line);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		else{
			sql = "select * from airline where name='"+name+"'";
			ExecuteQuery(sql,1);			
			
			try {
				while(rs.next()){
					line = new Vector<String>();
					line.add(rs.getString("aId"));
					line.add(rs.getString("name"));
					line.add(rs.getString("country"));
					line.add(rs.getString("address"));
					line.add(rs.getString("phone"));
					
					swing.Airline.model.addRow(line);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	/** Get the Last ID to Insert Rows to DB */
	public String getID(int TabNum){
		String id = null;
		
		switch(TabNum){
			case 1:		//Airline Class 
				sql = "select aId from airline;";
				
				try {
					//Move the cursor to the last row
					rs.last();
					id = rs.getString(1);
					System.out.println("last id: "+id);
					
					//Separate tokens (A+number)
					StringTokenizer st = new StringTokenizer(id,"A");
					id = st.nextToken();
					
					//String -> Integer (To increase the number of id)
					int lastId = Integer.parseInt(id);
					lastId++;
					//Intger -> String (To use the number of id for a new row)
					if(lastId<10)
						id = "0"+String.valueOf(lastId);
					else id = String.valueOf(lastId);
					id = "A"+id;
					System.out.println("id: "+id);					
				} catch (SQLException e) {
					e.printStackTrace();
				}
				break;
			case 2: 	//Customer Class
				sql = "select cId from customer;";
				
				try {
					while(rs.next()){
						
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
				break;
			case 3: break;
			case 4: break;
			case 5: break;
		}
		
		return id;
	}
	
	/** Insert Data into DB */
	public void InsertData(int TabNum, String[] rows){
		switch(TabNum){
		case 1:		//Airline Class 
			sql = "insert into airline values('"+rows[0]+"','"+rows[1]
					+"','"+rows[2]+"','"+rows[3]+"','"+rows[4]+"');";
			ExecuteQuery(sql, 0);			
			//System.out.println("sql: "+sql);
			break;
		case 2: 	//Customer Class
			sql = "";
			
			try {
				while(rs.next()){
					
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;
		case 3: break;
		case 4: break;
		case 5: break;
		}
	}
	
	/** Delete Data from DB */
	public void DeleteData(int TabNum, String str){
		switch(TabNum){
		case 1:		//Airline Class 
			sql = "delete * from airline where ";
			
			break;
		case 2: 	//Customer Class
			sql = "";
			
			break;
		case 3: break;
		case 4: break;
		case 5: break;
		}
	}
	
	/** Close Database */
	public void closeDatabase()
	{
		try
		{
			if( conn != null ){	conn.close(); }
			if( st != null ){ st.close(); }
			if( rs != null ){ rs.close(); }
		}
		catch (SQLException e)
		{
			System.out.println("[�ݱ� ����]\n" +  e.getStackTrace());
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