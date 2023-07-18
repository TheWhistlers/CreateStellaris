package com.whistler.create_stellaris;

import com.simibubi.create.Create;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.whistler.create_stellaris.block.ModBlockEntities;
import com.whistler.create_stellaris.block.ModBlocks;
import com.whistler.create_stellaris.group.ItemsGroup;
import com.whistler.create_stellaris.item.ModItems;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreateStellaris implements ModInitializer {
	public static final String MOD_ID = "create_stellaris";
	public static final String CREATE_VERSION = Create.VERSION;
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(MOD_ID);

	@Override
	public void onInitialize() {
		new ItemsGroup("main");

		ModItems.register();
		ModBlocks.register();
		ModBlockEntities.register();

		REGISTRATE.register();
	}
}