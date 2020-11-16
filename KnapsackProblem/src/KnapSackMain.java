import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.*;
//This assignment was done by Beau Cranston 000397019

///Stat to optimize:  WISDOM

public class KnapSackMain {

    public static void debugWrite( String s ) {
        if ( DEBUG )
            System.out.print( s );
    }

    public static Items[] getItems(String pathToCSV, HashMap<String, Integer> desiredAttributes) {
        // adapted from https://stackabuse.com/reading-and-writing-csvs-in-java/
        BufferedReader csvReader;
        Items[] items = null;
        String row;

        try {
            ArrayList<Items> al = new ArrayList<>();
            csvReader = new BufferedReader( new FileReader(pathToCSV));
            // consume header row
            csvReader.readLine();
            while ((row = csvReader.readLine() ) != null ) {
                String[] data = row.split(",");
                int value=-1;

                for( int a = 0; a < data.length; a++ ) {
                    String stat = data[a];
                    if (desiredAttributes.containsKey(stat)) {
                        // assume each attribute only exists one time per item
                        int statWeight = desiredAttributes.get(stat).intValue();
                        System.out.println(statWeight);
                        value += statWeight * Integer.parseInt(data[a+1]);
                        break;
                    }
                }
                if ( value != -1 )
                    al.add ( new Items( Integer.parseInt(data[2]), value , data[1] ) );
            }
            csvReader.close();

            items = new Items[al.size()];
            for ( int i = 0; i < al.size(); i++ ) {
                items[i]=(Items)al.get(i);
            }
        } catch ( FileNotFoundException e) {
            System.err.println("Cannot find "+pathToCSV+". Aborting.");
        } catch ( IOException e ) {
            System.err.println("IO Error while reading from file, aborting.");
        }

        return items;
    }

    public static Items[] getItems() {
        /* Class Example */
        Items[] its = new Items[6];
        its[0] = new Items(0,0,"0");
        its[1] = new Items(2,3,"Item 1");
        its[2] = new Items(3,4,"Item 2");
        its[3] = new Items(4,5,"Item 3");
        its[4] = new Items(5,6,"Item 4");


        return its;
    }



    // These lines must be edited to your selection
    static final String[] attribs = {"HP","MN","MV","DR","HR","STR","DEX","CON","INT","WIS","LCK"};

    //static int W = 6;  // class example
    //weight = gold that we have
    static int W = 1000; // assignment code
    //static boolean DEBUG = true;  //46 seconds - shows helper output
    static boolean DEBUG = true; // 3 seconds

    public static void main(String[] args) {
        // uncomment the lines noted as class example to see the solution to the knapsack that was done
        // manually in the lecture.  Be sure to comment out the lines labelled assignment code
        String path= "dataFile.csv";
        HashMap<String, Integer> statsToOptimize = new HashMap<>();
        statsToOptimize.put(attribs[9], 5);
        statsToOptimize.put(attribs[9], 5);
        try {
            Scanner sc = new Scanner(new File(path));
        } catch(Exception e) {
            System.out.println("file exception");
        }

        //Items[] it = getItems(); // class example
        Items[] it = getItems( path, statsToOptimize ); // assignment code

        if ( it == null ) {
            System.out.println("No items found, aborting.");
            System.exit(255);
        }
        //force memo item table to only have 2 rows
        MemoItem[][] m = new MemoItem[2][W+1];

        for ( int j = 0; j <= W; j++ ) {
            m[0][j] = new MemoItem();
        }

        for ( int i=1; i<it.length; i++ ) {
            for (int j = 0; j <= W; j++ ) {
                if ( it[i].weight > j ) {
                    // can't take it
                    debugWrite( String.format("Can't take item %d, bag too "+
                            "small\n", i) );
                    m[1][j] = m[0][j];
                } else {
                    if ( m[0][j].value > m[0][j-it[i].weight].value +
                            it[i].value ) {
                        // don't take it
                        debugWrite( String.format("Won't take item %d, " +
                                "previous bag is worth more\n", i));
                        m[1][j] = m[0][j];
                    } else {
                        debugWrite( String.format("Taking item %d, addition" +
                                " increases bag value\n", i));
                        ArrayList<Items> l = (ArrayList<Items>)
                                (m[0][j-it[i].weight].list.clone());
                        l.add(it[i]);
                        //l.add(it[i].name);
                        m[1][j] = new MemoItem( m[0][j-it[i].weight].value + it[i].value, l);

                    }
                }
            }
            m[0] = Arrays.copyOf(m[1], m[1].length);
            String result = "";
            for ( MemoItem in : m[1] )
                result += in.value + "\t";
            debugWrite( result );
        }
        String result = "";
        for ( Items s : m[1][W].list )
            result += "\t" + s.name + "\n";
        System.out.printf("\nThe optimal value is %d\nThe contents of the "+
                "bag are:\n%s",m[1][W].value,result);
        System.out.println("MemoItems \n Rows: " +  m.length + " Cols: " + m[0].length);
    }

}