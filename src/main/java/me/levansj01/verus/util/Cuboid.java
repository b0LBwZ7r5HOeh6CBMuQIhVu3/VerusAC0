package me.levansj01.verus.util;

import me.levansj01.verus.compat.*;
import org.bukkit.entity.*;
import org.bukkit.*;
import java.util.*;
import java.util.function.*;
import me.levansj01.launcher.*;
import me.levansj01.verus.data.transaction.world.*;
import me.levansj01.verus.util.java.*;
import me.levansj01.verus.data.transaction.tracker.*;
import me.levansj01.verus.compat.api.wrapper.*;
import me.levansj01.verus.util.location.*;

public class Cuboid
{
    private double z1;
    private double x2;
    private double y1;
    private double x1;
    private double y2;
    private static final Integer MIN_Y_LEVEL;
    private double z2;
    
    public Cuboid setValues(final ILocation location) {
        return this.setValues(location.getX(), location.getY(), location.getZ());
    }
    
    static {
        MIN_Y_LEVEL = NMSManager.getInstance().getMinYLevel();
    }
    
    public boolean contains(final Vector3d vector3d) {
        return vector3d != null && this.contains(vector3d.getX(), vector3d.getY(), vector3d.getZ());
    }
    
    public boolean checkBlocksInternal(final Supplier supplier, final int n, final BiFunction biFunction, final Predicate predicate) {
        return this.checkBlocksInternal(supplier, n, biFunction, Boolean::booleanValue, Cuboid::lambda$checkBlocksInternal$9);
    }
    
    private static boolean lambda$checkBlocks$0(final Predicate predicate, final Player player, final World world, final IBlockPosition blockPosition) {
        return predicate.test(blockPosition.getType(player, world));
    }
    
    public Cuboid setValues(final double n, final double n2, final double n3) {
        return this.setValues(n, n, n2, n2, n3, n3);
    }
    
    public boolean checkBlocksInternal(final Player player, final World world, final Predicate predicate) {
        final NMSManager instance = NMSManager.getInstance();
        Objects.requireNonNull(player);
        return this.checkBlocksInternal(player::getName, world.getMaxHeight(), Cuboid::lambda$checkBlocksInternal$8, predicate);
    }
    
    public Cuboid add(final double n, final double n2, final double n3, final double n4, final double n5, final double n6) {
        this.x1 += n;
        this.x2 += n2;
        this.y1 += n3;
        this.y2 += n4;
        this.z1 += n5;
        this.z2 += n6;
        return this;
    }
    
    private static boolean lambda$checkBlocks$4(final Predicate predicate, final IVerusBlock verusBlock) {
        final Iterator iterator = verusBlock.iterator();
        while (iterator.hasNext()) {
            if (!predicate.test(iterator.next().getType().get())) {
                return false;
            }
        }
        return true;
    }
    
    public boolean containsXZ(final PlayerLocation playerLocation) {
        return this.containsXZ(playerLocation.getX(), playerLocation.getZ());
    }
    
    public double getWidthX() {
        return this.x2 - this.x1;
    }
    
    public void setY1(final double y1) {
        this.y1 = y1;
    }
    
    public boolean checkBlocks(final Player player, final World world, final Predicate predicate) {
        return this.checkBlocksInternal(player, world, Cuboid::lambda$checkBlocks$0);
    }
    
    public boolean rayTraceInternal(final Player player, final Predicate predicate) {
        Objects.requireNonNull(player);
        return this.rayTraceInternal(player::getName, predicate);
    }
    
    private static boolean lambda$checkLoaded$2(final IVerusWorld verusWorld, final IBlockPosition blockPosition) {
        final IVerusBlock value = verusWorld.get(blockPosition);
        return value != null && value.getData() != null;
    }
    
    public double getZ2() {
        return this.z2;
    }
    
    private static Boolean lambda$checkBlocksInternal$8(final NMSManager nmsManager, final World world, final Integer n, final Integer n2) {
        return nmsManager.isLoaded(world, n, n2);
    }
    
