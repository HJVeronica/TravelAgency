package swing;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import main.Database;

/** Flight UI & Functions */
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
	private JTable airlineTable;
	
	//JTextField
	private JTextField TeStartAirport;
	private JTextField TeArriveAirport;
	
	//JComboBox
	private JComboBox CoStartDate;
	private JComboBox CoStarttime;
	private JComboBox CoArriveDate;
	private JComboBox CoArriveTime;

		
	//JRadioButton
	private JRadioButton RaPassStop1;
	private JRadioButton RaPassStop2;
	private JRadioButton RaPassStop3;
		
	Database db = null;
		
	//Vector, String
	Vector<String> FlightColNames;
	String alCombo[] = {"항공편명","비행기ID","출발지공항","도착지공항",
			"출발날짜","출발시간","도착날짜","도착시간","스케줄","요금"};
		
	//DefaultTableModel
	public static DefaultTableModel model = null;
	
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
		
		TeStartAirport = new JTextField();
		TeStartAirport.setBounds(180, 30, 200, 20);
		add(TeStartAirport);
		
		LaArriveAirport = new JLabel("도착지 공항 : ");
		LaArriveAirport.setBounds(400,30,85,20);
		LaArriveAirport.setFont(font);
		add(LaArriveAirport);
		
		TeArriveAirport = new JTextField();
		TeArriveAirport.setBounds(500, 30, 200, 20);
		add(TeArriveAirport);
		
		LaStartDate = new JLabel("출발 날짜 : ");
		LaStartDate.setBounds(80,70,85,20);
		LaStartDate.setFont(font);
		add(LaStartDate);
		
		CoStartDate = new JComboBox();
		CoStartDate.addItem("2015-11-30");
		CoStartDate.addActionListener(this);
		CoStartDate.setBounds(180, 70, 200, 20);
		add(CoStartDate);
		
		
		LaStartTime = new JLabel("출발 시간 : ");
		LaStartTime.setBounds(80,110,85,20);
		LaStartTime.setFont(font);
		add(LaStartTime);
		
		CoStarttime = new JComboBox();
		CoStarttime.addItem("09:20");
		CoStarttime.addActionListener(this);
		CoStarttime.setBounds(180, 110, 200, 20);
		add(CoStarttime);
		
		LaArriveDate = new JLabel("도착 날짜 : ");
		LaArriveDate.setBounds(400,70,85,20);
		LaArriveDate.setFont(font);
		add(LaArriveDate);
		
		CoArriveDate = new JComboBox();
		CoArriveDate.addItem("2015-11-31");
		CoArriveDate.addActionListener(this);
		CoArriveDate.setBounds(500, 70, 200, 20);
		add(CoArriveDate);
		
		LaArriveTime = new JLabel("도착 시간 : ");
		LaArriveTime.setBounds(400,110,85,20);
		LaArriveTime.setFont(font);
		add(LaArriveTime);
		
		CoArriveTime = new JComboBox();
		CoArriveTime.addItem("06:30");
		CoArriveTime.addActionListener(this);
		CoArriveTime.setBounds(500, 110, 200, 20);
		add(CoArriveTime);
		
		LaPassStop = new JLabel("경유 횟수 : ");
		LaPassStop.setBounds(80,160,85,20);
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
		RaPassStop1.setBounds(180,160,55,20);
		RaPassStop2.setBounds(250,160,50,20);
		RaPassStop3.setBounds(300,160,50,20);
		add(RaPassStop1);
		add(RaPassStop2);
		add(RaPassStop3);
				
		//검색 버튼
		btnAlSearch = new JButton("검색");
		btnAlSearch.setBounds(848, 155, 62, 20);
		btnAlSearch.addActionListener(this);
		add(btnAlSearch);
		
	}
		
	private void Table_init(){
		//Initialize Column Names
		FlightColNames = new Vector<>();
		//alColNames.add("ch");
		FlightColNames.add("항공편명");
		FlightColNames.add("비행기ID");
		FlightColNames.add("출발지 공항");
		FlightColNames.add("도착지 공항");
		FlightColNames.add("출발 날짜");
		FlightColNames.add("출발 시간");
		FlightColNames.add("도착 날짜");
		FlightColNames.add("도착 시간");
		FlightColNames.add("스케줄");
		FlightColNames.add("요금");
		
		model = new DefaultTableModel(FlightColNames, 0);
		//db.Table_Initialize(CLASS_ID);
		
		//Create a Table with Data and Column Names
		airlineTable = new JTable(model);		
		
		//Table Settings
		airlineTable.addMouseListener(new JTableMouseListener());
		airlineTable.getTableHeader().setReorderingAllowed(false);		//테이블 칼럼 이동 방지
		tableCellCenter(airlineTable);
		setColumnSize(airlineTable);
		scroll = new JScrollPane(airlineTable);
		scroll.setBounds(70, 200, 850, 360);
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