package org.litesoft.uuid;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UuidKeyedStringCodecTest {
    private static final UUID UUID1 = UUID.randomUUID();
    private static final UUID UUID2 = UUID.randomUUID();

    AtomicInteger seed = new AtomicInteger();

    UuidKeyedStringCodec codex = new UuidKeyedStringCodec( seed::get );

    @Test
    void encodeNonHappyCase() {
        String encoded = codex.encode( null, "123456" );
        assertNull( encoded );
        encoded = codex.encode( UUID1, null );
        assertNull( encoded );
        encoded = codex.encode( UUID1, "" );
        assertNull( encoded );
        // Insignificant text errors tested in CodexTest
    }

    @Test
    void decodeNonHappyCase() {
        String decoded = codex.decode( null, "UK1:123456" );
        assertNull( decoded );
        decoded = codex.decode( UUID1, null );
        assertNull( decoded );
        // Insignificant text errors tested in CodexTest
    }

    @Test
    void encodeDecode() {
        String teQBF = "Quick Brown Fox Jumped Over the Lazy Moon, The|~!@#$%^&*()_+╠`1234567890-=\\[]{}:;"; // non-ascii character!
        String encoded1_QBF = checkRT( UUID1, teQBF );
        String encoded2_QPF = checkRT( UUID2, teQBF );

        String te123 = "123456"; // (6 + 1) * 8 / 6 -> 4 Padding Bits
        String encoded1_123 = checkRT( UUID1, te123 );
        String encoded2_123 = checkRT( UUID2, te123 );
        // Construction errors tested in CodexTest

        assertNotEquals( encoded1_123, encoded2_123 );
        assertNotEquals( encoded1_123, encoded1_QBF );
        assertNotEquals( encoded1_123, encoded2_QPF );
        assertNotEquals( encoded2_123, encoded1_QBF );
        assertNotEquals( encoded2_123, encoded2_QPF );
        assertNotEquals( encoded1_QBF, encoded2_QPF );

        System.out.println( "UuidKeyedStringCodexTest.encodeDecode:" + "\n 1_123 " + encoded1_123 + "\n 2_123 " + encoded2_123 + "\n 1_QBF " + encoded1_QBF + "\n 2_QPF " + encoded2_QPF );

        checkRT( UUID1, "1234567" ); // (7 + 1) * 8 / 6 -> 2 Padding Bits
        checkRT( UUID1, "12345678" ); // (8 + 1) * 8 / 6 -> NO Padding Bits
    }

    private String checkRT( UUID uuid, String toEncode ) {
        String encoded = codex.encode( uuid, toEncode );
        String decoded = codex.decode( uuid, encoded );
        assertEquals( toEncode, decoded );

        // System.out.println( "UuidVersionCodexTest.encodeDecode: " + encoded );
        return encoded;
    }
}