import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class SlaveB {

    public static void main(String[] args)
    {
        args = new String[]{"127.0.0.1", "30404"};

        String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);

        try (
                Socket slaveB = new Socket(hostName, portNumber);
                PrintWriter toMaster = // stream to write text requests to server
                        new PrintWriter(slaveB.getOutputStream(), true);
                BufferedReader fromMaster = // stream to read text response from server
                        new BufferedReader(
                                new InputStreamReader(slaveB.getInputStream()));
        )
        {
            System.out.println("This is Slave B connected to the Master");

            String jobReceived;

            while ((jobReceived = fromMaster.readLine()) != null)
            {
                if (jobReceived.equalsIgnoreCase("a"))
                {
                    toMaster.println("Slave B recognized assignment of type A");
                }
                else if (jobReceived.equalsIgnoreCase("b"))
                {
                    toMaster.println("Slave B recognized assignment of type B");
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
    //Receive jobs assigned to me
    //Determine if job is type A or B
    //Sleep type B for 2 seconds
    //Sleep type A for 10 seconds
    //Remove job when complete?
    //As soon as job finishes sleeping, send message to Master that job has completed


}
