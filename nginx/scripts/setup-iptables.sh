#!/bin/sh

# Enable IP forwarding
echo 1 > /proc/sys/net/ipv4/ip_forward

# Clear existing rules
iptables -F
iptables -t nat -F

# Allow established connections
iptables -A INPUT -m state --state ESTABLISHED,RELATED -j ACCEPT

# NAT untuk internet access
iptables -t nat -A POSTROUTING -o ens4 -j MASQUERADE  # Gunakan interface host

# Redirect HTTP traffic ke nginx local
iptables -t nat -A PREROUTING -p tcp --dport 80 -j REDIRECT --to-port 80
iptables -t nat -A PREROUTING -p tcp --dport 443 -j REDIRECT --to-port 443

# Tangkap semua HTTP request keluar
iptables -t nat -A OUTPUT -p tcp --dport 80 ! -d 127.0.0.1 -j REDIRECT --to-port 80

# Allow forwarding
iptables -P FORWARD ACCEPT