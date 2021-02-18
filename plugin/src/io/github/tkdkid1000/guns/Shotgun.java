package io.github.tkdkid1000.guns;

import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;

import io.github.tkdkid1000.Gun;
import io.github.tkdkid1000.MCGO;
import net.md_5.bungee.api.ChatColor;

public class Shotgun extends Gun {

	public Shotgun(MCGO mcgo, String key, Material mat, String name, int maxammo, long delay, long reloadtime,
			double speed, double damage, boolean fireball, int fireticks) {
		super(mcgo, key, mat, name, maxammo, delay, reloadtime, speed, damage, fireball, fireticks);
	}

	public void shoot(Player player) {
		if (getAmmo().get(player.getUniqueId()) > 0) {
			if (getFireball()) {
				for (int x=0; x<5; x++) {
					Fireball fb = player.launchProjectile(Fireball.class);
					fb.setVelocity(fb.getVelocity().multiply(getSpeed()+(x/10)));
					fb.setIsIncendiary(false);
					fb.setFireTicks(getFireticks());
				}
			} else {
				for (int x=0; x<5; x++) {
					Arrow arrow = player.launchProjectile(Arrow.class);
					arrow.setVelocity(arrow.getVelocity().multiply(getSpeed()+(x/10)));
					arrow.setKnockbackStrength(0);
					arrow.setFireTicks(getFireticks());
					getBullets().add(arrow);
				}
			}
			getCooldown().replace(player.getUniqueId(), true);
			getAmmo().replace(player.getUniqueId(), getAmmo().get(player.getUniqueId())-1);
		} else {
			player.sendMessage(ChatColor.GREEN + "[MCGO+] " + ChatColor.RED + "That gun is out of ammo.");
		}
	}
}
