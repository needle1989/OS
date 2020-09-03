package sse.os.imperialElevator;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.border.MatteBorder;
import javax.swing.plaf.basic.BasicArrowButton;

/*
 * 
 */

/**
 * 
 */
public class ElevatorFrame extends JFrame implements Runnable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static int floorNum = 20; //初始化楼层数（可更改）
	private static int eleNum = 5; //初始化电梯数（可更改）
	private ElevatorThread[] eleThread; //代表电梯的线程数组

	Container cp;
	//显示每楼层的楼层号和向上/下运行的面板
	JPanel floorPanel = new JPanel(); 
	
    //楼层号及呼叫电梯向上/向下功能
	JButton[] floorButton; 
	BasicArrowButton[] upButton; 
	BasicArrowButton[] downButton; 

	JButton dispUp, dispDown, dispFloor;

	Color pressedUpDownColor = Color.GREEN;
	Color unPressedUpDownColor = new Color(28, 128, 128);
    //用来记录电梯向上/向下的状态
	int[] upState; 
	int[] downState; 
    //初始化菜单栏
	private JMenuBar menuBar;
	private JMenu menu;
	private JMenu menuSet;
	private JMenu menuSet1;
	private JMenu alarm;
	//file菜单栏，用于退出程序
	private JMenuItem chooses[] = {
		new JMenuItem("exit")};
	//setFloor菜单栏，用于更改楼层数
	private JMenuItem chooses2[] = {
			new JMenuItem("20"), 
			new JMenuItem("21"),
			new JMenuItem("22"),
			new JMenuItem("23"), 
			new JMenuItem("24"),
			new JMenuItem("25"),};
	//setElevator菜单栏，用于更改电梯数量
	private JMenuItem chooses3[] = {
			new JMenuItem("3"), 
			new JMenuItem("4"),
			new JMenuItem("5"), 
			new JMenuItem("6"),};
	//elevatorAlarm菜单栏，用于为每台电梯提供报警功能
	private JMenuItem chooses4[] = {
			new JMenuItem("1"), 
			new JMenuItem("2"),
			new JMenuItem("3"),
			new JMenuItem("4"),
			new JMenuItem("5"),
			new JMenuItem("6"),};
	//生成电梯系统主界面
	public ElevatorFrame()
	{
		cp = this.getContentPane();
		cp.setLayout(new GridLayout(1, eleNum + 1));

		floorPanel.setLayout(new GridLayout(floorNum + 1, 3));
		floorPanel.setBorder(new MatteBorder(2, 4, 2, 2, Color.black));
		floorButton = new JButton[floorNum];
		upButton = new BasicArrowButton[floorNum];
		downButton = new BasicArrowButton[floorNum];

		dispFloor = new JButton("F");
		//dispFloor.setEnabled(false);
		dispUp = new JButton("UP");
		//dispUp.setForeground(new Color(217, 123, 2));
		//dispUp.setEnabled(false);
		dispDown = new JButton("D");
		//dispDown.setEnabled(false);
		floorPanel.add(dispFloor);
		floorPanel.add(dispUp);
		floorPanel.add(dispDown);

		MouseListener upListener = new UpButtonAction(); //向上键的Listener

		//设置属性
		for (int i = floorButton.length - 1; i >= 0; i--)
		{
			floorButton[i] = new JButton(String.valueOf(i + 1));
			//floorButton[i].setForeground(Color.green);
			
			floorButton[i].setFont(new Font("Serif", Font.BOLD, 13));
			//floorButton[i].setEnabled(false);
			upButton[i] = new BasicArrowButton(BasicArrowButton.NORTH);
			upButton[i].addMouseListener(upListener);
			upButton[i].setBackground(unPressedUpDownColor);
			downButton[i] = new BasicArrowButton(BasicArrowButton.SOUTH);
			downButton[i].addMouseListener(upListener);
			downButton[i].setBackground(unPressedUpDownColor);
			floorPanel.add(floorButton[i]);
			floorPanel.add(upButton[i]);
			floorPanel.add(downButton[i]);
		}

		cp.add(floorPanel);
		// 设置菜单	
		menuBar = new JMenuBar();
		menu = new JMenu("File");
		menu.setFont(new Font("Serif", Font.BOLD, 14));
		menu.setForeground(new Color(128, 128, 128));
		menu.setMnemonic(KeyEvent.VK_M);
		
		menuSet = new JMenu("SetFloor");
		menuSet.setFont(new Font("Serif", Font.BOLD, 14));
		menuSet.setForeground(new Color(128, 128, 128));
		
		menuSet1 = new JMenu("SetElevator");
		menuSet1.setFont(new Font("Serif", Font.BOLD, 14));
		menuSet1.setForeground(new Color(128, 128, 128));
		
		alarm = new JMenu("ElevatorAlarm");
		alarm.setFont(new Font("Serif", Font.BOLD, 14));
		alarm.setForeground(new Color(128, 128, 128));
		
		ImageIcon imageIcon1=new ImageIcon("./ow.png");

		for (int i = 0; i < chooses.length; i++)
		{
			menu.add(chooses[i]);
			if (i < chooses.length - 1)
			{
				menu.addSeparator();
			}
			chooses[i].setForeground(Color.darkGray);
			chooses[i].setFont(new Font("Serif", Font.BOLD, 14));
		}

		chooses[0].addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				
				System.exit(0);
			}
		});
		
		
		for (int i = 0; i < chooses2.length; i++)
		{
			menuSet.add(chooses2[i]);
			if (i < chooses2.length - 1)
			{
				menuSet.addSeparator();
			}
			chooses2[i].setForeground(Color.darkGray);
			chooses2[i].setFont(new Font("Serif", Font.BOLD, 14));
		}

		chooses2[0].addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				floorNum = 20;
				JFrame frame1 = new ElevatorFrame();
				frame1.setTitle("Imperial Elevator");
				frame1.setLocation(250, 120);
				frame1.setSize(970, 560);
				frame1.setResizable(false);
				frame1.setVisible(true);
				frame1.setIconImage(imageIcon1.getImage());
				
				frame1.addWindowListener(new WindowAdapter() {
					public void windowClosing(WindowEvent e) {
						System.exit(0);
					}
				});
				
			}
		});
		chooses2[1].addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				floorNum = 21;
				JFrame frame1 = new ElevatorFrame();
				frame1.setTitle("Imperial Elevator");
				frame1.setLocation(250, 120);
				frame1.setSize(970, 560);
				frame1.setResizable(false);
				frame1.setVisible(true);
				frame1.setIconImage(imageIcon1.getImage());
				
				frame1.addWindowListener(new WindowAdapter() {
					public void windowClosing(WindowEvent e) {
						System.exit(0);
					}
				});
				
			}
		});
		chooses2[2].addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				floorNum = 22;
				JFrame frame1 = new ElevatorFrame();
				frame1.setTitle("Imperial Elevator");
				frame1.setLocation(250, 120);
				frame1.setSize(970, 560);
				frame1.setResizable(false);
				frame1.setVisible(true);
				frame1.setIconImage(imageIcon1.getImage());
				
				frame1.addWindowListener(new WindowAdapter() {
					public void windowClosing(WindowEvent e) {
						System.exit(0);
					}
				});
				
			}
		});
		chooses2[3].addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				floorNum = 23;
				JFrame frame1 = new ElevatorFrame();
				frame1.setTitle("Imperial Elevator");
				frame1.setLocation(250, 120);
				frame1.setSize(970, 560);
				frame1.setResizable(false);
				frame1.setVisible(true);
				frame1.setIconImage(imageIcon1.getImage());
				
				frame1.addWindowListener(new WindowAdapter() {
					public void windowClosing(WindowEvent e) {
						System.exit(0);
					}
				});
				
			}
		});
		chooses2[4].addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				floorNum = 24;
				JFrame frame1 = new ElevatorFrame();
				frame1.setTitle("Imperial Elevator");
				frame1.setLocation(250, 120);
				frame1.setSize(970, 560);
				frame1.setResizable(false);
				frame1.setVisible(true);
				frame1.setIconImage(imageIcon1.getImage());
				
				frame1.addWindowListener(new WindowAdapter() {
					public void windowClosing(WindowEvent e) {
						System.exit(0);
					}
				});
				
			}
		});
		chooses2[5].addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				floorNum = 25;
				JFrame frame1 = new ElevatorFrame();
				frame1.setTitle("Imperial Elevator");
				frame1.setLocation(250, 120);
				frame1.setSize(970, 560);
				frame1.setResizable(false);
				frame1.setVisible(true);
				frame1.setIconImage(imageIcon1.getImage());
				
				frame1.addWindowListener(new WindowAdapter() {
					public void windowClosing(WindowEvent e) {
						System.exit(0);
					}
				});
				
			}
		});
		
		for (int i = 0; i < chooses3.length; i++)
		{
			menuSet1.add(chooses3[i]);
			if (i < chooses3.length - 1)
			{
				menuSet1.addSeparator();
			}
			chooses3[i].setForeground(Color.darkGray);
			chooses3[i].setFont(new Font("Serif", Font.BOLD, 14));
		}
		chooses3[0].addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				eleNum = 3;
				JFrame frame1 = new ElevatorFrame();
				frame1.setTitle("Imperial Elevator");
				frame1.setLocation(250, 120);
				frame1.setSize(970, 560);
				frame1.setResizable(false);
				frame1.setVisible(true);
				frame1.setIconImage(imageIcon1.getImage());
				
				frame1.addWindowListener(new WindowAdapter() {
					public void windowClosing(WindowEvent e) {
						System.exit(0);
					}
				});
				
			}
		});
		chooses3[1].addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				eleNum = 4;
				JFrame frame1 = new ElevatorFrame();
				frame1.setTitle("Imperial Elevator");
				frame1.setLocation(250, 120);
				frame1.setSize(970, 560);
				frame1.setResizable(false);
				frame1.setVisible(true);
				frame1.setIconImage(imageIcon1.getImage());
				
				frame1.addWindowListener(new WindowAdapter() {
					public void windowClosing(WindowEvent e) {
						System.exit(0);
					}
				});
				
			}
		});
		chooses3[2].addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				eleNum = 5;
				JFrame frame1 = new ElevatorFrame();
				frame1.setTitle("Imperial Elevator");
				frame1.setLocation(250, 120);
				frame1.setSize(970, 560);
				frame1.setResizable(false);
				frame1.setVisible(true);
				frame1.setIconImage(imageIcon1.getImage());
				
				frame1.addWindowListener(new WindowAdapter() {
					public void windowClosing(WindowEvent e) {
						System.exit(0);
					}
				});
				
			}
		});
		chooses3[3].addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				eleNum = 6;
				JFrame frame1 = new ElevatorFrame();
				frame1.setTitle("Imperial Elevator");
				frame1.setLocation(250, 120);
				frame1.setSize(970, 560);
				frame1.setResizable(false);
				frame1.setVisible(true);
				frame1.setIconImage(imageIcon1.getImage());
				
				frame1.addWindowListener(new WindowAdapter() {
					public void windowClosing(WindowEvent e) {
						System.exit(0);
					}
				});
				
			}
		});
		for (int i = 0; i < chooses4.length; i++)
		{
			alarm.add(chooses4[i]);
			if (i < chooses2.length - 1)
			{
				alarm.addSeparator();
			}
			chooses2[i].setForeground(Color.darkGray);
			chooses2[i].setFont(new Font("Serif", Font.BOLD, 14));
		}

		menuBar.add(menu);
		menuBar.add(menuSet);
		menuBar.add(menuSet1);
		menuBar.add(alarm);
		setJMenuBar(menuBar);

		eleThread = new ElevatorThread[eleNum];

		//创建电梯线程
		for (int i = 0; i < eleNum; i++)
		{
			ElevatorThread list = new ElevatorThread();
			cp.add(list);
			list.getThread().start();
			eleThread[i] = list;
		}

		upState = new int[floorNum];
		downState = new int[floorNum];

		//初始化方向键状态
		for (int i = 0; i < upState.length; i++)
		{
			upState[i] = 0;
			downState[i] = 0;
		}

		Thread manageThread = new Thread(this);
		manageThread.start(); //启动调度线程

	}

	//	向上键的Listener
	class UpButtonAction extends MouseAdapter implements MouseListener
	{
		public void mousePressed(MouseEvent e)
		{
			for (int i = 0; i < upButton.length; i++)
			{
				if (e.getSource() == upButton[i])
				{
					upButton[i].setBackground(pressedUpDownColor);
					upState[i] = 1;
				}

				if (e.getSource() == downButton[i])
				{
					downButton[i].setBackground(pressedUpDownColor);
					downState[i] = 1;
				}
			}
		}
	}

	public static int getFloorNum()
	{
		return floorNum;
	}

	//调度线程run()方法
	public void run()
	{
		while (true)
		{
			try
			{
				Thread.sleep(100);
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}

			//处理向上键
			for (int i = 0; i < upState.length; i++)
			{
				if (upState[i] == 1)
				{
					upLookForList(i);
				}
				if (upState[i] >= 5)
				{
					if (i == eleThread[upState[i] - 5].getCurPos())
					{
						upState[i] = 0;
						upButton[i].setBackground(unPressedUpDownColor);
						//System.out.println("unPressedUpDownColor");
					}
				}
			}

			//处理向下键
			for (int i = 0; i < downState.length; i++)
			{
				if (downState[i] == 1)
				{
					downLookForList(i);
				}
				if (downState[i] >= 5)
				{
					if (i == eleThread[downState[i] - 5].getCurPos())
					{
						downState[i] = 0;
						downButton[i].setBackground(unPressedUpDownColor);
					}
				}
			}
		}
	}

	//	寻找响应向上键最近的电梯
	private boolean upLookForList(int floor)
	{
		int whichList = 0;
		int distance = floorNum;

		for (int j = 0; j < eleThread.length; j++)
		{
			if (eleThread[j].isAbort()
				|| (eleThread[j].isUp() && floor >= eleThread[j].getCurPos()))
			{
				int temp = Math.abs(floor - eleThread[j].getCurPos());
				if (temp < distance)
				{
					whichList = j;
					distance = Math.abs(floor - eleThread[j].getCurPos());
				}
			}
		}

		if (distance != floorNum)
		{
			upState[floor] = 5 + whichList;
			eleThread[whichList].setTarPos(floor);
			return true;
		} else
		{
			return false;
		}

	}

	//	寻找响应向下键最近的电梯
	private boolean downLookForList(int floor)
	{
		int whichList = 0;
		int distance = floorNum;

		for (int j = 0; j < eleThread.length; j++)
		{
			if (eleThread[j].isAbort()
				|| (eleThread[j].isDown() && floor <= eleThread[j].getCurPos()))
			{
				int temp = Math.abs(floor - eleThread[j].getCurPos());
				if (temp < distance)
				{
					whichList = j;
					distance = Math.abs(floor - eleThread[j].getCurPos());
				}
			}
		}

		if (distance != floorNum)
		{
			downState[floor] = 5 + whichList;
			eleThread[whichList].setTarPos(floor);
			return true;
		} else
		{
			return false;
		}

	}

}
