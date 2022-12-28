import java.lang.reflect.Array;
import java.util.ArrayList;

public class SlaveA {
    ArrayList<Job> toCompleteA = new ArrayList<>();
    ArrayList<Job> completedJobsA = new ArrayList<>();

    public static void main(String[] args) {
        while(true){
            //create slave A threads
            Thread fromMaster = new slaveAFromMaster();
            Thread toMaster = new slaveAToMaster();

            // synch on toCompleteA
                // take first job

            //int sleepLength = secondsToSleep(/*pass in the job*/);
            //sleep for this many seconds

            // once awake, synch on completedJobsA
                // add this job to the list
        }
    }
    public int secondsToSleep(Job currJob){
        if(currJob.getType().equalsIgnoreCase("A")){
            return 2;
        }else{
            return 10;
        }
    }
}
