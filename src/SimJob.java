public class SimJob
{
    /*
        Clients are going to connect directly to the master and submit jobs of either type.
        The clients submission should include
        1. the type,
        2. an ID number that will be used to identify the job throughout the system.
    */
    //TODO: Process should spawn threads as children
    
    private int jobID;
    private String jobType;
    private String slave;
    
    public SimJob(String type, int id)
    {
        jobType = type;
        jobID = id;
        slave = null;
    }

    public int getJobID()
    {
        return jobID;
    }

    public String getJobType()
    {
        return jobType;
    }

    public void setSlave(String slaveType)
    {
        slave = slaveType;
    }

    public String getSlave()
    {
        return slave;
    }
}
