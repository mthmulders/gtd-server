resource "oci_core_vcn" "demo-server" {
  cidr_block     = "172.16.0.0/16"
  compartment_id = var.project_compartment_ocid
  display_name   = "Demo"
  dns_label      = "awesomesauce"
}

resource "oci_core_internet_gateway" "demo-server" {
  compartment_id = var.project_compartment_ocid
  display_name   = "Demo"
  vcn_id         = oci_core_vcn.demo-server.id
}

resource "oci_core_route_table" "demo-server" {
  compartment_id = var.project_compartment_ocid
  vcn_id         = oci_core_vcn.demo-server.id
  display_name   = "Demo"

  route_rules {
    destination       = "0.0.0.0/0"
    network_entity_id = oci_core_internet_gateway.demo-server.id
  }
}

resource "oci_core_security_list" "demo-server-incoming" {
  display_name   = "Incoming traffic for demo-server"
  compartment_id = var.project_compartment_ocid
  vcn_id         = oci_core_vcn.demo-server.id

  egress_security_rules {
    protocol    = "all"
    destination = "0.0.0.0/0"
  }

  ingress_security_rules {
    icmp_options {
      type = 0
    }

    protocol = 1
    source   = "0.0.0.0/0"
  }
  ingress_security_rules {
    icmp_options {
      type = 3
      code = 4
    }

    protocol = 1
    source   = "0.0.0.0/0"
  }
  ingress_security_rules {
    icmp_options {
      type = 8
    }

    protocol = 1
    source   = "0.0.0.0/0"
  }

  # SSH traffic
  ingress_security_rules {
    protocol = "6" # TCP
    source   = "0.0.0.0/0"
    tcp_options {
      min = var.ssh_port
      max = var.ssh_port
    }
  }

  # HTTP traffic
  ingress_security_rules {
    protocol = "6" # TCP
    source   = "0.0.0.0/0"
    tcp_options {
      min = 80
      max = 80
    }
  }

  # HTTPS traffic
  ingress_security_rules {
    protocol = "6" # TCP
    source   = "0.0.0.0/0"
    tcp_options {
      min = 443
      max = 443
    }
  }
}

resource "oci_core_subnet" "demo-server" {
  cidr_block     = oci_core_vcn.demo-server.cidr_block
  compartment_id = var.project_compartment_ocid
  vcn_id         = oci_core_vcn.demo-server.id
  route_table_id = oci_core_route_table.demo-server.id

  display_name = "Demo"
  dns_label    = "demos"
  security_list_ids = [
    oci_core_security_list.demo-server-incoming.id
  ]
}
