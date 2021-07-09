package easytravel.command.travel;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import easytravel.Main;
import easytravel.travel.Travel;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class TravelCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (!(sender instanceof Player)) {
			Main.LogToConsole("The command /travel is only available for players!");
			return true;
		}

		Player p = (Player) sender;

		if (!sender.hasPermission("easytravel.travel")) {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
					"&cYou don't have permission to excecute this command!"));
			return true;

		}

		if (!Main.getTravelManager().hasTravel(p.getUniqueId())) {
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou currently aren't travelling anywhere!"));
			return true;
		}

		if (args.length > 0 && args[0].equalsIgnoreCase("stop")) {
			Travel t = Main.getTravelManager().getTravel(p.getUniqueId());
			p.sendMessage(ChatColor.translateAlternateColorCodes('&',
					"&7Your travel to &r&b&l" + t.getDestinationName() + " &r&7has been stopped!"));
			Main.getTravelManager().removeTravel(p.getUniqueId());
			return true;
		}

		Travel t = Main.getTravelManager().getTravel(p.getUniqueId());
		p.sendMessage("");
		p.sendMessage(ChatColor.translateAlternateColorCodes('&',
				"&7You are currently travelling to &r&b&l" + t.getDestinationName()));
		TextComponent text = new TextComponent("[Click to stop travel]");
		text.setBold(true);
		text.setColor(ChatColor.RED);
		text.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/travel stop"));
		ComponentBuilder hoverText = new ComponentBuilder("/travel stop");
		hoverText.italic(true);
		hoverText.color(ChatColor.GRAY);
		text.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverText.create()));
		p.spigot().sendMessage(text);
		p.sendMessage("");

		return true;
	}

}
