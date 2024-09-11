package fletchplugins.questingcraft;

import fletchplugins.questingcraft.files.CustomConfigClass;
import fletchplugins.questingcraft.files.PlayerQuestInfo;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.advancement.Advancement;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class QuestTabCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        if(command.getName().equalsIgnoreCase("quests")){
            if(args.length==1){
                String[] questTypes = {"incomplete", "unclaimed", "claimed", "advancement", "custom", "collection","statistic", "help"};

                List<String> questTypeTab=new ArrayList<>();
                for(String a: questTypes){
                    if(a.toLowerCase().startsWith(args[0].toLowerCase())){
                        questTypeTab.add(a);
                    }
                }
                return questTypeTab;
            }
            else if(args.length==2 && (args[0].equalsIgnoreCase("incomplete") || args[0].equalsIgnoreCase("unclaimed") || args[0].equalsIgnoreCase("claimed"))){
                String[] questTypes = {"advancement", "custom", "collection","statistic"};

                List<String> questTypeTab=new ArrayList<>();
                for(String a: questTypes){
                    if(a.toLowerCase().startsWith(args[1].toLowerCase())){
                        questTypeTab.add(a);
                    }
                }
                return questTypeTab;
            }
        }
        else if(command.getName().equalsIgnoreCase("questclaim")){
            if(args.length==1) {
                List<String> allQuests = new ArrayList<>();
                allQuests.add("all");
                allQuests.add("help");
                allQuests.addAll(PlayerQuestInfo.get().getStringList(player.getUniqueId().toString() + ".Unclaimed"));
                List<String> unclaimTablist = new ArrayList<>();
                for (String a : allQuests) {
                    if (a.toLowerCase().startsWith(args[0].toLowerCase())) {
                        unclaimTablist.add(a);
                    }
                }
                return unclaimTablist;
            }
        }
        else if(command.getName().equalsIgnoreCase("questinfo")){
            if(args.length==1) {
                List<String> allQuests = CustomConfigClass.get().getStringList("AdvancementQuests");
                allQuests.addAll(CustomConfigClass.get().getStringList("CollectionQuests"));
                allQuests.addAll(CustomConfigClass.get().getStringList("StatisticQuests"));
                allQuests.addAll(CustomConfigClass.get().getStringList("CustomQuests"));

                List<String> allQuestTabList = new ArrayList<>();
                allQuestTabList.add("help");
                for (String a : allQuests) {
                    if (a.toLowerCase().startsWith(args[0].toLowerCase())) {
                        allQuestTabList.add(a);
                    }
                }
                return allQuestTabList;
            }
        }
        else if(command.getName().equalsIgnoreCase("questcreate")){
            if(player.hasPermission("questingcraft.create")){
                //questcreate <name> <action_type> <action_data> <number> <reward> <reward_data>
                if(args.length==2){
                    String[] types = {"advancement", "collect", "statistic"};
                    List<String> typesTab = new ArrayList<>();
                    for (String a : types) {
                        if (a.toLowerCase().startsWith(args[1].toLowerCase())) {
                            typesTab.add(a);
                        }
                    }
                    return typesTab;
                }
                else if(args.length==3){
                    if(args[1].equalsIgnoreCase("statistic")){
                        List<String> allStats=new ArrayList<>();
                        for(Statistic s: Statistic.values()){
                            allStats.add(s.getKey().getKey());
                        }
                        List<String> statTab = new ArrayList<>();
                        for (String a : allStats) {
                            if (a.toLowerCase().startsWith(args[2].toLowerCase())) {
                                statTab.add(a);
                            }
                        }
                        return statTab;
                    }

                }
                else if(args.length==5){
                    String[] rewardTypes = {"command", "experience", "flair", "item"};
                    List<String> rewardTab = new ArrayList<>();
                    for (String a : rewardTypes) {
                        if (a.toLowerCase().startsWith(args[2].toLowerCase())) {
                            rewardTab.add(a);
                        }
                    }
                    return rewardTab;
                }
            }
        }
        else if(command.getName().equalsIgnoreCase("questgrant")){
            if(player.hasPermission("questingcraft.grant")){
                if(args.length==2){
                    Player grantedPlayer = Bukkit.getPlayer(args[0]);
                    if(grantedPlayer!=null){
                        List<String> allQuests = CustomConfigClass.get().getStringList("AdvancementQuests");
                        allQuests.addAll(CustomConfigClass.get().getStringList("CollectionQuests"));
                        allQuests.addAll(CustomConfigClass.get().getStringList("StatisticQuests"));
                        allQuests.addAll(CustomConfigClass.get().getStringList("CustomQuests"));
                        allQuests.removeAll(PlayerQuestInfo.get().getStringList(grantedPlayer.getUniqueId().toString() + ".Unclaimed"));
                        allQuests.removeAll(PlayerQuestInfo.get().getStringList(grantedPlayer.getUniqueId().toString() + ".Claimed"));

                        List<String> qTab = new ArrayList<>();
                        for (String a : allQuests) {
                            if (a.toLowerCase().startsWith(args[1].toLowerCase())) {
                                qTab.add(a);
                            }
                        }
                        return qTab;
                    }
                }
            }
        }
        else if(command.getName().equalsIgnoreCase("questdelete")){
            if(player.hasPermission("questingcraft.delete")){
                if(args.length==1){
                    List<String> allQuests = CustomConfigClass.get().getStringList("AdvancementQuests");
                    allQuests.addAll(CustomConfigClass.get().getStringList("CollectionQuests"));
                    allQuests.addAll(CustomConfigClass.get().getStringList("StatisticQuests"));
                    allQuests.addAll(CustomConfigClass.get().getStringList("CustomQuests"));

                    List<String> qTab = new ArrayList<>();
                    for (String a : allQuests) {
                        if (a.toLowerCase().startsWith(args[0].toLowerCase())) {
                            qTab.add(a);
                        }
                    }
                    return qTab;
                }
            }
        }
        else if(command.getName().equalsIgnoreCase("questcheck")){
                if(args.length==1){
                    Player grantedPlayer = player;
                    if(grantedPlayer!=null){
                        List<String> allQuests = CustomConfigClass.get().getStringList("AdvancementQuests");
                        allQuests.addAll(CustomConfigClass.get().getStringList("CollectionQuests"));
                        allQuests.addAll(CustomConfigClass.get().getStringList("StatisticQuests"));
                        allQuests.addAll(CustomConfigClass.get().getStringList("CustomQuests"));
                        allQuests.removeAll(PlayerQuestInfo.get().getStringList(grantedPlayer.getUniqueId().toString() + ".Unclaimed"));
                        allQuests.removeAll(PlayerQuestInfo.get().getStringList(grantedPlayer.getUniqueId().toString() + ".Claimed"));

                        List<String> qTab = new ArrayList<>();
                        for (String a : allQuests) {
                            if (a.toLowerCase().startsWith(args[0].toLowerCase())) {
                                qTab.add(a);
                            }
                        }
                        return qTab;
                    }
                }
        }
        return null;
    }
}
