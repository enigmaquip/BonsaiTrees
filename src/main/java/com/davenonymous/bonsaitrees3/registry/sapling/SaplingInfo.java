package com.davenonymous.bonsaitrees3.registry.sapling;

import com.davenonymous.libnonymous.base.RecipeData;
import com.davenonymous.bonsaitrees3.setup.Registration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.*;

public class SaplingInfo extends RecipeData {
	private final ResourceLocation id;

	public Ingredient ingredient;
	public int baseTicks;

	public ArrayList<SaplingDrop> drops;
	public Set<String> tags;

	public SaplingInfo(ResourceLocation id, Ingredient ingredient, int baseTicks) {
		this.id = id;
		this.ingredient = ingredient;
		this.baseTicks = baseTicks;
		this.drops = new ArrayList<>();
		this.tags = new HashSet<>();
	}

	@Override
	public ResourceLocation getId() {
		return id;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return Registration.SAPLING_SERIALIZER.get();
	}

	@Override
	public RecipeType<?> getType() {
		return Registration.RECIPE_TYPE_SAPLING;
	}

	public int getRequiredTicks() {
		return baseTicks;
	}

	public void addDrop(SaplingDrop drop) {
		this.drops.add(drop);
		this.drops.sort((a, b) -> (int) (b.chance * 1000) - (int) (a.chance * 1000));
	}

	public void addTag(String tag) {
		this.tags.add(tag);
	}

	public boolean isValidTag(String tag) {
		return this.tags.contains(tag);
	}

	public List<ItemStack> getRandomizedDrops(Random rand, int fortune, boolean hasSilkTouch, boolean hasBeeHive) {
		ArrayList<ItemStack> result = new ArrayList<>();
		for(SaplingDrop drop : this.drops) {
			if(drop.requiresSilkTouch && !hasSilkTouch) {
				continue;
			}

			if(drop.requiresBees && !hasBeeHive) {
				continue;
			}

			ItemStack dropStack = drop.getRandomDrop(rand, fortune);
			if(dropStack.isEmpty()) {
				continue;
			}

			result.add(dropStack);
		}

		return result;
	}

}