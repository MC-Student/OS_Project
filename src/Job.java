import java.io.Serializable;

public class Job implements Serializable
{
    private int id;
    private String type;
    private int fromClient;

    public Job(String userInput, int fromClient)
    {
        String[] userInfo = userInput.split(" ");
        id = Integer.parseInt(userInfo [1]);
        type = userInfo[0];
        this.fromClient = fromClient;
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

    public int getClient()
    {
        return fromClient;
    }

    public void setClient(int fromClient)
    {
        this.fromClient = fromClient;
    }
}
