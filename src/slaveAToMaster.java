public class slaveAToMaster extends Thread
{
    /*
    * It takes in:
    *   1. jobs completedA arraylist
    *   2. lock on the arraylist
    *   3. slaveA socket output stream back to master
    *
    * while(true)
    *   synch on completedA arraylist
    *       remove first job from list
    *   send back to master via output stream
    * */
}
