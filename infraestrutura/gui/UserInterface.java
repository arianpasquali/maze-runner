package infraestrutura.gui;

import infraestrutura.robots.AbstractRobot;
import infraestrutura.robots.AbstractThreadRobot;
import infraestrutura.util.IOUtils;
import infraestrutura.util.RobotClassLoader;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import jogo.GameManager;

public class UserInterface extends JFrame implements ActionListener{

	private JRadioButton rdEasy;
	private JRadioButton rdMedium;
	private JRadioButton rdHard;
	private JMenuItem mitem_installRobot;
	private JMenuItem mitem_newRound;
	private JMenuItem mitem_exit;
	private JButton btInstallRobot;
	private JButton controlButtons[];
	private JCheckBox chbIsMute;
	public JList list;
	public DefaultListModel listModel = new DefaultListModel();
	
	private GameManager gameManager;
		
	public UserInterface(GameManager gameManager){
		
		String nativeLF = UIManager.getSystemLookAndFeelClassName();

		// Install the look and feel
		try {
			UIManager.setLookAndFeel(nativeLF);

		} catch (InstantiationException e) {
		} catch (ClassNotFoundException e) {
		} catch (UnsupportedLookAndFeelException e) {
		} catch (IllegalAccessException e) {
		}
		
		this.gameManager = gameManager;
		
		this.setSize(850,680);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
		
		this.setMinimumSize(new Dimension(850,680));
		this.setPreferredSize(new Dimension(850,680));
		
		this.setJMenuBar(createMenuBar());
		this.createUIControls();
		this.setTitle("Robot Labyrinth");
		this.setVisible(true);
	}
	
	private JMenuBar createMenuBar(){
		JMenu menu = new JMenu();
		menu.setText("File");

		JMenu menu_help = new JMenu();
		menu_help.setText("Help");

		mitem_installRobot = new JMenuItem("Install Robot");
		mitem_newRound = new JMenuItem("New Round");
		mitem_exit = new JMenuItem("Exit");

		menu.add(mitem_installRobot);
		menu.add(mitem_newRound);
		menu.add(new JSeparator());
		menu.add(mitem_exit);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.add(menu);
		menuBar.add(menu_help);
		
		mitem_installRobot.addActionListener(this);
		mitem_exit.addActionListener(this);
		
		return menuBar;
	}
		
	
	public void createUIControls(){
		JPanel pHeadControls = new JPanel();
		pHeadControls.setLayout(new BorderLayout());
		pHeadControls.setFocusable(false);

		JPanel p1 = new JPanel();
		p1.setLayout(new GridLayout(1, 6));
		p1.setFocusable(false);

		String buttonText[] = { "Run!", "Stop", "Pause", "New Round" };
		controlButtons = new JButton[buttonText.length];
		for (int i = 0; i < controlButtons.length; i++) {
			controlButtons[i] = new JButton(buttonText[i]);
			controlButtons[i].addActionListener(this);
			controlButtons[i].setFocusable(false);
			p1.add(controlButtons[i]);
		}
		pHeadControls.add(p1, BorderLayout.WEST);
		
		JPanel pFoot = new JPanel();
		pFoot.setLayout(new BorderLayout());
		pFoot.setFocusable(false);
		
		JPanel pFootRight = new JPanel();
		JPanel pFootLeft = new JPanel();
		
		//pFootRight.setLayout(new BorderLayout());
		pFootRight.setFocusable(false);

		chbIsMute = new JCheckBox("Mute");
		JCheckBox chbShowRaster = new JCheckBox("Show Grid");
		
		chbIsMute.addActionListener(this);
		
		pFootRight.add(new JLabel("Sound:"));
		pFootRight.add(new JSlider());
		pFootRight.add(chbIsMute);
		pFootRight.add(chbShowRaster );
		
		pFootLeft.add(new JLabel("Time Left:"));
		JTextField txTimeLeft = new JTextField("00:00:30");
		txTimeLeft.setFocusable(false);
		txTimeLeft.setEditable(false);
		pFootLeft.add(txTimeLeft);
		pFootLeft.add(new JLabel("Level:"));
		
		rdEasy = new JRadioButton("Easy");
		rdMedium = new JRadioButton("Medium");
		rdHard = new JRadioButton("Hard"); 
		
		pFootLeft.add(rdEasy);
		pFootLeft.add(rdMedium);
		pFootLeft.add(rdHard);
		
		rdEasy.addActionListener(this);
		rdMedium.addActionListener(this);
		rdHard.addActionListener(this);
				
		
		pFoot.add(pFootRight,BorderLayout.EAST);
		pFoot.add(pFootLeft,BorderLayout.WEST);
		
		JPanel pAvailableRobots = new JPanel();
		pAvailableRobots.setFocusable(false);
		pAvailableRobots.setLayout(new BorderLayout());
		
		listModel = new DefaultListModel();
		List availableRobots = RobotClassLoader.loadRobotPlayers();
		for (Object robot : availableRobots) {
			listModel.addElement(robot);	
		}
		
		gameManager.setRobotsInStage(new AbstractRobot[listModel.getSize()]);
		
        list = new JList(listModel);
        list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        list.setSelectedIndex(0);        
        list.setVisibleRowCount(10);
        
        JScrollPane listScrollPane = new JScrollPane(list);                	

        btInstallRobot = new JButton("Install Robot...");
		btInstallRobot.addActionListener(this);
		
		pAvailableRobots.add(btInstallRobot, BorderLayout.NORTH);
		pAvailableRobots.add(listScrollPane,BorderLayout.CENTER);
				
		this.getContentPane().add(pHeadControls, BorderLayout.NORTH);
		this.getContentPane().add(pAvailableRobots, BorderLayout.EAST);
		this.getContentPane().add(pFoot, BorderLayout.SOUTH);
		

	}

