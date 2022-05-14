package me.levansj01.verus.compat;

import me.levansj01.verus.api.API;
import me.levansj01.verus.util.viaversion.*;
import org.bukkit.plugin.*;
import me.levansj01.verus.compat.netty.*;
import com.lunarclient.bukkitapi.*;
import me.levansj01.verus.data.version.*;
import com.viaversion.viaversion.api.*;
import protocolsupport.api.*;
import org.bukkit.material.*;
import me.levansj01.verus.data.state.*;
import me.levansj01.verus.data.transaction.world.*;
import org.bukkit.inventory.*;
import org.bukkit.block.*;
import me.levansj01.verus.util.location.*;
import org.bukkit.*;
import me.levansj01.verus.util.*;
import me.levansj01.verus.compat.api.wrapper.*;
import com.google.common.cache.*;

import java.util.concurrent.*;

import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.longs.*;

import java.util.*;

import me.levansj01.verus.data.*;
import org.bukkit.entity.*;

public abstract class NMSManager {
    private static NMSManager nmsManager;
    protected ServerVersion serverVersion;
    private final NettyHandler nettyHandler;
    protected boolean protocolsupportplugin;
    protected boolean viaversionplugin;
    protected boolean lunarClientAPI;
    protected boolean floodGateAPI;
    protected boolean fastUtil;
    protected boolean newViaAPI;
    private ViaHandler viaHandler;

    public static NMSManager getInstance() {
        return (NMSManager.nmsManager == null) ? (NMSManager.nmsManager = newInstance()) : NMSManager.nmsManager;
    }

    private static NMSManager newInstance() {
        final String s = Bukkit.getServer().getClass().getName().split("\\.")[3];
        final NMSManager nmsManager = (NMSManager) Class.forName(NMSManager.class.getName().replace(".NMSManager", "." + s + ".NMSManager")).newInstance();
        final ServerVersion value = ServerVersion.valueOf(s);
        nmsManager.setServerVersion(value);
        if (value.afterEq(ServerVersion.v1_17_R1) && Bukkit.getPluginManager().isPluginEnabled("ViaBackwards")) {
            V1_17Remapper.remap();
        }
        return nmsManager;
    }

    public NMSManager() {
        this.nettyHandler = this.newNettyHandler();
        final PluginManager pluginManager = Bukkit.getPluginManager();
        this.protocolsupportplugin = pluginManager.isPluginEnabled("ProtocolSupport");
        this.viaversionplugin = pluginManager.isPluginEnabled("ViaVersion");
        this.lunarClientAPI = pluginManager.isPluginEnabled("LunarClient-API");
        this.floodGateAPI = pluginManager.isPluginEnabled("floodgate");
        Class.forName("com.viaversion.viaversion.api.Via");
        this.newViaAPI = true;
        Class.forName("org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap");
        this.fastUtil = true;
        if (this.viaversionplugin) {
            this.viaHandler = new ViaHandler();
        }
    }

    public int getMinYLevel() {
        return 0;
    }

    public abstract boolean isRunning();

    public void syncCommands(final List list) {
    }

    public NettyHandler getNettyHandler() {
        return this.nettyHandler;
    }

    protected NettyHandler newNettyHandler() {
        return new UnshadedNettyHandler();
    }

    public ExecutorService getDataExecutorService(final String s) {
        return Executors.newCachedThreadPool(this.nettyHandler.newThreadFactory(s));
    }

    public boolean isRunningLunarClient(final Player player) {
        return this.lunarClientAPI && LunarClientAPI.getInstance().isRunningLunarClient(player);
    }

    protected abstract ClientVersion defaultVersion();

    protected abstract int versionNumber();

    public ClientVersion getVersion(final Player player) {
        if (this.viaversionplugin) {
            int n;
            if (this.newViaAPI) {
                n = Via.getAPI().getPlayerVersion(player.getUniqueId());
            } else {
                n = us.myles.ViaVersion.api.Via.getAPI().getPlayerVersion(player.getUniqueId());
            }
            if (n <= 5) {
                return ClientVersion.V1_7;
            }
            if (n <= 47) {
                return ClientVersion.V1_8;
            }
            if (!this.protocolsupportplugin || n != this.versionNumber()) {
                return this.getById(n);
            }
        }
        if (!this.protocolsupportplugin) {
            return this.defaultVersion();
        }
        final ProtocolVersion protocolVersion = ProtocolSupportAPI.getProtocolVersion(player);
        if (protocolVersion == null) {
            return ClientVersion.VERSION_UNSUPPORTED;
        }
        return this.getByProtocolSupport(protocolVersion.name(), protocolVersion.getId());
    }

