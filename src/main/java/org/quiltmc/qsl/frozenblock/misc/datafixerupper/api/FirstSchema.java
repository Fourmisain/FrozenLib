/*
 * Copyright 2022 QuiltMC
 * Copyright 2022 FrozenBlock
 * Modified to work on Fabric
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.quiltmc.qsl.frozenblock.misc.datafixerupper.api;

import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.templates.TypeTemplate;
import java.util.Map;
import java.util.function.Supplier;
import org.jetbrains.annotations.Range;

/**
 * Represents a {@link Schema} that has no parent.
 * <p>
 * Modified to work on Fabric
 */
public class FirstSchema extends Schema {
	/**
	 * Creates a schema.
	 *
	 * @param versionKey the data version key
	 */
	public FirstSchema(@Range(from = 0, to = Integer.MAX_VALUE) int versionKey) {
		super(versionKey, null);
	}

	// all of these methods refer to this.parent without checking if its null
	@Override
	public void registerTypes(Schema schema, Map<String, Supplier<TypeTemplate>> entityTypes,
							  Map<String, Supplier<TypeTemplate>> blockEntityTypes) {
	}

	@Override
	public Map<String, Supplier<TypeTemplate>> registerEntities(Schema schema) {
		return Map.of();
	}

	@Override
	public Map<String, Supplier<TypeTemplate>> registerBlockEntities(Schema schema) {
		return Map.of();
	}
}
