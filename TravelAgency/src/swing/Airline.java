package swing;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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
 * <p><b> Airline Class </b> 
 * : Airline UI & Functions</p>
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
 * @version 1.1 12/02/15
 * @author ������, ����, ������
 * */
@SuppressWarnings("serial")
public class Airline extends JPanel implements ActionListener{
	//Instance variables
	//Class id for distinguishing tabs(classes)
	private final int CLASS_ID = 1;
	
	//Search mode constant
	private final int SEARCH_NONE = 0;	//For initializing the table rows
	private final int SEARCH_ALL = 1;
	private final int SEARCH_ID = 2;
	private final int SEARCH_NAME = 3;
	private final int SEARCH_COUNTRY = 4;
	private final int SEARCH_ADDRESS = 5;
	private final int SEARCH_PHONE = 6;
	
	//JButton
	private JButton btnAlAddnUpdate;
	private JButton btnAlDelete;
	private JButton btnAlSearch;
	
	//JLabel
	private JLabel lblName;
	private JLabel lblPhone;
	private JLabel lblAddress;
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
	private JComboBox<String> cbSearch;
	private JComboBox<String> cbAirlines;

	//Vector<String>: Column Names, Airline Name ComboBox Items
	private Vector<String> alColNames;
	private Vector<String> comboNames;
	
	//String: ComboBox Items, Id for the last id number
	private String alCombo[] = {"��ü","ID","�̸�","����","�ּ�","��ȭ��ȣ"};	
	private String id;
	
	//Int: Get Search Mode
	private int searchMode;
	
	//DefaultTableModel
	public static DefaultTableModel model;
	
