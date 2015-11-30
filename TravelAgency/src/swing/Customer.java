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
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import main.Database;

/** Customer UI & Functions */
public class Customer extends JPanel implements ActionListener{
	private final int CLASS_ID = 2;
	
	private JButton btnCsEnroll;
	private JButton btnCsDelete;
	private JButton btnCsSearch;
	
	private JLabel lblName;
	private JLabel lblPhone;
	private JLabel lblAddress;
	private JLabel lblMembership;
	private JLabel lblPayment;
	private JLabel lblSeat;
	private JLabel location;
	
	private Font font;
	private JScrollPane scroll;
	private JTable customerTable;
	
	private JTextField tfName;
	private JTextField tfPhone;
	private JTextField tfAddress;
	private JTextField tfSearch;
	
	//JRadioButton: Membership
	ButtonGroup membership;
	JRadioButton membershipTrue;
	JRadioButton membershipFalse;
	
	//JRadioButton: Seat(Front/Back)
	ButtonGroup seatFB;
	JRadioButton seatFront;
	JRadioButton seatBack;
	
	//JRadioButton: Seat(Window/Aisle)
	ButtonGroup seatWA;
	JRadioButton seatWindow;
	JRadioButton seatAisle;
	
	private JComboBox cbSearch;
	private JComboBox cbCustomer;
	
	//private JCheckBox chBox;
	
	//Vector, String
	Vector<String> csColNames;
	String csCombo[] = {"ID","이름","주소","전화번호","멤버쉽","결제방법","좌석"};
	Vector<Vector> data;
	
	public static DefaultTableModel model;
	
	Database db;
	
	/** Customer Constructor 
	 * @throws SQLException */
	public Customer() throws SQLException{
		setLayout(null);		//Delete Layout Manager
		setBackground(Color.LIGHT_GRAY);
		
		//Connect to DB
		db = new Database();
		
		Enroll_init();
		Table_init();
		Location();
	}
	
