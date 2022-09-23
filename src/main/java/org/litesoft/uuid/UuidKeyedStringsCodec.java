package org.litesoft.uuid;

import java.util.List;
import java.util.UUID;
import java.util.function.IntSupplier;

import org.litesoft.uuid.codecsupport.AbstractCodecUUID;
import org.litesoft.uuid.codecsupport.K2_DecodeSupport;
import org.litesoft.uuid.codecsupport.K2_EncodeSupport;

/**
 * Encode and Decode String(s) (Secrets) which are Keyed to a passed in UUID (owner ID of the Secret) -- null strings are treated as empty strings.
 */
public class UuidKeyedStringsCodec extends AbstractCodecUUID {
    public UuidKeyedStringsCodec( IntSupplier randomizingSeedSupplier ) {
        super( randomizingSeedSupplier, "K", 2 );
    }

    /**
     * Encode String(s) (keyed to the <code>keyUUID</code>) -- null strings are treated as empty strings.
     *
     * @param keyUUID  nullable
     * @param toEncode strings to encode
     * @return null if <code>keyUUID</code> is null or there are no strings to encode, otherwise an encoding!
     */
    public String encode( UUID keyUUID, List<String> toEncode ) {
        return ((keyUUID == null) || (toEncode == null) || toEncode.isEmpty()) ? null :
               codecId + new K2_EncodeSupport( randomizingSeedSupplier, keyUUID, toEncode.toArray( new String[0] ) ).encode();
    }

    /**
     * Encode String(s) (keyed to the <code>keyUUID</code>) -- null strings are treated as empty strings.
     *
     * @param keyUUID  nullable
     * @param toEncode strings to encode
     * @return null if <code>keyUUID</code> is null or there are no strings to encode, otherwise an encoding!
     */
    public String encode( UUID keyUUID, String... toEncode ) {
        return ((keyUUID == null) || (toEncode == null) || (toEncode.length == 0)) ? null :
               codecId + new K2_EncodeSupport( randomizingSeedSupplier, keyUUID, toEncode ).encode();
    }

    /**
     * Decode the output of the encode methods.
     * <p>
     * Note: assuming that the <code>keyUUID</code> is not null AND the <code>ukEncoded</code> value is not null or empty,
     * then leading and trailing spaces are removed from the <code>ukEncoded</code>, and if the result is empty, then
     * it is treated as insignificant -- which is an error!
     *
     * @param keyUUID   nullable -- null triggers no encoding and a return of null.
     * @param ukEncoded null is treated as un-decode-able and trigger return of null, otherwise it is attempted to be decoded.
     * @return null or the decoded value
     * @throws IllegalArgumentException if the <code>ukEncoded</code> value is insignificant OR the encoding is not an
     *                                  output of the encode methods -- including if it is determined that it is NOT keyed to the passed in UUID!
     */
    public List<String> decode( UUID keyUUID, String ukEncoded ) {
        return ((keyUUID == null) || (ukEncoded == null)) ? null :
               new K2_DecodeSupport( keyUUID, validateToDecode( ukEncoded ) ).decode();
    }
}
