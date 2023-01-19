package me.kqn.orearea;

import fr.mrmicky.fastboard.FastBoard;
import me.kqn.orearea.Buff.AbstractBuff;
import me.kqn.orearea.Storage.LevelStorage;
import me.kqn.orearea.Storage.TreasureStorage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;
import org.checkerframework.checker.units.qual.A;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class OreArea extends JavaPlugin implements Listener {
    public static OreArea instance;
    BukkitTask task=null;
    public static ConcurrentHashMap<String,PlayerMetaData> data=new ConcurrentHashMap<>();
    public int[] staminaLevel={110,120,130,140,150,160,170,180,190,200,210,220};
    public double [] swimSpeedLevel={0.3,0.4,0.5,0.6,0.7,0.8,0.85,0.9,0.95,1,1.1,1.2};
    public double [] walkspeedLevel={0.22,0.24,0.26,0.28,0.30,0.32,0.34,0.36,0.38,0.40,0.42,0.44};
    public double [] basewalkConsumeLevel={0.01,0.02,0.03,0.04,0.05,0.06,0.07,0.08,0.09,0.11,0.12};
    public double [] expRequired={10,50,100,200,300,400,500,600,700,800,900,1000};
    public double [] minestrong={0.1,0.2,0.3,0.4,0.5};
    public double[] minelight={0.1,0.2,0.3,0.4,0.5};
    public Material[] mine1ore= {Material.COAL_ORE, Material.DEEPSLATE_COAL_ORE,Material.CLAY,Material.STONE,Material.GRANITE,Material.GRAVEL,Material.DEEPSLATE,Material.TUFF};
    public Material[] mine2ore={Material.COPPER_ORE,Material.DEEPSLATE_COPPER_ORE,Material.IRON_ORE
    ,Material.DEEPSLATE_IRON_ORE,Material.LAPIS_ORE,Material.DEEPSLATE_LAPIS_ORE,
            Material.REDSTONE_ORE,Material.DEEPSLATE_REDSTONE_ORE,
            Material.AMETHYST_BLOCK,Material.AMETHYST_CLUSTER,Material.BUDDING_AMETHYST,Material.DIRT,Material.GRASS_BLOCK};
    public Material[] mine3ore={Material.GOLD_ORE,Material.DEEPSLATE_GOLD_ORE};
    public Material[] mine4ore={Material.DIAMOND_ORE,Material.DEEPSLATE_DIAMOND_ORE,Material.EMERALD_ORE,Material.DEEPSLATE_EMERALD_ORE};
    public HashMap<Material,Double> blockExp=new HashMap<>();
    public int saveInterval=2400;

    void initBlockExpMap(){
        blockExp.put(Material.STONE, 0.5);
        blockExp.put(Material.COAL_ORE,1D);
        blockExp.put(Material.DEEPSLATE_COAL_ORE,1.5);
        blockExp.put(Material.COPPER_ORE,1D);
        blockExp.put(Material.DEEPSLATE_COPPER_ORE,1.5);
        blockExp.put(Material.IRON_ORE,1.5);
        blockExp.put(Material.DEEPSLATE_IRON_ORE,2D);
        blockExp.put(Material.DEEPSLATE_LAPIS_ORE,2D);
        blockExp.put(Material.DEEPSLATE_REDSTONE_ORE,2D);
        blockExp.put(Material.AMETHYST_CLUSTER,2.5);
        blockExp.put(Material.DEEPSLATE_GOLD_ORE,3d);
        blockExp.put(Material.DEEPSLATE_DIAMOND_ORE,4d);
        blockExp.put(Material.EMERALD_ORE,4d);
        blockExp.put(Material.DEEPSLATE_EMERALD_ORE,4d);
    }



    public boolean iscontain(Material material,Material[] materials){
        for (Material material1 : materials) {
            if(material1.equals(material)){
                return true;
            }
        }
        return false;
    }
   public static World world=null;
    String enableworld="world_spelunker_spelunker";

    Treasure tr;
    public static GlowAPI glowAPI=null;
    @Override
    public void onEnable() {
        // Plugin startup logic


        world=Bukkit.getWorld(enableworld);
        LevelStorage.read();
        TreasureStorage.read();

        instance=this;

        initBlockExpMap();

        Bukkit.getPluginManager().registerEvents(new BUFF(),this);
        Bukkit.getPluginManager().registerEvents(this,this);
        tr=new Treasure();
       // Bukkit.getPluginManager().registerEvents(tr,this);
        Bukkit.getPluginCommand("oa").setExecutor(tr);
        PotionEffect fanwei=new PotionEffect(PotionEffectType.CONFUSION,200,5);
        PotionEffect slow=new PotionEffect(PotionEffectType.SLOW,200,5);
        PotionEffect tired=new PotionEffect(PotionEffectType.SLOW_DIGGING,200,10);
        PotionEffect nightversion=new PotionEffect(PotionEffectType.NIGHT_VISION,400,1);
        //定时保存等级数据 异步保存
        Bukkit.getScheduler().runTaskTimerAsynchronously(this, ()->{
            Bukkit.getLogger().info("矿区数据保存任务开始");
            LevelStorage.save();

            Bukkit.getLogger().info("矿区数据保存任务完成");
        },saveInterval,saveInterval);
        //开始寻宝暂时弃用
        //tr.Start(100,600,20,enableworld);

        //主循环
        task=Bukkit.getScheduler().runTaskTimer(this, new Runnable() {
            int timecnt=0;
            @Override
            public void run() {
                timecnt++;
               // System.out.println(timecnt);
            //获取世界上的所有玩家
            for (Player player : world.getPlayers()) {
                if(!data.containsKey(player.getName())){
                    data.put(player.getName(),new PlayerMetaData(player,LevelStorage.leveldata.getOrDefault(player.getName(),0),LevelStorage.expdata.getOrDefault(player.getName(),0)));

                }
            }
            for (Map.Entry<String, PlayerMetaData> playerPlayerMetaDataEntry : data.entrySet()) {
                Player player=Bukkit.getPlayer(playerPlayerMetaDataEntry.getKey());

                PlayerMetaData metaData=playerPlayerMetaDataEntry.getValue();
                if(player==null||!player.isOnline()){
                    //离线后1分钟恢复5点体力
                    if(timecnt==60){
                        metaData.currentStamina+=5;
                        System.out.println("恢复体力");
                        if(metaData.totalStamina<metaData.currentStamina)metaData.currentStamina=metaData.totalStamina;
                    }
                    continue;
                }
              //  System.out.println(player.getName()+"   "+(metaData.board==null));
                if(metaData.board==null||metaData.board.isDeleted()){
                    metaData.board=new FastBoard(player);
                }
                metaData.player=player;
                //给予夜视效果
                metaData.player.addPotionEffect(nightversion);
                //更新bossbar
                /*if(metaData.bossBar!=null&&metaData.bossBar.getPlayers().size()==0){
                    metaData.bossBar.addPlayer(player);
                }*/
                //计算冲刺消耗数值 = (0,1+物品数*0.05)
                int cnt=0;
                for (ItemStack storageContent : player.getInventory().getStorageContents()) {

                    if(storageContent!=null&&!storageContent.getType().isAir())cnt+=storageContent.getAmount();
                }
                metaData.sprintConsume=0.1+cnt*0.05;
                //计算步行消耗
                metaData.baseWalkConsume=metaData.level==0?0:basewalkConsumeLevel[metaData.level-1];
                metaData.walkConsume=metaData.baseWalkConsume+cnt*0.0001;
                //计算游泳消耗
                metaData.swimConsume=0.5+cnt*0.05;
                //计算等级速度体力
                metaData.totalStamina= metaData.level==0?100:staminaLevel[metaData.level-1];
                metaData.swimspeed=metaData.level==0?(0.2):swimSpeedLevel[metaData.level-1];
                metaData.walkspped=metaData.level==0?(0.2):walkspeedLevel[metaData.level-1];

                //设置步行速度
                player.setWalkSpeed((float)metaData.walkspped);
                //计算初始挖掘消耗
                metaData.mine1Consume=0.5;metaData.mine2Consume=1;metaData.mine3Consume=1;metaData.mine4Consume=5;
                //附魔修饰
                Enchantmodifier(player,metaData);
                //buff处理
                BuffProccess(player,metaData);
                //消耗体力
                StaminaConsume(player,metaData);
                if(metaData.debug){
                    ArrayList<String> sb= new ArrayList<>(Arrays.asList("冲刺消耗：" + metaData.sprintConsume, "步行速度：" + metaData.walkspped, "游泳消耗：" + metaData.swimConsume, "游泳速度：" + metaData.swimspeed, "mine1:" + metaData.mine1Consume, "mine2:" + metaData.mine2Consume, "mine3:" + metaData.mine3Consume, "mine4:" + metaData.mine4Consume, "总体力:" + metaData.totalStamina, "当前体力:" + metaData.currentStamina, "当前等级：" + metaData.level+" "+metaData.exp));
                    sb.add("走路消耗:"+metaData.walkConsume);
                    sb.add("当前buff：");
                    for (Map.Entry<String, AbstractBuff> entry : metaData.currentBUFF.entrySet()) {
                        sb.add(entry.getKey()+" "+entry.getValue().currentBUFFLevel+" "+entry.getValue().cache[0]+" "+entry.getValue().cache[1]);
                    }

                    metaData.board.updateLines(sb);
                }
                else {
                    ArrayList<String> sb= new ArrayList<>(Arrays.asList("体力上限:"+(int)metaData.totalStamina,"当前体力："+(int)metaData.currentStamina,"当前等级："+metaData.level,"当前经验："+metaData.exp));
                    sb.add("当前buff：");
                    for (Map.Entry<String, AbstractBuff> entry : metaData.currentBUFF.entrySet()) {
                        sb.add(AbstractBuff.buffTranslation.get(entry.getKey())+" 等级："+entry.getValue().currentBUFFLevel);

                    }

                    metaData.board.updateLines(sb);
                }
                //体力不足则致盲，缓慢，挖掘疲劳
                if(metaData.currentStamina<=0){


                    player.addPotionEffect(slow);
                    player.addPotionEffect(tired);
                    player.addPotionEffect(fanwei);
                    player.sendTitle("你已疲惫不堪,休息一下吧","回城后每分钟恢复5点体力");
                }
                //升级判定
                if(metaData.exp>=expRequired[metaData.level]){
                    metaData.exp=0;
                    metaData.level++;
                    player.sendTitle("升级！","体力+ 移动速度+");
                }


            }
            if(timecnt>=60)timecnt=0;


            }},20,20);
    }



    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        if(data.containsKey(event.getPlayer().getName())){
            data.get(event.getPlayer().getName()).board.delete();
           // data.get(event.getPlayer().getName()).bossBar.removeAll();
        }
    }
    @EventHandler
    public void getExp(BlockBreakEvent event){
        if(event.getBlock().getWorld().getName().equalsIgnoreCase(enableworld)){
            if(blockExp.containsKey(event.getBlock().getType())){
                data.get(event.getPlayer().getName()).exp+=blockExp.get(event.getBlock().getType());
            }
        }
    }
    @EventHandler
    public void MineConsume(BlockBreakEvent event){
        if(event.getBlock().getWorld().getName().equalsIgnoreCase(enableworld)){
            Player player=event.getPlayer();
            StaminaConsume(player,data.get(player.getName()),event.getBlock());
        }
    }
    //游泳速度
    @EventHandler
    public void onSwim(PlayerMoveEvent event){
        if(event.getPlayer().getWorld().getName().equalsIgnoreCase(enableworld)){
            if(event.getPlayer().isSwimming()){
                event.getPlayer().setVelocity(event.getPlayer().getLocation ( ) . getDirection ( ) . multiply ( data.get(event.getPlayer().getName()).swimspeed) );
            }
        }
    }
    public boolean StaminaConsume(Player player, PlayerMetaData metaData, Block block){
        Material material=block.getType();
        if(iscontain(block.getType(),mine1ore)){
            metaData.currentStamina-=metaData.mine1Consume;
        }
        if(iscontain(block.getType(),mine2ore)){
            metaData.currentStamina-=metaData.mine2Consume;
        }
        if(iscontain(block.getType(),mine3ore)){
            metaData.currentStamina-=metaData.mine3Consume;
        }
        if(iscontain(block.getType(),mine4ore)){
            metaData.currentStamina-=metaData.mine4Consume;
        }
        if(metaData.currentStamina<=0){
            metaData.currentStamina=0;
            return false;
        }
        return true;
    }
    @EventHandler
    public void walkConsume(PlayerMoveEvent event){
        if(!event.getPlayer().getWorld().getName().equalsIgnoreCase(enableworld))return;
        if(!event.getPlayer().isSwimming()&&!event.getPlayer().isSprinting()&&data.containsKey(event.getPlayer().getName())){
            if((int) event.getFrom().getX() != (int) event.getTo().getX() || (int) event.getFrom().getY() != (int) event.getTo().getY() || (int) event.getFrom().getZ() != (int) event.getTo().getZ()){
                PlayerMetaData metaData=data.get(event.getPlayer().getName());
                data.get(event.getPlayer().getName()).currentStamina-=metaData.walkConsume;
            }
        }
    }
    public boolean StaminaConsume(Player player,PlayerMetaData metaData){

        if(player.isSwimming()){
            metaData.currentStamina-=metaData.swimConsume;
        }
        else if(player.isSprinting()){
            metaData.currentStamina-=metaData.sprintConsume;
        }


        if(metaData.currentStamina<=0){
            metaData.currentStamina=0;
            return false;
        }
        return true;
    }
    public void multiplyStaminaConsume(PlayerMetaData metaData,double percentage){
        double multipier2=1+percentage;
        metaData.sprintConsume=metaData.sprintConsume*multipier2;
        metaData.swimConsume=metaData.swimConsume*multipier2;
        metaData.mine1Consume=metaData.mine1Consume*multipier2;
        metaData.mine2Consume=metaData.mine2Consume*multipier2;
        metaData.mine3Consume=metaData.mine3Consume*multipier2;
        metaData.mine4Consume=metaData.mine4Consume*multipier2;

    }
    public void multiplyMiningConsume(PlayerMetaData metaData,double percentage){
        double multipier2=1+percentage;
        metaData.mine1Consume=metaData.mine1Consume*multipier2;
        metaData.mine2Consume=metaData.mine2Consume*multipier2;
        metaData.mine3Consume=metaData.mine3Consume*multipier2;
        metaData.mine4Consume=metaData.mine4Consume*multipier2;
    }
    public void BuffProccess(Player player,PlayerMetaData metaData){
         //  if(metaData.currentBUFF!=null) metaData.currentBUFF.onBuffProcess(metaData);
        for (Map.Entry<String, AbstractBuff> entry : metaData.currentBUFF.entrySet()) {
            entry.getValue().onBuffProcess(metaData);
        }
    }

    public void Enchantmodifier(Player player,PlayerMetaData metaData){
        //附魔修饰
        double multipier1=1;
        double multipier2=1;
        for (ItemStack content : player.getInventory().getArmorContents()) {
            if(content!=null){
                for (Map.Entry<Enchantment, Integer> enchantmentIntegerEntry : content.getEnchantments().entrySet()) {
                    if(enchantmentIntegerEntry.getKey().getKey().toString().equalsIgnoreCase("minecraft:mine_strong")){
                        multipier1=multipier1*(1+minestrong[enchantmentIntegerEntry.getValue()-1]);
                    }
                    if(enchantmentIntegerEntry.getKey().getKey().toString().equalsIgnoreCase("minecraft:mine_light")){
                        multipier2=multipier2*(1-minelight[enchantmentIntegerEntry.getValue()-1]);
                    }
                }
            }
        }
        metaData.totalStamina=metaData.totalStamina*multipier1;
        metaData.sprintConsume=metaData.sprintConsume*multipier2;
        metaData.swimConsume=metaData.swimConsume*multipier2;
        metaData.mine1Consume=metaData.mine1Consume*multipier2;
        metaData.mine2Consume=metaData.mine2Consume*multipier2;
        metaData.mine3Consume=metaData.mine3Consume*multipier2;
        metaData.mine4Consume=metaData.mine4Consume*multipier2;

    }
    //步行速度示例 Bukkit.getPlayer("masterk3366").setWalkSpeed(0.5f);
    //游泳速度示例
    /*@EventHandler
    public void onMove(PlayerMoveEvent event){
        if(event.getPlayer().isSwimming()){

            event.getPlayer().setVelocity(event.getPlayer().getLocation ( ) . getDirection ( ) . multiply ( 1) );
        }
    }*/
    @Override
    public void onDisable() {
        LevelStorage.save();
        TreasureStorage.save(Treasure.treasureContent);
    //    tr.stop();
     //   tr.clearChest();

    }
}
