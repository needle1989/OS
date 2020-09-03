package sse.os.imperialElevator;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.MatteBorder;

/*
 *
 *
 * 
 */

/**
 *
 */
public class ElevatorThread extends JPanel implements Runnable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final int UP = 1, DN = -1, ABORT = 0; //电梯的状态
	private int floorNum; //楼层数
	private int direction; //电梯运行的方向或停止
	private int curPos; //电梯目前处于的楼层位置
	private boolean[] numState; //电梯内部数字键的状态
	private int tarPos; //电梯到达的目标位置
	private Thread thread; //自身线程

	private Color numColor0 = new Color(92, 160, 190), numColor1 = Color.green;
	private Color floorColor0 = new Color(175, 238, 238),
		floorColor1 = new Color(0, 255, 255);

	JButton[] listButton; //代表电梯的数组
	JButton[] numButton; //电梯内部数字按钮
	JLabel dispState,  dispFloor;

	public ElevatorThread()
	{
		floorNum = ElevatorFrame.getFloorNum();
		direction = ABORT;
		curPos = 0;
		tarPos = 0;

		//对电梯内部的数字键进行状态初始化
		numState = new boolean[floorNum];
		for (int i = 0; i < numState.length; i++)
		{
			numState[i] = false;
		}

		//产生自身线程
		thread = new Thread(this);

		//面板布局
		setLayout(new GridLayout(floorNum+1, 2));
		this.setBorder(new MatteBorder(25, 25, 25, 25, new Color(235,235,230)));
		listButton = new JButton[floorNum];
		numButton = new JButton[floorNum];

		dispFloor = new JLabel("F",  SwingConstants.CENTER);
		//dispFloor.setBackground(new Color(17, 123, 2));
		dispState = new JLabel("WAIT", SwingConstants.CENTER);
		dispState.setForeground(new Color(217, 123, 2));

		this.add(dispFloor);
		this.add(dispState);

		MouseListener numListener = new NumButtonAction();
		//设置属性
		for (int i = listButton.length - 1; i >= 0; i--)
		{
			numButton[i] = new JButton(String.valueOf(i + 1));
			numButton[i].addMouseListener(numListener);
			numButton[i].setBackground(numColor0);
			listButton[i] = new JButton();
			listButton[i].setEnabled(false);
			listButton[i].setBackground(floorColor0);
			this.add(numButton[i]);
			this.add(listButton[i]);
		}
		listButton[curPos].setBackground(floorColor1);
	}

	//电梯内部数字键的监听器类
	class NumButtonAction extends MouseAdapter implements MouseListener
	{

		public void mousePressed(MouseEvent e)
		{
			for (int i = 0; i < numButton.length; i++)
			{
				if (e.getSource() == numButton[i])
				{
					numState[i] = true;
					numButton[i].setBackground(numColor1);
					if (direction == ABORT)
					{
						tarPos = i;
					}

					if (direction == UP)
					{
						tarPos = getMaxPressedNum();
					}
					if (direction == DN)
					{
						tarPos = getMinPressedNum();
					}
				}
			}
		}
	}

	//电梯线程的run()方法
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
		if (direction == UP || direction == DN)
			{
				try
				{
					Thread.sleep(100);
				} catch (InterruptedException e)
				{
					e.printStackTrace();
				}
				direction = ABORT;
			}
			
			if (tarPos > curPos)
			{
				direction = UP;
				dispState.setText("UP");
				moveUp();
				direction = ABORT;
				dispState.setText("WAIT");
			} else if (tarPos < curPos)
			{
				direction = DN;
				dispState.setText("DOWN");
				moveDn();
				direction = ABORT;
				dispState.setText("WAIT");
			}

		}
	}

	//电梯向上运行
	public void moveUp()
	{
		int oldPos = curPos;
		for (int i = curPos + 1; i <= tarPos; i++)
		{
			try
			{
				dispState.setText("UP");
				Thread.sleep(600);
				listButton[i].setBackground(floorColor1);
				
				if (i > oldPos)
				{
					listButton[i - 1].setBackground(floorColor0);
				}
				
				if (numState[i])
				{				
					dispState.setText("OPEN");
					Thread.sleep(1000);
					
					dispState.setText("CLOSE");
					numButton[i].setBackground(numColor0);
					Thread.sleep(1000);
				}			
				curPos = i;

			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}

		clearState();
	}

	//	电梯向下运行
	public void moveDn()
	{
		int oldPos = curPos;
		for (int i = curPos - 1; i >= tarPos; i--)
		{
			try
			{
				Thread.sleep(600);
				listButton[i].setBackground(Color.blue);
				
				if (i < oldPos)
				{
					listButton[i + 1].setBackground(floorColor0);
				}
				
				if (numState[i])
				{
					dispState.setText("OPEN");
					Thread.sleep(2000);			
					dispState.setText("CLOSE");
					numButton[i].setBackground(numColor0);
					Thread.sleep(800);
				}
				curPos = i;


			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
		clearState();
	}

	public Thread getThread()
	{
		return thread;
	}

	private int getMinPressedNum()
	{
		int min = 0;
		for (int i = 0; i < numState.length; i++)
		{
			if (numState[i])
			{
				min = i;
				break;
			}
		}
		return min;
	}

	private int getMaxPressedNum()
	{
		int max = 0;
		for (int i = numState.length - 1; i >= 0; i--)
		{
			if (numState[i])
			{
				max = i;
				break;
			}
		}
		return max;
	}

	private void clearState()
	{
		for (int i = 0; i < numState.length; i++)
		{
			if (numState[i])
			{
				numState[i] = false;
				numButton[i].setBackground(numColor0);
			}
		}
	}

	public int getDirection()
	{
		return direction;
	}

	public int getTarPos()
	{
		return tarPos;
	}

	public void setDirection(int i)
	{
		direction = i;
	}

	public void setTarPos(int i)
	{
		if (direction == ABORT)
		{
			tarPos = i;
			numState[i] = true;
			if (curPos > tarPos)
			{
				direction = DN;
			}
			if (curPos < tarPos)
				{
					direction = UP;
				}				
		}
		
		if (direction == UP && i > tarPos)
		{
			tarPos = i;
			numState[i] = true;
		}

		if (direction == DN && i < tarPos)
		{
			tarPos = i;
			numState[i] = true;
		}
	}

	public boolean isUp()
	{
		return direction == UP;
	}

	public boolean isDown()
	{
		return direction == DN;
	}

	public boolean isAbort()
	{
		return direction == ABORT;
	}

	public int getCurPos()
	{
		return curPos;
	}

	public void setDirectionUp()
	{
		direction = UP;
	}

	public void setDirectionDn()
	{
		direction = DN;
	}

	public void setDirectionAbort()
	{
		direction = ABORT;
	}


}
