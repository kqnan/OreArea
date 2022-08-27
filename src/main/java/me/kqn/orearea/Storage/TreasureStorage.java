package me.kqn.orearea.Storage;


import me.kqn.orearea.OreArea;
import me.kqn.orearea.PlayerMetaData;
import me.kqn.orearea.Treasure;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TreasureStorage {

    public static final String path="../矿区插件-宝箱物品存储文件（不懂勿删，谨慎）.yml";
    public static YamlConfiguration yml;
    public static void read(){
        try {
            File file=new File(path);
            file.createNewFile();
            yml=YamlConfiguration.loadConfiguration(file);
            for (String key : yml.getKeys(false)) {
                ItemStack itemmap=yml.getItemStack(key);
                Treasure.treasureContent.put(itemmap,Integer.parseInt(key));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void save(ConcurrentHashMap<ItemStack,Integer> items) {
        try {
            File file=new File(path);
            FileWriter fw=new FileWriter(file);
            fw.write("");
            fw.flush();
            fw.close();
            yml=YamlConfiguration.loadConfiguration(file);
            for (Map.Entry<ItemStack, Integer> en : items.entrySet()) {
                yml.set(String.valueOf(en.getValue()),en.getKey());
            }
            yml.save(file);

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
