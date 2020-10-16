import java.util.ArrayDeque;

import java.util.Random;

public class Core {
    private ArrayDeque<Process> arrProcess = new ArrayDeque<>();
    private Random rand = new Random();
    private final int time = 7;

    public void createProcess() {
        for (int i = 0; i < 3 + rand.nextInt(10); i++) {
        	Process pr = new Process(i);
            pr.createFlows();
            System.out.println("Process " + pr.getId());
            for (int j = 0; j < pr.getFlows().size(); j++) {
                System.out.println("\tFlow " + pr.getFlows().get(j).getId() + " time: " + pr.getFlows().get(j).getTime());
            }
            arrProcess.add(pr);
        }
        System.out.println("\n");
    }

    public void planProcess() {
        while (!arrProcess.isEmpty()) {
        	Process pr = arrProcess.pop();
            for (int j = 0; j < pr.getFlows().size(); j++) {
                int flowId = pr.getFlows().get(j).getId();
                int processId = pr.getId();
                int flowTime = pr.getFlows().get(j).getTime();
                System.out.println("Flow " + flowId + " in process " + processId + " started");
                if (flowTime - time > 0) {
                	pr.getFlows().get(j).changeTime(time);
                	arrProcess.add(pr);
                    System.out.println("\tFlow " + flowId + " in process " + processId + " aborted, need time: "+flowTime +  " given time: "+time);
                }
                if (flowTime - time <= 0) {
                	pr.getFlows().get(j).flowDone();
                	pr.getFlows().remove(j);
                    j--;
                }
            }
            if (pr.getFlows().isEmpty()) {
                pr.processDone();
            }
        }
    }

    public void run() {
        createProcess();
        planProcess();
    }
}
