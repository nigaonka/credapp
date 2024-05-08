terraform {
  backend "s3" {
    encrypt             = true
    bucket              = "tivo-inception-k8s-terraform-storage-dev"
    dynamodb_table      = "tivo-inception-k8s-terraform-locking-dev"
    # do not specify key - the inception-k8s-docker-helper auto sets it to a path based on the datacenter,
    # service, and instance_name
    region              = "us-east-1"
  }
}

locals {
  app_name = "creditapp"
  app_env          = var.ephemeral_name
  aws_region       = var.aws_region
  tivo_env        = "Dev"
  image_user = var.user == "build" ? "" : "${var.user}/"
  image_tag = var.user == "build" ? var.ephemeral_name : "latest"
  image = format("docker.tivo.com/%screditapp:%s", local.image_user, local.image_tag)
  smoke_test_image = format("docker.tivo.com/%screditapp_smoke-test:%s", local.image_user, local.image_tag)
  functional_test_image = format("docker.tivo.com/%screditapp_functional-test:%s", local.image_user, local.image_tag)
  performance_test_image = format("docker.tivo.com/%screditapp_performance-test:%s", local.image_user, local.image_tag)
  private_domain   = data.terraform_remote_state.infrastructure.outputs.eks_cluster_domain
  private_fqdn   = "${local.app_name}-${local.app_env}.${local.private_domain}"
  eks_cluster_name = var.eks_cluster_name
  tivo_service     = "Linpub"
  // Remote State variable
  tf_remote_state = {
    bucket         = "tivo-inception-k8s-terraform-storage-dev"
    dynamodb_table = "tivo-inception-k8s-terraform-locking-dev"
    key            = "infrastructure/tek2/terraform.tfstate"
    rds            = {
      bucket     = "terraform.us-east-1.s3.awsops.com"
      datacenter = "tea2"
      platform   = "tivodev"
      region     = "us-east-1"
    }
    slack = {
      token_name = "/dev-shared/kubernetes/monitoring/slack-token"
    }
  }

}

provider "aws" {
  region  = var.aws_region
}

data "aws_ssm_parameter" "slack_token" {
  name = "/dev-shared/kubernetes/monitoring/slack-token"
}

provider "slack" {
  token = data.aws_ssm_parameter.slack_token.value
}

data "aws_eks_cluster" "eks" {
  name = var.eks_cluster_name
}

provider "kubernetes" {
  host                   = data.aws_eks_cluster.eks.endpoint
  cluster_ca_certificate = base64decode(data.aws_eks_cluster.eks.certificate_authority.0.data)
  exec {
    api_version = "client.authentication.k8s.io/v1beta1"
    args        = ["eks", "get-token", "--cluster-name", var.eks_cluster_name]
    command     = "aws"
  }
}

module "namespace" {
  source = "github.com/tivocorp/inception-k8s-terraform//modules/namespace?ref=v6.0.5"
  name = "creditapp-${var.ephemeral_name}"
  slack_alert_channel = var.slack_alert_channel
}
data "aws_security_group" "frontend_sg" {
  name = "frontend-sg-${local.eks_cluster_name}"
}


module "creditapp" {
  source = "./modules/creditapp"
  aws_region = var.aws_region
  eks_cluster_name = var.eks_cluster_name
  instance_name = var.ephemeral_name
  app_env = "dev"
  tivo_env = local.tivo_env
  tivo_service     = local.tivo_service
  deployment_owner = var.user
  owner_email = var.owner_email
  namespace = module.namespace.this_namespace_name
  image = local.image
  smoke_test_image = local.smoke_test_image
  functional_test_image = local.functional_test_image
  performance_test_image = local.performance_test_image
  private_fqdn = local.private_fqdn
  frontend_sg = data.aws_security_group.frontend_sg.id

}
data "terraform_remote_state" "vpc" {
  backend = "s3"
  config  = {
    region = local.tf_remote_state.rds.region
    bucket = local.tf_remote_state.rds.bucket
    key    = "tfstate/${local.tf_remote_state.rds.platform}/${local.tf_remote_state.rds.datacenter}.vpc.json"
  }
}

data "terraform_remote_state" "infrastructure" {
  backend = "s3"
  config  = {
    encrypt        = true
    bucket         = local.tf_remote_state.bucket
    dynamodb_table = local.tf_remote_state.dynamodb_table
    key            = local.tf_remote_state.key
    region         = local.aws_region
  }
}

data "terraform_remote_state" "sg" {
  backend = "s3"
  config  = {
    region = local.tf_remote_state.rds.region
    bucket = local.tf_remote_state.rds.bucket
    key    = "tfstate/${local.tf_remote_state.rds.platform}/${local.tf_remote_state.rds.datacenter}.sg.json"
  }
}

