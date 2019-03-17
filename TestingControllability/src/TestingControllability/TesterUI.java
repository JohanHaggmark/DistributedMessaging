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
	private JPanel m_GUIDrawPanel;
	private JPanel m_GUIRemovePanel;
	private JPanel m_GUIFrontEndPanel;
	
	private JButton m_terminateReplicaManagerButton;
	private JButton m_terminateFrontEndButton;
	private JButton m_terminateCadButton;
	private JButton m_GUIDrawButton;
	private JButton m_GUIRemoveButton;
	private JButton m_GUIFrontEndButton;

	private ProgramConnection m_replicaManagerConnection;
	private ProgramConnection m_frontEndConnection;
	private ProgramConnection m_cadConnection;
	private ProgramConnection m_GUIDrawConnection;
	private ProgramConnection m_GUIRemoveConnection;
	
	public static void main(String[] args) {
		if(args[0].equals("TesterUI")) {
			new TesterUI();			
		}		
	}
	
	public TesterUI(){
		startProgramThreads();
		
		setTitle("Tester");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setBounds(100, 100, 450, 470);
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
		getContentPane().setLayout(null);
		this.configurePanels();
		this.repaint();
	}
	
	private void startProgramThreads() {
		m_cadConnection = new ProgramConnection(26000);
		m_GUIDrawConnection = new ProgramConnection(26001);
		m_GUIRemoveConnection = new ProgramConnection(26002);
		m_frontEndConnection = new ProgramConnection(27000);
		m_replicaManagerConnection = new ProgramConnection(28000);

		new Thread(m_replicaManagerConnection).start();
		new Thread(m_frontEndConnection).start();
		new Thread(m_cadConnection).start();
		new Thread(m_GUIDrawConnection).start();
		new Thread(m_GUIRemoveConnection).start();
	}
	
	private void configurePanels() {
		m_replicaManagerPanel = new JPanel();
		m_frontEndPanel = new JPanel();
		m_cadPanel = new JPanel();
		m_GUIDrawPanel = new JPanel();
		m_GUIRemovePanel = new JPanel();
		m_GUIFrontEndPanel = new JPanel();

		configureReplicaManagerPanel();
		configureFrontEndPanel();
		configureCadPanel();
		configureGUIDrawPanel();
		configureGUIRemovePanel();
		configureGUIFrontEndPanel();
	}
	
	private void configureReplicaManagerPanel() {
		m_replicaManagerPanel.setLayout(null);
		m_replicaManagerPanel.setBackground(Color.gray);
		m_replicaManagerPanel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		m_replicaManagerPanel.setBounds(10, 11, 414, 60);
		getContentPane().add(m_replicaManagerPanel);

		m_terminateReplicaManagerButton = new JButton("Terminate Replica Manager");
		m_terminateReplicaManagerButton.setBounds(100, 10, 200, 40);
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
		m_frontEndPanel.setLayout(null);
		m_frontEndPanel.setBackground(Color.darkGray);
		m_frontEndPanel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		m_frontEndPanel.setBounds(10, 80, 414, 60);
		getContentPane().add(m_frontEndPanel);

		m_terminateFrontEndButton = new JButton("Terminate Front End");
		m_terminateFrontEndButton.setBounds(100, 10, 200, 40);
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
		m_cadPanel.setLayout(null);
		m_cadPanel.setBackground(Color.black);
		m_cadPanel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		m_cadPanel.setBounds(10, 150, 414, 60);
		getContentPane().add(m_cadPanel);

		m_terminateCadButton = new JButton("Terminate Cad");
		m_terminateCadButton.setBounds(100, 10, 200, 40);
		m_cadPanel.add(m_terminateCadButton);

		m_terminateCadButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				terminateCad();
			}
		});
		// add buttons
		m_cadPanel.add(m_terminateCadButton);
	}
	
	private void configureGUIDrawPanel() {
		m_GUIDrawPanel.setLayout(null);
		m_GUIDrawPanel.setBackground(Color.red);
		m_GUIDrawPanel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		m_GUIDrawPanel.setBounds(10, 220, 414, 60);
		getContentPane().add(m_GUIDrawPanel);

		m_GUIDrawButton = new JButton("Draw object");
		m_GUIDrawButton.setBounds(100, 10, 200, 40);
		m_GUIDrawPanel.add(m_GUIDrawButton);

		m_GUIDrawButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				drawOnGUI();
			}
		});
		// add buttons
		m_GUIDrawPanel.add(m_GUIDrawButton);
	}
	
	private void configureGUIRemovePanel() {
		m_GUIRemovePanel.setLayout(null);
		m_GUIRemovePanel.setBackground(Color.blue);
		m_GUIRemovePanel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		m_GUIRemovePanel.setBounds(10, 290, 414, 60);
		getContentPane().add(m_GUIRemovePanel);

		m_GUIRemoveButton = new JButton("Remove object");
		m_GUIRemoveButton.setBounds(100, 10, 200, 40);
		m_GUIRemovePanel.add(m_GUIRemoveButton);

		m_GUIRemoveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				removeFromGUI();
			}
		});
		// add buttons
		m_GUIRemovePanel.add(m_GUIRemoveButton);
	}
	
	private void configureGUIFrontEndPanel() {
		m_GUIFrontEndPanel.setLayout(null);
		m_GUIFrontEndPanel.setBackground(Color.GREEN);
		m_GUIFrontEndPanel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		m_GUIFrontEndPanel.setBounds(10, 360, 414, 60);
		getContentPane().add(m_GUIFrontEndPanel);

		m_GUIFrontEndButton = new JButton("Draw and shutdown FrontEnd");
		m_GUIFrontEndButton.setBounds(100, 10, 200, 40);
		m_GUIFrontEndPanel.add(m_GUIFrontEndButton);

		m_GUIFrontEndButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GUIFrontEnd();
			}
		});
		// add buttons
		m_GUIFrontEndPanel.add(m_GUIFrontEndButton);
	}
	
	private void terminateReplicaManager() {
		m_replicaManagerConnection.sendActionMessage();
	}
	
	private void terminateFrontEnd() {
		m_frontEndConnection.sendActionMessage();
	}

	private void terminateCad() {
		m_cadConnection.sendActionMessage();
	}
	
	private void drawOnGUI() {
		m_GUIDrawConnection.sendActionMessage();
	}
	
	private void removeFromGUI() {
		m_GUIRemoveConnection.sendActionMessage();
	}
	
	private void GUIFrontEnd() {
		drawOnGUI();
		terminateFrontEnd();
	}
}
