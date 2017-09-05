FROM ubuntu:14.04
MAINTAINER GrowBit "ehy@growingabit.io"

RUN apt-get update

RUN apt-get upgrade -y

RUN apt-get install -y locales

# https://serverfault.com/questions/362903/how-do-you-set-a-locale-non-interactively-on-debian-ubuntu
ENV LC_ALL="en_US.UTF-8"
ENV LANG="en_US.UTF-8"
ENV LANGUAGE="en_US.UTF-8"

RUN echo -e 'LANG="en_US.UTF-8"\nLANGUAGE="en_US:en"\n' > /etc/default/locale

RUN apt-get install -y wget
RUN apt-get install -y unzip
RUN apt-get install -y git
RUN apt-get install -y libxt-dev
RUN apt-get install -y mercurial
RUN apt-get install -y zip
WORKDIR /opt

RUN wget https://storage.googleapis.com/growingabit-io-backend/jdk-7u80-linux-x64.tar.gz
RUN tar -xzf jdk-7u80-linux-x64.tar.gz
ENV JAVA_HOME="/opt/jdk1.7.0_80"

ENV PATH="$JAVA_HOME/bin:$PATH"

RUN echo $PATH

RUN java -version

RUN wget https://storage.googleapis.com/growingabit-io-backend/apache-maven-3.3.9-bin.zip
RUN unzip apache-maven-3.3.9-bin.zip
ENV M2_HOME="/opt/apache-maven-3.3.9"

ENV PATH="$M2_HOME/bin:$PATH"

RUN echo $PATH

RUN mvn -v

ADD ../spring-boot-app /opt/spring-boot-app

WORKDIR /opt/spring-boot-app

RUN ./mvnw clean package

RUN ls -la

WORKDIR /opt

ADD cmd.sh cmd.sh

RUN chmod +x cmd.sh

CMD cmd.sh