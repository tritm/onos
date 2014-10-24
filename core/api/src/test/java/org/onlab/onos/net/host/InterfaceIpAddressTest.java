package org.onlab.onos.net.host;

import org.junit.Test;
import org.onlab.packet.IpAddress;
import org.onlab.packet.IpPrefix;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

/**
 * Tests for class {@link InterfaceIpAddress}.
 */
public class InterfaceIpAddressTest {
    private static final IpAddress IP_ADDRESS = IpAddress.valueOf("1.2.3.4");
    private static final IpPrefix SUBNET_ADDRESS =
        IpPrefix.valueOf("1.2.0.0/16");
    private static final IpAddress BROADCAST_ADDRESS =
        IpAddress.valueOf("1.2.0.255");         // NOTE: non-default broadcast
    private static final IpAddress PEER_ADDRESS = IpAddress.valueOf("5.6.7.8");

    private static final IpAddress IP_ADDRESS2 = IpAddress.valueOf("10.2.3.4");
    private static final IpPrefix SUBNET_ADDRESS2 =
        IpPrefix.valueOf("10.2.0.0/16");
    private static final IpAddress BROADCAST_ADDRESS2 =
        IpAddress.valueOf("10.2.0.255");        // NOTE: non-default broadcast
    private static final IpAddress PEER_ADDRESS2 =
        IpAddress.valueOf("50.6.7.8");

    /**
     * Tests valid class copy constructor.
     */
    @Test
    public void testCopyConstructor() {
        InterfaceIpAddress fromAddr;
        InterfaceIpAddress toAddr;

        // Regular interface address with default broadcast address
        fromAddr = new InterfaceIpAddress(IP_ADDRESS, SUBNET_ADDRESS);
        toAddr = new InterfaceIpAddress(fromAddr);
        assertThat(toAddr.toString(),
                   is("InterfaceIpAddress{ipAddress=1.2.3.4, subnetAddress=1.2.0.0/16}"));

        // Interface address with non-default broadcast address
        fromAddr = new InterfaceIpAddress(IP_ADDRESS, SUBNET_ADDRESS,
                                          BROADCAST_ADDRESS);
        toAddr = new InterfaceIpAddress(fromAddr);
        assertThat(toAddr.toString(),
                   is("InterfaceIpAddress{ipAddress=1.2.3.4, subnetAddress=1.2.0.0/16, broadcastAddress=1.2.0.255}"));

        // Point-to-point address with peer IP address
        fromAddr = new InterfaceIpAddress(IP_ADDRESS, SUBNET_ADDRESS, null,
                                          PEER_ADDRESS);
        toAddr = new InterfaceIpAddress(fromAddr);
        assertThat(toAddr.toString(),
                   is("InterfaceIpAddress{ipAddress=1.2.3.4, subnetAddress=1.2.0.0/16, peerAddress=5.6.7.8}"));
    }

    /**
     * Tests invalid class copy constructor for a null object to copy from.
     */
    @Test(expected = NullPointerException.class)
    public void testInvalidConstructorNullObject() {
        InterfaceIpAddress fromAddr = null;
        InterfaceIpAddress toAddr = new InterfaceIpAddress(fromAddr);
    }

    /**
     * Tests valid class constructor for regular interface address with
     * default broadcast address.
     */
    @Test
    public void testConstructorForDefaultBroadcastAddress() {
        InterfaceIpAddress addr =
            new InterfaceIpAddress(IP_ADDRESS, SUBNET_ADDRESS);
        assertThat(addr.toString(),
                   is("InterfaceIpAddress{ipAddress=1.2.3.4, subnetAddress=1.2.0.0/16}"));
    }

    /**
     * Tests valid class constructor for interface address with
     * non-default broadcast address.
     */
    @Test
    public void testConstructorForNonDefaultBroadcastAddress() {
        InterfaceIpAddress addr =
            new InterfaceIpAddress(IP_ADDRESS, SUBNET_ADDRESS,
                                   BROADCAST_ADDRESS);
        assertThat(addr.toString(),
                   is("InterfaceIpAddress{ipAddress=1.2.3.4, subnetAddress=1.2.0.0/16, broadcastAddress=1.2.0.255}"));
    }

    /**
     * Tests valid class constructor for point-to-point interface address with
     * peer address.
     */
    @Test
    public void testConstructorForPointToPointAddress() {
        InterfaceIpAddress addr =
            new InterfaceIpAddress(IP_ADDRESS, SUBNET_ADDRESS, null,
                                   PEER_ADDRESS);
        assertThat(addr.toString(),
                   is("InterfaceIpAddress{ipAddress=1.2.3.4, subnetAddress=1.2.0.0/16, peerAddress=5.6.7.8}"));
    }

    /**
     * Tests getting the fields of an interface address.
     */
    @Test
    public void testGetFields() {
        InterfaceIpAddress addr;

        // Regular interface address with default broadcast address
        addr = new InterfaceIpAddress(IP_ADDRESS, SUBNET_ADDRESS);
        assertThat(addr.ipAddress().toString(), is("1.2.3.4"));
        assertThat(addr.subnetAddress().toString(), is("1.2.0.0/16"));
        assertThat(addr.broadcastAddress(), is(nullValue()));   // TODO: Fix
        assertThat(addr.peerAddress(), is(nullValue()));

        // Interface address with non-default broadcast address
        addr = new InterfaceIpAddress(IP_ADDRESS, SUBNET_ADDRESS,
                                      BROADCAST_ADDRESS);
        assertThat(addr.ipAddress().toString(), is("1.2.3.4"));
        assertThat(addr.subnetAddress().toString(), is("1.2.0.0/16"));
        assertThat(addr.broadcastAddress().toString(), is("1.2.0.255"));
        assertThat(addr.peerAddress(), is(nullValue()));

        // Point-to-point address with peer IP address
        addr = new InterfaceIpAddress(IP_ADDRESS, SUBNET_ADDRESS, null,
                                      PEER_ADDRESS);
        assertThat(addr.ipAddress().toString(), is("1.2.3.4"));
        assertThat(addr.subnetAddress().toString(), is("1.2.0.0/16"));
        assertThat(addr.broadcastAddress(), is(nullValue()));
        assertThat(addr.peerAddress().toString(), is("5.6.7.8"));
    }

