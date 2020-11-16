import java.util.ArrayList;

public class MemoItem {
    int value;
    ArrayList<Items> list;
    
    public MemoItem () {
        value = 0;
        list = new ArrayList<>();
    }
    
    public MemoItem (int v, ArrayList l ) {
        value = v;
        list = l;
    }
    
    @Override
    public String toString() {
        String result = "";
        for ( Items i : list )
            result += "[" + i.name + "]\t";
            
        return String.format("Value: %f\t\tContents: %s\n",value,result);
    }
}
