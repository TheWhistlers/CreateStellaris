package com.whistler.create_stellaris.block;

import com.simibubi.create.content.contraptions.base.HalfShaftInstance;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.whistler.create_stellaris.CreateStellaris;
import com.whistler.create_stellaris.block.coal_motor.CoalMotorTileEntity;
import com.whistler.create_stellaris.block.coal_motor.CoalMotorRenderer;
import com.whistler.create_stellaris.block.shaft_furnace.ShaftFurnaceTileEntity;

public class ModBlockEntities {
    public static final BlockEntityEntry<CoalMotorTileEntity> COAL_MOTOR = CreateStellaris.REGISTRATE
            .tileEntity("coal_motor", CoalMotorTileEntity::new)
            .instance(() -> HalfShaftInstance::new)
            .validBlocks(ModBlocks.COAL_MOTOR)
            .renderer(() -> CoalMotorRenderer::new)
            .register();

	public static final BlockEntityEntry<ShaftFurnaceTileEntity> SHAFT_FURNACE = CreateStellaris.REGISTRATE
			.tileEntity("shaft_furnace", ShaftFurnaceTileEntity::new)
			.validBlocks(ModBlocks.SHAFT_FURNACE)
			.register();

    public static void register() { }
}
