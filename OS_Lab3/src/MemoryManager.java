import java.util.ArrayList;
import java.util.LinkedList;

public class MemoryManager {
	private ArrayList<Process> processes = new ArrayList<Process>();
	private int memoryCapacity = 512;
	private int pageCapacity = 64;
	private PageTable RAM = new PageTable(memoryCapacity/pageCapacity);
	private LinkedList<Page> pagesQueue = new LinkedList<Page>();
	
	public MemoryManager() {
        for (int i = 0; i < 5; i++) {
            processes.add(new Process(i));
        }
	}
	
	public void run() {
		while (!processes.isEmpty()) {
			for (Process process : processes) {
				if (RAM.checkContain()) {
					swap(process);
				}
				System.out.println("Process : " + process.getID());
				System.out.println("\tLoad page");
				Page nowPage = process.getNextPage();
				
				if (nowPage != null) {
					if (RAM.getSize() < RAM.getPagesCount()) {
						System.out.println("\tPage : " + nowPage.getPageID() + " adding to RAM");
						pagesQueue.add(nowPage);
						RAM.addPage(nowPage);
					}
					else {
						Page safePage;
						while (true) {
							safePage = pagesQueue.poll();
							safePage.setApp();
							if (safePage.getApp()) {
								pagesQueue.add(safePage);
							}
							else {
								pagesQueue.add(nowPage);
								System.out.println("\tPage : " + safePage.getPageID() + " from process : " + safePage.getProcessID() + " remove from RAM");
								RAM.removePage(safePage);
								System.out.println("\tPage : " + nowPage.getPageID() + " adding to RAM");
								RAM.addPage(nowPage);
								for (Process safeProcess : processes) {
									if (safePage.getProcessID() == safeProcess.getID()) {
										safeProcess.addPage(safePage);
										break;
									}
								}
								break;
							}
						}
					}
				}
				else {
					System.out.println("\tNULL new pages in process : " + process.getID());
					RAM.removePage(pagesQueue.poll());
					if (!RAM.havePageInProcess(process.getID())) {
						System.out.println("\tProcess : " + process.getID() + " removing");
						processes.remove(process);
						break;
					}
					
				}
			}
		}
	}
	
	public void swap(Process process) {
		System.out.print("Swapping start");
		for (int i = 0;i<process.getSize();i++) {
			Page safePage = RAM.getPage(i);
			if (RAM.getPage(i) != null) {
				System.out.print("\tSave page " + safePage.getPageID() + " of process : " + safePage.getProcessID());
				for (Process safeProcess : processes) {
					if (safePage.getProcessID() == safeProcess.getID()) {
						safeProcess.addPage(safePage);
						break;
					}
				}
			}
			System.out.println("\tAdding a page " + process.getPage(i).getPageID() + " of  process : " + process.getPage(i).getProcessID());
			RAM.addPage(process.getPage(i));
		}
	}
}
