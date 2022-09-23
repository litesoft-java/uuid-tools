package org.litesoft.uuid;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UuidKeyedUuidAndStringsCodecTest {
    private static final String[] NULL_STRING_TO_ENCODE = new String[]{null};

    private static final UUID UUID1 = UUID.randomUUID();
    private static final UUID UUID2 = UUID.randomUUID();
    private static final UUID UUID3 = UUID.randomUUID();

    AtomicInteger seed = new AtomicInteger();

    UuidKeyedUuidAndStringsCodec codex = new UuidKeyedUuidAndStringsCodec( seed::get );

    @Test
    void encodeNonHappyCase() {
        String encoded = codex.encode( null, UUID3, "123456" );
        assertNull( encoded );
        encoded = codex.encode( UUID1, null, "123456" );
        assertNull( encoded );
    }

    @Test
    void decodeNonHappyCase() {
        UuidStringsPair decoded = codex.decode( null, "UK2:123456" );
        assertNull( decoded );
        decoded = codex.decode( UUID1, null );
        assertNull( decoded );
    }

    @Test
    void encodeDecode() {
        String encoded = codex.encode( UUID1, UUID3, NULL_STRING_TO_ENCODE );
        UuidStringsPair pair = codex.decode( UUID1, encoded );
        assertNotNull( pair );
        assertEquals( UUID3, pair.getUuid() );
        assertEquals( 1, pair.getStrings().size() );
        assertEquals( "", pair.getStrings().get( 0 ) );

        checkRT( UUID1, UUID3, List.of( "" ) );
        checkRT( UUID1, UUID3, List.of() ); // No strings!

        checkRT( UUID1, UUID3, List.of( "12345678" ) ); // 1st of 3 with one byte differences to test Padding
        checkRT( UUID1, UUID3, List.of( "1234567" ) ); // 2nd of 3 -- padding test

        List<String> te123 = List.of( "123456" ); // 3rd of 3 -- padding test
        String encoded1_123 = checkRT( UUID1, UUID3, te123 );
        String encoded2_123 = checkRT( UUID2, UUID3, te123 );

        List<String> teQBF = List.of( "Quick Brown Fox "
                , "Jumped Over the Lazy Moon"
                , ", "
                , "The|~!@#$%^&*()_+"
                , "╠"  // non-ascii character!
                , "`1234567890-=\\[]{}:;"
                , "" // empty string
        );
        String encoded1_QBF = checkRT( UUID1, UUID3, teQBF );
        String encoded2_QPF = checkRT( UUID2, UUID3, teQBF );

        assertNotEquals( encoded1_123, encoded2_123 );
        assertNotEquals( encoded1_123, encoded1_QBF );
        assertNotEquals( encoded1_123, encoded2_QPF );
        assertNotEquals( encoded2_123, encoded1_QBF );
        assertNotEquals( encoded2_123, encoded2_QPF );
        assertNotEquals( encoded1_QBF, encoded2_QPF );
    }

    @SuppressWarnings("SameParameterValue")
    private String checkRT( UUID keyUUID, UUID uuid, List<String> toEncode ) {
        String encoded = codex.encode( keyUUID, uuid, toEncode );
        UuidStringsPair pair = codex.decode( keyUUID, encoded );
        assertNotNull( pair );
        assertEquals( uuid, pair.getUuid() );
        assertEquals( toEncode.size(), pair.getStrings().size() );
        for ( int i = 0; i < toEncode.size(); i++ ) {
            assertEquals( toEncode.get( i ), pair.getStrings().get( i ), "string-" + i );

        }
        // System.out.println( "UuidVersionCodexTest.encodeDecode: " + encoded );
        return encoded;
    }
}