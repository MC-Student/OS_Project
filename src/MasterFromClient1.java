import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class MasterFromClient1 extends Thread
{
    ArrayList<Job> incomingJobs;
    final Object ij_LOCK;
    ObjectInputStream ois;
    String client;

    public MasterFromClient1(ArrayList<Job> incomingJobs, Object ij_LOCK, ObjectInputStream ois, String client)
    {
        this.incomingJobs = incomingJobs;
        this.ij_LOCK = ij_LOCK;
        this.ois = ois;
        this.client = client;
    }

    @Override
    public void run()
    {
        while (true)
        {
            Job incoming = null;
            try
            {
                incoming = (Job) ois.readObject();
            }
            catch (IOException | ClassNotFoundException e)
            {
                e.printStackTrace();
            }

            if (incoming != null)
            {

                System.out.println(client + " read object");

                synchronized (ij_LOCK)
                {
                    System.out.println(client + " entered synchronized block");
                    incomingJobs.add(incoming);
                    System.out.println("Received job from " + client + " and added job with ID " + incoming.getId() + " to list");
                }
            }
        }
    }
}