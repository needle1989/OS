package cytus;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTextField;



public abstract class  FileFCB
{	
	JPanel viewPanel=new JPanel();
	JTextField nameField=new JTextField();
	ImageIcon viewImg;
	
	Layout contentPanel;			//文件夹说拥有的面板  文件没有这个属性
	Layout fatherContentPanel;	//自己说在的面板
	String whoAmI;				
	String fatherAddress;
	String name;
	String createTime;
	String visitTime;
	String modifiTime;
	Block block;
	JFrame propertyFrame;
	boolean isHide=false;
	
	
	public abstract void create();
	public abstract void resetName();
	public abstract	boolean delete(boolean isRootPanel);
	public abstract void open();

	public void setFatherAddress(String str)
	{
		fatherAddress=str;
	}
	
	public String getSystemTime()
	{
		 Date currentTime = new Date();
		 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 return  formatter.format(currentTime);
	}
	
	public void setCreateTime()
	{
		createTime=getSystemTime();
		visitTime=createTime;
		modifiTime=createTime;
	
	}
	
	public void setVisitTime()
	{
		visitTime=getSystemTime();

	}
	
	public void setModifiTime()
	{
		modifiTime=getSystemTime();
		
	}
	
	public void showProperty()
	{
		propertyFrame=new JFrame();
		propertyFrame.setTitle("properties of "+name);
		propertyFrame.setSize(320, 300);
		propertyFrame.setResizable(false);
		propertyFrame.setVisible(true);
		propertyFrame.setLocationRelativeTo(DiskInit.mainFrame);
		propertyFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		JPanel panel=new JPanel();
		panel.setBackground(Color.white);
		panel.setLayout(null);
		propertyFrame.add(panel);
		
		JLabel name1=new JLabel("name:",JLabel.LEFT);
		name1.setBounds(20, 50, 50, 20);
		panel.add(name1);
		
		JLabel nameLabel=new JLabel(name,JLabel.LEFT);
		nameLabel.setBounds(100, 50, 200, 20);
		panel.add(nameLabel);
		
		
		JLabel type=new JLabel("type:",JLabel.LEFT);
		type.setBounds(20, 90, 50, 20);
		panel.add(type);
		
		JLabel typeLabel;
		typeLabel=new JLabel(whoAmI,JLabel.LEFT);
		typeLabel.setBounds(100, 90, 200, 20);
		panel.add(typeLabel);
		
		JLabel address=new JLabel("address:",JLabel.LEFT);
		address.setBounds(20, 130, 50, 20);
		panel.add(address);
		
		JLabel addressLabel=new JLabel(fatherAddress,JLabel.LEFT);
		addressLabel.setBounds(100, 130, 200, 20);
		panel.add(addressLabel);
		
		if (whoAmI.equals("folder"))
		{
			JLabel include=new JLabel("containment:",JLabel.LEFT);
			include.setBounds(20, 170, 50, 20);
			panel.add(include);
			
			JLabel includeFile=new JLabel("folder:"+contentPanel.folderList.size()+"  file:"+contentPanel.fileList.size(),JLabel.LEFT);
			includeFile.setBounds(100,170,200,20);
			panel.add(includeFile);
		}
		else if (whoAmI.equals("file"))
		{
			JLabel size=new JLabel("size:",JLabel.LEFT);
			size.setBounds(20, 170, 50, 20);
			panel.add(size);
			
			int index;
			for (index=0; index<fatherContentPanel.fileList.size(); index++)
				if (fatherContentPanel.fileList.get(index)==this) break;
			JLabel fileSize=new JLabel(new Integer(fatherContentPanel.fileList.get(index).text.length()).toString(),JLabel.LEFT);
			fileSize.setBounds(100, 170, 200, 20);
			panel.add(fileSize);
		}
	}
	
	public void getProperty(Block block)
	{
		this.block=block;
		
		String str=block.property;
		int begin;
		int end;
		
		begin=str.indexOf("文件名:");
		str=str.substring(begin+4);
		end=str.indexOf('\n');
		name=str.substring(0, end);
		str=str.substring(end+1);
		
		begin=str.indexOf("地址:");
		str=str.substring(begin+3);
		end=str.indexOf('\n');
		fatherAddress=str.substring(0,end);
		str=str.substring(end+1);
	}
	
}

