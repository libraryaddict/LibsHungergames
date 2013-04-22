package me.libraryaddict.Hungergames.Kits;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

import me.libraryaddict.Hungergames.Managers.KitManager;
import me.libraryaddict.Hungergames.Managers.PlayerManager;
import me.libraryaddict.Hungergames.Types.Enchants;
import me.libraryaddict.Hungergames.Types.HungergamesApi;

public class Salvager implements Listener {

    private PlayerManager pm = HungergamesApi.getPlayerManager();
    private KitManager kits = HungergamesApi.getKitManager();

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player p = event.getPlayer();
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getClickedBlock().getType() == Material.ANVIL
                && pm.getGamer(p).isAlive() && kits.hasAbility(p, "Salvager")) {
            ItemStack item = event.getItem();
            if (item != null && item.getType() != Material.AIR && item.getType() != Material.ANVIL
                    && item.getType() != Material.COMPASS && !item.containsEnchantment(Enchants.UNLOOTABLE)) {
                for (Recipe recipe : Bukkit.getRecipesFor(item)) {
                    if (recipe.getResult().getAmount() > item.getAmount())
                        continue;
                    if (recipe instanceof ShapelessRecipe) {
                        item.setAmount(item.getAmount() - recipe.getResult().getAmount());
                        if (item.getAmount() <= 0)
                            p.setItemInHand(new ItemStack(0));
                        event.setCancelled(true);
                        for (ItemStack items : ((ShapelessRecipe) recipe).getIngredientList())
                            if (items != null && items.getType() != Material.AIR)
                                kits.addItem(p, items.clone());
                        p.updateInventory();
                        break;
                    }
                    if (recipe instanceof ShapedRecipe) {
                        item.setAmount(item.getAmount() - recipe.getResult().getAmount());
                        if (item.getAmount() <= 0)
                            p.setItemInHand(new ItemStack(0));
                        event.setCancelled(true);
                        for (ItemStack items : ((ShapedRecipe) recipe).getIngredientMap().values())
                            if (items != null && items.getType() != Material.AIR)
                                kits.addItem(p, items.clone());
                        p.updateInventory();
                        break;
                    }
                }
            }
        }
    }

}