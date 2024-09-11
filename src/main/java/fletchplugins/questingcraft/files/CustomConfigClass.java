package fletchplugins.questingcraft.files;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class CustomConfigClass {

    private static File file;
    private static FileConfiguration customFile;

    public static void setup() throws IOException{
        file=new File(Bukkit.getServer().getPluginManager().getPlugin("QuestingCraft").getDataFolder(),"questList.yml");

        if(!file.exists()){

            file.createNewFile();
        }
        customFile= YamlConfiguration.loadConfiguration(file);
    }
    public static FileConfiguration get(){
        return customFile;
    }

    public static void save() throws IOException{
        customFile.save(file);
    }

    public static void reload(){
        customFile=YamlConfiguration.loadConfiguration(file);
    }
}
