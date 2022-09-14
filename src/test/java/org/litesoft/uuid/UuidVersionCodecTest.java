package org.litesoft.uuid;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UuidVersionCodecTest {
    AtomicInteger seed = new AtomicInteger();

    UuidVersionCodec codec = new UuidVersionCodec( seed::get );

    @Test
    void encodeNonHappyCase() {
        String encoded = codec.encode( null, 0 );
        assertNull( encoded );
        encoded = codec.encode( null );
        assertNull( encoded );
        // Insignificant text errors tested in CodecTest
    }

    @Test
    void decodeNonHappyCase() {
        UuidVersionPair pair = codec.decode( null );
        assertNull( pair );
        pair = codec.decode( "" );
        assertNull( pair );
        // Insignificant text errors tested in CodecTest
    }

    @Test
    void encodeDecode() {
        UUID uuid = UUID.randomUUID();
        String encoded42 = checkRT( uuid, 42 );
        String encodedMin = checkRT( uuid, Long.MIN_VALUE );
        String encodedMax = checkRT( uuid, Long.MAX_VALUE );
        assertNotEquals( encoded42, encodedMin );
        assertNotEquals( encoded42, encodedMax );
        assertNotEquals( encodedMin, encodedMax );
        // Construction errors tested in CodecTest

        // System.out.println( "UuidVersionCodecTest.encodeDecode:" + "\n Min " + encodedMin + "\n Max " + encodedMax + "\n  42 " + encoded42 );
    }

    private String checkRT( UUID uuid, long version ) {
        UuidVersionPair pair = UuidVersionPair.of( uuid, version );
        String encoded = codec.encode( pair );
        UuidVersionPair pair2 = codec.decode( encoded );
        assertEquals( pair, pair2 );

        // System.out.println( "UuidVersionCodecTest.encodeDecode: " + encoded );
        return encoded;
    }
}