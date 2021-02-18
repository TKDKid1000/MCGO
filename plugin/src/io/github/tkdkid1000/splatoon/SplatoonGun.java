package io.github.tkdkid1000.splatoon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import io.github.tkdkid1000.Commands;
import io.github.tkdkid1000.MCGO;
import io.github.tkdkid1000.Utils;
import net.md_5.bungee.api.ChatColor;

public class SplatoonGun implements Listener {

	private List<Projectile> bullets = new ArrayList<Projectile>();
	private Map<UUID, Boolean> cooldown = new HashMap<UUID, Boolean>();
	public static Map<UUID, Integer> ink = new HashMap<UUID, Integer>();
	private MCGO mcgo;
	private String key;
	private Material mat;
	private String name;
	private long delay;
	private double speed;
	private double damage;

	public SplatoonGun(MCGO mcgo, String key, Material mat, String name, long delay, double speed, double damage) {
		this.mcgo = mcgo;
		this.key = key;
		this.mat = mat;
		this.name = name;
		this.delay = delay;
		this.speed = speed;
		this.damage = damage;
	}
	
	@SuppressWarnings("unchecked")
	public void register() {
		mcgo.getServer().getPluginManager().registerEvents(this, mcgo);
		
		@SuppressWarnings("rawtypes")
		List gunmeta = new ArrayList();
		gunmeta.add(mat);
		gunmeta.add(name);
		Commands.guns.put(key, gunmeta);
		new BukkitRunnable() {

			@Override
			public void run() {
				for (Player player : Bukkit.getOnlinePlayers()) {
					ink.putIfAbsent(player.getUniqueId(), 10);
					player.setExp(((float)ink.get(player.getUniqueId()))/10);
				}
			}
			
		}.runTaskTimer(mcgo, 1, 1);
		new BukkitRunnable() {

			@Override
			public void run() {
				for (Player player : Bukkit.getOnlinePlayers()) {
					if (player.getInventory().getItemInHand() != null) {
						if (player.isSneaking() 
								&& player.getInventory().getItemInHand().getType() == mat
								&& player.getInventory().getItemInHand().getItemMeta().getDisplayName().equals(name)) {
							if (ink.get(player.getUniqueId()) <= 10) {
								ink.replace(player.getUniqueId(), ink.get(player.getUniqueId())+1);
							} else {
								player.sendMessage(ChatColor.GREEN + "[SplatCraft] " + ChatColor.RED + "Your ink is full.");
							}
						}
					}
				}
			}
			
		}.runTaskTimer(mcgo, 20, 20);
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onArrowLand(ProjectileHitEvent event) {
		Projectile proj = event.getEntity();
		if (bullets.contains(proj)) {
			if (proj instanceof Arrow) {
				Arrow arrow = (Arrow) proj;
				Location loc = arrow.getLocation();
				int color = new Random().nextInt(17);
				for (Vector v : Utils.getCube(loc.clone().subtract(2, 2, 2).toVector(), loc.clone().add(2, 2, 2).toVector())) {
					if (v.toLocation(loc.getWorld()).getBlock().getType() != Material.AIR) {
						v.toLocation(loc.getWorld()).getBlock().setType(Material.WOOL);
						v.toLocation(loc.getWorld()).getBlock().setData((byte) color);
					}
				}
				loc.getWorld().playEffect(loc, Effect.COLOURED_DUST, color);
				bullets.remove(proj);
				arrow.remove();
			}
		}
	}
	
	public void shoot(Player player) {
		if (ink.get(player.getUniqueId()) > 0) {
			Arrow arrow = player.launchProjectile(Arrow.class);
			arrow.setVelocity(arrow.getVelocity().multiply(this.speed));
			arrow.setKnockbackStrength(0);
			bullets.add(arrow);
			cooldown.replace(player.getUniqueId(), true);
			ink.replace(player.getUniqueId(), ink.get(player.getUniqueId())-1);
		} else {
			player.sendMessage(ChatColor.GREEN + "[SplatCraft] " + ChatColor.RED + "You are out of ink.");
		}
	}
	
	@EventHandler
	public void onEntityDamage(EntityDamageByEntityEvent event) {
		if (bullets.contains(event.getEntity())) {
			event.setCancelled(true);
			if (event.getEntity() instanceof Player) {
				Player player = (Player) event.getEntity();
				player.setHealth(player.getHealth()-this.damage);
				player.getWorld().playEffect(player.getLocation(), Effect.CRIT, 1);
			}
		}
	}
	
	@EventHandler
	public void onPickupExp(PlayerExpChangeEvent event) {
		event.setAmount(0);
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		ink.putIfAbsent(player.getUniqueId(), 20);
		cooldown.putIfAbsent(player.getUniqueId(), false);
		if (event.getItem() != null) {
			if (event.getItem().getItemMeta().getDisplayName() == null) return;
			if (event.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(name) && event.getItem().getType() == mat) {
				if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
					if (cooldown.get(player.getUniqueId())) {
						player.sendMessage(ChatColor.GREEN + "[SplatCraft] " + ChatColor.RED + "That gun is still charging.");
					} else {
						shoot(player);
						new BukkitRunnable() {

							@Override
							public void run() {
								cooldown.replace(player.getUniqueId(), false);
							}
							
						}.runTaskLater(mcgo, delay);
					}
				}
			}
		}
	}
	
	public Material getMaterial() {
		return mat;
	}
	
	public String getName() {
		return name;
	}
	
	public String getKey() {
		return key;
	}
	
	public List<Projectile> getBullets() {
		return bullets;
	}
	
	public Map<UUID, Boolean> getCooldown() {
		return cooldown;
	}
	
	public long getDelay() {
		return delay;
	}
	
	public double getSpeed() {
		return speed;
	}
	
	public double getDamage() {
		return damage;
	}
}
