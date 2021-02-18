package io.github.tkdkid1000;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class Utils {

	public static List<Vector> getCube(Vector pos1, Vector pos2) {
		List<Vector> blocks = new ArrayList<Vector>();
		int x1 = Math.min(pos1.getBlockX(), pos2.getBlockX());
		int y1 = Math.min(pos1.getBlockY(), pos2.getBlockY());
		int z1 = Math.min(pos1.getBlockZ(), pos2.getBlockZ());
		int x2 = Math.max(pos1.getBlockX(), pos2.getBlockX());
		int y2 = Math.max(pos1.getBlockY(), pos2.getBlockY());
		int z2 = Math.max(pos1.getBlockZ(), pos2.getBlockZ());
		for (int x=x1; x<x2; x++) {
			for (int y=y1; y<y2; y++) {
				for (int z=z1; z<z2; z++) {
					blocks.add(new Vector(x, y, z));
				}
			}
		}
		return blocks;
	}
	
	public static int randomBound(int low, int high) {
		return new Random().nextInt(high-low)+low;
	}
	
	public Arrow shootArrowOffset(Player player) {
		Arrow arrow = player.launchProjectile(Arrow.class);
		arrow.setVelocity(arrow.getVelocity().multiply(randomBound(-2, 2)));
		return arrow;
	}
}
