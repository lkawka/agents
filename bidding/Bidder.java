import biddingOntology.*;
import jade.content.ContentElement;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.*;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.ContractNetInitiator;
import jade.proto.ContractNetResponder;

import java.util.*;

public class Bidder extends Agent {
    private Codec codec = new SLCodec();
    //change to custom ontology
    private Ontology onto = BiddingOntology.getInstance();
    private Gom myGom;

    @Override
    protected void setup() {
        super.setup();

        Random r = new Random();

        // add oneself to the df
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType("tr");
        sd.setName("factory1");
        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);
        }
        catch (FIPAException fe) {
            fe.printStackTrace();
        }


        Agent bidder = this;
        myGom = new Gom();
        myGom.setGomId(new AID("someGom", AID.ISLOCALNAME));
        myGom.setPosition(new Position(0,0));

        getContentManager().registerLanguage(codec, FIPANames.ContentLanguage.FIPA_SL);
        getContentManager().registerOntology(onto);

        MessageTemplate mt = (MessageTemplate.and(MessageTemplate.and(MessageTemplate.MatchOntology(onto.getName()),
                MessageTemplate.MatchLanguage(codec.getName())), MessageTemplate.MatchPerformative(ACLMessage.CFP)));

        // RESPOND TO HELP REQUEST
        addBehaviour(new ContractNetResponder(this, mt){
            @Override
            protected ACLMessage handleCfp(ACLMessage cfp) throws RefuseException, FailureException, NotUnderstoodException {
                ACLMessage reply = cfp.createReply();
                //process the content
                reply = new ACLMessage(ACLMessage.PROPOSE);
                //calculate the UTILITY
                float utility = r.nextFloat();

                reply.setOntology(onto.getName());
                reply.setLanguage(codec.getName());

                SendResult sr = new SendResult();
                sr.setResult(utility);

                Action a = new Action(getAID(), sr);
                try {
                    getContentManager().fillContent(reply, a);
                } catch (Codec.CodecException ce) {
                    ce.printStackTrace();
                } catch (OntologyException oe) {
                    oe.printStackTrace();
                }

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


        // SEND HELP REQUESTS
        //TODO
        //change to cyclic behaviour for listening to Gom
        addBehaviour(new TickerBehaviour(this, 5000) {
            ArrayList<AID> responders;
            // on message received from he Gom
            @Override
            protected void onTick() {
                // info from the gom's request
                int trNumber = 2;
                int tokens = 10;
                Gom destGom = new Gom();
                destGom.setPosition(new Position(1,1));
                destGom.setGomId(new AID("someGom2", AID.ISLOCALNAME));

                // get other TRs
                DFAgentDescription template = new DFAgentDescription();
                ServiceDescription sd = new ServiceDescription();
                sd.setName("factory1");
                template.addServices(sd);
                try {
                    // add them as the receivers of the cfp
                    DFAgentDescription[] result = DFService.search(bidder, template);
                    responders = new ArrayList<>();
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
                        bid.addReceiver(responders.get(i));
                    }

                    bid.setOntology(onto.getName());
                    bid.setLanguage(codec.getName());

                    GetHelp gh = new GetHelp();
                    Proposal prop = new Proposal();
                    // based on the message from the gom
                    prop.setSrcGom(myGom);
                    prop.setDestGom(destGom);
                    prop.setProposalId(r.nextInt());
                    prop.setTrNumber(trNumber);
                    prop.setTokens(tokens);
                    gh.setProposal(prop);

                    Action a = new Action(getAID(), gh);
                    try {
                        getContentManager().fillContent(bid, a);
                    } catch (Codec.CodecException ce) {
                        ce.printStackTrace();
                    } catch (OntologyException oe) {
                        oe.printStackTrace();
                    }

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
                            // accept best trNumber proposals
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
            try {
                ContentElement ce1 = getContentManager().extractContent(o1);
                ContentElement ce2 = getContentManager().extractContent(o2);
                if (ce1 instanceof SendResult && ce2 instanceof SendResult) {
                    float r1 = ((SendResult) ce1).getResult();
                    float r2 = ((SendResult) ce2).getResult();
                    if(r1 > r2) return -1;
                    if(r1 < r2) return 1;
                    return 0;
                }
            } catch (Codec.CodecException ce) {
                ce.printStackTrace();
            } catch (OntologyException oe) {
                oe.printStackTrace();
            }

            return -1;
        }
    }
}
