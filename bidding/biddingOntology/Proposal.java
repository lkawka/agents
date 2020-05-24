package biddingOntology;

public class Proposal {
    private Integer proposalId;
    private Integer trNumber;
    private Gom destGom, srcGom;
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

    public Gom getDestGom() {
        return destGom;
    }

    public void setDestGom(Gom destGom) {
        this.destGom = destGom;
    }

    public Gom getSrcGom() {
        return srcGom;
    }

    public void setSrcGom(Gom srcGom) {
        this.srcGom = srcGom;
    }

    public Integer getTokens() {
        return tokens;
    }

    public void setTokens(Integer tokens) {
        this.tokens = tokens;
    }
}
