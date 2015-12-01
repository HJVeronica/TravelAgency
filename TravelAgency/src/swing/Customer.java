package swing;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
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
 * @version 1.1 12/02/15
 * @author 심현정, 김상완, 유란영
 * */
@SuppressWarnings("serial")
public class Customer extends JPanel implements ActionListener, ItemListener{
	//Instance variables
	//Class id for distinguishing tabs(classes)
	private final int CLASS_ID = 2;
	
	//Search mode constant
	private final int SEARCH_NONE = 0;	//For initializing the table rows
	private final int SEARCH_ALL = 1;
	private final int SEARCH_ID = 2;
	private final int SEARCH_NAME = 3;
	private final int SEARCH_ADDRESS = 4;
	private final int SEARCH_PHONE = 5;
	private final int SEARCH_MEMBER = 6;
	private final int SEARCH_PAYMENT = 7;
	private final int SEARCH_SEAT = 8;
	
	//JButton
	private JButton btnCsAddnUpdate;
	private JButton btnCsDelete;
	private JButton btnCsSearch;
	private JButton btnShowAll;
	
	//JLabel
	private JLabel lblName;
	private JLabel lblPhone;
	private JLabel lblMembership;
	private JLabel lblPayment;
	private JLabel lblAddress;
	private JLabel lblSeat;
	
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
	
	//Vector<String>: Column Names
	Vector<String> csColNames;
	
	//String: ComboBox Items 
	private String csCombo[] = {"전체","ID","이름","주소",
								"전화번호","멤버쉽","결제방법","좌석"};
	private String id;			//Id for the last id number
	private String member;		//Get chosen radio button from membership group
	private String pay;			//Get chosen radio button from payment group
	private String seat1;		//Get chosen radio button from seatFB group
	private String seat2;		//Get chosen radio button from seatWA group
	
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
		
		//Get last id number
		id = db.getID(CLASS_ID);
		
