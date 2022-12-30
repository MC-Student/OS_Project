import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class SlaveA
{
    public static void main(String[] args)
    {
        ArrayList<Job> toCompleteA = new ArrayList<>();
        final Object toDoA_LOCK = new Object();
        ArrayList<Job> completedJobsA = new ArrayList<>();
        final Object doneA_LOCK = new Object();

        Socket mSAConnection = null;
        ObjectInputStream sFromMaster = null;
        ObjectOutputStream sToMaster = null;

        try
        {
            mSAConnection = new Socket("127.0.0.1", 9000);
            System.out.println("Slave A connected to Master");
        }
        catch (IOException e)
        {
            System.out.println("Slave A could not connect to Master");
            e.printStackTrace();
        }

        try
        {
            sFromMaster = new ObjectInputStream(mSAConnection.getInputStream());
        }
        catch (IOException e)
        {
            System.out.println("Slave A could not get input stream from Master");
            e.printStackTrace();
        }
        try
        {
            sToMaster = new ObjectOutputStream(mSAConnection.getOutputStream());
        }
        catch (IOException e)
        {
            System.out.println("Slave A could not get output stream from Master");
            e.printStackTrace();
        }


        Thread fromMaster = new slaveAFromMaster(toCompleteA, toDoA_LOCK, sFromMaster);
        Thread toMaster = new slaveAToMaster(completedJobsA, doneA_LOCK, sToMaster);

        fromMaster.start();
        toMaster.start();

        while(true)
        {
            Job doNow;

            synchronized (toDoA_LOCK)
            {
                doNow = toCompleteA.get(0);
            }
            System.out.println("Slave A got next job from to-do list");

            int sleepLength = secondsToSleep(doNow);

            try
            {
                Thread.sleep(sleepLength);
            }
            catch (InterruptedException e)
            {
                System.out.println("Slave A did not sleep to complete job " + doNow.getId());
                e.printStackTrace();
            }

            System.out.println("Slave A woke up - job " + doNow.getId() + " is complete");

            synchronized (doneA_LOCK)
            {
                completedJobsA.add(doNow);
            }

            System.out.println("Slave A added job " + doNow.getId() + " to completed list");
        }

    }

    private static int secondsToSleep(Job currJob)
    {
        if (currJob.getType().equalsIgnoreCase("A"))
        {
            return 2;
        }
        else
        {
            return 10;
        }
    }
}
