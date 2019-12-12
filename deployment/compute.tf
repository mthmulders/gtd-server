# Following https://www.terraform.io/docs/providers/oci/guides/best_practices.html
variable "image_ids" {
  type = map(string)
  default = {
    // See https://docs.cloud.oracle.com/iaas/images/
    // Oracle-provided image "Canonical-Ubuntu-18.04-2019.11.13-0"
    eu-frankfurt-1 = "ocid1.image.oc1.eu-frankfurt-1.aaaaaaaau3hbpid4koglg5m3fcnjqxlhyvyzxhhcgemq3xgn2bgnxqyqac4a"
  }
}

resource "oci_core_instance" "gtd-server" {
  # availability_domain = "MoMM:EU-FRANKFURT-1-AD-1"
  availability_domain = "MoMM:EU-FRANKFURT-1-AD-2"
  compartment_id      = var.project_compartment_ocid
  # shape               = "VM.Standard.E2.1.Micro"
  shape               = "VM.Standard.E2.1"
  display_name        = "GTD Server"

  agent_config {
    is_monitoring_disabled = false
  }

  create_vnic_details {
    assign_public_ip = true
    display_name     = "Primary VNIC"
    hostname_label   = "demo"
    subnet_id        = oci_core_subnet.gtd-server.id
  }

  metadata = {
    ssh_authorized_keys = var.ssh_public_key
    #  user_data           = base64encode(file("./oci-init.sh"))
  }

  source_details {
    source_id   = var.image_ids[var.region]
    source_type = "image"
  }
}

output "GTD-Server-Public-IP" {
  value = [ oci_core_instance.gtd-server.public_ip ]
}
