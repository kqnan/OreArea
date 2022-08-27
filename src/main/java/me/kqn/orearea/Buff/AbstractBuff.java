package me.kqn.orearea.Buff;

import me.kqn.orearea.PlayerMetaData;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;

public abstract class AbstractBuff {
    public static HashMap<String,String> buffTranslation=new HashMap<>();
    static {
        buffTranslation.put("pepper","辣椒");
        buffTranslation.put("corn","玉米");
        buffTranslation.put("grape","芒果");
        buffTranslation.put("garlic","菠萝");
    }
    public int currentBUFFLevel=0;
    public String id;
    public BukkitTask currentBUFFTask=null;
    public double[] cache=null;


    public AbstractBuff(int level){
        this.currentBUFFLevel=level;
    }
    public static AbstractBuff getInstance(String id,int level){
        if(id.equalsIgnoreCase("pepper")){

            Pepper i=new Pepper(level);
            i.id=id;
            return i;
        }
        else if(id.equalsIgnoreCase("corn")){

            Corn i=new Corn(level);
            i.id=id;
            return i;
        }
        else if(id.equalsIgnoreCase("garlic")){

            Garlic i=new Garlic(level);
            i.id=id;
            return i;
        }
        else if(id.equalsIgnoreCase("grape")){

            Grape i=new Grape(level);
            i.id=id;
            return i;
        }
        return null;
    }

    public void regenStamina(PlayerMetaData metaData,double amount){
        metaData.currentStamina+=amount;
        if(metaData.currentStamina>=metaData.totalStamina){
            metaData.currentStamina=metaData.totalStamina;
        }
    }

    public abstract void onEat(PlayerMetaData metaData);
    public abstract void onBuffProcess(PlayerMetaData metaData);
}
