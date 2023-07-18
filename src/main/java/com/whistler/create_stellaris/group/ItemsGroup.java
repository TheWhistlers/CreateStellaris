package com.whistler.create_stellaris.group;

import com.whistler.create_stellaris.CreateStellaris;
import com.whistler.create_stellaris.item.ModItems;
import io.github.fabricators_of_create.porting_lib.util.LazyItemGroup;
import net.minecraft.world.item.ItemStack;

public class ItemsGroup extends LazyItemGroup {
    public static ItemsGroup MAIN;

    public ItemsGroup(String name) {
        super(CreateStellaris.MOD_ID + ":" + name);
        MAIN = this;
    }

    @Override
    public ItemStack makeIcon() {
        return new ItemStack(ModItems.BRASS_ENGINE.get());
    }
}
