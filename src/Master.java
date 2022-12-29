import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Master
{
    static ArrayList<Job> incomingJobs = new ArrayList<>();
    final static Object incJobs_LOCK = new Object();
    static ArrayList<Job> finishedJobs;
    final static Object finJobs_LOCK = new Object();
    static ObjectInputStream ois1 = null;
    static ObjectInputStream ois2 = null;

    public static void main(String[] args)
    {
        try
        {
            ServerSocket client1Socket = new ServerSocket(6000);
            System.out.println("Master opened port 6000 for client1");
            Socket mClientSocket1 = client1Socket.accept();
            System.out.println("Made connection at port 6000");
            ois1 = new ObjectInputStream(mClientSocket1.getInputStream());

            ServerSocket client2Socket = new ServerSocket(7000);
            System.out.println("Master opened port 7000 for client2");
            Socket mClientSocket2 = client2Socket.accept();
            System.out.println("Made connection at port 7000");
            ois2 = new ObjectInputStream(mClientSocket2.getInputStream());


            ServerSocket slaveASocket = new ServerSocket(9000);
            System.out.println("Master opened port 9000 for Slave A");

            ServerSocket slaveBSocket = new ServerSocket(9500);
            System.out.println("Master opened port 9500 for Slave B");

            Thread mFromClient1 = new MasterFromClient1(incomingJobs, incJobs_LOCK, ois1, "Client 1");
            Thread mFromClient2 = new MasterFromClient1(incomingJobs, incJobs_LOCK, ois2, "Client 2");
            Thread mFromSlaveA = new masterFromSlaveA();
            Thread mFromSlaveB = new masterFromSlaveB();
            Thread mToClients = new masterToClients();

            mFromClient1.start();
            mFromClient2.start();
            mFromSlaveA.start();
            mFromSlaveB.start();
            mToClients.start();

            mFromClient1.join();
            System.out.println("Joined 1");
            mFromClient2.join();
            System.out.println("Joined 2");
            mFromSlaveA.join();
            System.out.println("Joined 3");
            mFromSlaveB.join();
            System.out.println("Joined 4");
            mToClients.join();
            System.out.println("Joined 5");
        }
        catch (InterruptedException | IOException e)
        {
            e.printStackTrace();
        }
    }

}
