package repulica.nofunallowed.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import repulica.nofunallowed.AdventureHelper;
import repulica.nofunallowed.NoFunAllowed;

@Mixin(ItemStack.class)
public abstract class MixinItemStack {

	@Shadow public abstract boolean isIn(TagKey<Item> tag);

	private boolean canUse(LivingEntity user, World world, Vec3d pos, @Nullable String prefix) {
		if (isIn(NoFunAllowed.ITEM_USAGE_IGNORE)) return true;
		if (isIn(NoFunAllowed.ITEM_USAGE_NONE)) {
			if (user instanceof PlayerEntity player) {
				if (!player.canModifyBlocks()) return false;
			}
		}
		if (isIn(NoFunAllowed.ITEM_USAGE_STRICT)) {
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

	@Inject(method = "use", at = @At("HEAD"), cancellable = true)
	private void injectUse(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> info) {
		//todo: grab from user/hand instead?
		if (!canUse(user, world, user.getPos(), "InAir")) {
			info.setReturnValue(TypedActionResult.pass(((ItemStack) (Object) this)));
		}
	}

	@Inject(method = "useOnBlock", at = @At("HEAD"), cancellable = true)
	private void injectUseOnBlock(ItemUsageContext context, CallbackInfoReturnable<ActionResult> info) {
		BlockPos bPos = context.getBlockPos();
		Vec3d pos = new Vec3d(bPos.getX() + 0.5, bPos.getY() + 0.5, bPos.getZ() + 0.5);
		if (!canUse(context.getPlayer(), context.getWorld(), pos, "OnBlock")) {
			info.setReturnValue(ActionResult.PASS);
		}
	}

	@Inject(method = "useOnEntity", at = @At("HEAD"), cancellable = true)
	private void injectUseOnEntity(PlayerEntity user, LivingEntity entity, Hand hand, CallbackInfoReturnable<ActionResult> info) {
		if (!canUse(user, user.getWorld(), user.getPos(), "OnEntity")) {
			info.setReturnValue(ActionResult.PASS);
		}
	}
}
