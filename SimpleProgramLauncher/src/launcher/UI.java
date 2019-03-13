package launcher;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class UI extends JFrame {
	private GridBagConstraints m_gbc;
	private JPanel m_replicaManagerPanel;
	private JPanel m_frontEndPanel;
	private JButton m_startReplicaManagerButton;
	private JButton m_startFrontEndButton;	
	
	static String[] argis;
	
	public static void main(String[] args) {
		new UI();
		argis = args;
	}

	public UI() {
		this.createGridBag();
		this.configurePanels();
		this.addPanels();
		
		pack();
		setSize(450, 450);
		setLocationRelativeTo(null);
		setMinimumSize(new Dimension(450, 450));
		setVisible(true);
	}
	
	private void createGridBag() {
		this.setLayout(new GridBagLayout());
		m_gbc = new GridBagConstraints();

		m_gbc.fill = GridBagConstraints.BOTH;
		
		m_gbc.weightx = 1;
	}
	
	
	private void addPanels() {		
		m_gbc.gridx = 0;
		
		m_gbc.gridy = 0;
		m_gbc.weighty = 1;
		this.add(m_replicaManagerPanel, m_gbc);
		
		m_gbc.gridy = 1;
		m_gbc.weighty = 1;
		this.add(m_frontEndPanel, m_gbc);
	}
	
	
	private void configurePanels() {
		m_replicaManagerPanel = new JPanel();
		m_frontEndPanel = new JPanel();
		
		configureReplicaManagerPanel();
		configureFrontEndPanel();
	}
	
	
	private void configureReplicaManagerPanel() {	
		m_replicaManagerPanel.setBackground(Color.lightGray);

		m_startReplicaManagerButton = new JButton("Start Replica Manager");
		
		m_startReplicaManagerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				startReplicaManager();
			}
		});
		
		//add buttons
		m_replicaManagerPanel.add(m_startReplicaManagerButton);
	}
	
	
	private void configureFrontEndPanel() {
		m_frontEndPanel.setBackground(Color.darkGray);

		m_startFrontEndButton = new JButton("Start Front End");
		
		m_startFrontEndButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				startFrontEnd();	
			}
		});
		
		//add buttons
		m_frontEndPanel.add(m_startFrontEndButton);
	}
	
	private void startReplicaManager() {
		// KANSKE LÄGGA IN SÅ MAN MÅSTE SKRIVA IN IP OSV
		
		Thread rmThread = new Thread() {					
			@Override
			public void run() {
				System.out.println("Start Replica Manager");
				String[] arg = new String[1];
				arg[0] = "25000";
				ProcessMonitor.startReplicaManager(arg);						
			}
		};				
		rmThread.start();
	}
	
	private void startFrontEnd() {
		// KANSKE LÄGGA IN SÅ MAN MÅSTE SKRIVA IN IP OSV
		
		Thread feThread = new Thread() {					
			@Override
			public void run() {
				System.out.println("Start Front End");
				String[] arg = new String[1];
				arg[0] = "25000";
				ProcessMonitor.startFrontEnd(arg);						
			}
		};				
		feThread.start();	
	}
}
