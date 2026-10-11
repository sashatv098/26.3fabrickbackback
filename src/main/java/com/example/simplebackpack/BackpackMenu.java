package com.example.simplebackpack;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;

/**
 * Серверное меню рюкзака. Использует ванильный тип меню 9x3,
 * поэтому клиенту не нужен свой экран: рисуется обычный интерфейс сундука.
 * Содержимое хранится в компоненте DataComponents.CONTAINER самого предмета.
 */
public class BackpackMenu extends AbstractContainerMenu {
	public static final int ROWS = 3;
	public static final int SIZE = ROWS * 9;

	private final ItemStack backpack;
	private final SimpleContainer container = new SimpleContainer(SIZE);

	public BackpackMenu(int containerId, Inventory playerInventory, ItemStack backpack) {
		super(MenuType.GENERIC_9x3, containerId);
		this.backpack = backpack;

		// Загружаем содержимое из предмета
		NonNullList<ItemStack> items = NonNullList.withSize(SIZE, ItemStack.EMPTY);
		backpack.getOrDefault(DataComponents.CONTAINER, ItemContainerContents.EMPTY).copyInto(items);
		for (int i = 0; i < SIZE; i++) {
			container.setItem(i, items.get(i));
		}
		// Сохраняем при любом изменении (слушатель ставим после загрузки)
		container.addListener(c -> save());

		// Слоты рюкзака (координаты как у ванильного сундука)
		for (int row = 0; row < ROWS; row++) {
			for (int col = 0; col < 9; col++) {
				this.addSlot(new BackpackSlot(container, col + row * 9, 8 + col * 18, 18 + row * 18));
			}
		}

		// Инвентарь игрока
		int yOffset = (ROWS - 4) * 18;
		for (int row = 0; row < 3; row++) {
			for (int col = 0; col < 9; col++) {
				this.addSlot(new LockedSlot(playerInventory, col + row * 9 + 9, 8 + col * 18, 103 + row * 18 + yOffset));
			}
		}
		// Хотбар
		for (int col = 0; col < 9; col++) {
			this.addSlot(new LockedSlot(playerInventory, col, 8 + col * 18, 161 + yOffset));
		}
	}

	private void save() {
		List<ItemStack> list = new ArrayList<>(SIZE);
		for (int i = 0; i < SIZE; i++) {
			list.add(container.getItem(i).copy());
		}
		backpack.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(list));
	}

	@Override
	public boolean stillValid(Player player) {
		Inventory inv = player.getInventory();
		for (int i = 0; i < inv.getContainerSize(); i++) {
			if (inv.getItem(i) == backpack) {
				return true;
			}
		}
		return false;
	}

	@Override
	public ItemStack quickMoveStack(Player player, int index) {
		ItemStack result = ItemStack.EMPTY;
		Slot slot = this.slots.get(index);
		if (slot.hasItem()) {
			ItemStack stack = slot.getItem();
			result = stack.copy();
			if (index < SIZE) {
				if (!this.moveItemStackTo(stack, SIZE, this.slots.size(), true)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.moveItemStackTo(stack, 0, SIZE, false)) {
				return ItemStack.EMPTY;
			}

			if (stack.isEmpty()) {
				slot.setByPlayer(ItemStack.EMPTY);
			} else {
				slot.setChanged();
			}
		}
		return result;
	}

	/** Слот рюкзака: нельзя положить рюкзак в рюкзак. */
	private static class BackpackSlot extends Slot {
		BackpackSlot(Container container, int index, int x, int y) {
			super(container, index, x, y);
		}

		@Override
		public boolean mayPlace(ItemStack stack) {
			return !(stack.getItem() instanceof BackpackItem);
		}
	}

	/** Слот игрока: нельзя вытащить открытый рюкзак, пока он открыт. */
	private class LockedSlot extends Slot {
		LockedSlot(Container container, int index, int x, int y) {
			super(container, index, x, y);
		}

		@Override
		public boolean mayPickup(Player player) {
			return this.getItem() != backpack;
		}
	}
}