    public double getY2() {
        return this.y2;
    }
    
    public void setX2(final double x2) {
        this.x2 = x2;
    }
    
    @Override
    public String toString() {
        return "Cuboid(x1=" + this.getX1() + ", x2=" + this.getX2() + ", y1=" + this.getY1() + ", y2=" + this.getY2() + ", z1=" + this.getZ1() + ", z2=" + this.getZ2() + ")";
    }
    
    private static boolean lambda$checkBlocksInternal$9(final Predicate predicate, final Boolean b, final IBlockPosition blockPosition) {
        return predicate.test(blockPosition);
    }
    
    public Cuboid hitbox() {
        return this.add(-0.3, 0.3, 0.0, 1.8, -0.3, 0.3).expand(0.1, 0.1, 0.1);
    }
    
    public void setX1(final double x1) {
        this.x1 = x1;
    }
    
    public boolean contains(final ILocation location) {
        return this.contains(location.getX(), location.getY(), location.getZ());
    }
    
    public static Cuboid withLimit(final ILocation location, final ILocation location2, final int n) {
        return (location.distanceSquared(location2) < n) ? new Cuboid(location, location2) : new Cuboid(location2);
    }
    
    public Cuboid to(final ILocation location, final int n) {
        if (this.distanceSquared(location) > n) {
            return this;
        }
        this.include(location);
        return this;
    }
    
    public double distanceSquared(final ILocation location) {
        if (this.contains(location)) {
            return 0.0;
        }
        return Math.min(Math.pow(location.getX() - this.x1, 2.0), Math.pow(location.getX() - this.x2, 2.0)) + Math.min(Math.pow(location.getY() - this.y1, 2.0), Math.pow(location.getY() - this.y2, 2.0)) + Math.min(Math.pow(location.getZ() - this.z1, 2.0), Math.pow(location.getZ() - this.z2, 2.0));
    }
    
    public boolean checkBlocksInternal(final Supplier supplier, final int n, final BiFunction biFunction, final Predicate predicate, final BiPredicate biPredicate) {
        final int n2 = (int)Math.floor(this.x1);
        final int n3 = (int)Math.ceil(this.x2);
        final int max = Math.max((int)Math.floor(this.y1), Cuboid.MIN_Y_LEVEL);
        final int min = Math.min((int)Math.ceil(this.y2), n);
        final int z = (int)Math.floor(this.z1);
        final int n4 = (int)Math.ceil(this.z2);
        final int n5 = (1 + n3 - n2) * (1 + n4 - z) * (1 + min - max);
        if (n5 > 500) {
            VerusLauncher.getPlugin().getLogger().severe(String.format("Tried to check %s blocks for %s in (%s, %s, %s) -> (%s, %s, %s) ", n5, supplier.get(), n2, max, z, n3, min, n4));
            return false;
        }
        final MutableBlockLocation mutableBlockLocation = new MutableBlockLocation(n2, max, z);
        while (mutableBlockLocation.getX() < n3) {
            while (mutableBlockLocation.getZ() < n4) {
                final Object apply = biFunction.apply(mutableBlockLocation.getX(), mutableBlockLocation.getZ());
                if (predicate.test(apply)) {
                    while (mutableBlockLocation.getY() < min) {
                        if (!biPredicate.test(apply, mutableBlockLocation)) {
                            return false;
                        }
                        mutableBlockLocation.incrementY();
                    }
                    mutableBlockLocation.setY(max);
                }
                mutableBlockLocation.incrementZ();
            }
            mutableBlockLocation.setZ(z);
            mutableBlockLocation.setY(max);
            mutableBlockLocation.incrementX();
        }
        return true;
    }
    
    public Cuboid move(final double n, final double n2, final double n3) {
        this.x1 += n;
        this.x2 += n;
        this.y1 += n2;
        this.y2 += n2;
        this.z1 += n3;
        this.z2 += n3;
        return this;
    }
    
