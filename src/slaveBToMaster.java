public class slaveBToMaster extends Thread
{
    /*
     * It takes in:
     *   1. jobs completedB arraylist
     *   2. lock on the arraylist
     *   3. slaveB socket output stream back to master
     *
     * while(true)
     *   synch on completedB arraylist
     *       remove first job from list
     *   send back to master via output stream
     * */
}
