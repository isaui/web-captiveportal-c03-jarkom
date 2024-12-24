FROM nginx:alpine

# Install iptables
RUN apk add --no-cache iptables

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