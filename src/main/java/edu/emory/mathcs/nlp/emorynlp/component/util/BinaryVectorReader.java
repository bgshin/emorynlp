package edu.emory.mathcs.nlp.emorynlp.component.util;

/**
 * Created by bong on 11/3/15.
 */
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import edu.emory.clir.clearnlp.collection.pair.Pair;


public class BinaryVectorReader {
    private DataInputStream f_in;
    private int demension;
    private boolean toUnitVector;
    private long count, vocab_size;
    private byte[] buff = new byte[4];

    public BinaryVectorReader(boolean toUnitVector){
        this.toUnitVector = toUnitVector;
    }

    public BinaryVectorReader(InputStream in, boolean toUnitVector){
        open(in); this.toUnitVector = toUnitVector;
    }

    public void open(InputStream in){
        count = 0;	f_in = new DataInputStream(in);

        try{
            char c; StringBuilder token = new StringBuilder();

            while((c = (char)f_in.read()) != ' ') token.append(c);
            vocab_size = Long.parseLong(token.toString());
            token.setLength(0);

            while((c = (char)f_in.read()) != '\n') token.append(c);
            demension = Integer.parseInt(token.toString());
        }
        catch(Exception e){ e.printStackTrace(); }
    }

    public void close(){
        try{	f_in.close(); }
        catch (IOException e) {e.printStackTrace();}
    }

    public Pair<String, double[]> next() {
        if(f_in != null && count++ < vocab_size){
            try {
                char c; StringBuilder token = new StringBuilder();
                while((c = (char)f_in.read()) != ' ') {
                    if(c == -1)		return null;
                    else 			token.append(c);
                }

                int i; double f; double len = 0;
                double[] array = new double[demension];
                for(i = 0; i < demension; i++){
                    f_in.read(buff, 0, 4);
                    f = ByteBuffer.wrap(buff).order(ByteOrder.LITTLE_ENDIAN).getFloat();
                    array[i] = f;	len += f * f;
                }

                if(toUnitVector){
                    Math.sqrt(len);
                    for(i = 0; i < demension; i++)	array[i] /= len;
                }

                f_in.read();	/* Read off NEXT_LINE */
                return new Pair<>(token.toString(), array);

            } catch (Exception e) { e.printStackTrace(); }
        }
        return null;
    }
}
