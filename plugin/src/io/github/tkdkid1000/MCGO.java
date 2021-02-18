package io.github.tkdkid1000;

import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.tkdkid1000.guns.Pistol;
import io.github.tkdkid1000.guns.RocketLauncher;
import io.github.tkdkid1000.guns.SMG;
import io.github.tkdkid1000.guns.Shotgun;
import io.github.tkdkid1000.guns.Sniper;
import io.github.tkdkid1000.splatoon.PaintBlaster;
import io.github.tkdkid1000.splatoon.PaintRoller;
import net.md_5.bungee.api.ChatColor;

public class MCGO extends JavaPlugin {

	@SuppressWarnings("deprecation")
	@Override
	public void onEnable() {
		new Pistol(this, "mcgo:pistol", Material.IRON_HOE, ChatColor.GRAY + "Pistol", 10, 10, 40, 1, 4.0, false, 0).register();
		new RocketLauncher(this, "mcgo:rpg", Material.IRON_BARDING, ChatColor.GRAY + "Rocket Launcher", 1, 20, 60, 5.0, 10.0, true, -1).register();
		new SMG(this, "mcgo:smg", Material.STONE_HOE, ChatColor.GRAY + "SMG", 30, 0, 40, 2, 3.0, false, 0).register();
		new Sniper(this, "mcgo:sniper", Material.DIAMOND_BARDING, ChatColor.GRAY + "Sniper", 1, 20, 60, 10.0, 10.0, false, 0).register();
		new Shotgun(this, "mcgo:shotgun", Material.IRON_BARDING, ChatColor.GRAY + "Shotgun", 5, 20, 60, 2, 5.0, false, 0).register();
		new PaintBlaster(this, "splatcraft:splattershot", Material.getMaterial(418), ChatColor.GREEN + "[SplatCraft] " + ChatColor.GRAY + "Splattershot", 20, 2, 5).register();
		new PaintRoller(this, "splatcraft:splatroller", Material.GOLD_BARDING, ChatColor.GREEN + "[SplatCraft] " + ChatColor.GRAY + "Splat Roller", 100, 4, 0).register();
		new Commands(this).register();
	}
	
	@Override
	public void onDisable() {
		
	}
}