    protected ClientVersion getByProtocolSupport(String var1, int var2) {
        switch (var1.hashCode()) {
            case -374160366:
                if (var1.equals("MINECRAFT_1_11_1")) {
                }
                break;
            case -374159405:
                if (var1.equals("MINECRAFT_1_12_1")) {
                }
                break;
            case -374159404:
                if (var1.equals("MINECRAFT_1_12_2")) {
                }
                break;
            case -374158444:
                if (var1.equals("MINECRAFT_1_13_1")) {
                }
                break;
            case -374158443:
                if (var1.equals("MINECRAFT_1_13_2")) {
                }
                break;
            case -374157483:
                if (var1.equals("MINECRAFT_1_14_1")) {
                }
                break;
            case -374157482:
                if (var1.equals("MINECRAFT_1_14_2")) {
                }
                break;
            case -374157481:
                if (var1.equals("MINECRAFT_1_14_3")) {
                }
                break;
            case -374157480:
                if (var1.equals("MINECRAFT_1_14_4")) {
                }
                break;
            case -374156522:
                if (var1.equals("MINECRAFT_1_15_1")) {
                }
                break;
            case -374156521:
                if (var1.equals("MINECRAFT_1_15_2")) {
                }
                break;
            case -374155561:
                if (var1.equals("MINECRAFT_1_16_1")) {
                }
                break;
            case -374155560:
                if (var1.equals("MINECRAFT_1_16_2")) {
                }
                break;
            case -374155559:
                if (var1.equals("MINECRAFT_1_16_3")) {
                }
                break;
            case -374155558:
                if (var1.equals("MINECRAFT_1_16_4")) {
                }
                break;
            case -374154600:
                if (var1.equals("MINECRAFT_1_17_1")) {
                }
                break;
            case -374153639:
                if (var1.equals("MINECRAFT_1_18_1")) {
                }
                break;
            case -374153638:
                if (var1.equals("MINECRAFT_1_18_2")) {
                }
                break;
            case -373938841:
                if (var1.equals("MINECRAFT_1_7_10")) {
                }
                break;
            case 218893493:
                var1.equals("MINECRAFT_FUTURE");
                break;
            case 591949304:
                if (var1.equals("MINECRAFT_1_8")) {
                }
                break;
            case 591949305:
                if (var1.equals("MINECRAFT_1_9")) {
                }
                break;
            case 1170559071:
                if (var1.equals("MINECRAFT_1_10")) {
                }
                break;
            case 1170559072:
                if (var1.equals("MINECRAFT_1_11")) {
                }
                break;
            case 1170559073:
                if (var1.equals("MINECRAFT_1_12")) {
                }
                break;
            case 1170559074:
                if (var1.equals("MINECRAFT_1_13")) {
                }
                break;
            case 1170559075:
                if (var1.equals("MINECRAFT_1_14")) {
                }
                break;
            case 1170559076:
                if (var1.equals("MINECRAFT_1_15")) {
                }
                break;
            case 1170559077:
                if (var1.equals("MINECRAFT_1_16")) {
                }
                break;
            case 1170559078:
                if (var1.equals("MINECRAFT_1_17")) {
                }
                break;
            case 1170559079:
                if (var1.equals("MINECRAFT_1_18")) {
                }
                break;
            case 1927600109:
                if (var1.equals("MINECRAFT_1_7_5")) {
                }
                break;
            case 1927602027:
                if (var1.equals("MINECRAFT_1_9_1")) {
                }
                break;
            case 1927602028:
                if (var1.equals("MINECRAFT_1_9_2")) {
                }
                break;
            case 1927602030:
                if (var1.equals("MINECRAFT_1_9_4")) {
                }
        }

        return this.defaultVersion().next();
    }


    protected ClientVersion getById(final int n) {
        if (n >= 757) {
            return ClientVersion.V1_18;
        }
        if (n >= 755) {
            return ClientVersion.V1_17;
        }
        if (n >= 735) {
            return ClientVersion.V1_16;
        }
        if (n >= 573) {
            return ClientVersion.V1_15;
        }
        if (n >= 477) {
            return ClientVersion.V1_14;
        }
        if (n >= 393) {
            return ClientVersion.V1_13;
        }
        if (n >= 335) {
            return ClientVersion.V1_12;
        }
        if (n >= 315) {
            return ClientVersion.V1_11;
        }
        if (n >= 210) {
            return ClientVersion.V1_10;
        }
        if (n >= 107) {
            return ClientVersion.V1_9;
        }
        if (n >= 47) {
            return ClientVersion.V1_8;
        }
        return ClientVersion.V1_7;
    }

    public abstract boolean isLoaded(final World p0, final int p1, final int p2);

    public abstract Material getType(final Player p0, final World p1, final IBlockPosition p2);

    public abstract MaterialData toData(final Object p0);

    public Object fromID(final int n) {
        throw new UnsupportedOperationException();
    }

