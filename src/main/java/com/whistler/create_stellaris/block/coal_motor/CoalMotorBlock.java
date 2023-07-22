package com.whistler.create_stellaris.block.coal_motor;

import com.simibubi.create.content.contraptions.base.DirectionalKineticBlock;
import com.simibubi.create.foundation.block.ITE;
import com.simibubi.create.foundation.utility.VoxelShaper;
import com.whistler.create_stellaris.CreateStellaris;
import com.whistler.create_stellaris.block.ModBlockEntities;
import com.whistler.create_stellaris.shape.CSShape;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings({"deprecation"})
public class CoalMotorBlock extends DirectionalKineticBlock implements ITE<CoalMotorTileEntity> {
    public static final VoxelShaper COAL_MOTOR_SHAPE =
            CSShape.shape(3, 2, 3, 13, 15, 13)
                    .add(0, 0, 0, 16, 5, 16).forDirectional();

    public CoalMotorBlock(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult use(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hit) {
        if (level.isClientSide)
            return InteractionResult.SUCCESS;

        withTileEntityDo(level, pos, te -> {
            if (!player.getItemInHand(hand).isEmpty()
                    && te.input.getStackInSlot(0).isEmpty()) {
                te.input.setStackInSlot(0, player.getItemInHand(hand));
                player.setItemInHand(hand, ItemStack.EMPTY);
                CreateStellaris.LOGGER.info(te.input.getStackInSlot(0).getItem().getRegistryName().getPath());
            } else if (player.getItemInHand(hand).isEmpty()) {
                player.setItemInHand(hand, te.input.getStackInSlot(0));
                te.input.setStackInSlot(0, ItemStack.EMPTY);
            }
        });

        return InteractionResult.SUCCESS;
    }

    @Override
    public VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter worldIn, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return COAL_MOTOR_SHAPE.get(state.getValue(FACING));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return state.getValue(FACING).getAxis();
    }

    @Override
    public Class<CoalMotorTileEntity> getTileEntityClass() {
        return CoalMotorTileEntity.class;
    }

    @Override
    public BlockEntityType<? extends CoalMotorTileEntity> getTileEntityType() {
        return ModBlockEntities.COAL_MOTOR.get();
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return ModBlockEntities.COAL_MOTOR.create(pos, state);
    }

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return (face == state.getValue(FACING));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction preferred = getPreferredFacing(context);

        if ((context.getPlayer() != null && context.getPlayer().isShiftKeyDown()) || preferred == null)
            return super.getStateForPlacement(context);

        return defaultBlockState().setValue(FACING, preferred);
    }

    @Override
    public boolean hideStressImpact() {
        return true;
    }
}
