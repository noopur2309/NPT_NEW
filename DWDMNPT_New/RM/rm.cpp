#include<bits/stdc++.h>
#include "json.hpp"
#include "functions.h"  // header to add all output json item creation in format as desired
using json = nlohmann::json;
#include "WLMclass.h"
#include "MSMHclass.h"
#include "NODEclass.h"
using namespace std;

map<int,json>global_demand;
json process_unprotected(json p,bool flag,json array);
json process_oduk_sncp(json p);
json process_client_1_plus_1(json p);

json type_0(json demand);
json type_1(json demand);
json type_2(json demand);
json process(json client);
void input_topology(json topology);



map<string,Node*>node_info;

void release(json ans_for_demand){
  if(ans_for_demand.find("node_id")!=ans_for_demand.end()){
    string node_name=ans_for_demand["node_id"];
    if(ans_for_demand.find("msmh mapping")!=ans_for_demand.end()){
      // release msmh port of this node and used slot
        Node *temp=node_info[node_name];
        json k=ans_for_demand["msmh mapping"];
        temp->reallocate_msmh(k["msmh_slot"],k["msmh_port"]);
    }
    // release wlm tributaries


  }
}
json process_unprotected(json p,bool flag,json array){
     json j=p["path"];
     json::iterator it;
     json wlm,msmh,item,ans_for_demand;
     Node *pre;
     bool flag2=false;
     map<string,int>index;
     int i=0;
     string previous_node;
     int direction,wavelength;
     for(it=j.begin();it!=j.end();it++,i++){
        json node=*(it);
        if(it==j.begin()&&flag==true){
          Node *temp=node_info[node["node_id"]];
          pre=temp;
          item=make_item_begin(node,array["msmh mapping"]);
          ans_for_demand.push_back(item);
          index[item["node_id"]]=i;
          item.clear();
          direction=node["direction"];
          wavelength=node["wavelength"];
          previous_node=node["node_id"];
        }

        if(it==j.begin()&&flag==false){

          Node *temp=node_info[node["node_id"]];
          pre=temp;
          msmh=temp->msmh_mapping();
          item=make_item_begin(node,msmh);
          ans_for_demand.push_back(item);
          index[item["node_id"]]=i;
          item.clear();
          previous_node=node["node_id"];
          direction=node["direction"];
          wavelength=node["wavelength"];
        }

        else if(node["switch"]!=0|| it==j.end()-1){
            Node *temp=node_info[node["node_id"]];

            if(wlm.empty()){

                Node *previous_node_pointer=node_info[previous_node];
				        ans_for_demand[index[previous_node]]=add_node_port(ans_for_demand[index[previous_node]],previous_node_pointer->get_port_info(node["node_id"]));

                wlm=temp->wlm_mapping(pre,direction,wavelength,p["required_tributaries"],previous_node_pointer);
                if(wlm.empty()){
                  flag2=true;
                  break;
                }


                ans_for_demand[index[wlm["node previous"]]]=make_item_out(ans_for_demand.at(index[wlm["node previous"]]),wlm);
                ans_for_demand.push_back(make_item_in(wlm,node));
                index[node["node_id"]]=i;
                pre=temp;
                previous_node=node["node_id"];
                direction=node["direction"];
                wavelength=node["wavelength"];
            }

           else{
          			Node *previous_node_pointer=node_info[previous_node];
          			ans_for_demand[index[previous_node]]=add_node_port(ans_for_demand[index[previous_node]],previous_node_pointer->get_port_info(node["node_id"]));


                wlm=temp->wlm_mapping(pre,direction,wavelength,p["required_tributaries"],previous_node_pointer);
                if(wlm.empty()){
                  flag2=true;
                  break;
                }
                ans_for_demand[index[wlm["node previous"]]]=make_item_out(ans_for_demand.at(index[wlm["node previous"]]),wlm);
                ans_for_demand.push_back(make_item_in(wlm,node));
                index[node["node_id"]]=i;
                pre=temp;
                previous_node=node["node_id"];

                direction=node["direction"];
                wavelength=node["wavelength"];
            }
            if(it==j.end()-1){
                    Node *last=node_info[node["node_id"]];
                    msmh=last->msmh_mapping();
                    item=ans_for_demand.at(index[node["node_id"]]);
                    item["node_id"]=node["node_id"];
                    item["msmh mapping"]=msmh;
                    ans_for_demand[index[node["node_id"]]]=item;
                    item.clear();
            }

        }

        else{
            if(it!=j.begin()){
            item["node_id"]=node["node_id"];
            item["statement"]="pass through node";

            Node *previous_node_pointer=node_info[previous_node];
            ans_for_demand[index[previous_node]]=add_node_port(ans_for_demand[index[previous_node]],previous_node_pointer->get_port_info(node["node_id"]));
            ans_for_demand.push_back(item);
            item.clear();
			      index[node["node_id"]]=i;
            previous_node=node["node_id"];

            }

        }


    }

    if(flag2==false)
      return ans_for_demand;
    else{
      // reallocate all resources acquired by this demand
      // reallocate(ans_for_demand);
      release(ans_for_demand);
      json cannot_satisfy;
      cannot_satisfy["success code"]="-1";
      return cannot_satisfy;
    }
}
json process_oduk_sncp(json p){
  json demand=make_demand(p,p["path1"]);
  json ans;
  json array;
  ans["result1"]=process_unprotected(demand,false,array);
  if(ans["result1"].is_array()){
    array["msmh mapping"]=make_array(ans["result1"]);
    json demand2=make_demand(p,p["path2"]);
    ans["result2"]=process_unprotected(demand2,true,array);
    if(ans["result2"].is_array()){
      return ans;
    }
  }
  json cannot_satisfy;
  cannot_satisfy["success code"]=-1;
  return cannot_satisfy;
}
json process_client_1_plus_1(json p){
  json array,ans;
  json demand=make_demand(p,p["path1"]);
  ans["result1"]=process_unprotected(demand,false,array);
  if(ans["result1"].is_array()){
    json demand2=make_demand(p,p["path2"]);
    ans["result2"]=process_unprotected(demand2,false,array);
    if(ans["result2"].is_array()){
        ans["result1"]=client_add_msmh_both_path(ans["result1"],ans["result2"]);
        ans["result2"]=client_add_msmh_both_path(ans["result2"],ans["result1"]);
        return ans;
      }
  }
  json cannot_satisfy;
  cannot_satisfy["success code"]=-1;
  return cannot_satisfy;
}

