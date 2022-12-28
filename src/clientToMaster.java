public class clientToMaster extends Thread
{
    /*
    * Takes in
    *   1. Client arraylist jobsToDo
    *   2. object lock jToDo_LOCK
    *   2. output stream
    *
    * sync on the list
    *   take in first element of list in client
    *       Job currJobToSendToMaster = jobsToDo.get(0);
    *
    * remove item from list
    *       jobsToDo.remove(0);
    *   send via output stream to master
    *       sendJob
    * */
}
