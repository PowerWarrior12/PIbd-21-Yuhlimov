import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class MemoryDispatcher {
    private PageTable physicalMemory;
    private static int memoryCapacity = 512;
    private static int pageCapacity = 32;
    private final ArrayList<Process> processList = new ArrayList<>();
    Random r = new Random();

    public void work() {
        for (int count = 1; count < 10; count++) {
            for (Process process : processList) {
                if(count%2 == 1){
                    swapping(process);
                    continue;
                }
                int ind = r.nextInt(process.getVirtualMemory().size());
                Page nowPage = process.getVirtualMemory().get(ind);
                int actionType = r.nextInt(2); // 0 - Appeal, 1 - modification
                Page[] pageT = physicalMemory.getPageTable();

                if (nowPage.isInPhysicalMemory()) {
                    if (actionType == 0) {
                    	nowPage.setApp(1);
                        System.out.println("Page: " + nowPage.getID() + " process: " + process.getID() + " physicalMemory: " + nowPage.getPhysID() + " appeal");
                    } else {
                    	nowPage.setMod(1);
                        System.out.println("Page: " + nowPage.getID() + " process: " + process.getID() + " physicalMemory: " + nowPage.getPhysID() + " modification");
                    }
                } else if (pageT[physicalMemory.getMaxPages() - 1] == null) {
                    for (int i = 0; i < physicalMemory.getMaxPages(); i++) {
                        if (pageT[i] == null) {
                        	nowPage.setInPhysicalMemory(true);
                        	nowPage.setPhysicalPageID(i);
                        	pageT[i] = nowPage;
                            System.out.println("Page: " + nowPage.getID() + " process: " + process.getID() + " in physicalMemory " + i);
                            break;
                        }
                    }
                } else {
                	PageBreak(pageT,nowPage,process);
                }
            }
            System.out.println("\nReducing the priority of pages\n");
            for (Page page : physicalMemory.getPageTable()) {
                if (page != null) {
                    page.setApp(0);
                    page.setMod(0);
                }
            }
        }
    }

    public void swapping(Process process) {
        System.out.println("SWAP START");
        Page[] pm = physicalMemory.getPageTable();
        for (int i = 0; i < process.getVirtualMemory().size(); i++) {
            Page trashPage = pm[i];
            if (trashPage != null) {
                System.out.println("\tWrite to disk pages " + trashPage.getID() + " process " + trashPage.getProcessID());
            }
            pm[i] = process.getVirtualMemory().get(i);
            System.out.println("\tAdding a page " + pm[i].getID() + " process " + pm[i].getProcessID());
        }
    }

    MemoryDispatcher() {
        physicalMemory = new PageTable(memoryCapacity / pageCapacity);
        for (int i = 0; i < r.nextInt(3) + 5; i++) {
            processList.add(new Process(i));
        }
    }
    
    public void PageBreak(Page[] pageT, Page nowPage,Process process) {
    	System.out.println("\nPAGE BREAK");
        Arrays.sort(pageT);
        for (Page page : pageT) {
            System.out.println("\tphysicalMemory: " + page.getPhysID() + " process: " + page.getProcessID() + " page: " + nowPage.getID() + " class: " + (page.getApp() * 2 + page.getMod()));
        }
        System.out.println("uploading a page " + pageT[0].getPhysID());
        nowPage.setInPhysicalMemory(true);
        nowPage.setApp(1);
        nowPage.setPhysicalPageID(pageT[0].getPhysID());
        pageT[0].setInPhysicalMemory(false);
        pageT[0] = nowPage;
        System.out.println("uploading a page " + nowPage.getID() + " process " + process.getID());
        System.out.println("PAGE BREAK COMPLETED");
    }
}