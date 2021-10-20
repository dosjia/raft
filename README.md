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
| 4 + Message body length | Message Body Length | 2 bytes | Refer to below table |

|15-10|9-0|
| ---- | -----|
| reserved | message body length|

#### Body

For different command we always have different definition of the body, but they should comply the same format. Please
refer to Command List part.

#### Checksum

### Command List

#### Request of connection setup

Every node should set up connection first with each other node. So node should send request to every node in node list
when they start. Command id for this message is 0x00 01. There is no information in message body.

#### Response of connection setup

As the protocol of Raft, only follower send this kind of response to leader. Command id for this message is 0x80 01.

| Start | Field | Length | Description |
| ---- | -----| ---- | ---- |
| 0 | Machine name length | 2 bytes | - |
| 2 | Machine name of server | Non fixed | support 65535 upmost |

#### Request for vote of leader
Node can request for leadership when there is no leader detected. The node should raise the term by 1 first and send
this kind of request to each peer node. Command id for this message is 0x00 02.

| Start | Field | Length | Description |
| ---- | -----| ---- | ---- |
| 0 | Term | 4 bytes | Term of the requester |
| 4 | Term of last log | 4 bytes | Term of the last log in requester |
| 8 | Index of last log | 4 bytes | Index of the last log in requester |
| 12 | Machine name length | 2 bytes | - |
| 14 | Machine Name of requester | Non fixed | support 65535 upmost |

#### Response for vote of leader

Response of node when being asked to vote for the requester. Command id for this message is 0x80 02.

| Start | Field | Length | Description |
| ---- | -----| ---- | ---- |
| 0 | Term | 4 bytes | Term of the host |
| 4 | Request result | 1 bytes | The result of vote for the requester |

#### Request for replication of log
Request of leader to follower, contains log need to push to follower. Command id for this message is 0x00 03.

| Start | Field | Length | Description |
| ---- | -----| ---- | ---- |
| 0 | Term | 4 bytes | Term of the leader |
| 4 | prevLogTerm | 4 bytes | The term of previous log |
| 8 | prevLogIndex | 4 bytes | The index of previous log |
| 12 | leaderCommit | 4 bytes | The commit value of leader |
| 16 | logCount | 4 bytes | The size of log need to synchronized |
| 20 | logList[] | 1 log item's bytes * logCount | The value array of log |

In the logList variable, each of them is a log item. For each log item, please refer to below table.

| Start | Field | Length | Description |
| ---- | -----| ---- | ---- |
| 0 | term | 4 bytes | The term of the log |
| 4 | index | 4 bytes | The index of the log |
| 8 | logContentLength | 2 bytes | The length of the log |
| 10 | logContent | logContentLength bytes | The content of the log |


#### Response for replication of log
Response of follower to leader, contains the synchronized status. Command id for this message is 0x80 03.

| Start | Field | Length | Description |
| ---- | -----| ---- | ---- |
| 0 | term | 4 bytes | currentTerm of the receiver |
| 4 | synchronizeResult | 1 bytes | The result of synchronization |