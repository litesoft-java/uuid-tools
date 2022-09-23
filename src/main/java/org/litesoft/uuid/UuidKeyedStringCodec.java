package org.litesoft.uuid;

import java.util.UUID;
import java.util.function.IntSupplier;

import org.litesoft.uuid.codecsupport.AbstractCodecUUID;
import org.litesoft.uuid.codecsupport.K1_DecodeSupport;
import org.litesoft.uuid.codecsupport.K1_EncodeSupport;

/**
 * Encode and Decode a String (Secret) which must be at least 6 bytes long and is Keyed to a passed in UUID (owner ID of the Secret).
 */
public class UuidKeyedStringCodec extends AbstractCodecUUID {
    public UuidKeyedStringCodec( IntSupplier randomizingSeedSupplier ) {
        super( randomizingSeedSupplier, "K", 1 );
    }

    /**
     * Encode a String (keyed to the <code>keyUUID</code>).
     * <p>
     * Note: assuming that the <code>keyUUID</code> is not null AND the <code>toEncode</code> value is not null or empty,
     * then leading and trailing spaces are removed from the <code>toEncode</code>, and if the result is empty, then
     * it is treated as insignificant -- which is an error!
     *
     * @param keyUUID  nullable
     * @param toEncode characters to encode
     * @return null if <code>keyUUID</code> is null or <code>toEncode</code> is null or empty, otherwise an encoding!
     * @throws IllegalArgumentException if the <code>toEncode</code> value is insignificant OR it is not at least 6 characters long after any
     *                                  leading and trailing spaces are removed!
     */
    public String encode( UUID keyUUID, String toEncode ) {
        return ((keyUUID == null) || (toEncode == null) || toEncode.isEmpty()) ? null :
               codecId + new K1_EncodeSupport( randomizingSeedSupplier, keyUUID, validateToEncode( toEncode, 6 ) ).encode();
    }

    /**
     * Decode the output of the encode method.
     * <p>
     * Note: assuming that the <code>keyUUID</code> is not null AND the <code>ukEncoded</code> value is not null or empty,
     * then leading and trailing spaces are removed from the <code>ukEncoded</code>, and if the result is empty, then
     * it is treated as insignificant -- which is an error!
     *
     * @param keyUUID   nullable -- null triggers no encoding and a return of null.
     * @param ukEncoded null and empty are treated as un-decode-able and trigger return of null, otherwise it is attempted to be decoded.
     * @return null or the decoded value
     * @throws IllegalArgumentException if the <code>ukEncoded</code> value is insignificant OR the encoding is not an
     *                                  output of the encode method -- including if it is determined that it is NOT keyed to the passed in UUID!
     */
    public String decode( UUID keyUUID, String ukEncoded ) {
        return ((keyUUID == null) || (ukEncoded == null)) ? null :
               new K1_DecodeSupport( keyUUID, validateToDecode( ukEncoded ) ).decode();
    }
}
