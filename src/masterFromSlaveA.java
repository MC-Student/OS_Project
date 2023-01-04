import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class masterFromSlaveA extends Thread
{
    ArrayList<Job> finishedJobs;
    final Object finJobs_lock;
    ObjectInputStream oisSA;

    public masterFromSlaveA(ArrayList<Job> finishedJobs, Object finJobs_lock, ObjectInputStream oisSA)
    {
        this.finishedJobs = finishedJobs;
        this.finJobs_lock = finJobs_lock;
        this.oisSA = oisSA;
    }

    @Override
    public void run()
    {
        while (true)
        {
            Job incoming = null;
            try
            {
                incoming = (Job) oisSA.readObject();
            }
            catch (IOException | ClassNotFoundException e)
            {
                e.printStackTrace();
            }

            if (incoming != null)
            {
                synchronized (finJobs_lock)
                {
                    finishedJobs.add(incoming);
                }
                System.out.println("FROM SLAVE A TO MASTER: 1. received completed job for Client # " + incoming.getClient() + " 2. added job with ID " + incoming.getId() + " to finished job list");
            }
        }
    }
}