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
	//DB���� Ŭ���� ���п� ����� ID
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
	private JLabel lblCount1;	//�¼� �� ī��Ʈ�� ("��")
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
	String apCombo[] = {"��ü","ID","�װ���","���ۻ�","����",
			"�ϵ","����Ͻ�","���ڳ��","����","ũ��"};
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
	
	/** ���/���� ��ư �� ��� �κ� UI */
	private void Enroll_init(){
		//Label�� ����� ��Ʈ
		font = new Font("",Font.BOLD,12);
		
		//��� �κ� ���
		lblAirline = new JLabel("* ��  ��  �� : ");
		lblAirline.setBounds(80,30,85,20);
		lblAirline.setFont(font);
		add(lblAirline);
		
		//ComboBox: Search			
		cbAirline = new JComboBox(alCombo);
		cbAirline.addActionListener(this);
		cbAirline.setBounds(165, 30, 100, 20);
		add(cbAirline);
		
		lblAircraft = new JLabel("* ��  ��  �� : ");
		lblAircraft.setBounds(80,65,85,20);
		lblAircraft.setFont(font);
		add(lblAircraft);
		
		//Aircraft Radio Button
		aircraft = new ButtonGroup();
		aircraftBoing = new JRadioButton("����");
		aircraftAirbus = new JRadioButton("�������");
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
		  
		lblType = new JLabel("* ��        �� : ");
		lblType.setBounds(80,100,75,20);
		lblType.setFont(font);
		add(lblType);
		
		//Type Radio Button
		type = new ButtonGroup();
		typeJet = new JRadioButton("��Ʈ��");
		typeTurbo = new JRadioButton("�ͺ� �����緯 �װ���");
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
		
		lblmeter2 = new JLabel("m");
		lblmeter2.setBounds(430, 170, 30, 20);
		add(lblmeter2);
		
		//��� ��ư: ���̺� ���� �� �� �߰�
		btnApEnroll = new JButton("���");
		btnApEnroll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnAddRow();					
			}
		});
		btnApEnroll.setBounds(778, 170, 62, 30);
		add(btnApEnroll);
		
		//���� ��ư: ���õ� ���̺� �� �� ����
		btnApDelete = new JButton("����");
		btnApDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnDelRow();					
			}
		});
		btnApDelete.setBounds(848, 170, 62, 30);
		add(btnApDelete);
	}
	
	/** ���̺� �� �˻� �κ� UI 
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
		apColNames.add("�װ���");
		apColNames.add("���ۻ�");
		apColNames.add("����");
		apColNames.add("�ϵ");
		apColNames.add("����Ͻ�");
		apColNames.add("���ڳ��");
		apColNames.add("����");
		apColNames.add("ũ��");
		
		//Create a Table with Data and Column Names
		airlineTable = new JTable(data,apColNames);		
		
		//Table Settings
		airlineTable.addMouseListener(new JTableMouseListener());
		airlineTable.getTableHeader().setReorderingAllowed(false);		//���̺� Į�� �̵� ����
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
		
		//�˻� ��ư
		btnApSearch = new JButton("�˻�");
		btnApSearch.setBounds(600, 580, 62, 30);
		btnApSearch.addActionListener(this);
		add(btnApSearch);
	}
	
	/** ��ǥ ǥ�� */
	public void Location(){
		location = new JLabel("���� ��ǥ");
		MouseMotionAdapter ma = new MouseMotionAdapter() {
			public void mouseMoved(MouseEvent e) {
                // ���콺 ��ǥ ������
                int x = e.getX();
                int y = e.getY();
                String str = "x��ǥ:" + x + ",y��ǥ:" + y;

                // Label�� ���ڿ� �ֱ�
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
		Object source = e.getSource();	//���õ� ��ư ��������
		
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
		
		//tcm.getColumn(0).setPreferredWidth(5);
		/*tcm.getColumn(0).setPreferredWidth(25);
		tcm.getColumn(1).setPreferredWidth(100);
		tcm.getColumn(2).setPreferredWidth(50);
		tcm.getColumn(3).setPreferredWidth(400);
		tcm.getColumn(4).setPreferredWidth(100);*/
		
		//��ü �� ������ ���� �Ұ�
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
			
			System.out.println(model.getValueAt(row, 1));	//������ ���� �κп��� 1��°(2��° ��) �� ���
			System.out.println(model.getValueAt(row, col));	//������ ��� ���� �ش��ϴ� ���õ� ������ �ϳ� ���			
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
