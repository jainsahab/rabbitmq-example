FROM rabbitmq:3.7.5-alpine
MAINTAINER ThoughtWorks <daimlermbd@thoughtworks.com>

ADD rabbitmq_delayed_message_exchange-20171201-3.7.x.ez /plugins/

RUN rabbitmq-plugins enable --offline rabbitmq_mqtt rabbitmq_federation_management rabbitmq_stomp rabbitmq_tracing rabbitmq_delayed_message_exchange
