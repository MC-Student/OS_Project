import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client
{
    /*
              Instance Variable:
                      jobCounter
              Create a job based on user input
                  Tell user job request has been received by client
                  Pass user input and incremented jobCounter as String and int to new SimJob
                  Tell user that job has been created
              Send job to master
              Tell user job was sent to master "server"
              When confirm job is complete, tell user
              */
    private int jobCounter;

    public static void main(String[] args)
    {
        // Hardcode in IP and Port here if required
        args = new String[]{"127.0.0.1", "30121"};

        /*if (args.length != 2) {
            System.err.println(
                    "Usage: java EchoClient <host name> <port number>");
            System.exit(1);
        }*/

        String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);

        try (
                Socket clientSocket = new Socket(hostName, portNumber);
                PrintWriter requestWriter = // stream to write text requests to server
                        new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader responseReader = // stream to read text response from server
                        new BufferedReader(
                                new InputStreamReader(clientSocket.getInputStream()));
                BufferedReader stdIn = // standard input stream to get user's requests
                        new BufferedReader(
                                new InputStreamReader(System.in))
        )
        {
            //https://stackoverflow.com/questions/49709070/java-application-that-will-listen-for-user-input-in-a-loop-without-pausing

            while(true)
            {
                //send job one to the master
                String userInput = stdIn.readLine();
                String[] userInfo = userInput.split(" ");

                if (validateInput(userInfo))
                {
                    sendJobToMaster(requestWriter, userInput, userInfo);
                }
                else
                {
                    break;
                }
            }
        }
        catch (UnknownHostException e)
        {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        }
        catch (IOException e)
        {
            System.err.println("Couldn't get I/O for the connection to " +
                    hostName);
            System.exit(1);
        }
    }

    private static void sendJobToMaster(PrintWriter requestWriter, String userInput, String[] userInfo)
    {
        requestWriter.println(userInput);
        System.out.println("Sent job type " + userInfo[0] + " with ID " + userInfo[1]);
    }

    private static boolean validateInput(String[] userInfo)
    {
        boolean valid = false;

        if (userInfo[0].equalsIgnoreCase("a") || userInfo[0].equalsIgnoreCase("b"))
        {
            try
            {
                int id = Integer.parseInt(userInfo[1]);
                valid = true;
            }
            catch (NumberFormatException e)
            {
                System.out.println("The id is not valid: " + e);
            }
        }
        else
        {
            System.out.println("Invalid job type: " + userInfo[0] + "; must be of type A or B");
        }

        return valid;
    }



    /*
    Clients are going to connect directly to the master and submit jobs of either type.
    The clients submission should include
    1. the type,
    2. an ID number that will be used to identify the job throughout the system.
    */

    /*
    CLASS: SimJob holds the job we are sending between client/server
    //Send jobs to Master
    //Confirm submission of jobs
    //Receive message that job is complete
    //Communicate completion to user
    //Communicate each step of process as it is happening to user
    */
}
