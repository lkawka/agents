package utils;

import biddingOntology.BiddingOntology;
import biddingOntology.SendResult;
import jade.content.ContentElement;
import jade.content.ContentManager;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.lang.acl.ACLMessage;

import java.util.Comparator;

public class MessageComparator implements Comparator<ACLMessage> {
    // sort messages with utility function results
    private Codec codec = new SLCodec();
    private Ontology onto = BiddingOntology.getInstance();

    @Override
    public int compare(ACLMessage o1, ACLMessage o2) {
        ContentManager manager = new ContentManager();
        manager.registerLanguage(codec);
        manager.registerOntology(onto);
        try {
            ContentElement ce1 = manager.extractContent(o1);
            ContentElement ce2 = manager.extractContent(o2);
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