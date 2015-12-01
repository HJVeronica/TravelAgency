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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import main.Database;

/** 
 * <p><b> Customer Class </b> 
 * : Customer UI & Functions</p>
 * 
 * <p><b>Each method is as follows</b> <br>
 * - {@link #Customer()} : Constructor  <br>
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
 * @version 0.9.2 12/01/15
 * @author Hyunjeong Shim, 김상완, 유란영
 * */
public class Customer extends JPanel implements ActionListener{
	//Instance variables
	//Class id for distinguishing tabs(classes)
	private final int CLASS_ID = 2;
	
	//Search mode constant
	private final int SEARCH_NONE = 0;	//For initializing the table rows
	private final int SEARCH_ALL = 1;
	private final int SEARCH_ID = 2;
	private final int SEARCH_NAME = 3;
	private final int SEARCH_COUNTRY = 4;
	private final int SEARCH_ADDRESS = 5;
	private final int SEARCH_PHONE = 6;
	
	//JButton
	private JButton btnCsAddnUpdate;
	private JButton btnCsDelete;
	private JButton btnCsSearch;
	
	//JLabel
	private JLabel lblName;
	private JLabel lblPhone;
	private JLabel lblMembership;
	private JLabel lblPayment;
	private JLabel lblSeat;
	//private JLabel location;
	
	//Font, JScrollPane, JTable
	private Font font;
	private JScrollPane scroll;
	private JTable customerTable;
	
	//JTextField
	private JTextField tfName;
	private JTextField tfPhone;
	private JTextField tfAddress;
	private JTextField tfSearch;
	
	//JRadioButton: Payment
	ButtonGroup payment;
	JRadioButton paymentCash;
	JRadioButton paymentCredit;
	JRadioButton paymentCheck;	
	
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
	
	//JComboBox<String>: Search options
	private JComboBox<String> cbSearch;
	//private JComboBox<String> cbCustomer;
	
	//Vector<String>: Column Names
	Vector<String> csColNames;
	
	//String: ComboBox Items, Id for the last id number
	private String csCombo[] = {"ID","이름","주소","전화번호","멤버쉽","결제방법","좌석"};
	private String id;
	
	//Int: Get Search Mode
	private int searchMode;
	
	//DefaultTableModel
	public static DefaultTableModel model;
	
	//Database Class
	Database db;
	
	/** 
	 * Customer Constructor 
	 * @param
	 *  */
	public Customer(){
		setLayout(null);		//Delete Layout Manager
		setBackground(Color.LIGHT_GRAY);
		
		//Connect to DB
		db = new Database();
		
		Enroll_init();
		Table_init();
		//Location();
	}
	
	/** 등록/삭제 버튼 및 등록 부분 UI */
	private void Enroll_init(){
		//Font for Label
		font = new Font("",Font.BOLD,12);
		
		//Enroll Form
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
		
		//Payment Radio Button
		payment = new ButtonGroup();
		paymentCash = new JRadioButton("현금");
		paymentCredit = new JRadioButton("신용카드");
		paymentCheck = new JRadioButton("체크카드");
		payment.add(paymentCash);
		payment.add(paymentCredit);
		payment.add(paymentCheck);
		paymentCash.addActionListener(this);
		paymentCredit.addActionListener(this);
		paymentCheck.addActionListener(this);
		paymentCash.setBounds(190, 135, 55, 20);
		paymentCash.setFont(font);
		paymentCash.setBackground(Color.LIGHT_GRAY);
		paymentCredit.setBounds(270, 135, 80, 20);
		paymentCredit.setFont(font);
		paymentCredit.setBackground(Color.LIGHT_GRAY);
		paymentCheck.setBounds(365, 135, 80, 20);
		paymentCheck.setFont(font);
		paymentCheck.setBackground(Color.LIGHT_GRAY);
		add(paymentCash);
		add(paymentCredit);
		add(paymentCheck);
		
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
		btnCsAddnUpdate = new JButton("등록");
		btnCsAddnUpdate.addActionListener(this);
		btnCsAddnUpdate.setBounds(778, 155, 62, 30);
		add(btnCsAddnUpdate);
		
		//삭제 버튼: 선택된 테이블 한 줄 삭제
		btnCsDelete = new JButton("삭제");
		btnCsDelete.addActionListener(this);
		btnCsDelete.setBounds(848, 155, 62, 30);
		add(btnCsDelete);
	}
	
