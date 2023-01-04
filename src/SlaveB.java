import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import static java.lang.Thread.sleep;

public class SlaveB
{
    static ArrayList<Job> toCompleteB = new ArrayList<>();
    static final Object toDoB_LOCK = new Object();
    static ArrayList<Job> completedJobsB = new ArrayList<>();
    static final Object doneB_LOCK = new Object();

    static ObjectInputStream sFromMaster;
    static ObjectOutputStream sToMaster;

    public static void main(String[] args)
    {
        Socket mSBConnection = null;

        try
        {
            mSBConnection = new Socket("127.0.0.1", 9500);
            System.out.println("SLAVE B: connected to Master");
        }
        catch (IOException e)
        {
            System.out.println("SLAVE B: could not connect to Master");
            e.printStackTrace();
        }

        try
        {
            sToMaster = new ObjectOutputStream(mSBConnection.getOutputStream());
        }
        catch (IOException e)
        {
            System.out.println("SLAVE B: could not get output stream from Master");
            e.printStackTrace();
        }

        try
        {
            sFromMaster = new ObjectInputStream(mSBConnection.getInputStream());
        }
        catch (IOException e)
        {
            System.out.println("SLAVE B: could not get input stream from Master");
            e.printStackTrace();
        }


        Thread fromMaster = new slaveBFromMaster(toCompleteB, toDoB_LOCK, sFromMaster);
        Thread toMaster = new slaveBToMaster(completedJobsB, doneB_LOCK, sToMaster);

        fromMaster.start();
        toMaster.start();

        while (true)
        {
            boolean jobsToDo = true;

            synchronized (toDoB_LOCK)
            {
                if (toCompleteB.isEmpty())
                {
                    jobsToDo = false;
                }
            }

            Job doNow;
            if (jobsToDo)
            {
                synchronized (toDoB_LOCK)
                {
                    doNow = toCompleteB.get(0);
                }
                System.out.println("SLAVE B: got next job of type" + doNow.getType() + " from to-do list");

                int sleepLength = secondsToSleep(doNow);

                try
                {
                    Thread.sleep(sleepLength);
                }
                catch (InterruptedException e)
                {
                    System.out.println("SLAVE B: did not sleep to complete job " + doNow.getId());
                    e.printStackTrace();
                }

                System.out.println("SLAVE B: woke up - job " + doNow.getId() + " is complete");

                synchronized (doneB_LOCK)
                {
                    completedJobsB.add(doNow);
                }
                synchronized (toDoB_LOCK)
                {
                    toCompleteB.remove(0);
                }

                System.out.println("SLAVE B: added job " + doNow.getId() + " to completed list");
            }

            else
            {
                try
                {
                    sleep(20);
                }
                catch (InterruptedException e)
                {
                    System.out.println("SLAVE B: Could not sleep");
                    e.printStackTrace();
                }
            }
        }
    }

    private static int secondsToSleep(Job currJob)
    {
        if (currJob.getType().equalsIgnoreCase("B"))
        {
            return 2000;
        }
        else
        {
            return 10000;
        }
    }
}
