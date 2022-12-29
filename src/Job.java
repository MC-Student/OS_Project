import java.io.Serializable;

public class Job implements Serializable
{
    private int id;
    private String type;
    private int slaveToSendTo;

    public Job(String userInput)
    {
        String[] userInfo = userInput.split(" ");
        id = Integer.parseInt(userInfo [1]);
        type = userInfo[0];
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }
}
