import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class masterToClients extends Thread
{
    ObjectOutputStream oosc1;
    ObjectOutputStream oosc2;
    ArrayList<Job> finishedJobs;
    final Object finJobs_lock;

    public masterToClients(ObjectOutputStream oosc1, ObjectOutputStream oosc2, ArrayList<Job> finishedJobs, Object finJobs_lock)
    {
        this.oosc1 = oosc1;
        this.oosc2 = oosc2;
        this.finishedJobs = finishedJobs;
        this.finJobs_lock = finJobs_lock;
    }

    @Override
    public void run()
    {
        while(true)
        {
            boolean toNotify = true;

            synchronized (finJobs_lock)
            {
                if (finishedJobs.isEmpty())
                {
                    toNotify = false;
                }
            }

            if(toNotify)
            {

                Job finished;
                int client;

                synchronized (finJobs_lock)
                {
                    finished = finishedJobs.get(0);
                    client = finished.getClient();
                }

                if (client == 1)
                {
                    synchronized (finJobs_lock)
                    {
                        try
                        {
                            oosc1.writeObject(finished);
                        }
                        catch (IOException e)
                        {
                            System.out.println("MASTER TO CLIENT: could not send complete job # " + finished.getId() + " to Client " + client);
                            e.printStackTrace();
                        }
                        finishedJobs.remove(0);
                    }
                }
                else if(client == 2)
                {
                    synchronized (finJobs_lock)
                    {
                        try
                        {
                            oosc2.writeObject(finished);
                        }
                        catch (IOException e)
                        {
                            System.out.println("MASTER TO CLIENT: could not send complete job # " + finished.getId() + " to Client " + client);
                            e.printStackTrace();
                        }
                        finishedJobs.remove(0);
                    }
                }
            }

            else
            {
                try
                {
                    sleep(20);
                }
                catch (InterruptedException e)
                {
                    System.out.println("MASTER TO CLIENT: Could not sleep");
                    e.printStackTrace();
                }
            }
        }
    }
}
