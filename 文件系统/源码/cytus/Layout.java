package cytus;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import cytus.DiskInit.DiskPanel;

public class Layout extends JPanel
{
	Vector<Folder> folderList;
	Vector<Files> fileList;
	Layout fatherContentPanel;
	static boolean isShowAll=false;
	static Layout runningPanel=null;

	Layout(Layout father)
	{
		fatherContentPanel=father;
		setBackground(Color.white);
		setLayout(new FlowLayout(FlowLayout.LEFT));
		addMouseListener(contentMouseListener);
		folderList=new Vector();
		fileList=new Vector();
		
	}
	

	Timer refreshTime;
	void deleteTime()
	{
		if (refreshTime==null) return ;
		refreshTime.cancel();
		refreshTime=null;
	}
	
	public void refresh()
	{
		refreshTime=new Timer();	
		class RefreshTask extends TimerTask
		{
			public void run()
			{
				for (int i=0; i<folderList.size(); i++)
				{
					if (isShowAll || !folderList.get(i).isHide)	add(folderList.get(i).folderView);
				}
				for (int i=0; i<fileList.size(); i++)
				{
					if (isShowAll || !fileList.get(i).isHide)	add(fileList.get(i).fileView);	
				}	
				repaint();
				updateUI();
				
				deleteTime();
			}
		}
	
		removeAll();
		updateUI();
		refreshTime.schedule(new RefreshTask(), 50);
		
		Folder folder=getFolder();
		if (folder==null)	Command.getToolBar().setAddress("CYTUS/");
		else	Command.getToolBar().setAddress(folder.getAddress());
	}
	
	public Folder getFolder()
	{
		if (this==DiskInit.contentPanel) return null;
		for (int i=0; i<fatherContentPanel.folderList.size(); i++)
		{
			if (fatherContentPanel.folderList.get(i).contentPanel==this) return fatherContentPanel.folderList.get(i);
		}
		return null;
	}
	
	Block getContentBlock()
	{	
		Folder fatherFolder=getFolder();
		if (fatherFolder==null) return DiskInit.block[0];
		else return fatherFolder.block;
	}
	
	public void createFolder()
	{
		Block block=DiskInit.fat.getBlock();
		if (block==null)
		{
			JOptionPane.showMessageDialog(null, "空间已满，无法新建文件");
			return ;
		}
		
		Folder folder=new Folder(this);
		folder.block=block;
		folderList.add(folder);
		
		Folder fatherFolder=folder.fatherContentPanel.getFolder();
		if (fatherFolder==null)
			folder.setFatherAddress("CYTUS/");
		else	
			folder.setFatherAddress(fatherFolder.getAddress());
		
		folder.block.setProperty(folder);
		DiskInit.fat.setUseBlock(folder.block.index);
		
		getContentBlock().setData(this);
		refresh();
	}
	
	public void createFile()
	{
		Block block=DiskInit.fat.getBlock();
		if (block==null)
		{
			JOptionPane.showMessageDialog(null, "空间已满，无法新建文件");
			return ;
		}
		
		Files file=new Files(this);
		file.block=block;
		fileList.add(file);
		
		if (file.fatherContentPanel.getFolder()==null)
			file.setFatherAddress("CYTUS/");
		else	
			file.setFatherAddress(file.fatherContentPanel.getFolder().getAddress());
		
		file.block.setProperty(file);
		DiskInit.fat.setUseBlock(file.block.index);
		
		getContentBlock().setData(this);
		refresh();
	}
	
	public boolean delete(Files file)
	{
		if (fileList.remove(file))
		{
			refresh();
			return true;
		}
		else return false;
	}
	
	public void addKeyStringDocument(String key,JPanel panel)
	{
		for (int i=0; i<folderList.size(); i++)
		{
			if (folderList.get(i).name.indexOf(key)!=-1) panel.add(folderList.get(i).folderView);
			folderList.get(i).contentPanel.addKeyStringDocument(key, panel);
		}
		for (int i=0; i<fileList.size(); i++)
		{
			if (fileList.get(i).name.indexOf(key)!=-1) panel.add(fileList.get(i).fileView);
		}
	}
	
	MouseListener contentMouseListener = new MouseListener()
	{
		public void mouseClicked(MouseEvent e) 
		{
			if (e.getButton()==MouseEvent.BUTTON3)
			{
				JPopupMenu menu=new JPopupMenu();
				
				
				JMenuItem newFile=new JMenuItem("New File");
				newFile.addActionListener(newFileMenuListener);
				menu.add(newFile);
				
				JMenuItem newFolder=new JMenuItem("New Folder");
				newFolder.addActionListener(newFolderMenuListener);
				menu.add(newFolder);
				
				
				menu.show(e.getComponent(),e.getX(),e.getY());
			}
		}
		public void mouseEntered(MouseEvent arg0) {}
		public void mouseExited(MouseEvent arg0) {}
		public void mousePressed(MouseEvent arg0) {}
		public void mouseReleased(MouseEvent arg0) {}
	
	
		
		ActionListener newFileMenuListener=new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				createFile();
			}
		};
		
		ActionListener newFolderMenuListener=new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				createFolder();
			}
		};
	};
	
	static Layout getRunningPanel()
	{
		return runningPanel;
	}
	static void switchPanel(Layout contentPanel)
	{
		runningPanel=contentPanel;
		DiskInit.getMainPanel().removeAll();
		DiskInit.getMainPanel().add(contentPanel);
		Layout.getRunningPanel().refresh();
	}
	static void switchPanel(DiskPanel panel)
	{
		runningPanel=null;
		DiskInit.getMainPanel().removeAll();
		DiskInit.getMainPanel().add(panel);
		DiskInit.getMainPanel().repaint();
		DiskInit.getMainPanel().updateUI();
	}
}
