package biddingOntology;

import jade.content.Concept;

public class Proposal implements Concept {
    private Integer proposalId;
    private Integer trNumber;
    private GomInfo destGom, srcGom;
    private Integer tokens;

    public Integer getProposalId() {
        return proposalId;
    }

    public void setProposalId(Integer proposalId) {
        this.proposalId = proposalId;
    }

    public Integer getTrNumber() {
        return trNumber;
    }

    public void setTrNumber(Integer trNumber) {
        this.trNumber = trNumber;
    }

    public GomInfo getDestGom() {
        return destGom;
    }

    public void setDestGom(GomInfo destGom) {
        this.destGom = destGom;
    }

    public GomInfo getSrcGom() {
        return srcGom;
    }

    public void setSrcGom(GomInfo srcGom) {
        this.srcGom = srcGom;
    }

    public Integer getTokens() {
        return tokens;
    }

    public void setTokens(Integer tokens) {
        this.tokens = tokens;
    }
}
