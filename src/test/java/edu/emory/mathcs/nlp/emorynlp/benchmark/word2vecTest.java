package edu.emory.mathcs.nlp.emorynlp.benchmark;

import edu.emory.mathcs.nlp.emorynlp.component.util.GlobalLexica;
import jsat.SimpleDataSet;
import jsat.classifiers.CategoricalData;
import jsat.classifiers.DataPoint;
import jsat.clustering.KClustererBase;
import jsat.clustering.kmeans.ElkanKMeans;
import jsat.linear.DenseVector;
import jsat.linear.Vec;
import jsat.linear.distancemetrics.CosineDistanceNormalized;
import org.junit.Test;
import edu.emory.mathcs.nlp.emorynlp.component.util.BinaryVectorReader;
import edu.emory.clir.clearnlp.collection.pair.Pair;
import edu.emory.clir.clearnlp.util.IOUtils;


import java.awt.*;
import java.io.ObjectOutputStream;
import java.util.*;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import jsat.linear.distancemetrics.EuclideanDistance;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;


/**
 * Created by bong on 11/3/15.
 */
public class word2vecTest {
    public final int VECTOR_SIZE = 50;
    public final String FILEPATH = "/Users/bong/Downloads/wiki_nyt.skip.word.50.vectors.bin";
    public final int nCluster = 10;

    @Test
    public void test() {

        BinaryVectorReader reader = new BinaryVectorReader(IOUtils.createFileInputStream(FILEPATH),true);

        String brown_path = "/Users/bong/Documents/CS571/HW2/brown-rcv1.clean.tokenized-CoNLL03.txt-c1000-freq1.txt.xz";
        List<String> paths = new ArrayList<>();
        paths.add(brown_path);
        List<Map<String,Set<String>>> b_clusters;

        b_clusters = paths.stream().map(path -> GlobalLexica.getClusters(edu.emory.mathcs.nlp.common.util.IOUtils.createObjectXZBufferedInputStream(path))).collect(Collectors.toList());

        HashMap<String, Integer> brown_words = new HashMap<>();

        for (String str:b_clusters.get(0).keySet() ) {
            String[] strs = str.split("-");
            for (String word:strs) {
                if (!Pattern.matches("[a-zA-Z]+", (CharSequence) word))
                    continue;
                brown_words.put(word,1);
            }
        }

//        b_clusters.get(0).keySet().toArray()[10].toString().split("-")

        Pair<String, double[]> pair;

        HashMap<String, double[]> map = new HashMap<>();
        HashMap<Integer, double[]> map2 = new HashMap<>();
        HashMap<Integer, String> map3 = new HashMap<>();
        HashMap<String, Integer> map4 = new HashMap<>();

        int count = 0;
        while((pair = reader.next()) != null){
//            Pattern.matches("[a-zA-Z]+", map.entrySet().toArray()[0].toString())
            if (brown_words.get(pair.o1)==null)
                continue;
            if (!Pattern.matches("[a-zA-Z]+", (CharSequence) pair.o1))
                continue;
            map.put(pair.o1, pair.o2);
            map2.put(count, pair.o2);
            map3.put(count, pair.o1);
//            System.out.println(count++);
//            if (count>=200) break;
//            if (++count%100000==0) System.out.println(count/1126315.0*100+"%");
            if (++count%10000==0) System.out.println(count/140000.0*100+"%");
        }

//        try
//        {
//            ObjectOutputStream out = IOUtils.createObjectXZBufferedOutputStream("/Users/bong/Downloads/wiki_nyt.skip.word.50.vectors.bin." + String.valueOf(nCluster) + ".xz");
//            out.writeObject(map);
//            out.writeObject(map2);
//            out.writeObject(map3);
//            out.close();
//        }
//        catch (Exception e) {e.printStackTrace();}



        reader.close();

//        ElkanKMeans kMeans = new ElkanKMeans(new EuclideanDistance());
        ElkanKMeans kMeans = new ElkanKMeans(new CosineDistanceNormalized());

        SimpleDataSet word2vec_dataset = makeData(map2);
        List<List<DataPoint>> clusters = kMeans.cluster(word2vec_dataset, nCluster);

        int kkk=0;
        int nClass=0;
        for(List<DataPoint> cluster :  clusters) {
            System.out.println("Class = " + nClass++);
            kkk=0;
            for (int jj=0; jj<cluster.size(); jj++) {
                System.out.println(kkk);
                String key_string  = map3.get(cluster.get(jj).getCategoricalValue(0));
                map4.put(key_string, nClass);
                System.out.println(key_string);
                kkk++;
            }
        }

        try
        {
            ObjectOutputStream out = IOUtils.createObjectXZBufferedOutputStream("/Users/bong/Downloads/wiki_nyt.skip.word.50.vectors.bin." + String.valueOf(nCluster) + ".xz");
            out.writeObject(map4);
            out.close();
        }
        catch (Exception e) {e.printStackTrace();}

    }


    public SimpleDataSet makeData(HashMap<Integer, double[]> map) {
//        int dim = 200;
        int dim = VECTOR_SIZE;
        CategoricalData[] catDataInfo = new CategoricalData[]{new CategoricalData(1)};
        List<DataPoint> dataPoints = new ArrayList<DataPoint>();

        for (Map.Entry<Integer, double[]> entry : map.entrySet()) {
            Integer key = entry.getKey();
            double[] value = entry.getValue();

//            System.out.println(key + " = " + value[0]);

            DenseVector datum = new DenseVector(dim);
            for(int j = 0; j < dim; j++) {
                datum.set(j, value[j] );
            }
            dataPoints.add(new DataPoint(datum, new int[]{key}, catDataInfo));
        }

        return new SimpleDataSet(dataPoints);

    }
}

//American:	0.0748035
//110
//we:	0.06668422
//111
//could:	0.012659424
//112
//three:	0.056078326
//113
//United:	0.07943377