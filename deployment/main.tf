terraform {
  required_providers {
    oci = "~> 3.53"
  }
}

variable "compartment_ocid" {}
variable "user_ocid" {}
variable "tenancy_ocid" {}
variable "fingerprint" {}
variable "private_key_path" {}
variable "private_key_password" {}
variable "region" {}
variable "project_compartment_ocid" {}
variable "ssh_public_key" {}
variable "database_admin_password" {}
variable "ssh_port" {}

# Configure the Oracle Cloud Infrastructure provider
provider "oci" {
  tenancy_ocid         = var.tenancy_ocid
  user_ocid            = var.user_ocid
  fingerprint          = var.fingerprint
  private_key_path     = var.private_key_path
  private_key_password = var.private_key_password
  region               = var.region
}
