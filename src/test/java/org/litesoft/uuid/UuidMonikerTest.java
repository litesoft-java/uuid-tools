package org.litesoft.uuid;

import java.util.UUID;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UuidMonikerTest {

    @Test
    @SuppressWarnings("SpellCheckingInspection")
    void from() {
        verify( "60fba26b-11b9-4fa8-96b7-674080889fff", "Kogar-127" );
        verify( "7ec7d16d-144e-499f-98d1-675b4fbe1746", "Gimon-70" );
        verify( "8c9d0621-1bc3-4c9f-b4c3-445b4e92f007", "Culoc-7" );
        verify( "a65ebb29-c38d-4ee3-be56-36db060c3885", "Cunic-5" );
        verify( "de8f36d2-3f23-4190-a9ab-dd2b8b5db33c", "Rapil-60" );
        verify( "b7cb9548-5251-439b-af48-2fb133adcee2", "Kohir-98" );
        verify( "75ca1611-cd65-4736-a390-1e3b6f1113fa", "Jumeg-122" );
        verify( "5178659b-ffdb-4628-915b-bb6df048d456", "Femip-86" );
        verify( "2b9260dc-dfac-40b7-b6ec-07fb35331350", "Lipuh-80" );
        verify( "8874a853-f283-46b4-bca5-94da888dbae1", "Tasir-97" );
        verify( "10b19acb-cb50-434f-8386-5dc80793637c", "Kokup-124" );
        verify( "d7d0b087-b984-47d4-8f1a-7e0b74cb686c", "Wepog-108" );
        verify( "99c11ec0-c4ef-461a-96b8-b823a63ef706", "Dafow-6" );
        verify( "bd5f8793-98be-41ff-9329-c94ea0585f78", "Nahit-120" );
        verify( "aee5e664-d8bc-4aff-b4c5-48d5ed5cf7e5", "Fedej-101" );
        verify( "87424221-d902-46dc-b782-bcdd64e11282", "Baboy-2" );
        verify( "d59705a0-96bb-4d6f-9787-9a1677075a89", "Muwoz-9" );
        verify( "70d6f80e-8341-4bcd-94d1-8426cabc1749", "Pavik-73" );
        verify( "c0258bba-727f-4260-8c9c-2d463a772889", "Ribuf-9" );
        verify( "6759043c-e3cb-443e-9467-567fba0e47e9", "Giyec-105" );
        verify( "d4f78abc-10c0-4862-a40f-d220c7d221e8", "Tewah-104" );
        verify( "68e97649-562f-4dd7-8ceb-e376d62bd826", "Hiviv-38" );
        verify( "ad701317-62d3-426b-a89e-da71bbdf83ea", "Pibis-106" );
        verify( "89a2c622-6e22-40e1-9f7f-1484c96c87f9", "Fanil-121" );
        verify( "6cbe6698-3c37-4222-8745-0b764f7524ff", "Lefob-127" );
        verify( "28d2c903-1bd4-4cc8-9034-74871ea64b47", "Ritil-71" );
    }

    void verify( String sourceUuid, String expectedMoniker ) {
        String actualMoniker = UuidMoniker.from( UUID.fromString( sourceUuid ) );
        assertEquals( expectedMoniker, actualMoniker, () -> "from '" + sourceUuid + "'" );
    }

    @Test
    @Disabled
    void generateMoreExamples() {
        for ( int i = 0; i < 25; i++ ) {
            UUID uuid = UUID.randomUUID();
            String moniker = UuidMoniker.from( uuid );
            System.out.println( uuid + " --> " + moniker );
        }
    }
}