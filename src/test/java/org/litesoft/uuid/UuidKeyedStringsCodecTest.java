package org.litesoft.uuid;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UuidKeyedStringsCodecTest {
    private static final String[] NO_STRINGS_TO_ENCODE = new String[0];
    private static final String[] NULL_STRING_TO_ENCODE = new String[]{null};

    private static final UUID UUID1 = UUID.randomUUID();
    private static final UUID UUID2 = UUID.randomUUID();

    AtomicInteger seed = new AtomicInteger();

    UuidKeyedStringsCodec codex = new UuidKeyedStringsCodec( seed::get );

    @Test
    void encodeNonHappyCase() {
        String encoded = codex.encode( null, "123456" );
        assertNull( encoded );
        encoded = codex.encode( UUID1, (List<String>)null );
        assertNull( encoded );
        encoded = codex.encode( UUID1, NO_STRINGS_TO_ENCODE );
        assertNull( encoded );
    }

    @Test
    void decodeNonHappyCase() {
        List<String> decoded = codex.decode( null, "UK2:123456" );
        assertNull( decoded );
        decoded = codex.decode( UUID1, null );
        assertNull( decoded );
    }

    @Test
    void encodeDecode() {
        String encoded = codex.encode( UUID1, NULL_STRING_TO_ENCODE );
        List<String> decoded = codex.decode( UUID1, encoded );
        assertEquals( 1, decoded.size() );
        assertEquals( "", decoded.get( 0 ) );

        checkRT( UUID1, List.of( "" ) );

        checkRT( UUID1, List.of( "12345678" ) ); // 1st of 3 with one byte differences to test Padding
        checkRT( UUID1, List.of( "1234567" ) ); // 2nd of 3 -- padding test

        List<String> te123 = List.of( "123456" ); // 3rd of 3 -- padding test
        String encoded1_123 = checkRT( UUID1, te123 );
        String encoded2_123 = checkRT( UUID2, te123 );

        List<String> teQBF = List.of( "Quick Brown Fox "
                , "Jumped Over the Lazy Moon"
                , ", "
                , "The|~!@#$%^&*()_+"
                , "╠"  // non-ascii character!
                , "`1234567890-=\\[]{}:;"
                , "" // empty string
        );
        String encoded1_QBF = checkRT( UUID1, teQBF );
        String encoded2_QPF = checkRT( UUID2, teQBF );

        assertNotEquals( encoded1_123, encoded2_123 );
        assertNotEquals( encoded1_123, encoded1_QBF );
        assertNotEquals( encoded1_123, encoded2_QPF );
        assertNotEquals( encoded2_123, encoded1_QBF );
        assertNotEquals( encoded2_123, encoded2_QPF );
        assertNotEquals( encoded1_QBF, encoded2_QPF );

        System.out.println( "UuidKeyedStringsCodexTest.encodeDecode:" + "\n 1_123 " + encoded1_123 + "\n 2_123 " + encoded2_123 + "\n 1_QBF " + encoded1_QBF + "\n 2_QPF " + encoded2_QPF );
    }

    private String checkRT( UUID uuid, List<String> toEncode ) {
        String encoded = codex.encode( uuid, toEncode );
        List<String> decoded = codex.decode( uuid, encoded );
        assertEquals( toEncode, decoded );

        // System.out.println( "UuidVersionCodexTest.encodeDecode: " + encoded );
        return encoded;
    }
}