json type_0(json demand){
    json array;
    if(global_demand.count(demand["Global_id"])){
                    json mapglobal=global_demand[demand["Global_id"]];
                    array=mapglobal["Details"];
                    json ans=process_unprotected(demand,true,array[0]);
                    if(ans.is_array()){
                      json result=make_result(ans,demand["id"],demand["Global_id"]);
                      global_demand[demand["id"]]=result;
                      return result;
                    }
                    else{
                      ans["Demand_id"]=demand["id"];
                      ans["Global_id"]=demand["Global_id"];
                      return ans;
                    }


          }
    else{
        json ans=process_unprotected(demand,false,array);
        if(ans.is_array()){
        json result=make_result(ans,demand["id"],demand["Global_id"]);
        global_demand[demand["id"]]=result;
        return result;
      }
      else{
        ans["Demand_id"]=demand["id"];
        ans["Global_id"]=demand["Global_id"];
        return ans;
      }
    }
}
json type_1(json demand){
    json ans=process_oduk_sncp(demand);
    if (ans.find("result1") != ans.end()) {
      json result=make_result(ans,demand["id"],demand["Global_id"]);
      global_demand[demand["id"]]=result;
      return result;
    }
  else{
    ans["Demand_id"]=demand["id"];
    ans["Global_id"]=demand["Global_id"];
    return ans;
  }
}
json type_2(json demand){
      json ans=process_client_1_plus_1(demand);
      if (ans.find("result1") != ans.end()) {
        json result=make_result(ans,demand["id"],demand["Global_id"]);
        global_demand[demand["id"]]=result;
        return result;
    }
    else{
      ans["Demand_id"]=demand["id"];
      ans["Global_id"]=demand["Global_id"];
      return ans;
    }
}
json process(json client){
    json::iterator it;
    json fans;
    for(it=client.begin();it!=client.end();it++){
        json demand=*(it);

       if(demand["Traffic_type"]==0){
            fans.push_back(type_0(demand)); // unprotected demand
        }

       else{
         if(demand["Protection_type"]==1){
                    fans.push_back(type_1(demand)); // protection oduk sncp

            }

         else if(demand["Protection_type"]==2){
                    fans.push_back(type_2(demand)); // protection client 1+1

            }

        }
    }
    return fans;
}
void input_topology(json topology){
    json::iterator it;
    for(it=topology.begin();it!=topology.end();it++){

        json node=*(it);
        if(node["Node Type"]==0){
          node_info[node["Node_id"]]=new Node(node["Node_id"]);
          Node *temp=node_info[node["Node_id"]];
          temp->node_port(node["node_port"]);
        }
        else{
          node_info[node["Node_id"]]=new Node(node["Node_id"]);
          Node *temp=node_info[node["Node_id"]];
          temp->wlmslots(node["wlm_slots"]);
          temp->msmhslots(node["msmh_slots"]);
          temp->tributaries(node["wlm_info"]);
          temp->msmh_ports(node["msmh_info"]);
          temp->node_port(node["node_port"]);
      }
    }
}

int main(){
    json s;
    cin>>s;
    json topology=s["topology"];
    json client=s["demand"];
    input_topology(topology);
    json x=process(client);
	  cout<<x<<endl;
    return 0;
}
