package fletchplugins.questingcraft;

import fletchplugins.questingcraft.files.CustomConfigClass;
import fletchplugins.questingcraft.files.PlayerQuestInfo;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public final class QuestingCraft extends JavaPlugin {
    QuestListener questListener=new QuestListener();
    @Override
    public void onEnable() {
        this.getLogger().info("Starting up QuestingCraft!");


        try{
            getDataFolder().mkdir();
            CustomConfigClass.setup();
            CustomConfigClass.save();
            PlayerQuestInfo.setup();
            PlayerQuestInfo.save();
        }
        catch (IOException e){
            //ignore
        }

        this.getCommand("quests").setExecutor(new CommandClass());
        this.getCommand("questcreate").setExecutor(new CommandClass());
        this.getCommand("questinfo").setExecutor(new CommandClass());
        this.getCommand("questclaim").setExecutor(new CommandClass());
        this.getCommand("flairs").setExecutor(new CommandClass());
        this.getCommand("flairset").setExecutor(new CommandClass());
        this.getCommand("questgrant").setExecutor(new CommandClass());
        this.getCommand("questcreatecustom").setExecutor(new CommandClass());
        this.getCommand("questdelete").setExecutor(new CommandClass());
        this.getCommand("questcheck").setExecutor(new CommandClass());

        this.getCommand("quests").setTabCompleter(new QuestTabCompleter());
        this.getCommand("questcreate").setTabCompleter(new QuestTabCompleter());
        this.getCommand("questinfo").setTabCompleter(new QuestTabCompleter());
        this.getCommand("questclaim").setTabCompleter(new QuestTabCompleter());
        this.getCommand("questgrant").setTabCompleter(new QuestTabCompleter());
        this.getCommand("questdelete").setTabCompleter(new QuestTabCompleter());
        this.getCommand("questcheck").setTabCompleter(new QuestTabCompleter());

        this.getServer().getPluginManager().registerEvents(questListener, this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
