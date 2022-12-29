import java.io.ObjectInputStream;
import java.util.ArrayList;

public class MasterFromClient2 extends Thread
{
    public MasterFromClient2(ArrayList<Job> incomingJobs, Object incJobs_lock, ObjectInputStream ois2)
    {

    }
    /*
     * Takes in:
     *   1. masterInputStream
     *   2. Arraylist of incomingJobs from client
     *   3. lock on arraylist
     *
     * waits for jobs from client2
     *
     *   when gets job, sync and add to list
     * */
}
