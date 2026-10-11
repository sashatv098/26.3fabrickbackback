package com.example.simplebackpack;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class BackpackItem extends Item {
	public BackpackItem(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult use(Level level, Player player, InteractionHand hand) {
		if (level.isClientSide()) {
			return InteractionResult.SUCCESS;
		}

		ItemStack stack = player.getItemInHand(hand);
		player.openMenu(new SimpleMenuProvider(
				(containerId, inventory, p) -> new BackpackMenu(containerId, inventory, stack),
				Component.translatable("container.simplebackpack.backpack")));
		return InteractionResult.SUCCESS;
	}
}