	/** 
	 * UI of Table and Search Part
	 * @param
	 * @return
	 * */
	private void Table_init(){
		//Initialize Column Names
		csColNames = new Vector<>();
		csColNames.add("ID");
		csColNames.add("이름");
		csColNames.add("주소");
		csColNames.add("전화번호");
		csColNames.add("멤버쉽");
		csColNames.add("결제방법");
		csColNames.add("좌석");
		
		model = new DefaultTableModel(csColNames, 0){
			//Prevent editing cells
			public boolean isCellEditable(int row, int column){
				if(column>=0)	return false;
				else return true;
			}
		};
		//db.Table_Initialize(CLASS_ID);
		db.CustomerSearch(SEARCH_NONE, null);
		
		//Create a table
		customerTable = new JTable(model);
		
		//Table settings
		//Enable auto row sorting
		customerTable.setAutoCreateRowSorter(true);
		//Add mouse listener
		customerTable.addMouseListener(new JTableMouseListener());
		//Fix the column's location
		customerTable.getTableHeader().setReorderingAllowed(false);		
		//Enable multiple selection
		customerTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		
		tableCellCenter(customerTable);
		setColumnSize(customerTable);
		scroll = new JScrollPane(customerTable);
		scroll.setBounds(70, 200, 850, 360);
		add(scroll);

		//ComboBox: Search			
		cbSearch = new JComboBox<String>(csCombo);
		cbSearch.setSelectedIndex(0);
		cbSearch.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				searchMode = cbSearch.getSelectedIndex();
				//System.out.println("Search Mode: "+searchMode);
			}			
		});
		cbSearch.setBounds(280, 580, 80, 30);
		add(cbSearch);
		
		//TextField: Search
		tfSearch = new JTextField();
		tfSearch.setBounds(380, 580, 200, 30);			
		add(tfSearch);
		
		//Button: Search
		btnCsSearch = new JButton("검색");
		btnCsSearch.setBounds(600, 580, 62, 30);
		btnCsSearch.addActionListener(this);
		add(btnCsSearch);
	}
	
	/** 좌표 표시 */
	/*public void Location(){
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
	}*/
	
	/** Add a New Row & Update Selected Row */
	private void AddnUpdateRow(int flag){
		//Get text and information from TextField
		String name = tfName.getText();
		String phone = tfPhone.getText();
		String address = tfAddress.getText();
		
		/*String[] rows = {id,name,phone,address,phone};
		
		//Check if essential fields are filled or not
		if(name.isEmpty() | phone.isEmpty() | member.isEmpty() | address.isEmpty())
			JOptionPane.showMessageDialog(null, "필수 입력칸을 모두 채워주세요.",
					"Message",JOptionPane.ERROR_MESSAGE);
		else{
			if(flag==0){	//Add a new row
				db.InsertData(CLASS_ID,rows);
				model.addRow(rows);
				cbAirlines.addItem(name);
			}
			else{			//Update selected row
				db.UpdateData(CLASS_ID,rows);
				JOptionPane.showMessageDialog(null, "수정 되었습니다.",
						"Message",JOptionPane.OK_OPTION);
				//Reset button text
				btnAlAddnUpdate.setText("등록");
				btnAlDelete.setText("삭제");
				//Reset ComboBox
				cbAirlines.removeAllItems();
				comboNames = db.AirlineComboNames();
				for(int i=0;i<comboNames.size();i++){
					cbAirlines.addItem(comboNames.get(i));
				}
			}
		}*/
	}
	
	private void DelRow(){
		int row = customerTable.getSelectedRow();
		if(row<0) return;
		DefaultTableModel model = (DefaultTableModel)customerTable.getModel();
		model.removeRow(row);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();	//선택된 버튼 가져오기
		
		
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
		
		tcm.getColumn(0).setPreferredWidth(40);
		tcm.getColumn(1).setPreferredWidth(60);
		tcm.getColumn(2).setPreferredWidth(300);
		tcm.getColumn(3).setPreferredWidth(120);
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