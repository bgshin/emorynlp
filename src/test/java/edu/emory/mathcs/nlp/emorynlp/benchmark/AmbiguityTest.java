package edu.emory.mathcs.nlp.emorynlp.benchmark;

import edu.emory.mathcs.nlp.common.util.IOUtils;
import edu.emory.mathcs.nlp.common.util.XMLUtils;
import org.junit.Test;
import edu.emory.mathcs.nlp.emorynlp.component.util.AmbiguityReader;
import org.w3c.dom.Element;

import java.io.InputStream;

/**
 * Created by bong on 11/6/15.
 */
public class AmbiguityTest {
    @Test
    public void test() {
        String configurationFile = "src/main/resources/configuration/config_train_ner.xml";
        InputStream configuration = IOUtils.createFileInputStream(configurationFile);

        Element xml = XMLUtils.getDocumentElement(configuration);
        AmbiguityReader.init(xml);
        System.out.println(AmbiguityReader.getAmbiguityClass("leicestershire"));
        System.out.println(AmbiguityReader.getAmbiguityClass("leichardt"));

    }
}