    public LazyData toLazy(final Object o, final State state) {
        throw new UnsupportedOperationException();
    }

    public LazyData toLazy(final Object o) {
        throw new UnsupportedOperationException();
    }

    public LazyData fromIDToLazy(final int n) {
        return this.toLazy(this.fromID(n), State.of(n));
    }

    public Object place(final ItemStack itemStack, final Player player, final BlockPosition blockPosition, final int n, final float n2, final float n3, final float n4) {
        throw new UnsupportedOperationException();
    }

    public LazyData getPlace(final ItemStack itemStack, final Player player, final BlockPosition blockPosition, final int n, final float n2, final float n3, final float n4) {
        final Object place = this.place(itemStack, player, blockPosition, n, n2, n3, n4);
        if (place == null) {
            return null;
        }
        return this.toLazy(place);
    }

    public abstract MaterialData getTypeAndData(final Player p0, final World p1, final IBlockPosition p2);

    public abstract float getDamage(final Player p0, final World p1, final IBlockPosition p2);

    public abstract double getMovementSpeed(final Player p0);

    public abstract double getKnockbackResistance(final Player p0);

    public abstract boolean setOnGround(final Player p0, final boolean p1);

    public abstract boolean setInWater(final Player p0, final boolean p1);

    public abstract float getFrictionFactor(final Block p0);

    public abstract float getFrictionFactor(final Player p0, final World p1, final IBlockPosition p2);

    public abstract void sendTransaction(final Player p0, final short p1);

    public void sendTeleport(final Player player, final ILocation location) {
        throw new UnsupportedOperationException();
    }

    public abstract void sendBlockUpdate(final Player p0, final World p1, final int p2, final int p3, final int p4);

    public abstract void sendBlockUpdate(final Player p0, final Location p1);

    public abstract Cuboid getBoundingBox(final Player p0, final World p1, final IBlockPosition p2);

    public abstract PlayerAbilities getAbilities(final Player p0);

    public boolean isGliding(final Player player) {
        return false;
    }

    public boolean isRiptiding(final Player player) {
        return false;
    }

    public float getJumpFactor(final Player player, final World world, final IBlockPosition blockPosition) {
        return 1.0f;
    }

    public Map createCache(final Long n, final Long n2) {
        final CacheBuilder builder = CacheBuilder.newBuilder();
        if (n != null) {
            builder.expireAfterAccess((long) n, TimeUnit.MILLISECONDS);
        }
        if (n2 != null) {
            builder.expireAfterWrite((long) n2, TimeUnit.MILLISECONDS);
        }
        return builder.build().asMap();
    }

    public Map long2ObjectHashMap(final int n, final float n2) {
        return (Map) (this.fastUtil ? new Long2ObjectOpenHashMap(n, n2) : new HashMap());
    }

    public void setAsyncPotionEffects(final Player player) {
    }

    public abstract void inject(final PlayerData p0);

    public abstract void uninject(final PlayerData p0);

    public abstract void postToMainThread(final Runnable p0);

    public abstract Runnable scheduleEnd(final Runnable p0);

    public abstract void close(final Player p0);

    public void close(final PlayerData playerData) {
        this.close(playerData.getPlayer());
    }

    public abstract void updatePing(final Player p0, final int p1);

    public void updatePing(final PlayerData playerData) {
        this.updatePing(playerData.getPlayer(), playerData.getPing());
    }

    public abstract int getCurrentTick();

    public ItemStack getOffHand(final Player player) {
        return null;
    }

    public boolean rayTrace(final World world, final double n, final double n2, final double n3, final double n4, final double n5, final double n6, final boolean b, final boolean b2, final boolean b3) {
        throw new UnsupportedOperationException();
    }

    public boolean isEmpty(final World world, final Entity entity, final Cuboid cuboid, final IBlockPosition blockPosition) {
        throw new UnsupportedOperationException();
    }

    public boolean isWaterLogged(final World world, final IBlockPosition blockPosition) {
        return false;
    }

    public ServerVersion getServerVersion() {
        return this.serverVersion;
    }

    public boolean isProtocolsupportplugin() {
        return this.protocolsupportplugin;
    }

    public boolean isViaversionplugin() {
        return this.viaversionplugin;
    }

    public boolean isLunarClientAPI() {
        return this.lunarClientAPI;
    }

    public boolean isFloodGateAPI() {
        return this.floodGateAPI;
    }

    public boolean isFastUtil() {
        return this.fastUtil;
    }

    public boolean isNewViaAPI() {
        return this.newViaAPI;
    }

    public ViaHandler getViaHandler() {
        return this.viaHandler;
    }

    public void setServerVersion(final ServerVersion serverVersion) {
        this.serverVersion = serverVersion;
    }

    static {
        NMSManager.nmsManager = null;
    }
}
