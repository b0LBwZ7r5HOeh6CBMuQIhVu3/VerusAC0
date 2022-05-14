package me.levansj01.verus.compat.v1_8_R3;

import java.lang.reflect.*;
import me.levansj01.verus.data.state.*;
import org.vspigot.*;
import it.unimi.dsi.fastutil.longs.*;
import gnu.trove.map.hash.*;
import me.levansj01.verus.compat.api.trove.*;
import gnu.trove.map.*;
import org.bukkit.block.*;
import org.bukkit.craftbukkit.v1_8_R3.util.*;
import org.bukkit.craftbukkit.v1_8_R3.*;
import me.levansj01.verus.util.java.*;
import org.bukkit.inventory.*;
import org.bukkit.entity.*;
import org.bukkit.craftbukkit.v1_8_R3.inventory.*;
import org.bukkit.material.*;
import me.levansj01.verus.data.transaction.world.*;
import me.levansj01.verus.util.*;
import me.levansj01.verus.data.version.*;
import com.ngxdev.viaversion.api.*;
import org.vspigot.protocolsupport.api.*;
import me.levansj01.verus.util.location.*;
import org.bukkit.*;
import me.levansj01.verus.compat.api.wrapper.*;
import java.util.concurrent.*;
import org.bukkit.craftbukkit.v1_8_R3.entity.*;
import me.levansj01.verus.data.*;
import java.util.function.*;
import io.netty.channel.*;
import java.util.*;
import net.minecraft.server.v1_8_R3.*;

public class NMSManager extends me.levansj01.verus.compat.NMSManager
{
    private boolean nspigot;
    private boolean vspigot;
    private boolean multiblock;
    private boolean vspigotprotocolsupport;
    private static final Field ItemBlock_a;
    private final BlockPosition.MutableBlockPosition blockPosition;
    private static final Field effects;
    private static final State listState;
    
    public static NMSManager getInstance() {
        return (NMSManager)me.levansj01.verus.compat.NMSManager.getInstance();
    }
    
    public NMSManager() {
        this.blockPosition = new BlockPosition.MutableBlockPosition();
        Class.forName("com.ngxdev.config.nSpigotConfig");
        this.nspigot = true;
        Class.forName("org.vspigot.vSpigotConfig");
        final boolean b = true;
        this.vspigot = b;
        this.multiblock = b;
        this.vspigotprotocolsupport = vSpigotConfig.options.protocolSupport;
        Class.forName("it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap");
        this.fastUtil = true;
    }
    
    @Override
    public boolean isRunning() {
        return MinecraftServer.getServer().isRunning();
    }
    
    @Override
    public Map long2ObjectHashMap(final int n, final float n2) {
        return (Map)(this.fastUtil ? new Long2ObjectOpenHashMap(n, n2) : new LongObjectMapWrapper((TLongObjectMap)new TLongObjectHashMap(n, n2)));
    }
    
    @Override
    public float getFrictionFactor(final Block block) {
        return CraftMagicNumbers.getBlock(block).frictionFactor;
    }
    
    @Override
    public boolean isLoaded(final World world, final int n, final int n2) {
        return ((CraftWorld)world).getHandle().chunkProviderServer.isChunkLoaded(n >> 4, n2 >> 4);
    }
    
    protected net.minecraft.server.v1_8_R3.Block getBlock(final ItemBlock itemBlock) {
        return (net.minecraft.server.v1_8_R3.Block)SafeReflection.fetch(NMSManager.ItemBlock_a, itemBlock);
    }
    
