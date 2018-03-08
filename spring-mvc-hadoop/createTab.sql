set hive.support.sql11.reserved.keywords=false;

CREATE TABLE IF NOT EXISTS traffic.monitor_flow_action(  
date string ,  
monitor_id string ,  
camera_id string ,  
car string ,  
action_time string ,
speed string  ,
road_id string,
area_id string
)  
ROW FORMAT DELIMITED FIELDS TERMINATED BY '\t' ; 

load data local inpath '/root/resource/monitor_flow_action.log' into table traffic.monitor_flow_action; 

CREATE TABLE IF NOT EXISTS traffic.monitor_camera_info(  
monitor_id string ,  
camera_id string 
)  
ROW FORMAT DELIMITED FIELDS TERMINATED BY '\t' ; 

load data local inpath '/root/resource/monitor_camera_info.log' into table traffic.monitor_camera_info; 
