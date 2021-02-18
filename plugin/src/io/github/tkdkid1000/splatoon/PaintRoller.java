package io.github.tkdkid1000.splatoon;

import java.util.Random;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import io.github.tkdkid1000.MCGO;
import net.md_5.bungee.api.ChatColor;

public class PaintRoller extends SplatoonGun {

	public PaintRoller(MCGO mcgo, String key, Material mat, String name, long delay, double speed, double damage) {
		super(mcgo, key, mat, name, delay, speed, damage);
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		if (player.getItemInHand() != null) {
			ItemStack item = player.getItemInHand();
			if (item.getType() == this.getMaterial()
					&& item.getItemMeta().getDisplayName().equals(this.getName())) {
				if (!player.isOnGround()) return;
				if (event.getFrom().getBlock().equals(event.getTo().getBlock())) return;
				if (!(ink.get(player.getUniqueId()) > 1)) return;
				int color = new Random().nextInt(17);
				player.getLocation().subtract(0, 1, 0).getBlock().setType(Material.WOOL);
				player.getLocation().subtract(0, 1, 0).getBlock().setData((byte) color);
				player.getWorld().playEffect(player.getLocation(), Effect.COLOURED_DUST, color);
				for (Entity ent : player.getWorld().getNearbyEntities(player.getLocation(), 2, 2, 2)) {
					if (!ent.equals(player)) {
						ent.setVelocity(ent.getLocation().getDirection().multiply(-1));
						ent.sendMessage(ChatColor.GREEN + "[SplatCraft] " + ChatColor.RED + "You were pushed by " + player.getName() + "'s splatroller.!");
					}
				}
				ink.replace(player.getUniqueId(), ink.get(player.getUniqueId())-1);
			}
		}
	}
	
	public void shoot(Player player) {
		if (ink.get(player.getUniqueId()) > 0) {
			getCooldown().replace(player.getUniqueId(), true);
			player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 40, (int) getSpeed()));
			ink.replace(player.getUniqueId(), ink.get(player.getUniqueId())-1);
		} else {
			player.sendMessage(ChatColor.GREEN + "[SplatCraft] " + ChatColor.RED + "You are out of ink.");
		}
	}
}
