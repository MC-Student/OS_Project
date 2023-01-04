import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Client
{
    public static ArrayList<Job> jobsToDo = new ArrayList<>();
    public static final Object jToDo_LOCK = new Object();
    public static ObjectOutputStream clientOutput = null;
    public static ObjectInputStream clientInput = null;

    private static final Scanner keyboard = new Scanner(System.in);
    private static final String message = "USER: Please enter a job type followed by a job ID, e.g. \"a 1234.\" Enter \"quit\" any time to exit.";

    public static void main(String[] args)
    {
        String host = "127.0.0.1";
        int port = getPort(args);

        Socket mcConnection = null;
        try
        {
            mcConnection = new Socket(host, port);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.out.println("CLIENT could not connect");
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
        try
        {
            clientInput = new ObjectInputStream(mcConnection.getInputStream());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        Thread clientToMaster = new ClientToMaster(jobsToDo, jToDo_LOCK, clientOutput);
        Thread clientFromMaster = new ClientFromMaster(clientInput);

        clientToMaster.start();
        clientFromMaster.start();

        while (true)
        {
            System.out.println(message);
            String input = keyboard.nextLine();

            if (input.equalsIgnoreCase("ERROR: quit"))
            {
                System.exit(3);
            }

            else
            {
                if (validInput(input))
                {
                    Job newJob = new Job(input, Integer.parseInt(args[1]));

                    synchronized (jToDo_LOCK)
                    {
                        jobsToDo.add(newJob);
                    }
                }
            }
        }
    }

    private static boolean validInput(String input)
    {
        boolean valid = false;

        String[] userInfo = input.split(" ");

        if (input.equals(" ") || input.equals(""))
        {
            System.out.println("ERROR: User input null, please enter valid input: ");// null error message, ignore input
        }
        else
        {
            if (userInfo[0].equalsIgnoreCase("a") || userInfo[0].equalsIgnoreCase("b"))
            {
                try
                {
                    if (userInfo.length < 2)
                    {
                        System.out.println("ERROR: Not enough information - enter a type and ID");
                    }
                    else
                    {
                        Integer.parseInt(userInfo[1]);
                        valid = true;
                    }
                }
                catch (NumberFormatException e)
                {
                    System.out.println("ERROR: Invalid id given input: " + userInfo[1]);// invalid id error message, ignore input
                }
            }

            else
            {
                System.out.println("ERROR: Invalid job type: " + userInfo[0] + "; must be of type A or B");
            }
        }

        return valid;
    }

    private static int getPort(String[] args)
    {
        int port = 0;

        if (Integer.parseInt(args[0]) == 1)
        {
            port = 6000;
        }

        else if (Integer.parseInt(args[0]) == 2)
        {
            port = 7000;
        }

        else
        {
            System.out.println("ERROR: Invalid client ID entered, unable to connect to Master");
            System.exit(1);
        }
        return port;
    }
}
