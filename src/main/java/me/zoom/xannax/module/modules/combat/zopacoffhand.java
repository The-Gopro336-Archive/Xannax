    package me.zoom.xannax.module.modules.combat;
    import net.minecraft.client.gui.inventory.GuiContainer;
    import com.mojang.realmsclient.gui.ChatFormatting;
    import me.zoom.xannax.module.ModuleManager;
    import net.minecraft.inventory.ClickType;
    import me.zoom.xannax.setting.Setting;
    import net.minecraft.item.ItemStack;
    import me.zoom.xannax.module.Module;
    import net.minecraft.init.Items;
    import net.minecraft.item.Item;
    import java.util.ArrayList;
    public class zopacoffhand extends Module{
    public zopacoffhand(){super("OffhandRewrite", "i own ur family -zopac", Category. Combat);}
    boolean moving = false; boolean returnI = false; int totems;
    Setting.Mode offhanditem; Setting.Integer health; Setting.Boolean autocrystal; Setting.Boolean auragap;
    public void setup(){ ArrayList<String> modes = new ArrayList<>();
    modes.add("Gapple"); modes.add("Crystal"); modes.add("Totem");
    autocrystal = registerBoolean("CrystalAC", "crystal swap on autocrystal", false);
    auragap = registerBoolean("AuraGap", "it does the thing", true);
    offhanditem = registerMode("Mode", "the mode thing", modes, "Crystal");
    health = registerInteger("Health", "switch health", 16, 1, 36); }
    @Override public void onUpdate(){ Item item = getItem(); if (mc.currentScreen instanceof GuiContainer) return; if (returnI) { int t = -1; for (int i = 0; i < 45; i++)
    if (mc.player.inventory.getStackInSlot(i).isEmpty()){ t = i; break; } if (t == -1) return;
    mc.playerController.windowClick(0, t < 9 ? t + 36 : t, 0, ClickType.PICKUP, mc.player); returnI = false; }
    totems = mc.player.inventory.mainInventory.stream().filter(itemStack -> itemStack.getItem() == item).mapToInt(ItemStack::getCount).sum();
    if (mc.player.getHeldItemOffhand().getItem() == item) totems++;
    else { if (moving){ mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, mc.player); moving = false; if (!mc.player.inventory.getItemStack().isEmpty()) returnI = true; return; }
    if (mc.player.inventory.getItemStack().isEmpty()){ if (totems == 0) return; int t = -1;
    for (int i = 0; i < 45; i++) if (mc.player.inventory.getStackInSlot(i).getItem() == item){ t = i; break; } if (t == -1) return;
    mc.playerController.windowClick(0, t < 9 ? t + 36 : t, 0, ClickType.PICKUP, mc.player); moving = true; } } }
    @Override public String getHudInfo(){ String t = "[" + ChatFormatting.WHITE + offhanditem.getValue() + ChatFormatting.GRAY + "]"; return t; }
    public Item getItem(){ Item item = Items.TOTEM_OF_UNDYING; Item mainItem = null;
    boolean Crystal = false; if (mc.player.getHealth() < health.getValue()){ item = Items.TOTEM_OF_UNDYING;
    } else if (autocrystal.getValue() && ModuleManager.isModuleEnabled("AutoCrystal")){ item = Items.END_CRYSTAL; Crystal = true;
    } else if (auragap.getValue() && ModuleManager.isModuleEnabled("Aura") && !Crystal)
    { item = Items.GOLDEN_APPLE; } else if (offhanditem.getValue() == "Totem")
    { item = Items.TOTEM_OF_UNDYING; mainItem = Items.TOTEM_OF_UNDYING;
    } else if (offhanditem.getValue() == "Crystal") { item = Items.END_CRYSTAL;
    mainItem = Items.END_CRYSTAL; } else if (offhanditem.getValue() == "Gapple"){
    item = Items.GOLDEN_APPLE; mainItem = Items.GOLDEN_APPLE; } return item; } }

// this was fucking cancer to make never tell me to make anything ever again i fucking hate you zoom -zopac
// ill make it look nicer once zoom is not chinese
// i hate all of you
