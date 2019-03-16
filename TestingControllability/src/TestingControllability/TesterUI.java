package TestingControllability;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

public class TesterUI extends JFrame {

	private JPanel m_replicaManagerPanel;
	private JPanel m_frontEndPanel;
	private JPanel m_cadPanel;
	
	private JButton m_terminateReplicaManagerButton;
	private JButton m_terminateFrontEndButton;
	private JButton m_terminateCadButton;
	
	private ProgramConnection m_cadConnection;
	private ProgramConnection m_frontEndConnection;
	private ProgramConnection m_replicaManagerConnection;
	
	public static void main(String[] args) {
		new TesterUI();
	}
	
	public TesterUI(){
		startProgramThreads();
		
		setTitle("Tester");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setBounds(100, 100, 450, 475);
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
		getContentPane().setLayout(null);
		this.configurePanels();
	}
	
	private void startProgramThreads() {
		m_cadConnection = new ProgramConnection(26000);
		m_frontEndConnection = new ProgramConnection(27000);
		m_replicaManagerConnection = new ProgramConnection(28000);

		new Thread(m_cadConnection).start();
		new Thread(m_frontEndConnection).start();
		new Thread(m_replicaManagerConnection).start();
	}
	
	private void configurePanels() {
		m_replicaManagerPanel = new JPanel();
		m_frontEndPanel = new JPanel();
		m_cadPanel = new JPanel();

		configureReplicaManagerPanel();
		configureFrontEndPanel();
		configureCadPanel();
	}
	
	private void configureReplicaManagerPanel() {
		m_replicaManagerPanel.setBackground(Color.gray);
		m_replicaManagerPanel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		m_replicaManagerPanel.setBounds(10, 11, 414, 135);
		m_replicaManagerPanel.setLayout(null);
		getContentPane().add(m_replicaManagerPanel);

		m_terminateReplicaManagerButton = new JButton("Terminate Replica Manager");
		m_terminateReplicaManagerButton.setBounds(100, 50, 200, 40);
		m_replicaManagerPanel.add(m_terminateReplicaManagerButton);

		m_terminateReplicaManagerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				terminateReplicaManager();				
			}
		});

		// add buttons
		m_replicaManagerPanel.add(m_terminateReplicaManagerButton);
	}
	
	private void configureFrontEndPanel() {
		m_frontEndPanel.setBackground(Color.darkGray);
		m_frontEndPanel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		m_frontEndPanel.setBounds(10, 157, 414, 133);
		m_frontEndPanel.setLayout(null);
		getContentPane().add(m_frontEndPanel);

		m_terminateFrontEndButton = new JButton("Terminate Front End");
		m_terminateFrontEndButton.setBounds(100, 40, 200, 40);
		m_frontEndPanel.add(m_terminateFrontEndButton);

		m_terminateFrontEndButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				terminateFrontEnd();
			}
		});
		// add buttons
		m_frontEndPanel.add(m_terminateFrontEndButton);
	}
	
	private void configureCadPanel() {
		m_cadPanel.setBackground(Color.black);
		m_cadPanel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		m_cadPanel.setBounds(10, 303, 414, 133);
		m_cadPanel.setLayout(null);
		getContentPane().add(m_cadPanel);

		m_terminateCadButton = new JButton("Terminate Cad");
		m_terminateCadButton.setBounds(100, 40, 200, 40);
		m_cadPanel.add(m_terminateCadButton);

		m_terminateCadButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				terminateCad();
			}
		});
		// add buttons
		m_cadPanel.add(m_terminateCadButton);
	}
	
	private void terminateReplicaManager() {
		m_replicaManagerConnection.sendShutdownMessage();
	}
	
	private void terminateFrontEnd() {
		m_frontEndConnection.sendShutdownMessage();
	}

	private void terminateCad() {
		m_cadConnection.sendShutdownMessage();
	}
}
