
public class Flow {
    public int id;
    public int time;

    public Flow (int id,int time){
        this.id=id;
        this.time=time;
    }

    public void changeTime(int time){
    	this.time-=time;
    }

    public void flowDone() {
        System.out.println("\tflow "+ id + " completed successfully in time : "+ time);
    }
}