    @Override
    public IBlockData place(final ItemStack itemStack, final Player player, final me.levansj01.verus.compat.api.wrapper.BlockPosition blockPosition, final int n, final float n2, final float n3, final float n4) {
        final EntityPlayer handle = ((CraftPlayer)player).getHandle();
        final net.minecraft.server.v1_8_R3.ItemStack nmsCopy = CraftItemStack.asNMSCopy(itemStack);
        if (itemStack == null) {
            return null;
        }
        final Item item = nmsCopy.getItem();
        if (item instanceof ItemBlock) {
            final ItemBlock itemBlock = (ItemBlock)item;
            return this.getBlock(itemBlock).getPlacedState(handle.world, new BlockPosition(blockPosition.getX(), blockPosition.getY(), blockPosition.getZ()), EnumDirection.fromType1(n), n2, n3, n4, itemBlock.filterData(nmsCopy.getData()), (EntityLiving)handle);
        }
        return null;
    }
    
    @Override
    public Material getType(final Player player, final World world, final IBlockPosition blockPosition) {
        final WorldServer handle = ((CraftWorld)world).getHandle();
        this.blockPosition.c(blockPosition.getX(), blockPosition.getY(), blockPosition.getZ());
        if (handle.isLoaded((BlockPosition)this.blockPosition)) {
            if (this.vspigot && this.multiblock) {
                final MaterialData materialData = (MaterialData)((CraftPlayer)player).getHandle().getFakeBlocks().get(this.blockPosition);
                if (materialData != null) {
                    return materialData.getItemType();
                }
            }
            return CraftMagicNumbers.getMaterial(handle.getType((BlockPosition)this.blockPosition).getBlock());
        }
        return Material.AIR;
    }
    
    public MaterialData toData(final IBlockData blockData) {
        final net.minecraft.server.v1_8_R3.Block block = blockData.getBlock();
        return CraftMagicNumbers.getMaterial(block).getNewData((byte)block.toLegacyData(blockData));
    }
    
    @Override
    public IBlockData fromID(final int n) {
        return (IBlockData)net.minecraft.server.v1_8_R3.Block.d.a(n);
    }
    
    public LazyData toLazy(final IBlockData blockData) {
        return this.toLazy(blockData, State.fast(NMSManager::lambda.toLazy.0));
    }
    
    public LazyData toLazy(final IBlockData blockData, final State state) {
        if (blockData == null) {
            return LazyData.AIR;
        }
        final net.minecraft.server.v1_8_R3.Block block = blockData.getBlock();
        final State fast = State.fast(NMSManager::lambda.toLazy.1);
        return new LazyData(state, State.fast(NMSManager::lambda.toLazy.2), fast, State.of(block.frictionFactor));
    }
    
    @Override
    public MaterialData getTypeAndData(final Player player, final World world, final IBlockPosition blockPosition) {
        final WorldServer handle = ((CraftWorld)world).getHandle();
        this.blockPosition.c(blockPosition.getX(), blockPosition.getY(), blockPosition.getZ());
        if (handle.isLoaded((BlockPosition)this.blockPosition)) {
            if (this.vspigot && this.multiblock) {
                final MaterialData materialData = (MaterialData)((CraftPlayer)player).getHandle().getFakeBlocks().get(this.blockPosition);
                if (materialData != null) {
                    return materialData;
                }
            }
            return this.toData(handle.getType((BlockPosition)this.blockPosition));
        }
        return new MaterialData(0);
    }
    
    @Override
    public float getDamage(final Player player, final World world, final IBlockPosition blockPosition) {
        final WorldServer handle = ((CraftWorld)world).getHandle();
        this.blockPosition.c(blockPosition.getX(), blockPosition.getY(), blockPosition.getZ());
        if (handle.isLoaded((BlockPosition)this.blockPosition)) {
            return ((this.vspigot && this.multiblock) ? handle.getType((Entity)((CraftPlayer)player).getHandle(), (BlockPosition)this.blockPosition) : handle.getType((BlockPosition)this.blockPosition)).getBlock().getDamage((EntityHuman)((CraftPlayer)player).getHandle(), (net.minecraft.server.v1_8_R3.World)handle, (BlockPosition)this.blockPosition);
        }
        return 0.0f;
    }
    
