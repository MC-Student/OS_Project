import java.util.ArrayList;

public class SlaveB
{
    ArrayList<Job> toCompleteB = new ArrayList<>();
    ArrayList<Job> completedJobsB = new ArrayList<>();

    public static void main(String[] args) {
        while(true){
            // run slave B threads
            Thread fromMaster = new slaveBFromMaster();
            Thread toMaster = new slaveBToMaster();

            // synch on toCompleteA
            // take first job

            //int sleepLength = secondsToSleep(/*pass in the job*/);
            //sleep for this many seconds

            // once awake, synch on completedJobsA
            // add this job to the list
        }
    }
    public int secondsToSleep(Job currJob){
        if(currJob.getType().equalsIgnoreCase("B")){
            return 2;
        }else{
            return 10;
        }
    }
}
