FROM nginx:alpine

# Install iptables & openssl
RUN apk add --no-cache iptables openssl

# Generate self-signed certificate
RUN openssl req -x509 -nodes -days 365 -newkey rsa:2048 \
    -keyout /etc/ssl/private/nginx.key \
    -out /etc/ssl/certs/nginx.crt \
    -subj "/CN=localhost"

# Copy configs
COPY conf/nginx.conf /etc/nginx/nginx.conf
COPY conf/default.conf /etc/nginx/conf.d/default.conf
COPY scripts/setup-iptables.sh /docker-entrypoint.d/

# Make script executable  
RUN chmod +x /docker-entrypoint.d/setup-iptables.sh

# Create custom entrypoint script
COPY docker-entrypoint.sh /
RUN chmod +x /docker-entrypoint.sh

ENTRYPOINT ["/docker-entrypoint.sh"]
CMD ["nginx", "-g", "daemon off;"]