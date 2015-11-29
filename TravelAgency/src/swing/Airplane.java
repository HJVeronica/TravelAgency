package swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;

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


/** Airplane UI & Functions */
public class Airplane extends JPanel implements ActionListener{
	private final int CLASS_ID = 3;
	
	//클래스 멤버 필드 설정
	private JButton btnCsEnroll;
	private JButton btnCsDelete;
	private JButton btnCsSearch;
	
	private JLabel lblName;
	private JLabel lblPhone;
	private JLabel lblAddress;
	private JLabel lblMembership;
	private JLabel lblPayment;
	private JLabel lblSeat;
	private JLabel lblDash1;
	private JLabel lblDash2;
	private JLabel location;
	
	private Font font;
	private JScrollPane scroll;
	private JTable customerTable;
	
	private JTextField tfName;
	private JTextField tfPhone1;
	private JTextField tfPhone2;
	private JTextField tfPhone3;
	private JTextField tfAddress;
	private JTextField tfSearch;
	
	private JComboBox cbSearch;
	private JComboBox cbAirlines;
	
	private JCheckBox chBox;
	
	String alColNames[] = {"ch","ID","이름","주소","전화번호"};
	String alCombo[] = {"ID","이름","주소","전화번호"};
	
	DefaultTableModel model = null;	
	
	public Airplane(){		//Constructor
		setLayout(null);		//Delete Layout Manager
		setBackground(Color.LIGHT_GRAY);
		
		Enroll_init();
		Table_init();
		Location();
	}	
	
	/** 등록/삭제 버튼 및 등록 부분 UI */
	private void Enroll_init(){
		//Label에 사용할 폰트
		font = new Font("",Font.BOLD,15);
		
		//등록 부분 양식
		lblName = new JLabel("이        름 : ");
		lblName.setBounds(80,30,75,30);
		lblName.setFont(font);
		add(lblName);
		
		tfName = new JTextField();
		tfName.setBounds(165, 30, 200, 30);
		add(tfName);
		  
		lblPhone = new JLabel("전화번호 : ");
		lblPhone.setBounds(80,70,75,30);
		lblPhone.setFont(font);
		add(lblPhone);
		
		tfPhone1 = new JTextField();
		tfPhone1.setBounds(165, 70, 50, 30);
		add(tfPhone1);
		
		lblDash1 = new JLabel(" ─ ");
		lblDash1.setBounds(220, 70, 20, 30);
		add(lblDash1);
		
		tfPhone2 = new JTextField();
		tfPhone2.setBounds(245, 70, 60, 30);
		add(tfPhone2);
		
		lblDash2 = new JLabel(" ─ ");
		lblDash2.setBounds(310, 70, 20, 30);
		add(lblDash2);
		
		tfPhone3 = new JTextField();
		tfPhone3.setBounds(335, 70, 60, 30);
		add(tfPhone3);
		
		lblAddress = new JLabel("주        소 : ");
		lblAddress.setBounds(80,110,75,30);
		lblAddress.setFont(font);
		add(lblAddress);
		
		tfAddress = new JTextField();
		tfAddress.setBounds(165, 110, 350, 30);
		add(tfAddress);			
		
		//ComboBox: Airline Names (Statistics)			
		cbAirlines = new JComboBox();
		cbAirlines.addItem("Names");
		cbAirlines.addActionListener(this);
		cbAirlines.setBounds(65, 160, 100, 30);
		add(cbAirlines);
		
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
	
	/** 테이블 및 검색 부분 UI */
	private void Table_init(){
		//DefaultTableModel
		model = new DefaultTableModel(alColNames, 0);
		customerTable = new JTable(model);
		
		//Insert Sample Data
		model.addRow(new Object[]{"","A01","Delta","주소1","+1-12-3456-7890"});
		model.addRow(new Object[]{"","A02","Cathay Pacific","주소2","+1-12-3456-7890"});
		model.addRow(new Object[]{"","A03","Air Canada","주소3","+1-12-3456-7890"});
		model.addRow(new Object[]{"","A04","Korean Air","주소4","+82-10-3456-7890"});
		model.addRow(new Object[]{"","A05","EastJet","주소5","+1-12-3456-7890"});
		
		//Table Settings
		customerTable.addMouseListener(new JTableMouseListener());
		customerTable.getTableHeader().setReorderingAllowed(false);		//테이블 칼럼 이동 방지 (드래그 앤 드롭)
		tableCellCenter(customerTable);
		setColumnSize(customerTable);
		scroll = new JScrollPane(customerTable);
		scroll.setBounds(60, 200, 850, 360);
		add(scroll);
		
		//CheckBox for the Table
		chBox = new JCheckBox();
		chBox.setHorizontalAlignment(JLabel.CENTER);
		customerTable.getColumn("ch").setCellEditor(new DefaultCellEditor(chBox));
		add(chBox);
		
		//ComboBox: Search			
		cbSearch = new JComboBox(alCombo);
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
	
	
	DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer(){
		public Component getTableCellRendererComponent
		(JTable t, Object value, boolean isSelected, boolean hasFocus, int row, int column){
			chBox.setSelected(((Boolean)value).booleanValue());
			//chBox.set
			
			return chBox;				
		}
	};
	
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
		
		tcm.getColumn(0).setPreferredWidth(20);
		tcm.getColumn(1).setPreferredWidth(30);
		tcm.getColumn(2).setPreferredWidth(100);
		tcm.getColumn(3).setPreferredWidth(400);
		
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