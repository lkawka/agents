package behaviours;

import biddingOntology.BiddingOntology;
import biddingOntology.SendResult;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.proto.SSContractNetResponder;

import java.util.Random;

public class HelpResponder extends SSContractNetResponder {
    private Codec codec = new SLCodec();
    private Ontology onto = BiddingOntology.getInstance();

    public HelpResponder(Agent a, ACLMessage cfp) {
        super(a, cfp);
    }

    @Override
    protected ACLMessage handleCfp(ACLMessage cfp) throws RefuseException, FailureException, NotUnderstoodException {
        ACLMessage reply = cfp.createReply();
        //process the content
        reply.setPerformative(ACLMessage.PROPOSE);
        //calculate the UTILITY
        float utility = new Random().nextFloat();

        reply.setOntology(onto.getName());
        reply.setLanguage(codec.getName());

        SendResult sr = new SendResult();
        sr.setResult(utility);

        Action a = new Action(super.myAgent.getAID(), sr);
        try {
            super.myAgent.getContentManager().fillContent(reply, a);
        } catch (Codec.CodecException ce) {
            ce.printStackTrace();
        } catch (OntologyException oe) {
            oe.printStackTrace();
        }

        System.out.println("REPLY: "+reply);

        return reply;
    }

    @Override
    protected ACLMessage handleAcceptProposal(ACLMessage cfp, ACLMessage propose, ACLMessage accept) throws FailureException {
        // go and help
        // update status
        // inform about result of the action
        ACLMessage result = accept.createReply();
        result.setPerformative(ACLMessage.INFORM);
        //result.setContent of the information
        System.out.println("INFORM: "+super.myAgent.getName()+result);
        return result;
    }
}

