package launcher;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import Logging.ProjectLogger;

public class UI extends JFrame {
	protected GridBagConstraints m_gbc;
	private JPanel m_replicaManagerPanel;
	private JPanel m_frontEndPanel;
	private JButton m_startReplicaManagerButton;
	private JButton m_startFrontEndButton;

	private JPanel m_console;
	private JTextArea m_textArea;
	private String m_fileName = "C:\\Java\\logs\\ReplicaManager.txt";
	private BufferedReader br;
	
	public static ProjectLogger replicaLogger = new ProjectLogger("ReplicaManager");
	public static ProjectLogger frontEndLogger = new ProjectLogger("FrontEnd");

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
		m_gbc.gridy = 2;
		m_gbc.weighty = 0.5;
		this.add(m_console, m_gbc);
	}

	private void configurePanels() {
		m_replicaManagerPanel = new JPanel();
		m_frontEndPanel = new JPanel();
		m_console = new JPanel();

		configureReplicaManagerPanel();
		configureFrontEndPanel();
		configureConsolePanel();
	}

	private void getConsoleText() {
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(m_fileName)));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		Thread textReadingThread = new Thread() {
			public void run() {
				try {
					sleep(5000);
					m_textArea.read(br, "m_textArea");
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		textReadingThread.start();
	}
	
	private void configureConsolePanel() {
		m_console.setMaximumSize(new Dimension(450, 50));
		m_console.setBackground(Color.gray);
		m_console.setBorder(new TitledBorder(new EmptyBorder(10, 10, 10, 10), "Console"));
		m_textArea = new JTextArea();
		m_textArea.setSize(m_console.getWidth() - 10, m_console.getHeight() - 10);
		m_textArea.setEditable(false);
		m_textArea.setWrapStyleWord(true);
		m_textArea.setBackground(Color.gray);
		m_textArea.setText("testing");
		m_console.add(m_textArea);
		getConsoleText();
	}

	private void configureReplicaManagerPanel() {
		m_replicaManagerPanel.setBackground(Color.lightGray);
		m_startReplicaManagerButton = new JButton("Start Replica Manager");
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
		m_startFrontEndButton = new JButton("Start Front End");
		
		m_startFrontEndButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				startFrontEnd();
				m_startFrontEndButton.setEnabled(false);
			}
		});
		// add buttons
		m_frontEndPanel.add(m_startFrontEndButton);
	}

	private void startReplicaManager() {
		// KANSKE LÄGGA IN SÅ MAN MÅSTE SKRIVA IN IP OSV
		m_fileName = replicaLogger.getDebugFileName();
		m_console.setBorder(new TitledBorder(new EmptyBorder(10, 10, 10, 10), "Replica Console"));
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
		m_fileName = frontEndLogger.getDebugFileName();
		m_console.setBorder(new TitledBorder(new EmptyBorder(10, 10, 10, 10), "Frontend Console"));

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