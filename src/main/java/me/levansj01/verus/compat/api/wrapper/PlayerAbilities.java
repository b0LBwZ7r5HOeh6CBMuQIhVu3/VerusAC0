package me.levansj01.verus.compat.api.wrapper;

import org.bukkit.*;
import me.levansj01.verus.compat.packets.*;

public class PlayerAbilities
{
    private final float flySpeed;
    private final boolean isFlying;
    private final float walkSpeed;
    private final boolean allowFlying;
    private final boolean isCreativeMode;
    private final boolean allowEdit;
    private final boolean disableDamage;
    
    @Override
    public int hashCode() {
        final int n = 59 + (this.isDisableDamage() ? 79 : 97);
        final int n2 = 59 + (this.isFlying() ? 79 : 97);
        final int n3 = 59 + (this.isAllowFlying() ? 79 : 97);
        final int n4 = 59 + (this.isCreativeMode() ? 79 : 97);
        final int n5 = 59 + (this.isAllowEdit() ? 79 : 97);
        final int n6 = 59 + Float.floatToIntBits(this.getFlySpeed());
        final int n7 = 59 + Float.floatToIntBits(this.getWalkSpeed());
        return 1;
    }
    
    public boolean isCreativeMode() {
        return this.isCreativeMode;
    }
    
    public float getWalkSpeed() {
        return this.walkSpeed;
    }
    
      public PlayerAbilities then(GameMode var1) {
      boolean var3 = this.isFlying;
      boolean var6 = var1 != GameMode.ADVENTURE;
      if (var1 != GameMode.CREATIVE) {
         var1.getValue();
         boolean var10001 = true;
      }


    public PlayerAbilities then(final VPacketPlayOutAbilities vPacketPlayOutAbilities) {
        return new PlayerAbilities(vPacketPlayOutAbilities.isInvulnerable(), vPacketPlayOutAbilities.isFlying(), vPacketPlayOutAbilities.isCanFly(), vPacketPlayOutAbilities.isCanInstantlyBuild(), this.allowEdit, vPacketPlayOutAbilities.getFlySpeed(), vPacketPlayOutAbilities.getWalkSpeed());
    }
    
    protected boolean canEqual(final Object o) {
        return o instanceof PlayerAbilities;
    }
    
    public float getFlySpeed() {
        return this.flySpeed;
    }
    
    @Deprecated
    public PlayerAbilities(final boolean b, final boolean b2, final boolean b3, final boolean b4, final float n, final float n2) {
        this(b, b2, b3, b4, true, n, n2);
    }
    
    public boolean isAllowEdit() {
        return this.allowEdit;
    }
    
    public boolean isDisableDamage() {
        return this.disableDamage;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof PlayerAbilities)) {
            return false;
        }
        final PlayerAbilities playerAbilities = (PlayerAbilities)o;
        return playerAbilities.canEqual(this) && this.isDisableDamage() == playerAbilities.isDisableDamage() && this.isFlying() == playerAbilities.isFlying() && this.isAllowFlying() == playerAbilities.isAllowFlying() && this.isCreativeMode() == playerAbilities.isCreativeMode() && this.isAllowEdit() == playerAbilities.isAllowEdit() && Float.compare(this.getFlySpeed(), playerAbilities.getFlySpeed()) == 0 && Float.compare(this.getWalkSpeed(), playerAbilities.getWalkSpeed()) == 0;
    }
    
    public boolean isFlying() {
        return this.isFlying;
    }
    
    public boolean fly() {
        return this.allowFlying || this.isFlying;
    }
    
    public PlayerAbilities(final boolean disableDamage, final boolean isFlying, final boolean allowFlying, final boolean isCreativeMode, final boolean allowEdit, final float flySpeed, final float walkSpeed) {
        this.disableDamage = disableDamage;
        this.isFlying = isFlying;
        this.allowFlying = allowFlying;
        this.isCreativeMode = isCreativeMode;
        this.allowEdit = allowEdit;
        this.flySpeed = flySpeed;
        this.walkSpeed = walkSpeed;
    }
    
    public boolean isAllowFlying() {
        return this.allowFlying;
    }
}
