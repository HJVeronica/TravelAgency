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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

/** 
 * Create UI Using Swing
 * 
 * 
 * @version 1.03 11/29/15
 * @author Hyunjeong Shim, ����, ������
 */

public class Swing extends JFrame{
	public Airline airline = null;
	public Customer customer = null;
	public Airplane airplane = null;
	public Flight flight = null;
	public Reservation reservation = null;
	
	/**  
	 * Constructor of Swing Class
	 * Set the Size of JFrame
	 * Manage the Tabs 
	 * @throws SQLException 
	 * */
	public Swing() throws SQLException{
		setTitle("Travel Agency Management Program");
		setSize(1000,700);
		setLocation(200,50);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	//Frame ������ Program�� ����

		airline = new Airline();
		customer = new Customer();
		airplane = new Airplane();
		flight = new Flight();
		reservation = new Reservation();
		
		JTabbedPane tab = new JTabbedPane();
		
		tab.addTab("�װ��� ����", airline);
		tab.addTab("�� ����", customer);
		tab.addTab("����� ����", airplane);
		tab.addTab("�װ��� ��ȸ", flight);
		tab.addTab("����", reservation);
		
		add(tab);
		setVisible(true);
	}	
}

