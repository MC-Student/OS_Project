import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Queue;

public class SlaveA
{

    public static void main(String[] args)
    {
        args = new String[]{"127.0.0.1", "20202"};

        String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);

        try (
                Socket slaveA = new Socket(hostName, portNumber);
                PrintWriter toMaster = // stream to write text requests to server
                        new PrintWriter(slaveA.getOutputStream(), true);
                BufferedReader fromMaster = // stream to read text response from server
                        new BufferedReader(
                                new InputStreamReader(slaveA.getInputStream()));
        )
        {
            System.out.println("This is Slave A connected to the Master");


            String jobReceived;

            while ((jobReceived = fromMaster.readLine()) != null)
            {
                if (jobReceived.equalsIgnoreCase("a"))
                {
                    toMaster.println("Slave A recognized assignment of type A");
                }
                else if (jobReceived.equalsIgnoreCase("b"))
                {
                    toMaster.println("Slave A recognized assignment of type B");
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

 /*
 When a slave receives a job, it should “work” on it by sleeping.
 When the slave is finished, it alerts the master that the job is complete,

  There will be two different types of “jobs”, A and B.
   Slave-A is optimized to perform jobs of
   type A and Slave-B is optimized to perform jobs of type B,
    both slave types can do the job for which
   they aren’t optimized, it just takes them longer.
   You will simulate this by having a slave sleep for 2 seconds
   for its optimal job, and 10 seconds for its non-optimal job

   FOOD FOR THOUGHT:
   One slave program or two?
  */

    //Receive jobs assigned to me
    //Determine if job is type A or B
    //Sleep type A for 2 seconds
    //Sleep type B for 10 seconds
    //Remove job when complete?
    //As soon as job finishes sleeping, send message to Master that job has completed
}
