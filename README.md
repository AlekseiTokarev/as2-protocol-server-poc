# AS2 protocol server POC

based on https://github.com/phax/as2-lib

#### To start local SQS analogue:
`docker run -p 9324:9324 -p 9325:9325 -v ~/projects/as2/hyla-as2-server/local/elasticmq.conf:/opt/elasticmq.conf softwaremill/elasticmq-native`

### AS2 Sender
AS2 client POC sample project is in https://github.com/AlekseiTokarev/as2-protocol-client
