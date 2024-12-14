max_stale = "10m"

wait {
  "min" = "5s"
  "max" = "10s"
}

consul {
  retry {
    enabled     = true
    attempts    = 12
    backoff     = "250ms"
    max_backoff = "1m"
  }
}

template {
  source      = "/etc/haproxy/haproxy.cfg.tmpl"
  destination = "/etc/haproxy/haproxy.cfg"
  exec {
    command = "/hap.sh"
  }
  perms = 0600
}
