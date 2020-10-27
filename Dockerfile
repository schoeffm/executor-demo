FROM payara/server-full:5.201 as base-payara

ENV PAYARA_PATH=$HOME_DIR
ENV ASADMIN $PAYARA_DIR/bin/asadmin --user $ADMIN_USER --passwordfile=/tmp/pwdfile
ENV JAVA_HOME /usr/lib/jvm/zulu-8-amd64
ARG asadmin_file=${asadmin_file:-src/main/docker/core/asadmin.local.sh}
ARG ENABLE_JPROFILER=${ENABLE_JPROFILER:-false}

COPY lib/*.jar $PAYARA_DIR/glassfish/domains/production/lib/

COPY target/payaraexecutordemo.war $DEPLOY_DIR
