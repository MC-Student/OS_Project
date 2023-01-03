import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class masterFromSlaveB extends Thread
{
    ArrayList<Job> finishedJobs;
    final Object finJobs_lock;
    ObjectInputStream oisSB;

    public masterFromSlaveB(ArrayList<Job> finishedJobs, Object finJobs_lock, ObjectInputStream oisSB)
    {
        this.finishedJobs = finishedJobs;
        this.finJobs_lock = finJobs_lock;
        this.oisSB = oisSB;
    }

    @Override
    public void run()
    {
        while (true)
        {
            Job incoming = null;
            try
            {
                incoming = (Job) oisSB.readObject();
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
                //TODO: this line prints infinitely, seems like job is never removed from list??? Not possible though, seems that it just keeps printing
                System.out.println("Master received completed job from " + incoming.getClient() + " and added job with ID " + incoming.getId() + " to finished job list");
            }
        }
    }
}
