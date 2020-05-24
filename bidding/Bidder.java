import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.BasicOntology;
import jade.content.onto.Ontology;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.*;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.ContractNetInitiator;
import jade.proto.ContractNetResponder;
import jade.util.leap.ArrayList;

import java.util.Collections;
import java.util.Comparator;
import java.util.Random;
import java.util.Vector;

public class Bidder extends Agent {
    private Codec codec = new SLCodec();
    //change to custom ontology
    private Ontology onto = BasicOntology.getInstance();
    @Override
    protected void setup() {
        super.setup();



        // add oneself to the df
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType("factory1");
        sd.setName((String)(getArguments()[0]));
        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);
        }
        catch (FIPAException fe) {
            fe.printStackTrace();
        }


        Agent bidder = this;

        MessageTemplate mt = (MessageTemplate.and(MessageTemplate.and(MessageTemplate.MatchOntology(onto.getName()),
                MessageTemplate.MatchLanguage(codec.getName())), MessageTemplate.MatchPerformative(ACLMessage.CFP)));
        addBehaviour(new ContractNetResponder(this, mt){
            @Override
            protected ACLMessage handleCfp(ACLMessage cfp) throws RefuseException, FailureException, NotUnderstoodException {
                ACLMessage reply = cfp.createReply();
                //proccess the content
                reply = new ACLMessage(ACLMessage.PROPOSE);
                //calculate the utility function
                double utility = Math.random();
                //set content
                return reply;
            }

            @Override
            protected ACLMessage handleAcceptProposal(ACLMessage cfp, ACLMessage propose, ACLMessage accept) throws FailureException {
                //go and help
                // update status
                // inform about result of the action
                ACLMessage result = cfp.createReply();
                result.setPerformative(ACLMessage.INFORM);
                //result.setContent of the information
                return result;
            }
        });

        //TODO
        //change to cyclic behaviour for listening to Gom
        addBehaviour(new TickerBehaviour(this, 5000) {
            ArrayList responders = new ArrayList();
            // on message received from he Gom
            @Override
            protected void onTick() {
                // get other TRs
                DFAgentDescription template = new DFAgentDescription();
                ServiceDescription sd = new ServiceDescription();
                sd.setName("factory1");
                template.addServices(sd);
                try {
                    // add them as the receivers of the cfp
                    DFAgentDescription[] result = DFService.search(bidder, template);
                    responders = new ArrayList();
                    for (int i = 0; i < result.length; ++i) {
                        responders.add(result[i].getName());
                    }
                } catch (FIPAException fe) {
                    fe.printStackTrace();
                }
                if (responders.size() > 0) {
                    // initialize cfp
                    ACLMessage bid = new ACLMessage(ACLMessage.CFP);
                    for (int i = 0; i < responders.size(); ++i) {
                        bid.addReceiver((AID)responders.get(i));
                    }
                    // TODO
                    // bid.setLanguage
                    // bid.setOntology
                    // bid.setContent

                    bidder.addBehaviour(new ContractNetInitiator(bidder,bid){
                        @Override
                        protected void handleAllResponses(Vector responses, Vector acceptances) {
                            java.util.ArrayList<ACLMessage> results = new java.util.ArrayList<>();
                            // process all the utility function results
                            for(Object proposal:responses){
                                if(((ACLMessage)proposal).getPerformative()==ACLMessage.PROPOSE){
                                    results.add((ACLMessage)proposal);
                                }
                            }
                            MessageComparator comparator = new MessageComparator();
                            Collections.sort(results, comparator);
                            // TODO
                            // procces content for TR number
                            // accept best trNumber proposals
                            int trNumber = 2;
                            for(int i=0;i<trNumber;i++){
                                acceptances.add(results.get(i));
                            }
                        }
                    });

                }
            }
        });
    }


    @Override
    protected void takeDown() {
        super.takeDown();
    }

    private class MessageComparator implements Comparator<ACLMessage>{

        @Override
        public int compare(ACLMessage o1, ACLMessage o2) {
            // TODO
            //process the content
            return -1;
        }
    }
}
