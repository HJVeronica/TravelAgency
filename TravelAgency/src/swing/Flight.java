package swing;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerDateModel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import main.Database;

/** 
 * <p><b> Flight Class </b> 
 * : Flight UI & Functions</p>
 * 
 * <p><b>Each method is as follows</b> <br>
 * - {@link #Flight()} : Constructor  <br>
 * - {@link #Search_init()} : UI of Enroll/Delete/Search Buttons and Enroll Form  <br>
 * - {@link #Table_init()} : UI of the Table  <br>
 * - {@link AddnUpdateRow()} : Add a New Row & Update Selected Row <br>
 * - {@link DelRow()} : Delete Selected Row  <br>
 * - {@link itemStateChanged(ItemEvent)} : Radio Button Listener <br>
 * - {@link #actionPerformed(ActionEvent)} : Action Listener  <br>
 * - {@link #tableCellCenter(JTable)} : Set the Alignment of the Rows  <br>
 * - {@link #setColumnSize(JTable)} : Set the Columns' Width & Fix the Columns' Location  </p>
 * 
 * <p><b>Another Class</b> <br>
 * - {@link JTableMouseListener} : Table Mouse Listener (Click, Enter, Exit, Press, Release) 
 * 
 * @version 0.9.9 12/03/15
 * @author 심현정, 김상완, 유란영
 * */
@SuppressWarnings("serial")
public class Flight extends JPanel implements ActionListener{
	//Instance variables
	//Class id for distinguishing tabs(classes)
	private final int CLASS_ID = 4;
	
	//Search mode constant
	private final int SEARCH_NONE = 0;	//For initializing the table rows
	private final int SEARCH_ALL = 1;
	private final int SEARCH_NAME = 2;
	private final int SEARCH_AIRCRAFT = 3;
	private final int SEARCH_DEPARTAIRPORT = 4;
	private final int SEARCH_ARRIVEAIRPORT = 5;
	private final int SEARCH_DEPARTDATE = 6;
	private final int SEARCH_ARRIVDATE = 7;
	private final int SEARCH_SCHEDULE = 8;
	private final int SEARCH_PRICE = 8;
	
	//JButton
	private JButton btnFtSearch;
	
	//JLabel
	private JLabel LaStartAirport;
	private JLabel LaStartDate;
	private JLabel LaStartTime;
	private JLabel LaArriveAirport;
	private JLabel LaArriveDate;
	private JLabel LaArriveTime;
	private JLabel LaPassStop;
		
	//Font, JScrollPane, JTable
	private Font font;
	private JScrollPane scroll;
	private JTable flightTable;
	
	//JComboBox
	private JComboBox<String> comboDeparture;
	private JComboBox<String> comboArrival;
	
	//JSpinner
	private JSpinner departDate;
	private JSpinner departTime;
	private JSpinner arriveDate;
	private JSpinner arriveTime;
		
	//JRadioButton
	private JRadioButton RaPassStop1;
	private JRadioButton RaPassStop2;
	private JRadioButton RaPassStop3;
		
	Database db = null;
		
	//Vector, String
	Vector<String> FlightColNames;
	Vector<String> comboDepartCountry;
	Vector<String> comboArriveCountry;
	
	String departure;
	String arrival;
	String dDate, dTime, aDate, aTime;
	
	//DefaultTableModel
	public static DefaultTableModel model = null;
	
	DateFormat df = null;
	
	/** 
	 * Flight Constructor 
	 * @param
	 * */
	public Flight(){		//Constructor
		setLayout(null);
		setBackground(Color.LIGHT_GRAY);
		
		//Connect to DB
		db = new Database();
				
		Table_init();
		Search_init();
	}
	
