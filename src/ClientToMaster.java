import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class ClientToMaster extends Thread
{
    ArrayList<Job> toDo;
    final Object j_LOCK;
    ObjectOutputStream oos;

    public ClientToMaster(ArrayList<Job> toDo, Object lock, ObjectOutputStream oos)
    {
        this.toDo = toDo;
        this.j_LOCK = lock;
        this.oos = oos;
    }

    @Override
    public void run()
    {
        while(true)
        {
            boolean jobsToAdd = true;

            synchronized (j_LOCK)
            {
                if (toDo.isEmpty())
                {
                    jobsToAdd = false;
                }
            }

            if (jobsToAdd)
            {
                synchronized (j_LOCK)
                {
                    Job currJobToSend = toDo.get(0);
                    toDo.remove(0);
                    try
                    {
                        oos.writeObject(currJobToSend);
                        System.out.println("FROM CLIENT TO MASTER: Job " + currJobToSend.getId() + " sent");
                    }
                    catch (IOException e)
                    {
                        System.out.println("FROM CLIENT TO MASTER: Could not send job with ID " + currJobToSend.getId());
                        e.printStackTrace();
                    }
                }
            }

            else
            {
                try
                {
                    sleep(5);
                }
                catch (InterruptedException e)
                {
                    System.out.println("FROM CLIENT TO MASTER: Could not sleep");
                    e.printStackTrace();
                }
            }
        }
    }
}
