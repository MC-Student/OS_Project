public class Job
{
    private int id;
    private String type;
    private int slaveToSendTo;
    public Job(String userInput){
        String[] userInfo = userInput.split(" ");
        /*
        depending on how we want it to be either
        * this.id =userInfo[0]
        * OR
        * this.id = userInfo[1]
        *
        * */
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
