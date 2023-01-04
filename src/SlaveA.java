import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import static java.lang.Thread.sleep;

public class SlaveA
{
    static ArrayList<Job> toCompleteA = new ArrayList<>();
    static final Object toDoA_LOCK = new Object();
    static ArrayList<Job> completedJobsA = new ArrayList<>();
    static final Object doneA_LOCK = new Object();

    static ObjectInputStream sFromMaster;
    static ObjectOutputStream sToMaster;

    public static void main(String[] args)
    {
        Socket mSAConnection = null;

        try
        {
            mSAConnection = new Socket("127.0.0.1", 9200);
            System.out.println("SLAVE A: connected to Master");
        }
        catch (IOException e)
        {
            System.out.println("SLAVE A: could not connect to Master");
            e.printStackTrace();
        }

        try
        {
            sToMaster = new ObjectOutputStream(mSAConnection.getOutputStream());
        }
        catch (IOException e)
        {
            System.out.println("SLAVE A: could not get output stream from Master");
            e.printStackTrace();
        }

        try
        {
            sFromMaster = new ObjectInputStream(mSAConnection.getInputStream());
        }
        catch (IOException e)
        {
            System.out.println("SLAVE A: could not get input stream from Master");
            e.printStackTrace();
        }

        Thread fromMaster = new slaveAFromMaster(toCompleteA, toDoA_LOCK, sFromMaster);
        Thread toMaster = new slaveAToMaster(completedJobsA, doneA_LOCK, sToMaster);

        fromMaster.start();
        toMaster.start();

        while (true)
        {
            boolean jobsToDo = true;

            synchronized (toDoA_LOCK)
            {
                if (toCompleteA.isEmpty())
                {
                    jobsToDo = false;
                }
            }

            Job doNow;
            if (jobsToDo)
            {
                synchronized (toDoA_LOCK)
                {
                    doNow = toCompleteA.get(0);
                }
                System.out.println("SLAVE A: got next job of type " + doNow.getType() + " from to-do list");

                int sleepLength = secondsToSleep(doNow);

                try
                {
                    sleep(sleepLength);
                }
                catch (InterruptedException e)
                {
                    System.out.println("SLAVE A: did not sleep to complete job " + doNow.getId());
                    e.printStackTrace();
                }

                System.out.println("SLAVE A: woke up - job " + doNow.getId() + " is complete");

                synchronized (doneA_LOCK)
                {
                    completedJobsA.add(doNow);
                }
                synchronized (toDoA_LOCK)
                {
                    toCompleteA.remove(0);
                }

                System.out.println("SLAVE A: added job " + doNow.getId() + " to completed list");
            }

            else
            {
                try
                {
                    sleep(20);
                }
                catch (InterruptedException e)
                {
                    System.out.println("SLAVE A: Could not sleep");
                    e.printStackTrace();
                }
            }
        }
    }

    private static int secondsToSleep(Job currJob)
    {
        if (currJob.getType().equalsIgnoreCase("A"))
        {
            return 2000;
        }
        else
        {
            return 10000;
        }
    }
}
