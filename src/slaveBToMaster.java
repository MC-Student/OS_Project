import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class slaveBToMaster extends Thread
{
    ArrayList<Job> done;
    final Object lock;
    ObjectOutputStream toMaster;

    public slaveBToMaster(ArrayList<Job> done, Object lock, ObjectOutputStream toMaster)
    {
        this.done = done;
        this.lock = lock;
        this.toMaster = toMaster;
    }

    @Override
    public void run()
    {
        while (true)
        {
            boolean toDo = true;
            synchronized (lock)
            {
                if(done.isEmpty())
                {
                    toDo = false;
                }
            }

            if(toDo)
            {
                Job doneJ;
                synchronized (lock)
                {
                    doneJ = done.get(0);
                }

                try
                {
                    toMaster.writeObject(doneJ);
                }
                catch (IOException e)
                {
                    System.out.println("Slave B could not send back completed job " + doneJ.getId() + " to Master");
                    e.printStackTrace();
                }
            }
            else
            {
                try
                {
                    sleep(2);
                }
                catch (InterruptedException e)
                {
                    System.out.println("Could not sleep");
                    e.printStackTrace();
                }
            }
        }
    }
}
