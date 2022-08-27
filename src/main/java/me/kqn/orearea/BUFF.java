package me.kqn.orearea;

import dev.lone.itemsadder.api.CustomStack;
import me.kqn.orearea.Buff.AbstractBuff;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;

public class BUFF implements Listener {
@EventHandler
public void applyBuff(PlayerItemConsumeEvent event){
    CustomStack citem=CustomStack.byItemStack(event.getItem());
    if(citem!=null&&citem.getNamespace().equalsIgnoreCase("customcrops")){
        String id=citem.getId();
        PlayerMetaData metaData=OreArea.data.get(event.getPlayer().getName());
        String star="1";
        if(id.split("_").length>=2){
            if(id.split("_")[1].equals("silver")){
                star="2";
            }
            else star="3";
        }
        String level=star;
      //  if(level.equalsIgnoreCase(""))level="1";
        String buffname=id.split("_")[0];
        if(metaData.currentBUFF.containsKey(buffname)){
            metaData.currentBUFF.get(buffname).currentBUFFLevel=Integer.parseInt(level);

            metaData.currentBUFF.get(buffname).onEat(metaData);
        }
        else {
            metaData.currentBUFF.put(buffname,AbstractBuff.getInstance(id.split("_")[0].toUpperCase(),Integer.parseInt(level)));

            if(metaData.currentBUFF.get(buffname)!=null)metaData.currentBUFF.get(buffname).onEat(metaData);
        }
        if(metaData.currentBUFF.get(buffname)!=null){
            if(metaData.currentBUFF.get(buffname).currentBUFFTask!=null)metaData.currentBUFF.get(buffname).currentBUFFTask.cancel();

            metaData.currentBUFF.get(buffname).currentBUFFTask=Bukkit.getScheduler().runTaskLater(OreArea.instance,()->{
                metaData.currentBUFF.remove(buffname);

            },1200);

        }



    }
}

    public static enum FRUITS{
        MANGO,CHILLI,BANANA,PINEAPPLE,CORN;

    }
}
