import dynamicSim.BestFit;
import dynamicSim.FirstFit;
import pageSim.LRU;

import java.util.Scanner;

public class mainMM
{
	public static void main(String[] args)
	{
		System.out.println("                     OS�ڴ����ϵͳ");
		System.out.println("                            by 1852141 �����");
		System.out.println();
		System.out.println("1   ģ�⶯̬�������䷽ʽ            2   ģ�������ҳ�洢����ʽ");
		Scanner sc = new Scanner(System.in);
		int key = 0;
		key = sc.nextInt();
		if(key==2) {
			LRU.run();
		}
		else {
			System.out.println("��ѡ��ʹ���㷨");
		    System.out.println("1   �״���Ӧ�㷨                          2   �����Ӧ�㷨");
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