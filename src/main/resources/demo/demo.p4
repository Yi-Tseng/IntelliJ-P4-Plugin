/*
 * Copyright 2017-present Yi Tseng
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

// This is a demo P4 file

#include <core.p4>
#include <v1model.p4>
#include "include/header.p4"

typedef bit<9> port_t;
typedef bit<16> next_hop_id_t;

/*
#elif defined(ABCDEFGHIJKLMNOPQRS)
const bit<32> abc = 1;
#endif
*/

#define  TEST(param1) \
    state ctrl_##param1 { \
}

#define
#define test=1

const port_t CPU_PORT = 255;

struct local_metadata_t {
    bit<16> l4_src_port;
    bit<16> l4_dst_port;
    next_hop_id_t next_hop_id;
}

parser parser_impl (packet_in packet,
                  out headers_t hdr,
                  inout local_metadata_t local_metadata,
                  inout standard_metadata_t standard_metadata) {

    state start {
        transition select(standard_metadata.ingress_port) {
            CPU_PORT: parse_packet_out;
            default: parse_ethernet;
        }
    }

    state parse_packet_out {
        packet.extract(hdr.packet_out);
        transition parse_ethernet;
    }

    state parse_ethernet {
        packet.extract(hdr.ethernet);
        transition select(hdr.ethernet.ether_type) {
            0x0800: parse_ipv4;
            default: accept;
        }
    }

    state parse_ipv4 {
        packet.extract(hdr.ipv4);
        transition select(hdr.ipv4.protocol) {
            8w6: parse_tcp;
            8w17: parse_udp;
            default: accept;
        }
    }

    state parse_tcp {
        packet.extract(hdr.tcp);
        local_metadata.l4_src_port = hdr.tcp.src_port;
        local_metadata.l4_dst_port = hdr.tcp.dst_port;
        transition accept;
    }

    state parse_udp {
        packet.extract(hdr.udp);
        local_metadata.l4_src_port = hdr.udp.src_port;
        local_metadata.l4_dst_port = hdr.udp.dst_port;
        transition accept;
    }
}

control deparser(packet_out packet, in headers_t hdr) {
    apply {
        packet.emit(hdr.packet_in);
        packet.emit(hdr.ethernet);
        packet.emit(hdr.ipv4);
        packet.emit(hdr.tcp);
        packet.emit(hdr.udp);
    }
}

action send_to_cpu(inout standard_metadata_t standard_metadata) {
    standard_metadata.egress_spec = CPU_PORT;
}

action set_egress_port(inout standard_metadata_t standard_metadata, port_t port) {
    standard_metadata.egress_spec = port;
}

action _drop() {
    mark_to_drop();
}

control port_counters_ingress(inout headers_t hdr,
                              inout standard_metadata_t standard_metadata) {

    counter(511, CounterType.packets) ingress_port_counter;

    apply {
        ingress_port_counter.count((bit<32>) standard_metadata.ingress_port);
    }
}

control port_counters_egress(inout headers_t hdr,
                             inout standard_metadata_t standard_metadata) {

    counter(511, CounterType.packets) egress_port_counter;

    apply {
        egress_port_counter.count((bit<32>) standard_metadata.egress_port);
    }
}
control verify_checksum_control(inout headers_t hdr,
                                inout local_metadata_t local_metadata) {
    apply {

    }
}

control compute_checksum_control(inout headers_t hdr,
                                 inout local_metadata_t local_metadata) {
    apply {

    }
}
control packetio_ingress(inout headers_t hdr,
                         inout standard_metadata_t standard_metadata) {
    apply {
        if (standard_metadata.ingress_port == CPU_PORT) {
            standard_metadata.egress_spec = hdr.packet_out.egress_port;
            hdr.packet_out.setInvalid();
            exit;
        }
    }
}

control packetio_egress(inout headers_t hdr,
                        inout standard_metadata_t standard_metadata) {
    apply {
        if (standard_metadata.egress_port == CPU_PORT) {
            hdr.packet_in.setValid();
            hdr.packet_in.ingress_port = standard_metadata.ingress_port;
        }
    }
}

control table0_control(inout headers_t hdr,
                       inout local_metadata_t local_metadata,
                       inout standard_metadata_t standard_metadata) {

    direct_counter(CounterType.packets_and_bytes) table0_counter;

    action set_next_hop_id(next_hop_id_t next_hop_id) {
        local_metadata.next_hop_id = next_hop_id;
    }

    table table0 {
        key = {
            standard_metadata.ingress_port : ternary;
            hdr.ethernet.src_addr : ternary;
            hdr.ethernet.dst_addr : ternary;
            hdr.ethernet.ether_type : ternary;
            hdr.ipv4.src_addr : ternary;
            hdr.ipv4.dst_addr : ternary;
            hdr.ipv4.protocol : ternary;
            local_metadata.l4_src_port : ternary;
            local_metadata.l4_dst_port : ternary;
        }
        actions = {
            set_egress_port(standard_metadata);
            send_to_cpu(standard_metadata);
            set_next_hop_id();
            _drop();
        }
        const default_action = _drop();
        counters = table0_counter;
    }

    apply {
        table0.apply();
     }
}

control wcmp_control(inout headers_t hdr,
                     inout local_metadata_t local_metadata,
                     inout standard_metadata_t standard_metadata) {

    direct_counter(CounterType.packets_and_bytes) wcmp_table_counter;
    action_selector(HashAlgorithm.crc16, 32w64, 32w16) wcmp_selector;

    table wcmp_table {
        support_timeout = false;
        key = {
            local_metadata.next_hop_id : exact;
            hdr.ipv4.src_addr : selector;
            hdr.ipv4.dst_addr : selector;
            hdr.ipv4.protocol : selector;
            local_metadata.l4_src_port : selector;
            local_metadata.l4_dst_port : selector;
        }
        actions = {
            set_egress_port(standard_metadata);
        }
        implementation = wcmp_selector;
        counters = wcmp_table_counter;
    }

    apply {
        if (local_metadata.next_hop_id != 0) {
            wcmp_table.apply();
        }
    }
}

control ingress(inout headers_t hdr,
                inout local_metadata_t local_metadata,
                inout standard_metadata_t standard_metadata) {

    apply {
        port_counters_ingress.apply(hdr, standard_metadata);
        packetio_ingress.apply(hdr, standard_metadata);
        table0_control.apply(hdr, local_metadata, standard_metadata);
        wcmp_control.apply(hdr, local_metadata, standard_metadata);
     }
}

control egress(inout headers_t hdr,
               inout local_metadata_t local_metadata,
               inout standard_metadata_t standard_metadata) {

    apply {
        port_counters_egress.apply(hdr, standard_metadata);
        packetio_egress.apply(hdr, standard_metadata);
    }
}

V1Switch(parser_impl(),
         verify_checksum_control(),
         ingress(),
         egress(),
         compute_checksum_control(),
         deparser()) main;
