package cytus;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Files extends FileFCB
{
	Vector<String> bufferString=new Vector(10);
	FileViewPanel fileView=new FileViewPanel();
	
	int bufferIndex;
	int fontSize;
	String text="";
	
	JMenuBar menuBar;
	JFrame frame;
	JPanel panel;
	JTextArea textArea;
	
	JFrame replaceFrame;
	JMenuItem undo;
	JMenuItem redo;
	
	
	class FileViewPanel extends JPanel
	{
		void setViewPanel()
		{
			viewPanel.setBackground(Color.white);
			viewPanel.setBounds(15, 5, 70, 70);
			viewPanel.addMouseListener(fileMouseListener);
			viewImg = new ImageIcon("resource/file.png");
			viewImg.setImage(viewImg.getImage().getScaledInstance(60,60,Image.SCALE_DEFAULT));
			JLabel imgLabel = new JLabel(viewImg);
			viewPanel.add(imgLabel);
			add(viewPanel);
		}
		
		void setNamePanel()
		{
			nameField.setHorizontalAlignment(JTextField.CENTER);
			nameField.setEditable(false);
			nameField.setBackground(Color.white);
			nameField.setBounds(10, 80, 80, 20);
			add(nameField);
		}
		FileViewPanel()
		{
			setLayout(null);
			setBackground(Color.white);
			setPreferredSize(new Dimension(100,100));
			setViewPanel();
			setNamePanel();
		}
		
		MouseListener fileMouseListener=new MouseListener()
		{
		
			public void mouseClicked(MouseEvent e) 
			{
			
				if (e.getButton()==MouseEvent.BUTTON3)
				{
					JPopupMenu menu=new JPopupMenu();
					JMenuItem openMenu=new JMenuItem("Open");
					openMenu.addActionListener(openMenuListener);
					menu.add(openMenu);
					
					//JMenuItem editMenu=new JMenuItem("Edit");
					//editMenu.addActionListener(openMenuListener);
					//menu.add(editMenu);
					
					JMenuItem deleteMenu=new JMenuItem("Delete");
					deleteMenu.addActionListener(deleteMenuListener);
					menu.add(deleteMenu);
					
					JMenuItem resetNameMenu=new JMenuItem("Rename");
					resetNameMenu.addActionListener(resetNameMenuListener);
					menu.add(resetNameMenu);
					
					
					JMenuItem propertyMenu=new JMenuItem("Properties");
					propertyMenu.addActionListener(propertyMenuListener);
					menu.add(propertyMenu);
					
					menu.show(e.getComponent(),e.getX(),e.getY());
				}
				if (e.getButton()==MouseEvent.BUTTON1 && e.getClickCount()==2)
				{
					viewPanel.setBackground(Color.white);
					open();
				}
			}

			public void mouseEntered(MouseEvent arg0) 
			{
				viewPanel.setBackground(new Color(230, 248, 255));
			}

			public void mouseExited(MouseEvent arg0) 
			{
				viewPanel.setBackground(Color.white);
			}

			public void mousePressed(MouseEvent arg0) 
			{			
			}

			public void mouseReleased(MouseEvent e) 
			{		
			}
			
			ActionListener openMenuListener = new ActionListener() 
			{
				public void actionPerformed(ActionEvent e) 
				{
					open();
				}				
			}; 
			ActionListener deleteMenuListener = new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					delete(true);
				}
			};
			
		
			
			ActionListener resetNameMenuListener=new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					resetName();
				}
			};
			
			ActionListener propertyMenuListener=new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					showProperty();
				}
			};
		};
	}
	
	void setTextArea()
	{
		textArea=new JTextArea();
		textArea.setBackground(Color.white);
		textArea.addKeyListener(setTextListener);
		JScrollPane scroll = new JScrollPane(textArea);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		panel.add(scroll);
	}
	
	//创建记事本
	public void createTxt()
	{
		//setMenuBar();
		setTextArea();
	
		frame=new JFrame();
		frame.setJMenuBar(menuBar);
		frame.add(panel);
		frame.setSize(400, 500);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.addWindowListener(closeListener); 
	}
	
	public Files(Layout father)
	{
		fatherContentPanel=father;
		panel=new JPanel();
		panel.setBackground(Color.white);
		panel.setLayout(new GridLayout(1,1));
		
		createTxt();
		
		create();
	}
	
	public Files(Block block,Layout father)
	{
		fatherContentPanel=father;
		fatherContentPanel.fileList.add(this);
		
		panel=new JPanel();
		panel.setBackground(Color.white);
		panel.setLayout(new GridLayout(1,1));
		
		createTxt();
		getProperty(block);
		nameField.setText(name);
		text=block.data;
		if (text==null) text="";
		whoAmI="file";
	}
	
	public void create()
	{
		setCreateTime();
		whoAmI="file";
		nameField.setText("newFile");
		resetName(true);
	}
	

	
	/////////打开
	public void open()
	{
		if (frame.isShowing())
		{
			frame.requestFocus();
			return ;
		}
	
		setVisitTime();
		block.setProperty(this);
		frame.show();
		frame.setTitle("noteBook - "+name);
		textArea.setText(text);
		
		undo.setEnabled(false);
		redo.setEnabled(false);
		bufferString.clear();
		bufferIndex=0;
		bufferString.add(text);
	}
	
	//////////删除
	public boolean delete(boolean isRootPanel)
	{
		if (frame.isShowing())
		{
			frame.requestFocus();
			JOptionPane.showMessageDialog(null, "file using", "error", JOptionPane.ERROR_MESSAGE, null);
			return false;
		}
		else
		{
			if (isRootPanel)
			{
			
			}
			
			DiskInit.fat.deleteBlock(this);
			fatherContentPanel.delete(this);
			
			Block fatherBlock;
			if (fatherContentPanel.getFolder()==null) fatherBlock=DiskInit.block[0];
			else fatherBlock=fatherContentPanel.getFolder().block;
			fatherBlock.setData(fatherContentPanel);
	
					
			return true;
		}
	}
	
	
	
	/////////替换
	
	
	//////添加入缓冲，给撤消使用
	void addBuffer()
	{
		
		if (bufferString.get(bufferIndex).equals(textArea.getText())) return ;
		for (int i=bufferString.size()-1; i>bufferIndex; i--)
		{
			bufferString.remove(i);
		}
		if (bufferString.size()<10)
		{
			bufferIndex++;
			bufferString.add(textArea.getText());
		}
		else
		{
			for (int i=0; i<9; i++)
			{
				bufferString.set(i, bufferString.get(i+1));
			}
			bufferString.set(9, textArea.getText());
		}
		if (bufferIndex!=0) undo.setEnabled(true);
		else undo.setEnabled(false);
		if (bufferIndex!=bufferString.size()-1) redo.setEnabled(true);
		else redo.setEnabled(false);
	}
	
	////////退出
	void fileExit()
	{
		if (replaceFrame!=null) replaceFrame.dispose();
		if (!text.equals(textArea.getText()))
		{
			int option = JOptionPane.showConfirmDialog(
				null, "do you wish to save?",
				"file changed", JOptionPane.YES_NO_CANCEL_OPTION,
				JOptionPane.WARNING_MESSAGE, null);  
			switch (option)
			{
			case JOptionPane.YES_OPTION:
				text=textArea.getText();
				block.setData(text);
				setModifiTime();
				frame.dispose();
				break;
			case JOptionPane.NO_OPTION:
				frame.dispose();
				break;
			case JOptionPane.CANCEL_OPTION:
				break;
			}
		}
		else frame.dispose();
		
		block.setProperty(this);
	}
	
	
	//////建仓是否有重复名字
	boolean checkName(String str)
	{
		for (int i=0; i<fatherContentPanel.fileList.size(); i++)
		{
			if (fatherContentPanel.fileList.get(i)!=this && fatherContentPanel.fileList.get(i).name.equals(str))	return false;
		}
		return true;
	}
	
	////自动调整重命名
	void resetName(boolean isAutomic)
	{
		String newName=nameField.getText();
		while (!newName.isEmpty() && newName.charAt(newName.length()-1)==' ') newName=newName.substring(0, newName.length()-1);
		while (!newName.isEmpty() && newName.charAt(0)==' ') newName=newName.substring(1);
		
		if (newName.length()>10) newName=newName.substring(0,10);
		
		if (newName.length()==0)
		{
			nameField.setText(name);
			return ;
		}
		
		if (!checkName(newName))
		{
			int i=0;
			while (!checkName(newName+"("+new Integer(++i).toString()+')'));
			
			newName=newName+"("+new Integer(i).toString()+')';
		}
		name=newName;
		nameField.setText(name);
		
		if (!isAutomic)
		{
			setModifiTime();
			block.setProperty(this);
		}
	}
	
	public void resetName()
	{
		nameField.setEditable(true);
		nameField.addFocusListener(nameFieldFocusListener);
		nameField.addActionListener(nameFieldActionListener);
	}

	///获取路径
	public String getAddress()
	{
		return fatherAddress+name;
	}
	
	
	//////////////////////////////监听器///////////////////////////
	
	/////////////////////////////命名////////////////////////////
	FocusListener nameFieldFocusListener=new FocusListener()
	{
		public void focusGained(FocusEvent arg0) 
		{
		}
		public void focusLost(FocusEvent e) 
		{
			nameField.setEditable(false);
			resetName(false);
		}
	};
	
	ActionListener nameFieldActionListener=new ActionListener()
	{
		public void actionPerformed(ActionEvent arg0) 
		{
			fatherContentPanel.requestFocus();
		}
	};
	
	//////////////////////////////关闭///////////////////////////////
	WindowAdapter closeListener=new WindowAdapter() 
	{ 
	       public void windowClosing(WindowEvent   e) 
	       {     
	    	   fileExit();
	       } 
	 };
	
	///////////////////////////修改文档//////////////////////
	KeyListener setTextListener=new KeyListener()
	{
		public void keyPressed(KeyEvent arg0) 
		{					
		}

		public void keyReleased(KeyEvent arg0) 
		{	
			addBuffer();
		}

		public void keyTyped(KeyEvent arg0) 
		{			
		}
		
	};
}
