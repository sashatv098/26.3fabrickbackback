package com.example.simplebackpack;

import net.fabricmc.api.ModInitializer;

public class SimpleBackpack implements ModInitializer {
	public static final String MOD_ID = "simplebackpack";

	@Override
	public void onInitialize() {
		ModItems.initialize();
	}
}
