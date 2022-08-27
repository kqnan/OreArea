package me.kqn.orearea.Storage;


import me.kqn.orearea.OreArea;
import me.kqn.orearea.PlayerMetaData;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerItemHeldEvent;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class LevelStorage {
    public static YamlConfiguration leveldataYMl=null;
    public static final String path="../矿区插件-等级存储文件（不懂勿删，谨慎）.yml";
    public static HashMap<String,Integer> leveldata=new HashMap<>();
    public static HashMap<String,Integer> expdata=new HashMap<>();
    public static void read(){
        try {
            File file=new File(path);
            file.createNewFile();
            leveldataYMl=YamlConfiguration.loadConfiguration(file);
           leveldata.clear();
           expdata.clear();
            for (String key : leveldataYMl.getKeys(false)) {
                leveldata.put(key,leveldataYMl.getInt(key+".level"));
                expdata.put(key,leveldataYMl.getInt(key+".exp"));


            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void save() {
        try {
            YamlConfiguration yml=YamlConfiguration.loadConfiguration(new File(path));
            for (Map.Entry<String, PlayerMetaData> entry : OreArea.data.entrySet()) {
                yml.set(entry.getKey()+".level",entry.getValue().level);

                yml.set(entry.getKey()+".exp",entry.getValue().exp);
            }

            yml.save(new File(path));

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
