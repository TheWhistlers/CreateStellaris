package com.whistler.create_stellaris.block.shaft_furnace;

import com.simibubi.create.AllTileEntities;

import com.simibubi.create.foundation.block.ITE;

import com.whistler.create_stellaris.block.ModBlockEntities;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class ShaftFurnaceBlock extends Block implements ITE<ShaftFurnaceTileEntity> {

	public ShaftFurnaceBlock(Properties properties) {
		super(properties);
	}

	@Override
	public Class<ShaftFurnaceTileEntity> getTileEntityClass() {
		return ShaftFurnaceTileEntity.class;
	}

	@Override
	public BlockEntityType<? extends ShaftFurnaceTileEntity> getTileEntityType() {
		return ModBlockEntities.SHAFT_FURNACE.get();
	}
}
