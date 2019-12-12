resource "oci_database_autonomous_database" "gtd-database" {
  admin_password           = var.database_admin_password
  compartment_id           = var.project_compartment_ocid
  cpu_core_count           = 1 # ignored for free tier
  data_storage_size_in_tbs = 1 # ignored for free tier
  db_name                  = "gtddb"
  display_name             = "GTD Database"
  is_free_tier             = "true"
}