    /**
     * Tests equality of {@link InterfaceIpAddress}.
     */
    @Test
    public void testEquality() {
        InterfaceIpAddress addr1, addr2;

        // Regular interface address with default broadcast address
        addr1 = new InterfaceIpAddress(IP_ADDRESS, SUBNET_ADDRESS);
        addr2 = new InterfaceIpAddress(IP_ADDRESS, SUBNET_ADDRESS);
        assertThat(addr1, is(addr2));

        // Interface address with non-default broadcast address
        addr1 = new InterfaceIpAddress(IP_ADDRESS, SUBNET_ADDRESS,
                                       BROADCAST_ADDRESS);
        addr2 = new InterfaceIpAddress(IP_ADDRESS, SUBNET_ADDRESS,
                                       BROADCAST_ADDRESS);
        assertThat(addr1, is(addr2));

        // Point-to-point address with peer IP address
        addr1 = new InterfaceIpAddress(IP_ADDRESS, SUBNET_ADDRESS, null,
                                       PEER_ADDRESS);
        addr2 = new InterfaceIpAddress(IP_ADDRESS, SUBNET_ADDRESS, null,
                                       PEER_ADDRESS);
        assertThat(addr1, is(addr2));
    }

    /**
     * Tests non-equality of {@link InterfaceIpAddress}.
     */
    @Test
    public void testNonEquality() {
        InterfaceIpAddress addr1, addr2, addr3, addr4;

        // Regular interface address with default broadcast address
        addr1 = new InterfaceIpAddress(IP_ADDRESS, SUBNET_ADDRESS);
        // Interface address with non-default broadcast address
        addr2 = new InterfaceIpAddress(IP_ADDRESS, SUBNET_ADDRESS,
                                       BROADCAST_ADDRESS);
        // Point-to-point address with peer IP address
        addr3 = new InterfaceIpAddress(IP_ADDRESS, SUBNET_ADDRESS, null,
                                       PEER_ADDRESS);

        // Test interface addresses with different properties:
        //  - default-broadcast vs non-default broadcast
        //   - regular vs point-to-point
        assertThat(addr1, is(not(addr2)));
        assertThat(addr1, is(not(addr3)));
        assertThat(addr2, is(not(addr3)));

        // Test regular interface address with default broadcast address
        addr4 = new InterfaceIpAddress(IP_ADDRESS2, SUBNET_ADDRESS);
        assertThat(addr1, is(not(addr4)));
        addr4 = new InterfaceIpAddress(IP_ADDRESS, SUBNET_ADDRESS2);
        assertThat(addr1, is(not(addr4)));

        // Test interface address with non-default broadcast address
        addr4 = new InterfaceIpAddress(IP_ADDRESS2, SUBNET_ADDRESS,
                                       BROADCAST_ADDRESS);
        assertThat(addr2, is(not(addr4)));
        addr4 = new InterfaceIpAddress(IP_ADDRESS, SUBNET_ADDRESS2,
                                       BROADCAST_ADDRESS);
        assertThat(addr2, is(not(addr4)));
        addr4 = new InterfaceIpAddress(IP_ADDRESS, SUBNET_ADDRESS,
                                       BROADCAST_ADDRESS2);
        assertThat(addr2, is(not(addr4)));

        // Test point-to-point address with peer IP address
        addr4 = new InterfaceIpAddress(IP_ADDRESS2, SUBNET_ADDRESS, null,
                                       PEER_ADDRESS);
        assertThat(addr3, is(not(addr4)));
        addr4 = new InterfaceIpAddress(IP_ADDRESS, SUBNET_ADDRESS2, null,
                                       PEER_ADDRESS);
        assertThat(addr3, is(not(addr4)));
        addr4 = new InterfaceIpAddress(IP_ADDRESS, SUBNET_ADDRESS, null,
                                       PEER_ADDRESS2);
        assertThat(addr3, is(not(addr4)));
    }

    /**
     * Tests object string representation.
     */
    @Test
    public void testToString() {
        InterfaceIpAddress addr;

        // Regular interface address with default broadcast address
        addr = new InterfaceIpAddress(IP_ADDRESS, SUBNET_ADDRESS);
        assertThat(addr.toString(),
                   is("InterfaceIpAddress{ipAddress=1.2.3.4, subnetAddress=1.2.0.0/16}"));

        // Interface address with non-default broadcast address
        addr = new InterfaceIpAddress(IP_ADDRESS, SUBNET_ADDRESS,
                                      BROADCAST_ADDRESS);
        assertThat(addr.toString(),
                   is("InterfaceIpAddress{ipAddress=1.2.3.4, subnetAddress=1.2.0.0/16, broadcastAddress=1.2.0.255}"));

        // Point-to-point address with peer IP address
        addr = new InterfaceIpAddress(IP_ADDRESS, SUBNET_ADDRESS, null,
                                      PEER_ADDRESS);
        assertThat(addr.toString(),
                   is("InterfaceIpAddress{ipAddress=1.2.3.4, subnetAddress=1.2.0.0/16, peerAddress=5.6.7.8}"));
    }
}
