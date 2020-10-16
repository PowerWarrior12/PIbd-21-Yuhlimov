
public class Flow {
    private int id;
    private int time;

    public Flow (int id,int time){
        this.id=id;
        this.time=time;
    }
    
    public int getId() {
    	return id;
    }
    
    public int getTime() {
    	return time;
    }

    public void changeTime(int time){
    	this.time-=time;
    }

    public void flowDone() {
        System.out.println("\tflow "+ id + " completed successfully in time : "+ time);
    }
}
