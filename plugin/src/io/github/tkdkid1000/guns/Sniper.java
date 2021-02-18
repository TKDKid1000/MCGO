package io.github.tkdkid1000.guns;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import io.github.tkdkid1000.Gun;
import io.github.tkdkid1000.MCGO;

public class Sniper extends Gun {

	public Sniper(MCGO mcgo, String key, Material mat, String name, int maxammo, long delay, long reloadtime,
			double speed, double damage, boolean fireball, int fireticks) {
		super(mcgo, key, mat, name, maxammo, delay, reloadtime, speed, damage, fireball, fireticks);
	}
	
	@EventHandler
	public void onMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		if (player.getItemInHand().getType() != Material.AIR) {
			ItemStack item = player.getItemInHand();
			if (item.getItemMeta().getDisplayName() == null) return;
			if (item.getType() == this.getMaterial() && item.getItemMeta().getDisplayName().equals(this.getName())) {
				if (player.isSneaking()) {
					player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 200000, 255, false, false));
					player.getInventory().setHelmet(new ItemStack(Material.PUMPKIN));
				} else {
					player.removePotionEffect(PotionEffectType.SLOW);
					player.getInventory().setHelmet(new ItemStack(Material.AIR));
				}
			}
		}
	}
}