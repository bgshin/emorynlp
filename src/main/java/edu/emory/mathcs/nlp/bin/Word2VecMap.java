package edu.emory.mathcs.nlp.bin;

import edu.emory.clir.clearnlp.collection.pair.Pair;
import edu.emory.clir.clearnlp.util.IOUtils;
import edu.emory.mathcs.nlp.emorynlp.component.util.BinaryVectorReader;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

/**
 * Created by bong on 11/4/15.
 */
public class Word2VecMap {

    static private void save(String fname) {
        BinaryVectorReader reader = new BinaryVectorReader(IOUtils.createFileInputStream(fname), true);

        Pair<String, double[]> pair;

        HashMap<Integer, double[]> map1 = new HashMap<>();
        HashMap<Integer, String> map2 = new HashMap<>();

        int count = 0;
        while ((pair = reader.next()) != null) {
            map1.put(count, pair.o2);
            map2.put(count, pair.o1);
            if (count>200) break;
            if (++count % 100000 == 0) System.out.println(count / 1126315.0 * 100 + "%");
        }

        try {
            ObjectOutputStream out = IOUtils.createObjectXZBufferedOutputStream(fname + ".xz");
            out.writeObject(map1);
            out.writeObject(map2);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static private void load(String fname) {
        try {
            HashMap<Integer, double[]> map1;
            HashMap<Integer, String> map2;

            ObjectInputStream in = new ObjectInputStream(IOUtils.createXZBufferedInputStream(fname));
            map1 = (HashMap<Integer, double[]>)in.readObject();
            map2 = (HashMap<Integer, String>)in.readObject();
            in.close();

//            System.out.println(map.get(map3.get(1)));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static public void main(String[] args)
    {
        String fname = "/Users/bong/Downloads/wiki_nyt.skip.word.50.vectors2.bin";
//        String fname = "/Users/bong/Downloads/wiki_nyt.skip.word.50.vectors.bin.xz";
        args[0] = fname;
        save(args[0]);
//        load(args[0]);
        System.out.println("done");
    }
}
