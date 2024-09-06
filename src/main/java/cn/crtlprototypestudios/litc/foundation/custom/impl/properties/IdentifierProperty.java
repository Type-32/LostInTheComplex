package cn.crtlprototypestudios.litc.foundation.custom.impl.properties;

import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Codec;
import net.minecraft.state.property.Property;
import net.minecraft.util.Identifier;

import java.util.Collection;
import java.util.Optional;

public class IdentifierProperty extends Property<Identifier> {
    private final ImmutableSet<Identifier> values = ImmutableSet.of();

    protected IdentifierProperty(String name) {
        super(name, Identifier.class);
    }

    @Override
    public Collection<Identifier> getValues() {
        return values;
    }

    @Override
    public String name(Identifier value) {
        return value.toString();
    }

    @Override
    public Optional<Identifier> parse(String name) {
        return Optional.of(Identifier.of(name));
    }

    public static IdentifierProperty of(String name) {
        return new IdentifierProperty(name);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else {
            if (object instanceof IdentifierProperty idp && super.equals(object)) {
                return this.values.equals(idp.values);
            }

            return false;
        }
    }

    @Override
    public Codec<Identifier> getCodec() {
        return Identifier.CODEC;
    }
}
