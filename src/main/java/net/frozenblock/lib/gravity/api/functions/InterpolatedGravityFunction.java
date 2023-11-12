package net.frozenblock.lib.gravity.api.functions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.frozenblock.lib.gravity.api.GravityBelt;
import net.frozenblock.lib.gravity.api.SerializableGravityFunction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

@ApiStatus.Experimental
@ApiStatus.Internal
public record InterpolatedGravityFunction(double gravity, double minLerpGravity, double maxLerpGravity, double minLerpY, double maxLerpY) implements SerializableGravityFunction<InterpolatedGravityFunction> {

	public static final Codec<InterpolatedGravityFunction> CODEC = RecordCodecBuilder.create(instance ->
		instance.group(
			Codec.DOUBLE.fieldOf("gravity").forGetter(InterpolatedGravityFunction::gravity),
			Codec.DOUBLE.fieldOf("minLerpGravity").forGetter(InterpolatedGravityFunction::minLerpGravity),
			Codec.DOUBLE.fieldOf("maxLerpGravity").forGetter(InterpolatedGravityFunction::maxLerpY),
			Codec.DOUBLE.fieldOf("minLerpY").forGetter(InterpolatedGravityFunction::minLerpY),
			Codec.DOUBLE.fieldOf("maxLerpY").forGetter(InterpolatedGravityFunction::maxLerpY)
		).apply(instance, InterpolatedGravityFunction::new)
	);

	public static final Codec<GravityBelt<InterpolatedGravityFunction>> BELT_CODEC = GravityBelt.codec(CODEC);

	@Override
	public double get(@Nullable Entity entity, double y, double minY, double maxY) {
		if (!(entity instanceof Player)) return 1.0;
		double normalizedY = (y - minLerpY) / (maxLerpY - minLerpY);

		if (normalizedY < 0) return minLerpGravity;
		if (normalizedY < 0.5) return Mth.lerp(normalizedY, minLerpGravity, gravity);
		if (normalizedY < 1.0) return Mth.lerp(normalizedY, gravity, maxLerpGravity);
		return maxLerpGravity;
	}

	@Override
	public Codec<InterpolatedGravityFunction> codec() {
		return CODEC;
	}
}
