package me.kqn.orearea;


import me.kqn.orearea.Storage.TreasureStorage;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.boss.BossBar;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Shulker;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitTask;
import org.checkerframework.checker.units.qual.C;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Treasure implements Listener, CommandExecutor {
    public static ConcurrentHashMap<ItemStack,Integer> treasureContent=new ConcurrentHashMap<>();

    ArrayList<Inventory> cache=new ArrayList<>();
    @EventHandler
    public void BreakChest(PlayerInteractEvent event){
        if(event.getPlayer().getWorld().getName().equalsIgnoreCase(OreArea.instance.enableworld)){
            if(event.getClickedBlock()==null)return;
            if(currentTr.contains(event.getClickedBlock())){
                currentTr.remove(event.getClickedBlock());
                event.getClickedBlock().breakNaturally();
            }
        }
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player ){
            Player player=(Player) sender;
            if(player.isOp()){
                if (args.length==0){
                    Inventory inventory=Bukkit.createInventory(player,54);
                    for (ItemStack key : treasureContent.keySet()) {
                        ItemStack itemStack=key.clone();
                        ItemMeta meta=itemStack.getItemMeta();
                        if(meta.getLore()!=null){
                            List<String> lore=meta.getLore();lore.add("概率："+treasureContent.get(key));
                            meta.setLore(lore);}
                        else {
                            ArrayList<String> lore=new ArrayList<>();
                            lore.add("概率："+treasureContent.get(key));
                            meta.setLore(lore);
                        }
                        itemStack.setItemMeta(meta);
                        inventory.addItem(itemStack);
                    }
                    cache.add(inventory);
                    player.openInventory(inventory);
                }
                else if(args.length==1&&args[0].equalsIgnoreCase("debug")){
                    OreArea.data.get(player.getName()).debug=!OreArea.data.get(player.getName()).debug;
                }
                else if(args.length>=2&&args[0].equalsIgnoreCase("add")){
                    Integer chance=Integer.parseInt(args[1]);
                    treasureContent.put(player.getInventory().getItemInMainHand(),chance);

                }
                else if(args[0].equalsIgnoreCase("save")){
                    TreasureStorage.save(treasureContent);
                }
                else if(args[0].equalsIgnoreCase("tr")){
                    for (Block location : currentTr) {
                        player.sendMessage(location.getLocation().getBlockX()+","+location.getLocation().getBlockY()+","+location.getLocation().getBlockZ());
                        System.out.println(location.getLocation().getBlockX()+","+location.getLocation().getBlockY()+","+location.getLocation().getBlockZ());
                    }
                }
            }
        }
        return false;
    }

    BukkitTask task=null;
    BukkitTask tipTask=null;
    ArrayList<Block> currentTr=new ArrayList<>();
    ArrayList<Material> whitelistblock=new ArrayList<>(Arrays.asList(Material.STONE,Material.GRANITE,Material.DIRT,Material.GRASS_BLOCK,Material.DEEPSLATE));
    public int lifetime=200;
//    public void Start(int radius,int interval_sec,int chestPerPlayer,String enableworld){
//        if(task!=null)task.cancel();
//        World world=Bukkit.getWorld(enableworld);
//        if(world==null)return;
//
//        task=Bukkit.getScheduler().runTaskTimer(OreArea.instance,()->{
//
//            for (Block location : currentTr) {
//                location.setType(Material.STONE);
//            }
//
//            for (Player player : world.getPlayers()) {
//                for(int i=0;i<chestPerPlayer;i++){
//                    int cnt=0;
//                    while (cnt<100){
//                        int dx=random.nextInt(radius*2)-radius;
//                        int dy=random.nextInt(radius*2)-radius;
//                        int dz=random.nextInt(radius*2)-radius;
//                        Location loc=player.getLocation().add(dx,dy,dz);
//
//                        if(whitelistblock.contains(loc.getBlock().getType())&&world.getChunkAt(loc).isLoaded()){
//                            GenerateChest(loc);
//                            break;
//                        }
//                        cnt++;
//                    }
//
//                }
//            }
//            for (Player player:world.getPlayers()){
//                Block chest=null;
//                double dis=999999;
//                for (Block block : currentTr) {
//                    double tmp=block.getLocation().distance(player.getLocation());
//                    if(dis>tmp){
//                        dis=tmp;
//                        chest=block;
//                    }
//                }
//               // player.sendMessage(chest.getLocation().toString());
//                OreArea.data.get(player.getName()).nearbyTreasure=chest;
//            }
//
//            broadcast("宝箱已重新生成",enableworld);
//        },40,interval_sec* 20L);
//        int detectradius=100;
//        tipTask=Bukkit.getScheduler().runTaskTimer(OreArea.instance,()->{
//            for (Player player : world.getPlayers()) {
//                if(OreArea.data.get(player.getName()).nearbyTreasure==null)continue;
//                if(OreArea.data.get(player.getName()).nearbyTreasure.getType().equals(Material.CHEST)){
//                    OreArea.data.get(player.getName()).bossBar.setTitle("附近的宝藏距离："+(int)player.getLocation().distance(OreArea.data.get(player.getName()).nearbyTreasure.getLocation()));
//                }
//                else {
//                    Block chest=null;
//                    double dis=999999;
//                    for (Block block : currentTr) {
//                        double tmp=block.getLocation().distance(player.getLocation());
//                        if(dis>tmp){
//                            dis=tmp;
//                            chest=block;
//                        }
//                    }
//                    OreArea.data.get(player.getName()).nearbyTreasure=chest;
//                }
//            }
//        },100,20);
//
//
//    }
    public void stop(){
        if(task!=null)task.cancel();
        if(tipTask!=null)tipTask.cancel();
    }
    Random random=new Random();
    public void clearChest(){
        for (Block  location : currentTr) {
            location.setType(Material.STONE);
        }
    }
    public void GenerateChest(Location location){
        Block block=location.getBlock();
        block.setType(Material.CHEST);
        Chest chest= (Chest) block.getState();
        for (Map.Entry<ItemStack, Integer> entry : treasureContent.entrySet()) {
            if(random.nextInt(100)<entry.getValue()){
                chest.getSnapshotInventory().addItem(entry.getKey().clone());
            }
        }
        chest.update(true);

        currentTr.add(chest.getBlock());
    }
    public void broadcast(String msg,String world){
        World world1=Bukkit.getWorld(world);
        if(world1!=null){
            for (Player player : world1.getPlayers()) {
                player.sendMessage(msg);
            }
        }
    }
//    public void sendBossBar(String msg,Player player){
//        BossBar bossBar=OreArea.data.get(player.getName()).bossBar;
//        if(bossBar!=null){
//            bossBar.setTitle(msg);
//        }
//    }
}
