package me.kqn.orearea.Buff;

import me.kqn.orearea.PlayerMetaData;

public class None extends AbstractBuff{
    public None(int level){
        super(level);
        cache=new double[2];
        cache[0]=cache[1]=0;
    }
    @Override
    public void onEat(PlayerMetaData metaData) {

    }

    @Override
    public void onBuffProcess(PlayerMetaData metaData) {

    }
}
