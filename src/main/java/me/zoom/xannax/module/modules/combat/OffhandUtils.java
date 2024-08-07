package me.zoom.xannax.module.modules.combat;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.item.EntityEnderCrystal;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.inventory.ClickType;
import net.minecraft.util.math.BlockPos;
import me.zoom.xannax.setting.Setting;
import net.minecraft.item.ItemStack;
import me.zoom.xannax.module.Module;
import java.util.stream.Collectors;
import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import java.util.ArrayList;
import java.util.List;

public class OffhandUtils extends Module {

    public int totems;
    int crystals;
    boolean moving;
    boolean returnI;
    Item item;
    Setting.Mode mode;
    Setting.Integer health;
    Setting.Double fallDist;

    public OffhandUtils() {
        super("OffhandUtils", "OffhandUtils", Category.Combat);
        this.moving = false;
        this.returnI = false;
    }

    @Override
    public void setup() {
        ArrayList<String> modes = new ArrayList<>();
        modes.add("Gapple");
        modes.add("Crystal");
        mode = registerMode("Mode", "ModeOH", modes, "Crystal");
        health = registerInteger("Health", "Health", 15, 0, 36);
        fallDist = registerDouble("FallDistance", "FallDistance", 15, 0, 30);
    }

    public void onDisable() {
        if (OffhandUtils.mc.currentScreen instanceof GuiContainer) {
            return;
        }
        this.crystals = OffhandUtils.mc.player.inventory.mainInventory.stream().filter(itemStack -> itemStack.getItem() == Items.TOTEM_OF_UNDYING).mapToInt(ItemStack::getCount).sum();
        if (OffhandUtils.mc.player.getHeldItemOffhand().getItem() != Items.TOTEM_OF_UNDYING) {
            if (this.crystals == 0) {
                return;
            }
            int t = -1;
            for (int i = 0; i < 45; ++i) {
                if (OffhandUtils.mc.player.inventory.getStackInSlot(i).getItem() == Items.TOTEM_OF_UNDYING) {
                    t = i;
                    break;
                }
            }
            if (t == -1) {
                return;
            }
            OffhandUtils.mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, OffhandUtils.mc.player);
            OffhandUtils.mc.playerController.windowClick(0, (t < 9) ? (t + 36) : t, 0, ClickType.PICKUP, OffhandUtils.mc.player);
            OffhandUtils.mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, OffhandUtils.mc.player);
        }
    }

    @Override
    public void onUpdate() {
        this.item = ittem();
        if (OffhandUtils.mc.currentScreen instanceof GuiContainer) {
            return;
        }
        if (this.returnI) {
            int t = -1;
            for (int i = 0; i < 45; ++i) {
                if (OffhandUtils.mc.player.inventory.getStackInSlot(i).isEmpty()) {
                    t = i;
                    break;
                }
            }
            if (t == -1) {
                return;
            }
            OffhandUtils.mc.playerController.windowClick(0, (t < 9) ? (t + 36) : t, 0, ClickType.PICKUP, OffhandUtils.mc.player);
            this.returnI = false;
        }
        this.totems = OffhandUtils.mc.player.inventory.mainInventory.stream().filter(itemStack -> itemStack.getItem() == Items.TOTEM_OF_UNDYING).mapToInt(ItemStack::getCount).sum();
        this.crystals = OffhandUtils.mc.player.inventory.mainInventory.stream().filter(itemStack -> itemStack.getItem() == this.item).mapToInt(ItemStack::getCount).sum();
        if (this.shouldTotem() && OffhandUtils.mc.player.getHeldItemOffhand().getItem() == Items.TOTEM_OF_UNDYING) {
            ++this.totems;
        } else if (!this.shouldTotem() && OffhandUtils.mc.player.getHeldItemOffhand().getItem() == this.item) {
            this.crystals += OffhandUtils.mc.player.getHeldItemOffhand().getCount();
        } else {
            if (this.moving) {
                OffhandUtils.mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, OffhandUtils.mc.player);
                this.moving = false;
                this.returnI = true;
                return;
            }
            if (OffhandUtils.mc.player.inventory.getItemStack().isEmpty()) {
                if (!this.shouldTotem() && OffhandUtils.mc.player.getHeldItemOffhand().getItem() == this.item) {
                    return;
                }
                if (this.shouldTotem() && OffhandUtils.mc.player.getHeldItemOffhand().getItem() == Items.TOTEM_OF_UNDYING) {
                    return;
                }
                if (!this.shouldTotem()) {
                    if (this.crystals == 0) {
                        return;
                    }
                    int t = -1;
                    for (int i = 0; i < 45; ++i) {
                        if (OffhandUtils.mc.player.inventory.getStackInSlot(i).getItem() == this.item) {
                            t = i;
                            break;
                        }
                    }
                    if (t == -1) {
                        return;
                    }
                    OffhandUtils.mc.playerController.windowClick(0, (t < 9) ? (t + 36) : t, 0, ClickType.PICKUP, OffhandUtils.mc.player);
                    this.moving = true;
                } else {
                    if (this.totems == 0) {
                        return;
                    }
                    int t = -1;
                    for (int i = 0; i < 45; ++i) {
                        if (OffhandUtils.mc.player.inventory.getStackInSlot(i).getItem() == Items.TOTEM_OF_UNDYING) {
                            t = i;
                            break;
                        }
                    }
                    if (t == -1) {
                        return;
                    }
                    OffhandUtils.mc.playerController.windowClick(0, (t < 9) ? (t + 36) : t, 0, ClickType.PICKUP, OffhandUtils.mc.player);
                    this.moving = true;
                }
            } else {
                int t = -1;
                for (int i = 0; i < 45; ++i) {
                    if (OffhandUtils.mc.player.inventory.getStackInSlot(i).isEmpty()) {
                        t = i;
                        break;
                    }
                }
                if (t == -1) {
                    return;
                }
                OffhandUtils.mc.playerController.windowClick(0, (t < 9) ? (t + 36) : t, 0, ClickType.PICKUP, OffhandUtils.mc.player);
            }
        }
    }

    private boolean shouldTotem() {
        final boolean hp = OffhandUtils.mc.player.getHealth() + OffhandUtils.mc.player.getAbsorptionAmount() <= health.getValue() || mc.player.fallDistance >= fallDist.getValue();
        final boolean endcrystal = !this.isCrystalsAABBEmpty();
        return hp;
    }

    public Item ittem() {
        Item p;
        if (mode.getValue().equalsIgnoreCase("Crystal")) {
            p = Items.END_CRYSTAL;
        } else {
            p = Items.GOLDEN_APPLE;
        }
        return p;
    }

    private boolean isEmpty(final BlockPos pos) {
        final List<Entity> crystalsInAABB = OffhandUtils.mc.world.getEntitiesWithinAABBExcludingEntity(null, new AxisAlignedBB(pos)).stream().filter(e -> e instanceof EntityEnderCrystal).collect(Collectors.toList());
        return crystalsInAABB.isEmpty();
    }

    private boolean isCrystalsAABBEmpty() {
        return this.isEmpty(OffhandUtils.mc.player.getPosition().add(1, 0, 0)) && this.isEmpty(OffhandUtils.mc.player.getPosition().add(-1, 0, 0)) && this.isEmpty(OffhandUtils.mc.player.getPosition().add(0, 0, 1)) && this.isEmpty(OffhandUtils.mc.player.getPosition().add(0, 0, -1)) && this.isEmpty(OffhandUtils.mc.player.getPosition());
    }

    @Override
    public String getHudInfo(){
        String t = "";
        if(mode.getValue().equalsIgnoreCase("Crystal")) {
            t = "[" + ChatFormatting.WHITE + "C" + ChatFormatting.GRAY + "]";
        } else{
            t = "[" + ChatFormatting.WHITE + "G" + ChatFormatting.GRAY + "]";
        }
        return t;
    }
}
