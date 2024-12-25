#!/bin/sh

# Enable IP forwarding
echo 1 > /proc/sys/net/ipv4/ip_forward

# Clear existing rules
iptables -F
iptables -t nat -F

# Allow established connections
iptables -A INPUT -m state --state ESTABLISHED,RELATED -j ACCEPT

# NAT for internet access - gunakan eth0 (interface di dalam container)
iptables -t nat -A POSTROUTING -o eth0 -j MASQUERADE

# Redirect HTTP traffic ke port 80
iptables -t nat -A PREROUTING -p tcp --dport 80 -j REDIRECT --to-port 80

# Redirect HTTPS traffic ke port 443
iptables -t nat -A PREROUTING -p tcp --dport 443 -j REDIRECT --to-port 443

# Basic forwarding rules
iptables -A FORWARD -i eth0 -j ACCEPT
iptables -A FORWARD -o eth0 -j ACCEPT