package com.whistler.create_stellaris.block.coal_motor;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.contraptions.base.GeneratingKineticTileEntity;
import com.simibubi.create.foundation.tileEntity.TileEntityBehaviour;
import com.simibubi.create.foundation.tileEntity.behaviour.CenteredSideValueBoxTransform;
import com.simibubi.create.foundation.tileEntity.behaviour.scrollvalue.ScrollValueBehaviour;
import com.simibubi.create.foundation.utility.Lang;
import com.whistler.create_stellaris.block.ModBlocks;
import io.github.fabricators_of_create.porting_lib.transfer.item.ItemStackHandler;
import io.github.fabricators_of_create.porting_lib.transfer.item.ItemTransferable;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.base.CombinedStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CoalMotorTileEntity extends GeneratingKineticTileEntity implements ItemTransferable {
    public ItemStackHandler input;
    public Storage<ItemVariant> storage;
    public int timer = 0, unit_burning_time = 20 * 12;
    protected boolean working = false;
    public ScrollValueBehaviour generatedSpeed;
    private boolean cc_update_rpm = false;
    private int cc_new_rpm = 32;
    private int cc_antiSpam = 0;
    private boolean first = true;

    private static final Integer DEFAULT_SPEED = 32, RPM_RANGE = 256, STRESS = 2048;

    public CoalMotorTileEntity(BlockEntityType<? extends CoalMotorTileEntity> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);

        this.input = new ItemStackHandler(1);
        this.storage = new CoalMotorInventoryHandler();
        setLazyTickRate(20);
    }

    @Override
    public void tick() {
        super.tick();

        if(first) {
            updateGeneratedRotation();
            first = false;
        }

        if(cc_update_rpm && cc_antiSpam > 0) {
            generatedSpeed.setValue(cc_new_rpm);
            cc_update_rpm = false;
            cc_antiSpam--;
            updateGeneratedRotation();
        }

        assert level != null;
        if(level.isClientSide())
            return;

        work();
    }

    protected void work() {
        ItemStack source = input.getStackInSlot(0);

        if(!working) {
            if((source.is(Items.COAL) || source.is(Items.CHARCOAL))
                    && !getBlockState().getValue(CoalMotorBlock.WORKING)) {
                working = true;
                updateGeneratedRotation();
            }
        }
        else {
            if (!source.isEmpty()) {
                if (timer == 0)
                    source.shrink(1);

                if (timer < unit_burning_time)
                    timer++;

                if (timer > unit_burning_time)
                    timer = 0;
            }

            if (source.isEmpty() || getBlockState().getValue(CoalMotorBlock.WORKING)) {
                working = false;
                updateGeneratedRotation();
            }
        }
    }

    @Override
    public void addBehaviours(List<TileEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);

        CenteredSideValueBoxTransform slot =
                new CenteredSideValueBoxTransform((motor, side) -> motor.getValue(CoalMotorBlock.FACING) == side.getOpposite());

        generatedSpeed = new ScrollValueBehaviour(Lang.translateDirect("generic.speed"), this, slot);
        generatedSpeed.between(-RPM_RANGE,  RPM_RANGE);
        generatedSpeed.value = DEFAULT_SPEED;
        generatedSpeed.scrollableValue = DEFAULT_SPEED;
        generatedSpeed.withUnit(i -> Lang.translateDirect("generic.unit.rpm"));
        generatedSpeed.withCallback(this::updateGeneratedRotation);
        generatedSpeed.withStepFunction(CoalMotorTileEntity::step);

        behaviours.add(generatedSpeed);
    }

    public void updateGeneratedRotation(int i) {
        super.updateGeneratedRotation();
        cc_new_rpm = i;
    }

    @Override
    public void initialize() {
        super.initialize();
        if (!hasSource() || getGeneratedSpeed() > getTheoreticalSpeed())
            updateGeneratedRotation();
    }

    @Override
    public float getGeneratedSpeed() {
        if (!ModBlocks.COAL_MOTOR.has(getBlockState()))
            return 0;
        return convertToDirection(working ? generatedSpeed.getValue() : 0, getBlockState().getValue(CoalMotorBlock.FACING));
    }

    public static int step(ScrollValueBehaviour.StepContext context) {
        int current = context.currentValue;
        int step = 1;

        if (!context.shift) {
            int magnitude = Math.abs(current) - (context.forward == current > 0 ? 0 : 1);

            if (magnitude >= 4)
                step *= 4;
            if (magnitude >= 32)
                step *= 4;
            if (magnitude >= 128)
                step *= 4;
        }

        return step;
    }

    @Nullable
    @Override
    public Storage<ItemVariant> getItemStorage(@Nullable Direction face) {
        return this.storage;
    }

    @Override
    protected void write(CompoundTag compound, boolean clientPacket) {
        compound.putInt("Timer", this.timer);
        compound.put("Input", this.input.serializeNBT());
        compound.putBoolean("Working", this.working);

        super.write(compound, clientPacket);
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        this.timer = compound.getInt("Timer");
        this.input.deserializeNBT(compound.getCompound("Input"));
        this.working = compound.getBoolean("Working");

        super.read(compound, clientPacket);
    }

    private class CoalMotorInventoryHandler extends CombinedStorage<ItemVariant, ItemStackHandler> {
        public CoalMotorInventoryHandler() {
            super(List.of(input));
        }

        @Override
        public long insert(ItemVariant resource, long maxAmount, TransactionContext transaction) {
            if (input.getStackInSlot(0).is(ItemTags.LOGS_THAT_BURN)
                    && input.getStackInSlot(0).is(resource.getItem()))
                return super.insert(resource, maxAmount, transaction);
            return 0L;
        }
    }

    public float calculateAddedStressCapacity() {
        float capacity = STRESS / 256f;
        this.lastCapacityProvided = capacity;
        return capacity;
    }

    @Override
    protected Block getStressConfigKey() {
        return AllBlocks.WATER_WHEEL.get();
    }

    @Override
    public void lazyTick() {
        super.lazyTick();
        cc_antiSpam = 5;
    }

    public int getGeneratedStress() {
        return (int) calculateAddedStressCapacity();
    }

    public boolean setRPM(int rpm) {
        rpm = Math.max(Math.min(rpm, RPM_RANGE), -RPM_RANGE);
        cc_new_rpm = rpm;
        cc_update_rpm = true;
        return cc_antiSpam > 0;
    }

    public int getRPM() {
        return cc_new_rpm;
    }
}
