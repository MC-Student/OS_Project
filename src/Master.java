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
    private int slaveAIncompleteA;
    private int slaveAIncompleteB;
    private int estWaitSlaveA = slaveAIncompleteA + slaveAIncompleteB;
    private int slaveBIncompleteA;
    private int slaveBIncompleteB;
    private int estWaitSlaveB = slaveBIncompleteA + slaveBIncompleteB;

    public static void main(String[] args)
    {
        args = new String[]{"30121"};        // Hard code in port number for testing purposes
        /*if (args.length != 1) {            // going to eventually take from user
            System.err.println("Error with port");
            System.exit(1);
        }*/

        int portNumber = Integer.parseInt(args[0]);

        ArrayList<SimJob> jobsToDo = new ArrayList<>();
        try (ServerSocket serverSocket = new ServerSocket(Integer.parseInt(args[0]));//master server socket 1 for client, will need for slaves
             Socket clientSocket = serverSocket.accept();//accepts connection to client
             PrintWriter toClient = new PrintWriter(clientSocket.getOutputStream(), true);// sent to client
             BufferedReader fromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))// reads from client
        )
        {
            String userInput;
            while ((userInput = fromClient.readLine()) != null)
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
