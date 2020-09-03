import dynamicSim.BestFit;
import dynamicSim.FirstFit;
import pageSim.LRU;

import java.util.Scanner;

public class mainMM
{
	public static void main(String[] args)
	{
		System.out.println("                     OS内存管理系统");
		System.out.println("                            by 1852141 李德涛");
		System.out.println();
		System.out.println("1   模拟动态分区分配方式            2   模拟请求调页存储管理方式");
		Scanner sc = new Scanner(System.in);
		int key = 0;
		key = sc.nextInt();
		if(key==2) {
			LRU.run();
		}
		else {
			System.out.println("请选择使用算法");
		    System.out.println("1   首次适应算法                          2   最佳适应算法");
		    key = sc.nextInt();
		    if(key ==1) {
		    	FirstFit.run();
		    }
		    else {
		    	BestFit.run();
		    }
		}
		sc.close();
	}
}