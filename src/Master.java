import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Master
{
    ArrayList<Job> incomingJobs;
    Object incJobs_LOCK;
    ArrayList<Job> finishedJobs;
    Object finJobs_LOCK;

    public static void main(String[] args)
    {
        try
        {
            ServerSocket socketForClient = new ServerSocket(Integer.parseInt(args[1]));
            System.out.println("Master opened port " + args[1] + " for client1");

            Socket mClientSocket = socketForClient.accept();

            ServerSocket client2Socket = new ServerSocket(Integer.parseInt(args[2]));
            System.out.println("Master opened port " + args[2] + " for client2");

            ServerSocket slaveASocket = new ServerSocket(Integer.parseInt(args[3]));
            System.out.println("Master opened port " + args[3] + " for Slave A");

            ServerSocket slaveBSocket = new ServerSocket(Integer.parseInt(args[4]));
            System.out.println("Master opened port " + args[4] + " for Slave B");


        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        Thread mFromClient1 = new masterFromClient1();
        Thread mFromClient2 = new masterFromClient2();
        Thread mFromSlaveA = new masterFromSlaveA();
        Thread mFromSlaveB = new masterFromSlaveB();
        Thread mToClients = new masterToClients();

        mFromClient1.start();
        mFromClient2.start();
        mFromSlaveA.start();
        mFromSlaveB.start();
        mToClients.start();

        try
        {
            mFromClient1.join();
            mFromClient2.join();
            mFromSlaveA.join();
            mFromSlaveB.join();
            mToClients.join();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

}
