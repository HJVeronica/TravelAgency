package swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
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
 * <p><b> Reservation Class </b> 
 * : Reservation UI & Functions</p>
 * 
 * <p><b>Each method is as follows</b> <br>
 * - {@link #Customer()} : Constructor  <br>
 * - {@link #Enroll_init()} : UI of Enroll/Delete Buttons and Enroll Form  <br>
 * - {@link #Table_init()} : UI of Table and Search Part  <br>
 * - {@link #AddnUpdateRow(int)} : Add a New Row & Update Selected Row <br>
 * - {@link #DelRow()} : Delete Selected Row  <br>
 * - {@link itemStateChanged(ItemEvent)} : Radio Button Listener <br>
 * - {@link #actionPerformed(ActionEvent)} : Action Listener  <br>
 * - {@link #tableCellCenter(JTable)} : Set the Alignment of the Rows  <br>
 * - {@link #setColumnSize(JTable)} : Set the Columns' Width & Fix the Columns' Location  </p>
 * 
 * <p><b>Another Class</b> <br>
 * - {@link JTableMouseListener} : Table Mouse Listener (Click, Enter, Exit, Press, Release) 
 * - {@link Dialog} : Dialog for Top 10 List
 * 
 * @version 0.9.9 12/03/15
 * @author 심현정, 김상완, 유란영
 * */
@SuppressWarnings("serial")
public class Reservation extends JPanel implements ActionListener{
	//Instance variables
	//Class id for distinguishing tabs(classes)
	private final int CLASS_ID = 5;
	
	//Search mode constant
	private final int SEARCH_NONE = 0;	//For initializing the table rows
	private final int SEARCH_ALL = 1;
	private final int SEARCH_ID = 2;
	private final int SEARCH_FLIGHT = 3;
	private final int SEARCH_BOOK_DATE = 4;
	private final int SEARCH_STAFF = 5;
	private final int SEARCH_PAYMENT = 6;
	private final int SEARCH_STATE = 7;
	private final int SEARCH_ROUTE = 8;
	
	//JButton
	private JButton btnRvAddnUpdate;
	private JButton btnRvDelete;
	private JButton btnRvSearch;
	private JButton btnRvSearch2;
	private JButton btnRvTop10;
	
	//JLabel
	private JLabel lblCustomer;
	private JLabel lblFlight;
	private JLabel lblBookDate;
	private JLabel lblStaff;
	private JLabel lblPayment;
	private JLabel lblState;
	private JLabel lblRoute;
	private JLabel lblSplit;
	private JLabel lblDash;
	
	//Font, JScrollPane, JTable
	private Font font;
	private JScrollPane scroll;
	private JTable reservTable;
	
	//JCalendar
	JSpinner reservDate;
	JSpinner startDate;
	JSpinner endDate;
	
	//JRadioButton: Payment
	ButtonGroup payment;
	JRadioButton paymentCash;
	JRadioButton paymentCredit;
	JRadioButton paymentCheck;
	
	//JRadioButton: State
	ButtonGroup state;
	JRadioButton stateOk;
	JRadioButton stateStandby;
	
	//JTextField
	private JTextField tfRoute;
	private JTextField tfSearch;
	
	//JComboBox
	private JComboBox<String> cbSearch;
	private JComboBox<String> cbCustomer;
	private JComboBox<String> cbCustomer2;
	private JComboBox<String> cbFlight;
	private JComboBox<String> cbStaff;
	
	//Vector, String
	private Vector<String> rvColNames;
	private Vector<String> comboNames;
	private Vector<String> comboFlight;
	
	String rvCombo[] = {"전체","ID","항공사","제작사","종류",
			"일등석","비즈니스","이코노미","길이","크기"};
	String RvComboStaff[] = {"심현정", "김상완", "유란영"};
	private String status;	//Get chosen status
	
	//DefaultTableModel
	public static DefaultTableModel model;
	
	//Database Class
	Database db = null;
	
	/** 
	 * Reservation Constructor 
	 * @param
	 * */
	public Reservation(){
		setLayout(null);		//Delete Layout Manager
		setBackground(Color.LIGHT_GRAY);
		
		//Connect to DB
		db = new Database();
		
		Enroll_init();
		Table_init();
	}
	
	/** 
	 * UI of Enroll/Delete/Search Buttons and Enroll Form 
	 * @param
	 * @return
	 * */
	private void Enroll_init(){
		//Font for Labels
		font = new Font("",Font.BOLD,12);
		
		//Enroll Form
		lblCustomer = new JLabel("* 고  객  명 : ");
		lblCustomer.setBounds(80,30,85,20);
		lblCustomer.setFont(font);
		add(lblCustomer);
		
		//ComboBox: Names of Customers
		comboNames = new Vector<>();
		comboNames = db.CustomerComboNames(0);
		cbCustomer2 = new JComboBox<String>(comboNames);
		cbCustomer2.setSelectedItem(0);
		cbCustomer2.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if(cbCustomer2.getSelectedItem()!=null){
					String name = cbCustomer2.getSelectedItem().toString();
					//db.CustomerSelectName(name);
				}
			}			
		});
		cbCustomer2.setBounds(165, 30, 80, 20);
		add(cbCustomer2);
		
		lblStaff = new JLabel("* 담당직원 : ");
		lblStaff.setBounds(275,30,85,20);
		lblStaff.setFont(font);
		add(lblStaff);
		
		//ComboBox: Staff Names			
		cbStaff = new JComboBox<String>(RvComboStaff);
		cbStaff.addActionListener(this);
		cbStaff.setBounds(360, 30, 80, 20);
		add(cbStaff);
		
		lblFlight = new JLabel("* 항공편명 : ");
		lblFlight.setBounds(80,65,85,20);
		lblFlight.setFont(font);
		add(lblFlight);
		
		//ComboBox: Flight Names
		comboFlight = new Vector<>();
		comboFlight = db.FlightComboNames();
		cbFlight = new JComboBox<String>(comboFlight);
		cbFlight.setSelectedItem(0);
		cbFlight.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if(cbFlight.getSelectedItem()!=null){
					String name = cbFlight.getSelectedItem().toString();
					//db.CustomerSelectName(name);
				}
			}			
		});
		cbFlight.setBounds(165, 65, 80, 20);
		add(cbFlight);
		
		lblRoute = new JLabel("* 여        정 : ");
		lblRoute.setBounds(275,65,85,20);
		lblRoute.setFont(font);
		add(lblRoute);
		
		tfRoute = new JTextField();
		tfRoute.setBounds(360, 65, 150, 20);
		add(tfRoute);			
		
		lblBookDate = new JLabel("* 예  약  일 : ");
		lblBookDate.setBounds(80,100,75,20);
		lblBookDate.setFont(font);
		add(lblBookDate);
		
		//JSpinner Setting: Book date
		final SpinnerDateModel spModel = new SpinnerDateModel();
		reservDate = new JSpinner(spModel);
		final DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		
		JSpinner.DateEditor editor = new JSpinner.DateEditor(reservDate, "yyyy-MM-dd");
		JFormattedTextField ftf = editor.getTextField();
		//ftf.setEditable(false);
		ftf.setHorizontalAlignment(JTextField.CENTER);		
		ftf.setBackground(Color.WHITE);
		reservDate.setEditor(editor);
		reservDate.addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent e) {
				Date value = (Date) spModel.getValue();
				Date next = (Date) spModel.getNextValue();
				if(value != null && next != null){
					System.out.println("value: "+ df.format(value)+"\t"
							+ "next: "+df.format(next));
				}
			}
			
		});
		reservDate.setBounds(165, 100, 130, 20);
		reservDate.setFont(font);
		add(reservDate);		

		lblPayment = new JLabel("* 지불방법 : ");
		lblPayment.setBounds(80,135,85,20);
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
		paymentCash.setBounds(160, 135, 70, 20);
		paymentCash.setFont(font);
		paymentCash.setBackground(Color.LIGHT_GRAY);
		paymentCredit.setBounds(230, 135, 80, 20);
		paymentCredit.setFont(font);
		paymentCredit.setBackground(Color.LIGHT_GRAY);
		paymentCheck.setBounds(320, 135, 80, 20);
		paymentCheck.setFont(font);
		paymentCheck.setBackground(Color.LIGHT_GRAY);
		add(paymentCash);
		add(paymentCredit);
		add(paymentCheck);

		lblState = new JLabel("* 예약상태 : ");
		lblState.setBounds(80,170,85,20);
		lblState.setFont(font);
		add(lblState);
		
		//State Radio Button
		state = new ButtonGroup();
		stateOk = new JRadioButton("OK");
		stateStandby = new JRadioButton("대기");
		state.add(stateOk);
		state.add(stateStandby);
		stateOk.addActionListener(this);
		stateStandby.addActionListener(this);
		stateOk.setBounds(160, 170, 70, 20);
		stateOk.setFont(font);
		stateOk.setBackground(Color.LIGHT_GRAY);
		stateStandby.setBounds(230, 170, 150, 20);
		stateStandby.setFont(font);
		stateStandby.setBackground(Color.LIGHT_GRAY);
		add(stateOk);
		add(stateStandby);
		
		//ComboBox: Names of Customers
		comboNames = new Vector<>();
		comboNames = db.CustomerComboNames(1);
		cbCustomer = new JComboBox<String>(comboNames);
		cbCustomer.setSelectedItem(0);
		cbCustomer.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if(cbCustomer.getSelectedItem()!=null){
					String name = cbCustomer.getSelectedItem().toString();
					//db.CustomerSelectName(name);
				}
			}			
		});
		cbCustomer.setBounds(75, 210, 130, 20);
		add(cbCustomer);
		
		//Button: Top 10
		btnRvTop10 = new JButton("Top 10");
		btnRvTop10.addActionListener(this);
		btnRvTop10.setBounds(680, 200, 80, 30);
		add(btnRvTop10);
		
		//JButton: Enroll
		btnRvAddnUpdate = new JButton("등록");
		btnRvAddnUpdate.addActionListener(this);
		btnRvAddnUpdate.setBounds(778, 200, 62, 30);
		add(btnRvAddnUpdate);
		
		//JButton: Delete
		btnRvDelete = new JButton("삭제");
		btnRvDelete.addActionListener(this);
		btnRvDelete.setBounds(848, 200, 62, 30);
		add(btnRvDelete);
	}
	
	/** 
	 * UI of Table and Search Part
	 * @param
	 * @return
	 * */
	private void Table_init(){
		//Initialize Column Names
		rvColNames = new Vector<>();
		rvColNames.add("고객명");
		rvColNames.add("항공편명");
		rvColNames.add("예약일");
		rvColNames.add("담당직원");
		rvColNames.add("지불방법");
		rvColNames.add("예약상태");
		rvColNames.add("여정");
		
		model = new DefaultTableModel(rvColNames, 0){
			//Prevent editing cells
			public boolean isCellEditable(int row, int column){
				if(column>=0)	return false;
				else return true;
			}
		};

		//Initialize DefaultTableModel
		db.ReservationSearch(SEARCH_NONE, null);
		
		//Create a table
		reservTable = new JTable(model);		
		
		//Table settings
		//Enable auto row sorting
		reservTable.setAutoCreateRowSorter(true);
		//Add mouse listener
		reservTable.addMouseListener(new JTableMouseListener());
		//Fix the column's location
		reservTable.getTableHeader().setReorderingAllowed(false);		
		//Enable multiple selection
		reservTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		
		tableCellCenter(reservTable);
		setColumnSize(reservTable);
		scroll = new JScrollPane(reservTable);
		scroll.setBounds(70, 240, 850, 320);
		add(scroll);

		//ComboBox: Search			
		cbSearch = new JComboBox<String>(rvCombo);
		cbSearch.addActionListener(this);
		cbSearch.setBounds(110, 580, 80, 30);
		add(cbSearch);
		
		//TextField: Search
		tfSearch = new JTextField();
		tfSearch.setBounds(210, 580, 200, 30);			
		add(tfSearch);
		
		//Button: Search
		btnRvSearch = new JButton("검색");
		btnRvSearch.setBounds(430, 580, 62, 30);
		btnRvSearch.addActionListener(this);
		add(btnRvSearch);
		
		//Font
		Font font2 = new Font("",Font.BOLD,15);
		lblSplit = new JLabel(" | ");
		lblSplit.setBounds(510,580,30,30);
		lblSplit.setFont(font2);
		add(lblSplit);
		
		//Spinner Setting: Book date range
		final SpinnerDateModel spModel = new SpinnerDateModel();
		startDate = new JSpinner(spModel);
		final DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		
		JSpinner.DateEditor editor = new JSpinner.DateEditor(startDate, "yyyy-MM-dd");
		JFormattedTextField ftf = editor.getTextField();
		ftf.setEditable(false);
		ftf.setHorizontalAlignment(JTextField.CENTER);		
		ftf.setBackground(Color.WHITE);
		startDate.setEditor(editor);
		startDate.addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent e) {
				Date value = (Date) spModel.getValue();
				Date next = (Date) spModel.getNextValue();
				if(value != null && next != null){
					System.out.println("value: "+ df.format(value)+"\t"
							+ "next: "+df.format(next));
				}
			}
			
		});
		startDate.setBounds(540, 580, 110, 30);
		startDate.setFont(font);
		add(startDate);
		
		lblDash = new JLabel(" ~ ");
		lblDash.setBounds(655,580,30,30);
		lblDash.setFont(font2);
		add(lblDash);
		
		//Spinner Setting: Book date range
		endDate = new JSpinner(spModel);		
		editor = new JSpinner.DateEditor(endDate, "yyyy-MM-dd");
		ftf = editor.getTextField();
		ftf.setEditable(false);
		ftf.setHorizontalAlignment(JTextField.CENTER);		
		ftf.setBackground(Color.WHITE);
		endDate.setEditor(editor);
		endDate.addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent e) {
				Date value = (Date) spModel.getValue();
				Date next = (Date) spModel.getNextValue();
				if(value != null && next != null){
					System.out.println("value: "+ df.format(value)+"\t"
							+ "next: "+df.format(next));
				}
			}
			
		});
		endDate.setBounds(680, 580, 110, 30);
		endDate.setFont(font);
		add(endDate);
		
		//Button: Search
		btnRvSearch2 = new JButton("검색");
		btnRvSearch2.setBounds(820, 580, 62, 30);
		btnRvSearch2.addActionListener(this);
		add(btnRvSearch2);
	}
	
	/** 
	 * Add a New Row & Update Selected Row 
	 * @param int flag To distinguish add or update
	 * @return
	 * */
	void AddnUpdateRow(int flag){
		
	}
	
	/** 
	 * Delete Selected Row
	 * @param
	 * @return
	 *  */
	void DelRow(){
		
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
		
		if(source.equals(btnRvTop10)){
			//Show the dialog
			Dialog d = new Dialog();
			d.dialog.setVisible(true);
		}
		
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
	 * Dialog for Top 10 List
	 * */
	private class Dialog{
		JList<String> list;
		JButton btnOk = new JButton("OK");
		JDialog dialog = new JDialog();
		
		/**
		 * Dialog Constructor
		 * @param
		 * */
		Dialog(){
			setLayout(new BorderLayout());
			Vector<String> str = db.SelectTop10();
			list = new JList<String>(str);
			//Set the Alignment of the Rows to Center
			list.setCellRenderer(new DefaultListCellRenderer(){
				public int getHorizontalAlignment(){
					return CENTER;
				}
			});
			dialog.add(list,BorderLayout.CENTER);
			dialog.add(btnOk,BorderLayout.SOUTH);
			dialog.setSize(100,250);
			dialog.setLocation(700, 200);
			
			//Button click listener
			btnOk.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					//Show the dialog
					dialog.setVisible(false);
				}
			});
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
			//Action when you right-click the column
			if(e.getButton()==3){
				//Check if the columns are multi-selected or not
				if(reservTable.getSelectedRowCount()>1){
					JOptionPane.showMessageDialog(null, "수정할 컬럼을 한 개만 선택하세요.",
							"Message",JOptionPane.ERROR_MESSAGE);
				}
				else if(reservTable.getSelectedRow()==-1){
					JOptionPane.showMessageDialog(null, "수정할 칼럼을 먼저 마우스 왼쪽 클릭해주세요.",
							"Message",JOptionPane.ERROR_MESSAGE);
				}
				else{	//Set text to the form
					//Reset variables
					
					
					
					btnRvAddnUpdate.setText("수정");
					btnRvDelete.setText("취소");
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
