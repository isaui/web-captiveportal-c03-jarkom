#!/bin/sh

# Enable IP forwarding
echo 1 > /proc/sys/net/ipv4/ip_forward

# Clear existing rules
iptables -F
iptables -t nat -F

# Allow established connections
iptables -A INPUT -m state --state ESTABLISHED,RELATED -j ACCEPT

# NAT for internet access - gunakan ens4 sebagai interface utama
iptables -t nat -A POSTROUTING -o ens4 -j MASQUERADE

# Redirect HTTP traffic ke port 80
iptables -t nat -A PREROUTING -p tcp --dport 80 -j REDIRECT --to-port 80

# Redirect HTTPS traffic ke port 443
iptables -t nat -A PREROUTING -p tcp --dport 443 -j REDIRECT --to-port 443

# Tambahan untuk forwarding ke docker network
iptables -A FORWARD -i ens4 -o br-$(docker network ls | grep portal_network | awk '{print $1}') -j ACCEPT
iptables -A FORWARD -o ens4 -i br-$(docker network ls | grep portal_network | awk '{print $1}') -j ACCEPT

# Simpan aturan
netfilter-persistent save