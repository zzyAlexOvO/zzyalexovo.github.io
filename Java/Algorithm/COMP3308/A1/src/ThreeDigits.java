import java.io.*;
import java.util.*;

public class ThreeDigits {
    public static node result;

    public static void BFS(String num , String goal){
        node K =  new node(num);
        node.await_list.add(K);
        int cnt = 0;
        while(cnt<1000){
            if(node.await_list.size() == 0){
                break;
            }
            else if(node.await_list.get(0).value.equals(goal)){
                result = node.await_list.get(0);
                node.expanded_list.add(result);
                break;}
            else{
                node.getKids(node.await_list.get(0));
                for(node i:node.await_list.get(0).kids){
                    boolean can = true;
                    for(node ii : node.expanded_list){
                        if(ii.value.equals(i.value)){
                            if(ii.cant == i.cant){
                                can = false;
                            }
                        }
                    }
                    for(node ii : node.await_list){
                        if(ii.value.equals(i.value)){
                            if(ii.cant == i.cant){
                                can = false;
                            }
                        }
                    }
                    if(!node.expanded_list.contains(i)) {
                        if(node.forbid.size()>=0) {
                            for (String x : node.forbid) {
                                if (i.value.equals(x)) {
                                    can = false;
                                }
                            }
                        }
                        if(can){node.await_list.add(i);}
                    }
                }
                node.expanded_list.add(node.await_list.get(0));
                node.await_list.remove(0);
            }
            cnt ++;
        }
    }
    public static void DFS(String num, String goal){
        node k = new node(num);
        node.await_list.add(k);
        int cnt = 0;
        while(cnt<1000){
            int count2 = 0;
            if(node.await_list.size() == 0){
                break;
            }
            else if(node.await_list.get(0).value.equals(goal)){
                result = node.await_list.get(0);
                node.expanded_list.add(result);
                break;}
            else{
                node current = node.await_list.get(0);
                node.getKids(current);
                node.expanded_list.add(current);
                for(node i:node.await_list.get(0).kids){
                    boolean can = true;
                    for(node ii : node.expanded_list){
                        if(ii.value.equals(i.value)){
                            if(ii.cant == i.cant){
                                can = false;
                            }
                        }
                    }
                    if(!node.expanded_list.contains(i)) {
                        for (String x : node.forbid) {
                            if(i.value.equals(x)){can = false;}
                        }
                        if(can){
                            node.await_list.add(count2,i);
                            count2 ++;
                        }
                    }
                }
                node.await_list.remove(node.await_list.indexOf(current));
            }
            cnt ++;
        }
    }

