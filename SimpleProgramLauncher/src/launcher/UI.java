package launcher;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.BevelBorder;

import Logging.ProjectLogger;

public class UI extends JFrame {
	
	private JPanel m_replicaManagerPanel;
	private JPanel m_frontEndPanel;
	private JPanel m_consolePanel;

	private JButton m_startReplicaManagerButton;
	private JButton m_startFrontEndButton;

	private JTextArea m_textArea;
	private BufferedReader br;

	private String m_rmFileName = ProjectLogger.getDebugFileName("ReplicaManager");
	private String m_feFileName = ProjectLogger.getDebugFileName("FrontEnd");

	private JRadioButtonMenuItem rmConsoleButton;
	private JRadioButtonMenuItem fmConsoleButton;

	static String[] argis;
	private JScrollPane m_scrollPane;
	private JTextArea m_newRMText = new JTextArea();
	private JTextArea m_newFEText = new JTextArea();
	private boolean m_logTextChanged = false;

	public static void main(String[] args) {
		UI ui = new UI();
		argis = args;
	}

	public UI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setBounds(100, 100, 450, 800);
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
		getContentPane().setLayout(null);
		this.configurePanels();
	}

	private void configurePanels() {
		m_replicaManagerPanel = new JPanel();
		m_frontEndPanel = new JPanel();
		m_consolePanel = new JPanel();

		configureReplicaManagerPanel();
		configureFrontEndPanel();
		configureConsolePanel();
		configureConsoleMenu();
		m_consolePanel.updateUI();
	}

	private void getRMConsoleText() {
		Thread textReadingThread = new Thread() {
			public void run() {
				while (true) {
					try {
						sleep(1000);
						if (rmConsoleSelected()) {
							br = new BufferedReader(new InputStreamReader(new FileInputStream(m_rmFileName)));
							m_newRMText.read(br, "newText");
							if (!m_newRMText.getText().equals(m_textArea.getText())) {
								m_textArea.setText(m_newRMText.getText());
								m_logTextChanged = true;
							}
							setTitle(m_rmFileName);
							br.close();
						}
					} catch (InterruptedException | IOException e) {
						e.printStackTrace();
					}
				}
			}
		};
		textReadingThread.start();
	}

	private void getFEConsoleText() {
		m_rmFileName = ProjectLogger.getDebugFileName("ReplicaManager");
		Thread textReadingThread = new Thread() {
			public void run() {
				while (true) {
					try {
						sleep(1000);
						if (feConsoleSelected()) {
							br = new BufferedReader(new InputStreamReader(new FileInputStream(m_feFileName)));
							m_newFEText.read(br, "newText");
							if (!m_newFEText.getText().equals(m_textArea.getText())) {
								m_textArea.setText(m_newFEText.getText());
								m_logTextChanged = true;
							}
							setTitle(m_rmFileName);
							br.close();
						}
					} catch (InterruptedException | IOException e) {
						e.printStackTrace();
					}
				}
			}
		};
		textReadingThread.start();
	}

	private void configureReplicaManagerPanel() {
		m_replicaManagerPanel.setBackground(Color.lightGray);
		m_replicaManagerPanel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		m_replicaManagerPanel.setBounds(10, 11, 414, 135);
		m_replicaManagerPanel.setLayout(null);
		getContentPane().add(m_replicaManagerPanel);

		m_startReplicaManagerButton = new JButton("Start Replica Manager");
		m_startReplicaManagerButton.setBounds(125, 50, 160, 40);
		m_replicaManagerPanel.add(m_startReplicaManagerButton);

		m_startReplicaManagerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				startReplicaManager();
				rmConsoleButton.setSelected(true);
				
				m_startReplicaManagerButton.setEnabled(false);
				m_startFrontEndButton.setEnabled(false);
			}
		});

		// add buttons
		m_replicaManagerPanel.add(m_startReplicaManagerButton);
	}

	private void configureFrontEndPanel() {
		m_frontEndPanel.setBackground(Color.darkGray);
		m_frontEndPanel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		m_frontEndPanel.setBounds(10, 157, 414, 133);
		m_frontEndPanel.setLayout(null);
		getContentPane().add(m_frontEndPanel);

		m_startFrontEndButton = new JButton("Start Front End");
		m_startFrontEndButton.setBounds(125, 40, 160, 40);
		m_frontEndPanel.add(m_startFrontEndButton);

		m_startFrontEndButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				startFrontEnd();
				fmConsoleButton.setSelected(true);
				
				m_startReplicaManagerButton.setEnabled(false);
				m_startFrontEndButton.setEnabled(false);
			}
		});
		// add buttons
		m_frontEndPanel.add(m_startFrontEndButton);
	}

	private void configureConsolePanel() {
		m_consolePanel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		m_consolePanel.setBackground(new Color(204, 153, 102));
		m_consolePanel.setLayout(null);
		m_consolePanel.setBounds(10, 301, 414, 450);

		getContentPane().add(m_consolePanel);
		m_textArea = new JTextArea();
		m_textArea.setEditable(false);
		m_textArea.setBackground(Color.gray);
		m_textArea.setBounds(10, 24, 394, 421);
		m_textArea.setText("Choose a console...");

		m_scrollPane = new JScrollPane(m_textArea);
		m_scrollPane.setBounds(10, 24, 394, 421);

		m_scrollPane.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
			public void adjustmentValueChanged(AdjustmentEvent e) {
				if (m_logTextChanged) {
					e.getAdjustable().setValue(e.getAdjustable().getMaximum());
					m_logTextChanged = false;
				}
			}
		});

		m_consolePanel.add(m_scrollPane);
		getRMConsoleText();
		getFEConsoleText();
	}

	private void configureConsoleMenu() {
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 414, 21);
		m_consolePanel.add(menuBar);

		JMenu switchConsoleMenu = new JMenu("Choose console");
		switchConsoleMenu.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));

		rmConsoleButton = new JRadioButtonMenuItem("RM console");
		fmConsoleButton = new JRadioButtonMenuItem("FE console");

		rmConsoleButton.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				rmConsoleSelected();
			}
		});

		fmConsoleButton.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				feConsoleSelected();
			}
		});
		switchConsoleMenu.add(rmConsoleButton);
		switchConsoleMenu.add(fmConsoleButton);
		menuBar.add(switchConsoleMenu);
	}

	private boolean rmConsoleSelected() {
		if (rmConsoleButton.isSelected()) {
			fmConsoleButton.setSelected(false);
			m_rmFileName = ProjectLogger.getDebugFileName("ReplicaManager");
			return true;
		}
		return false;
	}

	private boolean feConsoleSelected() {
		if (fmConsoleButton.isSelected()) {
			rmConsoleButton.setSelected(false);
			m_feFileName = ProjectLogger.getDebugFileName("FrontEnd");
			return true;
		}
		return false;
	}

	private void startReplicaManager() {
		// KANSKE LÄGGA IN SÅ MAN MÅSTE SKRIVA IN IP OSV
		m_consolePanel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		Thread rmThread = new Thread() {
			@Override
			public void run() {
//				System.out.println("Start Replica Manager");
//				String[] arg = new String[1];
//				arg[0] = "25000";
//				ProcessMonitor.startReplicaManager(arg);
				
				
				
				
				try {
					File correctFile1;
					correctFile1 = File.createTempFile("correctFile", "json");
					String correctFile1Text = 
							"[\n" 		
									+ "\t{\n"
//									+ "\t\t\"argument\": [\"C:/windows/notepad.exe\"],\n"
//									+ "\t\t\"argument\": [\"C:/java/exe/Cad.exe\"],\n"
//									+ "\t\t\"argument\": [\"C:/java/exe/UI.exe\"],\n"
//									+ "\t\t\"argument\": [\"C:/java/exe/ReplicaManager.exe\"],\n"
									+ "\t\t\"argument\": [\"C:/java/exe/ReplicaManagerShutdownOnDraw.exe\"],\n"
									+ "\t\t\"relaunch\": true,\n"
									+ "\t\t\"noOfRetries\": 10,\n"
									+ "\t\t\"deadReckoningOfStartedTimeInMilliSeconds\": 1000"
									+"\t}\n"
									+"]\n";
					PrintWriter out1 = new PrintWriter(correctFile1);
					out1.print(correctFile1Text);
					out1.close();
					System.out.print(correctFile1Text);
					
					
					ProcessMonitor[] monitoredProcessArray = ProcessMonitor.createMonitor(correctFile1);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
				
				
				
				
				
//				ProcessMonitor monitor;
////				String[] arg = {"C:/Windows/notepad.exe"};
//				String[] arg = {"C:/java/exe/launch4j.exe"};
//				try {
//					monitor = ProcessMonitor.createMonitor(arg);
//				} catch (Exception e2) {
//					System.err.println("Something could not be started");
//					e2.printStackTrace();
//					System.exit(1);
//				}
			}
		};
		rmThread.start();
	}

	private void startFrontEnd() {
		// KANSKE LÄGGA IN SÅ MAN MÅSTE SKRIVA IN IP OSV
		m_consolePanel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		Thread feThread = new Thread() {
			@Override
			public void run() {
//				System.out.println("Start Front End");
//				String[] arg = new String[1];
//				arg[0] = "25000";
//				ProcessMonitor.startFrontEnd(arg);
				
				
				
				

				try {
					File correctFile1;
					correctFile1 = File.createTempFile("correctFile", "json");
					String correctFile1Text = 
							"[\n" 		
									+ "\t{\n"
//									+ "\t\t\"argument\": [\"C:/windows/notepad.exe\"],\n"
//									+ "\t\t\"argument\": [\"C:/java/exe/Cad.exe\"],\n"
//									+ "\t\t\"argument\": [\"C:/java/exe/UI.exe\"],\n"
									+ "\t\t\"argument\": [\"C:/java/exe/FrontEnd.exe\"],\n"
									+ "\t\t\"relaunch\": true,\n"
									+ "\t\t\"noOfRetries\": 10,\n"
									+ "\t\t\"deadReckoningOfStartedTimeInMilliSeconds\": 1000"
									+"\t}\n"
									+"]\n";
					PrintWriter out1 = new PrintWriter(correctFile1);
					out1.print(correctFile1Text);
					out1.close();
					System.out.print(correctFile1Text);
					
					
					ProcessMonitor[] monitoredProcessArray = ProcessMonitor.createMonitor(correctFile1);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
				
				
			}
		};
		feThread.start();
	}
}