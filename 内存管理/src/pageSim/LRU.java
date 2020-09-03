package pageSim;

import java.util.Arrays;
import java.util.Random;

public class LRU
{
	private static Random rand = new Random();
	public static void run() 
	{
		int lossCount = 0;
        int getCount = 0;
		int tmp = 0;
		int[] seq = new int[320];
		for ( int i = 0;i < 320; i++) {
			seq[i] = tmp;
			tmp++;
		}
        shuffle(seq);
        System.out.println();
        System.out.println("����320��ָ���ִ�����У�");
        System.out.println(Arrays.toString(seq));
        

        Page[] pages = new Page[32];
        for (int i = 0; i < pages.length; i++)
        {
            pages[i] = new Page(i);
        }
        int[] memory = new int[4];
        for (int i = 0; i < memory.length; i++)
        {
        	memory[i]= -1;
        }

        
        
        for (int i = 0; i < seq.length; i++)
        {
        	System.out.println();
            System.out.println("ִ�е�"+seq[i]+"��ָ��");

            int pageNumber = seq[i] / 10;
            Page page = pages[pageNumber];
            
            //����ָ�����ڴ���ʱ����ʾ�������ַ
            
            if(page.getFlag())
            {
            	page.setVisited(getCount++);
                System.out.println("��ָ����"+seq[i]/10+"ҳ��λ���ڴ��"+page.getBlockNum());
            }
            else
            {
            	//����ȱҳ���ڴ�δ��ʱ��ֱ�ӽ�ҳ�����
            	
                lossCount++;
                boolean blockFlag = false;
                for (int j = 0; j < memory.length; j++)
                {
                    if(memory[j]==-1)
                    {
                    	tmp = page.getPageNum();
                        memory[j] = tmp;
                        page.setBlockNum(j);
                        page.setFlag(true);
                        blockFlag = true;
                        page.setVisited(getCount++);
                        System.out.println("����ȱҳ����"+"��"+tmp+"ҳ"+"�����ڴ��"+j);
                        break;
                    }
                }
                
                //����ȱҳ����Ҫ��ҳʱ��ʹ��LRU�㷨
                
                if(!blockFlag)
                {
                    int org = 0;
                    int lessVisited = pages[memory[0]].getVisited();
                    for (int j = 1; j < memory.length; j++)
                    {
                        int visited = pages[memory[j]].getVisited();
                        if(lessVisited>visited)
                        {
                            org = j;
                            lessVisited = visited;
                        }
                    }
                    pages[memory[org]].setFlag(false);
                    pages[memory[org]].setBlockNum(-1);
                    memory[org] = page.getPageNum();
                    page.setBlockNum(org);
                    page.setFlag(true);
                    page.setVisited(getCount++);
                    System.out.println("����ȱҳ������ҳ���û�����"+"��"+page.getPageNum()+"ҳ"+"�����ڴ��"+org);
                }
            }
        }
        System.out.println();
        for (int j = 0; j < memory.length; j++)
        {
            System.out.println("�ڴ��"+j+"��ǰ���ҳ��"+memory[j]);
        }

        System.out.println();
        System.out.println("320��ָ��ȫ��ִ�к��ȱҳ��Ϊ:"+((double)lossCount/320));
        
	}
	public static <T> void swap(int[] arr, int i, int j){
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    public static <T> void shuffle(int[] arr) {
        int length = arr.length;
        for ( int i = length; i > 0; i-- ){
            int randInd = rand.nextInt(i);
            swap(arr, randInd, i - 1);
        }
    }
}