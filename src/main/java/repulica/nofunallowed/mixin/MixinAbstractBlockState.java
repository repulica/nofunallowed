package repulica.nofunallowed.mixin;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import repulica.nofunallowed.AdventureHelper;
import repulica.nofunallowed.NoFunAllowed;

@Mixin(AbstractBlock.AbstractBlockState.class)
public abstract class MixinAbstractBlockState {

	@Shadow public abstract boolean isIn(TagKey<Block> tag);

	//todo: local/dimensional restrictions for block interaction
	private boolean canUse(LivingEntity user, World world, Vec3d pos, @Nullable String prefix) {
		if (isIn(NoFunAllowed.BLOCK_USAGE_NONE)) {
			if (user instanceof PlayerEntity player) {
				if (!player.canModifyBlocks()) return false;
			}
		}
		if (isIn(NoFunAllowed.BLOCK_USAGE_STRICT)) {
			if (!AdventureHelper.canUse(user, ((ItemStack)(Object) this), world, pos)) {
				return false;
			}
			if (prefix != null) {
				return AdventureHelper.canUse(user, prefix, ((ItemStack) (Object) this), world, pos);
			}
		} else {
			if (!AdventureHelper.canUseLoose(user, ((ItemStack)(Object) this), world, pos)) {
				return false;
			}
			if (prefix != null) {
				return AdventureHelper.canUseLoose(user, prefix, ((ItemStack) (Object) this), world, pos);
			}
		}
		return true;
	}

	@Inject(method = "onUse", at = @At("HEAD"), cancellable = true)
	private void injectOnUse(World world, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> info) {
		if (!player.canModifyBlocks() && isIn(NoFunAllowed.BLOCK_USAGE_NONE)) info.setReturnValue(ActionResult.PASS);
	}

	@Inject(method = "onBlockBreakStart", at = @At("HEAD"), cancellable = true)
	private void injectOnBlockBreakStart(World world, BlockPos pos, PlayerEntity player, CallbackInfo info) {
		if (!player.canModifyBlocks() && isIn(NoFunAllowed.BLOCK_USAGE_NONE)) info.cancel();;
	}
}
