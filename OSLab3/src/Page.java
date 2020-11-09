public class Page implements Comparable<Page> {
    private final int ID;
    private boolean InPhysyc;
    private int PhysID;
    private final int ProcessID;
    private int App = 0;
    private int Mod = 0;
    
    Page(int ID, int processID){
        this.ID = ID;
        this.ProcessID = processID;
    }

    public int getID() {
        return ID;
    }

    public int getMod(){
        return Mod;
    }

    public void setMod(int mod) {
    	Mod = mod;
    }

    public int getApp(){
        return App;
    }

    public void setApp(int app) {
    	App = app;
    }

    public int getPhysID() {
        return PhysID;
    }

    public void setPhysicalPageID(int physicalPageID) {
        this.PhysID = physicalPageID;
    }

    public boolean isInPhysicalMemory() {
        return InPhysyc;
    }

    public void setInPhysicalMemory(boolean InPhysyc) {
    	this.InPhysyc = InPhysyc;
    }

    @Override
    public int compareTo(Page page) {
        if(App*2+Mod >page.getApp()*2+page.getMod()){
            return 1;
        }
        else if (App*2+Mod < page.getApp()*2+page.getMod()){
            return -1;
        }
        return 0;
    }

    public int getProcessID() {
        return ProcessID;
    }
}