import java.util.ArrayList;
import java.util.Random;

public class Process {
    private int id;
    private int allTime = 0;

    private ArrayList<Flow> flows = new ArrayList<>();

    private Random ran = new Random();

    public Process(int id) {
        this.id = id;
    }
    
    public int getId() {
    	return id;
    }
    
    public ArrayList<Flow> getFlows() {
    	return flows;
    }
    
    public void createFlows() {
        for (int i = 0; i < 1 + ran.nextInt(10); i++) {
            int nesTime = 1 + ran.nextInt(10);
            flows.add(new Flow(i, nesTime));
            allTime+=nesTime;
        }
    }

    public void processDone(){
        System.out.println("Process "+ id + " completed successfully in time "+ allTime);
    }
}
