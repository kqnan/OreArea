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
        cache[0]=1;
        switch (currentBUFFLevel){
            case 1:
                duration=30*20;
                break;
            case 2:
                duration=40*20;
                break;
            case 3:
                duration=60*20;
        }
    }

    @Override
    public void onBuffProcess(PlayerMetaData metaData) {
        metaData.sprintConsume=0;
    }
}