    public double cZ() {
        return (this.z1 + this.z2) * 0.5;
    }
    
    public double distanceXZ(final double n, final double n2) {
        if (n2 <= 0) {
            return 0.0;
        }
        return MathHelper.sqrt_double(Math.min(Math.pow(n - this.x1, 2.0), Math.pow(n - this.x2, 2.0)) + Math.min(Math.pow(n2 - this.z1, 2.0), Math.pow(n2 - this.z2, 2.0)));
    }
    
    public boolean checkLoaded(final Supplier supplier, final IVerusWorld verusWorld) {
        return this.checkBlocksInternal(supplier, 256, Cuboid::lambda$checkLoaded$1, Cuboid::lambda$checkLoaded$2);
    }
    
    public boolean containsYZ(final double n, final double n2) {
        return this.y1 <= n && this.y2 >= n && this.z1 <= n2 && this.z2 >= n2;
    }
    
    public Cuboid(final IVector3d vector3d) {
        this(vector3d.getX(), vector3d.getY(), vector3d.getZ());
    }
    
    private static boolean lambda$checkBlocksInternal$7(final Predicate predicate, final IVerusChunk verusChunk, final IBlockPosition blockPosition) {
        return predicate.test((verusChunk == null) ? null : verusChunk.getType(blockPosition));
    }
    
    public Intersection calculateIntercept(final Vector3d vector3d, final Vector3d vector3d2) {
        Vector3d intermediateWithXValue = vector3d.getIntermediateWithXValue(vector3d2, this.x1);
        Vector3d intermediateWithXValue2 = vector3d.getIntermediateWithXValue(vector3d2, this.x2);
        Vector3d intermediateWithYValue = vector3d.getIntermediateWithYValue(vector3d2, this.y1);
        Vector3d intermediateWithYValue2 = vector3d.getIntermediateWithYValue(vector3d2, this.y2);
        Vector3d intermediateWithZValue = vector3d.getIntermediateWithZValue(vector3d2, this.z1);
        Vector3d intermediateWithZValue2 = vector3d.getIntermediateWithZValue(vector3d2, this.z2);
        if (intermediateWithXValue == null) {
            intermediateWithXValue = null;
        }
        if (intermediateWithXValue2 == null) {
            intermediateWithXValue2 = null;
        }
        if (intermediateWithYValue == null) {
            intermediateWithYValue = null;
        }
        if (intermediateWithYValue2 == null) {
            intermediateWithYValue2 = null;
        }
        if (intermediateWithZValue == null) {
            intermediateWithZValue = null;
        }
        if (intermediateWithZValue2 == null) {
            intermediateWithZValue2 = null;
        }
        Vector3d vector3d3 = null;
        if (intermediateWithXValue != null) {
            vector3d3 = intermediateWithXValue;
        }
        if (intermediateWithXValue2 != null && (vector3d3 == null || vector3d.distanceSquared(intermediateWithXValue2) < vector3d.distanceSquared(vector3d3))) {
            vector3d3 = intermediateWithXValue2;
        }
        if (intermediateWithYValue != null && (vector3d3 == null || vector3d.distanceSquared(intermediateWithYValue) < vector3d.distanceSquared(vector3d3))) {
            vector3d3 = intermediateWithYValue;
        }
        if (intermediateWithYValue2 != null && (vector3d3 == null || vector3d.distanceSquared(intermediateWithYValue2) < vector3d.distanceSquared(vector3d3))) {
            vector3d3 = intermediateWithYValue2;
        }
        if (intermediateWithZValue != null && (vector3d3 == null || vector3d.distanceSquared(intermediateWithZValue) < vector3d.distanceSquared(vector3d3))) {
            vector3d3 = intermediateWithZValue;
        }
        if (intermediateWithZValue2 != null && (vector3d3 == null || vector3d.distanceSquared(intermediateWithZValue2) < vector3d.distanceSquared(vector3d3))) {
            vector3d3 = intermediateWithZValue2;
        }
        if (vector3d3 == null) {
            return null;
        }
        Direction direction;
        if (vector3d3 == intermediateWithXValue) {
            direction = Direction.WEST;
        }
        else if (vector3d3 == intermediateWithXValue2) {
            direction = Direction.EAST;
        }
        else if (vector3d3 == intermediateWithYValue) {
            direction = Direction.DOWN;
        }
        else if (vector3d3 == intermediateWithYValue2) {
            direction = Direction.UP;
        }
        else if (vector3d3 == intermediateWithZValue) {
            direction = Direction.NORTH;
        }
        else {
            direction = Direction.SOUTH;
        }
        return new Intersection(vector3d3, vector3d3.copy().subtract((IVector3d)vector3d), direction);
    }
    
