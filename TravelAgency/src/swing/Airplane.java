package swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import main.Database;

/** Airplane UI & Functions */
public class Airplane extends JPanel implements ActionListener{
	//DB에서 클래스 구분에 사용할 ID
	private final int CLASS_ID = 3;
	
	//JButton
	private JButton btnApEnroll;
	private JButton btnApDelete;
	private JButton btnApSearch;
	
	//JLabel
	private JLabel lblAircraft;
	private JLabel lblType;
	private JLabel lblFirst;
	private JLabel lblBusiness;
	private JLabel lblEconomy;
	private JLabel lblLength;
	private JLabel lblSize;
	private JLabel lblAirline;
	private JLabel lblCount1;	//좌석 수 카운트용 ("개")
	private JLabel lblCount2;
	private JLabel lblCount3;
	private JLabel lblmeter1;
	private JLabel lblmeter2;
	private JLabel location;
	
	//Font, JScrollPane, JTable
	private Font font;
	private JScrollPane scroll;
	private JTable airlineTable;
	
	//JRadioButton: Aircraft
	ButtonGroup aircraft;
	JRadioButton aircraftBoing;
	JRadioButton aircraftAirbus;
	
	//JRadioButton: Type
	ButtonGroup type;
	JRadioButton typeJet;
	JRadioButton typeTurbo;
	
	//JTextField
	private JTextField tfFirst;
	private JTextField tfBusiness;
	private JTextField tfEconomy;
	private JTextField tfSearch;
	private JTextField tfLength;
	private JTextField tfSize;
	
	//JComboBox
	private JComboBox cbSearch;
	private JComboBox cbAirline;
	
	//JCheckBox
	//private JCheckBox chBox;
	
	//Vector, String
	Vector<String> apColNames;
	String apCombo[] = {"전체","ID","항공사","제작사","종류",
			"일등석","비즈니스","이코노미","길이","크기"};
	String alCombo[] = {"Korean Air","Emirates","Delta","Lufthansa","Air France"};
	Vector<Vector> data;
	
	//Database Class
	Database db = null;
	
	/** Airline Constructor 
	 * @throws SQLException */
	public Airplane() throws SQLException{
		setLayout(null);		//Delete Layout Manager
		setBackground(Color.LIGHT_GRAY);
		
		Enroll_init();
		Table_init();
		Location();
	}
	
