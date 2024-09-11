package fletchplugins.questingcraft;

import fletchplugins.questingcraft.files.CustomConfigClass;
import fletchplugins.questingcraft.files.PlayerQuestInfo;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.player.PlayerStatisticIncrementEvent;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class QuestListener implements Listener {
    @EventHandler
    public void onAdvancement(PlayerAdvancementDoneEvent event) {
        String questThatWins = "FAILED";
        boolean foundQuest = false;
        //maybe do a list here so account for these people doing multiple quests.

        for (String quest : CustomConfigClass.get().getStringList("AdvancementQuests")) {
                if (!foundQuest && CustomConfigClass.get().getString("Quests." + quest + ".data").equalsIgnoreCase("minecraft:" + event.getAdvancement().getKey().getKey())) {
                    questThatWins = quest;
                    if (!alreadyDone(quest, event.getPlayer())) {
                        foundQuest = true;
                    }
                }
            }

        if(foundQuest){
            Bukkit.broadcastMessage(ChatColor.AQUA + event.getPlayer().getName()+ChatColor.GOLD +" did the quest " +
                   ChatColor.LIGHT_PURPLE+"\""+questThatWins+"\"" +
                   ChatColor.GOLD+", Nice Work!");
            String messageNoColor="**"+event.getPlayer().getName() +"** did the quest **"+"\""+questThatWins+"\""+"**, Nice Work!";
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "discord broadcast :mcfrog: "+messageNoColor);
//           rewardHandler(questThatWins, event.getPlayer());
            List<String> unclaimed=PlayerQuestInfo.get().getStringList(event.getPlayer().getUniqueId().toString()+".Unclaimed");
            unclaimed.add(questThatWins);
            PlayerQuestInfo.get().set(event.getPlayer().getUniqueId().toString()+".Unclaimed",unclaimed);
            try {
                PlayerQuestInfo.save();
            } catch (IOException e) {
//                ignore this
            }
        }

    }

    @EventHandler
    public void onItemGet(EntityPickupItemEvent event){
        if(event.getEntity() instanceof Player){
            Player player=(Player) event.getEntity();
            String questThatWins="FAILED";
            boolean foundQuest=false;
            //maybe do a list here so account for these people doing multiple quests.
            for (String quest : CustomConfigClass.get().getStringList("CollectionQuests")) {
                    if (CustomConfigClass.get().getString("Quests." + quest + ".data").equalsIgnoreCase(event.getItem().getName().replace(' ', '_'))) {
                        int count = CustomConfigClass.get().getInt("Quests." + quest + ".number");
                        int numberInInv = 0;

                        HashMap<Integer, ? extends ItemStack> allOfItem = player.getInventory().all(Material.valueOf(CustomConfigClass.get().getString("Quests." + quest + ".data").toUpperCase(Locale.ROOT)));
                        for (HashMap.Entry<Integer, ? extends ItemStack> entry : allOfItem.entrySet()) {
                            numberInInv += entry.getValue().getAmount();
                        }
                        if (numberInInv + event.getItem().getItemStack().getAmount() >= count && !foundQuest) {
                            questThatWins = quest;
                            if (!alreadyDone(quest, player)) {
                                foundQuest = true;
                            }
                        }

                    }
                }
            if(foundQuest){
                Bukkit.broadcastMessage(ChatColor.AQUA + player.getName()+ChatColor.GOLD +" did the quest " +
                        ChatColor.LIGHT_PURPLE+"\""+questThatWins+"\"" +
                        ChatColor.GOLD+", Nice Work!");
                String messageNoColor="**"+player.getName() +"** did the quest **"+"\""+questThatWins+"\""+"**, Nice Work!";
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "discord broadcast :mcfrog: "+messageNoColor);
//                rewardHandler(questThatWins, player);
                List<String> unclaimed=PlayerQuestInfo.get().getStringList(player.getUniqueId().toString()+".Unclaimed");
                unclaimed.add(questThatWins);
                PlayerQuestInfo.get().set(player.getUniqueId().toString()+".Unclaimed",unclaimed);
                try {
                    PlayerQuestInfo.save();
                } catch (IOException e) {
//                ignore this
                }
            }
        }
    }

    @EventHandler
    public void onStatChange(PlayerStatisticIncrementEvent event){
        String questThatWins="FAILED";
        Player player=event.getPlayer();
        boolean foundQuest=false;
        Statistic statChanged=event.getStatistic();
        //maybe do a list here so account for these people doing multiple quests.
        for (String quest : CustomConfigClass.get().getStringList("StatisticQuests")) {
                String[] pieces = CustomConfigClass.get().getString("Quests." + quest + ".data").toUpperCase(Locale.ROOT).split(":");
                int count = CustomConfigClass.get().getInt("Quests." + quest + ".number");
                if (pieces[0].equalsIgnoreCase(event.getStatistic().getKey().getKey()) && event.getNewValue() >= count) {
                    if (statChanged.isBlock() || statChanged.getType() == Statistic.Type.ITEM) {
                        Material wantedMat = Material.valueOf(pieces[1].toUpperCase(Locale.ROOT));
                        if (wantedMat == event.getMaterial() && !foundQuest) {
                            questThatWins = quest;
                            if (!alreadyDone(quest, event.getPlayer())) {
                                foundQuest = true;
                            }
                        }
                    } else if (statChanged.getType() == Statistic.Type.ENTITY) {
                        EntityType entityType = EntityType.valueOf(pieces[1].toUpperCase(Locale.ROOT));
                        if (entityType == event.getEntityType() && !foundQuest) {
                            questThatWins = quest;
                            if (!alreadyDone(quest, event.getPlayer())) {
                                foundQuest = true;
                            }
                        }
                    } else if (statChanged.getType() == Statistic.Type.UNTYPED && !foundQuest) {
                        questThatWins = quest;
                        if (!alreadyDone(quest, event.getPlayer())) {
                            foundQuest = true;
                        }
                    }
                }
            }
        if(foundQuest){
            Bukkit.broadcastMessage(ChatColor.AQUA + player.getName()+ChatColor.GOLD +" did the quest " +
                    ChatColor.LIGHT_PURPLE+"\""+questThatWins+"\"" +
                    ChatColor.GOLD+", Nice Work!");
            String messageNoColor="**"+player.getName() +"** did the quest **"+"\""+questThatWins+"\""+"**, Nice Work!";
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "discord broadcast :mcfrog: "+messageNoColor);
//                rewardHandler(questThatWins, player);
            List<String> unclaimed=PlayerQuestInfo.get().getStringList(player.getUniqueId().toString()+".Unclaimed");
            unclaimed.add(questThatWins);
            PlayerQuestInfo.get().set(player.getUniqueId().toString()+".Unclaimed",unclaimed);
            try {
                PlayerQuestInfo.save();
            } catch (IOException e) {
//                ignore this
            }
        }
    }

    public boolean alreadyDone(String quest, Player player){
        List<String> unclaimed=PlayerQuestInfo.get().getStringList(player.getUniqueId().toString()+".Unclaimed");
        List<String> claimed=PlayerQuestInfo.get().getStringList(player.getUniqueId().toString()+".Claimed");
        if(unclaimed==null && claimed==null){
            return false;
        }
        else if(unclaimed.contains(quest)){
            return true;
        }
        else if(claimed.contains(quest)){
            return true;
        }
        else{
            return false;
        }
    }

    @EventHandler
    public void onCraftItem(CraftItemEvent event) {
        String name=event.getWhoClicked().getName();
        Player player=Bukkit.getPlayer(name);
        if(player!=null) {
            String questThatWins = "FAILED";
            boolean foundQuest = false;
            //maybe do a list here so account for these people doing multiple quests.
            for (String quest : CustomConfigClass.get().getStringList("CollectionQuests")) {
                    if (CustomConfigClass.get().getString("Quests." + quest + ".data").equalsIgnoreCase(event.getCurrentItem().getType().getKey().getKey().replace(' ', '_'))) {
                        int count = CustomConfigClass.get().getInt("Quests." + quest + ".number");
                        int numberInInv = 0;

                        HashMap<Integer, ? extends ItemStack> allOfItem = event.getInventory().all(Material.valueOf(CustomConfigClass.get().getString("Quests." + quest + ".data").toUpperCase(Locale.ROOT)));
                        for (HashMap.Entry<Integer, ? extends ItemStack> entry : allOfItem.entrySet()) {
                            numberInInv += entry.getValue().getAmount();
                        }
                        if (numberInInv + event.getCurrentItem().getAmount() >= count && !foundQuest) {
                            questThatWins = quest;

                            if (!alreadyDone(quest, player)) {
                                foundQuest = true;
                            }
                        }

                    }
                }
            if (foundQuest) {
                Bukkit.broadcastMessage(ChatColor.AQUA + player.getName()+ChatColor.GOLD +" did the quest " +
                        ChatColor.LIGHT_PURPLE + "\"" + questThatWins + "\"" +
                        ChatColor.GOLD + ", Nice Work!");
                String messageNoColor="**"+player.getName() +"** did the quest **"+"\""+questThatWins+"\""+"**, Nice Work!";
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "discord broadcast :mcfrog: "+messageNoColor);
//                rewardHandler(questThatWins, player);
                List<String> unclaimed = PlayerQuestInfo.get().getStringList(player.getUniqueId().toString() + ".Unclaimed");
                unclaimed.add(questThatWins);
                PlayerQuestInfo.get().set(player.getUniqueId().toString() + ".Unclaimed", unclaimed);
                try {
                    PlayerQuestInfo.save();
                } catch (IOException e) {
//                ignore this
                }
            }
        }
    }

}
