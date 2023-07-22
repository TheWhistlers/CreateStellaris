package com.whistler.create_stellaris.item;

import com.tterrag.registrate.util.entry.ItemEntry;
import com.whistler.create_stellaris.CreateStellaris;
import com.whistler.create_stellaris.group.ItemsGroup;
import net.minecraft.world.item.Item;

public class ModItems {
    static {
        CreateStellaris.REGISTRATE.creativeModeTab(() -> ItemsGroup.MAIN);
    }

    public static final ItemEntry<Item> BRASS_ENGINE =
            CreateStellaris.REGISTRATE.item("brass_engine", Item::new).register();

	public static final ItemEntry<Item> CARBON_STEEL =
			CreateStellaris.REGISTRATE.item("carbon_steel", Item::new).register();

	public static void register() { }
}