	//Database Class
	Database db;
	
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
	}
	
	/** UI of Enroll/Delete Buttons and Enroll Form */
	private void Enroll_init(){
		//Font for Label
		font = new Font("",Font.BOLD,12);
		
		//Enroll Form
		lblName = new JLabel("* ��        �� : ");
		lblName.setBounds(80,30,75,20);
		lblName.setFont(font);
		add(lblName);
		
		tfName = new JTextField();
		tfName.setBounds(155, 30, 200, 20);
		add(tfName);
		  
		lblPhone = new JLabel("* ��ȭ��ȣ : ");
		lblPhone.setBounds(80,65,75,20);
		lblPhone.setFont(font);
		add(lblPhone);
		
		tfPhone = new JTextField();
		tfPhone.setBounds(155,65, 200, 20);
		add(tfPhone);
		
		lblCountry = new JLabel("* ��        �� : ");
		lblCountry.setBounds(80,100,75,20);
		lblCountry.setFont(font);
		add(lblCountry);
		
		tfCountry = new JTextField();
		tfCountry.setBounds(155, 100, 150, 20);
		add(tfCountry);
		
		lblAddress = new JLabel("* ��        �� : ");
		lblAddress.setBounds(80,135,75,20);
		lblAddress.setFont(font);
		add(lblAddress);
		
		tfAddress = new JTextField();
		tfAddress.setBounds(155, 135, 350, 20);
		add(tfAddress);			
		
		//ComboBox: Airline Names (Statistics)
		comboNames = new Vector<>();
		comboNames = db.AirlineComboNames(CLASS_ID);
		cbAirlines = new JComboBox<String>(comboNames);
		cbAirlines.setSelectedItem(0);
		cbAirlines.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if(cbAirlines.getSelectedItem()!=null){
					String name = cbAirlines.getSelectedItem().toString();
					model.setRowCount(0);
					db.AirlineSelectName(name);
				}
			}			
		});
		cbAirlines.setBounds(75, 170, 130, 20);
		add(cbAirlines);
		
		//��� ��ư: ���̺� ���� �� �� �߰�
		btnAlAddnUpdate = new JButton("���");
		btnAlAddnUpdate.addActionListener(this);
		btnAlAddnUpdate.setBounds(778, 155, 62, 30);
		add(btnAlAddnUpdate);
		
		//���� ��ư: ���õ� ���̺� �� �� ����
		btnAlDelete = new JButton("����");
		btnAlDelete.addActionListener(this);
		btnAlDelete.setBounds(848, 155, 62, 30);
		add(btnAlDelete);
	}
	
	/** 
	 * UI of Table and Search Part
	 * @param
	 * @return
	 * */
	private void Table_init(){
		//Initialize column names
		alColNames = new Vector<>();
		alColNames.add("ID");
		alColNames.add("�̸�");
		alColNames.add("����");
		alColNames.add("�ּ�");
		alColNames.add("��ȭ��ȣ");
		
		model = new DefaultTableModel(alColNames, 0){
			//Prevent editing cells
			public boolean isCellEditable(int row, int column){
				if(column>=0)	return false;
				else return true;
			}
		};

		//Initialize DefaultTableModel
		db.AirlineSearch(SEARCH_NONE, null);
		
		//Create a table
		airlineTable = new JTable(model);		
		
		//Table settings
		//Enable auto row sorting
		airlineTable.setAutoCreateRowSorter(true);
		//Add mouse listener
		airlineTable.addMouseListener(new JTableMouseListener());
		//Fix the column's location
		airlineTable.getTableHeader().setReorderingAllowed(false);		
		//Enable multiple selection
		airlineTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		
		tableCellCenter(airlineTable);
		setColumnSize(airlineTable);
		scroll = new JScrollPane(airlineTable);
		scroll.setBounds(70, 200, 850, 360);
		add(scroll);
		
		//ComboBox: Search			
		cbSearch = new JComboBox<String>(alCombo);
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
		btnAlSearch = new JButton("�˻�");
		btnAlSearch.setBounds(600, 580, 62, 30);
		btnAlSearch.addActionListener(this);
		add(btnAlSearch);
	}
	
	/** Add a New Row & Update Selected Row */
	private void AddnUpdateRow(int flag){	
		//Get text from TextField
		String name = tfName.getText();
		String phone = tfPhone.getText();
		String country = tfCountry.getText();
		String address = tfAddress.getText();
		
		String[] rows = {id,name,country,address,phone};
		
		//Check if essential fields are filled or not
		if(name.isEmpty() || phone.isEmpty() || country.isEmpty() || address.isEmpty())
			JOptionPane.showMessageDialog(null, "�ʼ� �Է�ĭ�� ��� ä���ּ���.",
					"Message",JOptionPane.ERROR_MESSAGE);
		else{
			if(flag==0){	//Add a new row
				db.InsertData(CLASS_ID,rows);
				model.addRow(rows);
				cbAirlines.addItem(name);
			}
			else{			//Update selected row
				db.UpdateData(CLASS_ID,rows);
				JOptionPane.showMessageDialog(null, "���� �Ǿ����ϴ�.",
						"Message",JOptionPane.OK_OPTION);
				//Reset button text
				btnAlAddnUpdate.setText("���");
				btnAlDelete.setText("����");
				//Reset ComboBox
				cbAirlines.removeAllItems();
				comboNames = db.AirlineComboNames(CLASS_ID);
				for(int i=0;i<comboNames.size();i++){
					cbAirlines.addItem(comboNames.get(i));
				}
			}
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
	private void DelRow(){
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
			JOptionPane.showMessageDialog(null, "���õ� �÷��� �����ϴ�.",
					"Message",JOptionPane.ERROR_MESSAGE);
		}
	}

	/** Action Listener */
	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();	//Get Selected Object
		
		if(source.equals(btnAlAddnUpdate)){
			//Check if it's add mode(0) or update mode(1)
			if(btnAlAddnUpdate.getText().equals("���")){
				//Get last id number
				id = db.getID(CLASS_ID);
				AddnUpdateRow(0);
			}
			else{
				AddnUpdateRow(1);
			}
		}
		else if(source.equals(btnAlDelete)){
			if(btnAlDelete.getText().equals("����")){
				DelRow();
			}
			else{
				//Reset TextField
				tfName.setText(null);
				tfPhone.setText(null);
				tfCountry.setText(null);
				tfAddress.setText(null);
				//Reset ComboBox
				cbAirlines.setSelectedIndex(0);
				
				btnAlDelete.setText("����");
				btnAlAddnUpdate.setText("���");
			}
		}
		else if(source.equals(btnAlSearch)){
			String keyWord = tfSearch.getText().trim();
			if(keyWord.isEmpty())
				JOptionPane.showMessageDialog(null, "�˻�� �Է��ϼ���.",
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
	
	/** Set the Alignment of the Rows to Center */
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
			//Action when you right-click the column
			if(e.getButton()==3){
				//Check if the columns are multi-selected or not
				if(airlineTable.getSelectedRowCount()>1){
					JOptionPane.showMessageDialog(null, "������ �÷��� �� ���� �����ϼ���.",
							"Message",JOptionPane.ERROR_MESSAGE);
				}
				else if(airlineTable.getSelectedRow()==-1){
					JOptionPane.showMessageDialog(null, "������ Į���� ���� ���콺 ���� Ŭ�����ּ���.",
							"Message",JOptionPane.ERROR_MESSAGE);
				}
				else{
					int row = airlineTable.getSelectedRow();
					for(int i=0;i<airlineTable.getColumnCount();i++){
						if(i==0)	id = (String) model.getValueAt(row, i);
						if(i==1)	tfName.setText((String) model.getValueAt(row, i));
						else if(i==2)	tfCountry.setText((String) model.getValueAt(row, i));
						else if(i==3)	tfAddress.setText((String) model.getValueAt(row, i));
						else if(i==4)	tfPhone.setText((String) model.getValueAt(row, i));
					}
					btnAlAddnUpdate.setText("����");
					btnAlDelete.setText("���");
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
