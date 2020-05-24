package biddingOntology;

import jade.content.Concept;
import jade.core.AID;

public class Gom implements Concept {
    private AID gomId;
    private Position position;

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
