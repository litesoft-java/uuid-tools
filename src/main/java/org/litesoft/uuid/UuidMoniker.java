package org.litesoft.uuid;

import java.util.UUID;

import org.litesoft.utils.Hex;

public class UuidMoniker { // 2f6fd950-d4fc-4aab-ab2f-0cae2ac48809
    public static String from( UUID uuid ) {
        return (uuid == null) ? null
                              : new UuidMoniker( uuid.toString() ).unpack().toMoniker();
    }

    private final String uuid;
    private final StringBuilder sb = new StringBuilder();
    private final boolean[] used = new boolean[16];
    private final int[] bytes = new int[16];
    private int index = 0;

    private UuidMoniker( String uuid ) {
        this.uuid = uuid;
    }

    // 2f6fd950-d4fc-4aab-ab2f-0cae2ac48809
    // 01234567-101234567-201234567-3012345
    private UuidMoniker unpack() {
        addSection( 0, 8 );
        addSection( 9, 4 );
        addSection( 14, 4 );
        addSection( 19, 4 );
        addSection( 24, 12 );
        return this;
    }

    private void addSection( int from, int length ) {
        int uptoExclusive = from + length;
        while ( from < uptoExclusive ) {
            char c1 = uuid.charAt( from++ );
            char c2 = uuid.charAt( from++ );
            addAscii( 127 & Hex.from( c1, c2 ) );
        }
    }

    private void addAscii( int c ) {
        bytes[index++] = c;
    }

    @Override
    public String toString() {
        return uuid + " --> " + sb;
    }

    private String toMoniker() {
        addConsonant( Character::toUpperCase );
        addVowel( Character::toLowerCase );
        addConsonant( Character::toLowerCase );
        addVowel( Character::toLowerCase );
        addConsonant( Character::toLowerCase );
        return sb.append( '-' ).append( bytes[15] ).toString();
    }

    private static AlphabetMapper mapper( int from, int thru ) {
        return c -> ((from <= c) && (c <= thru)) ? (char)(c + 'A') : c;
    }

    private void addConsonant( CharCaseTransformer mCase ) {
        addCharacter( mCase, UuidMoniker::isConsonant, CONSONANTS );
    }

    private void addVowel( CharCaseTransformer mCase ) {
        addCharacter( mCase, UuidMoniker::isVowel, VOWELS );
    }

    private void addCharacter( CharCaseTransformer mCase, CharFilter charFilter, String fallbackChars ) {
        if ( !selectFromAlphabetMapper( mCase, charFilter, IDENTITY,
                                        GAP_UPPER_LOWER, GAP_BEYOND_LOWER,
                                        GAP_LOW_CHUNK_1, GAP_LOW_CHUNK_2, GAP_LOW_CHUNK_3 ) ) {
            selectFromFallback( mCase, fallbackChars );
        }
    }

    private void selectFromFallback( CharCaseTransformer caseTransformer, String fallbackChars ) {
        for ( int i = 0; i < bytes.length; i++ ) {
            if ( !used[i] ) {
                int value = bytes[i];
                while ( fallbackChars.length() <= value ) {
                    value /= 2;
                }
                addToOutput( i, caseTransformer.transform( fallbackChars.charAt( value ) ) );
                return;
            }
        }
    }

    private boolean selectFromAlphabetMapper( CharCaseTransformer caseTransformer, CharFilter charFilter, AlphabetMapper... mappers ) {
        for ( AlphabetMapper mapper : mappers ) {
            for ( int i = 0; i < bytes.length; i++ ) {
                if ( !used[i] ) {
                    int c = mapper.map( bytes[i] );
                    if ( charFilter.accept( c ) ) {
                        addToOutput( i, caseTransformer.transform( c ) );
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void addToOutput( int index, int c ) {
        sb.append( (char)c );
        used[index] = true;
    }

    interface CharFilter {
        boolean accept( int c );
    }

    interface CharCaseTransformer {
        int transform( int c );
    }

    interface AlphabetMapper {
        int map( int c );
    }

    private static final AlphabetMapper IDENTITY = c -> c;
    private static final AlphabetMapper GAP_UPPER_LOWER = mapper( '[', '`' );
    private static final AlphabetMapper GAP_BEYOND_LOWER = mapper( '{', 127 );
    private static final AlphabetMapper GAP_LOW_CHUNK_1 = mapper( 0, 25 ); // Bottom 26
    private static final AlphabetMapper GAP_LOW_CHUNK_2 = mapper( 20, 45 ); // Middle 26
    private static final AlphabetMapper GAP_LOW_CHUNK_3 = mapper( 39, 64 ); // Top 26

    private static boolean isConsonant( int c ) {
        return isLetter( c ) && !isVowel( c );
    }

    @SuppressWarnings("SpellCheckingInspection")
    private static final String CONSONANTS = "bcdfghjklmnprstvwxyz"; // Less Q
    @SuppressWarnings("SpellCheckingInspection")
    private static final String VOWELS = "aeiou"; // Less situational Y

    private static boolean isVowel( int c ) {
        return
                isEther( c, 'A', 'a' ) ||
                isEther( c, 'E', 'e' ) ||
                isEther( c, 'I', 'i' ) ||
                isEther( c, 'O', 'o' ) ||
                isEther( c, 'U', 'u' );
    }

    private static boolean isEther( int c, int uc, int lc ) {
        return (c == uc) || (c == lc);
    }

    private static boolean isLetter( int c ) {
        return ((('A' <= c) && (c <= 'Z')) || (('a' <= c) && (c <= 'z'))) && (c != 'Q') && (c != 'q') && (c != 'X') && (c != 'x');
    }
}
