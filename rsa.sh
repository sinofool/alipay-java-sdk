#!/bin/bash

openssl genrsa -out alipay-private-key.pem 2048
openssl rsa -in alipay-private-key.pem -pubout -out alipay-public-key.pem
openssl pkcs8 -topk8 -inform PEM -in alipay-private-key.pem -outform PEM -nocrypt
cat alipay-public-key.pem

