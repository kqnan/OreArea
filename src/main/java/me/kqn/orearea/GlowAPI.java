package me.kqn.orearea;


import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.core.BlockPosition;
import net.minecraft.network.protocol.game.PacketPlayOutEntityTeleport;
import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntityLiving;
import net.minecraft.server.network.PlayerConnection;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.monster.EntityShulker;
import org.bukkit.Bukkit;
import org.bukkit.Location;


import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_18_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftShulker;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Shulker;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Deprecated
public class GlowAPI {

     Random random=new Random();
    ArrayList<Integer> ids=new ArrayList<>();
    ArrayList<Location> locations=new ArrayList<>();
    ProtocolManager protocolManager;
    public GlowAPI (){
        protocolManager=ProtocolLibrary.getProtocolManager();
        protocolManager.addPacketListener(new PacketAdapter(OreArea.instance, ListenerPriority.NORMAL,PacketType.Play.Client.USE_ENTITY) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                //super.onPacketReceiving(event);
                Integer id=event.getPacket().getIntegers().read(0);
                if(ids.contains(id)){
                    PacketContainer destroy=new PacketContainer(PacketType.Play.Server.ENTITY_DESTROY);
                    IntList intList=new IntArrayList();
                    intList.add(id);
                    destroy.getModifier().write(0,intList);
                    ids.remove(id);
                    try {
                        protocolManager.sendServerPacket(event.getPlayer(),destroy);
                    } catch (InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }

                }
            }
        });
    }

    public  void sendGlowingBlock(Player p, Location loc,int lifetime)  {
        if(locations.contains(loc))return;
        int id=random.nextInt(Integer.MAX_VALUE);
       PacketContainer packet=new PacketContainer(PacketType.Play.Server.SPAWN_ENTITY);
       PacketContainer meta=new PacketContainer(PacketType.Play.Server.ENTITY_METADATA);
        //PacketContainer potion=new PacketContainer(PacketType.Play.Server.ENTITY_EFFECT);
        PacketContainer destroy=new PacketContainer(PacketType.Play.Server.ENTITY_DESTROY);
        IntList intList=new IntArrayList();
        intList.add(id);
        destroy.getModifier().write(0,intList);
       // destroy.getIntegerArrays().write(0,new int[]{id});
        WrappedDataWatcher watcher=new WrappedDataWatcher();
       // watcher.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(1, WrappedDataWatcher.Registry.get(Byte.class)), (byte) 0x40);
        watcher.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(0, WrappedDataWatcher.Registry.get(Byte.class)), (byte) (0x20|0x40));
        meta.getIntegers().write(0,id);
        meta.getWatchableCollectionModifier().write(0,watcher.getWatchableObjects());

       packet.getModifier().writeDefaults();
       packet.getIntegers().write(0,id);
       packet.getUUIDs().write(0,UUID.randomUUID());
        packet.getEntityTypeModifier().write(0, EntityType.SHULKER);
        packet.getDoubles().write(0,loc.getX());
        packet.getDoubles().write(1,loc.getY());
        packet.getDoubles().write(2,loc.getZ());

       // potion.getBytes().write(1,(byte)0x02);
        try {
            protocolManager.sendServerPacket(p, packet);
           protocolManager.sendServerPacket(p,meta);
        }catch (Exception e){
            e.printStackTrace();
        }
        ids.add(id);
        locations.add(loc);
       // ProtocolLibrary.getProtocolManager().sendServerPacket(p,potion);

      Bukkit.getScheduler().runTaskLater(OreArea.instance,()->{

          try {
              ids.remove((Integer)id);
              locations.remove(loc);
              protocolManager.sendServerPacket(p,destroy);
          } catch (InvocationTargetException e) {
              throw new RuntimeException(e);
          }
      },lifetime);



    }
}
