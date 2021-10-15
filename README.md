# raft

One implementation of RAFT protocol written in Java. Node communicate with each other by TCP/IP.

## Communication Protocol

### Why use TCP/IP?

TCP/IP layer has better performance compared with http protocol. TCP/IP protocol has shorter message than http message.
And it will take less bandwidth. Then the performance will be promoted significantly. Since the communication is high
concurrency. We chose TCP/IP as the communication protocol finally.

### Structure of Message

| Identifier | Header | Body | Checksum | Identifier |
| ---- | -----| ---- | -----| ---- |
| 1 byte(0x7e)  | 2 byte | Non fixed length | 1 byte | 1 byte(0x7e) |

#### Header

There are 3 information in header. They are Command Id, Machine Name Length, Machine Name and Message Length. We use 2
bytes to identify different command. We use 2 bytes to show the length of machine name, then the machine name should be
less than 65535. And the machine name should contain English characters(include some special character suck as: '-','_')
and number only.

| Command Id | Machine Name Length | Machine Name | Message Body Length |
| ---- | -----| ---- | ---- |
| 2 bytes | 2 bytes | Non fixed length | 2 bytes |

| Start | Field | Length | Description |
| ---- | -----| ---- | ---- |
| 0 | Command Id | 2 bytes | - |
| 2 | Machine Name Length | 2 bytes | support upmost 65535 |
| 4 | Machine Name |Non fixed length | - |
| 4 + machine name length | Message Body Length | 2 bytes | Refer to below table |

|15-10|9-0|
| ---- | -----|
| reserved | message body length|

#### Body

For different command we always have different definition of the body, but they should comply the same format. Please
refer to Command List part.

#### Checksum

### Command List

#### Request of connection setup

Every node should setup connection first with each other node. So node should send request to every node in node list
when they start.

| Start | Field | Length | Description |
| ---- | -----| ---- | ---- |
| 0 | Machine name length | 2 bytes | - |
| 2 | Machine name of requester | Non fixed | support 65535 upmost |

#### Response of connection setup

As the protocol of Raft, only follower send this kind of response to leader.

| Start | Field | Length | Description |
| ---- | -----| ---- | ---- |
| 0 | Machine name length | 2 bytes | - |
| 2 | Machine name of server | Non fixed | support 65535 upmost |


#### Request for vote of leader

Node can request for leadership when there is no leader detected. The node should raise the term by 1 first and send
this kind of request to each peer node.

| Start | Field | Length | Description |
| ---- | -----| ---- | ---- |
| 0 | Term | 4 bytes | Term of the requester |
| 4 | Term of last log | 4 bytes | Term of the last log in requester |
| 8 | Index of last log | 4 bytes | Index of the last log in requester |
| 12 | Machine name length | 2 bytes | - |
| 14 | Machine Name of requester | Non fixed | support 65535 upmost |


#### Response for vote of leader
Response of node when being asked to vote for the requester.

| Start | Field | Length | Description |
| ---- | -----| ---- | ---- |
| 0 | Term | 4 bytes | Term of the host |
| 4 | Request result | 1 bytes | The result of vote for the requester |


#### Request for replication of log


#### Response for replication of log