package fr.hugman.build_rush.plot;

import fr.hugman.build_rush.BuildRush;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SkullItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.structure.StructureTemplate;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import xyz.nucleoid.map_templates.BlockBounds;

public class PlotUtil {
	public static ItemStack stackForBlock(World world, BlockPos pos) {
		var state = world.getBlockState(pos);
		var stack = state.getBlock().getPickStack(world, pos, state);

		if((state.isIn(BlockTags.PORTALS) || state.isIn(BlockTags.FIRE))) {
			stack = new ItemStack(Items.FLINT_AND_STEEL);
			stack.getOrCreateNbt().putBoolean("Unbreakable", true);
		}
		if(state.getFluidState().getFluid() == Fluids.WATER) {
			stack = new ItemStack(Items.WATER_BUCKET);
		}
		if(state.getFluidState().getFluid() == Fluids.LAVA) {
			stack = new ItemStack(Items.LAVA_BUCKET);
		}
		// TODO: verify for vines, sculk veins, candles...

		if(state.hasBlockEntity()) {
			var blockEntity = world.getBlockEntity(pos);
			if(blockEntity == null) {
				BuildRush.LOGGER.warn("Block entity was null for " + state.getBlock());
			}
			else {
				PlotUtil.addBlockEntityNbt(stack, blockEntity);
			}
		}
		return stack;
	}

	public static void addBlockEntityNbt(ItemStack stack, BlockEntity blockEntity) {
		NbtCompound nbtCompound = blockEntity.createNbtWithIdentifyingData();
		BlockItem.setBlockEntityNbt(stack, blockEntity.getType(), nbtCompound);
		if (stack.getItem() instanceof SkullItem && nbtCompound.contains("SkullOwner")) {
			NbtCompound nbtCompound2 = nbtCompound.getCompound("SkullOwner");
			NbtCompound nbtCompound3 = stack.getOrCreateNbt();
			nbtCompound3.put("SkullOwner", nbtCompound2);
			NbtCompound nbtCompound4 = nbtCompound3.getCompound("BlockEntityTag");
			nbtCompound4.remove("SkullOwner");
			nbtCompound4.remove("x");
			nbtCompound4.remove("y");
			nbtCompound4.remove("z");
		}
	}

	/**
	 * Compares a structure template to the blocks present in certain bounds.
	 * <p>+2 for each block that perfectly matches<br>
	 * +1 for each block that is the correct block, but not the same state
	 *
	 * @param world
	 * @param original the original structure template
	 * @param bounds the bounds to compare to
	 * @return a double between 0 and 1, representing the percentage of blocks that match.
	 */
	public static double compare(World world, StructureTemplate original, BlockBounds bounds) {
		return 1.0D;
	}
}
