static class Utility{
  static HashMap<String,int[]> parse(String str){
    HashMap<String,int[]> map = new HashMap();
    String[] points = str.split("[)]+[(]|[,]");
    int[] pos = new int[10];
    for(int i =0;i < 10;i++){
      String a = points[i].replaceAll("[(]|[)]","");
       pos[i] = Integer.valueOf(a);
    }
    
    int left_hand;
    int left_leg;
    int body;
    int right_hand;
    int right_leg;
    
    int min_x = 9999;
    int min_x_index = -1;
    int sec_min_x = 9999;
    int sec_min_x_index = -1;
    for(int index =0;index < 10;index += 2){
        int x = pos[index];
        if(x < min_x){
          sec_min_x = min_x;
          sec_min_x_index = min_x_index;
          min_x = x;
          min_x_index = index;
        }else if(x < sec_min_x){
            sec_min_x = x;
            sec_min_x_index = index;
        }
    }
    
    if(pos[min_x_index+1] > pos[sec_min_x_index+1]){
      left_hand = sec_min_x_index;
      left_leg = min_x_index;
    }else{
      left_leg = sec_min_x_index;
      left_hand = min_x_index;
    }
    
    int max_x = 0;
    int max_x_index = -1;
    int sec_max_x = 0;
    int sec_max_x_index = -1;
    for(int index =0;index < 10;index += 2){
        if(index == left_hand || index == left_leg){
          continue;
        }
        int x = pos[index];
        if(x > max_x){
          sec_max_x = max_x;
          sec_max_x_index = max_x_index;
          max_x = x;
          max_x_index = index;
        }else if(x > sec_max_x){
            sec_max_x = x;
            sec_max_x_index = index;
        }
    }
    
    if(pos[max_x_index+1] > pos[sec_max_x_index+1]){
      right_hand = sec_max_x_index;
      right_leg = max_x_index;
    }else{
      right_leg = sec_max_x_index;
      right_hand = max_x_index;
    }
    
    body = 0+2+4+6+8 - left_hand - left_leg - right_hand - right_leg;
    map.put("left_hand",new int[]{pos[left_hand],pos[left_hand+1]});
    map.put("left_leg",new int[]{pos[left_leg],pos[left_leg+1]});
    map.put("right_hand",new int[]{pos[right_hand],pos[right_hand+1]});
    map.put("right_leg",new int[]{pos[right_leg],pos[right_leg+1]});
    map.put("body",new int[]{pos[body],pos[body+1]});
    return map;
  }
}
