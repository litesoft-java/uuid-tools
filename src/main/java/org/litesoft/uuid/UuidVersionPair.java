package org.litesoft.uuid;

import java.util.Objects;
import java.util.UUID;

import org.litesoft.annotations.NotNull;

public final class UuidVersionPair {
    public static UuidVersionPair of( UUID uuid, long version ) {
        return new UuidVersionPair( uuid, version );
    }

    private final UUID uuid;
    private final long version;

    private UuidVersionPair( UUID uuid, long version ) {
        this.uuid = NotNull.AssertArgument.namedValue( "uuid", uuid );
        this.version = version;
    }

    @NotNull
    public UUID getUuid() {
        return uuid;
    }

    public long getVersion() {
        return version;
    }

    @Override
    public String toString() {
        return "UuidVersionPair{" +
               "uuid=" + getUuid() +
               ", version=" + getVersion() +
               '}';
    }

    public boolean equals( UuidVersionPair them ) {
        return (this == them) ||
               (them != null
                && (this.version == them.version)
                && this.uuid.equals( them.uuid )
               );
    }

    @Override
    public int hashCode() {
        return Objects.hash( uuid, version );
    }

    @Override
    public boolean equals( Object them ) {
        return (this == them) ||
               (this.getClass() == them.getClass()
                && equals( (UuidVersionPair)them )
               );
    }
}
