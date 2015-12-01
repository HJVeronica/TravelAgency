package main;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.StringTokenizer;
import java.util.Vector;

/** 
 * Connect to TravelAgency Database 
 * Execute Querys
 * 
 * @version 0.9.4 12/02/15
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
	private Vector<String> line;
	private PreparedStatement ps = null;
	
	/** Database Constructor: Connect to DB */
	public Database(){		
		try {
			conn = DriverManager.getConnection(URL,USER,PASSWORD);
			st = conn.createStatement();			
		} catch (SQLException e) {
			System.out.println("Connection Error: "+e.getStackTrace());
		}
	}	

	/**
	 * Initialize the Table of Each Tab
	 * @param int TabNum Each class id for distinguishing classes
	 * @return
	 * */
	public void Table_Initialize(int TabNum){
		try {
			switch(TabNum){
				case 1: 	//Airline Table Initialize
					AirlineSearch(0, null);
					break;
				case 2: 	//Customer Table Initialize
					sql = "select * from customer";
					rs = st.executeQuery(sql);
					String member = null;
					
					while(rs.next()){
						line = new Vector<String>();
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
					break;
				case 3: 	//Airplane Table Initialize
					sql = "select * from airplane";
					rs = st.executeQuery(sql);
					
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
					rs = st.executeQuery(sql);
					
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
		} catch (SQLException e) {
			System.out.println("Connection Error: "+e.getStackTrace());
		}
	}
	
	/** 
	 * Get Airline Names to Add ComboBox Items 
	 * @param
	 * @return Return string vector which contains airline names 
	 * */
	public Vector<String> AirlineComboNames(){
		Vector<String> comboNames = new Vector<String>();
		sql = "select name from airline;";
		try {
			rs = st.executeQuery(sql);
			comboNames.add("전체");
		
			while(rs.next()){
				comboNames.add(rs.getString(1));
			}
		} catch (SQLException e) {
			System.out.println("Connection Error: "+e.getStackTrace());
		}
		return comboNames;
	}
	
	/** 
	 * Show the Tuples That Includes the Selected Airline Name 
	 * @param String name Selected item
	 * @return 
	 * */
	public void AirlineSelectName(String name){
		if(name.equals("전체")){
			sql = "select * from Airline";			
			try {
				rs = st.executeQuery(sql);
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
				System.out.println("Connection Error: "+e.getStackTrace());
			}
		}
		else{
			sql = "select * from airline where name='"+name+"'";
			try {
				rs = st.executeQuery(sql);
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
				System.out.println("Connection Error: "+e.getStackTrace());
			}
		}
	}
	
	/** 
	 * Get the Last ID to Insert Rows to DB 
	 * @param int TabNum Each class id for distinguishing classes
	 * */
	public String getID(int TabNum){
		String id = null;
		int lastId = 0;
		StringTokenizer stoken = null;
		
		try {
			switch(TabNum){
				case 1:		//Airline Class 
					sql = "select aId from airline";
					
					rs = st.executeQuery(sql);
					//Move the cursor to the last row
					rs.last();
					id = rs.getString(1);
					//System.out.println("last id: "+id);
					
					//Separate tokens (A+number)
					stoken = new StringTokenizer(id,"A");
					id = stoken.nextToken();
					
					//String -> Integer (To increase the number of id)
					lastId = Integer.parseInt(id);
					lastId++;
					//Intger -> String (To use the number of id for a new row)
					if(lastId<10)
						id = "0"+String.valueOf(lastId);
					else id = String.valueOf(lastId);
					id = "A"+id;
					//System.out.println("id: "+id);					
				
					break;
				case 2: 	//Customer Class
					sql = "select cId from customer;";
					
					rs = st.executeQuery(sql);
					//Move the cursor to the last row
					rs.last();
					id = rs.getString(1);
					//System.out.println("last id: "+id);
					
					//Separate tokens (C+number)
					stoken = new StringTokenizer(id,"C");
					id = stoken.nextToken();
					
					//String -> Integer (To increase the number of id)
					lastId = Integer.parseInt(id);
					lastId++;
					//Intger -> String (To use the number of id for a new row)
					if(lastId<10)
						id = "0"+String.valueOf(lastId);
					else id = String.valueOf(lastId);
					id = "C"+id;
					//System.out.println("id: "+id);					
				
					break;
				case 3: break;
				case 4: break;
				case 5: break;
			}
		} catch (SQLException e) {
			System.out.println("Connection Error: "+e.getStackTrace());
		}
		
		return id;
	}
	
	/** 
	 * Insert Data into DB 
	 * @param int TabNum Each class id for distinguishing classes
	 * @param String[] rows Acquired data from TextFields
	 * @return
	 * */
	public void InsertData(int TabNum, String[] rows){
		try {
			switch(TabNum){
				case 1:		//Airline Class 
					sql = "insert into airline values(?,?,?,?,?);";
					ps = conn.prepareStatement(sql);
					for(int i=0; i<rows.length; i++)						
						ps.setString(i+1, rows[i]);
					ps.executeUpdate();
					break;
				case 2: 	//Customer Class
					sql = "insert into customer values(?,?,?,?,?,?,?);";
					ps = conn.prepareStatement(sql);
					System.out.println("Length: "+rows.length);
					for(int i=0; i<rows.length; i++){
						if(i==4){
							if(rows[i].equals("X"))
								ps.setInt(i+1, 0);
							else
								ps.setInt(i+1, 1);
						}
						else ps.setString(i+1, rows[i]);
					}
					ps.executeUpdate();
					break;
				case 3: break;
				case 4: break;
				case 5: break;
			}
		} catch (SQLException e) {
			System.out.println("Connection Error: "+e.getStackTrace());
		}
	}
	
	/** 
	 * Delete Data from DB 
	 * @param int TabNum Each class id for distinguishing classes
	 * @param String id Get id of selected row
	 * @return
	 * */
	public void DeleteData(int TabNum, String id){
		try {
			switch(TabNum){
				case 1:		//Airline Class 
					sql = "delete from airline where aId=?";					
					ps = conn.prepareStatement(sql);
					ps.setString(1, id);
					ps.executeUpdate();				
					break;
				case 2: 	//Customer Class
					sql = "delete from customer where cId=?";					
					ps = conn.prepareStatement(sql);
					ps.setString(1, id);
					ps.executeUpdate();				
					break;
				case 3: break;
				case 4: break;
				case 5: break;
			}
		} catch (SQLException e) {
			System.out.println("Connection Error: "+e.getStackTrace());
		}
	}
	
	public void UpdateData(int TabNum, String[] rows){
		try {
			switch(TabNum){
				case 1:		//Airline Class 
					sql = "update airline set aId=?,name=?,country=?,"
							+ "address=?,phone=? where aId=?";
					ps = conn.prepareStatement(sql);
					for(int i=0;i<rows.length;i++)
						ps.setString(i+1, rows[i]);
					ps.setString(rows.length+1, rows[0]);
					ps.executeUpdate();	
					break;
				case 2: 	//Customer Class
					sql = "update customer set cId=?,name=?,address=?,"
							+ "phone=?,membership=?,"
							+ "payment_preference=?,seat=? where cId=?";
					ps = conn.prepareStatement(sql);
					for(int i=0;i<rows.length;i++){
						System.out.println(i+1+" rows["+i+"]: "+rows[i]);
						if(i==4){
							if(rows[i].equals("X"))
								ps.setInt(i+1, 0);
							else
								ps.setInt(i+1, 1);
						}
						else ps.setString(i+1, rows[i]);
					}
					ps.setString(rows.length+1, rows[0]);
					ps.executeUpdate();	
					break;
				case 3: break;
				case 4: break;
				case 5: break;
			}
		} catch (SQLException e) {
			System.out.println("Connection Error: "+e.getStackTrace());
		}
	}
	/**
	 * Search the Information from Airline Table
	 * @param int searchMode Get search mode from the ComboBox
	 * @param String keyWord Get keyword from the TextField
	 * @return
	 * */
	public void AirlineSearch(int searchMode, String keyWord){
		try {
			switch(searchMode){
				case 0: 	//SEARCH_NONE: Update Table
					sql = "select * from airline";
					rs = st.executeQuery(sql);
					break;
				case 1:		//SEARCH_ALL 
					sql = "select * from airline where aId like '%"+keyWord+"%'"
							+ " or name like '%"+keyWord+"%'"+" or country like '%"+keyWord+"%'"
							+ " or address like '%"+keyWord+"%'"+" or phone like '%"+keyWord+"%'";
					rs = st.executeQuery(sql);
					break;
				case 2:		//SEARCH_ID
					sql = "select * from airline where aId like '%"+keyWord+"%'";
					rs = st.executeQuery(sql);
					break;
				case 3:		//SEARCH_NAME
					sql = "select * from airline where name like '%"+keyWord+"%'";
					rs = st.executeQuery(sql);
					break;
				case 4:		//SEARCH_COUNTRY
					sql = "select * from airline where country like '%"+keyWord+"%'";
					rs = st.executeQuery(sql);
					break;
				case 5:		//SEARCH_ADDRESS
					sql = "select * from airline where address like '%"+keyWord+"%'";
					rs = st.executeQuery(sql);
					break;
				case 6:		//SEARCH_PHONE
					sql = "select * from airline where phone like '%"+keyWord+"%'";
					rs = st.executeQuery(sql);
					break;
			}		
		
			//Get column information
			ResultSetMetaData rsMetaData = rs.getMetaData();
			
			//Create an object to obtain the data
			Object[] tempObj = new Object[rsMetaData.getColumnCount()];
			
			//Reset DefaultTableModel
			swing.Airline.model.setRowCount(0);
			
			while(rs.next()){
				for(int i=0 ; i<rsMetaData.getColumnCount(); i++){
					tempObj[i] = rs.getString(i+1);
				}
				swing.Airline.model.addRow(tempObj);
			}
		} catch (SQLException e) {
			System.out.println("Connection Error: "+e.getStackTrace());
		}
	}
	
	/**
	 * Search the Information from Customer Table
	 * @param int searchMode Get search mode from the ComboBox
	 * @param String keyWord Get keyword from the TextField
	 * @return
	 * */
	public void CustomerSearch(int searchMode, String keyWord){
		try {
			switch(searchMode){
				case 0: 	//SEARCH_NONE: Update Table
					sql = "select * from Customer";
					rs = st.executeQuery(sql);
					break;
				case 1:		//SEARCH_ALL 
					String member = null;
					if(keyWord.equals("x") || keyWord.equals("o")){
						if(keyWord.equals("x")){
							member = "0";
						}
						else if(keyWord.equals("o")){
							member = "1";
						}
						sql = "select * from customer where cId like '%"+keyWord+"%'"
								+ " or name like '%"+keyWord+"%'"+" or address like '%"+keyWord+"%'"
								+ " or phone like '%"+keyWord+"%'"+" or membership like '%"+member+"%'"
								+ " or payment_preference like '%"+keyWord+"%'"
								+ " or seat like '%"+keyWord+"%'";
					}
					else{
						sql = "select * from customer where cId like '%"+keyWord+"%'"
								+ " or name like '%"+keyWord+"%'"+" or address like '%"+keyWord+"%'"
								+ " or phone like '%"+keyWord+"%'"
								+ " or payment_preference like '%"+keyWord+"%'"
								+ " or seat like '%"+keyWord+"%'";
					}
					
					rs = st.executeQuery(sql);
					break;
				case 2:		//SEARCH_ID
					sql = "select * from customer where cId like '%"+keyWord+"%'";
					rs = st.executeQuery(sql);
					break;
				case 3:		//SEARCH_NAME
					sql = "select * from customer where name like '%"+keyWord+"%'";
					rs = st.executeQuery(sql);
					break;
				case 4:		//SEARCH_ADDRESS
					sql = "select * from customer where address like '%"+keyWord+"%'";
					rs = st.executeQuery(sql);
					break;
				case 5:		//SEARCH_PHONE
					sql = "select * from customer where phone like '%"+keyWord+"%'";
					rs = st.executeQuery(sql);
					break;
				case 6:		//SEARCH_MEMBERSHIP
					if(keyWord.equals("x"))
						keyWord = "0";
					else if(keyWord.equals("o"))
						keyWord = "1";
					sql = "select * from customer where membership like '%"+keyWord+"%'";
					rs = st.executeQuery(sql);
					break;
				case 7:		//SEARCH_PAYMENT
					sql = "select * from customer where payment_preference like '%"+keyWord+"%'";
					rs = st.executeQuery(sql);
					break;
				case 8:		//SEARCH_SEAT
					sql = "select * from customer where seat like '%"+keyWord+"%'";
					rs = st.executeQuery(sql);
					break;
			}
			
			//Get column information
			ResultSetMetaData rsMetaData = rs.getMetaData();
			
			//Create an object to obtain the data
			Object[] tempObj = new Object[rsMetaData.getColumnCount()];
			
			//Reset DefaultTableModel
			swing.Customer.model.setRowCount(0);
			
			while(rs.next()){
				for(int i=0 ; i<rsMetaData.getColumnCount(); i++){
					tempObj[i] = rs.getString(i+1);
					if(i==4){	//When you get membership information,
						if(rs.getString(i+1).equals("1")){	//true
							tempObj[i] = "O";
						}
						else{	//false
							tempObj[i] = "X";
						}
					}
				}
				swing.Customer.model.addRow(tempObj);
			}
		} catch (SQLException e) {
			System.out.println("Connection Error: "+e.getStackTrace());
		}
	}
	
	/**
	 * Search the Information from Airplane Table
	 * @param int searchMode Get search mode from the ComboBox
	 * @param String keyWord Get keyword from the TextField
	 * @return
	 * */
	public void AirplaneSearch(int searchMode, String keyWord){
		try{
			switch(searchMode){
				case 0: 	//SEARCH_NONE: Update Table
					sql = "select * from Airplane";
					rs = st.executeQuery(sql);
					break;
				case 1:		//SEARCH_ALL 
					
					break;
				case 2:		//SEARCH_ID
					break;
				case 3:		//SEARCH_AIRLINE
					break;
				case 4:		//SEARCH_AIRCRAFT
					break;
				case 5:		//SEARCH_TYPE
					break;
				case 6:		//SEARCH_FIRST
					break;
				case 7:		//SEARCH_BUSINESS
					break;
				case 8:		//SEARCH_ECONOMY
					break;
				case 9:		//SEARCH_LENGTH
					break;
				case 10:	//SEARCH_SIZE
					break;
			}

			//Get column information
			ResultSetMetaData rsMetaData = rs.getMetaData();
			
			//Create an object to obtain the data
			Object[] tempObj = new Object[rsMetaData.getColumnCount()];
			
			//Reset DefaultTableModel
			swing.Airline.model.setRowCount(0);
			
			while(rs.next()){
				for(int i=0 ; i<rsMetaData.getColumnCount(); i++){
					tempObj[i] = rs.getString(i+1);
				}
				swing.Airplane.model.addRow(tempObj);
			}
		} catch (SQLException e) {
			System.out.println("Connection Error: "+e.getStackTrace());
		}
	}
	
	/**
	 * Search the Information from Flight Table
	 * @param int searchMode Get search mode from the ComboBox
	 * @param String keyWord Get keyword from the TextField
	 * @return
	 * */
	public void FlightSearch(int searchMode, String keyWord){
		try{
			switch(searchMode){
				case 0: 	//SEARCH_NONE: Update Table
					sql = "select * from Flight";
					rs = st.executeQuery(sql);
					break;
				case 1:		//SEARCH_ALL				
					break;
				case 2:		//SEARCH_ROUTE
					break;
				case 3:		//SEARCH_DATE
					break;
				case 4:		//SEARCH_TIME
					break;
				case 5:		//SEARCH_NONSTOP
					break;
				case 6:		//SEARCH_STOP_1
					break;
				case 7:		//SEARCH_STOP_2
					break;
			}

			//Get column information
			ResultSetMetaData rsMetaData = rs.getMetaData();
			
			//Create an object to obtain the data
			Object[] tempObj = new Object[rsMetaData.getColumnCount()];
			
			//Reset DefaultTableModel
			swing.Airline.model.setRowCount(0);
			
			while(rs.next()){
				for(int i=0 ; i<rsMetaData.getColumnCount(); i++){
					tempObj[i] = rs.getString(i+1);
				}
				swing.Flight.model.addRow(tempObj);
			}
		} catch (SQLException e) {
			System.out.println("Connection Error: "+e.getStackTrace());
		}
	}
	
	/**
	 * Search the Information from Reservation Table
	 * @param int searchMode Get search mode from the ComboBox
	 * @param String keyWord Get keyword from the TextField
	 * @return
	 * */
	public void ReservationSearch(int searchMode, String keyWord){
		try{
			switch(searchMode){
				case 0: 	//SEARCH_NONE: Update Table
					sql = "select * from Reservation";
					rs = st.executeQuery(sql);
					break;
				case 1:		//SEARCH_ALL 
					
					break;
				case 2:		//SEARCH_ID
					break;
				case 3:		//SEARCH_FLIGHT
					break;
				case 4:		//SEARCH_BOOK_DATE
					break;
				case 5:		//SEARCH_STAFF
					break;
				case 6:		//SEARCH_PAYMENT
					break;
				case 7:		//SEARCH_STATE
					break;
				case 8:		//SEARCH_ROUTE
					break;
			}

			//Get column information
			ResultSetMetaData rsMetaData = rs.getMetaData();
			
			//Create an object to obtain the data
			Object[] tempObj = new Object[rsMetaData.getColumnCount()];
			
			//Reset DefaultTableModel
			swing.Airline.model.setRowCount(0);
			
			while(rs.next()){
				for(int i=0 ; i<rsMetaData.getColumnCount(); i++){
					tempObj[i] = rs.getString(i+1);
				}
				swing.Reservation.model.addRow(tempObj);
			}
		} catch (SQLException e) {
			System.out.println("Connection Error: "+e.getStackTrace());
		}
	}
	
}