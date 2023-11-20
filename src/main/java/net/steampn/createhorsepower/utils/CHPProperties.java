package net.steampn.createhorsepower.utils;

import net.minecraft.world.level.block.state.properties.BooleanProperty;

public class CHPProperties {
    public static final BooleanProperty WORKER_SMALL_STATE = BooleanProperty.create("worker_small");
    public static final BooleanProperty WORKER_MEDIUM_STATE = BooleanProperty.create("worker_medium");
    public static final BooleanProperty WORKER_LARGE_STATE = BooleanProperty.create("worker_large");

    private CHPProperties(){}
}