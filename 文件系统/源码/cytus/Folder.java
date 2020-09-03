package cytus;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;

public class Folder extends FileFCB
{
	FolderViewPanel  folderView=new FolderViewPanel();
	
	class FolderViewPanel extends JPanel
	{	
		void setViewPanel()
		{
			viewPanel.setBackground(Color.white);
			viewPanel.setBounds(15, 5, 70, 70);
			viewPanel.addMouseListener(viewMouseListener);
			viewImg = new ImageIcon("resource/folder.png");
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
		
		FolderViewPanel()
		{
			setBackground(Color.white);
			setLayout(null);
			setPreferredSize(new Dimension(100,100));
			setViewPanel();
			setNamePanel();
		}
		
		MouseListener viewMouseListener=new MouseListener()
		{

			public void mouseClicked(MouseEvent e) 
			{	
				if (e.getButton()==MouseEvent.BUTTON1 && e.getClickCount()==2)
				{
					viewPanel.setBackground(Color.white);
					open();
				}
				else if (e.getButton()==MouseEvent.BUTTON3)
				{
					JPopupMenu menu=new JPopupMenu();
					JMenuItem openMenu=new JMenuItem("Open");
					openMenu.addActionListener(openMenuListener);
					menu.add(openMenu);
					
					JMenuItem resetNameMenu=new JMenuItem("Rename");
					resetNameMenu.addActionListener(resetNameMenuListener);
					menu.add(resetNameMenu);
					
					JMenuItem deleteMenu=new JMenuItem("Delete");
					deleteMenu.addActionListener(deleteMenuListener);
					menu.add(deleteMenu);
					
					JMenuItem propertyMenu=new JMenuItem("Properties");
					propertyMenu.addActionListener(propertyMenuListener);
					menu.add(propertyMenu);
					
					menu.show(e.getComponent(),e.getX(),e.getY());
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
			
			ActionListener resetNameMenuListener=new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					resetName();
				}
			};
			
			ActionListener deleteMenuListener=new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					delete(true);
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
	
	public Folder(Layout father)
	{
		fatherContentPanel=father;
		create();
		contentPanel=new Layout(fatherContentPanel);
	}
	


	public Folder(Block block,Layout father)
	{
		fatherContentPanel=father;
		father.folderList.add(this);
		contentPanel=new Layout(fatherContentPanel);
		getProperty(block);
		nameField.setText(name);
		whoAmI="folder"; 
		
		if (block.data==null) return ;
		String str=block.data;
		while (true)		//文件夹
		{
			int index=str.indexOf('\n');
			if (str.substring(0, index).equals("NULL"))
			{
				str=str.substring(index+1);
				break;
			}
			int blockIndex=new Integer(str.substring(0,index)).intValue();
			new Folder(DiskInit.block[blockIndex],this.contentPanel);
			str=str.substring(index+1);
		}
		while (!str.isEmpty())
		{
			int index=str.indexOf('\n');
			int blockIndex=new Integer(str.substring(0,index)).intValue();
			new Files(DiskInit.block[blockIndex],this.contentPanel);
			str=str.substring(index+1);
		}
		
	}
	
	//打开文件夹啊
	public void open()
	{
		setVisitTime();
		block.setProperty(this);
		Layout.switchPanel(contentPanel);
	}
	
	///删除文件夹
	public boolean delete(boolean isRootPanel)
	{
		if (isRootPanel)
		{	
			
		}
		boolean isDelete=true;
			
		for (int i=0; i<contentPanel.fileList.size(); i++)
		{
			if (!contentPanel.fileList.get(i).delete(false)) isDelete=false;
			else i--;
		}
		for (int i=0; i<contentPanel.folderList.size(); i++)
		{
			if (!contentPanel.folderList.get(i).delete(false)) isDelete=false;
			else i--;
		}
		
		if (isDelete)
		{
			DiskInit.fat.deleteBlock(this);
			contentPanel.fatherContentPanel.folderList.remove(this);
			
			Block fatherBlock;
			if (fatherContentPanel.getFolder()==null) fatherBlock=DiskInit.block[0];
			else fatherBlock=fatherContentPanel.getFolder().block;
			fatherBlock.setData(fatherContentPanel);
			
			if (isRootPanel)
			{
				fatherContentPanel.refresh();
			}
		}
		
		return isDelete;
	}
	
	boolean checkName(String str)
	{
		for (int i=0; i<fatherContentPanel.folderList.size(); i++)
		{
			if (fatherContentPanel.folderList.get(i)!=this && fatherContentPanel.folderList.get(i).name.equals(str))	return false;
		}
		return true;
	}
	
	//重命名以后，检查命名是否规范，帮助修改	  
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
		
		if (contentPanel!=null)
		{
			for (int i=0; i<contentPanel.folderList.size(); i++)
			{
				contentPanel.folderList.get(i).setFatherAddress(fatherAddress+name+'/');
			}
			for (int i=0; i<contentPanel.fileList.size(); i++)
			{
				contentPanel.fileList.get(i).setFatherAddress(fatherAddress+name+'/');
			}
		}
	}
	
	///点击重命名 更改nameField状态
	public void resetName()	
	{
		nameField.setEditable(true);
		nameField.addFocusListener(nameFieldFocusListener);
		nameField.addActionListener(nameFieldActionListener);
	}
	
	public void create()
	{
		setCreateTime();
		whoAmI="folder";
		nameField.setText("newFolder");
		resetName(true);	
	}
	
	//隐藏文件夹
	void hideFolder(boolean hide)
	{
		isHide=hide;
		fatherContentPanel.refresh();
		block.setProperty(this);
	}
	
	//////获取绝对地址
	public String getAddress()
	{
		return fatherAddress+name+'/';
	}
	
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
}

