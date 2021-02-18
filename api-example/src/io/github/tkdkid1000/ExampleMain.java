package io.github.tkdkid1000;

import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.tkdkid1000.guns.CustomShootSequence;
import io.github.tkdkid1000.guns.Overpowered;
import net.md_5.bungee.api.ChatColor;

public class ExampleMain extends JavaPlugin {

	@Override
	public void onEnable() {
		// Getting an instance of the MCGO class
		MCGO mcgo = (MCGO) getServer().getPluginManager().getPlugin("MCGO");
		// Registering the "Overpowered" gun class
		new Overpowered(mcgo, /*When making a key it is good to put a "prefix:" in front of it to seperate it from other extensions.*/"mcgoexample:overpowered", 
				/*The material that your gun will be. You can customize this with a resource pack.*/Material.DIAMOND_SPADE, 
				/*The name of the actual gun, what players see.*/ChatColor.RED + "[MCGO Example] " + ChatColor.GRAY + "Overpowered", 10, /*This is in game ticks, 20 ticks in a second*/20
				/*Also in game ticks*/, 0, 10.0, 10.0, false, /*How many game ticks the arrow is on fire for, -1 for infinity.*/-1).register();
		// You can also directly register a gun like this. This is the simplist way, but doesn't add much customization.
		new Gun(mcgo, "mcgoexample:testgun", Material.WOOD_HOE, ChatColor.RED + "[MCGO Example] " + ChatColor.GRAY + "test gun", 0, 0, 0, 0, 0, false, 0).register();
		// Just registering a "new SplatoonGun" will not work, you have to custom code it. Check the main SRC for how to.
		// Guns with custom shoot sequences (and custom events!) are registered the same as class based guns, but you may add extra stuff to the constructor.
		new CustomShootSequence(mcgo, "mcgoexample:css", Material.COMMAND, ChatColor.RED + "[MCGO Example] " + ChatColor.GRAY + "Custom Shoot Sequence", 0, 0, 0, 0, 0, false, 0).register();
	}
	
	@Override
	public void onDisable() {
		
	}
}