package dynamicSim;

public class Chain
{
    private int address;
    private int lenth;
    private String flag;

    public Chain(int address, int lenth, String flag)
    {
        this.address = address;
        this.lenth = lenth;
        this.flag = flag;
    }
    public String toString()
    {
        return flag+" "+"µØÖ·"+address+"K"+" "+"³¤¶È"+lenth+"k";
    }
    public int getAddress()
    {
        return address;
    }

    public void setAddress(int startAddress)
    {
        address = startAddress;
    }

    public int getLenth()
    {
        return lenth;
    }

    public void setLenth(int length)
    {
        this.lenth = length;
    }

    public String getFlag()
    {
        return flag;
    }

    public void setFlag(String flag)
    {
        this.flag = flag;
    }
}
