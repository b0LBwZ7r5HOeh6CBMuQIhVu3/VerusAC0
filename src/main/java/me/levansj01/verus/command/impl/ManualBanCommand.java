package me.levansj01.verus.command.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import me.levansj01.verus.VerusPlugin;
import me.levansj01.verus.alert.manager.AlertManager;
import me.levansj01.verus.check.Check;
import me.levansj01.verus.check.checks.manual.ManualA;
import me.levansj01.verus.command.BaseCommand;
import me.levansj01.verus.data.PlayerData;
import me.levansj01.verus.data.manager.DataManager;
import me.levansj01.verus.lang.EnumMessage;
import me.levansj01.verus.storage.StorageEngine;
import me.levansj01.verus.storage.database.Database;
import me.levansj01.verus.storage.database.Log;
import me.levansj01.verus.util.BukkitUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;


public class ManualBanCommand
        extends BaseCommand {

    public List<String> tabComplete(CommandSender commandSender, String string, String[] stringArray) throws IllegalArgumentException {
        if (BukkitUtil.hasPermission((CommandSender)commandSender, (String)this.getPermission()) && (stringArray.length == 1 || stringArray.length == 2 && stringArray[0].equalsIgnoreCase("check"))) {
            Player player;
            if (commandSender instanceof Player) {
                player = (Player)commandSender;
            } else {
                player = null;
            }
            Player player2 = player;
            ArrayList arrayList = new ArrayList();
            Iterator iterator = commandSender.getServer().getOnlinePlayers().iterator();
            while (true) {
                if (!iterator.hasNext()) {
                    arrayList.sort(String.CASE_INSENSITIVE_ORDER);
                    return arrayList;
                }
                Player player3 = (Player)iterator.next();
                String string2 = player3.getName();
                if (player2 != null && !player2.canSee(player3)) continue;
                String string3 = stringArray[stringArray.length - 1];
                if (StringUtil.startsWithIgnoreCase((String)string2, (String)string3)) {
                    arrayList.add((Object)string2);
                }
            }
        }
        return super.tabComplete(commandSender, string, stringArray);
    }

    public ManualBanCommand() {
        super(EnumMessage.MANUAL_BAN_COMMAND.get());
        this.setDatabase(true);
        this.setPermission("verus.admin");
        this.setUsageMessage(ChatColor.RED + "Usage: /" + this.getName() + " <name> OR /" + this.getName() + " check <name>");
    }

    public void execute(CommandSender commandSender, String[] stringArray) {
        if (stringArray.length == 1) {
            Player player;
            String string;
            String string2;
            if (commandSender instanceof Player) {
                string2 = ((Player)commandSender).getName();
            } else {
                string2 = string = "Console";
            }
            if ((player = Bukkit.getPlayer((String)stringArray[0])) != null) {
                PlayerData playerData = DataManager.getInstance().getPlayer(player);
                ManualA manualA = playerData.getCheckData().getManualBanCheck();
                if (manualA != null) {
                    manualA.setViolations(1.0);
                    manualA.handleViolation(string);
                    AlertManager alertManager = AlertManager.getInstance();
                    if (alertManager.insertBan(playerData, check)) {
                        playerData.setEnabled(false);
                    }
                }
            } else {
                commandSender.sendMessage(ChatColor.RED + EnumMessage.COMMAND_PLAYER_NOT_FOUND.get());
            }
        } else if (stringArray.length == 2 && stringArray[0].equalsIgnoreCase("check")) {
            String string = stringArray[1];
            Database database = StorageEngine.getInstance().getDatabase();
            database.getUUID(string, uUID -> {
                if (uUID == null) {
                    commandSender.sendMessage(ChatColor.RED + EnumMessage.COMMAND_PLAYER_NEVER_LOGGED_ON.get());
                    return;
                }
                database.getManualLogs(uUID, 10, iterable -> {
                    if (!iterable.iterator().hasNext()) {
                        commandSender.sendMessage(ChatColor.RED + EnumMessage.PLAYER_NO_LOGS.get());
                    } else {
                        String string = ChatColor.GRAY + "(Gathering logs for " + stringArray[0] + "/" + uUID + "...)\n";
                        long l = System.currentTimeMillis();
                        for (Log log : iterable) {
                            String string2 = log.getData();
                            if (string2 != null) {
                                string = string + ChatColor.GRAY + "(" + ChatColor.WHITE + me.levansj01.verus.util.java.StringUtil.differenceAsTime((long)(l - log.getTimestamp())) + ChatColor.GRAY + ") " + VerusPlugin.COLOR + log.getName() + ChatColor.GRAY + " was manual banned by " + ChatColor.WHITE + log.getData() + "\n";
                            }
                        }
                        commandSender.sendMessage(string);
                    }
                });
            });
        } else {
            commandSender.sendMessage(this.getUsageMessage());
        }
    }
}
