package repulica.nofunallowed;

import net.fabricmc.api.ModInitializer;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class NoFunAllowed implements ModInitializer {
	public static final String MOD_ID = "nofunallowed";

	public static final TagKey<Item> ITEM_USAGE_IGNORE = TagKey.of(Registries.ITEM.getKey(), new Identifier(MOD_ID, "usage_ignore"));
	public static final TagKey<Item> ITEM_USAGE_STRICT = TagKey.of(Registries.ITEM.getKey(), new Identifier(MOD_ID, "usage_strict"));
	public static final TagKey<Item> ITEM_USAGE_NONE = TagKey.of(Registries.ITEM.getKey(), new Identifier(MOD_ID, "usage_none"));
	public static final TagKey<Item> ITEM_OBLITERATE = TagKey.of(Registries.ITEM.getKey(), new Identifier(MOD_ID, "obliterate"));
	//todo: can a strict mode even work for blocks they cant use nbt
	public static final TagKey<Block> BLOCK_USAGE_IGNORE = TagKey.of(Registries.BLOCK.getKey(), new Identifier(MOD_ID, "usage_ignore"));
	public static final TagKey<Block> BLOCK_USAGE_STRICT = TagKey.of(Registries.BLOCK.getKey(), new Identifier(MOD_ID, "usage_strict"));
	public static final TagKey<Block> BLOCK_USAGE_NONE = TagKey.of(Registries.BLOCK.getKey(), new Identifier(MOD_ID, "usage_none"));

	@Override
	public void onInitialize() {

	}
}
