package kaptainwutax.featureutils.loot.function;

import kaptainwutax.featureutils.loot.LootContext;
import kaptainwutax.featureutils.loot.item.ItemStack;

public class EnchantRandomlyFunction implements LootFunction{

    @Override
    public ItemStack process(ItemStack baseStack, LootContext context) {
        context.advance(2);
        return baseStack;
    }
}
