package org.litesoft.uuid;

import java.util.UUID;
import java.util.function.IntSupplier;

import org.litesoft.uuid.codecsupport.AbstractCodecUUID;
import org.litesoft.uuid.codecsupport.V1_DecodeSupport;
import org.litesoft.uuid.codecsupport.V1_EncodeSupport;

/**
 * Encode and Decode a UUID and Long (Version)
 */
public class UuidVersionCodec extends AbstractCodecUUID {
    public UuidVersionCodec( IntSupplier randomizingSeedSupplier ) {
        super( randomizingSeedSupplier, "V", 1 );
    }

    /**
     * Encode the pair (UUID & Version)
     *
     * @param pair - nullable
     * @return null if pair is null, otherwise an encoding!
     */
    public String encode( UuidVersionPair pair ) {
        return (pair == null) ? null : encode( pair.getUuid(), pair.getVersion() );
    }

    /**
     * Encode the UUID & Version (pair)
     *
     * @param uuid    - nullable
     * @param version all long values can be encoded
     * @return null if UUID is null, otherwise an encoding!
     */
    public String encode( UUID uuid, long version ) {
        return (uuid == null) ? null :
               codecId + new V1_EncodeSupport( randomizingSeedSupplier, uuid, version ).encode();
    }

    /**
     * Decode the output of the encode methods.
     * <p>
     * Note: assuming that the <code>cvEncoded</code> value is not null or empty, then leading and trailing spaces are removed,
     * and if the result is empty, then it is treated as insignificant -- which is an error!
     *
     * @param cvEncoded null and empty are treated as un-decode-able and return null, otherwise it is attempted to be decoded.
     * @return null or a UuidVersionPair (UUID & Version)
     * @throws IllegalArgumentException if the <code>cvEncoded</code> value is insignificant OR the encoding is not an output of the encode methods!
     */
    public UuidVersionPair decode( String cvEncoded ) {
        return ((cvEncoded == null) || cvEncoded.isEmpty()) ? null :
               map( new V1_DecodeSupport( validateToDecode( cvEncoded ) ).decode() );
    }

    private static UuidVersionPair map( V1_DecodeSupport.Pair pair ) {
        return (pair == null) ? null : UuidVersionPair.of( pair.uuid(), pair.version() );
    }
}
