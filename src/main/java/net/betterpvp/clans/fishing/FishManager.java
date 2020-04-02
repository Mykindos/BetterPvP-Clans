package net.betterpvp.clans.fishing;

import net.betterpvp.core.utility.UtilTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FishManager {

    public static List<Fish> fish = new ArrayList<>();

    public static void addFish(Fish f){
        fish.add(f);
    }

    public static List<Fish> getFish(){
        return fish;
    }


    public static void sort(){
        Collections.sort(getFish(), (a, b) -> b.getSize() - a.getSize());
    }

    public static List<Fish> getTop(){

        sort();

        List<Fish> top = new ArrayList<>();
        for(int i = 0; i < 10; i++){
            if(getFish().size() > i){
                top.add(getFish().get(i));
            }
        }

        return top;
    }

    public static List<Fish> getTopToday(){
        sort();

        List<Fish> today = new ArrayList<>();
        for(Fish f : getFish()){
            if(!UtilTime.elapsed(f.getSystemTime(), 86400000)){

                today.add(f);
            }
        }

        List<Fish> top = new ArrayList<>();
        for(int i = 0; i < 10; i++){
            if(i < today.size()){
                top.add(today.get(i));
            }
        }

        return top;
    }

    public static List<Fish> getTopWeek(){
        sort();

        List<Fish> today = new ArrayList<>();
        for(Fish f : getFish()){
            if(!UtilTime.elapsed(f.getSystemTime(), 604800000)){

                today.add(f);
            }
        }

        List<Fish> top = new ArrayList<>();
        for(int i = 0; i < 10; i++){
            if(i < today.size()){
                top.add(today.get(i));
            }

        }

        return top;
    }

    public static boolean isTop(Fish f){
        if(getTop().get(0) == f){
            return true;
        }


        return false;

    }

    public static boolean isTopDay(Fish f){
        if(getTopToday().get(0) == f){
            return true;
        }


        return false;

    }

    public static boolean isTopWeek(Fish f){
        if(getTopWeek().get(0) == f){
            return true;
        }


        return false;

    }
}
