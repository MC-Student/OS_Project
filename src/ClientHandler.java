import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler extends Thread
{
    final ObjectInputStream fromClient;
    final ObjectOutputStream toClient;
    final Socket connectToClient;

    public ClientHandler(ObjectInputStream fromClient, ObjectOutputStream toClient, Socket connectToClient)
    {
        this.fromClient = fromClient;
        this.toClient = toClient;
        this.connectToClient = connectToClient;
    }

    @Override
    public void run()
    {

        String received;

        while (true)
        {
            try
            {
                // Ask user what he wants
                toClient.writeUTF("To submit a job, enter job type (A or B) followed by a space and job ID (number). Type quit to exit.");
                toClient.flush();

                while (true)
                {
                    // receive the answer from client
                    received = fromClient.readUTF();

                    if (received.equalsIgnoreCase("quit"))
                    {
                        System.out.println("Client " + this.connectToClient + " sends exit...");
                        System.out.println("Closing this connection.");
                        this.connectToClient.close();
                        System.out.println("Connection closed");
                        break;
                    }

                    // write job object to output stream if the input is valid

                    else if (validateInput(received))
                    {
                        toClient.writeUTF(received);
                        toClient.flush();
                    }

                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

            try
            {
                // closing resources
                this.fromClient.close();
                this.toClient.close();

            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

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
}