    public static void IDS(String num, String goal){
        node k = new node(num);
        node.await_list.add(k);
        List<node> temp_fringe = new ArrayList<>();
        int cnt = 0;

        int maxDepth = 1;
        while(cnt<1000){
            int count2 = 0;
            int depth = 1;
            if(node.await_list.size() == 0){
                node.expanded_list.addAll(temp_fringe);
                temp_fringe = new ArrayList<>();
                node.await_list.add(k);
                maxDepth ++;
            }
            else if(node.await_list.get(0).value.equals(goal)){
                result = node.await_list.get(0);
                node.expanded_list.addAll(temp_fringe);
                node.expanded_list.add(result);
                break;}
            else {
                node current = node.await_list.get(0);
                node test = current;
                while (test.father != null) {
                    test = test.father;
                    depth += 1;
                }
                if (depth < maxDepth) {
                    node.getKids(current);
                    temp_fringe.add(current);
                    cnt ++;
                    for (node i : current.kids) {
                        boolean can = true;
                        for(node ii : temp_fringe){
                            if(ii.value.equals(i.value)){
                                if(ii.cant == i.cant){
                                    can = false;
                                }
                            }

                        }
                        if(!temp_fringe.contains(i)) {
                            for (String x : node.forbid) {
                                if(i.value.equals(x)){can = false;}
                            }
                            if(can){
                                node.await_list.add(count2,i);
                                count2 ++;
                            }
                        }
                    }
                    node.await_list.remove(node.await_list.indexOf(current));
                }
                else if(depth == maxDepth){
                    temp_fringe.add(current);
                    cnt ++;
                    node.await_list.remove(node.await_list.indexOf(current));
                }
            }
        }}
    public static void Greedy(String num , String goal){
        node K =  new node(num);
        node.await_list.add(K);
        int cnt = 0;
        while(cnt<1000){
            if(node.await_list.size() == 0){
                break;
            }
            else if(node.await_list.get(0).value.equals(goal)){
                result = node.await_list.get(0);
                node.expanded_list.add(result);
                break;}
            else{
                int x = 0;
                int cc1 = Math.abs(node.await_list.get(x).s1 - Character.getNumericValue((goal).charAt(0))) +Math.abs(node.await_list.get(x).s2 - Character.getNumericValue((goal).charAt(1)))+Math.abs(node.await_list.get(x).s3 - Character.getNumericValue((goal).charAt(2)));
                for(node i:node.await_list){
                    int cc2 = Math.abs(i.s1 - Character.getNumericValue((goal).charAt(0))) +Math.abs(i.s2 - Character.getNumericValue((goal).charAt(1)))+Math.abs(i.s3 - Character.getNumericValue((goal).charAt(2)));
                    if(cc2<=cc1){
                        cc1 = cc2;
                        x = node.await_list.indexOf(i);
                    }
                }
                if(node.await_list.get(x).value.equals(goal)){
                    result = node.await_list.get(x);
                    node.expanded_list.add(result);
                    break;}
                node.getKids(node.await_list.get(x));
                for(node i:node.await_list.get(x).kids){
                    boolean can = true;
                    for(node ii : node.expanded_list){
                        if(ii.value.equals(i.value)){
                            if(ii.cant == i.cant){
                                can = false;
                            }
                        }
                    }
                    for(node ii : node.await_list){
                        if(ii.value.equals(i.value)){
                            if(ii.cant == i.cant){
                                can = false;
                            }
                        }
                    }
                    if(!node.expanded_list.contains(i)) {
                        if(node.forbid.size()>=0) {
                            for (String xx : node.forbid) {
                                if (i.value.equals(xx)) {
                                    can = false;
                                }
                            }
                        }
                        if(can){node.await_list.add(i);}
                    }
                }
                node.expanded_list.add(node.await_list.get(x));
                node.await_list.remove(x);
            }
            cnt ++;
        }
    }
    public static void Hill_climbing(String num, String goal){
        node K =  new node(num);
        node.expanded_list.add(K);
        node.await_list.add(K);
        node.getKids(K);
        boolean ok = true;
        if(!node.forbid.isEmpty()){
            for(node i:K.kids){
                ok = true;
                for(String x : node.forbid){
                    if(i.value.equals(x)){
                        ok = false;
                    }
                }
                if(ok){
                    node.await_list.add(i);
                }
            }}
        else{node.await_list.addAll(K.kids);}
        int last_best = Math.abs(K.s1 - Character.getNumericValue((goal).charAt(0))) +Math.abs(K.s2 - Character.getNumericValue((goal).charAt(1)))+Math.abs(K.s3 - Character.getNumericValue((goal).charAt(2)));
        int cc1 = Math.abs(K.s1 - Character.getNumericValue((goal).charAt(0))) +Math.abs(K.s2 - Character.getNumericValue((goal).charAt(1)))+Math.abs(K.s3 - Character.getNumericValue((goal).charAt(2)));
        int cnt = 0;
        int cc2 = 1000;
        while(cnt<1000){
            if(node.await_list.size() == 0){
                break;
            }
            else if(node.await_list.get(0).value.equals(goal)){
                result = node.await_list.get(0);
                node.expanded_list.add(result);
                break;}
            else{
                int x = 0;
                for(node i:node.await_list){
                    cc2 = Math.abs(i.s1 - Character.getNumericValue((goal).charAt(0))) +Math.abs(i.s2 - Character.getNumericValue((goal).charAt(1)))+Math.abs(i.s3 - Character.getNumericValue((goal).charAt(2)));
                    if(cc2<=cc1){
                        cc1 = cc2;
                        x = node.await_list.indexOf(i);
                    }
                }
                if(last_best <= cc1){
                    break;
                }
                else{
                    if(node.await_list.get(x).value.equals(goal)){
                        result = node.await_list.get(x);
                        node.expanded_list.add(result);
                        break;}
                    else{
                        last_best = cc1;
                        node.expanded_list.add(node.await_list.get(x));
                        node.getKids(node.await_list.get(x));
                        if(!node.forbid.isEmpty()){
                            for(node i:node.await_list.get(x).kids){
                                ok = true;
                                for(String xx : node.forbid){
                                    if(i.value.equals(xx)){
                                        ok = false;
                                    }
                                }
                                if(ok){
                                    node.await_list.add(i);
                                }
                            }

                        }
                        else{node.await_list.addAll(node.await_list.get(x).kids);}
                    }
                }
            }
            cnt++;
        }
    }
    public static void A_star(String num, String goal){
        node K =  new node(num);
        node.await_list.add(K);
        int cnt = 0;
        K.cost = 1 + Math.abs(K.s1 - Character.getNumericValue((goal).charAt(0))) +Math.abs(K.s2 - Character.getNumericValue((goal).charAt(1)))+Math.abs(K.s3 - Character.getNumericValue((goal).charAt(2)));
        int last_best = K.cost;
        int x = 0;
        while(cnt<1000){
            if(node.await_list.size() == 0){
                break;
            }
            else if(node.await_list.get(0).value.equals(goal)){
                result = node.await_list.get(0);
                node.expanded_list.add(result);
                break;}
            else{
                for(node i:node.await_list){
                    node y = i;
                    int depth = 1;
                    while (y.father != null) {
                        y = y.father;
                        depth += 1;
                    }
                    i.cost = depth + Math.abs(i.s1 - Character.getNumericValue((goal).charAt(0))) +Math.abs(i.s2 - Character.getNumericValue((goal).charAt(1)))+Math.abs(i.s3 - Character.getNumericValue((goal).charAt(2)));
                    if(i.cost<=last_best){
                        last_best = i.cost;
                        x = node.await_list.indexOf(i);
                    }
                }
                if(node.await_list.get(x).value.equals(goal)){
                    result = node.await_list.get(x);
                    node.expanded_list.add(result);
                    break;}
                node.getKids(node.await_list.get(x));
                for(node i:node.await_list.get(x).kids){
                    boolean can = true;
                    for(node ii : node.expanded_list){
                        if(ii.value.equals(i.value)){
                            if(ii.cant == i.cant){
                                can = false;
                            }
                        }
                    }
                    for(node ii : node.await_list){
                        if(ii.value.equals(i.value)){
                            if(ii.cant == i.cant){
                                can = false;
                            }
                        }
                    }
                    if(!node.expanded_list.contains(i)) {
                        if(node.forbid.size()>=0) {
                            for (String xx : node.forbid) {
                                if (i.value.equals(xx)) {
                                    can = false;
                                }
                            }
                        }
                        if(can){node.await_list.add(i);}
                    }
                }
                node.expanded_list.add(node.await_list.get(x));
                node.await_list.remove(x);
            }
            cnt ++;
        }
    }

