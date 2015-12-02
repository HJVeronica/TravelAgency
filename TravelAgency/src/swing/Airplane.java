package swing;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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
 * <p><b> Airplane Class </b> 
 * : Airplane UI & Functions</p>
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
 * @version 1.0 12/02/15
 * @author ������, ����, ������
 * */
@SuppressWarnings("serial")
public class Airplane extends JPanel implements ActionListener, ItemListener{
	//Instance variables
	//Class id for distinguishing tabs(classes)
	private final int CLASS_ID = 3;
	
	//Search mode constant
	private final int SEARCH_NONE = 0;	//For initializing the table rows
	private final int SEARCH_ALL = 1;
	private final int SEARCH_ID = 2;
	private final int SEARCH_AIRLINE = 3;
	private final int SEARCH_AIRCRAFT = 4;
	private final int SEARCH_TYPE = 5;
	private final int SEARCH_FIRST = 6;
	private final int SEARCH_BUSINESS = 7;
	private final int SEARCH_ECONOMY = 8;
	private final int SEARCH_LENGTH = 9;
	private final int SEARCH_SIZE = 10;
	
	//JButton
	private JButton btnApAddnUpdate;
	private JButton btnApDelete;
	private JButton btnApSearch;
	private JButton btnShowAll;
	
	//JLabel
	private JLabel lblAircraft;
	private JLabel lblType;
	private JLabel lblFirst;
	private JLabel lblBusiness;
	private JLabel lblEconomy;
	private JLabel lblLength;
	private JLabel lblSize;
	private JLabel lblAirline;
	private JLabel lblCount1;	//�¼� �� ī��Ʈ�� ("��")
	private JLabel lblCount2;
	private JLabel lblCount3;
	private JLabel lblmeter1;
	private JLabel lblmeter2;
	
	//Font, JScrollPane, JTable
	private Font font;
	private JScrollPane scroll;
	private JTable airplaneTable;
	
	//JRadioButton: Aircraft
	ButtonGroup aircraft;
	JRadioButton aircraftBoeing;
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
	private JComboBox<String> cbSearch;
	private JComboBox<String> cbAirline;
	
	//Vector<String>: Column Names, Airline Name ComboBox Items
	Vector<String> apColNames;
	Vector<String> comboNames;
	
	//String: ComboBox Items 
	String apCombo[] = {"��ü","ID","�װ���","���ۻ�","����",
			"�ϵ","����Ͻ�","���ڳ��","����","ũ��"};
	private String id;		//Id for the last id number
	private String aId;		//Id of airline
	private String at;		//Get chosen radio button from aircraft group
	private String tp;		//Get chosen radio button from type group
	private String airline;	//Get chosen item form airline names combo box
	
	//Int: Get Search Mode
	private int searchMode;
	
	//DefaultTableModel
	public static DefaultTableModel model;
	
	//Database Class
	Database db;
	
	/** Airplane Constructor */
	public Airplane(){
		setLayout(null);		//Delete Layout Manager
		setBackground(Color.LIGHT_GRAY);
		
		//Connect to DB
		db = new Database();
		
		//Get last id number
		id = db.getID(CLASS_ID);
		
		Enroll_init();
		Table_init();
	}
	
