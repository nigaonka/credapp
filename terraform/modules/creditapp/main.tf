# integrate with helper maps for object ownership
locals {
  app_name = "creditapp"
  app_instance = "${local.app_name}-${var.instance_name}"
  app_team = "inception-scrum" // use your own team name here
  app_product = "CoreServices" // use appropriate product (e.g. TivoService) here
  app_initiative = "ServicePlatform" // use appropriate initiative here
}

locals {
  owner_labels = {
    "app.kubernetes.io/name" = local.app_name
    "app.kubernetes.io/instance" = local.app_instance
    "app.kubernetes.io/part-of" = local.app_initiative
    "inception.tivo.com/owner" = var.deployment_owner
    "inception.tivo.com/team" = local.app_team
    "inception.tivo.com/product" = local.app_product
    "inception.tivo.com/env" = var.app_env
  }
  service_annotations = {
    "external-dns.alpha.kubernetes.io/hostname"                             = var.private_fqdn
    "service.beta.kubernetes.io/aws-load-balancer-additional-resource-tags" = "Datacenter=${var.eks_cluster_name},TivoEnv=${var.tivo_env},TivoOwner=${var.owner_email},TivoService=${var.tivo_service},TivoTTL=AlwaysOn"
    "service.beta.kubernetes.io/aws-load-balancer-internal"                 = "true"
    "service.beta.kubernetes.io/aws-load-balancer-security-groups" = "${var.frontend_sg}"
    "service.beta.kubernetes.io/aws-load-balancer-manage-backend-security-group-rules" =  "false"
  }

  owner_annotations = {
    "inception.tivo.com/email" = var.owner_email
    "inception.tivo.com/slack" = var.owner_slack
  }
  service_endpoint_tmp = "${kubernetes_service_v1.creditapp.metadata.0.name}.${kubernetes_service_v1.creditapp.metadata.0.namespace}:${kubernetes_service_v1.creditapp.spec.0.port.0.port}"
  owner_labels_tmp = jsonencode(local.owner_labels)
  owner_annotations_tmp = jsonencode(local.owner_annotations)
  test_configuration_tmp = <<-EOT
    {
      "k8s_service_endpoint": {
        "value": "${local.service_endpoint_tmp}"
      },
      "namespace": {
        "value": "${var.namespace}"
      },
      "owner_annotations": {
        "value": ${local.owner_annotations_tmp}
      },
      "owner_labels": {
        "value": ${local.owner_labels_tmp}
      },
      "smoke_test_image": {
        "value": "${var.smoke_test_image}"
      }
    }
    EOT
  test_annotations = {
    "inception.tivo.com/test-configuration" = local.test_configuration_tmp
  }
}

resource "kubernetes_pod_disruption_budget_v1" "creditapp" {
  metadata {
    name = local.app_name
    namespace = var.namespace
  }
  spec {
    min_available = "1"  // other values or a percentage might make more sense for your workload
    selector {
      match_labels = {
        app = local.app_instance // must mach deployment pod selector
      }
    }
  }
}

resource "kubernetes_horizontal_pod_autoscaler_v2" "creditapp" {
  metadata {
    name = local.app_name
    namespace = var.namespace
  }
  spec {
    max_replicas = 2 // the max replicas is dependent on your application
    min_replicas = 1 // at least two replicas allows the PDB to easily permit infrastructure disruptions
    scale_target_ref {
      api_version = "apps/v1"
      kind = "Deployment"
      name = local.app_name
    }
    metric {
      type = "Resource"
      resource {
        name = "cpu"
        target {
          type = "Utilization"
          average_utilization = 80 // a different average utilization might be more appropriate for your service
        }
      }
    }
    // CAVEAT: Autoscaling on the following custom metrics is experimental and subject to change:
    //      numberOfHits_per_second
    //          The number of hits for /ping endpoint
    //      http_server_requests_per_second
    //          Total number of http requests
    //      http_server_requests_seconds_max
    //          Maximum duration of an http request in this window
    // Please work with the inception team if you have a use case for autoscaling on custom metrics.
    metric {
      type = "Object"
      object {
        described_object{
          kind = "service"
          name = local.app_name
          api_version = "v1beta1"
        }
        metric {
          name = "numberOfHits_per_second" // CAVEAT: Usage of numberOfHits_per_second is experimental and subject to change.
        }
        target {
          type  = "Value"
          value = "5" // a different number of hits per second might be more appropriate for your service
        }
      }
    }
  }
}

resource "kubernetes_deployment_v1" "creditapp" {
  metadata {
    name = local.app_name
    namespace = var.namespace
    labels = local.owner_labels
    annotations = merge(local.owner_annotations, local.test_annotations)
  }

  # sadly, tws-ng uses so much cpu and takes so long to start up that
  # the ten minute default is insufficient. We really need to ditch
  # the inefficient trio schema marshalling code.
  timeouts {
    create = "7m"
    update = "7m"
  }

  spec {
    replicas = var.replicas

    selector {
      match_labels = {
        app = local.app_instance
      }
    }

    template {
      metadata {
        labels = {
          app = local.app_instance
        }
      }
      spec {
        topology_spread_constraint {
          max_skew = 1
          topology_key = "topology.kubernetes.io/zone"
          when_unsatisfiable = "ScheduleAnyway"
          label_selector {
            match_labels = {
              app = local.app_instance
            }
          }
        }
        topology_spread_constraint {
          max_skew = 1
          topology_key = "kubernetes.io/hostname"
          when_unsatisfiable = "ScheduleAnyway"
          label_selector {
            match_labels = {
              app = local.app_instance
            }
          }
        }
        container {
          name = "creditapp"
          image = var.image
          image_pull_policy = "Always"
/*
          readiness_probe {
            http_get {
              path = "/health"
              port = 40108
            }
          }
*/
          port {
            container_port = 40108
            name = "service"
          }
          port {
            container_port = 40108
            name = "metrics"
          }
          volume_mount {
            mount_path = "/tmp"
            name = "tmpdir"
          }
          env {
            name = "LOGBACK_CONFIG"
            value = "logback-stdout.xml"
          }
          env {
            name = "CONTAINER_NAME"
            value = "creditapp"
          }
          env {
            name = "DATACENTER_NAME"
            value = var.eks_cluster_name
          }
          env {
            name = "ENVIRONMENT_NAME"
            value_from {
              field_ref {
                field_path = "metadata.namespace"
              }
            }
          }
          env {
            name = "HOSTNAME"
            value = "some.node"
          }
          env {
            name = "KAFKA_ENDPOINT"
            value = var.kafka_endpoint
          }
          env {
            name = "TOKEN_ENDPOINT"
            value = var.token_endpoint
          }
          resources {
            limits = {
              cpu = "100m"
              memory = "512Mi"
            }
            requests = {
              cpu = "50m"
              memory = "256Mi"
            }
          }
        }
        volume {
          name = "tmpdir"
          empty_dir {}
        }
      }
    }
  }
}

resource "kubernetes_service_v1" "creditapp" {
  metadata {
    name = local.app_name
    namespace = var.namespace
    labels = merge(
      local.owner_labels,
      {
        "inception.tivo.com/monitoring" = "standard"
      }
    )
    annotations = merge(local.owner_annotations,local.service_annotations)
  }
  spec {
    selector = {
      app = local.app_instance
    }
    port {
      name = "service"
      port = 80
      protocol = "TCP"
      target_port = "service"
    }
    port {
      name = "metrics"
      port = 8080
      protocol = "TCP"
      target_port = "metrics"
    }
    type = "LoadBalancer"
  }
}
