package biddingOntology;

import jade.content.AgentAction;

public class SendResult implements AgentAction {
    private Float result;

    public Float getResult() {
        return result;
    }

    public void setResult(Float result) {
        this.result = result;
    }
}