	/** ���/���� ��ư �� ��� �κ� UI */
	private void Enroll_init(){
		//Font for Labels
		font = new Font("",Font.BOLD,12);
		
		//Enroll Form
		lblAirline = new JLabel("* ��  ��  �� : ");
		lblAirline.setBounds(80,30,85,20);
		lblAirline.setFont(font);
		add(lblAirline);
		
		//ComboBox: Airline Names
		comboNames = new Vector<>();
		comboNames = db.AirlineComboNames(CLASS_ID);
		cbAirline = new JComboBox<String>(comboNames);
		cbAirline.setSelectedItem(0);
		cbAirline.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if(cbAirline.getSelectedItem()!=null){
					airline = cbAirline.getSelectedItem().toString();
					System.out.println("airline: "+airline);
				}
			}			
		});
		cbAirline.setBounds(165, 30, 100, 20);
		add(cbAirline);
		
		lblAircraft = new JLabel("* ��  ��  �� : ");
		lblAircraft.setBounds(80,65,85,20);
		lblAircraft.setFont(font);
		add(lblAircraft);
		
		//Aircraft Radio Button
		aircraft = new ButtonGroup();
		aircraftBoeing = new JRadioButton("Boeing");
		aircraftAirbus = new JRadioButton("Airbus");
		aircraft.add(aircraftBoeing);
		aircraft.add(aircraftAirbus);
		aircraftBoeing.addItemListener(this);
		aircraftAirbus.addItemListener(this);
		aircraftBoeing.setBounds(160, 65, 60, 20);
		aircraftBoeing.setFont(font);
		aircraftBoeing.setBackground(Color.LIGHT_GRAY);
		aircraftAirbus.setBounds(230, 65, 80, 20);
		aircraftAirbus.setFont(font);
		aircraftAirbus.setBackground(Color.LIGHT_GRAY);
		add(aircraftBoeing);
		add(aircraftAirbus);
		  
		lblType = new JLabel("* ��        �� : ");
		lblType.setBounds(80,100,75,20);
		lblType.setFont(font);
		add(lblType);
		
		//Type Radio Button
		type = new ButtonGroup();
		typeJet = new JRadioButton("��Ʈ��");
		typeTurbo = new JRadioButton("�ͺ� �����緯");
		type.add(typeJet);
		type.add(typeTurbo);
		typeJet.addItemListener(this);
		typeTurbo.addItemListener(this);
		typeJet.setBounds(160, 100, 70, 20);
		typeJet.setFont(font);
		typeJet.setBackground(Color.LIGHT_GRAY);
		typeTurbo.setBounds(230, 100, 100, 20);
		typeTurbo.setFont(font);
		typeTurbo.setBackground(Color.LIGHT_GRAY);
		add(typeJet);
		add(typeTurbo);
		
		lblFirst = new JLabel("* ��  ��  �� : ");
		lblFirst.setBounds(80,135,85,20);
		lblFirst.setFont(font);
		add(lblFirst);
		
		tfFirst = new JTextField();
		tfFirst.setBounds(165, 135, 40, 20);
		add(tfFirst);
		
		lblCount1 = new JLabel("��");
		lblCount1.setBounds(220, 135, 30, 20);
		add(lblCount1);
		
		lblBusiness = new JLabel("* ����Ͻ� : ");
		lblBusiness.setBounds(265,135,85,20);
		lblBusiness.setFont(font);
		add(lblBusiness);
		
		tfBusiness = new JTextField();
		tfBusiness.setBounds(350,135, 40, 20);
		add(tfBusiness);
		
		lblCount2 = new JLabel("��");
		lblCount2.setBounds(405, 135, 30, 20);
		add(lblCount2);
		
		lblEconomy = new JLabel("* ���ڳ�� : ");
		lblEconomy.setBounds(450,135,85,20);
		lblEconomy.setFont(font);
		add(lblEconomy);
		
		tfEconomy = new JTextField();
		tfEconomy.setBounds(535,135, 40, 20);
		add(tfEconomy);
		
		lblCount3 = new JLabel("��");
		lblCount3.setBounds(590, 135, 30, 20);
		add(lblCount3);
		
		lblLength = new JLabel("* ��        �� : ");
		lblLength.setBounds(80,170,85,20);
		lblLength.setFont(font);
		add(lblLength);
		
		tfLength = new JTextField();
		tfLength.setBounds(165, 170, 50, 20);
		add(tfLength);		
		
		lblmeter1 = new JLabel("m");
		lblmeter1.setBounds(230, 170, 30, 20);
		add(lblmeter1);
		
		lblSize = new JLabel("* ũ        �� : ");
		lblSize.setBounds(265,170,85,20);
		lblSize.setFont(font);
		add(lblSize);
		
		tfSize = new JTextField();
		tfSize.setBounds(350, 170, 50, 20);
		add(tfSize);			
		
		lblmeter2 = new JLabel("m^2");
		lblmeter2.setBounds(415, 170, 30, 20);
		add(lblmeter2);
		
		//ShowAll Button: Refresh the table
		btnShowAll = new JButton("��ü����");
		btnShowAll.addActionListener(this);
		btnShowAll.setBounds(778, 130, 132, 30);
		add(btnShowAll);
		
		//��� ��ư: ���̺� ���� �� �� �߰�
		btnApAddnUpdate = new JButton("���");
		btnApAddnUpdate.addActionListener(this);
		btnApAddnUpdate.setBounds(778, 170, 62, 30);
		add(btnApAddnUpdate);
		
		//���� ��ư: ���õ� ���̺� �� �� ����
		btnApDelete = new JButton("����");
		btnApDelete.addActionListener(this);
		btnApDelete.setBounds(848, 170, 62, 30);
		add(btnApDelete);
	}
	
	/** ���̺� �� �˻� �κ� UI */
	private void Table_init(){
		//Initialize Column Names
		apColNames = new Vector<>();
		apColNames.add("ID");
		apColNames.add("�װ���");
		apColNames.add("���ۻ�");
		apColNames.add("����");
		apColNames.add("�ϵ");
		apColNames.add("����Ͻ�");
		apColNames.add("���ڳ��");
		apColNames.add("����(m)");
		apColNames.add("ũ��(m^2)");
		
		model = new DefaultTableModel(apColNames, 0){
			//Prevent editing cells
			public boolean isCellEditable(int row, int column){
				if(column>=0)	return false;
				else return true;
			}
		};
		
		db.AirplaneSearch(SEARCH_NONE, null);
		
		//Create a table
		airplaneTable = new JTable(model);	
		
		//Table settings
		//Enable auto row sorting
		airplaneTable.setAutoCreateRowSorter(true);
		//Add mouse listener
		airplaneTable.addMouseListener(new JTableMouseListener());
		//Fix the column's location
		airplaneTable.getTableHeader().setReorderingAllowed(false);		
		//Enable multiple selection
		airplaneTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		
		tableCellCenter(airplaneTable);
		setColumnSize(airplaneTable);
		scroll = new JScrollPane(airplaneTable);
		scroll.setBounds(70, 210, 850, 350);
		add(scroll);
		
		//ComboBox: Search			
		cbSearch = new JComboBox<String>(apCombo);
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
		
		//�˻� ��ư
		btnApSearch = new JButton("�˻�");
		btnApSearch.setBounds(600, 580, 62, 30);
		btnApSearch.addActionListener(this);
		add(btnApSearch);
	}
	
	/** Add a New Row & Update Selected Row */
	void AddnUpdateRow(int flag){
		//Get text and information from TextField and RadioButton
		String first = tfFirst.getText();
		String business = tfBusiness.getText();
		String economy = tfEconomy.getText();
		String length = tfLength.getText();
		String size = tfSize.getText();
		aId = db.GetForeignKeyfromOtherTables("aid", airline);

		//String[] for DB
		String[] rowsforDB = {id,aId,at,tp,first,business,economy,length,size};
		//String[] for insertion
		String[] rowsforInsert = {id,airline,at,tp,first,business,economy,length,size};
		
		//Check if essential fields are filled or not
		if(cbAirline.getSelectedIndex() == 0 || tp==null || at==null || first.isEmpty()
				|| business.isEmpty() || economy.isEmpty() || length.isEmpty() || size.isEmpty())
			JOptionPane.showMessageDialog(null, "�ʼ� �Է�ĭ�� ��� ä���ּ���.",
					"Message",JOptionPane.ERROR_MESSAGE);
		else{
			if(flag==0){	//Add a new row
				db.InsertData(CLASS_ID,rowsforDB);
				model.addRow(rowsforInsert);
			}
			else{			//Update selected row
				db.UpdateData(CLASS_ID,rowsforDB);
				JOptionPane.showMessageDialog(null, "���� �Ǿ����ϴ�.",
						"Message",JOptionPane.OK_OPTION);
				//Reset button text
				btnApAddnUpdate.setText("���");
				btnApDelete.setText("����");
				
				//Refresh the table
				db.AirplaneSearch(SEARCH_NONE, null);
			}
			//Reset TextField
			tfFirst.setText(null);
			tfBusiness.setText(null);
			tfEconomy.setText(null);
			tfLength.setText(null);
			tfSize.setText(null);
			
			//Reset RadioButton
			aircraft.clearSelection();
			type.clearSelection();
			
			//Reset ComboBox
			cbAirline.setSelectedIndex(0);
			
			//Reset variables
			at=null;
			tp=null;
		}
	}
	
	/** Delete Selected Row */
	void DelRow(){
		if(airplaneTable.getSelectedRowCount()>0){
			for(int i : airplaneTable.getSelectedRows()){
				db.DeleteData(CLASS_ID, String.valueOf(model.getValueAt(i, 0)));
			}
			
			//Refresh the table
			db.AirplaneSearch(SEARCH_NONE,null);
			
			//Get last id number
			id = db.getID(CLASS_ID);
		}
		else{
			JOptionPane.showMessageDialog(null, "���õ� �÷��� �����ϴ�.",
					"Message",JOptionPane.ERROR_MESSAGE);
		}
	}
	
	/** Radio Button Item Listener */
	@Override
	public void itemStateChanged(ItemEvent e) {
		Object source = e.getSource();	//Get Selected Object
		
		//Aircraft group radio buttons
		if(source == aircraftAirbus || source == aircraftBoeing){
			if(aircraftAirbus.isSelected()){
				at = "Airbus";
			}
			else if(aircraftBoeing.isSelected()){
				at ="Boeing";
			}
			else{}
		}
		
		//Type group radio buttons
		if(source == typeJet || source == typeTurbo){
			if(typeJet.isSelected()){
				tp = "��Ʈ��";
			}
			else if(typeTurbo.isSelected()){
				tp ="�ͺ� �����緯";
			}
			else{}
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();	//���õ� ��ư ��������
		
		if(source.equals(btnApAddnUpdate)){
			//Check if it's add mode(0) or update mode(1)
			if(btnApAddnUpdate.getText().equals("���")){
				//Get last id number
				id = db.getID(CLASS_ID);
				AddnUpdateRow(0);
			}
			else{
				AddnUpdateRow(1);
			}
		}
		else if(source.equals(btnApDelete)){
			if(btnApDelete.getText().equals("����")){
				System.out.println("����");
				DelRow();
			}
			else{
				//Reset TextField
				tfFirst.setText(null);
				tfBusiness.setText(null);
				tfEconomy.setText(null);
				tfLength.setText(null);
				tfSize.setText(null);
				
				//Reset RadioButton
				aircraft.clearSelection();
				type.clearSelection();
				
				//Reset ComboBox
				cbAirline.setSelectedIndex(0);
				
				//Reset variables
				at=null;
				tp=null;
				
				btnApDelete.setText("����");
				btnApAddnUpdate.setText("���");
			}
		}
		else if(source.equals(btnApSearch)){
			String keyWord = tfSearch.getText().trim();
			if(keyWord.isEmpty())
				JOptionPane.showMessageDialog(null, "�˻�� �Է��ϼ���.",
						"Message",JOptionPane.ERROR_MESSAGE);
			else{
				switch(searchMode){
					case 0: db.AirplaneSearch(SEARCH_ALL,keyWord); break;
					case 1: db.AirplaneSearch(SEARCH_ID,keyWord); break;
					case 2: db.AirplaneSearch(SEARCH_AIRLINE,keyWord); break;
					case 3: db.AirplaneSearch(SEARCH_AIRCRAFT,keyWord); break;
					case 4: db.AirplaneSearch(SEARCH_TYPE,keyWord); break;
					case 5: db.AirplaneSearch(SEARCH_FIRST,keyWord); break;
					case 6: db.AirplaneSearch(SEARCH_BUSINESS,keyWord); break;
					case 7: db.AirplaneSearch(SEARCH_ECONOMY,keyWord); break;
					case 8: db.AirplaneSearch(SEARCH_LENGTH,keyWord); break;
					case 9: db.AirplaneSearch(SEARCH_SIZE,keyWord); break;
				}
			}
			
			//Reset
			tfSearch.setText(null);
			cbSearch.setSelectedIndex(0);
		}
		else if(source.equals(btnShowAll)){
			id = db.getID(CLASS_ID);
			db.AirplaneSearch(SEARCH_NONE, null);
		}
	}
	
	/** ���̺� ���� ��� ���� */
	private void tableCellCenter(JTable t){
		DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
		dtcr.setHorizontalAlignment(SwingConstants.CENTER);		//Renderer�� ��� ���ķ�
		
		TableColumnModel tcm = t.getColumnModel();
		
		//��ü ���� ��� ����
		for(int i=0;i<tcm.getColumnCount();i++){
			tcm.getColumn(i).setCellRenderer(dtcr);
			//�𵨿��� �÷� ������ŭ �÷� �����ͼ� for������
			//������ �� Renderer�� �Ʊ� ������ dtcr�� set
		}
	}
	
	/** ���̺� �� ũ�� ���� �� ���� */
	private void setColumnSize(JTable t){
		TableColumnModel tcm = t.getColumnModel();
		
		tcm.getColumn(0).setPreferredWidth(20);
		tcm.getColumn(1).setPreferredWidth(80);
		tcm.getColumn(2).setPreferredWidth(60);
		tcm.getColumn(3).setPreferredWidth(80);
		
		for(int i=4;i<9;i++)
			tcm.getColumn(i).setPreferredWidth(40);
		
		//��ü �� ������ ���� �Ұ�
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
				if(airplaneTable.getSelectedRowCount()>1){
					JOptionPane.showMessageDialog(null, "������ �÷��� �� ���� �����ϼ���.",
							"Message",JOptionPane.ERROR_MESSAGE);
				}
				else if(airplaneTable.getSelectedRow()==-1){
					JOptionPane.showMessageDialog(null, "������ Į���� ���� ���콺 ���� Ŭ�����ּ���.",
							"Message",JOptionPane.ERROR_MESSAGE);
				}
				else{	//Set text to the form
					//Reset variables
					at = null;
					tp = null;
					
					int row = airplaneTable.getSelectedRow();
					for(int i=0;i<airplaneTable.getColumnCount();i++){
						if(i==0)	{ id = (String) model.getValueAt(row, i); }
						if(i==1)	{ cbAirline.setSelectedItem((String) model.getValueAt(row, i)); }
						else if(i==2)	{ 	//Aircraft
							String at_ = (String) model.getValueAt(row, i);
							if(at_.equals("Boeing")){	
								aircraftBoeing.setSelected(true);
							}
							else{
								aircraftAirbus.setSelected(true);
							}
						}
						else if(i==3)	{ 	//Type
							String tp_ = (String) model.getValueAt(row, i);
							if(tp_.equals("��Ʈ��")){
								typeJet.setSelected(true);
							}
							else{
								typeTurbo.setSelected(true);
							} 
						}
						else if(i==4){ tfFirst.setText((String) model.getValueAt(row, i)); }
						else if(i==5){ tfBusiness.setText((String) model.getValueAt(row, i)); }
						else if(i==6){ tfEconomy.setText((String) model.getValueAt(row, i)); }
						else if(i==7){ tfLength.setText((String) model.getValueAt(row, i));	}
						else if(i==8){ tfSize.setText((String) model.getValueAt(row, i)); }
					}
					
					btnApAddnUpdate.setText("����");
					btnApDelete.setText("���");
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
