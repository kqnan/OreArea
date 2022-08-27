package me.kqn.orearea.Buff;

import me.kqn.orearea.PlayerMetaData;

public class Garlic extends AbstractBuff{
    public Garlic(int level){
        super(level);
        cache=new double[2];

    }
    @Override
    public void onEat(PlayerMetaData metaData) {

    }

    @Override
    public void onBuffProcess(PlayerMetaData metaData) {
        int level=this.currentBUFFLevel;
        switch (level){
            case 1:
                this.regenStamina(metaData,0.5);
                break;
            case 2:
                this.regenStamina(metaData,1);
                break;
            case 3:
                this.regenStamina(metaData,1.5);
        }
    }
}
