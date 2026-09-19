package com.brass_amber.ba_bt.registries;

import com.mojang.serialization.MapCodec;

public interface LocalModificationType<S extends LocalModification> {
    MapCodec<S> codec();
}
