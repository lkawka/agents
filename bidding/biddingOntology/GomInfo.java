package biddingOntology;

import jade.content.Concept;
import jade.core.AID;

public class GomInfo implements Concept {
    private AID gomId;
    private PositionInfo position;

    public AID getGomId() {
        return gomId;
    }

    public void setGomId(AID gomId) {
        this.gomId = gomId;
    }

    public PositionInfo getPosition() {
        return position;
    }

    public void setPosition(PositionInfo position) {
        this.position = position;
    }
}
