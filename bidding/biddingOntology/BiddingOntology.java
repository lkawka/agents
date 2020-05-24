package biddingOntology;

import jade.content.AgentAction;
import jade.content.Concept;
import jade.content.onto.BasicOntology;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.schema.AgentActionSchema;
import jade.content.schema.ConceptSchema;
import jade.content.schema.PrimitiveSchema;

public class BiddingOntology extends Ontology {
    public static final String ONTOLOGY_NAME = "Bidding-ontology";

    public static final String GET_HELP = "GetHelp";

    public static final String PROPOSAL = "Proposal";
    public static final String PROPOSAL_ID = "proposalId";
    public static final String NUMBER = "trNumber";

    public static final String GOM = "Gom";
    public static final String GOM_ID = "gomId";

    public static final String POSITION = "Position";
    public static final String X_POS = "x";
    public static final String Y_POS = "y";

    public static final String DESTINATION = "destGom";
    public static final String SOURCE = "srcGom";
    public static final String TOKENS = "tokens";

    private static Ontology theInstance = new BiddingOntology();

    public static Ontology getTheInstance(){
        return theInstance;
    }

    private BiddingOntology(){
        super(ONTOLOGY_NAME, BasicOntology.getInstance());

        try{
            add(new ConceptSchema(POSITION), Position.class);
            add(new ConceptSchema(PROPOSAL), Position.class);
            add(new ConceptSchema(GOM), Gom.class);
            add(new AgentActionSchema(GET_HELP), GetHelp.class);

            ConceptSchema cs = (ConceptSchema) getSchema(POSITION);
            cs.add(X_POS, (PrimitiveSchema)getSchema(BasicOntology.INTEGER));
            cs.add(Y_POS, (PrimitiveSchema)getSchema(BasicOntology.INTEGER));

            cs = (ConceptSchema) getSchema(GOM);
            cs.add(GOM_ID, (PrimitiveSchema)getSchema(BasicOntology.INTEGER));
            cs.add(POSITION, (ConceptSchema)getSchema(POSITION));

            cs = (ConceptSchema) getSchema(PROPOSAL);
            cs.add(PROPOSAL_ID, (PrimitiveSchema)getSchema(BasicOntology.INTEGER));
            cs.add(NUMBER, (PrimitiveSchema)getSchema(BasicOntology.INTEGER));
            cs.add(DESTINATION, (ConceptSchema)getSchema(GOM));
            cs.add(SOURCE, (ConceptSchema)getSchema(GOM));
            cs.add(TOKENS, (PrimitiveSchema)getSchema(BasicOntology.INTEGER));

            AgentActionSchema as = (AgentActionSchema) getSchema(GET_HELP);
            as.add(PROPOSAL, (ConceptSchema) getSchema(PROPOSAL));
        }
        catch(OntologyException oe){
            oe.printStackTrace();
        }
    }
}
