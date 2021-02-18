package io.github.tkdkid1000;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import net.md_5.bungee.api.ChatColor;

public class Gun implements Listener {

	private List<Projectile> bullets = new ArrayList<Projectile>();
	private Map<UUID, Boolean> cooldown = new HashMap<UUID, Boolean>();
	private Map<UUID, Integer> ammo = new HashMap<UUID, Integer>();
	private MCGO mcgo;
	private String key;
	private Material mat;
	private String name;
	private int maxammo;
	private long delay;
	private long reloadtime;
	private double speed;
	private double damage;
	private boolean fireball;
	private int fireticks;

	public Gun(MCGO mcgo, String key, Material mat, String name, int maxammo, long delay, long reloadtime, double speed, double damage, boolean fireball, int fireticks) {
		this.mcgo = mcgo;
		this.key = key;
		this.mat = mat;
		this.name = name;
		this.maxammo = maxammo;
		this.delay = delay;
		this.reloadtime = reloadtime;
		this.speed = speed;
		this.damage = damage;
		this.fireball = fireball;
		this.fireticks = fireticks;
	}
	
	@SuppressWarnings("unchecked")
	public void register() {
		mcgo.getServer().getPluginManager().registerEvents(this, mcgo);
		
		@SuppressWarnings("rawtypes")
		List gunmeta = new ArrayList();
		gunmeta.add(mat);
		gunmeta.add(name);
		Commands.guns.put(key, gunmeta);
	}
	
	public void shoot(Player player) {
		if (ammo.get(player.getUniqueId()) > 0) {
			if (fireball) {
				Fireball fb = player.launchProjectile(Fireball.class);
				fb.setVelocity(fb.getVelocity().multiply(this.speed));
				fb.setIsIncendiary(false);
				fb.setFireTicks(fireticks);
			} else {
				Arrow arrow = player.launchProjectile(Arrow.class);
				arrow.setVelocity(arrow.getVelocity().multiply(this.speed));
				arrow.setKnockbackStrength(0);
				arrow.setFireTicks(fireticks);
				bullets.add(arrow);
			}
			cooldown.replace(player.getUniqueId(), true);
			ammo.replace(player.getUniqueId(), ammo.get(player.getUniqueId())-1);
		} else {
			player.sendMessage(ChatColor.GREEN + "[MCGO] " + ChatColor.RED + "That gun is out of ammo.");
		}
	}
	
	@EventHandler
	public void onProjectileHit(ProjectileHitEvent event) {
		if (bullets.contains(event.getEntity())) {
			bullets.remove(event.getEntity());
			event.getEntity().remove();

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
	public void onInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		ammo.putIfAbsent(player.getUniqueId(), maxammo);
		cooldown.putIfAbsent(player.getUniqueId(), false);
		if (event.getItem() != null) {
			if (event.getItem().getItemMeta().getDisplayName() == null) return;
			if (event.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(name) && event.getItem().getType() == mat) {
				event.setCancelled(true);
				if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
					if (cooldown.get(player.getUniqueId())) {
						player.sendMessage(ChatColor.GREEN + "[MCGO] " + ChatColor.RED + "That gun is still charging.");
					} else {
						shoot(player);
						new BukkitRunnable() {

							@Override
							public void run() {
								cooldown.replace(player.getUniqueId(), false);
							}
							
						}.runTaskLater(mcgo, delay);
					}
				} else if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
					player.sendMessage(ChatColor.GREEN + "[MCGO] " + ChatColor.GREEN + "Reloading...");
					new BukkitRunnable() {

						@Override
						public void run() {
							player.sendMessage(ChatColor.GREEN + "[MCGO] " + ChatColor.GREEN + "Reloaded!");
							ammo.replace(player.getUniqueId(), maxammo);
						}
						
					}.runTaskLater(mcgo, reloadtime);
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
	
	public Map<UUID, Integer> getAmmo() {
		return ammo;
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
	
	public boolean getFireball() {
		return fireball;
	}
	
	public int getFireticks() {
		return fireticks;
	}
}