    public double getWidthZ() {
        return this.z2 - this.z1;
    }
    
    public boolean containsBlock(final World world, final int n, final int n2, final int n3) {
        final int n4 = (int)Math.floor(this.x1);
        final int n5 = (int)Math.ceil(this.x2);
        final int max = Math.max((int)Math.floor(this.y1), Cuboid.MIN_Y_LEVEL);
        final int min = Math.min((int)Math.ceil(this.y2), world.getMaxHeight());
        final int n6 = (int)Math.floor(this.z1);
        final int n7 = (int)Math.ceil(this.z2);
        return n4 <= n && n5 > n && max <= n2 && min > n2 && n6 <= n3 && n7 > n3;
    }
    
    private static boolean lambda$checkBlocksAndLoaded$3(final Predicate predicate, final IVerusBlock verusBlock) {
        if (verusBlock == null || verusBlock.getData() == null) {
            return false;
        }
        final Iterator iterator = verusBlock.iterator();
        while (iterator.hasNext()) {
            if (!predicate.test(iterator.next().getType().get())) {
                return false;
            }
        }
        return true;
    }
    
    public Cuboid expand(final double n, final double n2, final double n3) {
        this.x1 -= n;
        this.x2 += n;
        this.y1 -= n2;
        this.y2 += n2;
        this.z1 -= n3;
        this.z2 += n3;
        return this;
    }
    
    public Cuboid(final IBlockPosition blockPosition) {
        this(blockPosition.getX(), blockPosition.getY(), blockPosition.getZ());
    }
    
    public double getZ1() {
        return this.z1;
    }
    
    private Cuboid(final IVector3d vector3d, final IVector3d vector3d2) {
        this(Math.min(vector3d.getX(), vector3d2.getX()), Math.max(vector3d.getX(), vector3d2.getX()), Math.min(vector3d.getY(), vector3d2.getY()), Math.max(vector3d.getY(), vector3d2.getY()), Math.min(vector3d.getZ(), vector3d2.getZ()), Math.max(vector3d.getZ(), vector3d2.getZ()));
    }
    
    private static IVerusChunk lambda$checkBlocksInternal$5(final ChunkRef chunkRef, final IVerusWorld verusWorld, final Integer n, final Integer n2) {
        final int n3 = n >> 4;
        final int n4 = n2 >> 4;
        if (chunkRef.same(n3, n4)) {
            return (IVerusChunk)chunkRef.getChunk();
        }
        return (IVerusChunk)chunkRef.set((Object)verusWorld.getChunk(n3, n4), n3, n4);
    }
    
    public Cuboid face(final Direction direction) {
        switch (Cuboid.Cuboid$1.$SwitchMap$me$levansj01$verus$compat$api$wrapper$Direction[direction.ordinal()]) {
            case 1: {
                this.y1 = this.y2;
                break;
            }
            case 2: {
                this.y2 = this.y1;
                break;
            }
            case 3: {
                this.x1 = this.x2;
                break;
            }
            case 4: {
                this.x2 = this.x1;
                break;
            }
            case 5: {
                this.z1 = this.z2;
                break;
            }
            case 6: {
                this.z2 = this.z1;
                break;
            }
        }
        return this;
    }
    
