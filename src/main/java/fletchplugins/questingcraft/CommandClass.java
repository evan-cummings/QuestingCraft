package fletchplugins.questingcraft;

import fletchplugins.questingcraft.files.CustomConfigClass;
import fletchplugins.questingcraft.files.PlayerQuestInfo;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.Statistic;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementDisplayType;
import org.bukkit.block.CommandBlock;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.Material;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import java.io.IOException;
import java.util.*;

public class CommandClass implements CommandExecutor {
    //PlayerStatisticIncrementEvent
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player){
            Player player=(Player) sender;

            if(command.getName().equalsIgnoreCase("quests")){
                if(args.length==0){
                    player.sendMessage(ChatColor.RED+"Not enough arguments! Try /quests help for more info.");
                }
                else if(args.length==1) {
                    if(args[0].equalsIgnoreCase("help")){
                        player.sendMessage(ChatColor.GREEN + "==========/QUESTS==========\n" +
                                        ChatColor.AQUA + "/quests <criteria>"+ChatColor.YELLOW + " will give you a list of all quests fitting the search criteria." +
                                "\nArguments can be incomplete, unclaimed, claimed, advancement, collection, custom, or statistic." +
                                "\nFor example, running " +
                                ChatColor.AQUA + "/quests incomplete"+ChatColor.YELLOW +" will show you all of your currently incomplete quests." +
                                "\nMore specificity can be added by using subcategories. Run /quest incomplete, unclaimed, or claimed, followed by on of the other four options.\n" +
                                "(advancement, collection, custom, or statistic)" +
                                "\nTo get more specific searches. For example,\n" +
                                ChatColor.AQUA + "/quests incomplete advancement"+ChatColor.YELLOW +" will show all incomplete " +
                                ChatColor.LIGHT_PURPLE +"advancement " +
                                ChatColor.YELLOW +"quests you have.");
                    }
                    else if (args[0].equalsIgnoreCase("incomplete")) {
                        List<String> allQuests = CustomConfigClass.get().getStringList("AdvancementQuests");
                        allQuests.addAll(CustomConfigClass.get().getStringList("CollectionQuests"));
                        allQuests.addAll(CustomConfigClass.get().getStringList("StatisticQuests"));
                        allQuests.addAll(CustomConfigClass.get().getStringList("CustomQuests"));
                        allQuests.removeAll(PlayerQuestInfo.get().getStringList(player.getUniqueId().toString() + ".Unclaimed"));
                        allQuests.removeAll(PlayerQuestInfo.get().getStringList(player.getUniqueId().toString() + ".Claimed"));

                        player.sendMessage(ChatColor.GREEN + "==========INCOMPLETE QUESTS==========\n" +
                                ChatColor.YELLOW + "Incomplete quests are quests that you have not yet successfully completed. Try running " +
                                ChatColor.AQUA + "/questinfo <name>" + ChatColor.YELLOW + " on them to learn more!\n" +
                                " \n" + ChatColor.LIGHT_PURPLE + allQuests.toString());
                        if (allQuests.isEmpty()) {
                            player.sendMessage(ChatColor.YELLOW + "\nWow, either you actually did every quest we have right now or I've screwed up horribly" +
                                    "... either way nice work!" +
                                    ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "\n-Fletch");
                        }
                    }
                    else if (args[0].equalsIgnoreCase("unclaimed")) {
                        List<String> unclaimed = PlayerQuestInfo.get().getStringList(player.getUniqueId().toString() + ".Unclaimed");
                        player.sendMessage(ChatColor.GREEN + "==========UNCLAIMED QUESTS==========\n" +
                                ChatColor.YELLOW + "Unclaimed quests are quests that you have successfully completed, but haven't run " +
                                ChatColor.AQUA + "/questclaim" + ChatColor.YELLOW + " on yet.\n" +
                                " \n" + ChatColor.LIGHT_PURPLE + unclaimed.toString());
                        if (unclaimed.isEmpty()) {
                            player.sendMessage(ChatColor.YELLOW + "You've got no outstanding unclaimed quests right now friend!");
                        }
                    }
                    else if (args[0].equalsIgnoreCase("claimed")) {
                        List<String> claimed = PlayerQuestInfo.get().getStringList(player.getUniqueId().toString() + ".Claimed");
                        player.sendMessage(ChatColor.GREEN + "==========CLAIMED QUESTS==========\n" +
                                ChatColor.YELLOW + "Claimed quests are quests that you have successfully completed, and taken the rewards of." +
                                " \n" +
                                " \n" + ChatColor.LIGHT_PURPLE + claimed.toString());
                        if (claimed.isEmpty()) {
                            player.sendMessage(ChatColor.YELLOW + "You haven't claimed any quests yet!");
                        }
                    }
                    else if (args[0].equalsIgnoreCase("advancement")) {
                        List<String> questList = CustomConfigClass.get().getStringList("AdvancementQuests");
                        player.sendMessage(ChatColor.GREEN + "==========ALL ADVANCEMENT QUESTS==========\n" +
                                ChatColor.YELLOW + "Advancement quests require you to complete a vanilla minecraft advancement." +
                                " \n" +
                                " \n" + ChatColor.LIGHT_PURPLE + questList.toString());
                        if (questList.isEmpty()) {
                            player.sendMessage(ChatColor.YELLOW + "There are no existing advancement quests right now!");
                        }
                    }
                    else if (args[0].equalsIgnoreCase("collect") || args[0].equalsIgnoreCase("collection")) {
                        List<String> questList = CustomConfigClass.get().getStringList("CollectionQuests");
                        player.sendMessage(ChatColor.GREEN + "==========ALL COLLECTION QUESTS==========\n" +
                                ChatColor.YELLOW + "Collection quests require you to obtain a certain amount of a specific item." +
                                " \n" +
                                " \n" + ChatColor.LIGHT_PURPLE + questList.toString());
                        if (questList.isEmpty()) {
                            player.sendMessage(ChatColor.YELLOW + "There are no existing collection quests right now!");
                        }
                    }
                    else if (args[0].equalsIgnoreCase("statistic")) {
                        List<String> questList = CustomConfigClass.get().getStringList("StatisticQuests");
                        player.sendMessage(ChatColor.GREEN + "==========ALL STATISTIC QUESTS==========\n" +
                                ChatColor.YELLOW + "Statistic quests require you to get some stat from the vanilla statistics menu to a certain value." +
                                "\nThese can include \"kill_entity:drowned\" or \"interactions_with_smoker\" for some reason \n" +
                                " \n" + ChatColor.LIGHT_PURPLE + questList.toString());
                        if (questList.isEmpty()) {
                            player.sendMessage(ChatColor.YELLOW + "There are no existing statistic quests right now!");
                        }
                    }
                    else if (args[0].equalsIgnoreCase("custom")) {
                        List<String> questList = CustomConfigClass.get().getStringList("CustomQuests");
                        player.sendMessage(ChatColor.GREEN + "==========ALL CUSTOM QUESTS==========\n" +
                                ChatColor.YELLOW + "Custom quests require you to complete some task not trackable by the game." +
                                " \nThis could be \"complete the hub parkour!\" or \"find where Phoned lives in real life!\"\n" +
                                " \n" + ChatColor.LIGHT_PURPLE + questList.toString());
                        if (questList.isEmpty()) {
                            player.sendMessage(ChatColor.YELLOW + "There are no existing custom quests right now!");
                        }
                    }
                    else {
                        player.sendMessage(ChatColor.RED + "No valid arguments! Try /quests incomplete, /quests unclaimed, or /quests claimed");
                    }
                }
                else{
                    List<String> baseList=null;
                    String type="";
                    if(args[0].equalsIgnoreCase("incomplete")){
                        baseList = CustomConfigClass.get().getStringList("AdvancementQuests");
                        baseList.addAll(CustomConfigClass.get().getStringList("CollectionQuests"));
                        baseList.addAll(CustomConfigClass.get().getStringList("StatisticQuests"));
                        baseList.addAll(CustomConfigClass.get().getStringList("CustomQuests"));
                        baseList.removeAll(PlayerQuestInfo.get().getStringList(player.getUniqueId().toString() + ".Unclaimed"));
                        baseList.removeAll(PlayerQuestInfo.get().getStringList(player.getUniqueId().toString() + ".Claimed"));
                        type="INCOMPLETE";
                    }
                    else if(args[0].equalsIgnoreCase("unclaimed")){
                        baseList=PlayerQuestInfo.get().getStringList(player.getUniqueId().toString() + ".Unclaimed");
                        type="UNCLAIMED";
                    }
                    else if(args[0].equalsIgnoreCase("claimed")){
                        baseList=PlayerQuestInfo.get().getStringList(player.getUniqueId().toString() + ".Claimed");
                        type="CLAIMED";
                    }
                    else{
                        player.sendMessage(ChatColor.RED + "No valid arguments! Try /quests incomplete <type>, /quests unclaimed <type>, or /quests claimed <type>");
                        return true;
                    }

                    if(args[1].equalsIgnoreCase("advancement")){
                        baseList.removeAll(CustomConfigClass.get().getStringList("CollectionQuests"));
                        baseList.removeAll(CustomConfigClass.get().getStringList("StatisticQuests"));
                        baseList.removeAll(CustomConfigClass.get().getStringList("CustomQuests"));
                        player.sendMessage(ChatColor.GREEN + "=========="+type+" ADVANCEMENT QUESTS==========\n" +
                                ChatColor.YELLOW + "Advancement quests require you to complete a vanilla minecraft advancement." +
                                " \n" +
                                " \n" + ChatColor.LIGHT_PURPLE + baseList.toString());
                        if (baseList.isEmpty()) {
                            player.sendMessage(ChatColor.YELLOW + "There are no advancement quests here right now!");
                        }
                    }
                    else if(args[1].equalsIgnoreCase("collect") || args[1].equalsIgnoreCase("collection")){
                        baseList.removeAll(CustomConfigClass.get().getStringList("StatisticQuests"));
                        baseList.removeAll(CustomConfigClass.get().getStringList("CustomQuests"));
                        baseList.removeAll(CustomConfigClass.get().getStringList("AdvancementQuests"));
                        player.sendMessage(ChatColor.GREEN + "=========="+type+" COLLECTION QUESTS==========\n" +
                                ChatColor.YELLOW + "Collection quests require you to obtain a certain amount of a specific item." +
                                " \n" +
                                " \n" + ChatColor.LIGHT_PURPLE + baseList.toString());
                        if (baseList.isEmpty()) {
                            player.sendMessage(ChatColor.YELLOW + "There are no collection quests here right now!");
                        }
                    }
                    else if(args[1].equalsIgnoreCase("statistic")){
                        baseList.removeAll(CustomConfigClass.get().getStringList("CollectionQuests"));
                        baseList.removeAll(CustomConfigClass.get().getStringList("CustomQuests"));
                        baseList.removeAll(CustomConfigClass.get().getStringList("AdvancementQuests"));
                        player.sendMessage(ChatColor.GREEN + "=========="+type+" STATISTIC QUESTS==========\n" +
                                ChatColor.YELLOW + "Statistic quests require you to get some stat from the vanilla statistics menu to a certain value." +
                                "\nThese can include \"kill_entity:drowned\" or \"interactions_with_smoker\" for some reason \n" +
                                " \n" + ChatColor.LIGHT_PURPLE + baseList.toString());
                        if (baseList.isEmpty()) {
                            player.sendMessage(ChatColor.YELLOW + "There are no statistic quests here right now!");
                        }
                    }
                    else if(args[1].equalsIgnoreCase("custom")){
                        baseList.removeAll(CustomConfigClass.get().getStringList("StatisticQuests"));
                        baseList.removeAll(CustomConfigClass.get().getStringList("CollectionQuests"));
                        baseList.removeAll(CustomConfigClass.get().getStringList("AdvancementQuests"));
                        player.sendMessage(ChatColor.GREEN + "=========="+type+" CUSTOM QUESTS==========\n" +
                                ChatColor.YELLOW + "Custom quests require you to complete some task not trackable by the game." +
                                " \nThis could be \"complete the hub parkour!\" or \"find where Phoned lives in real life!\"\n" +
                                " \n" + ChatColor.LIGHT_PURPLE + baseList.toString());
                        if (baseList.isEmpty()) {
                            player.sendMessage(ChatColor.YELLOW + "There are no custom quests here right now!");
                        }
                    }
                    else {
                        player.sendMessage(ChatColor.RED + "No valid arguments! Valid types are advancement, collection, statistic, custom");
                    }
                }
            }
            else if(command.getName().equalsIgnoreCase("questclaim")){
                List<String> unclaimed=PlayerQuestInfo.get().getStringList(player.getUniqueId().toString()+".Unclaimed");
                List<String> claimed=PlayerQuestInfo.get().getStringList(player.getUniqueId().toString()+".Claimed");
                if(args.length!=1){
                    player.sendMessage(ChatColor.RED+"Not enough arguments. Try either /questclaim <name> or /questclaim all");
                }
                else if(unclaimed.isEmpty() || unclaimed==null){
                    player.sendMessage(ChatColor.RED+"You have no unclaimed quests!");
                }
                else if(args[0].equalsIgnoreCase("help")){
                    player.sendMessage(ChatColor.GREEN + "==========/QUESTCLAIM==========\n" +
                            ChatColor.AQUA + "/questclaim <name>"+ChatColor.YELLOW + " will grant you the reward of a quest you've completed." +
                            "\nYou can either run "+ChatColor.AQUA+"/questclaim <name>"+ChatColor.YELLOW + " to get the reward of a single quest or "+ChatColor.AQUA+"/questclaim all "+ChatColor.YELLOW +"to claim all of your completed quests instantly." +
                            "\nBe careful! If the reward is an item, it will be dropped as an entity at your location. Don't claim the quest if you aren't in a safe location or your inventory is full!");
                }
                else if(args[0].equalsIgnoreCase("all")){
                    for(String currQuest:unclaimed) {
                        player.sendMessage(ChatColor.GREEN+"Successfully claimed "+ChatColor.GOLD+"\""+currQuest+"\"");
                        rewardHandler(currQuest,player);
                        rewardNotifyer(currQuest, player);
                    }

                    claimed.addAll(unclaimed);
                    PlayerQuestInfo.get().set(player.getUniqueId().toString()+".Claimed",claimed);
                    unclaimed.clear();
                    PlayerQuestInfo.get().set(player.getUniqueId().toString()+".Unclaimed",unclaimed);
                    try {
                        PlayerQuestInfo.save();
                    } catch (IOException e) {
                        //ignore
                    }
                }
                else if(unclaimed.contains(args[0])){
                    rewardHandler(args[0], player);
                    unclaimed.remove(args[0]);
                    PlayerQuestInfo.get().set(player.getUniqueId().toString()+".Unclaimed",unclaimed);
                    claimed.add(args[0]);
                    PlayerQuestInfo.get().set(player.getUniqueId().toString()+".Claimed",claimed);
                    try {
                        PlayerQuestInfo.save();
                    } catch (IOException e) {
                        //ignore
                    }
                    player.sendMessage(ChatColor.GREEN+"Successfully claimed "+ChatColor.GOLD+"\""+args[0]+"\"");
                    rewardNotifyer(args[0], player);
                }
                else{
                    player.sendMessage(ChatColor.RED+"Argument is not a valid quest name or 'all'");
                }
            }
            else if(command.getName().equalsIgnoreCase("questinfo")){
                if(args.length<1){
                    player.sendMessage(ChatColor.RED+"Not enough arguments. /questinfo <quest name>");
                }
                else if(args[0].equalsIgnoreCase("help")){
                    player.sendMessage(ChatColor.GREEN + "==========/QUESTINFO==========\n" +
                            ChatColor.AQUA + "/questinfo <name>"+ChatColor.YELLOW + " will provide all the information you need about the given quest.\n" +
                            "It will tell you the name, type, requirements and reward of the quest.\n" +
                            "Quest Type and information will tell you what you have to do to complete the quest.\n" +
                            "The types are:" +
                            ChatColor.AQUA +"\nAdvancement:"+ChatColor.YELLOW +" complete the listed advancement" +
                            ChatColor.AQUA +"\nCollection:"+ChatColor.YELLOW +" collect a certain amount of the listed item in your inventory. (if this isn't working, try dropping the item and picking it back up)" +
                            ChatColor.AQUA +"\nStatistic:"+ChatColor.YELLOW +" get your value of the listed statistic (can be viewed in the vanilla statistics menu) to the value." +
                            "\n     (if your statistic value is right, but you haven't been granted the quest, increase the statistic one more time.)");
                }
                else{
                    String type=CustomConfigClass.get().getString("Quests."+args[0]+".type");
                    if(type==null){
                        player.sendMessage(ChatColor.RED+"Quest \""+args[0]+"\" does not exist!");
                    }
                    else{
                        player.sendMessage(ChatColor.GREEN+"==========QUEST INFO==========\n" +
                                ChatColor.GOLD+"Name: "+ChatColor.AQUA+args[0]+"\n" +
                                ChatColor.GOLD+"Quest Type: "+ChatColor.AQUA+type+"\n" +
                                ChatColor.GOLD+"Info:\n");
                        if(type.equalsIgnoreCase("advancement")){
                            NamespacedKey advanceKey=NamespacedKey.fromString(CustomConfigClass.get().getString("Quests."+args[0]+".data"));
                            Advancement advancementQuest=Bukkit.getAdvancement(advanceKey);
                            player.sendMessage(ChatColor.LIGHT_PURPLE+"Complete the advancement: " +
                                    ChatColor.YELLOW+""+advancementQuest.getDisplay().getTitle());
                        }
                        else if(type.equalsIgnoreCase("collect")){
                            player.sendMessage(ChatColor.LIGHT_PURPLE+"Collect " +
                                    ChatColor.YELLOW+""+CustomConfigClass.get().getInt("Quests."+args[0]+".number")+" " +
                                    CustomConfigClass.get().getString("Quests."+args[0]+".data")+"(s)");
                        }
                        else if(type.equalsIgnoreCase("statistic")){
                            player.sendMessage(ChatColor.LIGHT_PURPLE+"Get the statistic " +
                                    ChatColor.YELLOW+""+CustomConfigClass.get().getString("Quests."+args[0]+".data")+ChatColor.LIGHT_PURPLE+" to " +
                                    ChatColor.YELLOW+CustomConfigClass.get().getInt("Quests."+args[0]+".number"));
                        }
                        else if(type.equalsIgnoreCase("custom")){
                            player.sendMessage(ChatColor.YELLOW+""+CustomConfigClass.get().getString("Quests."+args[0]+".data"));
                        }
                        player.sendMessage(ChatColor.GOLD+"\nReward: \n");
                        String rewardType=CustomConfigClass.get().getString("Quests."+args[0]+".reward.type");
                        if(rewardType.equalsIgnoreCase("item")){
                            player.sendMessage(ChatColor.LIGHT_PURPLE+"You receive: " +
                                    ChatColor.YELLOW+""+CustomConfigClass.get().getString("Quests."+args[0]+".reward.item_count")+" " +
                                    ""+CustomConfigClass.get().getString("Quests."+args[0]+".reward.item")+"(s)");
                        }
                        else if(rewardType.equalsIgnoreCase("command")){
                            player.sendMessage(ChatColor.LIGHT_PURPLE+"Executes the command: " +
                                    ChatColor.YELLOW+""+CustomConfigClass.get().getString("Quests."+args[0]+".reward.command"));
                        }
                        else if(rewardType.equalsIgnoreCase("flair")){
                            player.sendMessage(ChatColor.LIGHT_PURPLE+"Grants access to the flair: " +
                                    ChatColor.RESET+""+CustomConfigClass.get().getString("Quests."+args[0]+".reward.flair").replace('&', '§'));
                        }
                        else if(rewardType.equalsIgnoreCase("experience")){
                            player.sendMessage(ChatColor.LIGHT_PURPLE+"You receive: " +
                                    ChatColor.YELLOW+""+CustomConfigClass.get().getString("Quests."+args[0]+".reward.levels")+ChatColor.LIGHT_PURPLE+" EXP levels");
                        }
                    }
                }
            }
            else if(command.getName().equalsIgnoreCase("questcreate")){
                if(player.hasPermission("questingcraft.create")){
                    //questcreate <name> <action_type> <action_data> <number> <reward_type> <reward_data>
                    if(args.length==1&&args[0].equalsIgnoreCase("help")){
                        player.sendMessage(ChatColor.GREEN+"==========CREATING A QUEST==========\n"
                                +ChatColor.YELLOW+"To make a quest you have to use /questcreate <name> <action_type> <action_data> <number> <reward_type> <reward_data>." +
                                ChatColor.LIGHT_PURPLE+"\nName "+ChatColor.YELLOW+"is the name of the quest\n" +
                                ChatColor.LIGHT_PURPLE+"Action_type "+ChatColor.YELLOW+"is what the player has to do (advancement,statistic,collect)\n" +
                                ChatColor.LIGHT_PURPLE+"action_data "+ChatColor.YELLOW+"is what the player has to do related to type (if type is statistic, data should be 'kill_entity:zombie', if its collect, data should be 'eggs')\n" +
                                ChatColor.LIGHT_PURPLE+"Number "+ChatColor.YELLOW+"is applicable if its a counting quest (kill mob, get item), this will be ignored if not needed\n" +
                                ChatColor.LIGHT_PURPLE+"reward_type "+ChatColor.YELLOW+"is what the player gets for completing the quest (item, flair, command, experience)\n" +
                                ChatColor.LIGHT_PURPLE+"reward_data "+ChatColor.YELLOW+"is information about what the player gets. If its a flair, its the flair, if its an item, its the item and how many, if its command " +
                                "its the command that runs, and if it's experience, it's how many levels they receive\n" +
                                "Example: /questcreate Eggs! collect egg 17 flair &3EggFinder");
                    }
                    else if(args.length>1&&(args[0].equalsIgnoreCase("help")||args[0].equalsIgnoreCase("all"))){
                        player.sendMessage(ChatColor.LIGHT_PURPLE+"I'm not going to let you name a quest 'help' or 'all' that'll break so much shit -Fletch");
                    }
                    else if(args.length<6){
                        player.sendMessage(ChatColor.YELLOW+"Missing arguments, remember: /questcreate <name> <action_type> <action_data> <number> <reward_type> <reward_data>\nDo " +
                                "/questcreate help for a deeper explanation");
                    }
                    else{
                        String questName=args[0];
                        String action_type=args[1];
                        String action_data=args[2];
                        String number=args[3];
                        String reward=args[4];
                        String reward_data=args[5];

                        if(action_type.equalsIgnoreCase("advancement")){
                            NamespacedKey advanceKey=NamespacedKey.fromString(action_data);
                            Advancement advancementQuest=Bukkit.getAdvancement(advanceKey);

                            if(advancementQuest==null){
                                player.sendMessage(ChatColor.RED+"That isn't a valid advancement!");
                                return true;
                            }
                            String outerPath="Quests."+questName;
                            CustomConfigClass.get().set(outerPath+".type", "advancement");
                            CustomConfigClass.get().set(outerPath+".data", action_data);
                            CustomConfigClass.get().set(outerPath+".reward.type", reward);
                            List<String> advancementQuests = new ArrayList<>();
                            if(CustomConfigClass.get().getList("AdvancementQuests")!=null) {
                                advancementQuests = new ArrayList<String>((Collection<? extends String>) CustomConfigClass.get().getList("AdvancementQuests"));
                                if(!CustomConfigClass.get().getList("AdvancementQuests").contains(questName)){
                                    advancementQuests.add(questName);
                                    CustomConfigClass.get().set("AdvancementQuests", advancementQuests);
                                }
                            }
                            else{
                                advancementQuests.add(questName);
                                CustomConfigClass.get().set("AdvancementQuests", advancementQuests);
                            }
                            boolean successWithSetup=setupQuestReward(args, player);
                            if(successWithSetup) {
                                try {
                                    CustomConfigClass.save();
                                    player.sendMessage(ChatColor.GREEN+"Successfully created the quest "+ChatColor.GOLD+"\""+questName+"\"");
                                } catch (IOException e) {
                                    //ignore
                                }
                            }
                        }
                        else if(action_type.equalsIgnoreCase("statistic")){
                            Statistic statForQuest;
                            String[] info=action_data.split(":");
                            String statToUse=info[0];
                            try{
                                statForQuest=Statistic.valueOf(statToUse.toUpperCase(Locale.ROOT));
                            }
                            catch (Exception sta){
                                player.sendMessage(ChatColor.RED+"That isn't a real statistic!");
                                return true;
                            }

                            int realNumber=-15;
                            try{
                                realNumber=Integer.parseInt(number);
                            }
                            catch(Exception e){
                                player.sendMessage(ChatColor.RED+"Not a real number!");
                                return true;
                            }
                            if(statForQuest.isSubstatistic()){
                                if(!action_data.contains(":")){
                                    player.sendMessage(ChatColor.RED+"This is a substatistic, you have to include more info, like kill_entity:zombie");
                                    return true;
                                }
                                else{
                                    if(statForQuest.isBlock()){
                                        //check is block
                                        Material statBlock=Material.EGG;
                                        try{
                                            statBlock=Material.valueOf(info[1].toUpperCase(Locale.ROOT));
                                        }
                                        catch (Exception i){
                                            player.sendMessage(ChatColor.RED+"Not a valid block for a block statistic!");
                                            return true;
                                        }
                                    }
                                    else if(statForQuest.getType()== Statistic.Type.ENTITY){
                                        //mob
                                        EntityType statMob=EntityType.ZOMBIE;
                                        try{
                                            statMob=EntityType.valueOf(info[1].toUpperCase(Locale.ROOT));
                                        }
                                        catch (Exception i){
                                            player.sendMessage(ChatColor.RED+"Not a valid mob for a mob statistic!");
                                            return true;
                                        }
                                    }
                                    else if(statForQuest.getType()== Statistic.Type.ITEM){
                                        //item
                                        Material statBlock=Material.EGG;
                                        try{
                                            statBlock=Material.valueOf(info[1].toUpperCase(Locale.ROOT));
                                        }
                                        catch (Exception i){
                                            player.sendMessage(ChatColor.RED+"Not a valid item for an item statistic!");
                                            return true;
                                        }
                                    }
                                }
                            }
                            String outerPath="Quests."+questName;
                            CustomConfigClass.get().set(outerPath+".type", "statistic");
                            CustomConfigClass.get().set(outerPath+".data", action_data);
                            CustomConfigClass.get().set(outerPath+".number", realNumber);
                            CustomConfigClass.get().set(outerPath+".reward.type", reward);
                            List<String> statisticQuests = new ArrayList<>();
                            if(CustomConfigClass.get().getList("StatisticQuests")!=null) {
                                statisticQuests = new ArrayList<String>((Collection<? extends String>) CustomConfigClass.get().getList("StatisticQuests"));
                                if(!CustomConfigClass.get().getList("StatisticQuests").contains(questName)){
                                    statisticQuests.add(questName);
                                    CustomConfigClass.get().set("StatisticQuests", statisticQuests);
                                }
                            }
                            else{
                                statisticQuests.add(questName);
                                CustomConfigClass.get().set("StatisticQuests", statisticQuests);
                            }
                            boolean successWithSetup=setupQuestReward(args, player);
                            if(successWithSetup) {
                                try {
                                    CustomConfigClass.save();
                                    player.sendMessage(ChatColor.GREEN+"Successfully created the quest "+ChatColor.GOLD+"\""+questName+"\"");
                                } catch (IOException e) {
                                    //ignore
                                }
                            }

                        }
                        else if(action_type.equalsIgnoreCase("collect")){
                            int realNumber=-15;
                            try{
                                realNumber=Integer.parseInt(number);
                            }
                            catch(Exception e){
                                player.sendMessage(ChatColor.RED+"Not a real number!");
                                return true;
                            }

                            Material itemToCollect=Material.EGG;
                            try{
                                itemToCollect=Material.valueOf(action_data.toUpperCase(Locale.ROOT));
                            }
                            catch (Exception i){
                                player.sendMessage(ChatColor.RED+"Player cannot collect that, it isn't a real item.");
                                return true;
                            }

                            String outerPath="Quests."+questName;
                            CustomConfigClass.get().set(outerPath+".type", "collect");
                            CustomConfigClass.get().set(outerPath+".data", action_data);
                            CustomConfigClass.get().set(outerPath+".number", realNumber);
                            CustomConfigClass.get().set(outerPath+".reward.type", reward);
                            List<String> collectionQuests = new ArrayList<>();
                            if(CustomConfigClass.get().getList("CollectionQuests")!=null) {
                                collectionQuests = new ArrayList<String>((Collection<? extends String>) CustomConfigClass.get().getList("CollectionQuests"));
                                if(!CustomConfigClass.get().getList("CollectionQuests").contains(questName)){
                                    collectionQuests.add(questName);
                                    CustomConfigClass.get().set("CollectionQuests", collectionQuests);
                                }
                            }
                            else{
                                collectionQuests.add(questName);
                                CustomConfigClass.get().set("CollectionQuests", collectionQuests);
                            }
                            boolean successWithSetup=setupQuestReward(args, player);
                            if(successWithSetup) {
                                try {
                                    CustomConfigClass.save();
                                    player.sendMessage(ChatColor.GREEN+"Successfully created the quest "+ChatColor.GOLD+"\""+questName+"\"");
                                } catch (IOException e) {
                                    //ignore
                                }
                            }

                        }
                        else if(action_type.equalsIgnoreCase("custom")){
                            player.sendMessage("Custom Quest!");
                        }
                        else{
                            player.sendMessage(ChatColor.RED+"Not a valid quest type");
                        }
                    }
                }
                else{
                    player.sendMessage(ChatColor.RED+"Don't have permission, sorry!");
                }
            }
            else if(command.getName().equalsIgnoreCase("flairs")){
                int page=1;

                if(args.length==0){
                    page=1;
                }
                else{
                    try{
                        page=Integer.parseInt(args[0]);
                    }catch (Exception e){
                        player.sendMessage(ChatColor.RED+"That isn't a valid page number");
                        return true;
                    }
                }
                List<String> availableFlairs=PlayerQuestInfo.get().getStringList(player.getUniqueId().toString()+".flairs");
                if(availableFlairs==null || availableFlairs.isEmpty()){
                    player.sendMessage(ChatColor.LIGHT_PURPLE+"You don't have any flairs right now! Do some quests and get some!");
                }
                else{
                    String flairsString="";
                    int flairIndex=0;
                    while(flairIndex<10 && flairIndex+(10*page-10)<availableFlairs.size()){
                        flairsString+=ChatColor.LIGHT_PURPLE+""+(flairIndex+1+(10*page-10))+". "+
                                ChatColor.RESET+availableFlairs.get(flairIndex+(10*page-10)).replace('&','§')+"\n";
                        flairIndex+=1;
                    }

                    player.sendMessage(ChatColor.GREEN+"==========AVAILABLE FLAIRS==========\n" +
                            flairsString);
                    if(flairsString.equalsIgnoreCase("")){
                        player.sendMessage(ChatColor.YELLOW+"This page is empty! Either you don't have any flairs, or you picked a page too far ahead. Try /flairs 1\n");
                    }
                    player.sendMessage(ChatColor.GREEN+"To set your flair, run /flairset <number>");
                    player.sendMessage(ChatColor.GREEN+"If you have more than 10 flairs, run /flairs <pageNumber> to see more");
                }
            }
            else if(command.getName().equalsIgnoreCase("flairset")){
                if(args.length<1){
                    player.sendMessage(ChatColor.RED+"Not enough arguments. Try /flairset <number>.");
                }
                else{
                    int flairNumber=0;
                    try{
                        flairNumber=Integer.parseInt(args[0]);
                    } catch (Exception e) {
                        player.sendMessage(ChatColor.RED+"Not a valid number!");
                        return true;
                    }
                    List<String> flairs=PlayerQuestInfo.get().getStringList(player.getUniqueId().toString()+".flairs");
                    String flair="A";
                    try {
                        flair=flairs.get(flairNumber-1);
                    }catch (Exception e){
                        player.sendMessage(ChatColor.RED+"That isn't a valid flair number. Run /flairs <page> to see your options.");
                        return true;
                    }

                    String commandForFlair="lp user "+player.getUniqueId().toString()+" meta setprefix 10 \""+flair+" &r\"";
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), commandForFlair);
                    player.sendMessage(ChatColor.GREEN+"Successfully set your flair to "+ChatColor.RESET+flair.replace('&','§'));
                }
            }
            else if(command.getName().equalsIgnoreCase("questgrant")){
                if(player.hasPermission("questingcraft.grant")){
                    if(args.length<2){
                        player.sendMessage(ChatColor.RED+"Not enough arguments! /questgrant <playername> <questname>");
                    }
                    else{
                        String playerName=args[0];
                        String questName=args[1];
                        Player grantedPlayer=null;
                        if(playerName.equalsIgnoreCase("@p") || playerName.equalsIgnoreCase("@s")){
                            playerName=player.getName();
                        }
                        grantedPlayer=Bukkit.getPlayer(playerName);
                        if(grantedPlayer==null) {
                            player.sendMessage(ChatColor.RED + "Player \"" + playerName + "\" does not exist!");
                            return true;
                        }
                        String type=CustomConfigClass.get().getString("Quests."+questName+".type");
                        if(type==null){
                            player.sendMessage(ChatColor.RED+"Quest \""+args[0]+"\" does not exist!");
                            return true;
                        }
                        String playerUUID=grantedPlayer.getUniqueId().toString();
                        List<String> unclaimed=PlayerQuestInfo.get().getStringList(playerUUID+".Unclaimed");
                        List<String> claimed=PlayerQuestInfo.get().getStringList(playerUUID+".Claimed");
                        if(claimed.contains(questName) || unclaimed.contains(questName)){
                            player.sendMessage(ChatColor.RED+"That player already completed this quest!");
                            return true;
                        }
//                        grantedPlayer.sendMessage(org.bukkit.ChatColor.GOLD+"You did the quest " +
//                                org.bukkit.ChatColor.LIGHT_PURPLE+"\""+questName+"\"" +
//                                org.bukkit.ChatColor.GOLD+", Nice Work!");
                        Bukkit.broadcastMessage(org.bukkit.ChatColor.AQUA + grantedPlayer.getName()+ org.bukkit.ChatColor.GOLD +" did the quest " +
                                org.bukkit.ChatColor.LIGHT_PURPLE+"\""+questName+"\"" +
                                org.bukkit.ChatColor.GOLD+", Nice Work!");
                        String messageNoColor="**"+grantedPlayer.getName() +"** did the quest **"+"\""+questName+"\""+"**, Nice Work!";
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "discord broadcast :mcfrog: "+messageNoColor);
                        unclaimed.add(questName);
                        PlayerQuestInfo.get().set(grantedPlayer.getUniqueId().toString()+".Unclaimed",unclaimed);
                        try {
                            PlayerQuestInfo.save();
                        } catch (Exception e) {
                            //ignore
                        }
                        player.sendMessage(ChatColor.GREEN+"Successfully granted "+ChatColor.YELLOW+playerName+ChatColor.GREEN+" the quest "+ChatColor.YELLOW+questName);
                    }
                }
                else{
                    player.sendMessage(ChatColor.RED+"You don't have permission, sorry!");
                }
            }
            else if(command.getName().equalsIgnoreCase("questcreatecustom")){
                // /questcreatecustom <name> <reward_type> <reward data> : <description>
                //then do an update later
                if(player.hasPermission("questingcraft.create")) {
                    if(args.length==1 && args[0].equalsIgnoreCase("help")){
                        player.sendMessage(ChatColor.GREEN + "=========='CUSTOM' QUESTS==========\n" +
                                ChatColor.YELLOW + "A 'custom' quest is one that isn't trackable by the game. Like 'complete the parkour' or 'Meet one of the staff'" +
                                "\nThese have to be "+ChatColor.ITALIC+"granted"+ChatColor.RESET+ChatColor.YELLOW+" using "+ChatColor.AQUA+"/questgrant <playername> <questname>" +
                                ChatColor.YELLOW+"\nThe format for these are "+ChatColor.AQUA+"/questcreatecustom <name> <reward_type> <reward_data> : <description>" +
                                ChatColor.YELLOW+"\nName, reward_type, and reward_data are the same as normal quests and the : is required for the formatter." +
                                "\nDescription is what /questinfo will tell the player to do, like 'complete the parkour in the hub!'");
                    }
                    else if (args.length < 5) {
                        player.sendMessage(ChatColor.RED + "Not enough arguments! /questcreatecustom <name> <reward_type> <reward_data> : <description>");
                    }
                    else if(args.length>1&&(args[0].equalsIgnoreCase("help")||args[0].equalsIgnoreCase("all"))){
                        player.sendMessage(ChatColor.LIGHT_PURPLE+"I'm not going to let you name a quest 'help' or 'all' that'll break so much shit -Fletch");
                    }
                    else{
                        int i=0;
                        int splitIndex=0;
                        boolean foundColon=false;
                        while (i<args.length && !foundColon){
                            if(args[i].equalsIgnoreCase(":")){
                                foundColon=true;
                                splitIndex=i;
                            }
                            i=i+1;
                        }
                        if(!foundColon || splitIndex<3){
                            player.sendMessage(ChatColor.RED+"Formatting is wrong! /questcreatecustom <name> <reward_type> <reward data> : <description>");
                            return true;
                        }

                        List<String> fakeArgs=new ArrayList<>();
                        //questcreate <name> <action_type> <action_data> <number> <reward> <reward_data>
                        fakeArgs.add(args[0]);
                        fakeArgs.add("action_type");
                        fakeArgs.add("action_data");
                        fakeArgs.add("number");
                        for(int j=1; j<splitIndex; j++){
                            fakeArgs.add(args[j]);
                        }

                        String description="";
                        for(int p=splitIndex+1; p<args.length; p++){
                            description=description+args[p]+" ";
                        }
                        String questName=args[0];
                        description=description.substring(0, description.length()-1);
                        String outerPath="Quests."+args[0];
                        CustomConfigClass.get().set(outerPath+".type", "custom");
                        CustomConfigClass.get().set(outerPath+".data", description);
                        CustomConfigClass.get().set(outerPath+".reward.type", args[1]);
                        List<String> customQuests = new ArrayList<>();
                        if(CustomConfigClass.get().getList("CustomQuests")!=null) {
                            customQuests = new ArrayList<String>((Collection<? extends String>) CustomConfigClass.get().getList("CustomQuests"));
                            if(!CustomConfigClass.get().getList("CustomQuests").contains(questName)){
                                customQuests.add(questName);
                                CustomConfigClass.get().set("CustomQuests", customQuests);
                            }
                        }
                        else{
                            customQuests.add(questName);
                            CustomConfigClass.get().set("CustomQuests", customQuests);
                        }

                        boolean successfulSetup=setupQuestReward(fakeArgs.toArray(new String[0]), player);
                        if(successfulSetup) {
                            try {
                                CustomConfigClass.save();
                                player.sendMessage(ChatColor.GREEN+"Successfully created the quest "+ChatColor.GOLD+"\""+questName+"\"");
                            } catch (IOException e) {
                                //ignore
                            }
                        }
                    }
                }
                else{
                    player.sendMessage(ChatColor.RED+"Don't have permission!");
                }
            }
            else if(command.getName().equalsIgnoreCase("questdelete")){
                if(player.hasPermission("questingcraft.delete")){
                    if(args.length==1) {
                        List<String> allQuests = CustomConfigClass.get().getStringList("AdvancementQuests");
                        allQuests.addAll(CustomConfigClass.get().getStringList("CollectionQuests"));
                        allQuests.addAll(CustomConfigClass.get().getStringList("StatisticQuests"));
                        allQuests.addAll(CustomConfigClass.get().getStringList("CustomQuests"));
                        if (allQuests.contains(args[0])){
                            CustomConfigClass.get().set("Quests."+args[0], null);
                            List<String> advance=CustomConfigClass.get().getStringList("AdvancementQuests");
                            advance.remove(args[0]);
                            List<String> collection=CustomConfigClass.get().getStringList("CollectionQuests");
                            collection.remove(args[0]);
                            List<String> stat=CustomConfigClass.get().getStringList("StatisticQuests");
                            stat.remove(args[0]);
                            List<String> custom=CustomConfigClass.get().getStringList("CustomQuests");
                            custom.remove(args[0]);

                            CustomConfigClass.get().set("AdvancementQuests", advance);
                            CustomConfigClass.get().set("CollectionQuests", collection);
                            CustomConfigClass.get().set("StatisticQuests", stat);
                            CustomConfigClass.get().set("CustomQuests", custom);

                            player.sendMessage(ChatColor.GREEN+"Successfully deleted \""+args[0]+"\"");
                            try {
                                CustomConfigClass.save();
                            } catch (Exception e) {
                                //ignore
                            }
                        }
                        else{
                            player.sendMessage(ChatColor.RED+"That quest doesn't exist!");
                        }
                    }
                    else{
                        player.sendMessage(ChatColor.RED+"Incorrect number of arguments. /questdelete <name>");
                    }
                }
                else {
                    player.sendMessage(ChatColor.RED+"Don't have permission!");
                }
            }
            else if(command.getName().equalsIgnoreCase("questcheck")){
                String playerUUID=player.getUniqueId().toString();
                List<String> unclaimed=PlayerQuestInfo.get().getStringList(playerUUID+".Unclaimed");
                List<String> claimed=PlayerQuestInfo.get().getStringList(playerUUID+".Claimed");
                if(args.length==0){
                    player.sendMessage(ChatColor.RED+"No quest provided. /questcheck <questname>");
                    return true;
                }
                if(unclaimed.contains(args[0]) || claimed.contains(args[0])){
                    player.sendMessage(ChatColor.RED+"You already have that quest!");
                    return true;
                }
                if(args[0].equalsIgnoreCase("help")){
                    player.sendMessage(ChatColor.AQUA+"/questcheck <questname> "+ChatColor.YELLOW+"will check your playerdata to see if you've met the conditions for the given quest");
                }
                else{
                    String type=CustomConfigClass.get().getString("Quests."+args[0]+".type");
                    if(type==null){
                        player.sendMessage(ChatColor.RED+"Quest \""+args[0]+"\" does not exist!");
                        return true;
                    }
                    else if(type.equalsIgnoreCase("custom")){
                        player.sendMessage(ChatColor.YELLOW+"Sorry, this is a "+ChatColor.AQUA+"custom quest,"+ChatColor.YELLOW+" it isn't tracked by the game, and has to be manually granted by an admin!");
                    }
                    else if(type.equalsIgnoreCase("collect")){
                        String item=CustomConfigClass.get().getString("Quests."+args[0]+".data");
                        int number=CustomConfigClass.get().getInt("Quests."+args[0]+".number");
                        int numberInInv=0;
                        HashMap<Integer, ? extends ItemStack> allOfItem = player.getInventory().all(Material.valueOf(item.toUpperCase(Locale.ROOT)));
                        for (HashMap.Entry<Integer, ? extends ItemStack> entry : allOfItem.entrySet()) {
                            numberInInv += entry.getValue().getAmount();
                        }
                        if(numberInInv>=number){
                            Bukkit.broadcastMessage(org.bukkit.ChatColor.AQUA + player.getName()+ org.bukkit.ChatColor.GOLD +" did the quest " +
                                    org.bukkit.ChatColor.LIGHT_PURPLE+"\""+args[0]+"\"" +
                                    org.bukkit.ChatColor.GOLD+", Nice Work!");
                            String messageNoColor="**"+player.getName() +" did the quest "+"\""+args[0]+"\""+", Nice Work!";
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "discord broadcast :mcfrog: "+messageNoColor);
                            unclaimed.add(args[0]);
                            PlayerQuestInfo.get().set(player.getUniqueId().toString()+".Unclaimed",unclaimed);
                            try {
                                PlayerQuestInfo.save();
                            } catch (Exception e) {
                                //ignore
                            }
                        }
                        else{
                            player.sendMessage(ChatColor.YELLOW+"Not done yet! You only have: "+ChatColor.AQUA+""+numberInInv);
                        }
                    }
                    else if(type.equalsIgnoreCase("advancement")){
                        String advancementName=CustomConfigClass.get().getString("Quests."+args[0]+".data");
                        if(player.getAdvancementProgress(Bukkit.getAdvancement(NamespacedKey.fromString(advancementName))).isDone()){
                            Bukkit.broadcastMessage(org.bukkit.ChatColor.AQUA + player.getName()+ org.bukkit.ChatColor.GOLD +" did the quest " +
                                    org.bukkit.ChatColor.LIGHT_PURPLE+"\""+args[0]+"\"" +
                                    org.bukkit.ChatColor.GOLD+", Nice Work!");
                            String messageNoColor="**"+player.getName() +" did the quest "+"\""+args[0]+"\""+", Nice Work!";
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "discord broadcast :mcfrog: "+messageNoColor);
                            unclaimed.add(args[0]);
                            PlayerQuestInfo.get().set(player.getUniqueId().toString()+".Unclaimed",unclaimed);
                            try {
                                PlayerQuestInfo.save();
                            } catch (Exception e) {
                                //ignore
                            }
                        }
                        else{
                            player.sendMessage(ChatColor.YELLOW+"Not done yet! You still need to complete the advancement: "+ChatColor.AQUA+Bukkit.getAdvancement(NamespacedKey.fromString(advancementName)).getDisplay().getTitle());
                        }
                    }
                    else if(type.equalsIgnoreCase("statistic")){
                        String[] pieces = CustomConfigClass.get().getString("Quests." + args[0] + ".data").toUpperCase(Locale.ROOT).split(":");
                        int count = CustomConfigClass.get().getInt("Quests." + args[0] + ".number");
                        Statistic baseStat=Statistic.valueOf(pieces[0]);
                        if (baseStat.isBlock() || baseStat.getType() == Statistic.Type.ITEM) {
                            Material wantedMat = Material.valueOf(pieces[1].toUpperCase(Locale.ROOT));
                            if(player.getStatistic(baseStat, wantedMat)>=count){
                                Bukkit.broadcastMessage(org.bukkit.ChatColor.AQUA + player.getName()+ org.bukkit.ChatColor.GOLD +" did the quest " +
                                        org.bukkit.ChatColor.LIGHT_PURPLE+"\""+args[0]+"\"" +
                                        org.bukkit.ChatColor.GOLD+", Nice Work!");
                                String messageNoColor="**"+player.getName() +"** did the quest **"+"\""+args[0]+"\""+"**, Nice Work!";
                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "discord broadcast :mcfrog: "+messageNoColor);
                                unclaimed.add(args[0]);
                                PlayerQuestInfo.get().set(player.getUniqueId().toString()+".Unclaimed",unclaimed);
                                try {
                                    PlayerQuestInfo.save();
                                } catch (Exception e) {
                                    //ignore
                                }
                            }
                            else{
                                player.sendMessage(ChatColor.YELLOW+"You haven't hit this stat yet! You're at: "+ChatColor.AQUA+""+player.getStatistic(baseStat, wantedMat));
                            }

                        } else if (baseStat.getType() == Statistic.Type.ENTITY) {
                            EntityType entityType = EntityType.valueOf(pieces[1].toUpperCase(Locale.ROOT));
                            if(player.getStatistic(baseStat, entityType)>=count){
                                Bukkit.broadcastMessage(org.bukkit.ChatColor.AQUA + player.getName()+ org.bukkit.ChatColor.GOLD +" did the quest " +
                                        org.bukkit.ChatColor.LIGHT_PURPLE+"\""+args[0]+"\"" +
                                        org.bukkit.ChatColor.GOLD+", Nice Work!");
                                String messageNoColor="**"+player.getName() +"** did the quest **"+"\""+args[0]+"\""+"**, Nice Work!";
                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "discord broadcast :mcfrog: "+messageNoColor);
                                unclaimed.add(args[0]);
                                PlayerQuestInfo.get().set(player.getUniqueId().toString()+".Unclaimed",unclaimed);
                                try {
                                    PlayerQuestInfo.save();
                                } catch (Exception e) {
                                    //ignore
                                }
                            }
                            else{
                                player.sendMessage(ChatColor.YELLOW+"You haven't hit this stat yet! You're at: "+ChatColor.AQUA+""+player.getStatistic(baseStat, entityType));
                            }

                        } else if (baseStat.getType() == Statistic.Type.UNTYPED) {
                            if(player.getStatistic(baseStat)>=count){
                                Bukkit.broadcastMessage(org.bukkit.ChatColor.AQUA + player.getName()+ org.bukkit.ChatColor.GOLD +" did the quest " +
                                        org.bukkit.ChatColor.LIGHT_PURPLE+"\""+args[0]+"\"" +
                                        org.bukkit.ChatColor.GOLD+", Nice Work!");
                                String messageNoColor="**"+player.getName() +"** did the quest **"+"\""+args[0]+"\""+"**, Nice Work!";
                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "discord broadcast :mcfrog: "+messageNoColor);
                                unclaimed.add(args[0]);
                                PlayerQuestInfo.get().set(player.getUniqueId().toString()+".Unclaimed",unclaimed);
                                try {
                                    PlayerQuestInfo.save();
                                } catch (Exception e) {
                                    //ignore
                                }
                            }
                            else{
                                player.sendMessage(ChatColor.YELLOW+"You haven't hit this stat yet! You're at: "+ChatColor.AQUA+""+player.getStatistic(baseStat));
                            }

                        }
                    }

                }
            }
        }
        else{
            if(command.getName().equalsIgnoreCase("questgrant")){
                    if(args.length<2){
                        sender.sendMessage(ChatColor.RED+"Not enough arguments! /questgrant <playername> <questname>");
                    }
                    else{
                        String playerName=args[0];
                        String questName=args[1];
                        Player grantedPlayer=null;

                        grantedPlayer=Bukkit.getPlayer(playerName);
                        if(grantedPlayer==null) {
                            sender.sendMessage(ChatColor.RED + "Player \"" + playerName + "\" does not exist!");
                            return true;
                        }
                        String type=CustomConfigClass.get().getString("Quests."+questName+".type");
                        if(type==null){
                            sender.sendMessage(ChatColor.RED+"Quest \""+args[0]+"\" does not exist!");
                            return true;
                        }
                        String playerUUID=grantedPlayer.getUniqueId().toString();
                        List<String> unclaimed=PlayerQuestInfo.get().getStringList(playerUUID+".Unclaimed");
                        List<String> claimed=PlayerQuestInfo.get().getStringList(playerUUID+".Claimed");
                        if(claimed.contains(questName) || unclaimed.contains(questName)){
                            sender.sendMessage(ChatColor.RED+"That player already completed this quest!");
                            return true;
                        }
//                        grantedPlayer.sendMessage(org.bukkit.ChatColor.GOLD+"You did the quest " +
//                                org.bukkit.ChatColor.LIGHT_PURPLE+"\""+questName+"\"" +
//                                org.bukkit.ChatColor.GOLD+", Nice Work!");
                        Bukkit.broadcastMessage(org.bukkit.ChatColor.AQUA + grantedPlayer.getName()+ org.bukkit.ChatColor.GOLD +" did the quest " +
                                org.bukkit.ChatColor.LIGHT_PURPLE+"\""+questName+"\"" +
                                org.bukkit.ChatColor.GOLD+", Nice Work!");
                        String messageNoColor="**"+grantedPlayer.getName() +"** did the quest **"+"\""+questName+"\""+"**, Nice Work!";
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "discord broadcast :mcfrog: "+messageNoColor);
                        unclaimed.add(questName);
                        PlayerQuestInfo.get().set(grantedPlayer.getUniqueId().toString()+".Unclaimed",unclaimed);
                        try {
                            PlayerQuestInfo.save();
                        } catch (Exception e) {
                            //ignore
                        }
                        sender.sendMessage(ChatColor.GREEN+"Successfully granted "+ChatColor.YELLOW+playerName+ChatColor.GREEN+" the quest "+ChatColor.YELLOW+questName);
                    }
            }
        }
        return true;
    }
    private boolean setupQuestReward(String[] args, Player player){
        String questName=args[0];
        String action_type=args[1];
        //String action_data=args[2];
        //String number=args[3];
        String reward_type=args[4];
        //String reward_data=args[5];
        if(reward_type.equalsIgnoreCase("item")){
            if(args.length<7){
                player.sendMessage(ChatColor.RED+"You didn't give enough information, item requires and item to give and a count.");
                return false;
            }
            String itemToGive=args[5];
            Material itemToCollect=Material.EGG;
            try{
                itemToCollect=Material.valueOf(itemToGive.toUpperCase(Locale.ROOT));
            }
            catch (Exception i){
                player.sendMessage(ChatColor.RED+"That's not a real reward item!");
                return false;
            }
            String itemsNumber=args[6];
            int realNumber=-15;
            try{
                realNumber=Integer.parseInt(itemsNumber);
            }
            catch(Exception e){
                player.sendMessage(ChatColor.RED+"Not a real number!");
                return false;
            }
            //everything is valid
            String outerPath="Quests."+questName+".reward";
            CustomConfigClass.get().set(outerPath+".item", itemToGive);
            CustomConfigClass.get().set(outerPath+".item_count",realNumber);
            return true;
        }
        else if(reward_type.equalsIgnoreCase("flair")){
            String wholeFlair="";
            for(int i=5; i<args.length; i++){
                wholeFlair=wholeFlair+args[i]+" ";
            }
            wholeFlair=wholeFlair.substring(0, wholeFlair.length()-1);
            String outerPath="Quests."+questName+".reward";
            CustomConfigClass.get().set(outerPath+".flair", wholeFlair);

            return true;
        }
        else if(reward_type.equalsIgnoreCase("command")){
            String wholeCommand="";
            for(int i=5; i<args.length; i++){
                wholeCommand=wholeCommand+args[i]+" ";
            }
            if(wholeCommand.charAt(0)=='/'){
                wholeCommand=wholeCommand.substring(1);
            }
            if(!wholeCommand.substring(0,10).equalsIgnoreCase("minecraft:")){
                wholeCommand="minecraft:"+wholeCommand;
            }
            String outerPath="Quests."+questName+".reward";
            CustomConfigClass.get().set(outerPath+".command", wholeCommand);
            player.sendMessage(ChatColor.YELLOW+"Make sure you put the command in correctly, I can't check if it's correct from here!");
            return true;
        }
        else if(reward_type.equalsIgnoreCase("experience")){
            String levelsNumber=args[5];
            int realNumber=-15;
            try{
                realNumber=Integer.parseInt(levelsNumber);
            }
            catch(Exception e){
                player.sendMessage(ChatColor.RED+"Not a real number!");
                return false;
            }
            String outerPath="Quests."+questName+".reward";
            CustomConfigClass.get().set(outerPath+".levels", realNumber);
            return true;
        }
        else{
            player.sendMessage(ChatColor.RED+"That isn't a valid reward option! Try item, flair, command, or experience.");
            return false;
        }
    }


    private void rewardHandler(String questThatWins, Player player){
        if(CustomConfigClass.get().getString("Quests."+questThatWins+".reward.type").equalsIgnoreCase("item")){
            String itemType=CustomConfigClass.get().getString("Quests."+questThatWins+".reward.item");
            int number=CustomConfigClass.get().getInt("Quests."+questThatWins+".reward.item_count");
            ItemStack itemStack = new ItemStack(Material.valueOf(itemType.toUpperCase(Locale.ROOT)), number);
            player.getWorld().dropItem(player.getLocation(), itemStack);

        }
        else if(CustomConfigClass.get().getString("Quests."+questThatWins+".reward.type").equalsIgnoreCase("flair")){
            List<String> flairs=PlayerQuestInfo.get().getStringList(player.getUniqueId().toString()+".flairs");
            flairs.add(CustomConfigClass.get().getString("Quests."+questThatWins+".reward.flair"));
            PlayerQuestInfo.get().set(player.getUniqueId().toString()+".flairs", flairs);
        }
        else if(CustomConfigClass.get().getString("Quests."+questThatWins+".reward.type").equalsIgnoreCase("command")){
            String dataCommand=CustomConfigClass.get().getString("Quests."+questThatWins+".reward.command");
            while (dataCommand.charAt(0)=='/' || dataCommand.charAt(0)==' '){
                dataCommand=dataCommand.substring(1);
            }
            String newCommand="execute as "+player.getUniqueId().toString()+" at "+player.getUniqueId().toString()+" run "+dataCommand;
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(),newCommand);
        }
        else if(CustomConfigClass.get().getString("Quests."+questThatWins+".reward.type").equalsIgnoreCase("experience")){
            player.giveExpLevels(CustomConfigClass.get().getInt("Quests."+questThatWins+".reward.levels"));
        }
    }

    private void rewardNotifyer(String questName, Player player){
        String rewardType=CustomConfigClass.get().getString("Quests."+questName+".reward.type");
        if(rewardType.equalsIgnoreCase("item")){
            player.sendMessage(ChatColor.YELLOW+"You receive "+ChatColor.LIGHT_PURPLE+CustomConfigClass.get().getString("Quests."+questName+".reward.item")+" x"
                    +CustomConfigClass.get().getString("Quests."+questName+".reward.item_count"));
        }
        else if(rewardType.equalsIgnoreCase("command")){
            player.sendMessage(ChatColor.YELLOW+"Executing the command "+ChatColor.LIGHT_PURPLE+CustomConfigClass.get().getString("Quests."+questName+".reward.command"));
        }
        else if(rewardType.equalsIgnoreCase("experience")){
            player.sendMessage(ChatColor.YELLOW+"You receive "+ChatColor.LIGHT_PURPLE+CustomConfigClass.get().getString("Quests."+questName+".reward.levels")+" xp levels");
        }
        else if(rewardType.equalsIgnoreCase("flair")){
            player.sendMessage(ChatColor.YELLOW+"You have been granted access to the flair "
                    +ChatColor.RESET+CustomConfigClass.get().getString("Quests."+questName+".reward.flair").replace('&','§')
                    +ChatColor.YELLOW+"\nUse /flairs to check it out.");
        }
    }
}