		Enroll_init();
		Table_init();
	}
	
	/** 등록/삭제 버튼 및 등록 부분 UI */
	private void Enroll_init(){
		//Font for Labels
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
		
		lblAddress = new JLabel("* 주        소 : ");
		lblAddress.setBounds(80,100,75,20);
		lblAddress.setFont(font);
		add(lblAddress);
		
		tfAddress = new JTextField();
		tfAddress.setBounds(180,100, 300, 20);
		add(tfAddress);
		
		lblMembership = new JLabel("* 멤버십 여부 : ");
		lblMembership.setBounds(80,135,85,20);
		lblMembership.setFont(font);
		add(lblMembership);
		
		//Membership Radio Button
		membership = new ButtonGroup();
		membershipTrue = new JRadioButton("보유");
		membershipFalse = new JRadioButton("미보유");
		membership.add(membershipTrue);
		membership.add(membershipFalse);
		membership.clearSelection();
		membershipTrue.addItemListener(this);
		membershipFalse.addItemListener(this);
		membershipTrue.setBounds(190, 135, 60, 20);
		membershipTrue.setFont(font);
		membershipTrue.setBackground(Color.LIGHT_GRAY);
		membershipFalse.setBounds(270, 135, 70, 20);
		membershipFalse.setFont(font);
		membershipFalse.setBackground(Color.LIGHT_GRAY);
		add(membershipTrue);
		add(membershipFalse);		
		
		lblPayment = new JLabel("  선호 결제방법 : ");
		lblPayment.setBounds(80,170,100,20);
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
		payment.clearSelection();
		paymentCash.addItemListener(this);
		paymentCredit.addItemListener(this);
		paymentCheck.addItemListener(this);
		paymentCash.setBounds(190, 170, 55, 20);
		paymentCash.setFont(font);
		paymentCash.setBackground(Color.LIGHT_GRAY);
		paymentCredit.setBounds(270, 170, 80, 20);
		paymentCredit.setFont(font);
		paymentCredit.setBackground(Color.LIGHT_GRAY);
		paymentCheck.setBounds(365, 170, 80, 20);
		paymentCheck.setFont(font);
		paymentCheck.setBackground(Color.LIGHT_GRAY);
		add(paymentCash);
		add(paymentCredit);
		add(paymentCheck);
		
		lblSeat = new JLabel("  선호 좌석 : ");
		lblSeat.setBounds(80,200,75,20);
		lblSeat.setFont(font);
		add(lblSeat);
		
		//Seat Radio Button
		seatFB = new ButtonGroup();
		seatFront = new JRadioButton("앞");
		seatBack = new JRadioButton("뒤");
		seatFB.add(seatFront);
		seatFB.add(seatBack);
		seatFB.clearSelection();
		seatFront.addItemListener(this);
		seatBack.addItemListener(this);
		seatFront.setBounds(190, 200, 55, 20);
		seatFront.setFont(font);
		seatFront.setBackground(Color.LIGHT_GRAY);
		seatBack.setBounds(240, 200, 55, 20);
		seatBack.setFont(font);
		seatBack.setBackground(Color.LIGHT_GRAY);
		add(seatFront);
		add(seatBack);
		
		seatWA = new ButtonGroup();
		seatWindow = new JRadioButton("창가");
		seatAisle = new JRadioButton("복도");
		seatWA.add(seatWindow);
		seatWA.add(seatAisle);
		seatWA.clearSelection();
		seatWindow.addItemListener(this);
		seatAisle.addItemListener(this);
		seatWindow.setBounds(340, 200, 55, 20);
		seatWindow.setFont(font);
		seatWindow.setBackground(Color.LIGHT_GRAY);
		seatAisle.setBounds(400, 200, 55, 20);
		seatAisle.setFont(font);
		seatAisle.setBackground(Color.LIGHT_GRAY);
		add(seatWindow);
		add(seatAisle);
		
		//ShowAll Button: Refresh the table
		btnShowAll = new JButton("전체보기");
		btnShowAll.addActionListener(this);
		btnShowAll.setBounds(778, 150, 132, 30);
		add(btnShowAll);
		
		//등록 버튼: 테이블 새로 한 줄 추가
		btnCsAddnUpdate = new JButton("등록");
		btnCsAddnUpdate.addActionListener(this);
		btnCsAddnUpdate.setBounds(778, 190, 62, 30);
		add(btnCsAddnUpdate);
		
		//삭제 버튼: 선택된 테이블 한 줄 삭제
		btnCsDelete = new JButton("삭제");
		btnCsDelete.addActionListener(this);
		btnCsDelete.setBounds(848, 190, 62, 30);
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
		scroll.setBounds(70, 230, 850, 330);
		add(scroll);

		//ComboBox: Search			
		cbSearch = new JComboBox<String>(csCombo);
		cbSearch.setSelectedIndex(0);
		cbSearch.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				searchMode = cbSearch.getSelectedIndex();
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
	
	/** Add a New Row & Update Selected Row */
	private void AddnUpdateRow(int flag){
		//Get text and information from TextField and RadioButton
		String name = tfName.getText();
		String phone = tfPhone.getText();
		String address = tfAddress.getText();
		String seat = null;
		
		if(seat1 == null)
			seat = seat2;
		else if(seat2 == null)
			seat = seat1;
		else
			seat = seat1+","+seat2;
		
		if(member.equals("0"))
			member = "X";
		else
			member = "O";
		
		String[] rows = {id,name,address,phone,member,pay,seat};
		
		//Check if essential fields are filled or not
		if(name.isEmpty() || phone.isEmpty() || member==null || address.isEmpty())
			JOptionPane.showMessageDialog(null, "필수 입력칸을 모두 채워주세요.",
					"Message",JOptionPane.ERROR_MESSAGE);
		else{
			if(flag==0){	//Add a new row
				db.InsertData(CLASS_ID,rows);
				model.addRow(rows);
			}
			else{			//Update selected row
				db.UpdateData(CLASS_ID,rows);
				JOptionPane.showMessageDialog(null, "수정 되었습니다.",
						"Message",JOptionPane.OK_OPTION);
				//Reset button text
				btnCsAddnUpdate.setText("등록");
				btnCsDelete.setText("삭제");
				
				//Refresh the table
				db.CustomerSearch(SEARCH_NONE, null);
			}
			//Reset TextField
			tfName.setText(null);
			tfPhone.setText(null);
			tfAddress.setText(null);
			
			//Reset RadioButton
			membership.clearSelection();
			payment.clearSelection();
			seatFB.clearSelection();
			seatWA.clearSelection();
			
			//Reset variables
			pay=null;
			seat1=null;
			seat2=null;
		}		
	}
	
	/** Delete Selected Row */
	private void DelRow(){
		if(customerTable.getSelectedRowCount()>0){
			for(int i : customerTable.getSelectedRows()){
				db.DeleteData(CLASS_ID, String.valueOf(model.getValueAt(i, 0)));
			}
			
			//Refresh the table
			db.CustomerSearch(SEARCH_NONE,null);
			
			//Get last id number
			id = db.getID(CLASS_ID);
		}
		else{
			JOptionPane.showMessageDialog(null, "선택된 컬럼이 없습니다.",
					"Message",JOptionPane.ERROR_MESSAGE);
		}
	}
	
	/** Radio Button Item Listener */
	@Override
	public void itemStateChanged(ItemEvent e) {
		Object source = e.getSource();	//Get Selected Object
		
		//Radio buttons
		if(source == membershipTrue || source == membershipFalse){
			if(membershipTrue.isSelected()){
				member = "1";
			}
			else if(membershipFalse.isSelected()){
				member ="0";
			}
		}
		
		//Payment group radio buttons
		if(source == paymentCash || source == paymentCredit 
				|| source == paymentCheck){
			if(paymentCash.isSelected()){
				pay = "현금";
			}
			else if(paymentCredit.isSelected()){
				pay ="신용카드";
			}
			else if(paymentCheck.isSelected()){
				pay = "체크카드";
			}
		}
		
		//Seat group radio buttons
		if(source == seatFront || source == seatBack
				|| source == seatWindow || source == seatAisle){
			if(seatFront.isSelected()){
				seat1 = "앞";
			}
			else if(seatBack.isSelected()){
				seat1 ="뒤";
			}
			
			if(seatWindow.isSelected()){
				seat2 = "창가";
			}
			else if(seatAisle.isSelected()){
				seat2 = "복도";
			}
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();	//Get Selected Object
		
		if(source.equals(btnCsAddnUpdate)){
			//Check if it's add mode(0) or update mode(1)
			if(btnCsAddnUpdate.getText().equals("등록")){
				//Get last id number
				id = db.getID(CLASS_ID);
				AddnUpdateRow(0);
			}
			else{
				AddnUpdateRow(1);
			}
		}
		else if(source.equals(btnCsDelete)){
			if(btnCsDelete.getText().equals("삭제")){
				DelRow();
			}
			else{
				//Reset TextField
				tfName.setText(null);
				tfPhone.setText(null);
				tfAddress.setText(null);
				
				//Reset RadioButton
				membership.clearSelection();
				payment.clearSelection();
				seatFB.clearSelection();
				seatWA.clearSelection();
				
				//Reset variables
				pay=null;
				seat1=null;
				seat2=null;
				
				btnCsDelete.setText("삭제");
				btnCsAddnUpdate.setText("등록");
			}
		}
		else if(source.equals(btnCsSearch)){
			String keyWord = tfSearch.getText().trim();
			if(keyWord.isEmpty())
				JOptionPane.showMessageDialog(null, "검색어를 입력하세요.",
						"Message",JOptionPane.ERROR_MESSAGE);
			else{
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
		}
	}
	
	/** Set the Alignment of the Rows to Center */
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
	
	/** Set the Columns' Width & Fix the Columns' Location */
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
			//Action when you right-click the column
			if(e.getButton()==3){
				//Check if the columns are multi-selected or not
				if(customerTable.getSelectedRowCount()>1){
					JOptionPane.showMessageDialog(null, "수정할 컬럼을 한 개만 선택하세요.",
							"Message",JOptionPane.ERROR_MESSAGE);
				}
				else if(customerTable.getSelectedRow()==-1){
					JOptionPane.showMessageDialog(null, "수정할 칼럼을 먼저 마우스 왼쪽 클릭해주세요.",
							"Message",JOptionPane.ERROR_MESSAGE);
				}
				else{	//Set text to the form
					//Reset variables
					seat1 = null;
					seat2 = null;
					
					int row = customerTable.getSelectedRow();
					for(int i=0;i<customerTable.getColumnCount();i++){
						if(i==0)	{ id = (String) model.getValueAt(row, i); }
						if(i==1)	{ tfName.setText((String) model.getValueAt(row, i)); }
						else if(i==2)	{ tfAddress.setText((String) model.getValueAt(row, i)); }
						else if(i==3)	{ tfPhone.setText((String) model.getValueAt(row, i)); }
						else if(i==4){	//Membership
							String mem = (String) model.getValueAt(row, i);
							if(mem.equals("O")){
								membershipTrue.setSelected(true);
							}
							else{
								membershipFalse.setSelected(true);
							}
						}
						else if(i==5){	//Payment
							String pay = (String) model.getValueAt(row, i);
							if(pay != null){
								if(pay.equals("현금")){
									paymentCash.setSelected(true);
								}
								else if(pay.equals("신용카드")){
									paymentCredit.setSelected(true);
								}
								else if(pay.equals("체크카드")){
									paymentCheck.setSelected(true);
								}
							}
						}
						else if(i==6){	//Seat
							String seat = (String) model.getValueAt(row, i);
							
							if(seat != null){
								if(seat.contains(",")){		//If both group(seatFB/seatWA) should be selected,
									//Parsing
									StringTokenizer st = new StringTokenizer(seat,",");
									seat1 = st.nextToken();
									seat2 = st.nextToken();
									
									if(seat1.equals("앞")){
										seatFront.setSelected(true);
									}
									else if(seat1.equals("뒤")){
										seatBack.setSelected(true);
									}
									
									if(seat2.equals("창가")){
										seatWindow.setSelected(true);
									}
									else if(seat2.equals("복도")){
										seatAisle.setSelected(true);
									}
								}	//If not,
								else if(seat.equals("앞")){
									seatFront.setSelected(true);
								}
								else if(seat.equals("뒤")){
									seatBack.setSelected(true);
								}
								else if(seat.equals("창가")){
									seatWindow.setSelected(true);
								}
								else if(seat.equals("복도")){
									seatAisle.setSelected(true);
								}
							}
						}
					}
					
					btnCsAddnUpdate.setText("수정");
					btnCsDelete.setText("취소");
				}
			}
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