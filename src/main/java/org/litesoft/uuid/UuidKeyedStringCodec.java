package org.litesoft.uuid;

import java.util.UUID;
import java.util.function.IntSupplier;

import org.litesoft.uuid.codecsupport.AbstractCodecUUID;
import org.litesoft.uuid.codecsupport.K1_DecodeSupport;
import org.litesoft.uuid.codecsupport.K1_EncodeSupport;

/**
 * Encode and Decode a String (Secret) which is Keyed to a passed in UUID (owner ID of the Secret).
 */
public class UuidKeyedStringCodec extends AbstractCodecUUID {
    public UuidKeyedStringCodec( IntSupplier randomizingSeedSupplier ) {
        super( randomizingSeedSupplier, "K", 1 );
    }

    /**
     * Encode a String (keyed to the UUID).
     * <p>
     * Note: assuming that the <code>UUID</code> is not null AND the <code>toEncode</code> value is not null or empty,
     * then leading and trailing spaces are removed from the <code>toEncode</code>, and if the result is empty, then
     * it is treated as insignificant -- which is an error!
     *
     * @param uuid     - nullable
     * @param toEncode characters to encode -- all long values can be encoded
     * @return null if UUID is null, otherwise an encoding!
     * @throws IllegalArgumentException if the <code>toEncode</code> value is insignificant OR it is not at least 6 characters long after any
     *                                  leading and trailing spaces are removed!
     */
    public String encode( UUID uuid, String toEncode ) {
        return ((uuid == null) || (toEncode == null) || toEncode.isEmpty()) ? null :
               codecId + new K1_EncodeSupport( randomizingSeedSupplier, uuid, validateToEncode( toEncode, 6 ) ).encode();
    }

    /**
     * Decode the output of the encode method.
     * <p>
     * Note: assuming that the <code>UUID</code> is not null AND the <code>ukEncoded</code> value is not null or empty,
     * then leading and trailing spaces are removed from the <code>ukEncoded</code>, and if the result is empty, then
     * it is treated as insignificant -- which is an error!
     *
     * @param uuid      nullable -- null triggers no encoding and a return of null.
     * @param ukEncoded null and empty are treated as un-decode-able and trigger return of null, otherwise it is attempted to be decoded.
     * @return null or the decoded value
     * @throws IllegalArgumentException if the <code>ukEncoded</code> value is insignificant OR the encoding is not an
     *                                  output of the encode method -- including if it is determined that it is NOT keyed to the passed in UUID!
     */
    public String decode( UUID uuid, String ukEncoded ) {
        return ((uuid == null) || (ukEncoded == null)) ? null :
               new K1_DecodeSupport( uuid, validateToDecode( ukEncoded ) ).decode();
    }
}
