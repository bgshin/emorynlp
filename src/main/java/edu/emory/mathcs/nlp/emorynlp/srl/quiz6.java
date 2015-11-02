package edu.emory.mathcs.nlp.emorynlp.srl;

import edu.emory.mathcs.nlp.emorynlp.component.node.NLPNode;

import java.util.*;

/**
 * Created by bong on 10/30/15.
 */

public class quiz6 {
    public quiz6() {}

    public List<NLPNode> getArgumentCandidateList(NLPNode[] nodes, int predicateID)
    {
        // TODO: return argument candidates using the higher-order pruning.
        List<NLPNode> candidates = new ArrayList<NLPNode>();;

        Set<NLPNode> ancestors =  nodes[predicateID].getAncestorSet();

        // ancestors' dependents
        for (NLPNode n:ancestors) {
            for (NLPNode d:n.getDependentList())
                if (d.compareTo(nodes[predicateID])!=0)
                    candidates.add(d);
        }

        //subtree
        Queue<NLPNode> queue = new LinkedList<NLPNode>();
        queue.clear();
        queue.add(nodes[predicateID]);

        while(!queue.isEmpty()) {
            NLPNode node = queue.remove();

            List<NLPNode> dependents = node.getDependentList();
            for (NLPNode n : dependents) {
                queue.add(n);
                candidates.add(n);
            }
        }

        return candidates;
    }
}
