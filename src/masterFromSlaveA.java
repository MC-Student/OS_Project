public class masterFromSlaveA extends Thread
{
    /*
     * Takes in
     * 1. masterInputStream
     * 2. Arraylist of finishedJobs from the slaveA
     * 3. lock on the arraylist from slaveB
     *
     * when a job is completed
     *   sync on finishedJobs list in master (?)
     *   add job to list
     * */
}
