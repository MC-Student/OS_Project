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
                System.out.println("Slave A could not send back completed job " + doneJ.getId() + " to Master");
                e.printStackTrace();
            }
        }
    }
}
