package com.matsg.battlegrounds.item;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ItemStackBuilderTest {

    private ItemMeta itemMeta;
    private ItemStack itemStack;

    @Before
    public void setUp() {
        this.itemMeta = mock(ItemMeta.class);
        this.itemStack = mock(ItemStack.class);

        when(itemStack.getItemMeta()).thenReturn(itemMeta);
    }

    @Test
    public void testItemStackBuild() {
        ItemStackBuilder builder = new ItemStackBuilder(itemStack);
        ItemStack itemStack = builder.build();

        assertEquals(this.itemStack, itemStack);
        assertEquals(this.itemMeta, itemStack.getItemMeta());
    }

    @Test
    public void testAddEnchantment() {
        Enchantment enchantment = Enchantment.ARROW_DAMAGE;
        int level = 1;

        ItemStackBuilder builder = new ItemStackBuilder(itemStack).addEnchantment(enchantment, level);
        ItemStack itemStack = builder.build();

        verify(itemStack, times(1)).addUnsafeEnchantment(enchantment, level);
    }

    @Test
    public void testAddEnchantments() {
        Enchantment[] enchantments = new Enchantment[] { Enchantment.ARROW_DAMAGE, Enchantment.ARROW_FIRE };
        int level = 1;

        ItemStackBuilder builder = new ItemStackBuilder(itemStack).addEnchantments(enchantments, level);
        ItemStack itemStack = builder.build();

        verify(itemStack, times(2)).addUnsafeEnchantment(any(Enchantment.class), anyInt());
    }

    @Test
    public void testAddItemFlags() {
        ItemFlag flag = ItemFlag.HIDE_ATTRIBUTES;

        ItemStackBuilder builder = new ItemStackBuilder(itemStack).addItemFlags(flag);
        builder.build();

        verify(itemMeta, times(1)).addItemFlags(flag);
    }

    @Test
    public void testSetAmount() {
        int amount = 1;

        ItemStackBuilder builder = new ItemStackBuilder(itemStack).setAmount(amount);
        ItemStack itemStack = builder.build();

        verify(itemStack, times(1)).setAmount(amount);
    }

    @Test(expected = ClassCastException.class)
    public void testSetColorWithNormalItemMeta() {
        Color color = Color.RED;

        new ItemStackBuilder(itemStack).setColor(color);
    }

    @Test
    public void testSetColorWithLeatherArmorMeta() {
        LeatherArmorMeta itemMeta = mock(LeatherArmorMeta.class);

        when(itemStack.getItemMeta()).thenReturn(itemMeta);

        Color color = Color.RED;

        ItemStackBuilder builder = new ItemStackBuilder(itemStack).setColor(color);
        builder.build();

        verify(itemMeta, times(1)).setColor(color);
    }

    @Test
    public void testSetDisplayName() {
        String displayName = "Test";

        ItemStackBuilder builder = new ItemStackBuilder(itemStack).setDisplayName(displayName);
        builder.build();

        verify(itemMeta, times(1)).setDisplayName(displayName);
    }

    @Test
    public void testSetDurability() {
        short durability = (short) 1;

        ItemStackBuilder builder = new ItemStackBuilder(itemStack).setDurability(durability);
        ItemStack itemStack = builder.build();

        verify(itemStack, times(1)).setDurability(durability);
    }

    @Test
    public void testSetLore() {
        String[] lore = new String[] { "Test", "Test" };
        List<String> loreList = Arrays.asList(lore);

        ItemStackBuilder builder = new ItemStackBuilder(itemStack).setLore(lore);
        builder.build();

        verify(itemMeta, times(1)).setLore(loreList);
    }
}
