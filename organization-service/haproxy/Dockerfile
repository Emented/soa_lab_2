FROM ubuntu

RUN \
  apt-get update && \
  apt-get install -y software-properties-common && \
  add-apt-repository ppa:vbernat/haproxy-3.1 && \
  apt-get update && \
  apt-get install -y wget curl unzip && \
  apt-get install -y haproxy

ADD haproxy.cfg /etc/haproxy/haproxy.cfg
ADD haproxy.pem /etc/ssl/certs/haproxy.pem
ADD consul.hcl /consul.hcl

ADD startup.sh /startup.sh
RUN chmod u+x /startup.sh

ADD hap.sh /hap.sh
RUN chmod u+x /hap.sh

ENV CONSUL_TEMPLATE_VERSION=0.39.1
ENV CONSUL_TEMPLATE_FILE=consul-template_${CONSUL_TEMPLATE_VERSION}_linux_amd64.zip
ENV CONSUL_TEMPLATE_URL="https://releases.hashicorp.com/consul-template/${CONSUL_TEMPLATE_VERSION}/${CONSUL_TEMPLATE_FILE}"

WORKDIR /tmp
RUN wget $CONSUL_TEMPLATE_URL && \
  unzip $CONSUL_TEMPLATE_FILE && \
  mv consul-template /usr/local/bin/consul-template && \
  chmod a+x /usr/local/bin/consul-template

ADD haproxy.cfg.tmpl /etc/haproxy/haproxy.cfg.tmpl

WORKDIR /

CMD ["/startup.sh"]