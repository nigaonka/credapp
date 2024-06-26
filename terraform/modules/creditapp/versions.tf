terraform {
  required_providers {
      aws = {
      source = "hashicorp/aws"
      version = ">= 2.57.0"
    }
    kubernetes = {
      source = "hashicorp/kubernetes"
      version = ">= 2.0.1"
    }
  }
  required_version = ">= 1.0"
}
