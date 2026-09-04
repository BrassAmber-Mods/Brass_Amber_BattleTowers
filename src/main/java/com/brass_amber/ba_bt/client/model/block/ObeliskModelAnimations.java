package com.brass_amber.ba_bt.client.model.block;
// Save this class in your mod and generate all required imports

import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.animation.KeyframeAnimations;

/**
 * Made with Blockbench 5.1.6
 * Exported for Minecraft version 1.19 or later with Mojang mappings
 * @author Author
 */
public class ObeliskModelAnimations {
	public static final AnimationDefinition powered = AnimationDefinition.Builder.withLength(16.0F).looping()
		.addAnimation("stone", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 137.766F, 0.0F), AnimationChannel.Interpolations.LINEAR)
		))
		.build();
}