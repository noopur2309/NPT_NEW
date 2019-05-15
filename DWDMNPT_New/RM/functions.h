// Set of all independent functions
#include <bits/stdc++.h>
#include<set>
#include "json.hpp"
using json = nlohmann::json;

json make_item_out(json item,json wlm){
    item["out tributaries"]=wlm["tributaries"];
    item["out wlm port"]=wlm["port previous"];
    item["out wlm slot"]=wlm["slot previous"];
    item["out wavelength"]=wlm["out wavelength"];
    return item;

}
json make_item_in(json wlm,json node){
    json item;
    item["node_id"]=node["node_id"];
    item["in tributaries"]=wlm["tributaries"];
    item["in wlm port"]=wlm["port next"];
    item["in wlm slot"]=wlm["slot next"];
    item["in wavelength"]=wlm["in wavelength"];
    return item;
}
json make_item_begin(json node,json array){
  json item;
  item["node_id"]=node["node_id"];
  item["msmh mapping"]=array;
  item["out wavelength"]=node["wavelength"];
  item["direction"]=node["direction"];
  return item;
}
json make_demand(json client,json path){
  client["path"]=path;
  return client;
}
json add_node_port(json item,int node_port){
    item["out node port"]=node_port;
    return item;
}
json add_second_msmh(json ans,json second){
  ans["msmh mapping 2"]=second["msmh mapping"];
  return ans;
}
json make_array(json ans){

  json::iterator it = ans.begin();
  json first_node =*(it);
  return first_node["msmh mapping"];

}
json client_add_msmh_both_path(json ans,json second){
    ans[0]=add_second_msmh(ans[0],second[0]);
    return ans;
}


json make_result(json ans,int demand_id,int global_id){
    json result;
    result["Details"]=ans;
    result["Global_id"]=global_id;
    result["Demand_id"]=demand_id;
    result["success code"]=1;
    return result;
}
