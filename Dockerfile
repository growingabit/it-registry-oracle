FROM ubuntu:14.04
MAINTAINER GrowBit "ehy@growingabit.io"

RUN apt-get update

RUN apt-get upgrade -y

RUN apt-get install -y wget
RUN apt-get install -y unzip
RUN apt-get install -y git
RUN apt-get install -y jq
RUN apt-get install -y zip
RUN apt-get install -y curl
WORKDIR /opt

RUN wget https://storage.googleapis.com/growingabit-io-backend/jdk-8u144-linux-x64.tar.gz
RUN tar -xzf jdk-8u144-linux-x64.tar.gz
ENV JAVA_HOME="/opt/jdk1.8.0_144"

ENV PATH="$JAVA_HOME/bin:$PATH"

RUN echo $PATH

RUN java -version

RUN wget https://storage.googleapis.com/growingabit-io-backend/apache-maven-3.3.9-bin.zip
RUN unzip apache-maven-3.3.9-bin.zip
ENV M2_HOME="/opt/apache-maven-3.3.9"

ENV PATH="$M2_HOME/bin:$PATH"

RUN echo $PATH

RUN mvn -v

ADD spring-boot-app /opt/spring-boot-app

WORKDIR /opt/spring-boot-app

RUN ./mvnw clean package

RUN ls -la

WORKDIR /opt

ADD cmd.sh cmd.sh

RUN chmod +x cmd.sh

CMD /opt/cmd.sh