package org.litesoft.uuid;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.litesoft.annotations.NotNull;

public final class UuidStringsPair {
    public static UuidStringsPair of( @NotNull UUID uuid, List<String> strings ) {
        return new UuidStringsPair( uuid,
                                    (strings == null) ? Collections.emptyList() :
                                    List.copyOf( strings ) );
    }

    public static UuidStringsPair of( @NotNull UUID uuid, String... strings ) {
        return new UuidStringsPair( uuid,
                                    (strings == null) ? Collections.emptyList() :
                                    Arrays.asList( strings ) );
    }

    private final UUID uuid;
    private final List<String> strings; // immutable

    private UuidStringsPair( UUID uuid, List<String> strings ) {
        this.uuid = NotNull.AssertArgument.namedValue( "uuid", uuid );
        this.strings = strings;
    }

    @NotNull
    public UUID getUuid() {
        return uuid;
    }

    @NotNull
    public List<String> getStrings() {
        return strings;
    }

    @Override
    public String toString() {
        return "UuidVersionPair{" +
               "uuid=" + getUuid() +
               ", strings=" + getStrings() +
               '}';
    }

    public boolean equals( UuidStringsPair them ) {
        return (this == them) ||
               (them != null
                && this.strings.equals( them.strings )
                && this.uuid.equals( them.uuid )
               );
    }

    @Override
    public int hashCode() {
        return Objects.hash( uuid );
    }

    @Override
    public boolean equals( Object them ) {
        return (this == them) ||
               (this.getClass() == them.getClass()
                && equals( (UuidStringsPair)them )
               );
    }
}
