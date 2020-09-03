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
        System.out.println("生成320条指令的执行序列：");
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
            System.out.println("执行第"+seq[i]+"条指令");

            int pageNumber = seq[i] / 10;
            Page page = pages[pageNumber];
            
            //访问指令在内存中时，显示其物理地址
            
            if(page.getFlag())
            {
            	page.setVisited(getCount++);
                System.out.println("该指令在"+seq[i]/10+"页，位于内存块"+page.getBlockNum());
            }
            else
            {
            	//访问缺页且内存未满时，直接将页面调入
            	
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
                        System.out.println("访问缺页，将"+"第"+tmp+"页"+"调入内存块"+j);
                        break;
                    }
                }
                
                //访问缺页且需要调页时，使用LRU算法
                
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
                    System.out.println("访问缺页，进行页面置换，将"+"第"+page.getPageNum()+"页"+"调入内存块"+org);
                }
            }
        }
        System.out.println();
        for (int j = 0; j < memory.length; j++)
        {
            System.out.println("内存块"+j+"当前存放页面"+memory[j]);
        }

        System.out.println();
        System.out.println("320条指令全部执行后的缺页率为:"+((double)lossCount/320));
        
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