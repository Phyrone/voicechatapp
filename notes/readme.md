# This directory contains notes which are not ready to be described as documents.
It is subject to change and may be deleted at any time.

UID: <snowflake>@<hostname of homeserver>
  f.e. 183257676496339456@example.com

SNOWFLAKE:
    64-bit integer
    1 bit for sign (could be used to increase the timestamp to 42 bits)
    41 bits for timestamp (epoch 2020-01-01 00:00:00)
    10 bits for worker id
    12 bits for sequence number 


messages:
- Messages are immutable
- Messages can be singed by the author
- Messages can be encrypted by the author
  - must contain all channel members public keys
  - clients do only encrypt for joined memebers at the time of sending
  - clients do not encrypt for members who joined after sending!!!
  - members who joined after sending can not decrypt the message and so not read them
- to edit or delete a message it has to be overridden as it cannot be deleted
  - a new message is created and it contains a reference to the old message to be overridden
  - if the message contains no chatcomponent it is considered deleted
  - new messages with no chatcomponent are considered invalid

users:
- users are identified by UID
- users can have 0..* devices and connections
- users can have 0..* public keys
  - one public key is marked as primary (pinned) this is the one used by other users to encrypt messages
  - other public keys are used to decrypt messages (f.e. to decrypt messages from expired keys)
  - a user must have a home server
  - 