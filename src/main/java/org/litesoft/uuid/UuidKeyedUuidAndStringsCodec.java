package org.litesoft.uuid;

import java.util.List;
import java.util.UUID;
import java.util.function.IntSupplier;

import org.litesoft.uuid.codecsupport.AbstractCodecUUID;
import org.litesoft.uuid.codecsupport.K3_DecodeSupport;
import org.litesoft.uuid.codecsupport.K3_EncodeSupport;

/**
 * Encode and Decode a UUID and String(s) (Secrets) which are Keyed to a passed in <code>keyUUID</code> (owner ID of the Secret) -- null strings are treated as empty strings.
 */
public class UuidKeyedUuidAndStringsCodec extends AbstractCodecUUID {
    public static final String[] NO_STRINGS_TO_ENCODE = new String[0];

    public UuidKeyedUuidAndStringsCodec( IntSupplier randomizingSeedSupplier ) {
        super( randomizingSeedSupplier, "K", 3 );
    }

    /**
     * Encode a UUID & optional String(s) (keyed to the <code>keyUUID</code>) -- null strings are treated as empty strings.
     *
     * @param keyUUID  nullable -- null triggers no encoding and a return of null.
     * @param uuid     nullable -- null triggers no encoding and a return of null.
     * @param toEncode strings to encode
     * @return null if either <code>UUID</code>) is null!
     */
    public String encode( UUID keyUUID, UUID uuid, List<String> toEncode ) {
        return encode( keyUUID, uuid,
                       (toEncode == null) ? NO_STRINGS_TO_ENCODE : toEncode.toArray( new String[0] ) );
    }

    /**
     * Encode a UUID & String(s) (keyed to the <code>keyUUID</code>) -- null strings are treated as empty strings.
     *
     * @param keyUUID  nullable -- null triggers no encoding and a return of null.
     * @param uuid     nullable -- null triggers no encoding and a return of null.
     * @param toEncode strings to encode
     * @return null if either <code>UUID</code>) is null, otherwise an encoding!
     */
    public String encode( UUID keyUUID, UUID uuid, String... toEncode ) {
        return ((keyUUID == null) || (uuid == null)) ? null :
               codecId + new K3_EncodeSupport( randomizingSeedSupplier, keyUUID, uuid,
                                               (toEncode == null) ? NO_STRINGS_TO_ENCODE : toEncode ).encode();
    }

    /**
     * Decode the output of the encode methods.
     * <p>
     * Note: assuming that the <code>UUID</code> is not null AND the <code>ukEncoded</code> value is not null or empty,
     * then leading and trailing spaces are removed from the <code>ukEncoded</code>, and if the result is empty, then
     * it is treated as insignificant -- which is an error!
     *
     * @param keyUUID   nullable -- null triggers no encoding and a return of null.
     * @param ukEncoded null and empty are treated as un-decode-able and trigger return of null, otherwise it is attempted to be decoded.
     * @return null or the decoded value
     * @throws IllegalArgumentException if the <code>ukEncoded</code> value is insignificant OR the encoding is not an
     *                                  output of the encode methods -- including if it is determined that it is NOT keyed to the passed in UUID!
     */
    public UuidStringsPair decode( UUID keyUUID, String ukEncoded ) {
        if ( (keyUUID == null) || (ukEncoded == null) ) {
            return null;
        }
        K3_DecodeSupport.Pair pair = new K3_DecodeSupport( keyUUID, validateToDecode( ukEncoded ) ).decode();
        return UuidStringsPair.of( pair.uuid(), pair.strings() );
    }
}