	private void showInstallRobotDialog() {
		JFileChooser fc = new JFileChooser();
		int res = fc.showOpenDialog(null);

		if (res == JFileChooser.APPROVE_OPTION) {
			File fileSelected = fc.getSelectedFile();

			System.out.println("Selected file : " + fileSelected.getName());

			URL url = Thread.currentThread().getContextClassLoader().getResource("robots");
			File extensionDir = null;
			try {
				extensionDir = new File(url.toURI());
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			try {
				new IOUtils().copyFile(fileSelected, new File(extensionDir
						.getAbsolutePath()
						+ File.separator + fileSelected.getName()));

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			listModel = new DefaultListModel();
			List robots = RobotClassLoader.loadRobotPlayers();
			for (Object robot : robots) {
				listModel.addElement(robot);
				System.out.println("Players instalados : " + robot);
			}

			gameManager.setRobotsInStage(new AbstractRobot[listModel.getSize()]);
			list.setModel(listModel);
		}
	}

	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
	
		if(e.getSource().equals(this.rdEasy)){
			this.rdMedium.setSelected(false);
			this.rdHard.setSelected(false);
			
			GameManager.getInstance().setGameLavel(GameManager.EASY);
			
		}else
			if(e.getSource().equals(this.rdMedium)){
				
				this.rdEasy.setSelected(false);
				this.rdHard.setSelected(false);

				GameManager.getInstance().setGameLavel(GameManager.MEDIUM);
				
			}else
				if(e.getSource().equals(this.rdHard)){
					
					this.rdMedium.setSelected(false);
					this.rdEasy.setSelected(false);
					
					GameManager.getInstance().setGameLavel(GameManager.HARD);
					
				}
		if(e.getSource().equals(this.chbIsMute)){
			if(!this.chbIsMute.isSelected()){
				GameManager.getInstance().playSounds();					
			}else{
				GameManager.getInstance().stopSounds();
			}
			
			
		}else
		if (e.getSource().equals(this.btInstallRobot)
				|| e.getSource().equals(this.mitem_installRobot)) {
			showInstallRobotDialog();
		} else

		if (e.getSource().equals(this.mitem_exit)) {
			System.exit(0);
		} else

		if (e.getSource().equals(this.mitem_newRound) ||
				e.getSource().equals(controlButtons[3])) {
			gameManager.buildRandomMap();
		} else
			
		if (e.getSource().equals(controlButtons[0])) {
			
			list.setEnabled(false);
			
			controlButtons[0].setEnabled(false);
			controlButtons[1].setEnabled(true);
			controlButtons[2].setEnabled(true);
			controlButtons[3].setEnabled(false);	
			
			GameManager.getInstance().resetStage();
			
			System.out.println("list.getSelectedValues().length : " + list.getSelectedValues().length);
			
			for(int i =0; i < list.getModel().getSize() ; i++){
				System.out.println("i : " + i);
				if(list.isSelectedIndex(i)){
					System.out.println("Selected Code : " + i);
					System.out.println("Selected Item : " + list.getModel().getElementAt(i));
					
					try {
						GameManager.getInstance().createRobot(i,"robots." + list.getModel().getElementAt(i));
					} catch (SecurityException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IllegalArgumentException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (ClassNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (NoSuchMethodException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (InstantiationException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IllegalAccessException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (InvocationTargetException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}	
				}
				
			}
			
			GameManager.getInstance().start();
			
			
		} else 
		
		if (e.getSource().equals(controlButtons[1])) {
			
			controlButtons[0].setEnabled(true);
			controlButtons[1].setEnabled(false);
			controlButtons[2].setEnabled(false);
			controlButtons[3].setEnabled(true);

			list.setEnabled(true);		

			if (controlButtons[2].getText() == "Resume") {
				controlButtons[2].setText("Pause");
			}
			
			GameManager.getInstance().stop();
			
		} else 
		
		if (e.getSource().equals(controlButtons[2])) {
			
			if (controlButtons[2].getText() == "Pause") {
				controlButtons[2].setText("Resume");
				GameManager.getInstance().pause();

			} else {
				controlButtons[2].setText("Pause");
				GameManager.getInstance().resume();
			}
			
			
			GameManager.getInstance().pause();
		} else 
		
		if (e.getSource().equals(controlButtons[3])) {
			GameManager.getInstance().buildRandomMap();
		}
	
	}
}