    public void setY2(final double y2) {
        this.y2 = y2;
    }
    
    public Cuboid shrink(final double n, final double n2, final double n3) {
        this.x1 += n;
        this.x2 -= n;
        this.y1 += n2;
        this.y2 -= n2;
        this.z1 += n3;
        this.z2 -= n3;
        return this;
    }
    
    public Cuboid setValues(final double x1, final double x2, final double y1, final double y2, final double z1, final double z2) {
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
        this.z1 = z1;
        this.z2 = z2;
        return this;
    }
    
    public Cuboid(final double x1, final double x2, final double y1, final double y2, final double z1, final double z2) {
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
        this.z1 = z1;
        this.z2 = z2;
    }
    
    public double getX1() {
        return this.x1;
    }
    
    public boolean isSquare() {
        return this.x1 == 0.0 && this.x2 == 1.0 && this.y1 == 0.0 && this.y2 == 1.0 && this.z1 == 0.0 && this.z2 == 1.0;
    }
    
    public Cuboid copy() {
        return new Cuboid(this.x1, this.x2, this.y1, this.y2, this.z1, this.z2);
    }
    
    public boolean rayTraceInternal(final Supplier supplier, final Predicate predicate) {
        final double hypot = MathUtil.hypot(this.x2 - this.x1, this.y2 - this.y1, this.z2 - this.z1);
        final double lowest = MathUtil.lowest((this.x2 - this.x1) / hypot, (this.y2 - this.y1) / hypot, (this.z2 - this.z1) / hypot);
        int n = (int)Math.ceil(lowest / hypot);
        if (n > 100) {
            VerusLauncher.getPlugin().getLogger().severe(String.format("Tried to raytrace %s blocks for %s (%s, %s, %s) -> (%s, %s, %s) ", n, supplier.get(), this.x1, this.y1, this.z1, this.x2, this.y2, this.z2));
            return false;
        }
        final Vector3d vector3d = new Vector3d(this.x1, this.y1, this.z1);
        final MutableBlockLocation from = MutableBlockLocation.from(vector3d);
        while (n-- >= 0) {
            if (!predicate.test(from)) {
                return false;
            }
            vector3d.add(lowest);
            from.andThen(vector3d);
        }
        return true;
    }
    
    public double cX() {
        return (this.x1 + this.x2) * 0.5;
    }
    
    public boolean contains(final double n, final double n2, final double n3) {
        return this.x1 <= n && this.x2 >= n && this.y1 <= n2 && this.y2 >= n2 && this.z1 <= n3 && this.z2 >= n3;
    }
    
    public void setZ2(final double z2) {
        this.z2 = z2;
    }
    
    public boolean checkBlocks(final Supplier supplier, final IVerusWorld verusWorld, final Predicate predicate) {
        return this.checkBlocksInternal(supplier, verusWorld, Cuboid::lambda$checkBlocks$4);
    }
    
    public double distanceXZ(final PlayerLocation playerLocation) {
        return this.distanceXZ(playerLocation.getX(), playerLocation.getZ());
    }
    
    public BasicLocation[] corners() {
        final double[] array = (this.x1 != this.x2) ? new double[] { this.x1, this.x2 } : new double[] { this.x1 };
        final double[] array2 = (this.y1 != this.y2) ? new double[] { this.y1, this.y2 } : new double[] { this.y1 };
        final double[] array3 = (this.z1 != this.z2) ? new double[] { this.z1, this.z2 } : new double[] { this.z1 };
        final BasicLocation[] array4 = new BasicLocation[array.length * array2.length * array3.length];
        final double[] array5 = array;
        while (0 < array5.length) {
            final double n = array5[0];
            final double[] array6 = array2;
            while (0 < array6.length) {
                final double n2 = array6[0];
                final double[] array7 = array3;
                while (0 < array7.length) {
                    final double n3 = array7[0];
                    final BasicLocation[] array8 = array4;
                    final int n4 = 0;
                    int n5 = 0;
                    ++n5;
                    array8[n4] = new BasicLocation(n, n2, n3);
                    int n6 = 0;
                    ++n6;
                }
                int n7 = 0;
                ++n7;
            }
            int n8 = 0;
            ++n8;
        }
        return array4;
    }
    