    @Override
    public float getFrictionFactor(final Player player, final World world, final IBlockPosition blockPosition) {
        final WorldServer handle = ((CraftWorld)world).getHandle();
        this.blockPosition.c(blockPosition.getX(), blockPosition.getY(), blockPosition.getZ());
        if (!handle.isLoaded((BlockPosition)this.blockPosition)) {
            return Blocks.AIR.frictionFactor;
        }
        if (this.vspigot && this.multiblock) {
            return handle.getType((Entity)((CraftPlayer)player).getHandle(), (BlockPosition)this.blockPosition).getBlock().frictionFactor;
        }
        return handle.getType((BlockPosition)this.blockPosition).getBlock().frictionFactor;
    }
    
    @Override
    public Cuboid getBoundingBox(final Player player, final World world, final IBlockPosition blockPosition) {
        final WorldServer handle = ((CraftWorld)world).getHandle();
        this.blockPosition.c(blockPosition.getX(), blockPosition.getY(), blockPosition.getZ());
        if (!handle.isLoaded((BlockPosition)this.blockPosition)) {
            return null;
        }
        IBlockData blockData;
        if (this.vspigot && this.multiblock) {
            blockData = handle.getType((Entity)((CraftPlayer)player).getHandle(), (BlockPosition)this.blockPosition);
        }
        else {
            blockData = handle.getType((BlockPosition)this.blockPosition);
        }
        final AxisAlignedBB a = blockData.getBlock().a((net.minecraft.server.v1_8_R3.World)handle, (BlockPosition)this.blockPosition, blockData);
        if (a == null) {
            return null;
        }
        return new Cuboid(a.a, a.d, a.b, a.e, a.c, a.f);
    }
    
