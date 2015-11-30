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
 * @author Hyunjeong Shim, 김상완, 유란영
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
							member = "미보유";
						else member = "보유";
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
						line.add(rs.getString("firstclass")+"개");
						line.add(rs.getString("business")+"개");
						line.add(rs.getString("economy")+"개");
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
		comboNames.add("전체");
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
		if(name.equals("전체")){
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
			System.out.println("[닫기 오류]\n" +  e.getStackTrace());
		}
	}
}