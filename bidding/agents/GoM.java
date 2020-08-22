package agents;

import jade.core.AID;
import jade.core.Agent;
import utils.Position;

public class GoM extends Agent {
    private Integer id;
    private AID gomId;
    private Position position;

    public GoM(Integer id, Position position){
        this.id = id;
        this.position = position;
    }

    @Override
    protected void setup() {
        super.setup();
    }

    public AID getGomId() {
        return gomId;
    }

    public void setGomId(AID gomId) {
        this.gomId = gomId;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }
}