    public Cuboid(final double n, final double n2, final double n3) {
        this(n, n, n2, n2, n3, n3);
    }
    
    public void setZ1(final double z1) {
        this.z1 = z1;
    }
    
    public double getWidthY() {
        return this.y2 - this.y1;
    }
    
    private static boolean lambda$checkBlocksInternal$6(final IVerusChunk verusChunk) {
        return true;
    }
    
    public Cuboid add(final Cuboid cuboid) {
        return this.add(cuboid.getX1(), cuboid.getX2(), cuboid.getY1(), cuboid.getY2(), cuboid.z1, cuboid.z2);
    }
    
    public Cuboid combine(final Cuboid cuboid) {
        return new Cuboid(Math.min(this.x1, cuboid.x1), Math.max(this.x2, cuboid.x2), Math.min(this.y1, cuboid.y1), Math.max(this.y2, cuboid.y2), Math.min(this.z1, cuboid.z1), Math.max(this.z2, cuboid.z2));
    }
    
    public boolean checkBlocksInternal(final Supplier supplier, final IVerusWorld verusWorld, final Predicate predicate) {
        return this.checkBlocksInternal(supplier, 256, Cuboid::lambda$checkBlocksInternal$5, Cuboid::lambda$checkBlocksInternal$6, Cuboid::lambda$checkBlocksInternal$7);
    }
    
    public double getY1() {
        return this.y1;
    }
    
    public double cY() {
        return (this.y1 + this.y2) * 0.5;
    }
    
    private static Boolean lambda$checkLoaded$1(final Integer n, final Integer n2) {
        return true;
    }
    
    public double getX2() {
        return this.x2;
    }
    
    public void include(final IVector3d vector3d) {
        final double x = vector3d.getX();
        final double y = vector3d.getY();
        final double z = vector3d.getZ();
        if (x < this.x1) {
            this.x1 = x;
        }
        if (x > this.x2) {
            this.x2 = x;
        }
        if (y < this.y1) {
            this.y1 = y;
        }
        if (y > this.y2) {
            this.y2 = y;
        }
        if (z < this.z1) {
            this.z1 = z;
        }
        if (z > this.z2) {
            this.z2 = z;
        }
    }
    
    public boolean containsBlock(final World world, final BlockPosition blockPosition) {
        return this.containsBlock(world, blockPosition.getX(), blockPosition.getY(), blockPosition.getZ());
    }
    
    public boolean overlaps(final Cuboid cuboid) {
        return this.x1 <= cuboid.x2 && cuboid.x1 <= this.x1 && this.z1 <= cuboid.z2 && cuboid.z1 <= this.z1;
    }
    
    public Cuboid() {
        this(0.0, 0.0, 0.0);
    }
    
    public boolean containsBlock(final World world, final PacketLocation packetLocation) {
        return this.containsBlock(world, (int)Math.floor(packetLocation.getX()), (int)Math.floor(packetLocation.getY()), (int)Math.floor(packetLocation.getZ()));
    }
    
    public boolean checkBlocksAndLoaded(final Supplier supplier, final IVerusWorld verusWorld, final Predicate predicate) {
        return this.checkBlocksInternal(supplier, verusWorld, Cuboid::lambda$checkBlocksAndLoaded$3);
    }
    
    public boolean containsXY(final double n, final double n2) {
        return this.x1 <= n && this.x2 >= n && this.y1 <= n2 && this.y2 >= n2;
    }
}
