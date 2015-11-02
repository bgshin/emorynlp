package edu.emory.mathcs.nlp.emorynlp.benchmark;

import static org.junit.Assert.assertEquals;

import edu.emory.mathcs.nlp.emorynlp.component.node.FeatMap;
import edu.emory.mathcs.nlp.emorynlp.component.node.NLPNode;
import org.junit.Test;
import edu.emory.mathcs.nlp.emorynlp.srl.quiz6;

import java.util.List;


/**
 * Created by bong on 10/30/15.
 */
public class Quiz6 {
    @Test
    public void test() {
        NLPNode[] nodes = new NLPNode[11];
        NLPNode root_node = new NLPNode(0, "root");
        root_node.setToRoot();

        // public NLPNode(int id, String form, String lemma, String posTag, String namentTag, FeatMap feats, NLPNode dhead, String deprel)
        NLPNode node1 = new NLPNode(1, "went", "go", "NXX1", "TAG1", new FeatMap("xx1"), root_node, "REL1");
        NLPNode node2 = new NLPNode(2, "officers", "officer", "NXX2", "TAG2", new FeatMap("xx2"), node1, "REL2");
        NLPNode node3 = new NLPNode(3, "to", "to", "NXX3", "TAG3", new FeatMap("xx3"), node1, "REL3");

        NLPNode node4 = new NLPNode(4, "David", "david", "NXX4", "TAG4", new FeatMap("xx4"), node2, "REL4");
        NLPNode node5 = new NLPNode(5, "land", "land", "NXX5", "TAG5", new FeatMap("xx5"), node3, "REL5");


        NLPNode node6 = new NLPNode(6, "`s", "`s", "NXX6", "TAG6", new FeatMap("xx6"), node4, "REL6");


        NLPNode node7 = new NLPNode(7, "the", "the", "NXX7", "TAG7", new FeatMap("xx7"), node5, "REL7");
        NLPNode node8 = new NLPNode(8, "of", "of", "NXX8", "TAG8", new FeatMap("xx8"), node5, "REL8");

        NLPNode node9 = new NLPNode(9, "Ammonites", "ammonites", "NXX9", "TAG9", new FeatMap("xx9"), node8, "REL9");
        NLPNode node10 = new NLPNode(10, "the", "the", "NXX10", "TAG10", new FeatMap("xx10"), node9, "REL10");



        assertEquals(root_node, node1.getDependencyHead());
        assertEquals(node1, node2.getDependencyHead());
        assertEquals(node1, node3.getDependencyHead());

        assertEquals(node2, node4.getDependencyHead());
        assertEquals(node3, node5.getDependencyHead());

        assertEquals(node4, node6.getDependencyHead());

        assertEquals(node5, node7.getDependencyHead());
        assertEquals(node5, node8.getDependencyHead());

        assertEquals(node8, node9.getDependencyHead());
        assertEquals(node9, node10.getDependencyHead());

//        for (NLPNode n:node9.getAncestorSet())
//            System.out.println(n);

        nodes[0]=root_node;
        nodes[1]=node1;
        nodes[2]=node2;
        nodes[3]=node3;
        nodes[4]=node4;
        nodes[5]=node5;
        nodes[6]=node6;
        nodes[7]=node7;
        nodes[8]=node8;
        nodes[9]=node9;
        nodes[10]=node10;

        quiz6 q=new quiz6();
        List<NLPNode> candidates = q.getArgumentCandidateList(nodes, 2);
        for (NLPNode n:candidates)
            System.out.println(n.getLemma());



//        1	went	go	NXX1	TAG1	_	0	REL1	_
//        3	to	to	NXX3	TAG3	_	1	REL3	_
//        5	land	land	NXX5	TAG5	_	3	REL5	_
//        0	@#r$%	@#r$%	@#r$%	@#r$%	_	_	_	_
//        8	of	of	NXX8	TAG8	_	5	REL8	_




    }
}

