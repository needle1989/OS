package cytus;

import javax.swing.JOptionPane;

public class Block
{
	String property;
	String data;
	int index;

	void setProperty(FileFCB fcb)
	{
		property="�ļ�����:"+fcb.whoAmI+'\n';
		property=property+"�ļ���:"+fcb.name+'\n';
		property=property+"��ַ:"+fcb.fatherAddress+'\n';
	}
	
	void setData(String str)
	{
		data=str;
	}
	
	void setData(Layout content)
	{
		data="";
		for (int i=0; i<content.folderList.size(); i++)
		{
			data=data+content.folderList.get(i).block.index+'\n';
		}
		data=data+"NULL\n";
		
		for (int i=0; i<content.fileList.size(); i++)
		{
			data=data+content.fileList.get(i).block.index+'\n';
		}
	}
}

