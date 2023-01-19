package me.kqn.orearea;

import fr.mrmicky.fastboard.FastBoard;
import fr.mrmicky.fastboard.FastReflection;
import me.kqn.orearea.Buff.AbstractBuff;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;

public class PlayerMetaData {
    public Block nearbyTreasure=null;
    public double sprintConsume=-1;
    public double swimConsume=-1;
    public double mine1Consume=0.5;
    public double mine2Consume=1;
    public double mine3Consume=2;
    public double mine4Consume=5;
    public double totalStamina=100;
    public double currentStamina=100;
    public double walkConsume=0.01;
    public double baseWalkConsume=0.01;

    public HashMap<String,AbstractBuff> currentBUFF=new HashMap<>();
    public boolean debug=false;
    public Player player;
    public int level=0;
    public int exp=0;
    public double walkspped=0.1;
    public double swimspeed=0;
    public FastBoard board=null;
    //public BossBar bossBar=null;

public PlayerMetaData(Player player,int level,int exp){
    this.player=player;
    this.board=new FastBoard(player);
    this.level=level;
    this.exp=exp;
    //bossBar=Bukkit.createBossBar("附近的宝藏距离:∞", BarColor.YELLOW, BarStyle.SOLID);
  //  bossBar.addPlayer(player);
}
public PlayerMetaData(Player player){
    this.player=player;
    this.board=new FastBoard(player);
   // bossBar=Bukkit.createBossBar("附近的宝藏距离:∞", BarColor.YELLOW, BarStyle.SOLID);
  //  bossBar.addPlayer(player);

}
}
