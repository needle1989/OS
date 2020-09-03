package dynamicSim;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class BestFit
{
	public static void run() 
	{
		List<Chain> undisChain = new LinkedList<>();
        List<Chain> disChain = new LinkedList<>();
        undisChain.add(new Chain(0, 640, "δ����"));
        
		distribute("��ҵ1", 130, true, undisChain, disChain);
		distribute("��ҵ2", 60, true, undisChain, disChain);
		distribute("��ҵ3", 100, true, undisChain, disChain);
		distribute("��ҵ2", 60, false, undisChain, disChain);
		distribute("��ҵ4", 200, true, undisChain, disChain);
		distribute("��ҵ3", 100, false, undisChain, disChain);
		distribute("��ҵ1", 130, false, undisChain, disChain);
		distribute("��ҵ5", 140, true, undisChain, disChain);
		distribute("��ҵ6", 60, true, undisChain, disChain);
		distribute("��ҵ7", 50, true, undisChain, disChain);
		distribute("��ҵ6", 60, false, undisChain, disChain);
	}
	public static void distribute(String num,int lenth,boolean flag,List<Chain> undisChain,List<Chain> disChain) 
	{
		System.out.println("��ǰ������:");
		
		//�ͷŲ���
		
		if(!flag) {
			System.out.println("�ͷŴ�СΪ"+ lenth + "��" + num  );
			int address = 0, size = 0;
            for (int i = 0; i < disChain.size(); i++)
            {
                Chain chain = disChain.get(i);

                if (chain.getFlag().equals(num))
                {
                    address = chain.getAddress();
                    size = chain.getLenth();
                    disChain.remove(i);
                    break;
                }
            }

            boolean changed = false;
            for (int i = 0; i < undisChain.size(); i++)
            {
                Chain chain = undisChain.get(i);
                if (address < chain.getAddress())
                {
                    if (address + size == chain.getAddress())
                    {
                        chain.setAddress(address);
                        chain.setLenth(chain.getLenth() + size);
                        changed = true;
                    }
                    if (i - 1 >= 0)
                    {
                        Chain preItem = undisChain.get(i - 1);
                        if (preItem.getAddress() + preItem.getLenth() == chain.getAddress())
                        {
                            chain.setAddress(preItem.getAddress());
                            chain.setLenth(chain.getLenth() + preItem.getLenth());
                            undisChain.remove(i - 1);
                            changed = true;
                        } else if (preItem.getAddress() + preItem.getLenth() == address)//��ϲ��ø���ǰ��һ��
                        {
                            preItem.setLenth(preItem.getLenth() + size);
                            changed = true;
                        }
                    }
                    if (changed) break;
                    undisChain.add(i, new Chain(address, size, "δ����"));
                    changed = true;
                    break;
                }
            }

            if (!changed)
            {
                undisChain.add(undisChain.size(), new Chain(address, size, "δ����"));
            }
		}
		
		//�������
		
		else {
			System.out.println("�����СΪ"+ lenth + "��" + num  );
	        Optional<Chain> min = undisChain.stream()
	                .filter((Chain chain) -> chain.getLenth() >= lenth)
	                .min((o1, o2) -> o1.getLenth() - o2.getLenth());
	        Chain chain = min.get();
	        if (chain.getLenth() > lenth)
	        {
	            disChain.add(new Chain(chain.getAddress(), lenth, num));
	            chain.setAddress(chain.getAddress() + lenth);
	            chain.setLenth(chain.getLenth() - lenth);
	        } else if (chain.getLenth() == lenth)
	        {
	            disChain.add(new Chain(chain.getAddress(), lenth, num));
	            undisChain.remove(chain);
	        }
		}
		
		//������ͷŲ����������ӡ���
		
		System.out.println();
        System.out.println("���з�����:");
        for (Chain chain : undisChain)
        {
            System.out.println(chain);
        }
        System.out.println();
        System.out.println("�ѷ��������:");
        for (Chain chain : disChain)
        {
            System.out.println(chain);
        }
        System.out.println();
		
	}
}