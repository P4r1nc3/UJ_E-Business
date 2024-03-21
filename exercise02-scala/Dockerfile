FROM ubuntu:22.04

RUN apt-get update && \
    apt-get -y install software-properties-common curl unzip zip

RUN curl -s "https://get.sdkman.io" | bash
RUN bash -c "source $HOME/.sdkman/bin/sdkman-init.sh && sdk install java 17.0.10-amzn"
RUN bash -c "source $HOME/.sdkman/bin/sdkman-init.sh && sdk install sbt 1.9.8"
RUN bash -c "source $HOME/.sdkman/bin/sdkman-init.sh && sdk install scala 2.13.10"

ENV PATH=$PATH:/root/.sdkman/candidates/java/17.0.10-amzn/bin/
ENV PATH=$PATH:/root/.sdkman/candidates/sbt/1.9.8/bin
ENV PATH=$PATH:/root/.sdkman/candidates/scala/2.13.10/bin

RUN mkdir ebusiness
WORKDIR ebusiness

COPY . .

EXPOSE 9000

CMD ["sbt", "run"]