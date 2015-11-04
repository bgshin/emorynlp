package edu.emory.mathcs.nlp.bin;

import edu.emory.clir.clearnlp.util.IOUtils;
import jsat.SimpleDataSet;
import jsat.classifiers.CategoricalData;
import jsat.classifiers.DataPoint;
import jsat.clustering.kmeans.*;
import jsat.linear.DenseVector;
import jsat.linear.distancemetrics.*;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;

/**
 * Created by bong on 11/4/15.
 */
public class MakeWord2VecCluster {
    static private HashMap<String, Integer> make(String fname, int nCluster, String distance, String algorithm) {
        try {
//            HashMap<String, double[]> map;
            HashMap<Integer, double[]> map1;
            HashMap<Integer, String> map2;
            HashMap<String, Integer> map_cluster = new HashMap<>();

            ObjectInputStream in = new ObjectInputStream(IOUtils.createXZBufferedInputStream(fname));
//            map1 = (HashMap<String, double[]>)in.readObject();
            map1 = (HashMap<Integer, double[]>)in.readObject();
            map2 = (HashMap<Integer, String>)in.readObject();
            in.close();

            DistanceMetric dm;
            switch (distance) {
                case "cosine":
                    dm = new CosineDistance();
                    break;
                case "cosine_normal":
                    dm = new CosineDistanceNormalized();
                    break;
                case "mahal":
                    dm = new MahalanobisDistance();
                    break;
                case "manhattan":
                    dm = new ManhattanDistance();
                    break;
                case "minko_0.1":
                    dm = new MinkowskiDistance(0.1);
                    break;
                case "minko_0.5":
                    dm = new MinkowskiDistance(0.5);
                    break;
                case "euclidean_normal":
                    dm = new NormalizedEuclideanDistance();
                    break;
                case "chebyshev":
                    dm = new ChebyshevDistance();
                    break;
                case "euclidean":
                    dm = new EuclideanDistance();
                    break;
                default: //"euclidean":
                    dm = new EuclideanDistance();
                    break;
            }

            KMeans kMeans;
            switch (algorithm) {
                case "hamerly":
                    kMeans = new HamerlyKMeans();
                    break;
                case "xmean":
                    kMeans = new XMeans();
                    break;
                case "gmean":
                    kMeans = new GMeans();
                    break;
                case "elkan":
                    kMeans = new ElkanKMeans(dm);
                    break;
                default:
                    kMeans = new ElkanKMeans(dm);
            }

//            ElkanKMeans kMeans = new ElkanKMeans(dm);

            SimpleDataSet word2vec_dataset = makeData(map1);
            List<List<DataPoint>> clusters = kMeans.cluster(word2vec_dataset, nCluster);

            int kkk=0;
            int nClass=0;
            for(List<DataPoint> cluster :  clusters) {
                System.out.println("Class = " + nClass);
                kkk=0;
                for (int jj=0; jj<cluster.size(); jj++) {
                    String key_string  = map2.get(cluster.get(jj).getCategoricalValue(0));
                    System.out.println(kkk + key_string);
                    map_cluster.put(key_string, nClass);
                    kkk++;
                }
                nClass++;
            }

            return map_cluster;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    static private SimpleDataSet makeData(HashMap<Integer, double[]> map) {
        int dim = 50;
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

    static public void main(String[] args) {
        String fname = "/Users/bong/Downloads/wiki_nyt.skip.word.50.vectors2.bin.xz";
        String distance = "minko_0.1";
        String algorithm = "elkan";
        int nCluster=100;
        String basepath = "/Users/bong/Downloads/";



        fname = args[0];
        distance = args[1];
        algorithm = args[2];
        nCluster = Integer.parseInt(args[3]);
        basepath = args[4];


        StringJoiner joiner = new StringJoiner(".");
        joiner.add("word2vec50");
        joiner.add("cluster");
        joiner.add(String.valueOf(nCluster));
        joiner.add(algorithm);
        joiner.add(distance);
        joiner.add("xz");

        String outfname = basepath+joiner.toString();

        HashMap<String, Integer>  map_cluster = make(fname, nCluster, distance, algorithm);

        System.out.println("first="+map_cluster.get("first"));
        System.out.println("second="+map_cluster.get("second"));

        try {
            ObjectOutputStream out = IOUtils.createObjectXZBufferedOutputStream(outfname);
            out.writeObject(map_cluster);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Done");
    }
}
