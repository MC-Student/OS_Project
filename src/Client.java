import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

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
        //TODO: Chana: fix if 1. wrong job type 2. wrong id type 3. no id 4. no job type 5. nothing
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
           System.out.println("Please enter a job type + id or type quit to exit: ");
            while(true){
                String userInput = stdIn.readLine();
                if(userInput.equals("quit")){//loop ends with "quit" or when hit 100, to keep things contained
                    break;
                }
                else if (validateInput(userInput))// validate the string
                {
                    sendJobToMaster(requestWriter, userInput);//send it to master
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

    private static void sendJobToMaster(PrintWriter requestWriter, String userInput)
    {
        requestWriter.println(userInput);
        System.out.println("Sent job " + userInput + " to user");
    }

    private static boolean validateInput(String userInput)
    {
        boolean valid = false;

        if(userInput == null || userInput.equals(" ") || userInput.equals("")){
            System.out.println("User input null, please enter valid input: ");// null error message, ignore input
        } else{
            String [] userInfo = userInput.split(" ");
            if (userInfo[0].equalsIgnoreCase("a") || userInfo[0].equalsIgnoreCase("b"))
            {
                try
                {
                    int id = Integer.parseInt(userInfo[1]);
                    valid = true;// id & type are valid, we send input to master
                }
                catch (NumberFormatException e)
                {
                    System.out.println("Invalid id for input: " + userInfo[1]);// invalid id error message, ignore input
                }
            }
            else
            {
                System.out.println("Invalid job type for input: " + userInfo[0] );// invalid type error message, ignore input
            }
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
