/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2007-2014 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2014 The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * OpenNMS(R) is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with OpenNMS(R).  If not, see:
 *      http://www.gnu.org/licenses/
 *
 * For more information contact:
 *     OpenNMS(R) Licensing <license@opennms.org>
 *     http://www.opennms.org/
 *     http://www.opennms.com/
 *******************************************************************************/

package org.opennms.core.utils;

import org.opennms.core.xml.JaxbUtils;
import org.opennms.netmgt.config.discovery.DiscoveryConfiguration;
import org.opennms.netmgt.config.discovery.IncludeRange;

import java.io.StringWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>SubnetUtilsUtils class.</p>
 *
 * @author <a href="">Apache</a>
 * @version $Id: $
 */
public class SubnetUtils {

    private static final String IP_ADDRESS = "(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})";
    private static final String SLASH_FORMAT = IP_ADDRESS + "/(\\d{1,3})";
    private static final Pattern addressPattern = Pattern.compile(IP_ADDRESS);
    private static final Pattern cidrPattern = Pattern.compile(SLASH_FORMAT);
    private static final int NBITS = 32;

    private int netmask = 0;
    private int address = 0;
    private int network = 0;
    private int broadcast = 0;

    /**
     * Constructor that takes a CIDR-notation string, e.g. "192.168.0.1/16"
     * @param cidrNotation A CIDR-notation string, e.g. "192.168.0.1/16"
     */
    public SubnetUtils(String cidrNotation) {
        calculate(cidrNotation);
    }

    /**
     * Constructor that takes two dotted decimal addresses.
     * @param address An IP address, e.g. "192.168.0.1"
     * @param mask A dotted decimal netmask e.g. "255.255.0.0"
     */
    public SubnetUtils(String address, String mask) {
        calculate(toCidrNotation(address, mask));
    }

    /**
     * Convenience container for subnet summary information.
     *
     */
    public final class SubnetInfo {
        private SubnetInfo() {}

        private int netmask()       { return netmask; }
        private int network()       { return network; }
        private int address()       { return address; }
        private int broadcast()     { return broadcast; }
        private int low()           { return network() + 1; }
        private int high()          { return broadcast() - 1; }

        public boolean isInRange(String address)    { return isInRange(toInteger(address)); }
        private boolean isInRange(int address)      { return ((address-low()) <= (high()-low())); }

        public String getBroadcastAddress()         { return format(toArray(broadcast())); }
        public String getNetworkAddress()           { return format(toArray(network())); }
        public String getNetmask()                  { return format(toArray(netmask())); }
        public String getAddress()                  { return format(toArray(address())); }
        public String getLowAddress()               { return format(toArray(low())); }
        public String getHighAddress()              { return format(toArray(high())); }
        public int getAddressCount()                { return (broadcast() - low()); }

        public int asInteger(String address)        { return toInteger(address); }

        public String getCidrSignature() {
            return toCidrNotation(
                    format(toArray(address())),
                    format(toArray(netmask()))
            );
        }

        public String[] getAllAddresses() {
            String[] addresses = new String[getAddressCount()];
            for (int add = low(), j=0; add <= high(); ++add, ++j) {
                addresses[j] = format(toArray(add));
            }
            return addresses;
        }
    }

    /**
     * Return a {@link SubnetInfo} instance that contains subnet-specific statistics
     * @return
     */
    public final SubnetInfo getInfo() { return new SubnetInfo(); }

    /*
     * Initialize the internal fields from the supplied CIDR mask
     */
    private void calculate(String mask) {
        Matcher matcher = cidrPattern.matcher(mask);

        if (matcher.matches()) {
            address = matchAddress(matcher);

            /* Create a binary netmask from the number of bits specification /x */
            int cidrPart = rangeCheck(Integer.parseInt(matcher.group(5)), 0, NBITS-1);
            for (int j = 0; j < cidrPart; ++j) {
                netmask |= (1 << 31-j);
            }

            /* Calculate base network address */
            network = (address & netmask);

            /* Calculate broadcast address */
            broadcast = network | ~(netmask);
        }
        else
            throw new IllegalArgumentException("Could not parse [" + mask + "]");
    }

    /*
     * Convert a dotted decimal format address to a packed integer format
     */
    private int toInteger(String address) {
        Matcher matcher = addressPattern.matcher(address);
        if (matcher.matches()) {
            return matchAddress(matcher);
        }
        else
            throw new IllegalArgumentException("Could not parse [" + address + "]");
    }

    /*
     * Convenience method to extract the components of a dotted decimal address and
     * pack into an integer using a regex match
     */
    private int matchAddress(Matcher matcher) {
        int addr = 0;
        for (int i = 1; i <= 4; ++i) {
            int n = (rangeCheck(Integer.parseInt(matcher.group(i)), 0, 255));
            addr |= ((n & 0xff) << 8*(4-i));
        }
        return addr;
    }

    /*
     * Convert a packed integer address into a 4-element array
     */
    private int[] toArray(int val) {
        int ret[] = new int[4];
        for (int j = 3; j >= 0; --j)
            ret[j] |= ((val >>> 8*(3-j)) & (0xff));
        return ret;
    }

    /*
     * Convert a 4-element array into dotted decimal format
     */
    private String format(int[] octets) {
        StringBuilder str = new StringBuilder();
        for (int i =0; i < octets.length; ++i){
            str.append(octets[i]);
            if (i != octets.length - 1) {
                str.append(".");
            }
        }
        return str.toString();
    }

    /*
     * Convenience function to check integer boundaries
     */
    private int rangeCheck(int value, int begin, int end) {
        if (value >= begin && value <= end)
            return value;

        throw new IllegalArgumentException("Value out of range: [" + value + "]");
    }

    /*
     * Count the number of 1-bits in a 32-bit integer using a divide-and-conquer strategy
     * see Hacker's Delight section 5.1
     */
    int pop(int x) {
        x = x - ((x >>> 1) & 0x55555555);
        x = (x & 0x33333333) + ((x >>> 2) & 0x33333333);
        x = (x + (x >>> 4)) & 0x0F0F0F0F;
        x = x + (x >>> 8);
        x = x + (x >>> 16);
        return x & 0x0000003F;
    }

    /* Convert two dotted decimal addresses to a single xxx.xxx.xxx.xxx/yy format
     * by counting the 1-bit population in the mask address. (It may be better to count
     * NBITS-#trailing zeroes for this case)
     */
    private String toCidrNotation(String addr, String mask) {
        return addr + "/" + pop(toInteger(mask));
    }

    public static void main(String args[]) {
 
        SubnetUtils su = new SubnetUtils("172.168.0.110","255.255.255.148");
        SubnetUtils sus = new SubnetUtils("192.168.0.110","255.255.252.0");
        System.out.println(su.getInfo().getLowAddress() + " " + su.getInfo().getHighAddress());
        System.out.println(sus.getInfo().getLowAddress() + " " + sus.getInfo().getHighAddress());
        System.out.println(sus.getInfo().getAllAddresses().length);
    }
}