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
        undisChain.add(new Chain(0, 640, "未分配"));
        
		distribute("作业1", 130, true, undisChain, disChain);
		distribute("作业2", 60, true, undisChain, disChain);
		distribute("作业3", 100, true, undisChain, disChain);
		distribute("作业2", 60, false, undisChain, disChain);
		distribute("作业4", 200, true, undisChain, disChain);
		distribute("作业3", 100, false, undisChain, disChain);
		distribute("作业1", 130, false, undisChain, disChain);
		distribute("作业5", 140, true, undisChain, disChain);
		distribute("作业6", 60, true, undisChain, disChain);
		distribute("作业7", 50, true, undisChain, disChain);
		distribute("作业6", 60, false, undisChain, disChain);
	}
	public static void distribute(String num,int lenth,boolean flag,List<Chain> undisChain,List<Chain> disChain) 
	{
		System.out.println("当前的请求:");
		
		//释放操作
		
		if(!flag) {
			System.out.println("释放大小为"+ lenth + "的" + num  );
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
                        } else if (preItem.getAddress() + preItem.getLenth() == address)//这合并该个和前面一个
                        {
                            preItem.setLenth(preItem.getLenth() + size);
                            changed = true;
                        }
                    }
                    if (changed) break;
                    undisChain.add(i, new Chain(address, size, "未分配"));
                    changed = true;
                    break;
                }
            }

            if (!changed)
            {
                undisChain.add(undisChain.size(), new Chain(address, size, "未分配"));
            }
		}
		
		//分配操作
		
		else {
			System.out.println("分配大小为"+ lenth + "的" + num  );
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
		
		//分配或释放操作结束后打印结果
		
		System.out.println();
        System.out.println("空闲分区链:");
        for (Chain chain : undisChain)
        {
            System.out.println(chain);
        }
        System.out.println();
        System.out.println("已分配分区链:");
        for (Chain chain : disChain)
        {
            System.out.println(chain);
        }
        System.out.println();
		
	}
}