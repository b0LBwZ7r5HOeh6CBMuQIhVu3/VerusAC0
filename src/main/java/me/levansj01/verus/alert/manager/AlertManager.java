package me.levansj01.verus.alert.manager;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;
import me.levansj01.verus.VerusPlugin;
import me.levansj01.verus.alert.Alert;
import me.levansj01.verus.api.API;
import me.levansj01.verus.check.Check;
import me.levansj01.verus.check.manager.CheckManager;
import me.levansj01.verus.check.version.CheckVersion;
import me.levansj01.verus.compat.PacketManager;
import me.levansj01.verus.data.PlayerData;
import me.levansj01.verus.data.manager.DataManager;
import me.levansj01.verus.messaging.MessagingHandler;
import me.levansj01.verus.storage.StorageEngine;
import me.levansj01.verus.storage.database.Ban;
import me.levansj01.verus.storage.database.Log;
import me.levansj01.verus.util.hastebin.HasteBin;
import me.levansj01.verus.util.java.CachedSupplier;
import me.levansj01.verus.util.java.JavaV;
import me.levansj01.verus.util.pastebin.AccountCredentials;
import me.levansj01.verus.util.pastebin.Paste;
import me.levansj01.verus.util.pastebin.PasteBin;
import me.levansj01.verus.util.pastebin.PasteExpiration;
import me.levansj01.verus.util.pastebin.PasteHighLight;
import me.levansj01.verus.util.pastebin.PasteVisibility;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AlertManager {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM HH:mm");
    private static Boolean api = null;;
    private static final StorageEngine ENGINE = StorageEngine.getInstance();
    private final ExecutorService executorService = Executors.newSingleThreadExecutor(
            new ThreadFactoryBuilder().setPriority(3).setNameFormat("Verus Executor Thread").build());
    private static final ExecutorService hastebinService = Executors
            .newCachedThreadPool(new ThreadFactoryBuilder().setPriority(3).build());
    public static final String PERMISSION_ADMIN = "verus.admin";
    public static AlertManager instance;
    public static final String PERMISSION_ALERTS = "verus.alerts";
    private static final String DEBUG = VerusPlugin.COLOR + ChatColor.BOLD.toString() + "Verus-Debug "
            + ChatColor.DARK_GRAY + "> " + ChatColor.WHITE + "%s" + ChatColor.GRAY + " failed " + ChatColor.WHITE
            + "%s Type %s" + ChatColor.GRAY + " VL[" + VerusPlugin.COLOR + "%.1f." + ChatColor.GRAY + "] %s";

    private static void handleAlertBroadcast(HoverEvent hoverEvent, ClickEvent clickEvent,
                                             BaseComponent baseComponent) {
        baseComponent.setHoverEvent(hoverEvent);
        baseComponent.setClickEvent(clickEvent);
    }

    private static void uploadLogs(CommandSender commandSender, String string, boolean bl, UUID uUID) {
        String string2;
        int n = 0;
        String[] object = HasteBin.ENDPOINTS;
        int n2 = ((String[]) object).length;
        if (n < n2) {
            String string3;
            String string4 = object[n];
            commandSender.sendMessage(ChatColor.GRAY + "(Uploading logs to hastebin...)");
            String string5 = null;
            try {
                string5 = HasteBin.paste((String) string, (String) string4);
            } catch (IOException e) {
                e.printStackTrace();
            }
            StringBuilder stringBuilder = new StringBuilder().append(VerusPlugin.COLOR);
            if (bl) {
                string3 = "Admin ";
            } else {
                string3 = "";
            }
            commandSender.sendMessage(stringBuilder.append(string3).append("Logs URL: ")
                    .append(ChatColor.WHITE).append(string5).toString());
            return;
        }
        commandSender.sendMessage(ChatColor.GRAY + "(Uploads failed, uploading to pastebin...)");
        String pastebin = new PasteBin(
                new AccountCredentials("5cf15f66d42e7b9613def0588fd47c39", "levansj04", "/hFpFC9Ef3b&G3-"))
                .createPaste(new Paste("Logs: " + uUID, string, PasteHighLight.TEXT, PasteExpiration.ONE_WEEK,
                        PasteVisibility.UNLISTED));
        StringBuilder stringBuilder = new StringBuilder().append(VerusPlugin.COLOR);
        if (bl) {
            string2 = "Admin ";

        } else {
            string2 = "";
        }
        commandSender.sendMessage(stringBuilder.append(string2).append("Logs URL ").append(ChatColor.WHITE)
                .append(pastebin).toString());

    }

    public static boolean isAPI() {
        Boolean bl;
        if (api == null) {
            bl = api = Boolean.valueOf((boolean) Bukkit.getPluginManager().isPluginEnabled("VerusAPI"));
        } else {
            bl = api;
        }
        return bl;
    }

    public void handleBan(PlayerData playerData, Check check, boolean bl) {
        if (!ENGINE.getVerusConfig().isAutoBan()) {
            return;
        }
        if (!CheckManager.getInstance().isAutoban(check)) {
            return;
        }
        if (!(check.getCheckVersion() != CheckVersion.RELEASE || playerData.isBanned() || playerData.isDebug()
                || ENGINE.getVerusConfig().isBypassEnabled()
                && playerData.getPlayer().hasPermission(ENGINE.getVerusConfig().getBypassPermission()))) {
            if (bl) {
                playerData.setSpoofBan(true);
                playerData.setSpoofBanCheck(check);
            } else {
                playerData.setEnabled(false);
                this.insertBan(playerData, check);
            }
        }
    }

    public void handleDebug(PlayerData playerData, Check check, double d, Supplier<String> supplier) {
        DataManager.getInstance().getPlayers().stream().filter(PlayerData::isDebug).map(PlayerData::getPlayer)
                .forEach(arg_0 -> AlertManager.handleDebug(playerData, check, d, supplier, arg_0));
    }

    public void run(Runnable runnable) {
        this.executorService.execute(runnable);
    }

    private void _handleViolation(PlayerData playerData, Check check, Supplier<String> supplier, double d, boolean bl) {
        double d2;
        boolean bl2;
        if (!playerData.isEnabled()) {
            return;
        }
        if (playerData.hasLag() || playerData.hasFast()) {
            bl2 = true;
        } else {
            bl2 = false;
        }
        boolean bl3 = bl2;
        int n = (int) Math.floor((double) check.getViolations());
        double d3 = check.getViolations();
        if (bl3) {
            d2 = 3.0;
        } else {
            d2 = 5.0;
        }
        check.setViolations(d3 + Math.min((double) d2, (double) d));
        if (check.getViolations() > 0.0) {
            this.handleDebug(playerData, check, check.getViolations(), supplier);
        }
        if (n > 0) {
            if (n > check.getLastViolation()) {
                if ((bl || this.handleAlert(playerData, check, n)) && ENGINE.isConnected()) {
                    ENGINE.getDatabase()
                            .insertLog(Log.create((PlayerData) playerData, (Check) check, supplier, (int) n));
                }
                if (check.getViolations() >= (double) check.getMaxViolation()) {
                    this.handleBan(playerData, check, bl);
                }
                if (bl) {
                    Alert alert = new Alert(check, supplier, n);
                    playerData.getSpoofedAlerts().add(alert);
                    JavaV.trim(playerData.getSpoofedAlerts(), (int) 5);
                    playerData.setCheckSpoofing(true);
                }
            }
            check.setLastViolation(n);
        }
    }

    private static void runCommand(String string, String string2, PlayerData playerData, String string3) {
        String string4 = string3.replace((CharSequence) "%s", (CharSequence) string)
                .replace((CharSequence) "{player}", (CharSequence) string)
                .replace((CharSequence) "{reason}", (CharSequence) string2);
        if (ENGINE.getVerusConfig().isBungeeBans()) {
            MessagingHandler.getInstance().handleBan(playerData.getPlayer(), string4);
        } else {
            Bukkit.dispatchCommand((CommandSender) Bukkit.getConsoleSender(), (String) string4);
        }
    }

    public ExecutorService getExecutorService() {
        return this.executorService;
    }

    private String ms(Object object) {
        return object + "ms";
    }

    public boolean handleAlert(PlayerData playerData, Check check, int n) {
        if (!CheckManager.getInstance().isEnabled(check)) {
            return false;
        }
        API aPI = API.getAPI();
        if (aPI != null && aPI.fireViolationEvent(playerData, check, n)) {
            check.setViolations((double) check.getLastViolation());
            return false;
        }
        String string = playerData.getName();
        String string2 = this.getFormattedAlert(playerData, check, String.valueOf((int) n));
        this.handleAlertBroadcast(string2, string);
        return true;
    }

    private static void handleAlertBroadcast(BaseComponent[] baseComponentArray, Player player) {
        player.spigot().sendMessage(baseComponentArray);
    }

    public static AlertManager getInstance() {
        AlertManager alertManager;
        if (instance == null) {
            alertManager = instance = new AlertManager();
        } else {
            alertManager = instance;
        }
        return alertManager;
    }

    private static void handleDebug(PlayerData playerData, Check check, double d, Supplier supplier,
                                    Player player) {
        String string;
        Object[] objectArray = new Object[5];
        objectArray[0] = playerData.getName();
        objectArray[1] = check.getType().getName();
        StringBuilder stringBuilder = new StringBuilder().append(check.getSubType());
        if (check.getCheckVersion() == CheckVersion.RELEASE) {
            string = "";
        } else {
            string = "*";
        }
        objectArray[2] = stringBuilder.append(string).toString();
        objectArray[3] = d;
        objectArray[4] = supplier.get();
        player.sendMessage(String.format((String) DEBUG, (Object[]) objectArray));
    }

    public void handleViolation(PlayerData playerData, Check check, Supplier<String> supplier, double d, boolean bl) {
        this.run(() -> this.handleViolation1(playerData, check, supplier, d, bl));
    }

    public void uploadLogs(CommandSender commandSender, UUID uUID, Iterable<Log> iterable, boolean bl) {
        String string;
        StringBuilder stringBuilder = new StringBuilder();
        if (bl) {
            string = "Admin ";
        } else {
            string = "";
        }
        String string2 = stringBuilder.append(string).append("Logs of (").append(uUID).append("):\n")
                .toString();
        for (Log log : iterable) {
            string2 = string2 + this.toString(log, bl);
        }
        Object object = string2;
        hastebinService.execute(() -> AlertManager.uploadLogs(commandSender, (String) object, bl, uUID));
    }

    private static void insertBan(String string, String string2, PlayerData playerData) {
        ENGINE.getVerusConfig().getBanCommands()
                .forEach(arg_0 -> AlertManager.runCommand(string, string2, playerData, arg_0));
    }

    public String toString(Log log, boolean bl) {
        String string;
        String string2 = log.getData();
        if (string2 == null || string2.isEmpty()) {
            string2 = "No Provided Data";
        }
        StringBuilder stringBuilder = new StringBuilder().append("(")
                .append(DATE_FORMAT.format(new Date(log.getTimestamp()))).append("|").append(log.getTimestamp())
                .append(") ").append(log.getName()).append(" failed ").append(log.getType()).append(" Type ")
                .append(log.getSubType()).append(" VL: ").append(log.getViolations()).append(" | Ping: ")
                .append(this.ms(log.getPing())).append(" Lag: ").append(this.ms(log.getLag()));
        if (bl) {
            string = " | " + string2;
        } else {
            string = "";
        }
        return stringBuilder.append(string).append("\n").toString();
    }

    private static void handleAlertBroadcast(String string, Player player) {
        player.sendMessage(string);
    }

    public void handleAlertBroadcast(String string, String string2) {
        if (ENGINE.getVerusConfig().isAlertClick()) {
            Object[] objectArray = TextComponent.fromLegacyText(string);
            HoverEvent hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                    TextComponent.fromLegacyText(
                            (String) ChatColor.translateAlternateColorCodes((char) '&', (String) ENGINE.getVerusConfig()
                                    .getAlertClickHover().replace((CharSequence) "{player}", (CharSequence) string2))));
            ClickEvent clickEvent = new ClickEvent(ClickEvent.Action.RUN_COMMAND, ENGINE.getVerusConfig()
                    .getAlertClickCommand().replace((CharSequence) "{player}", (CharSequence) string2));
            Arrays.stream(objectArray).forEach(
                    arg_0 -> AlertManager.handleAlertBroadcast(hoverEvent, clickEvent, (BaseComponent) arg_0));
            DataManager.getInstance().getPlayers().stream().filter(PlayerData::isAlerts).map(PlayerData::getPlayer)
                    .forEach(arg_0 -> AlertManager.handleAlertBroadcast((BaseComponent[]) objectArray, arg_0));
        } else {
            DataManager.getInstance().getPlayers().stream().filter(PlayerData::isAlerts).map(PlayerData::getPlayer)
                    .forEach(arg_0 -> AlertManager.handleAlertBroadcast(string, arg_0));
        }
    }

    private String getFormattedAlert(PlayerData playerData, Check check, String string) {
        int n;
        String string2;
        String string3 = ENGINE.getVerusConfig().getAlertMessage()
                .replace((CharSequence) "{name}",
                        (CharSequence) (VerusPlugin.COLOR + ENGINE.getVerusConfig().getAnticheatName()))
                .replace((CharSequence) "{player}", (CharSequence) playerData.getName())
                .replace((CharSequence) "{certainty}", (CharSequence) ENGINE.getVerusConfig().getAlertCertainty());
        StringBuilder stringBuilder = new StringBuilder().append(check.getFriendlyName()).append(" ")
                .append(check.getType().getSuffix()).append(check.getSubType());
        if (check.getCheckVersion() == CheckVersion.RELEASE) {
            string2 = "";
        } else {
            string2 = "*";
        }
        String string4 = string3
                .replace((CharSequence) "{cheat}", (CharSequence) stringBuilder.append(string2).toString())
                .replace((CharSequence) "{vl}", (CharSequence) string);
        if (check.getMaxViolation() == Integer.MAX_VALUE) {
            n = 0;
        } else {
            n = check.getMaxViolation();
        }
        return ChatColor.translateAlternateColorCodes((char) '&',
                (String) string4.replace((CharSequence) "{maxvl}", (CharSequence) String.valueOf((int) n)).replace(
                        (CharSequence) "{ping}", (CharSequence) String.valueOf((int) playerData.getTransactionPing())));
    }

    public void insertBan(PlayerData playerData, Check check) {
        API aPI = API.getAPI();
        if (aPI != null && aPI.fireBanEvent(playerData, check)) {
            return;
        }
        playerData.setBanned(true);
        String string = playerData.getName();
        String string2 = "Cheat-" + check.getType().getSuffix() + check.getSubType();
        if (ENGINE.getVerusConfig().isBanAnnouncement()) {
            String string3 = ENGINE.getVerusConfig().getBanMessage().replace((CharSequence) "%s", (CharSequence) string)
                    .replace((CharSequence) "{player}", (CharSequence) string)
                    .replace((CharSequence) "{reason}", (CharSequence) string2);
            Bukkit.broadcastMessage((String) ChatColor.translateAlternateColorCodes((char) '&', (String) string3));
        }
        PacketManager.getInstance().postToMainThread(() -> AlertManager.insertBan(string, string2, playerData));
        if (ENGINE.isConnected()) {
            ENGINE.getDatabase().insertBan(Ban.create((PlayerData) playerData, (Check) check));
        }
    }

    private void handleViolation1(PlayerData playerData, Check check, Supplier supplier, double d, boolean bl) {
        this._handleViolation(playerData, check, (Supplier<String>) CachedSupplier.of(supplier), d, bl);
    }
}