	/** 등록/삭제 버튼 및 등록 부분 UI */
	private void Enroll_init(){
		//Label에 사용할 폰트
		font = new Font("",Font.BOLD,12);
		
		//등록 부분 양식
		lblName = new JLabel("* 이        름 : ");
		lblName.setBounds(80,30,75,20);
		lblName.setFont(font);
		add(lblName);
		
		tfName = new JTextField();
		tfName.setBounds(180, 30, 200, 20);
		add(tfName);
		  
		lblPhone = new JLabel("* 전화번호 : ");
		lblPhone.setBounds(80,65,75,20);
		lblPhone.setFont(font);
		add(lblPhone);
		
		tfPhone = new JTextField();
		tfPhone.setBounds(180,65, 200, 20);
		add(tfPhone);
		
		lblMembership = new JLabel("* 멤버십 여부 : ");
		lblMembership.setBounds(80,100,85,20);
		lblMembership.setFont(font);
		add(lblMembership);
		
		//Membership Radio Button
		membership = new ButtonGroup();
		membershipTrue = new JRadioButton("보유");
		membershipFalse = new JRadioButton("미보유");
		membership.add(membershipTrue);
		membership.add(membershipFalse);
		membershipTrue.addActionListener(this);
		membershipFalse.addActionListener(this);
		membershipTrue.setBounds(190, 100, 60, 20);
		membershipTrue.setFont(font);
		membershipTrue.setBackground(Color.LIGHT_GRAY);
		membershipFalse.setBounds(270, 100, 70, 20);
		membershipFalse.setFont(font);
		membershipFalse.setBackground(Color.LIGHT_GRAY);
		add(membershipTrue);
		add(membershipFalse);		
		
		lblPayment = new JLabel("  선호 결제방법 : ");
		lblPayment.setBounds(80,135,100,20);
		lblPayment.setFont(font);
		add(lblPayment);
		
		tfAddress = new JTextField();
		tfAddress.setBounds(180, 135, 350, 20);
		add(tfAddress);
		
		lblSeat = new JLabel("  선호 좌석 : ");
		lblSeat.setBounds(80,170,75,20);
		lblSeat.setFont(font);
		add(lblSeat);
		
		//Seat Radio Button
		seatFB = new ButtonGroup();
		seatFront = new JRadioButton("앞");
		seatBack = new JRadioButton("뒤");
		seatFB.add(seatFront);
		seatFB.add(seatBack);
		seatFront.addActionListener(this);
		seatBack.addActionListener(this);
		seatFront.setBounds(190, 170, 55, 20);
		seatFront.setFont(font);
		seatFront.setBackground(Color.LIGHT_GRAY);
		seatBack.setBounds(240, 170, 55, 20);
		seatBack.setFont(font);
		seatBack.setBackground(Color.LIGHT_GRAY);
		add(seatFront);
		add(seatBack);
		
		seatWA = new ButtonGroup();
		seatWindow = new JRadioButton("창가");
		seatAisle = new JRadioButton("복도");
		seatWA.add(seatWindow);
		seatWA.add(seatAisle);
		seatWindow.addActionListener(this);
		seatAisle.addActionListener(this);
		seatWindow.setBounds(340, 170, 55, 20);
		seatWindow.setFont(font);
		seatWindow.setBackground(Color.LIGHT_GRAY);
		seatAisle.setBounds(400, 170, 55, 20);
		seatAisle.setFont(font);
		seatAisle.setBackground(Color.LIGHT_GRAY);
		add(seatWindow);
		add(seatAisle);
		
		//등록 버튼: 테이블 새로 한 줄 추가
		btnCsEnroll = new JButton("등록");
		btnCsEnroll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnAddRow();					
			}
		});
		btnCsEnroll.setBounds(778, 155, 62, 30);
		add(btnCsEnroll);
		
		//삭제 버튼: 선택된 테이블 한 줄 삭제
		btnCsDelete = new JButton("삭제");
		btnCsDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnDelRow();					
			}
		});
		btnCsDelete.setBounds(848, 155, 62, 30);
		add(btnCsDelete);
	}
	
	/** 테이블 및 검색 부분 UI 
	 * @throws SQLException */
	private void Table_init() throws SQLException{
		
		
		//Initialize Column Names
		csColNames = new Vector<>();
		//alColNames.add("ch");
		csColNames.add("ID");
		csColNames.add("이름");
		csColNames.add("전화번호");
		csColNames.add("주소");
		csColNames.add("멤버쉽");
		csColNames.add("결제방법");
		csColNames.add("좌석");
		
		//Create DefaultTableModel
		model = new DefaultTableModel(csColNames, 0);
		db.Table_Initialize(CLASS_ID);
		
		//Create a Table with DefaultTableModel
		customerTable = new JTable(model);		
		
		//Table Settings
		customerTable.addMouseListener(new JTableMouseListener());
		customerTable.getTableHeader().setReorderingAllowed(false);		//테이블 칼럼 이동 방지 (드래그 앤 드롭)
		tableCellCenter(customerTable);
		setColumnSize(customerTable);
		scroll = new JScrollPane(customerTable);
		scroll.setBounds(70, 200, 850, 360);
		add(scroll);
		
		//CheckBox for the Table
		/*chBox = new JCheckBox();
		chBox.setHorizontalAlignment(JLabel.CENTER);
		customerTable.getColumn("ch").setCellEditor(new DefaultCellEditor(chBox));
		add(chBox);*/
		
		//ComboBox: Search			
		cbSearch = new JComboBox(csCombo);
		cbSearch.addActionListener(this);
		cbSearch.setBounds(280, 580, 80, 30);
		add(cbSearch);
		
		//TextField: Search
		tfSearch = new JTextField();
		tfSearch.setBounds(380, 580, 200, 30);			
		add(tfSearch);
		//enroll = new EnrollDialog(new JFrame(),"항공사 등록");
		
		//검색 버튼
		btnCsSearch = new JButton("검색");
		btnCsSearch.setBounds(600, 580, 62, 30);
		btnCsSearch.addActionListener(this);
		add(btnCsSearch);
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
		DefaultTableModel model = (DefaultTableModel)customerTable.getModel();
		model.addRow(new String[]{"","","","",""});
	}
	
	void btnDelRow(){
		int row = customerTable.getSelectedRow();
		if(row<0) return;
		DefaultTableModel model = (DefaultTableModel)customerTable.getModel();
		model.removeRow(row);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();	//선택된 버튼 가져오기
		
		if(source == "btnCsEnroll"){
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
		
		tcm.getColumn(0).setPreferredWidth(40);
		tcm.getColumn(1).setPreferredWidth(60);
		tcm.getColumn(2).setPreferredWidth(120);
		tcm.getColumn(3).setPreferredWidth(400);
		tcm.getColumn(4).setPreferredWidth(70);
		tcm.getColumn(5).setPreferredWidth(100);
		tcm.getColumn(6).setPreferredWidth(100);
		
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
			
			System.out.println(model.getValueAt(row, 0));	//눌려진 행의 부분에서 0번째 값 출력
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
	
	private class TableModel extends AbstractTableModel{
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
		
	}
}