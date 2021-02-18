package io.github.tkdkid1000.guns;

import org.bukkit.Material;

import io.github.tkdkid1000.Gun;
import io.github.tkdkid1000.MCGO;

public class RocketLauncher extends Gun {

	public RocketLauncher(MCGO mcgo, String key, Material mat, String name, int maxammo, long delay, long reloadtime,
			double speed, double damage, boolean fireball, int fireticks) {
		super(mcgo, key, mat, name, maxammo, delay, reloadtime, speed, damage, fireball, fireticks);
	}
}
