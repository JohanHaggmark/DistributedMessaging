package launcher;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import Logging.ProjectLogger;
import replicaManager.JGroups;

public class UI extends JFrame {
	private JPanel m_replicaManagerPanel;
	private JPanel m_frontEndPanel;
	private JPanel m_consolePanel;
	
	private JButton m_startReplicaManagerButton;
	private JButton m_startFrontEndButton;

	private JTextArea m_textArea;
	private BufferedReader br;

	private static volatile UI single_instance = null;

	private String m_fileName = ProjectLogger.getDebugFileName("ReplicaManager");

	static String[] argis;

	public static void main(String[] args) {
		getInstance();
		argis = args;
	}

	public static UI getInstance() {
		if (single_instance == null) { // if there is no instance available... create new one
			synchronized (UI.class) {
				if (single_instance == null) {
					single_instance = new UI();
					JGroups.logger.debugLog("UI identifier: " + String.valueOf(single_instance.hashCode()));
				}
			}
		}

		return single_instance;
	}

	protected static UI readResolve() {
		return single_instance;
	}

	public UI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setBounds(100, 100, 450, 800);
		setLocationRelativeTo(null);
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
	}

	private void getConsoleText() {

		Thread textReadingThread = new Thread() {
			public void run() {
				while (true) {
					try {
						sleep(1000);
						br = new BufferedReader(new InputStreamReader(new FileInputStream(m_fileName)));
						m_textArea.setText("");
						m_textArea.read(br, "m_textArea");
						setTitle(m_fileName);
						br.close();
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
			}
		});

		// add buttons
		m_replicaManagerPanel.add(m_startReplicaManagerButton);
	}

	private void configureFrontEndPanel() {
		m_frontEndPanel.setBackground(Color.darkGray);
		m_frontEndPanel.setMaximumSize(new Dimension(450, 200));
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
				m_startFrontEndButton.setEnabled(false);
			}
		});
		// add buttons
		m_frontEndPanel.add(m_startFrontEndButton);
	}

	private void configureConsolePanel() {
		m_consolePanel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Console",
				TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 0, 204)));
		m_consolePanel.setBounds(10, 301, 414, 450);
		m_consolePanel.setBackground(new Color(204, 153, 102));
		getContentPane().add(m_consolePanel);
		m_consolePanel.setLayout(null);

		m_textArea = new JTextArea();
		m_textArea.setEditable(false);
		m_textArea.setBackground(Color.gray);
		m_textArea.setBounds(10, 15, 378, 73);
		m_textArea.setText("initializing...");
		
		JScrollPane scrollPane = new JScrollPane(m_textArea);
		scrollPane.setBounds(10, 15, 394, 430);
		
		scrollPane.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
	        public void adjustmentValueChanged(AdjustmentEvent e) {
	            e.getAdjustable().setValue(e.getAdjustable().getMaximum());
	        }
	    });

		m_consolePanel.add(scrollPane);
		getConsoleText();
	}

	private void startReplicaManager() {
		// KANSKE LÄGGA IN SÅ MAN MÅSTE SKRIVA IN IP OSV
		m_fileName = ProjectLogger.getDebugFileName("ReplicaManager");
		m_consolePanel.setBorder(new TitledBorder(new EmptyBorder(10, 10, 10, 10), "Replica Console"));
		
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
		m_fileName = ProjectLogger.getDebugFileName("FrontEnd");
		m_consolePanel.setBorder(new TitledBorder(new EmptyBorder(10, 10, 10, 10), "Frontend Console"));

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