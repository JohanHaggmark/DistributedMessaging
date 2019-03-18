package TesterUI;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import TestingControllability.ProgramConnection;

public class TesterUI extends JFrame {

	private JPanel m_replicaManagerPanel1;
	private JPanel m_replicaManagerPanel2;
	private JPanel m_replicaManagerPanel3;
	private JPanel m_frontEndPanel;
	private JPanel m_cadPanel;
	private JPanel m_GUIDrawPanel;
	private JPanel m_GUIRemovePanel;
	private JPanel m_GUIFrontEndPanel;
	
	private JButton m_terminateReplicaManagerButton1;
	private JButton m_terminateReplicaManagerButton2;
	private JButton m_terminateReplicaManagerButton3;
	private JButton m_terminateFrontEndButton;
	private JButton m_terminateCadButton;
	private JButton m_GUIDrawButton;
	private JButton m_GUIRemoveButton;
	private JButton m_GUIFrontEndButton;

	private ProgramConnection m_replicaManagerConnection1;
	private ProgramConnection m_replicaManagerConnection2;
	private ProgramConnection m_replicaManagerConnection3;
	private ProgramConnection m_frontEndConnection;
	private ProgramConnection m_cadConnection;
	private ProgramConnection m_GUIDrawConnection;
	private ProgramConnection m_GUIRemoveConnection;
	
	public static void main(String[] args) {
		if(args[0].equals("TesterUI")) {
			new TesterUI(args[0]);			
		}		
	}
	
	public TesterUI(String arg){
		if(arg.equals("TesterUI")) {
			startProgramThreads();
			
			setTitle("Tester");
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			pack();
			setBounds(100, 100, 450, 600);
			setLocationRelativeTo(null);
			setResizable(false);
			setVisible(true);
			getContentPane().setLayout(null);
			this.configurePanels();
			this.repaint();			
		}
	}
	
	private void startProgramThreads() {
		m_cadConnection = new ProgramConnection(26000);
		m_GUIDrawConnection = new ProgramConnection(26001);
		m_GUIRemoveConnection = new ProgramConnection(26002);
		m_frontEndConnection = new ProgramConnection(27000);
		m_replicaManagerConnection1 = new ProgramConnection(28001);
		m_replicaManagerConnection1 = new ProgramConnection(28002);
		m_replicaManagerConnection1 = new ProgramConnection(28003);

		new Thread(m_replicaManagerConnection1).start();
		new Thread(m_replicaManagerConnection2).start();
		new Thread(m_replicaManagerConnection3).start();
		new Thread(m_frontEndConnection).start();
		new Thread(m_cadConnection).start();
		new Thread(m_GUIDrawConnection).start();
		new Thread(m_GUIRemoveConnection).start();
	}
	
	private void configurePanels() {
		m_replicaManagerPanel1 = new JPanel();
		m_replicaManagerPanel2 = new JPanel();
		m_replicaManagerPanel3 = new JPanel();
		m_frontEndPanel = new JPanel();
		m_cadPanel = new JPanel();
		m_GUIDrawPanel = new JPanel();
		m_GUIRemovePanel = new JPanel();
		m_GUIFrontEndPanel = new JPanel();

		configureReplicaManagerPanel1();
		configureReplicaManagerPanel2();
		configureReplicaManagerPanel3();
		configureFrontEndPanel();
		configureCadPanel();
		configureGUIDrawPanel();
		configureGUIRemovePanel();
		configureGUIFrontEndPanel();
	}
	
	private void configureReplicaManagerPanel1() {
		m_replicaManagerPanel1.setLayout(null);
		m_replicaManagerPanel1.setBackground(Color.white);
		m_replicaManagerPanel1.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		m_replicaManagerPanel1.setBounds(10, 10, 414, 60);
		getContentPane().add(m_replicaManagerPanel1);

		m_terminateReplicaManagerButton1 = new JButton("Terminate Replica Manager 1");
		m_terminateReplicaManagerButton1.setBounds(100, 10, 200, 40);
		m_replicaManagerPanel1.add(m_terminateReplicaManagerButton1);

		m_terminateReplicaManagerButton1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				terminateReplicaManager1();				
			}
		});

		// add buttons
		m_replicaManagerPanel1.add(m_terminateReplicaManagerButton1);
	}
	
	private void configureReplicaManagerPanel2() {
		m_replicaManagerPanel2.setLayout(null);
		m_replicaManagerPanel2.setBackground(Color.lightGray);
		m_replicaManagerPanel2.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		m_replicaManagerPanel2.setBounds(10, 80, 414, 60);
		getContentPane().add(m_replicaManagerPanel2);

		m_terminateReplicaManagerButton2 = new JButton("Terminate Replica Manager 2");
		m_terminateReplicaManagerButton2.setBounds(100, 10, 200, 40);
		m_replicaManagerPanel2.add(m_terminateReplicaManagerButton2);

		m_terminateReplicaManagerButton2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				terminateReplicaManager2();				
			}
		});

		// add buttons
		m_replicaManagerPanel2.add(m_terminateReplicaManagerButton2);
	}
	
	private void configureReplicaManagerPanel3() {
		m_replicaManagerPanel3.setLayout(null);
		m_replicaManagerPanel3.setBackground(Color.gray);
		m_replicaManagerPanel3.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		m_replicaManagerPanel3.setBounds(10, 150, 414, 60);
		getContentPane().add(m_replicaManagerPanel3);

		m_terminateReplicaManagerButton3 = new JButton("Terminate Replica Manager 3");
		m_terminateReplicaManagerButton3.setBounds(100, 10, 200, 40);
		m_replicaManagerPanel3.add(m_terminateReplicaManagerButton3);

		m_terminateReplicaManagerButton3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				terminateReplicaManager3();				
			}
		});

		// add buttons
		m_replicaManagerPanel3.add(m_terminateReplicaManagerButton3);
	}
	
	private void configureFrontEndPanel() {
		m_frontEndPanel.setLayout(null);
		m_frontEndPanel.setBackground(Color.darkGray);
		m_frontEndPanel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		m_frontEndPanel.setBounds(10, 220, 414, 60);
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
		m_cadPanel.setBounds(10, 290, 414, 60);
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
		m_GUIDrawPanel.setBounds(10, 360, 414, 60);
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
		m_GUIRemovePanel.setBounds(10, 430, 414, 60);
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
		m_GUIFrontEndPanel.setBounds(10, 500, 414, 60);
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
	
	private void terminateReplicaManager1() {
		m_replicaManagerConnection1.sendActionMessage();
	}
	
	private void terminateReplicaManager2() {
		m_replicaManagerConnection2.sendActionMessage();
	}
	
	private void terminateReplicaManager3() {
		m_replicaManagerConnection3.sendActionMessage();
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
		terminateFrontEnd();
		drawOnGUI();
	}
}
