import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class ClientToMaster extends Thread
{
    ArrayList<Job> toDo;
    final Object j_LOCK;
    ObjectOutputStream oos;
    final Object stream_LOCK;

    public ClientToMaster(ArrayList<Job> toDo, Object lock, ObjectOutputStream oos, Object stream_LOCK)
    {
        this.toDo = toDo;
        this.j_LOCK = lock;
        this.oos = oos;
        this.stream_LOCK = stream_LOCK;
    }

    @Override
    public void run()
    {
        synchronized (j_LOCK)
        {
            if(toDo.isEmpty())
            {
                System.out.println("Empty list, thread sleeping");
                try
                {
                    sleep(5);
                }
                catch (InterruptedException e)
                {
                    System.out.println("Could not sleep");
                    e.printStackTrace();
                }
            }
            else
            {
                Job currJobToSend = toDo.get(0);
                toDo.remove(0);
                try
                {
                    oos.writeObject(currJobToSend);
                }
                catch (IOException e)
                {
                    System.out.println("Could not send job with ID " + currJobToSend.getId());
                    e.printStackTrace();
                }
            }
        }
    }
    /*
    * Takes in
    *   1. Client arraylist jobsToDo
    *   2. object lock jToDo_LOCK
    *   2. output stream
    *
    * sync on the list
    *   take in first element of list in client
    *       Job currJobToSendToMaster = jobsToDo.get(0);
    *
    * remove item from list
    *       jobsToDo.remove(0);
    *   send via output stream to master
    *       sendJob
    * */
}
