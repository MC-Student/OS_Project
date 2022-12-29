import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Client
{
    public static ArrayList<Job> jobsToDo = new ArrayList<>(); //public so can be shared with its thread to master
    public static final Object jToDo_LOCK = new Object();
    public static ObjectOutputStream clientOutput = null;

    private static final Scanner keyboard = new Scanner(System.in);
    private static final String message = "Please enter a job type followed by a job ID, e.g. \"a 1234.\" Enter \"quit\" any time to exit.";

    public static void main(String[] args)
    {
        int port = getPort(args);

        Socket mcConnection = null;
        try
        {
            mcConnection = new Socket(args[0], port);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.out.println("Could not connect");
            System.exit(9);
        }

        try
        {
            clientOutput = new ObjectOutputStream(mcConnection.getOutputStream());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        Thread clientToMaster = new ClientToMaster(jobsToDo, jToDo_LOCK, clientOutput);
        //Thread clientFromMaster = new ClientFromMaster();

        clientToMaster.start();

        while (true)
        {
            System.out.println(message);
            String input = keyboard.nextLine();

            if (input.equalsIgnoreCase("quit"))
            {
                System.exit(3);
            }

            else
            {
                if (validInput(input))
                {
                    Job newJob = new Job(input);

                    synchronized (jToDo_LOCK)
                    {
                        jobsToDo.add(newJob);
                    }
                }
                else
                {
                    System.out.println("Invalid input, try again");
                }
            }
        }

        /*try
        {
            clientToMaster.join();
            clientFromMaster.join();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }*/

    }

    private static boolean validInput(String input)
    {
        boolean valid = false;

        String[] userInfo = input.split(" ");

        if (input.equals(" ") || input.equals(""))
        {
            System.out.println("User input null, please enter valid input: ");// null error message, ignore input
        }
        else
        {
            if (userInfo[0].equalsIgnoreCase("a") || userInfo[0].equalsIgnoreCase("b"))
            {
                try
                {
                    if (userInfo.length < 2)
                    {
                        System.out.println("Not enough information - enter a type and ID");
                    }
                    else
                    {
                        Integer.parseInt(userInfo[1]);
                        valid = true;
                    }
                }
                catch (NumberFormatException e)
                {
                    System.out.println("Invalid id given input: " + userInfo[1]);// invalid id error message, ignore input
                }
            }

            else
            {
                System.out.println("Invalid job type: " + userInfo[0] + "; must be of type A or B");
            }
        }

        return valid;
    }

    private static int getPort(String[] args)
    {
        int port = 0;

        if (Integer.parseInt(args[1]) == 1)
        {
            port = 6000;
        }

        else if (Integer.parseInt(args[1]) == 2)
        {
            port = 7000;
        }

        else
        {
            System.out.println("Invalid client ID entered, unable to connect to Master");
            System.exit(1);
        }
        return port;
    }
}
