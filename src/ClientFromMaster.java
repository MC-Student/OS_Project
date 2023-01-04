import java.io.IOException;
import java.io.ObjectInputStream;

public class ClientFromMaster extends Thread
{
    ObjectInputStream clientInput;

    public ClientFromMaster(ObjectInputStream clientInput)
    {
        this.clientInput = clientInput;
    }

    @Override
    public void run()
    {
        while (true)
        {
            Job incoming = null;
            try
            {
                incoming = (Job) clientInput.readObject();
            }
            catch (IOException | ClassNotFoundException e)
            {
                e.printStackTrace();
            }

            if (incoming != null)
            {
                System.out.println("TO CLIENT FROM MASTER: Job " + incoming.getId() + " was completed");
            }

            try
            {
                sleep(20);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }
}
