import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class slaveAFromMaster extends Thread
{
    ArrayList<Job> toDo;
    final Object lock;
    ObjectInputStream fromMaster;

    public slaveAFromMaster(ArrayList<Job> toDo, Object lock, ObjectInputStream fromMaster)
    {
        this.toDo = toDo;
        this.lock = lock;
        this.fromMaster = fromMaster;
    }

    @Override
    public void run()
    {
        while (true)
        {
            Job jobToDo = null;
            try
            {
                jobToDo = (Job) fromMaster.readObject();
            }
            catch (IOException | ClassNotFoundException e)
            {
                e.printStackTrace();
            }

            if (jobToDo != null)
            {
                System.out.println("Slave A received job " + jobToDo.getId() + " from Master");
                synchronized (lock)
                {
                    toDo.add(jobToDo);
                }
                System.out.println("Slave A added job " + jobToDo.getId() + " to its to-do list");
            }
        }
    }
}
