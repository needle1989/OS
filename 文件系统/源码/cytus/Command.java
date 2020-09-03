package cytus;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Command 
{
	JTextField addressField;
	JTextField searchField;
	
	JPanel addressPanel;
	JPanel searchPanel;
	
	
	JPanel panel;
	static Command folderToolBar;
	
	static public Command getToolBar()
	{
		return folderToolBar;
	}
	
	public Command()
	{
		
		folderToolBar=this;
		
		panel=new JPanel();
		panel.setLayout(new BorderLayout());
		
		JButton back=new JButton("Backward");
		back.setBorderPainted(false);
		back.setFocusPainted(false);
		back.addActionListener(backButtonListener);
		panel.add(back,BorderLayout.WEST);
		
		
		searchPanel=new JPanel();
		searchPanel.setLayout(new BorderLayout());
		
		
		
		
		panel.add(searchPanel,BorderLayout.EAST);
		
		addressPanel=new JPanel();
		addressPanel.setLayout(new BorderLayout());
		addressField=new JTextField();
		addressField.setFont(new Font(addressField.getFont().getFontName(),addressField.getFont().getStyle(),15));
		
		
		addressPanel.add(addressField,BorderLayout.CENTER);
		panel.add(addressPanel,BorderLayout.CENTER);
				

	}
	
	void setAddress(String str)
	{
		addressField.setText(str);
	}
	
	ActionListener backButtonListener=new ActionListener()
	{
		public void actionPerformed(ActionEvent e)
		{
			if (Layout.getRunningPanel()==null || Layout.getRunningPanel().fatherContentPanel==null)	//在我的q盘下或者在我的q盘的第一集目录
			{
				setAddress("CYTUS");
				Layout.switchPanel(DiskInit.getDiskPanel());
				return ;
			}
			else
			{
				Layout.switchPanel(Layout.getRunningPanel().fatherContentPanel);
			}
		}
	};
	
	
	
	
}
