import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class BalanceLoad extends Thread
{
    int totalTimeA;
    final Object timeA_LOCK;
    int totalTimeB;
    final Object timeB_LOCK;
    ArrayList<Job> jobsToAssign;
    final Object jobList_LOCK;
    ObjectOutputStream toSlaveA;
    ObjectOutputStream toSlaveB;

    public BalanceLoad(int totalTimeA, Object timeA_lock, int totalTimeB, Object timeB_lock, ArrayList<Job> jobsToAssign,
                       Object jobList_LOCK, ObjectOutputStream toSlaveA, ObjectOutputStream toSlaveB)
    {
        this.totalTimeA = totalTimeA;
        this.timeA_LOCK = timeA_lock;
        this.totalTimeB = totalTimeB;
        this.timeB_LOCK = timeB_lock;
        this.jobsToAssign = jobsToAssign;
        this.jobList_LOCK = jobList_LOCK;
        this.toSlaveA = toSlaveA;
        this.toSlaveB = toSlaveB;
    }

    @Override
    public void run()
    {
        while(true)
        {
            boolean jobsToDo = true;

            synchronized (jobList_LOCK)
            {
                if (jobsToAssign.isEmpty())
                {
                    jobsToDo = false;
                }
            }

            if(jobsToDo)
            {
                String jobType;

                synchronized (jobList_LOCK)
                {
                    jobType = jobsToAssign.get(0).getType();
                }

                String optimized;
                int optimizedTime;
                int nonOptTime;

                if (jobType.equalsIgnoreCase("a"))
                {
                    optimized = "A";
                    optimizedTime = totalTimeA;
                    nonOptTime = totalTimeB;
                }
                else
                {
                    optimized = "B";
                    optimizedTime = totalTimeB;
                    nonOptTime = totalTimeA;
                }

                if ((optimizedTime + 2) <= (nonOptTime + 10))
                {
                    sendToOptimized(optimized);
                }
                else
                {
                    sendToNonOptimized(optimized);
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

    private void sendToOptimized(String optimized)
    {
        if(optimized.equalsIgnoreCase("A"))
        {
            synchronized (timeA_LOCK)
            {
                totalTimeA += 2;
                System.out.println("Added 2 ms to total sleep time of slave A");
            }

            Job sending;

            synchronized (jobList_LOCK)
            {
                sending = jobsToAssign.get(0);
                jobsToAssign.remove(0);
                System.out.println("Removed job to send from original list");
            }

            try
            {
                toSlaveA.writeObject(sending);
                System.out.println("Sent job " + sending.getId() + " to optimized slave");
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        else
        {
            synchronized (timeB_LOCK)
            {
                totalTimeB += 2;
                System.out.println("Added 2 ms to total sleep time of slave B");
            }

            Job sending;

            synchronized (jobList_LOCK)
            {
                sending = jobsToAssign.get(0);
                jobsToAssign.remove(0);
                System.out.println("Removed job to send from original list");
            }

            try
            {
                toSlaveB.writeObject(sending);
                System.out.println("Sent job " + sending.getId() + " to optimized slave");
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    private void sendToNonOptimized(String optimized)
    {
        if(optimized.equalsIgnoreCase("A"))
        {
            synchronized (timeB_LOCK)
            {
                totalTimeB += 10;
                System.out.println("Added 10 ms to total sleep time of slave B");
            }

            Job sending;

            synchronized (jobList_LOCK)
            {
                sending = jobsToAssign.get(0);
                jobsToAssign.remove(0);
                System.out.println("Removed job to send from original list");
            }

            try
            {
                toSlaveB.writeObject(sending);
                System.out.println("Sent job " + sending.getId() + " to NON-optimized slave");
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        else
        {
            synchronized (timeA_LOCK)
            {
                totalTimeA += 10;
                System.out.println("Added 10 ms to total sleep time of slave A");
            }

            Job sending;

            synchronized (jobList_LOCK)
            {
                sending = jobsToAssign.get(0);
                jobsToAssign.remove(0);
                System.out.println("Removed job to send from original list");
            }

            try
            {
                toSlaveA.writeObject(sending);
                System.out.println("Sent job " + sending.getId() + " to NON-optimized slave");
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}