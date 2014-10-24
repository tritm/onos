package org.onlab.onos.net.host;

import java.util.Objects;
import org.onlab.packet.IpAddress;
import org.onlab.packet.IpPrefix;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Represents a single IP address information on an interface.
 *
 * TODO:
 *  - Add computation for the default broadcast address if it is not
 *    specified
 *  - Add explicit checks that each IP address or prefix belong to the
 *    same IP version: IPv4/IPv6.
 *  - Inside the copy constructor we should use copy constructors for each
 *    field
 */
public class InterfaceIpAddress {
    private final IpAddress ipAddress;
    private final IpPrefix subnetAddress;
    private final IpAddress broadcastAddress;
    private final IpAddress peerAddress;

    /**
     * Copy constructor.
     *
     * @param other the object to copy from
     */
    public InterfaceIpAddress(InterfaceIpAddress other) {
        // TODO: we should use copy constructors for each field
        this.ipAddress = other.ipAddress;
        this.subnetAddress = other.subnetAddress;
        this.broadcastAddress = other.broadcastAddress;
        this.peerAddress = other.peerAddress;
    }

    /**
     * Constructor for a given IP address and a subnet address.
     *
     * @param ipAddress the IP address
     * @param subnetAddress the IP subnet address
     */
    public InterfaceIpAddress(IpAddress ipAddress, IpPrefix subnetAddress) {
        this.ipAddress = checkNotNull(ipAddress);
        this.subnetAddress = checkNotNull(subnetAddress);
        // TODO: Recompute the default broadcast address from the subnet
        // address
        this.broadcastAddress = null;
        this.peerAddress = null;
    }

    /**
     * Constructor for a given IP address and a subnet address.
     *
     * @param ipAddress the IP address
     * @param subnetAddress the IP subnet address
     * @param broadcastAddress the IP broadcast address. It can be used
     * to specify non-default broadcast address
     */
    public InterfaceIpAddress(IpAddress ipAddress, IpPrefix subnetAddress,
                              IpAddress broadcastAddress) {
        this.ipAddress = checkNotNull(ipAddress);
        this.subnetAddress = checkNotNull(subnetAddress);
        this.broadcastAddress = broadcastAddress;
        this.peerAddress = null;
    }

    /**
     * Constructor for a given IP address and a subnet address.
     *
     * @param ipAddress the IP address
     * @param subnetAddress the IP subnet address
     * @param broadcastAddress the IP broadcast address. It can be used
     * to specify non-default broadcast address. It should be null for
     * point-to-point interfaces with a peer address
     * @param peerAddress the peer IP address for point-to-point interfaces
     */
    public InterfaceIpAddress(IpAddress ipAddress, IpPrefix subnetAddress,
                              IpAddress broadcastAddress,
                              IpAddress peerAddress) {
        this.ipAddress = checkNotNull(ipAddress);
        this.subnetAddress = checkNotNull(subnetAddress);
        this.broadcastAddress = broadcastAddress;
        this.peerAddress = peerAddress;
    }

    /**
     * Gets the IP address.
     *
     * @return the IP address
     */
    public IpAddress ipAddress() {
        return ipAddress;
    }

    /**
     * Gets the IP subnet address.
     *
     * @return the IP subnet address
     */
    public IpPrefix subnetAddress() {
        return subnetAddress;
    }

    /**
     * Gets the subnet IP broadcast address.
     *
     * @return the subnet IP broadcast address
     */
    public IpAddress broadcastAddress() {
        return broadcastAddress;
    }

    /**
     * Gets the IP point-to-point interface peer address.
     *
     * @return the IP point-to-point interface peer address
     */
    public IpAddress peerAddress() {
        return peerAddress;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof InterfaceIpAddress)) {
            return false;
        }
        InterfaceIpAddress otherAddr = (InterfaceIpAddress) other;

        return Objects.equals(this.ipAddress, otherAddr.ipAddress)
            && Objects.equals(this.subnetAddress, otherAddr.subnetAddress)
            && Objects.equals(this.broadcastAddress,
                              otherAddr.broadcastAddress)
            && Objects.equals(this.peerAddress, otherAddr.peerAddress);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ipAddress, subnetAddress, broadcastAddress,
                            peerAddress);
    }

    @Override
    public String toString() {
        return toStringHelper(this).add("ipAddress", ipAddress)
            .add("subnetAddress", subnetAddress)
            .add("broadcastAddress", broadcastAddress)
            .add("peerAddress", peerAddress)
            .omitNullValues().toString();
    }
}
