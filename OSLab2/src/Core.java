import java.util.ArrayDeque;

import java.util.Random;

public class Core {
    ArrayDeque<Process> arrProcess = new ArrayDeque<>();
    Random rand = new Random();
    private final int time = 7;

    public void createProcess() {
        for (int i = 0; i < 3 + rand.nextInt(10); i++) {
        	Process pr = new Process(i);
            pr.createFlows();
            System.out.println("Process " + pr.id);
            for (int j = 0; j < pr.flows.size(); j++) {
                System.out.println("\tFlow " + pr.flows.get(j).id + " time: " + pr.flows.get(j).time);
            }
            arrProcess.add(pr);
        }
        System.out.println("\n");
    }

    public void planProcess() {
        while (!arrProcess.isEmpty()) {
        	Process pr = arrProcess.pop();
            for (int j = 0; j < pr.flows.size(); j++) {
                int flowId = pr.flows.get(j).id;
                int processId = pr.id;
                int flowTime = pr.flows.get(j).time;
                System.out.println("Flow " + flowId + " in process " + processId + " started");
                if (flowTime - time > 0) {
                	pr.flows.get(j).changeTime(time);
                	arrProcess.add(pr);
                    System.out.println("\tFlow " + flowId + " in process " + processId + " aborted, need time: "+flowTime +  " given time: "+time);
                }
                if (flowTime - time <= 0) {
                	pr.flows.get(j).flowDone();
                	pr.flows.remove(j);
                    j--;
                }
            }
            if (pr.flows.isEmpty()) {
                pr.processDone();
            }
        }
    }

    public void run() {
        createProcess();
        planProcess();
    }
}
