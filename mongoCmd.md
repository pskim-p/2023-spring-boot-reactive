
## 1. install 
### 1.1 download mongosh based on your os types
> https://www.mongodb.com/docs/mongodb-shell/

## 2. excute mongosh 
### 2.1 run bin/mongosh.exe (window)

## 3. mongo commands 
### 3.1 commands 
```mysql-sql
-- query databases 
show databases
or 
show dbs  

test> show databases
admin    8.00 KiB
config  12.00 KiB
local    8.00 KiB
test     8.00 KiB
mydb     8.00 KiB

-- use databases
test> use mydb

-- show tables
mydb> show tables 
item

-- query table
db.item.find()
mydb> db.item.find()
[
  {
    _id: ObjectId('65648a87e558a15f48841afb'),
    name: 'alarmClock',
    price: 19.99,
    _class: 'com.pskim.springboot.reactive.ecommerce.domain.model.Item'
  },
  {
    _id: ObjectId('65648a87e558a15f48841afc'),
    name: 'smart-TV-tray',
    price: 244.99,
    _class: 'com.pskim.springboot.reactive.ecommerce.domain.model.Item'
  }
]

-- drop table 
db.item.drop()
true
```
