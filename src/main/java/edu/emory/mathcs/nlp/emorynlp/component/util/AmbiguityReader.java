package edu.emory.mathcs.nlp.emorynlp.component.util;

import edu.emory.mathcs.nlp.common.util.BinUtils;
import edu.emory.mathcs.nlp.common.util.DSUtils;
import edu.emory.mathcs.nlp.common.util.IOUtils;
import edu.emory.mathcs.nlp.common.util.XMLUtils;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by bong on 11/6/15.
 */
public class AmbiguityReader {
    static public Map<String,String> ambiguity_classes;

    static public void init(Element doc)
    {
        Element eGlobal = XMLUtils.getFirstElementByTagName(doc, "global");
        if (eGlobal == null) return;

        NodeList nodes = eGlobal.getElementsByTagName("ambiguity");
        List<String> paths = new ArrayList<>();

        for (int i=0; i<nodes.getLength(); i++)
            paths.add(XMLUtils.getTrimmedTextContent((Element)nodes.item(i)));

        ambiguity_classes = new HashMap<>();
        initAmbiguityClasses(paths.get(0));
    }

    static public void initAmbiguityClasses(String path)
    {
        BinUtils.LOG.info("Loading Ambiguity classes:");

        try {
            String item;
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
            while ((item = reader.readLine()) != null) {
                String[] elements = item.split("\t");
                if (elements.length<2)
                    continue;

                String entity = elements[0];

                String[] entities = entity.split(" ");

                if (entities.length>1)
                    continue;

                String[] raw_tags = elements[1].split(" ");
                StringJoiner tags = new StringJoiner("_");
                tags.add("Ambiguity");

                for (int i=0; i<raw_tags.length; i++) {
                    tags.add(raw_tags[i]);
                }

//                Map<String,String> one_class= new HashMap<>();
                String entity_key = entity.trim().toLowerCase();
                ambiguity_classes.put(entity_key, tags.toString());
//                ambiguity_classes.add(one_class);
            }
            BinUtils.LOG.info("\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    static public String getAmbiguityClass(String word)
    {
        if (ambiguity_classes == null)
            return null;

        return ambiguity_classes.get(word);
    }

}
