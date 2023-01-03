import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class slaveAToMaster extends Thread
{
    ArrayList<Job> done;
    final Object lock;
    ObjectOutputStream toMaster;

    public slaveAToMaster(ArrayList<Job> done, Object lock, ObjectOutputStream toMaster)
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
                    System.out.println("Slave A sent back to Master - completed job " + doneJ.getId());
                }
                catch (IOException e)
                {
                    System.out.println("Slave A could not send back completed job " + doneJ.getId() + " to Master");
                    e.printStackTrace();
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
                    System.out.println("Could not sleep");
                    e.printStackTrace();
                }
            }
        }
    }
}