    @Override
    public double getMovementSpeed(final Player player) {
        return ((CraftPlayer)player).getHandle().getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).getValue();
    }
    
    @Override
    public double getKnockbackResistance(final Player player) {
        return ((CraftPlayer)player).getHandle().getAttributeInstance(GenericAttributes.c).getValue();
    }
    
    @Override
    public boolean setOnGround(final Player player, final boolean onGround) {
        final EntityPlayer handle = ((CraftPlayer)player).getHandle();
        final boolean onGround2 = handle.onGround;
        handle.onGround = onGround;
        return onGround2;
    }
    
    @Override
    public boolean setInWater(final Player player, final boolean inWater) {
        final EntityPlayer handle = ((CraftPlayer)player).getHandle();
        final boolean inWater2 = handle.inWater;
        handle.inWater = inWater;
        return inWater2;
    }
    
    @Override
    protected ClientVersion defaultVersion() {
        return ClientVersion.V1_8;
    }
    
    @Override
    protected int versionNumber() {
        return 47;
    }
    
    @Override
    public ClientVersion getVersion(final Player player) {
        if (this.viaversionplugin || this.nspigot) {
            int n;
            if (this.nspigot) {
                n = Via.getAPI().getPlayerVersion(player.getUniqueId());
            }
            else if (this.newViaAPI) {
                n = com.viaversion.viaversion.api.Via.getAPI().getPlayerVersion(player.getUniqueId());
            }
            else {
                n = us.myles.ViaVersion.api.Via.getAPI().getPlayerVersion(player.getUniqueId());
            }
            return this.getById(n);
        }
        if (this.vspigotprotocolsupport) {
            final ProtocolVersion protocolVersion = ProtocolSupportAPI.getProtocolVersion(player);
            if (protocolVersion == null) {
                return ClientVersion.VERSION_UNSUPPORTED;
            }
            return this.getByProtocolSupport(protocolVersion.name(), protocolVersion.getId());
        }
        else {
            if (!this.protocolsupportplugin) {
                return ClientVersion.V1_8;
            }
            final protocolsupport.api.ProtocolVersion protocolVersion2 = protocolsupport.api.ProtocolSupportAPI.getProtocolVersion(player);
            if (protocolVersion2 == null) {
                return ClientVersion.VERSION_UNSUPPORTED;
            }
            return this.getByProtocolSupport(protocolVersion2.name(), protocolVersion2.getId());
        }
    }
    
    @Override
    public void updatePing(final Player player, final int ping) {
        ((CraftPlayer)player).getHandle().ping = ping;
    }
    
    @Override
    public void sendTransaction(final Player player, final short n) {
        ((CraftPlayer)player).getHandle().playerConnection.sendPacket((Packet)new PacketPlayOutTransaction(0, n, false));
    }
    
    @Override
    public void sendTeleport(final Player player, final ILocation location) {
        ((CraftPlayer)player).getHandle().playerConnection.sendPacket((Packet)new PacketPlayOutPosition(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch(), (Set)Collections.emptySet()));
    }
    
    @Override
    public void sendBlockUpdate(final Player player, final World world, final int n, final int n2, final int n3) {
        final WorldServer handle = ((CraftWorld)world).getHandle();
        this.blockPosition.c(n, n2, n3);
        if (handle.isLoaded((BlockPosition)this.blockPosition)) {
            if (this.vspigot) {
                IBlockData blockData;
                if (this.multiblock) {
                    blockData = handle.getType((Entity)((CraftPlayer)player).getHandle(), (BlockPosition)this.blockPosition);
                }
                else {
                    blockData = handle.getType((BlockPosition)this.blockPosition);
                }
                ((CraftPlayer)player).getHandle().playerConnection.sendPacket((Packet)new PacketPlayOutBlockChange((BlockPosition)this.blockPosition, blockData));
            }
            else {
                ((CraftPlayer)player).getHandle().playerConnection.sendPacket((Packet)new PacketPlayOutBlockChange((net.minecraft.server.v1_8_R3.World)handle, (BlockPosition)this.blockPosition));
            }
        }
    }
    
    @Override
    public void sendBlockUpdate(final Player player, final Location location) {
        this.sendBlockUpdate(player, location.getWorld(), location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }
    
    @Override
    public PlayerAbilities getAbilities(final Player player) {
        final net.minecraft.server.v1_8_R3.PlayerAbilities abilities = ((CraftPlayer)player).getHandle().abilities;
        return new PlayerAbilities(abilities.isInvulnerable, abilities.isFlying, abilities.canFly, abilities.canInstantlyBuild, abilities.mayBuild, abilities.a(), abilities.b());
    }
    
    @Override
    public void setAsyncPotionEffects(final Player player) {
        if (this.vspigot) {
            return;
        }
        final EntityPlayer handle = ((CraftPlayer)player).getHandle();
        final Map effects = handle.effects;
        if (effects instanceof HashMap) {
            SafeReflection.set(NMSManager.effects, handle, new ConcurrentHashMap(effects));
        }
    }
    
    @Override
    public int getCurrentTick() {
        return MinecraftServer.currentTick;
    }
    
    @Override
    public boolean rayTrace(final World world, final double n, final double n2, final double n3, final double n4, final double n5, final double n6, final boolean b, final boolean b2, final boolean b3) {
        final WorldServer handle = ((CraftWorld)world).getHandle();
        final MovingObjectPosition rayTrace = handle.rayTrace(new Vec3D(n, n2, n3), new Vec3D(n4, n5, n6), b, b2, b3);
        return rayTrace != null && handle.getType(rayTrace.a()).getBlock() != Blocks.END_PORTAL;
    }
    
    @Override
    public boolean isEmpty(final World world, final org.bukkit.entity.Entity entity, final Cuboid cuboid, final IBlockPosition blockPosition) {
        final WorldServer handle = ((CraftWorld)world).getHandle();
        this.blockPosition.c(blockPosition.getX(), blockPosition.getY(), blockPosition.getZ());
        if (handle.isLoaded((BlockPosition)this.blockPosition)) {
            final IBlockData type = handle.getType((BlockPosition)this.blockPosition);
            final AxisAlignedBB axisAlignedBB = new AxisAlignedBB(cuboid.getX1(), cuboid.getY1(), cuboid.getZ1(), cuboid.getX2(), cuboid.getY2(), cuboid.getZ2());
            final ArrayList list = new ArrayList(1);
            type.getBlock().a((net.minecraft.server.v1_8_R3.World)handle, (BlockPosition)this.blockPosition, type, axisAlignedBB, (List)list, ((CraftEntity)entity).getHandle());
            return list.isEmpty();
        }
        return true;
    }
    
    @Override
    public void inject(final PlayerData playerData) {
        final Channel channel = ((CraftPlayer)playerData.getPlayer()).getHandle().playerConnection.networkManager.channel;
        if (channel == null) {
            return;
        }
        this.getNettyHandler().inject(channel, new SPacketListener(playerData));
    }
    
    @Override
    public void uninject(final PlayerData playerData) {
        final PlayerConnection playerConnection = ((CraftPlayer)playerData.getPlayer()).getHandle().playerConnection;
        if (playerConnection == null || playerConnection.isDisconnected()) {
            return;
        }
        this.getNettyHandler().uninject(playerConnection.networkManager.channel);
    }
    
    @Override
    public void postToMainThread(final Runnable runnable) {
        MinecraftServer.getServer().postToMainThread(runnable);
    }
    
    @Override
    public Runnable scheduleEnd(final Runnable runnable) {
        Objects.requireNonNull(runnable);
        final IUpdatePlayerListBox updatePlayerListBox = runnable::run;
        final List<IUpdatePlayerListBox> list = NMSManager.listState.get();
        list.add(updatePlayerListBox);
        return NMSManager::lambda.scheduleEnd.4;
    }
    
    @Override
    public void close(final Player player) {
        final Channel channel = ((CraftPlayer)player).getHandle().playerConnection.networkManager.channel;
        if (channel == null) {
            return;
        }
        channel.close();
    }
    
    @Override
    public Object place(final ItemStack itemStack, final Player player, final me.levansj01.verus.compat.api.wrapper.BlockPosition blockPosition, final int n, final float n2, final float n3, final float n4) {
        return this.place(itemStack, player, blockPosition, n, n2, n3, n4);
    }
    
    @Override
    public LazyData toLazy(final Object o) {
        return this.toLazy((IBlockData)o);
    }
    
    @Override
    public LazyData toLazy(final Object o, final State state) {
        return this.toLazy((IBlockData)o, state);
    }
    
    @Override
    public Object fromID(final int n) {
        return this.fromID(n);
    }
    
    @Override
    public MaterialData toData(final Object o) {
        return this.toData((IBlockData)o);
    }
    
    private static void lambda.scheduleEnd.4(final List list, final IUpdatePlayerListBox updatePlayerListBox) {
        list.remove(updatePlayerListBox);
    }
    
    private static List lambda.static.3() {
        return (List)SafeReflection.getLocalField(MinecraftServer.class, MinecraftServer.getServer(), "p");
    }
    
    private static MaterialData lambda.toLazy.2(final State state, final net.minecraft.server.v1_8_R3.Block block, final IBlockData blockData) {
        return state.get().getNewData((byte)block.toLegacyData(blockData));
    }
    
    private static Material lambda.toLazy.1(final net.minecraft.server.v1_8_R3.Block block) {
        return CraftMagicNumbers.getMaterial(block);
    }
    
    private static Integer lambda.toLazy.0(final IBlockData blockData) {
        return net.minecraft.server.v1_8_R3.Block.d.b((Object)blockData);
    }
    
    static {
        ItemBlock_a = SafeReflection.access(ItemBlock.class, "a");
        effects = SafeReflection.access(EntityLiving.class, "effects");
        listState = State.fast(NMSManager::lambda.static.3);
    }
}
