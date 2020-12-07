import java.util.Random;

public class Process implements Cloneable {

    private int PID;
    private int performT;
    private int Timeout;
    private Random rnd = new Random();

    public Process(int pid, int quantumTime, boolean block) {
        PID = pid;
        performT = rnd.nextInt(quantumTime) + 30;
        if (block) {
        	Timeout = rnd.nextInt(quantumTime * 3) + 50;
        } else {
        	Timeout = 0;
        }
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public int getPID() {
        return PID;
    }

    public boolean isCompleted() {
        return performT <= 0;
    }

    public boolean isAwaitingIO() {
        return Timeout > 0;
    }

    public int getTimeout() {
        return Timeout;
    }

    public int getPerformTime() {
        return performT;
    }

    public void setTimeout(int quantumTime) {
    	Timeout -= quantumTime;
    }

    public void setExecutionTime(int quantumTime) {
    	performT -= quantumTime;
    }

    public void performProcess() {
    	performT = 0; 
    }

    public void performWait() {
    	Timeout = 0;
    }
}