	/** 
	 * UI of Enroll/Delete/Search Buttons and Enroll Form 
	 * @param
	 * @return
	 * */
	private void Search_init(){
		//Font for Labels
		font = new Font("",Font.BOLD,12);
		
		//Enroll Form
		LaStartAirport = new JLabel("출발지 공항 : ");
		LaStartAirport.setBounds(80,30,85,20);
		LaStartAirport.setFont(font);
		add(LaStartAirport);
		
		//ComboBox: Departure airport
		comboDepartCountry = new Vector<String>();
		comboDepartCountry = db.CountryComboNames();
		comboDeparture = new JComboBox<String>(comboDepartCountry);
		comboDeparture.setSelectedItem(0);
		comboDeparture.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if(comboDeparture.getSelectedItem()!=null){
					departure = comboDeparture.getSelectedItem().toString();
					System.out.println("departure: "+departure);
				}
			}			
		});
		comboDeparture.setBounds(180, 30, 200, 20);
		add(comboDeparture);
		
		LaArriveAirport = new JLabel("도착지 공항 : ");
		LaArriveAirport.setBounds(400,30,85,20);
		LaArriveAirport.setFont(font);
		add(LaArriveAirport);
		
		
		//ComboBox: Arrival airport
		comboArriveCountry = new Vector<String>();
		comboArriveCountry = db.CountryComboNames();
		comboArrival = new JComboBox<String>(comboArriveCountry);
		comboArrival.setSelectedItem(0);
		comboArrival.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if(comboDeparture.getSelectedItem()!=null){
					arrival = comboArrival.getSelectedItem().toString();
					System.out.println("arrival: "+arrival);
				}
			}			
		});
		comboArrival.setBounds(500, 30, 200, 20);
		add(comboArrival);		
		
		LaStartDate = new JLabel("출발 날짜 : ");
		LaStartDate.setBounds(80,70,85,20);
		LaStartDate.setFont(font);
		add(LaStartDate);
		
		//JSpinner Setting: Depart date
		final SpinnerDateModel spModel1 = new SpinnerDateModel();
		departDate = new JSpinner(spModel1);
		df = new SimpleDateFormat("yyyy-MM-dd");
		
		JSpinner.DateEditor editor = new JSpinner.DateEditor(departDate, "yyyy-MM-dd");
		JFormattedTextField ftf = editor.getTextField();
		ftf.setEditable(false);		
		ftf.setBackground(Color.WHITE);
		departDate.setEditor(editor);
		//dDate = df.format(spModel1.getValue());
		System.out.println("dDate: "+ dDate);
		departDate.addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent e) {
				Date value = (Date) spModel1.getValue();
				if(value != null){
					dDate = df.format(value);
					System.out.println("dDate: "+ dDate);
				}
			}
			
		});
		departDate.setBounds(180, 70, 200, 20);
		departDate.setFont(font);
		add(departDate);		
		
		
		LaStartTime = new JLabel("출발 시간 : ");
		LaStartTime.setBounds(80,110,85,20);
		LaStartTime.setFont(font);
		add(LaStartTime);
		
		//JSpinner Setting: Depart time
		final SpinnerDateModel spModel2 = new SpinnerDateModel();
		spModel2.setCalendarField(Calendar.MINUTE);
		
		departTime = new JSpinner(spModel2);
		departTime.setEditor(new JSpinner.DateEditor(departTime, "HH:mm"));
		JFormattedTextField ftf2 = editor.getTextField();
		ftf2.setEditable(false);	
		ftf2.setBackground(Color.WHITE);
		dTime = df.format(spModel2.getValue());
		//System.out.println("dTime: "+ dTime);
		departTime.addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent e) {
				Date value = (Date) spModel2.getValue();
				Date next = (Date) spModel2.getNextValue();
				if(value != null && next != null){
					dTime = df.format(value);
					System.out.println("dTime: "+ dTime);
				}
			}
			
		});
		departTime.setBounds(180, 110, 200, 20);
		departTime.setFont(font);
		add(departTime);
		
		LaArriveDate = new JLabel("도착 날짜 : ");
		LaArriveDate.setBounds(400,70,85,20);
		LaArriveDate.setFont(font);
		add(LaArriveDate);
		
		//JSpinner Setting: Arrive Date
		final SpinnerDateModel spModel3 = new SpinnerDateModel();
		arriveDate = new JSpinner(spModel3);
		df = new SimpleDateFormat("yyyy-MM-dd");
		
		JSpinner.DateEditor editor2 = new JSpinner.DateEditor(arriveDate, "yyyy-MM-dd");
		JFormattedTextField ftf3 = editor.getTextField();
		ftf3.setEditable(false);	
		ftf3.setBackground(Color.WHITE);
		arriveDate.setEditor(editor2);
		//aDate = df.format(spModel3.getValue());
		System.out.println("aDate: "+ aDate);
		arriveDate.addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent e) {
				Date value = (Date) spModel3.getValue();
				Date next = (Date) spModel3.getNextValue();
				if(value != null && next != null){
					aDate = df.format(value);
					System.out.println("aDate: "+ aDate);
				}
			}
			
		});
		arriveDate.setBounds(500, 70, 200, 20);
		arriveDate.setFont(font);
		add(arriveDate);
		
		LaArriveTime = new JLabel("도착 시간 : ");
		LaArriveTime.setBounds(400,110,85,20);
		LaArriveTime.setFont(font);
		add(LaArriveTime);
		
		//JSpinner Setting: Arrive Time
		final SpinnerDateModel spModel4 = new SpinnerDateModel();
		spModel2.setCalendarField(Calendar.MINUTE);
		
		arriveTime = new JSpinner(spModel4);
		arriveTime.setEditor(new JSpinner.DateEditor(arriveTime, "HH:mm"));

		JFormattedTextField ftf4 = editor.getTextField();
		ftf4.setEditable(false);	
		ftf4.setBackground(Color.WHITE);
		aTime = df.format(spModel4.getValue());
		//System.out.println("aTime: "+ aTime);
		arriveTime.addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent e) {
				Date value = (Date) spModel4.getValue();
				Date next = (Date) spModel4.getNextValue();
				if(value != null && next != null){
					aTime = df.format(value);
					System.out.println("aTime: "+ aTime);
				}
			}
			
		});
		arriveTime.setBounds(500, 110, 200, 20);
		arriveTime.setFont(font);
		add(arriveTime);
		
		LaPassStop = new JLabel("경유 횟수 : ");
		LaPassStop.setBounds(80,155,85,20);
		LaPassStop.setFont(font);
		add(LaPassStop);
		
		//Stop Radio Button
		RaPassStop1 = new JRadioButton("직항");
		RaPassStop2 = new JRadioButton("1회");
		RaPassStop3 = new JRadioButton("2회");
		ButtonGroup bg = new ButtonGroup();
		bg.add(RaPassStop1);
		bg.add(RaPassStop2);
		bg.add(RaPassStop3);
		RaPassStop1.setBackground(Color.LIGHT_GRAY);
		RaPassStop2.setBackground(Color.LIGHT_GRAY);
		RaPassStop3.setBackground(Color.LIGHT_GRAY);
		RaPassStop1.setBounds(180,155,55,20);
		RaPassStop2.setBounds(250,155,50,20);
		RaPassStop3.setBounds(300,155,50,20);
		add(RaPassStop1);
		add(RaPassStop2);
		add(RaPassStop3);
				
		//Button: Search
		btnFtSearch = new JButton("검색");
		btnFtSearch.setBounds(848, 155, 62, 30);
		btnFtSearch.addActionListener(this);
		add(btnFtSearch);
		
	}
	
	/** 
	 * UI of the Table
	 * @param
	 * @return
	 * */
	private void Table_init(){
		//Initialize Column Names
		FlightColNames = new Vector<>();
		FlightColNames.add("항공편명");
		FlightColNames.add("비행기 제작사");
		FlightColNames.add("출발지 공항");
		FlightColNames.add("도착지 공항");
		FlightColNames.add("출발 날짜");
		FlightColNames.add("출발 시간");
		FlightColNames.add("도착 날짜");
		FlightColNames.add("도착 시간");
		FlightColNames.add("스케줄");
		FlightColNames.add("요금");
		
		model = new DefaultTableModel(FlightColNames, 0){
			//Prevent editing cells
			public boolean isCellEditable(int row, int column){
				if(column>=0)	return false;
				else return true;
			}
		};

		//Initialize DefaultTableModel
		db.FlightSearch(0, null);
		
		//Create a table
		flightTable = new JTable(model);		
		
		//Table settings
		//Enable auto row sorting
		flightTable.setAutoCreateRowSorter(true);
		//Add mouse listener
		flightTable.addMouseListener(new JTableMouseListener());
		//Fix the column's location
		flightTable.getTableHeader().setReorderingAllowed(false);		
		//Enable multiple selection
		flightTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		
		tableCellCenter(flightTable);
		setColumnSize(flightTable);
		scroll = new JScrollPane(flightTable);
		scroll.setBounds(70, 200, 850, 400);
		add(scroll);
	}
	
	/** 
	 * Radio Button Item Listener 
	 * @param ItemEvent e Get Objects
	 * */
	public void itemStateChanged(ItemEvent e) {
		Object source = e.getSource();	//Get Selected Object
		
		
	}
	
	/** 
	 * Action Listener 
	 * @param ActionEvent e Get Objects
	 * @return
	 * */
	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();	//Get Selected Object
	
		/*if(source.equals(btnFtSearch)){
			String keyWord = tfSearch.getText().trim();
			if(keyWord.isEmpty())
				JOptionPane.showMessageDialog(null, "검색어를 입력하세요.",
						"Message",JOptionPane.ERROR_MESSAGE);
			else{		//Set the Search Mode & Search
				switch(searchMode){
					case 0: db.CustomerSearch(SEARCH_ALL,keyWord); break;
					case 1: db.CustomerSearch(SEARCH_ID,keyWord); break;
					case 2: db.CustomerSearch(SEARCH_NAME,keyWord); break;
					case 3: db.CustomerSearch(SEARCH_ADDRESS,keyWord); break;
					case 4: db.CustomerSearch(SEARCH_PHONE,keyWord); break;
					case 5: db.CustomerSearch(SEARCH_MEMBER,keyWord); break;
					case 6: db.CustomerSearch(SEARCH_PAYMENT,keyWord); break;
					case 7: db.CustomerSearch(SEARCH_SEAT,keyWord); break;
				}
			}
			
			//Reset
			tfSearch.setText(null);
			cbSearch.setSelectedIndex(0);
		}
		else if(source.equals(btnShowAll)){
			db.CustomerSearch(SEARCH_NONE, null);
		}*/
	}
	
	/** 
	 * Set the Alignment of the Rows to Center 
	 * @param JTable t Get table
	 * @return
	 * */
	private void tableCellCenter(JTable t){
		DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
		//Set the alignment of the renderer to center
		dtcr.setHorizontalAlignment(SwingConstants.CENTER);
		
		TableColumnModel tcm = t.getColumnModel();
		
		//Set the alignment of all the rows to center
		for(int i=0;i<tcm.getColumnCount();i++){
			tcm.getColumn(i).setCellRenderer(dtcr);
		}
	}
	
	/** 
	 * Set the Columns' Width & Fix the Columns' Location 
	 * @param JTable t
	 * @return
	 * */
	private void setColumnSize(JTable t){
		TableColumnModel tcm = t.getColumnModel();
		
		//Fix the columns' location
		for(int i=0;i<tcm.getColumnCount();i++){
			tcm.getColumn(i).setResizable(false);
		}
	}
	
	/** 
	 * Table Mouse Listener (Click, Enter, Exit, Press, Release)
	 * implements MouseListener
	 * */
	private class JTableMouseListener implements MouseListener{
		/**
		 * Mouse Click Event
		 * @param MouseEvent e Get Object
		 * @return
		 */
		public void mouseClicked(MouseEvent e) {
		}

		@Override
		public void mouseEntered(MouseEvent e) {}

		@Override
		public void mouseExited(MouseEvent e) {}

		@Override
		public void mousePressed(MouseEvent e) {}

		@Override
		public void mouseReleased(MouseEvent e) {}		
	}
}