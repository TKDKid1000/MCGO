package io.github.tkdkid1000;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.StringUtil;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;

public class Commands implements CommandExecutor, TabExecutor {

	private MCGO mcgo;
	@SuppressWarnings("rawtypes")
	public static Map<String, List> guns = new HashMap<String, List>();
	
	public Commands(MCGO mcgo) {
		this.mcgo = mcgo;
	}
	
	public void register() {
		mcgo.getCommand("mcgo").setExecutor(this);
		mcgo.getCommand("mcgo").setTabCompleter(this);
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String alias,
			String[] args) {
		List<String> commands = new ArrayList<String>();
		List<String> completions = new ArrayList<String>();
		if (cmd.getName().equalsIgnoreCase("mcgo")) {
			if (args.length == 1) {
				commands.add("help");
				commands.add("version");
				commands.add("guns");
				commands.add("give");
				StringUtil.copyPartialMatches(args[0], commands, completions);
			} else {
				if (args[0].equalsIgnoreCase("give")) {
					if (args.length == 2) {
						Bukkit.getOnlinePlayers().forEach((Player p) -> commands.add(p.getName()));
						StringUtil.copyPartialMatches(args[1], commands, completions);
					} else if (args.length == 3) {
						guns.keySet().forEach((String s) -> commands.add(s));
						StringUtil.copyPartialMatches(args[2], commands, completions);
					}
				}
			}
		}
		Collections.sort(completions);
		return completions;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (args.length == 0) {
				player.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "------MCGO Commands------");
				player.sendMessage(ChatColor.GOLD + "/mcgo | Main Command");
				player.sendMessage(ChatColor.GOLD + "/mcgo guns | List Guns");
				player.sendMessage(ChatColor.GOLD + "/mcgo give <user> <gun> | Command to give Guns");
				player.sendMessage(ChatColor.GOLD + "/mcgo version | Show the plugin version and info");
				player.sendMessage(ChatColor.GOLD + "/mcgo help | Show this message");
			} else if (args[0].equalsIgnoreCase("give")) {
				if (args.length == 1) {
					player.sendMessage(ChatColor.GREEN + "Please specify a player and a gun!");
				} else if (args.length == 2) {
					player.sendMessage(ChatColor.GREEN + "Please specify a gun!");
				} else if (args.length == 3) {
					if (guns.containsKey(args[2])) {
						player.sendMessage(ChatColor.GREEN + "Giving " + args[2] + " to " + args[1] + ".");
						Player target = Bukkit.getPlayer(args[1]);
						if (target == null) {
							player.sendMessage(ChatColor.GREEN + "That player is not online!");
							return true;
						}
						ItemStack item = new ItemStack((Material) guns.get(args[2]).get(0));
						ItemMeta meta = item.getItemMeta();
						meta.setDisplayName((String) guns.get(args[2]).get(1));
						item.setItemMeta(meta);
						target.getInventory().addItem(item);
					} else {
						player.sendMessage(ChatColor.GREEN + "That gun does not exist!");
					}
				}
			} else if (args[0].equalsIgnoreCase("guns")) {
				String message = ChatColor.translateAlternateColorCodes('&', "&4&l------MCGO Guns------\n");
				for (String s : guns.keySet()) {
					message = message + ChatColor.GOLD + s + "\n";
				}
				player.spigot().sendMessage(new ComponentBuilder(message)
						.event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/mcgo give " + player.getName() + " <gun>"))
						.create());
			} else if (args[0].equalsIgnoreCase("version")) {
				player.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "------MCGO------");
				player.sendMessage(ChatColor.GOLD + "Author: " + mcgo.getDescription().getName());
				player.sendMessage(ChatColor.GOLD + "Version: " + mcgo.getDescription().getVersion());
				player.sendMessage(ChatColor.GOLD + "Website: " + mcgo.getDescription().getWebsite());
			} else if (args[0].equalsIgnoreCase("help")) {
				player.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "------MCGO Commands------");
				player.sendMessage(ChatColor.GOLD + "/mcgo | Main Command");
				player.sendMessage(ChatColor.GOLD + "/mcgo guns | List Guns");
				player.sendMessage(ChatColor.GOLD + "/mcgo give <user> <gun> | Command to give Guns");
				player.sendMessage(ChatColor.GOLD + "/mcgo version | Show the plugin version and info");
				player.sendMessage(ChatColor.GOLD + "/mcgo help | Show this message");
			} else {
				player.sendMessage(ChatColor.RED + "Unknown Command, type /mcgo help for help.");
			}
		}
		return true;
	}

}
