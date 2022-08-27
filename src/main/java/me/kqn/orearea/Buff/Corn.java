package me.kqn.orearea.Buff;

import me.kqn.orearea.OreArea;
import me.kqn.orearea.PlayerMetaData;

public class Corn extends AbstractBuff{
    public Corn(int level){
        super(level);
        cache=new double[2];
        cache[0]=cache[1]=0;
    }
    @Override
    public void onEat(PlayerMetaData metaData) {
        switch (currentBUFFLevel){
            case 1:
                cache[0]+=0.05;
                cache[1]+=0.1;
                break;
            case 2:
                cache[0]+=0.2;
                cache[1]+=0.2;
                break;
            case 3:
                cache[0]+=0.4;
                cache[1]+=0.3;
        }

    }

    @Override
    public void onBuffProcess(PlayerMetaData metaData) {
        metaData.swimspeed=Math.min(metaData.swimspeed*(1+cache[0]),OreArea.instance.swimSpeedLevel[OreArea.instance.swimSpeedLevel.length-1]);
        metaData.swimConsume=metaData.swimConsume*(1+cache[1]);
    }
}
