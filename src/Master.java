import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Master
{
    /* INSTANCE VARIABLES:
                  total jobs received
                  current estimated wait time on slaveA =
                      total A jobs sent to slaveA not yet completed *2
                      +
                      total B jobs sent to slaveA not yet completed *10

                  current estimated wait time on slaveB =
                      total A jobs send to slaveB not yet completed *2
                      +
                      total B jobs send to slaveB not yet completed *10
        */
    private static int jobCount = 0;
    private int waitTimeA;
    private int waitTimeB;
    private int totalJobsReceived;
    private static int slaveAIncompleteA;
    private static int slaveAIncompleteB;
    private static int estWaitSlaveA = slaveAIncompleteA + slaveAIncompleteB;
    private static int slaveBIncompleteA;
    private static int slaveBIncompleteB;
    private static int estWaitSlaveB = slaveBIncompleteA + slaveBIncompleteB;

    public static void main(String[] args)
    {
        args = new String[]{"30121", "20202", "30404"};        // Hard code in port number for testing purposes
        /*if (args.length != 1) {            // going to eventually take from user
            System.err.println("Error with port");
            System.exit(1);
        }*/

        int portNumber = Integer.parseInt(args[0]);
        int portSlaveA = Integer.parseInt(args[1]);
        int portSlaveB = Integer.parseInt(args[2]);


        ArrayList<SimJob> jobsToDo = new ArrayList<>();

        try (ServerSocket serverSocket = new ServerSocket(portNumber);//master server socket 1 for client, will need for slaves
             ServerSocket slaveASocket = new ServerSocket(portSlaveA);//server socket for slave A
             ServerSocket slaveBSocket = new ServerSocket(portSlaveB);//server socket for slave B
             Socket clientSocket = serverSocket.accept();//accepts connection to client
             Socket socketA = slaveASocket.accept();//accepts connection to slave A
             Socket socketB = slaveBSocket.accept();//accepts connection to slave B
             PrintWriter toClient = new PrintWriter(clientSocket.getOutputStream(), true);// sent to client
             BufferedReader fromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));// reads from client
             PrintWriter toSlaveA = new PrintWriter(socketA.getOutputStream(), true);// sent to client
             BufferedReader fromSlaveA = new BufferedReader(new InputStreamReader(socketA.getInputStream()));// reads from client
             PrintWriter toSlaveB = new PrintWriter(socketB.getOutputStream(), true);// sent to client
             BufferedReader fromSlaveB = new BufferedReader(new InputStreamReader(socketB.getInputStream()))// reads from client
        )
        {
            String userInput;
            while ((userInput = fromClient.readLine()) != null)
            {
                getJobs(jobsToDo, toClient, userInput);

                int thisJobIndex = jobCount - 1;
                SimJob thisJob = jobsToDo.get(thisJobIndex);
                int thisJobID = thisJob.getJobID();

                if (Math.abs(estWaitSlaveA - estWaitSlaveB) < 10)
                {
                    String optimSlave = thisJob.getJobType();

                    System.out.println("Optimal slave for job " + thisJobID + " is Slave " + optimSlave);

                    thisJob.setSlave(optimSlave);

                    if (thisJob.getJobType().equalsIgnoreCase("a"))
                    {
                        System.out.println("Master sending Slave A job type A");
                        toSlaveA.println("a");
                    }
                    else if (thisJob.getJobType().equalsIgnoreCase("b"))
                    {
                        System.out.println("Master sending Slave B job type B");
                        toSlaveB.println("b");
                    }

                    System.out.println("Sent job " + thisJobID + " to optimal slave");
                }

                else
                {
                    String nonOptimSlave;

                    if(estWaitSlaveA < estWaitSlaveB)
                    {
                        nonOptimSlave = "a";
                    }
                    else
                    {
                        nonOptimSlave = "b";
                    }

                    thisJob.setSlave(nonOptimSlave);

                    if (thisJob.getSlave().equalsIgnoreCase("a"))
                    {
                        System.out.println("sending job " + thisJobID + "to Slave A - unoptimized");
                        toSlaveA.println(thisJob.getJobType());
                    }
                    else if (thisJob.getJobType().equalsIgnoreCase("b"))
                    {
                        System.out.println("sending job " + thisJobID + "to Slave B - unoptimized");
                        toSlaveB.println(thisJob.getJobType());
                    }

                    System.out.println("Sent job " + thisJobID + " to non-optimal slave (current quickest slave)");
                }
            }


        }

            /*
                Receive jobs from clients via socket and inform console
                    update total jobs received

                Check current wait time for each slave
                If |wait time on A - wait time on B| < 10
                    Assign job to its optimized slave and inform console
                Else
                    Assign job to slave with least wait time and inform console

                Get message back from slave that job completes and inform console
                inform client that job completed via socket
            */
        // send jobCount to client as test upon request

        catch (IOException e)
        {
            System.out.println("Exception caught when trying to listen on port " + portNumber + " or listening for a connection");
            System.out.println(e.getMessage());
        }
    }

    private static void getJobs(ArrayList<SimJob> jobsToDo, PrintWriter toClient, String userInput)
    {
        String[] jobInfo = userInput.split(" ");
        String jobType = jobInfo[0];
        int jobID = Integer.parseInt(jobInfo[1]);
        jobCount++;
        System.out.println(jobCount + " jobs now in process");
        jobsToDo.add(new SimJob(jobType, jobID));
        System.out.println("Now these jobs are in process: " + jobsToDo);
        toClient.println("job " + jobID + " received");
    }
    /*
    THREADING:
    The master will require at least
    one thread for each program with which it communicates.
     So to truly make your program robust you will probably want a
     separate thread for every direction in which a message can be sent
     All communication between threads will use shared memory
     objects with appropriate locking as discussed in the class lectures.
    */
}
