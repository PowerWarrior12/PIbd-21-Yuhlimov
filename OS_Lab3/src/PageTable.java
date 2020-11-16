import java.util.ArrayList;

public class PageTable {
	private int PagesCount;
	private int ProcessID;
	private ArrayList<Page> pageTable;
	
	public PageTable() {
		PagesCount = (int)(Math.random() * 5) + 1;
		pageTable = new ArrayList<Page>();
	}
	
	public PageTable(int PagesCount) {
		this.PagesCount = PagesCount;
		pageTable = new ArrayList<Page>();
	}
	
	public Page getPage() {
		if (pageTable.size() > 0) {
			return pageTable.remove(0);
		}
		else {
			return null;
		}
	}
	
	public int getSize() {
		return this.pageTable.size();
	}
	
	public int getPagesCount() {
		return this.PagesCount;
	}
	
	public void addPage(Page page) {
		this.pageTable.add(page);
	}
	
	public void removePage(Page page) {
		this.pageTable.remove(page);
	}
	
	public void setProcessID(int ProcessID) {
		this.ProcessID = ProcessID;
		for (int i = 0;i<PagesCount;i++) {
			pageTable.add(new Page(i,ProcessID));
		}
	}
	
	public boolean havePageInProcess(int getProcessID) {
		for (Page page : pageTable) {
			if (getProcessID == page.getProcessID()) {
				return true;
			}
		}
		return false;
	}
}