    public static void main(String[] args) throws IOException {
        File F = new File(args[1]);
        Scanner scan = new Scanner(F);
        String Start = scan.nextLine();
        String Goal = scan.nextLine();
        if(scan.hasNextLine()){
            String Forbidden = scan.nextLine();
            node.forbid = Arrays.asList(Forbidden.split(","));
        }
        if(args[0].equals("B")){
            BFS(Start,Goal);
        }
        else if(args[0].equals("D")){
            DFS(Start,Goal);
        }
        else if(args[0].equals("I")){
            IDS(Start,Goal);
        }
        else if(args[0].equals("G")){
            Greedy(Start,Goal);
        }
        else if(args[0].equals("H")){
            Hill_climbing(Start,Goal);
        }
        else if(args[0].equals("A")){
            A_star(Start,Goal);
        }
        List<node> RR = new ArrayList<>();
        if(result != null) {
            while (result.father != null) {
                RR.add(result);
                result = result.father;
            }
            RR.add(result);
            Collections.reverse(RR);
            for (int i = 0; i < RR.size(); i++) {
                if(i == RR.size() - 1){System.out.println(RR.get(i).value);}
                else{System.out.print(RR.get(i).value + ",");}
            }
        }
        else{System.out.println("No solution found");}
        for(int i = 0; i < node.expanded_list.size();i++){
            if(i == node.expanded_list.size() - 1){System.out.println(node.expanded_list.get(i).value);}
            else{System.out.print(node.expanded_list.get(i).value + ",");}
        }
    }
}