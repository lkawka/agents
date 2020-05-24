package biddingOntology;

import jade.content.AgentAction;

public class GetHelp implements AgentAction {
    private Proposal proposal;

    public Proposal getProposal() {
        return proposal;
    }

    public void setProposal(Proposal proposal) {
        this.proposal = proposal;
    }
}
