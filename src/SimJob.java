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
    
    public SimJob(String type, int id)
    {
        jobType = type;
        jobID = id;
    }

    public int getJobID()
    {
        return jobID;
    }

    public String getJobType()
    {
        return jobType;
    }

}
