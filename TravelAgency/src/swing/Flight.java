package swing;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.SQLException;
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
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
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
 * - {@link #Airline()} : Constructor  <br>
 * - {@link #Enroll_init()} : UI of Enroll/Delete Buttons and Enroll Form  <br>
 * - {@link #Table_init()} : UI of Table and Search Part  <br>
 * - {@link #AddnUpdateRow()} : Add a New Row & Update Selected Row <br>
 * - {@link #DelRow()} : Delete Selected Row  <br>
 * - {@link #actionPerformed(ActionEvent)} : Action Listener  <br>
 * - {@link #tableCellCenter(JTable)} : Set the Alignment of the Rows  <br>
 * - {@link #setColumnSize(JTable)} : Set the Columns' Width & Fix the Columns' Location  </p>
 * <p><b>Another Class</b> <br>
 * - {@link JTableMouseListener} : Table Mouse Listener (Click, Enter, Exit, Press, Release) 
 * 
 * @version 0.9.8 12/03/15
 * @author 심현정, 김상완, 유란영
 * */
public class Flight extends JPanel implements ActionListener{
	private final int CLASS_ID = 4;
	
	//JButton
	private JButton btnAlSearch;
	
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
	private JComboBox comboDeparture;
	private JComboBox comboArrival;
	
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
		
	//DefaultTableModel
	public static DefaultTableModel model = null;
	
	DateFormat df = null;
	
	/** Flight Constructor 
	 * @throws SQLException */
	public Flight() throws SQLException{		//Constructor
		setLayout(null);
		setBackground(Color.LIGHT_GRAY);
		
		//Connect to DB
		db = new Database();
				
		Table_init();
		Search_init();
	}
	
	private void Search_init(){
		//Label에 사용할 폰트
		font = new Font("",Font.BOLD,12);
		//검색 부분 양식
		LaStartAirport = new JLabel("출발지 공항 : ");
		LaStartAirport.setBounds(80,30,85,20);
		LaStartAirport.setFont(font);
		add(LaStartAirport);
		
		//ComboBox: Airline Names (Statistics)
		comboDepartCountry = new Vector<String>();
		comboDepartCountry = db.CountryComboNames();
		comboDeparture = new JComboBox<String>(comboDepartCountry);
		comboDeparture.setSelectedItem(0);
		comboDeparture.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if(comboDeparture.getSelectedItem()!=null){
					String name = comboDeparture.getSelectedItem().toString();
					//db.CustomerSelectName(name);
				}
			}			
		});
		comboDeparture.setBounds(180, 30, 200, 20);
		add(comboDeparture);
		
		LaArriveAirport = new JLabel("도착지 공항 : ");
		LaArriveAirport.setBounds(400,30,85,20);
		LaArriveAirport.setFont(font);
		add(LaArriveAirport);
		
		
		//ComboBox: Airline Names (Statistics)
		comboArriveCountry = new Vector<String>();
		comboArriveCountry = db.CountryComboNames();
		comboArrival = new JComboBox<String>(comboArriveCountry);
		comboArrival.setSelectedItem(0);
		comboArrival.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if(comboDeparture.getSelectedItem()!=null){
					String name = comboArrival.getSelectedItem().toString();
					//db.CustomerSelectName(name);
				}
			}			
		});
		comboArrival.setBounds(500, 30, 200, 20);
		add(comboArrival);		
		
		LaStartDate = new JLabel("출발 날짜 : ");
		LaStartDate.setBounds(80,70,85,20);
		LaStartDate.setFont(font);
		add(LaStartDate);
		
		final SpinnerDateModel spModel1 = new SpinnerDateModel();
		departDate = new JSpinner(spModel1);
		df = new SimpleDateFormat("yyyy-MM-dd");
		
		JSpinner.DateEditor editor = new JSpinner.DateEditor(departDate, "yyyy-MM-dd");
		JFormattedTextField ftf = editor.getTextField();
		ftf.setEditable(false);		
		ftf.setBackground(Color.WHITE);
		departDate.setEditor(editor);
		departDate.addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent e) {
				Date value = (Date) spModel1.getValue();
				Date next = (Date) spModel1.getNextValue();
				if(value != null && next != null){
					System.out.println("value: "+ df.format(value)+"\t"
							+ "next: "+df.format(next));
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
		
		final SpinnerDateModel spModel2 = new SpinnerDateModel();
		spModel2.setCalendarField(Calendar.MINUTE);
		
		departTime = new JSpinner(spModel2);
		departTime.setEditor(new JSpinner.DateEditor(departTime, "HH:mm"));
		JFormattedTextField ftf2 = editor.getTextField();
		ftf2.setEditable(false);	
		ftf2.setBackground(Color.WHITE);
		
		departTime.addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent e) {
				Date value = (Date) spModel2.getValue();
				Date next = (Date) spModel2.getNextValue();
				if(value != null && next != null){
					System.out.println("value: "+ df.format(value)+"\t"
							+ "next: "+df.format(next));
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
		
		final SpinnerDateModel spModel3 = new SpinnerDateModel();
		arriveDate = new JSpinner(spModel3);
		df = new SimpleDateFormat("yyyy-MM-dd");
		
		JSpinner.DateEditor editor2 = new JSpinner.DateEditor(arriveDate, "yyyy-MM-dd");
		JFormattedTextField ftf3 = editor.getTextField();
		ftf3.setEditable(false);	
		ftf3.setBackground(Color.WHITE);
		arriveDate.setEditor(editor2);
		arriveDate.addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent e) {
				Date value = (Date) spModel3.getValue();
				Date next = (Date) spModel3.getNextValue();
				if(value != null && next != null){
					System.out.println("value: "+ df.format(value)+"\t"
							+ "next: "+df.format(next));
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
		
		final SpinnerDateModel spModel4 = new SpinnerDateModel();
		spModel2.setCalendarField(Calendar.MINUTE);
		
		arriveTime = new JSpinner(spModel4);
		arriveTime.setEditor(new JSpinner.DateEditor(arriveTime, "HH:mm"));

		JFormattedTextField ftf4 = editor.getTextField();
		ftf4.setEditable(false);	
		ftf4.setBackground(Color.WHITE);
		
		arriveTime.addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent e) {
				Date value = (Date) spModel4.getValue();
				Date next = (Date) spModel4.getNextValue();
				if(value != null && next != null){
					System.out.println("value: "+ df.format(value)+"\t"
							+ "next: "+df.format(next));
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
				
		//검색 버튼
		btnAlSearch = new JButton("검색");
		btnAlSearch.setBounds(848, 155, 62, 30);
		btnAlSearch.addActionListener(this);
		add(btnAlSearch);
		
	}
		
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
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Object source = e.getSource();	//선택된 버튼 가져오기
		
		if(source.equals(btnAlSearch)){
			setVisible(false);
		}		
	}
	
	/** 테이블 내용 가운데 정렬 */
	private void tableCellCenter(JTable t){
		DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
		dtcr.setHorizontalAlignment(SwingConstants.CENTER);		//Renderer을 가운데 정렬로
		
		TableColumnModel tcm = t.getColumnModel();
		
		//전체 열에 가운데 정렬
		for(int i=0;i<tcm.getColumnCount();i++){
			tcm.getColumn(i).setCellRenderer(dtcr);
			//모델에서 컬럼 갯수만큼 컬럼 가져와서 for문으로
			//각각의 셀 Renderer을 아까 생성한 dtcr에 set
		}
	}
	
	/** 테이블 셀 크기 변경 및 고정 */
	private void setColumnSize(JTable t){
		TableColumnModel tcm = t.getColumnModel();
		
		//tcm.getColumn(0).setPreferredWidth(5);
		/*tcm.getColumn(0).setPreferredWidth(25);
		tcm.getColumn(1).setPreferredWidth(100);
		tcm.getColumn(2).setPreferredWidth(50);
		tcm.getColumn(3).setPreferredWidth(400);
		tcm.getColumn(4).setPreferredWidth(100);*/
		
		//전체 열 사이즈 변경 불가
		for(int i=0;i<tcm.getColumnCount();i++){
			tcm.getColumn(i).setResizable(false);
		}
	}
	
	/** Table Mouse Listener (Click, Enter, Exit, Press, Release)*/
	private class JTableMouseListener implements MouseListener{
		public void mouseClicked(MouseEvent e) {
			JTable jtable = (JTable)e.getSource();
			int row = jtable.getSelectedRow();
			int col = jtable.getSelectedColumn();
			DefaultTableModel model = (DefaultTableModel)jtable.getModel();
			
			System.out.println(model.getValueAt(row, 1));	//눌려진 행의 부분에서 1번째(2번째 열) 값 출력
			System.out.println(model.getValueAt(row, col));	//눌려진 행과 열에 해당하는 선택된 데이터 하나 출력			
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