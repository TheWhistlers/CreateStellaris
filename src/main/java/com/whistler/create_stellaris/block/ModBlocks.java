package com.whistler.create_stellaris.block;

import com.simibubi.create.AllTags;
import com.simibubi.create.foundation.block.BlockStressDefaults;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.whistler.create_stellaris.CreateStellaris;
import com.whistler.create_stellaris.block.coal_motor.CoalMotorBlock;

import static com.simibubi.create.foundation.data.ModelGen.customItemModel;

public class ModBlocks {
    public static final BlockEntry<CoalMotorBlock> COAL_MOTOR = CreateStellaris.REGISTRATE
            .block("coal_motor", CoalMotorBlock::new)
            .initialProperties(SharedProperties::stone)
            .tag(AllTags.AllBlockTags.SAFE_NBT.tag) //Dono what this tag means (contraption safe?).
            .item()
            .transform(customItemModel())
            .register();

    public static void register() { }
}
