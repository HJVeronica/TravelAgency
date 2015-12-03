package swing;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

/** 
 * <p><b> Swing Class </b> 
 * : Create UI Using Swing</p>
 * 
 * <p><b>Each method is as follows</b> <br>
 * - {@link #Swing()} : Constructor  <br></p>
 * 
 * @version 1.0 11/29/15
 * @author ������, ����, ������
 * */
@SuppressWarnings("serial")
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
	 * */
	public Swing(){
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