	/** 등록/삭제 버튼 및 등록 부분 UI */
	private void Enroll_init(){
		//Label에 사용할 폰트
		font = new Font("",Font.BOLD,12);
		
		//등록 부분 양식
		lblAirline = new JLabel("* 항  공  사 : ");
		lblAirline.setBounds(80,30,85,20);
		lblAirline.setFont(font);
		add(lblAirline);
		
		//ComboBox: Search			
		cbAirline = new JComboBox(alCombo);
		cbAirline.addActionListener(this);
		cbAirline.setBounds(165, 30, 100, 20);
		add(cbAirline);
		
		lblAircraft = new JLabel("* 제  작  사 : ");
		lblAircraft.setBounds(80,65,85,20);
		lblAircraft.setFont(font);
		add(lblAircraft);
		
		//Aircraft Radio Button
		aircraft = new ButtonGroup();
		aircraftBoing = new JRadioButton("보잉");
		aircraftAirbus = new JRadioButton("에어버스");
		aircraft.add(aircraftBoing);
		aircraft.add(aircraftAirbus);
		aircraftBoing.addActionListener(this);
		aircraftAirbus.addActionListener(this);
		aircraftBoing.setBounds(160, 65, 60, 20);
		aircraftBoing.setFont(font);
		aircraftBoing.setBackground(Color.LIGHT_GRAY);
		aircraftAirbus.setBounds(230, 65, 80, 20);
		aircraftAirbus.setFont(font);
		aircraftAirbus.setBackground(Color.LIGHT_GRAY);
		add(aircraftBoing);
		add(aircraftAirbus);
		  
		lblType = new JLabel("* 종        류 : ");
		lblType.setBounds(80,100,75,20);
		lblType.setFont(font);
		add(lblType);
		
		//Type Radio Button
		type = new ButtonGroup();
		typeJet = new JRadioButton("제트기");
		typeTurbo = new JRadioButton("터보 프로펠러 항공기");
		type.add(typeJet);
		type.add(typeTurbo);
		typeJet.addActionListener(this);
		typeTurbo.addActionListener(this);
		typeJet.setBounds(160, 100, 70, 20);
		typeJet.setFont(font);
		typeJet.setBackground(Color.LIGHT_GRAY);
		typeTurbo.setBounds(230, 100, 150, 20);
		typeTurbo.setFont(font);
		typeTurbo.setBackground(Color.LIGHT_GRAY);
		add(typeJet);
		add(typeTurbo);
		
		lblFirst = new JLabel("* 일  등  석 : ");
		lblFirst.setBounds(80,135,85,20);
		lblFirst.setFont(font);
		add(lblFirst);
		
		tfFirst = new JTextField();
		tfFirst.setBounds(165, 135, 40, 20);
		add(tfFirst);
		
		lblCount1 = new JLabel("개");
		lblCount1.setBounds(220, 135, 30, 20);
		add(lblCount1);
		
		lblBusiness = new JLabel("* 비즈니스 : ");
		lblBusiness.setBounds(265,135,85,20);
		lblBusiness.setFont(font);
		add(lblBusiness);
		
		tfBusiness = new JTextField();
		tfBusiness.setBounds(350,135, 40, 20);
		add(tfBusiness);
		
		lblCount2 = new JLabel("개");
		lblCount2.setBounds(405, 135, 30, 20);
		add(lblCount2);
		
		lblEconomy = new JLabel("* 이코노미 : ");
		lblEconomy.setBounds(450,135,85,20);
		lblEconomy.setFont(font);
		add(lblEconomy);
		
		tfEconomy = new JTextField();
		tfEconomy.setBounds(535,135, 40, 20);
		add(tfEconomy);
		
		lblCount3 = new JLabel("개");
		lblCount3.setBounds(590, 135, 30, 20);
		add(lblCount3);
		
		lblLength = new JLabel("* 길        이 : ");
		lblLength.setBounds(80,170,85,20);
		lblLength.setFont(font);
		add(lblLength);
		
		tfLength = new JTextField();
		tfLength.setBounds(165, 170, 50, 20);
		add(tfLength);		
		
		lblmeter1 = new JLabel("m");
		lblmeter1.setBounds(230, 170, 30, 20);
		add(lblmeter1);
		
		lblSize = new JLabel("* 크        기 : ");
		lblSize.setBounds(265,170,85,20);
		lblSize.setFont(font);
		add(lblSize);
		
		tfSize = new JTextField();
		tfSize.setBounds(350, 170, 50, 20);
		add(tfSize);			
		
		lblmeter2 = new JLabel("m");
		lblmeter2.setBounds(430, 170, 30, 20);
		add(lblmeter2);
		
		//등록 버튼: 테이블 새로 한 줄 추가
		btnApEnroll = new JButton("등록");
		btnApEnroll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnAddRow();					
			}
		});
		btnApEnroll.setBounds(778, 170, 62, 30);
		add(btnApEnroll);
		
		//삭제 버튼: 선택된 테이블 한 줄 삭제
		btnApDelete = new JButton("삭제");
		btnApDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnDelRow();					
			}
		});
		btnApDelete.setBounds(848, 170, 62, 30);
		add(btnApDelete);
	}
	
	/** 테이블 및 검색 부분 UI 
	 * @throws SQLException */
	private void Table_init() throws SQLException{
		//Connect to DB & Get Data from Airline Table
		db = new Database();
		data = new Vector<>();
		data = db.Table_Initialize(CLASS_ID, data);
		
		//Initialize Column Names
		apColNames = new Vector<>();
		//apColNames.add("ch");
		apColNames.add("ID");
		apColNames.add("항공사");
		apColNames.add("제작사");
		apColNames.add("종류");
		apColNames.add("일등석");
		apColNames.add("비즈니스");
		apColNames.add("이코노미");
		apColNames.add("길이");
		apColNames.add("크기");
		
		//Create a Table with Data and Column Names
		airlineTable = new JTable(data,apColNames);		
		
		//Table Settings
		airlineTable.addMouseListener(new JTableMouseListener());
		airlineTable.getTableHeader().setReorderingAllowed(false);		//테이블 칼럼 이동 방지
		tableCellCenter(airlineTable);
		setColumnSize(airlineTable);
		scroll = new JScrollPane(airlineTable);
		scroll.setBounds(70, 210, 850, 350);
		add(scroll);
		
		//CheckBox for the Table
		/*chBox = new JCheckBox();
		chBox.setHorizontalAlignment(JLabel.CENTER);
		airlineTable.getColumn("ch").setCellEditor(new DefaultCellEditor(chBox));
		add(chBox);*/
		
		//ComboBox: Search			
		cbSearch = new JComboBox(apCombo);
		cbSearch.addActionListener(this);
		cbSearch.setBounds(280, 580, 80, 30);
		add(cbSearch);
		
		//TextField: Search
		tfSearch = new JTextField();
		tfSearch.setBounds(380, 580, 200, 30);			
		add(tfSearch);
		
		//검색 버튼
		btnApSearch = new JButton("검색");
		btnApSearch.setBounds(600, 580, 62, 30);
		btnApSearch.addActionListener(this);
		add(btnApSearch);
	}
	
	/** 좌표 표시 */
	public void Location(){
		location = new JLabel("현재 좌표");
		MouseMotionAdapter ma = new MouseMotionAdapter() {
			public void mouseMoved(MouseEvent e) {
                // 마우스 좌표 얻어오기
                int x = e.getX();
                int y = e.getY();
                String str = "x좌표:" + x + ",y좌표:" + y;

                // Label에 문자열 넣기
                location.setText(str);
            }
		};
		this.addMouseMotionListener(ma);
		location.setBounds(610, 155, 200, 32);
		add(location);
	}
	
	void btnAddRow(){
		DefaultTableModel model = (DefaultTableModel)airlineTable.getModel();
		model.addRow(new String[]{"","","","",""});
	}
	
	void btnDelRow(){
		int row = airlineTable.getSelectedRow();
		if(row<0) return;
		DefaultTableModel model = (DefaultTableModel)airlineTable.getModel();
		model.removeRow(row);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();	//선택된 버튼 가져오기
		
		if(source == "btnApEnroll"){
			setVisible(false);
		}
		
	}
	
	
	/*DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer(){
		public Component getTableCellRendererComponent
		(JTable t, Object value, boolean isSelected, boolean hasFocus, int row, int column){
			chBox.setSelected(((Boolean)value).booleanValue());
			//chBox.set
			
			return chBox;				
		}
	};*/
	
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
	
	/*private class TableModel extends AbstractTableModel{
		@Override
		public int getColumnCount() {
			return 0;
		}

		@Override
		public int getRowCount() {
			return 0;
		}

		@Override
		public Object getValueAt(int arg0, int arg1) {
			return null;
		}
		
	}*/
	
}
