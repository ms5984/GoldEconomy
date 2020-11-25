package com.youtube.hempfest.goldeco.util.versions;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;

public class ComponentR1_16 {


		// *
		//
		// SEND TEXT THE PLAYER CAN ONLY HOVER OVER AND SEE MORE TEXT WITH
		//
	
	private static String color(String text) {
		return ChatColor.translateAlternateColorCodes('&', text);
	}
	
	public static TextComponent textHoverable(CommandSender sender, String normalText, String hoverText, String hoverTextMessage) {
		TextComponent text = new TextComponent(color(normalText));
		TextComponent hover = new TextComponent(color(hoverText));
		text.addExtra(hover);
		hover.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, (new Text(color(hoverTextMessage)))));
		return text;
	}
	
	public static TextComponent textHoverable(CommandSender sender, String normalText, String hoverText, String normalText2, String hoverTextMessage) {
		TextComponent text = new TextComponent(color(normalText));
		TextComponent hover = new TextComponent(color(hoverText));
		TextComponent text2 = new TextComponent(color(normalText2));
		text.addExtra(hover);
		text.addExtra(text2);
		hover.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, (new Text(color(hoverTextMessage))))); 
		return text;
	}
	
	public static TextComponent textHoverable(CommandSender sender, String normalText, String hoverText, String normalText2, String hoverText2, String hoverTextMessage, String hoverText2Message) {
		TextComponent text = new TextComponent(color(normalText));
		TextComponent hover = new TextComponent(color(hoverText));
		TextComponent text2 = new TextComponent(color(normalText2));
		TextComponent hover2 = new TextComponent(color(hoverText2));
		text.addExtra(hover);
		text.addExtra(text2);
		text.addExtra(hover2);
		hover.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, (new Text(color(hoverTextMessage)))));
		hover2.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, (new Text(color(hoverText2Message))))); 
		return text;
	}
		// *
		//
		// SEND TEXT THE PLAYER CAN BE SUGGESTED TO EXECUTE COMMANDS WITH
		//
	
	public static TextComponent textSuggestable(CommandSender sender, String normalText, String hoverText, String hoverTextMessage, String commandName) {
		TextComponent text = new TextComponent(color(normalText));
		TextComponent hover = new TextComponent(color(hoverText));
		text.addExtra(hover);
		hover.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, (new Text(color(hoverTextMessage)))));
		hover.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/" + commandName));
		return text;
	}
		// *
		//
		// SEND TEXT THE PLAYER CAN RUN COMMANDS WITH
		//
	
	public static TextComponent textRunnable(CommandSender sender, String normalText, String hoverText, String hoverTextMessage, String commandName) {
		TextComponent text = new TextComponent(color(normalText));
		TextComponent hover = new TextComponent(color(hoverText));
		text.addExtra(hover);
		hover.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, (new Text(color(hoverTextMessage)))));
		hover.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/" + commandName)); 
		return text;
	}
	
	public static TextComponent textRunnable(CommandSender sender, String normalText, String hoverText, String normalText2, String hoverTextMessage, String commandName) {
		TextComponent text = new TextComponent(color(normalText));
		TextComponent hover = new TextComponent(color(hoverText));
		TextComponent text2 = new TextComponent(color(normalText2));
		text.addExtra(hover);
		text.addExtra(text2);
		hover.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, (new Text(color(hoverTextMessage)))));
		hover.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/" + commandName)); 
		return text;
	}
	
	public static TextComponent textRunnable(CommandSender sender, String normalText, String hoverText, String normalText2, String hoverText2, String hoverTextMessage, String hoverText2Message, String commandName, String commandName2) {
		TextComponent text = new TextComponent(color(normalText));
		TextComponent hover = new TextComponent(color(hoverText));
		TextComponent text2 = new TextComponent(color(normalText2));
		TextComponent hover2 = new TextComponent(color(hoverText2));
		text.addExtra(hover);
		text.addExtra(text2);
		text.addExtra(hover2);
		hover.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, (new Text(color(hoverTextMessage)))));
		hover.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/" + commandName));
		hover2.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, (new Text(color(hoverText2Message)))));
		hover2.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/" + commandName2));
		return text;
	}
	
	public static TextComponent textRunnable(CommandSender sender, String hoverText, String hoverText2, String hoverTextBody3, String hoverTextMessage, String hoverText2Message, String hoverMessage3, String commandName, String commandName2, String commandName3) {
		TextComponent hover = new TextComponent(color(hoverText));
		TextComponent hover2 = new TextComponent(color(hoverText2));
		TextComponent hover3 = new TextComponent(color(hoverTextBody3));
		hover.addExtra(hover2);
		hover.addExtra(hover3);
		hover.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, (new Text(color(hoverTextMessage)))));
		hover.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/" + commandName));
		hover2.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, (new Text(color(hoverText2Message)))));
		hover2.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/" + commandName2));
		hover3.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, (new Text(color(hoverMessage3)))));
		hover3.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/" + commandName3));
		return hover;
	}
	
	

}
