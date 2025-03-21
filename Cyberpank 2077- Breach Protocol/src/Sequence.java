import java.util.ArrayList;

public class Sequence extends ArrayList<Symbol> {

    @Override
    public Sequence subList(int Start, int end){
         Sequence sublist = new Sequence();
         for(int i = Start; i < end; i++){
             sublist.add(this.get(i));

         }
         return sublist;

    }

    public boolean matchesFromOffset (Sequence  sub, int offset){

        if (sub.size() == offset) {
            for(int i = 0; i < sub.size(); i++){
                if (this.get(i).equals(sub.get(i))) {
                    return true;
                }
            }
        }
        return false;

    }




    }



