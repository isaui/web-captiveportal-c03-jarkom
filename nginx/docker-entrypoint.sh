#!/bin/sh

# Run iptables setup
/docker-entrypoint.d/setup-iptables.sh

# Start Nginx
exec "$@"