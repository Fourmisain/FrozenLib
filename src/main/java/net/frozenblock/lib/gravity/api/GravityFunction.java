/*
 * Copyright 2023 FrozenBlock
 * This file is part of FrozenLib.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, see <https://www.gnu.org/licenses/>.
 */

package net.frozenblock.lib.gravity.api;

import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;

@FunctionalInterface
public interface GravityFunction {
	/***
	 * @param entity The optional entity being tracked
	 * @param y The current y position
	 * @param minY The minimum Y position of the gravity belt
	 * @param maxY The maximum Y position of the gravity belt
	 * @return The gravity value
	 */
	double get(@Nullable Entity entity, double y, double minY, double maxY);
}
