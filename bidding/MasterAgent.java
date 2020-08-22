import agents.GoM;
import agents.TR;
import jade.core.Agent;
import utils.Position;
import utils.Scenario;

import java.util.HashMap;

public class MasterAgent extends Agent {
    Board board;
    HashMap<Integer, Agent[]> agentPairs;

    public MasterAgent(Scenario scenario){
        // initialization based on the scenario
        board = new Board();

        int gomNumber = 3;
        Position[] gomPositions = new Position[gomNumber];

        for(int i=0; i<gomNumber; i++){
            gomPositions[i] = new Position();
            Agent[] agents = new Agent[]{new TR(i), new GoM(i, gomPositions[i])};
            agentPairs.put(i, agents);
        }
    }
}
