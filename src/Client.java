import java.io.*;
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
        args = new String[]{"127.0.0.1", "30121"};

        /*if (args.length != 2) {
            System.err.println(
                    "Usage: java EchoClient <host name> <port number>");
            System.exit(1);
        }*/

        String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);

        final Scanner keyboard = new Scanner(System.in);

        try (
                Socket clientSocket = new Socket(hostName, portNumber);
                ObjectOutputStream toHandler = new ObjectOutputStream(clientSocket.getOutputStream());
                ObjectInputStream fromHandler = new ObjectInputStream(clientSocket.getInputStream());
        )
        {
            System.out.println(fromHandler.readUTF());
            String userInput = keyboard.nextLine();
            toHandler.writeUTF(userInput);


            if (userInput.equalsIgnoreCase("quit"))
            {
                System.out.println("Closing this connection : " + clientSocket);
                clientSocket.close();
                System.out.println("Connection closed");
            }
            else
            {
                SimJob received = (SimJob) fromHandler.readObject();
                System.out.println("Going to submit job " + received.getJobID() + " to Master");
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
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
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

        if (userInput == null || userInput.equals(" ") || userInput.equals(""))
        {
            System.out.println("User input null, please enter valid input: ");// null error message, ignore input
        }
        else
        {
            String[] userInfo = userInput.split(" ");
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
                System.out.println("Invalid job type for input: " + userInfo[0]);// invalid type error message, ignore input
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
