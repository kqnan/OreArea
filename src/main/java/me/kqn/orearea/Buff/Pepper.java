package me.kqn.orearea.Buff;

import me.kqn.orearea.OreArea;
import me.kqn.orearea.PlayerMetaData;

public class Pepper extends AbstractBuff{
    public Pepper(int level){
        super(level);
        cache=new double[2];
        cache[0]=0;
    }
    @Override
    public void onEat(PlayerMetaData metaData) {
        switch (currentBUFFLevel){
            case 1:
                this.regenStamina(metaData,10);

                break;
            case 2:
                this.regenStamina(metaData,20);
                break;
            case 3:
                this.regenStamina(metaData,40);
        }
        cache[0]+=0.5;

    }

    @Override
    public void onBuffProcess(PlayerMetaData metaData) {
        OreArea.instance.multiplyStaminaConsume(metaData,cache[0]);
    }
}
