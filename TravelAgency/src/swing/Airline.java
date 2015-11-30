package swing;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
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
 * <b> Airline Class </b>
 * : Airline UI & Functions
 * 
 * Each method is as follows
 * : Constructor {@link #Airline()}
 * : UI of Enroll/Delete Buttons and Enroll Form {@link #Enroll_init()}
 * : UI of Table and Search Part {@link #Table_init()}
 * : Add a New Row {@link #AddRow()}
 * : Delete Selected Row {@link #DelRow()}
 * : Action Listener {@link #actionPerformed(ActionEvent)}
 * : Set the Alignment of the Rows {@link #tableCellCenter(JTable)}
 * : Set the Columns' Width & Fix the Columns' Location {@link #setColumnSize(JTable)}
 * : Table Mouse Listener (Click, Enter, Exit, Press, Release) {@link JTableMouseListener}
 * 
 * @version 0.3 12/01/15
 * @author Hyunjeong Shim, 김상완, 유란영
 * */
public class Airline extends JPanel implements ActionListener{
	//Instance variables
	//Class id for distinguishing tabs(classes)
	private final int CLASS_ID = 1;
	
	//Search mode constant
	private final int SEARCH_NONE = 0;
	private final int SEARCH_ALL = 1;
	private final int SEARCH_ID = 2;
	private final int SEARCH_NAME = 3;
	private final int SEARCH_COUNTRY = 4;
	private final int SEARCH_ADDRESS = 5;
	private final int SEARCH_PHONE = 6;
	
	//JButton
	private JButton btnAlEnroll;
	private JButton btnAlDelete;
	private JButton btnAlSearch;
	
	//JLabel
	private JLabel lblName;
	private JLabel lblPhone;
	private JLabel lblAddress;
	private JLabel location;
	private JLabel lblCountry;
	
	//Font, JScrollPane, JTable
	private Font font;
	private JScrollPane scroll;
	private JTable airlineTable;
	
	//JTextField
	private JTextField tfName;
	private JTextField tfPhone;
	private JTextField tfAddress;
	private JTextField tfSearch;
	private JTextField tfCountry;
	
	//JComboBox
	private JComboBox cbSearch;
	private JComboBox<String> cbAirlines;

	//Vector, String
	Vector<String> alColNames;
	String alCombo[] = {"전체","ID","이름","국가","주소","전화번호"};
	Vector<String> comboNames;
	private String id = null;
	private int searchMode;
	
	//DefaultTableModel
	public static DefaultTableModel model;
	
	//Database Class
	Database db = null;
	
	/** 
	 * Airline Constructor 
	 * @param
	 * */
	public Airline(){
		setLayout(null);		//Delete Layout Manager
		setBackground(Color.LIGHT_GRAY);
		
		//Connect to DB
		db = new Database();
		
		Enroll_init();
		Table_init();
		//Location();
	}
	
	/** UI of Enroll/Delete Buttons and Enroll Form */
	private void Enroll_init(){
		//Label에 사용할 폰트
		font = new Font("",Font.BOLD,12);
		
		//등록 부분 양식
		lblName = new JLabel("* 이        름 : ");
		lblName.setBounds(80,30,75,20);
		lblName.setFont(font);
		add(lblName);
		
		tfName = new JTextField();
		tfName.setBounds(155, 30, 200, 20);
		add(tfName);
		  
		lblPhone = new JLabel("* 전화번호 : ");
		lblPhone.setBounds(80,65,75,20);
		lblPhone.setFont(font);
		add(lblPhone);
		
		tfPhone = new JTextField();
		tfPhone.setBounds(155,65, 200, 20);
		add(tfPhone);
		
		lblCountry = new JLabel("* 국        가 : ");
		lblCountry.setBounds(80,100,75,20);
		lblCountry.setFont(font);
		add(lblCountry);
		
		tfCountry = new JTextField();
		tfCountry.setBounds(155, 100, 150, 20);
		add(tfCountry);
		
		lblAddress = new JLabel("* 주        소 : ");
		lblAddress.setBounds(80,135,75,20);
		lblAddress.setFont(font);
		add(lblAddress);
		
		tfAddress = new JTextField();
		tfAddress.setBounds(155, 135, 350, 20);
		add(tfAddress);			
		
		//ComboBox: Airline Names (Statistics)
		comboNames = new Vector<>();
		comboNames = db.AirlineComboNames();
		cbAirlines = new JComboBox<String>(comboNames);
		cbAirlines.setSelectedItem(0);
		cbAirlines.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				String name = cbAirlines.getSelectedItem().toString();
				System.out.println("Name: "+name);
				model.setRowCount(0);
				db.AirlineSelectName(name);
			}			
		});
		cbAirlines.setBounds(75, 170, 130, 20);
		add(cbAirlines);
		
		//등록 버튼: 테이블 새로 한 줄 추가
		btnAlEnroll = new JButton("등록");
		btnAlEnroll.addActionListener(this);
		btnAlEnroll.setBounds(778, 155, 62, 30);
		add(btnAlEnroll);
		
		//삭제 버튼: 선택된 테이블 한 줄 삭제
		btnAlDelete = new JButton("삭제");
		btnAlDelete.addActionListener(this);
		btnAlDelete.setBounds(848, 155, 62, 30);
		add(btnAlDelete);
	}
	
	/** UI of Table and Search Part */
	private void Table_init(){
		//Initialize column names
		alColNames = new Vector<>();
		alColNames.add("ID");
		alColNames.add("이름");
		alColNames.add("국가");
		alColNames.add("주소");
		alColNames.add("전화번호");
		
		model = new DefaultTableModel(alColNames, 0);
		db.Table_Initialize(CLASS_ID);
		
		//Create a table
		airlineTable = new JTable(model);		
		
		//Table settings
		airlineTable.addMouseListener(new JTableMouseListener());
		airlineTable.getTableHeader().setReorderingAllowed(false);		//테이블 칼럼 이동 방지
		//Enable multiple selection
		airlineTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		tableCellCenter(airlineTable);
		setColumnSize(airlineTable);
		scroll = new JScrollPane(airlineTable);
		scroll.setBounds(70, 200, 850, 360);
		add(scroll);
		
		//ComboBox: Search			
		cbSearch = new JComboBox(alCombo);
		cbSearch.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				searchMode = cbSearch.getSelectedIndex();
				System.out.println("Search Mode: "+searchMode);
			}			
		});
		cbSearch.setSelectedIndex(0);
		cbSearch.setBounds(280, 580, 80, 30);
		add(cbSearch);
		
		//TextField: Search
		tfSearch = new JTextField();
		tfSearch.setBounds(380, 580, 200, 30);			
		add(tfSearch);
		
		//검색 버튼
		btnAlSearch = new JButton("검색");
		btnAlSearch.setBounds(600, 580, 62, 30);
		btnAlSearch.addActionListener(this);
		add(btnAlSearch);
	}
	
	/** Show the Coordinate of Cursor */
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
	
	/** Add a New Row */
	void AddRow(){	
		//Get last id number
		id = db.getID(CLASS_ID);
		//Get text from TextField
		String name = tfName.getText();
		String phone = tfPhone.getText();
		String country = tfCountry.getText();
		String address = tfAddress.getText();
		String[] rows = {id,name,country,address,phone};
		
		//Check if essential fields are filled or not
		if(name.isEmpty() | phone.isEmpty() | country.isEmpty() | address.isEmpty())
			JOptionPane.showMessageDialog(null, "필수 입력칸을 모두 채워주세요.",
					"Message",JOptionPane.ERROR_MESSAGE);
		else{
			db.InsertData(CLASS_ID,rows);
			model.addRow(rows);
			cbAirlines.addItem(name);
		}
		
		//Reset TextField
		tfName.setText(null);
		tfPhone.setText(null);
		tfCountry.setText(null);
		tfAddress.setText(null);
		//Reset ComboBox
		cbAirlines.setSelectedIndex(0);
	}
	
	/** Delete Selected Row */
	void DelRow(){
		if(airlineTable.getSelectedRowCount()>0){
			for(int i : airlineTable.getSelectedRows()){
				db.DeleteData(CLASS_ID, String.valueOf(model.getValueAt(i, 0)));
			}
			
			//Reset ComboBox
			cbAirlines.setSelectedIndex(0);
			//Get last id number
			id = db.getID(CLASS_ID);
		}
		else{
			JOptionPane.showMessageDialog(null, "선택된 컬럼이 없습니다.",
					"Message",JOptionPane.ERROR_MESSAGE);
		}
	}

	/** Action Listener */
	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();	//Get Selected Object
		
		if(source.equals(btnAlEnroll)){
			AddRow();
		}
		else if(source.equals(btnAlDelete)){
			DelRow();
		}
		else if(source.equals(btnAlSearch)){
			String keyWord = tfSearch.getText().trim();
			if(keyWord.isEmpty())
				JOptionPane.showMessageDialog(null, "검색어를 입력하세요.",
						"Message",JOptionPane.ERROR_MESSAGE);
			else{
				switch(searchMode){
					case 0: db.AirlineSearch(SEARCH_ALL,keyWord); break;
					case 1: db.AirlineSearch(SEARCH_ID,keyWord); break;
					case 2: db.AirlineSearch(SEARCH_NAME,keyWord); break;
					case 3: db.AirlineSearch(SEARCH_COUNTRY,keyWord); break;
					case 4: db.AirlineSearch(SEARCH_ADDRESS,keyWord); break;
					case 5: db.AirlineSearch(SEARCH_PHONE,keyWord); break;
				}
			}
			//Reset
			tfSearch.setText(null);
			cbSearch.setSelectedIndex(0);
		}
	}	

	/** Set the Alignment of the Rows */
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
		
		//Set the columns' width
		tcm.getColumn(0).setPreferredWidth(25);
		tcm.getColumn(1).setPreferredWidth(100);
		tcm.getColumn(2).setPreferredWidth(50);
		tcm.getColumn(3).setPreferredWidth(400);
		tcm.getColumn(4).setPreferredWidth(100);
		
		//Fix the columns' location
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
			
			//System.out.println(model.getValueAt(row, 1));	//눌려진 행의 부분에서 1번째(2번째 열) 값 출력
			//System.out.println(model.getValueAt(row, col));	//눌려진 행과 열에 해당하는 선택된 데이터 하나 출력			
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
