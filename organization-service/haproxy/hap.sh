#!/bin/bash

echo 'Starting reload of haproxy config!'
haproxy -f /etc/haproxy/haproxy.cfg -p /var/run/haproxy.pid -D -st $(cat /var/run/haproxy.pid)
echo 'Successfully reloaded haproxy config!'