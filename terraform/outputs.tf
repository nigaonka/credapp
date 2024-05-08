output "k8s_service_endpoint" {
  description = "The kubernetes endpoint for the service"
  value = module.creditapp.k8s_service_endpoint
}

output "smoke_test_image" {
  description = "The docker image for the smoke test"
  value = module.creditapp.smoke_test_image
}

output "functional_test_image" {
  description = "The docker image for the functional test"
  value = module.creditapp.functional_test_image
}

output "performance_test_image" {
  description = "The docker image for the performance test"
  value = module.creditapp.performance_test_image
}

output "namespace" {
  description = "The namespaces in which the service was deployed"
  value = module.creditapp.namespace
}

output "owner_labels" {
  description = "The ownership labels for the service"
  value = module.creditapp.owner_labels
}

output "owner_annotations" {
  description = "The ownership annotations for the service"
  value = module.creditapp.owner_annotations
}