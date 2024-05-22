FROM golang:1.22.1-alpine3.18

RUN apk add --no-cache bash mysql-client

WORKDIR /backend

COPY go.mod ./
COPY go.sum ./
RUN go mod download

COPY . .

RUN go build -o server ./server.go

EXPOSE 8080

COPY wait-for-it.sh /wait-for-it.sh
RUN chmod +x /wait-for-it.sh
