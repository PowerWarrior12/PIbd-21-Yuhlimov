import java.util.Random;

public class Page {
	private int PageID;
	private int ProcessID;
	private boolean App = false;
	
	public Page(int PageID,int ProcessID) {
		this.PageID = PageID;
		this.ProcessID = ProcessID;
		System.out.println("\tPage : " + this.PageID + " created;");
	}
	
	public void setApp() {
		Random random = new Random();
		this.App = random.nextBoolean();
		System.out.println("\tPage " + this.PageID + " from process : " + ProcessID + " appeal set to : " + this.App);
	}
	
	public boolean getApp() {
		return this.App;
	}
	
	public int getPageID() {
		return this.PageID;
	}
	
	public int getProcessID() {
		return this.ProcessID;
	}
}
