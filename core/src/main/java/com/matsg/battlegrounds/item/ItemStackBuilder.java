package com.matsg.battlegrounds.item;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.Arrays;
import java.util.List;

public class ItemStackBuilder {

	private ItemStack item;
	private ItemMeta meta;
	
	public ItemStackBuilder(ItemStack item) {
		this.item = item;
		this.meta = item.getItemMeta();
	}
	
	public ItemStackBuilder(Material material) {
		this.item = new ItemStack(material);
		this.meta = item.getItemMeta();
	}
	
	public ItemStackBuilder addEnchantment(Enchantment enchantment, int level) {
		item.addUnsafeEnchantment(enchantment, level);
		return this;
	}
	
	public ItemStackBuilder addEnchantments(Enchantment[] enchantments, int level) {
		for (Enchantment enchantment : enchantments) {
			item.addUnsafeEnchantment(enchantment, level);
		}
		return this;
	}
	
	public ItemStackBuilder addItemFlags(ItemFlag... flags) {
		meta.addItemFlags(flags);
		return this;
 	}
	
	public ItemStack build() {
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStackBuilder setAmount(int amount) {
		item.setAmount(amount);
		return this;
	}

	public ItemStackBuilder setColor(Color color) {
		LeatherArmorMeta leatherMeta = (LeatherArmorMeta) meta;
		leatherMeta.setColor(color);
		meta = leatherMeta;
		return this;
	}
	
	public ItemStackBuilder setDisplayName(String displayName) {
		meta.setDisplayName(displayName);
		return this;
	}

	public ItemStackBuilder setDurability(short durability) {
		item.setDurability(durability);
		return this;
	}
	
	public ItemStackBuilder setLore(String... lore) {
		List<String> list = Arrays.asList(lore);
		meta.setLore(list);
		return this;
	}

	public ItemStackBuilder setLore(List<String> lore) {
		meta.setLore(lore);
		return this;
	}

	public ItemStackBuilder setUnbreakable(boolean unbreakable) {
		try {
			meta.setUnbreakable(unbreakable);
		} catch (NoSuchMethodError e) {
			meta.spigot().setUnbreakable(unbreakable);
		}
		return this;
	}
}
