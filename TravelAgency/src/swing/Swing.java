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
 * @author 심현정, 김상완, 유란영
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
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	//Frame 닫히면 Program도 종료

		airline = new Airline();
		customer = new Customer();
		airplane = new Airplane();
		flight = new Flight();
		reservation = new Reservation();
		
		JTabbedPane tab = new JTabbedPane();
		
		tab.addTab("항공사 관리", airline);
		tab.addTab("고객 관리", customer);
		tab.addTab("비행기 관리", airplane);
		tab.addTab("항공편 조회", flight);
		tab.addTab("예약", reservation);
		
		add(tab);
		setVisible(true);
	}	
}

