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
import javax.swing.ComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
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

/** Airline UI & Functions */
public class Airline extends JPanel implements ActionListener{
	//DB���� Ŭ���� ���п� ����� ID
	private final int CLASS_ID = 1;
	
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
	
	//JCheckBox
	//private JCheckBox chBox;
	
	//Vector, String
	Vector<String> alColNames;
	String alCombo[] = {"��ü","ID","�̸�","����","�ּ�","��ȭ��ȣ"};
	Vector<String> comboNames;
	
	//DefaultTableModel
	public static DefaultTableModel model;
	
	//Database Class
	Database db = null;
	
	/** Airline Constructor */
	public Airline(){
		setLayout(null);		//Delete Layout Manager
		setBackground(Color.LIGHT_GRAY);
		
		//Connect to DB
		db = new Database();
		
		Enroll_init();
		Table_init();
		Location();
	}
	
	/** ���/���� ��ư �� ��� �κ� UI */
	private void Enroll_init(){
		//Label�� ����� ��Ʈ
		font = new Font("",Font.BOLD,12);
		
		//��� �κ� ���
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
		comboNames = db.AirlineComboNames(comboNames);
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
		
		//��� ��ư: ���̺� ���� �� �� �߰�
		btnAlEnroll = new JButton("���");
		btnAlEnroll.addActionListener(this);
		btnAlEnroll.setBounds(778, 155, 62, 30);
		add(btnAlEnroll);
		
		//���� ��ư: ���õ� ���̺� �� �� ����
		btnAlDelete = new JButton("����");
		btnAlDelete.addActionListener(this);
		btnAlDelete.setBounds(848, 155, 62, 30);
		add(btnAlDelete);
	}
	
	/** ���̺� �� �˻� �κ� UI */
	private void Table_init(){
		//Initialize Column Names
		alColNames = new Vector<>();
		//alColNames.add("ch");
		alColNames.add("ID");
		alColNames.add("�̸�");
		alColNames.add("����");
		alColNames.add("�ּ�");
		alColNames.add("��ȭ��ȣ");
		
		model = new DefaultTableModel(alColNames, 0);
		db.Table_Initialize(CLASS_ID);
		
		//Create a Table with Data and Column Names
		airlineTable = new JTable(model);		
		
		//Table Settings
		airlineTable.addMouseListener(new JTableMouseListener());
		airlineTable.getTableHeader().setReorderingAllowed(false);		//���̺� Į�� �̵� ����
		tableCellCenter(airlineTable);
		setColumnSize(airlineTable);
		scroll = new JScrollPane(airlineTable);
		scroll.setBounds(70, 200, 850, 360);
		add(scroll);
		
		//CheckBox for the Table
		/*chBox = new JCheckBox();
		chBox.setHorizontalAlignment(JLabel.CENTER);
		airlineTable.getColumn("ch").setCellEditor(new DefaultCellEditor(chBox));
		add(chBox);*/
		
		//ComboBox: Search			
		cbSearch = new JComboBox(alCombo);
		cbSearch.addActionListener(this);
		cbSearch.setBounds(280, 580, 80, 30);
		add(cbSearch);
		
		//TextField: Search
		tfSearch = new JTextField();
		tfSearch.setBounds(380, 580, 200, 30);			
		add(tfSearch);
		
		//�˻� ��ư
		btnAlSearch = new JButton("�˻�");
		btnAlSearch.setBounds(600, 580, 62, 30);
		btnAlSearch.addActionListener(this);
		add(btnAlSearch);
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
	
	/** Add a New Row */
	void btnAddRow(String[] rows){
		DefaultTableModel model = (DefaultTableModel)airlineTable.getModel();
		model.addRow(rows);
	}
	
	/** Delete Selected Row */
	void btnDelRow(){
		int row = airlineTable.getSelectedRow();
		if(row<0) return;
		DefaultTableModel model = (DefaultTableModel)airlineTable.getModel();
		model.removeRow(row);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();	//Get Selected Object
		
		if(source.equals(btnAlEnroll)){
			String id = db.getID(CLASS_ID);
			String name = tfName.getText();
			String phone = tfPhone.getText();
			String country = tfCountry.getText();
			String address = tfAddress.getText();
			String[] rows = {id,name,country,address,phone};
			
			if(name.isEmpty() | phone.isEmpty() | country.isEmpty() | address.isEmpty())
				JOptionPane.showMessageDialog(null, "�ʼ� �Է�ĭ�� ��� ä���ּ���!","Message",JOptionPane.ERROR_MESSAGE);
			else{
				db.InsertData(CLASS_ID,rows);
				btnAddRow(rows);
				comboNames.add(name);
			}
		}
		else if(source.equals(btnAlDelete)){
			btnDelRow();
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
		tcm.getColumn(0).setPreferredWidth(25);
		tcm.getColumn(1).setPreferredWidth(100);
		tcm.getColumn(2).setPreferredWidth(50);
		tcm.getColumn(3).setPreferredWidth(400);
		tcm.getColumn(4).setPreferredWidth(100);
		
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
