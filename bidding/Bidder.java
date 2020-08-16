import behaviours.HelpInitiator;
import behaviours.HelpResponder;
import biddingOntology.*;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.*;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.*;

public class Bidder extends Agent {
    private Codec codec = new SLCodec();
    //change to custom ontology
    private Ontology onto = BiddingOntology.getInstance();
    private Gom myGom;

    @Override
    protected void setup() {
        super.setup();

        // add oneself to the df
        addToDf();

        // info on TR's gom
        myGom = new Gom();
        myGom.setGomId(new AID("someGom", AID.ISLOCALNAME));
        myGom.setPosition(new Position(0, 0));

        getContentManager().registerLanguage(codec, FIPANames.ContentLanguage.FIPA_SL);
        getContentManager().registerOntology(onto);

        // RESPOND TO HELP REQUEST
        addBehaviour(new CyclicBehaviour() {
            MessageTemplate mt = MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.CFP),
                    MessageTemplate.and(MessageTemplate.MatchOntology(onto.getName()),
                            MessageTemplate.MatchLanguage(codec.getName())));

            @Override
            public void action() {
                ACLMessage cfp = myAgent.receive(mt);
                if (cfp != null) {
                    myAgent.addBehaviour(new HelpResponder(myAgent, cfp));
                } else {
                    block();
                }
            }
        });

        // SEND HELP REQUESTS
        // TODO change to cyclic whenever request from gom is received
        addBehaviour(new TickerBehaviour(this, 10000) {
            @Override
            protected void onTick() {
                if (myAgent.getLocalName().contains("TR1")) {
                    ACLMessage bid = new ACLMessage(ACLMessage.CFP);
                    ACLMessage gomRequest = new ACLMessage(ACLMessage.REQUEST);
                    // info from the gom's request
                    int trNumber = prepareCfp(gomRequest, bid, myAgent);
                    myAgent.addBehaviour(new HelpInitiator(myAgent, bid, trNumber));
                }
            }
        });
    }


    private void addToDf() {
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType("tr");
        sd.setName("factory1");
        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }
    }

    private int prepareCfp(ACLMessage gomRequest, ACLMessage bid, Agent bidder) {
        ArrayList<AID> responders = new ArrayList<>();

        // to be retrieved from the gomRequest
        int trNumber = 2;
        int tokens = 10;
        Gom destGom = new Gom();
        destGom.setPosition(new Position(1, 1));
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
            for (int i = 0; i < responders.size(); ++i) {
                if (!responders.get(i).equals(getAID()))
                    bid.addReceiver(responders.get(i));
            }

            bid.setOntology(onto.getName());
            bid.setLanguage(codec.getName());

            GetHelp gh = new GetHelp();
            Proposal prop = new Proposal();

            // to be retrieved from the gomRequest
            prop.setSrcGom(myGom);
            prop.setDestGom(destGom);
            prop.setProposalId(new Random().nextInt());
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
            System.out.println(bid);
        }

        return trNumber;
    }

    @Override
    protected void takeDown() {
        super.takeDown();
    }
}
