package pageSim;

public class Page
{
    private int pageNum;
    private int blockNum;
    private boolean flag;
    private int visited;

    public Page(int pageNum)
    {
        this.pageNum = pageNum;
        blockNum = -1;
        flag = false;
        visited = -1;
    }

    public int getPageNum()
    {
        return pageNum;
    }

    public void setPageNum(int pageNum)
    {
        this.pageNum = pageNum;
    }

    public int getBlockNum()
    {
        return blockNum;
    }

    public void setBlockNum(int blockNum)
    {
        this.blockNum = blockNum;
    }

    public boolean getFlag()
    {
        return flag;
    }

    public void setFlag(boolean flag)
    {
        this.flag = flag;
    }

    public int getVisited()
    {
        return visited;
    }

    public void setVisited(int visited)
    {
        this.visited = visited;
    }
}
