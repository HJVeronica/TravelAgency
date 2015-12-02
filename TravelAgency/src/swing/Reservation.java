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
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import main.Database;


/** Reservation UI & Functions */
public class Reservation extends JPanel implements ActionListener{
	//DB���� Ŭ���� ���п� ����� ID
	private final int CLASS_ID = 5;
	
	//JButton
	private JButton btnRvEnroll;
	private JButton btnRvDelete;
	private JButton btnRvSearch;
	
	//JLabel
	private JLabel lblCustomer;
	private JLabel lblFlight;
	private JLabel lblBookDate;
	private JLabel lblStaff;
	private JLabel lblPayment;
	private JLabel lblState;
	private JLabel lblRoute;
	private JLabel location;
	
	//Font, JScrollPane, JTable
	private Font font;
	private JScrollPane scroll;
	private JTable airlineTable;
	
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
	private JTextField tfFirst;
	private JTextField tfBusiness;
	private JTextField tfEconomy;
	private JTextField tfSearch;
	private JTextField tfLength;
	private JTextField tfSize;
	
	//JComboBox
	private JComboBox cbSearch;
	private JComboBox cbCustomer;
	private JComboBox cbFlight;
	private JComboBox cbStaff;
	
	//JCheckBox
	//private JCheckBox chBox;
	
	//Vector, String
	Vector<String> apColNames;
	String apCombo[] = {"��ü","ID","�װ���","���ۻ�","����",
			"�ϵ","����Ͻ�","���ڳ��","����","ũ��"};
	String RvComboCustomer[] = {"��OO","��OO","��OO","��OO","��OO"};
	String RvComboFlight[] = {"KE001","KE001","KE001","KE001","KE001"};
	String RvComboStaff[] = {"������", "����", "������"};
	
	public static DefaultTableModel model;
	
	//Database Class
	Database db = null;
	
	/** Airline Constructor */
	public Reservation(){
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
		lblCustomer = new JLabel("* ��  ��  �� : ");
		lblCustomer.setBounds(80,30,85,20);
		lblCustomer.setFont(font);
		add(lblCustomer);
		
		//ComboBox: Customer Names			
		cbCustomer = new JComboBox(RvComboCustomer);
		cbCustomer.addActionListener(this);
		cbCustomer.setBounds(165, 30, 80, 20);
		add(cbCustomer);
		
		lblStaff = new JLabel("* ������� : ");
		lblStaff.setBounds(275,30,85,20);
		lblStaff.setFont(font);
		add(lblStaff);
		
		//ComboBox: Customer Names			
		cbStaff = new JComboBox(RvComboStaff);
		cbStaff.addActionListener(this);
		cbStaff.setBounds(360, 30, 80, 20);
		add(cbStaff);
		
		lblFlight = new JLabel("* �װ���� : ");
		lblFlight.setBounds(80,65,85,20);
		lblFlight.setFont(font);
		add(lblFlight);
		
		//ComboBox: Customer Names			
		cbFlight = new JComboBox(RvComboFlight);
		cbFlight.addActionListener(this);
		cbFlight.setBounds(165, 65, 80, 20);
		add(cbFlight);
		
		lblRoute = new JLabel("* ��        �� : ");
		lblRoute.setBounds(275,65,85,20);
		lblRoute.setFont(font);
		add(lblRoute);
		
		tfLength = new JTextField();
		tfLength.setBounds(360, 65, 150, 20);
		add(tfLength);			

		  
		lblBookDate = new JLabel("* ��  ��  �� : ");
		lblBookDate.setBounds(80,100,75,20);
		lblBookDate.setFont(font);
		add(lblBookDate);

		lblPayment = new JLabel("* ���ҹ�� : ");
		lblPayment.setBounds(80,170,85,20);
		lblPayment.setFont(font);
		add(lblPayment);
		
		//Payment Radio Button
		payment = new ButtonGroup();
		paymentCash = new JRadioButton("����");
		paymentCredit = new JRadioButton("�ſ�ī��");
		paymentCheck = new JRadioButton("üũī��");
		payment.add(paymentCash);
		payment.add(paymentCredit);
		paymentCash.addActionListener(this);
		paymentCredit.addActionListener(this);
		paymentCheck.addActionListener(this);
		paymentCash.setBounds(160, 170, 70, 20);
		paymentCash.setFont(font);
		paymentCash.setBackground(Color.LIGHT_GRAY);
		paymentCredit.setBounds(230, 170, 80, 20);
		paymentCredit.setFont(font);
		paymentCredit.setBackground(Color.LIGHT_GRAY);
		paymentCheck.setBounds(320, 170, 80, 20);
		paymentCheck.setFont(font);
		paymentCheck.setBackground(Color.LIGHT_GRAY);
		add(paymentCash);
		add(paymentCredit);
		add(paymentCheck);

		lblState = new JLabel("* ������� : ");
		lblState.setBounds(80,200,85,20);
		lblState.setFont(font);
		add(lblState);
		
		//State Radio Button
		state = new ButtonGroup();
		stateOk = new JRadioButton("OK");
		stateStandby = new JRadioButton("���");
		state.add(stateOk);
		state.add(stateStandby);
		stateOk.addActionListener(this);
		stateStandby.addActionListener(this);
		stateOk.setBounds(160, 200, 70, 20);
		stateOk.setFont(font);
		stateOk.setBackground(Color.LIGHT_GRAY);
		stateStandby.setBounds(230, 200, 150, 20);
		stateStandby.setFont(font);
		stateStandby.setBackground(Color.LIGHT_GRAY);
		add(stateOk);
		add(stateStandby);
		
		//��� ��ư: ���̺� ���� �� �� �߰�
		btnRvEnroll = new JButton("���");
		btnRvEnroll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnAddRow();					
			}
		});
		btnRvEnroll.setBounds(778, 220, 62, 30);
		add(btnRvEnroll);
		
		//���� ��ư: ���õ� ���̺� �� �� ����
		btnRvDelete = new JButton("����");
		btnRvDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnDelRow();					
			}
		});
		btnRvDelete.setBounds(848, 220, 62, 30);
		add(btnRvDelete);
	}
	
	/** ���̺� �� �˻� �κ� UI 
	 * @throws SQLException */
	private void Table_init(){
		//Initialize Column Names
		apColNames = new Vector<>();
		//apColNames.add("ch");
		apColNames.add("����");
		apColNames.add("�װ����");
		apColNames.add("������");
		apColNames.add("�������");
		apColNames.add("���ҹ��");
		apColNames.add("�������");
		apColNames.add("����");
		
		//Create DefaultTableModel
		model = new DefaultTableModel(apColNames, 0);
		//db.Table_Initialize(CLASS_ID);
		
		//Create a Table with DefaultTableModel
		airlineTable = new JTable(model);
		
		//Table Settings
		airlineTable.addMouseListener(new JTableMouseListener());
		airlineTable.getTableHeader().setReorderingAllowed(false);		//���̺� Į�� �̵� ����
		tableCellCenter(airlineTable);
		setColumnSize(airlineTable);
		scroll = new JScrollPane(airlineTable);
		scroll.setBounds(70, 260, 850, 300);
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
		btnRvSearch = new JButton("�˻�");
		btnRvSearch.setBounds(600, 580, 62, 30);
		btnRvSearch.addActionListener(this);
		add(btnRvSearch);
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
		
		if(source == "btnRvEnroll"){
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
