#!/bin/sh

# Enable IP forwarding
echo 1 > /proc/sys/net/ipv4/ip_forward

# Clear existing iptables rules
iptables -F
iptables -t nat -F
iptables -X

# Allow established connections
iptables -A INPUT -m state --state ESTABLISHED,RELATED -j ACCEPT

# NAT untuk internet access (gunakan interface keluar yang sesuai, misalnya `ens4`)
iptables -t nat -A POSTROUTING -o ens4 -j MASQUERADE

# Redirect semua HTTP ke Nginx lokal (port 80)
iptables -t nat -A PREROUTING -p tcp --dport 80 -j REDIRECT --to-port 80

# Tangkap semua request HTTP keluar kecuali localhost dan arahkan ke Nginx lokal
iptables -t nat -A OUTPUT -p tcp --dport 80 ! -d 127.0.0.1 -j REDIRECT --to-port 80

# Set default policy untuk mengizinkan forward traffic
iptables -P FORWARD ACCEPT
