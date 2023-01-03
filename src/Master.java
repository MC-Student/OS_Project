import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Master
{
    static ArrayList<Job> incomingJobs = new ArrayList<>();
    final static Object incJobs_LOCK = new Object();
    static ArrayList<Job> finishedJobs = new ArrayList<>();
    final static Object finJobs_LOCK = new Object();
    static int totalTimeA = 0;
    final static Object timeA_LOCK = new Object();
    static int totalTimeB = 0;
    final static Object timeB_LOCK = new Object();

    static ObjectInputStream oisc1;
    static ObjectInputStream oisc2;
    static ObjectInputStream oisSA;
    static ObjectInputStream oisSB;

    static ObjectOutputStream oosc1;
    static ObjectOutputStream oosc2;
    static ObjectOutputStream oosSA;
    static ObjectOutputStream oosSB = null;


    public static void main(String[] args)
    {
        try
        {
            ServerSocket client1Socket = new ServerSocket(6000);
            System.out.println("Master opened port 6000 for client 1");
            Socket mClientSocket1 = client1Socket.accept();
            System.out.println("Made connection at port 6000");
            oisc1 = new ObjectInputStream(mClientSocket1.getInputStream());
            oosc1 = new ObjectOutputStream(mClientSocket1.getOutputStream());

            ServerSocket client2Socket = new ServerSocket(7000);
            System.out.println("Master opened port 7000 for client 2");
            Socket mClientSocket2 = client2Socket.accept();
            System.out.println("Made connection at port 7000");
            oisc2 = new ObjectInputStream(mClientSocket2.getInputStream());
            oosc2 = new ObjectOutputStream(mClientSocket2.getOutputStream());

            ServerSocket slaveASocket = new ServerSocket(9200);
            System.out.println("Master opened port 9200 for Slave A");
            Socket mSlaveASocket = slaveASocket.accept();
            System.out.println("Made connection at port 9200");
            oisSA = new ObjectInputStream(mSlaveASocket.getInputStream());
            oosSA = new ObjectOutputStream(mSlaveASocket.getOutputStream());

            ServerSocket slaveBSocket = new ServerSocket(9500);
            System.out.println("Master opened port 9500 for Slave B");
            Socket mSlaveBSocket = slaveBSocket.accept();
            System.out.println("Made connection at port 9500");
            oisSB = new ObjectInputStream(mSlaveBSocket.getInputStream());
            oosSB = new ObjectOutputStream(mSlaveBSocket.getOutputStream());

            Thread mFromClient1 = new MasterFromClient(incomingJobs, incJobs_LOCK, oisc1, "Client 1");
            Thread mFromClient2 = new MasterFromClient(incomingJobs, incJobs_LOCK, oisc2, "Client 2");
            Thread balanceLoad = new BalanceLoad(totalTimeA, timeA_LOCK, totalTimeB, timeB_LOCK, incomingJobs, incJobs_LOCK, oosSA, oosSB);
            Thread mFromSlaveA = new masterFromSlaveA(finishedJobs, finJobs_LOCK, oisSA);
            Thread mFromSlaveB = new masterFromSlaveB(finishedJobs, finJobs_LOCK, oisSB);
            Thread mToClients = new masterToClients(oosc1, oosc2, finishedJobs, finJobs_LOCK);

            System.out.println("created threads");

            mFromClient1.start();
            mFromClient2.start();
            balanceLoad.start();
            mFromSlaveA.start();
            mFromSlaveB.start();
            mToClients.start();

            /*mFromClient1.join();
            System.out.println("Joined 1");
            mFromClient2.join();
            System.out.println("Joined 2");
            mFromSlaveA.join();
            System.out.println("Joined 3");
            mFromSlaveB.join();
            System.out.println("Joined 4");
            mToClients.join();
            System.out.println("Joined 5");*/
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

}
