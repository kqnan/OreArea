package me.kqn.orearea.Buff;

import me.kqn.orearea.OreArea;
import me.kqn.orearea.PlayerMetaData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Grape extends AbstractBuff{
    public Grape(int level){
        super(level);
        cache=new double[2];
        cache[0]=cache[1]=0;
        id="none";
    }
    PotionEffect haste1=new PotionEffect(PotionEffectType.FAST_DIGGING,1200,0);
    PotionEffect haste2=new PotionEffect(PotionEffectType.FAST_DIGGING,1200,1);
    PotionEffect haste3=new PotionEffect(PotionEffectType.FAST_DIGGING,1200,2);
    @Override
    public void onEat(PlayerMetaData metaData) {

        switch (currentBUFFLevel){
            case 1:
                cache[0]=0.2;
                metaData.player.addPotionEffect(haste1);
                //cache[1]+=0.05;
                break;
            case 2:
                cache[0]+=0.3;
                metaData.player.addPotionEffect(haste2);
               // cache[1]+=0.2;
                break;
            case 3:
                cache[0]+=0.6;
                metaData.player.addPotionEffect(haste3);
              //  cache[1]+=0.3;
        }
      //  if(cache[0]>1)cache[0]=1;
    }

    @Override
    public void onBuffProcess(PlayerMetaData metaData) {
        OreArea.instance.multiplyMiningConsume(metaData,-cache[0]);
       // metaData.sprintConsume=metaData.sprintConsume*(1+cache[1]);

    }
}
