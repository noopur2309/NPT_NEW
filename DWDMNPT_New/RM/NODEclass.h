#include<bits/stdc++.h>
#include "json.hpp"

using json = nlohmann::json;
//#include "WLMclass.h"
//#include "MSMHclass.h"
using namespace std;

json create_tributaries(set<int>intersect,int required_tributaries);
class Node{
    private:
        set<int>MSMH_slots;
        set<int>WLM_slots;
        set<int>Unused_slots;
        string node_name;
        map<int,MSMH_card*>msmh_info;
        map<int,WLM_card*>wlm_info;
        map<pair<int,int>,pair<int,int>>direction_wavelength;
        map<string,int>port_info;
        map<string,pair<int,int>>present_wlm;
        map<pair<int,int>,pair<int,int>>slot_port;
    public:

        Node(string x){
                    node_name=x;
                    int i;
                    for(i=2;i<=5;i++){
                        Unused_slots.insert(i);
                    }
                    for(i=10;i<=13;i++){
                        Unused_slots.insert(i);
                    }

        };
        void add_direction_wavelength(int slot,int port,int direction,int wavelength){
              if(slot_port.count(make_pair(slot,port))>0){
                direction_wavelength[make_pair(direction,wavelength)]=make_pair(slot,port);
                slot_port.erase(make_pair(slot,port));
              }

        }
        pair<int,int>get_slot_port(int direction,int wavelength){

          pair<int,int>p;
          if(direction_wavelength.count(make_pair(direction,wavelength))>0)
            p=direction_wavelength[make_pair(direction,wavelength)];
          else{
            p.first=-1;
            p.second=-1;
          }
          return p;
        }
        pair<int,int>assign_slot_port(int direction,int wavelength){
          if(slot_port.size()>0){
            for (map<pair<int,int>,pair<int,int>>::iterator it=slot_port.begin(); it!=slot_port.end(); ++it){
                pair<int,int>p1=it->first;

                direction_wavelength[make_pair(direction,wavelength)]=make_pair(p1.first,p1.second);
                slot_port.erase(p1);
                return p1;
              }
          }
        pair<int,int>p1;
        p1.first=-1;
        p1.second=-1;
        return p1;
        }
        string getname(){
            return node_name;
        }
        void add_MSMH(int x){
            MSMH_slots.insert(x);
            Unused_slots.erase(x);
            msmh_info[x]=new MSMH_card(x);
        }
        void add_WLM(int x){
            WLM_slots.insert(x);
            Unused_slots.erase(x);
            wlm_info[x]=new WLM_card(x);
            slot_port[make_pair(x,1)]=make_pair(-1,-1);
            slot_port[make_pair(x,2)]=make_pair(-2,-2);
        }
        void wlmslots(json j){
            json::iterator it;
            for(it=j.begin();it!=j.end();it++){
                add_WLM(*(it));
            }
        }
        void msmhslots(json j){
            json::iterator it;
            for(it=j.begin();it!=j.end();it++){
                add_MSMH(*(it));
            }
        }
        void msmh_ports(json j){
            json::iterator it;
            for(it=j.begin();it!=j.end();it++){
                json info =*(it);
                MSMH_card *temp= msmh_info[info["slot_no"]];
                temp->ports(info["used_port"]);
            }
        }
        void reallocate_msmh(int slot,int port){
          MSMH_card *temp= this->msmh_info[slot];
          temp->release_port(port);
        }
        void tributaries(json j){
            json::iterator it;
            for(it=j.begin();it!=j.end();it++){
                json info =*(it);
                WLM_card *temp= wlm_info[info["slot_no"]];
                temp->assign(info["port"],info["used_tributaries"]);
                this->add_direction_wavelength(info["slot_no"],info["port"],info["Direction"],info["Wavelength"]);
            }
        }
        void node_port(json j){
            json::iterator it;
            for(it=j.begin();it!=j.end();it++){
                port_info[it.key()]=it.value();
            }
        }
        int get_port_info(string s){
            return port_info[s];
        }
        int getsize_unused_slots(){
          return Unused_slots.size();
        }
        int getfree_slot(){
          return *(Unused_slots.begin());
        }
        WLM_card* get_WLM(int slot){
           return this->wlm_info[slot];;
        }
        json msmh_mapping(){
            set<int>::iterator it;
            json j;
            for(it=MSMH_slots.begin();it!=MSMH_slots.end();it++){
                MSMH_card *msmh=msmh_info[*(it)];
                if(msmh->getsize()>=1){
                    j["msmh_slot"]=msmh->getslot();
                    int x=msmh->getfree();
                    msmh->add_MSMH_port(x);
                    j["msmh_port"]=x;
                    return j;
                }
            }
            // not a single msmh slot is available
            if(getsize_unused_slots()>=1){
              json j;
              int y=getfree_slot();
              add_MSMH(y);
              MSMH_card *msmh=msmh_info[y];
              if(msmh->getsize()>=1){
                  j["msmh_slot"]=msmh->getslot();
                  int x=msmh->getfree();
                  msmh->add_MSMH_port(x);
                  j["msmh_port"]=x;
                  return j;
              }
            }

            return j;
        }
        json create_ans(Node *pre,json tribu, int previous_slot,int current_slot,int previous_port,int current_port,int wavelength){
                json ans;
                ans["node previous"]=pre->getname();
                ans["slot previous"]=previous_slot;
                ans["port previous"]=previous_port;
                ans["node next"]=this->getname();
                ans["slot next"]=current_slot;
                ans["port next"]=current_port;
                ans["tributaries"]=tribu;
                ans["in wavelength"]=wavelength;
                ans["out wavelength"]=wavelength;

                return ans;
        }

        json wlm_mapping(Node *pre,int direction,int wavelength,int required_tributaries, Node *previous_node_pointer){
            json ans;
            int direction2=this->get_port_info( previous_node_pointer->getname());
            pair<int,int>p2=this->get_slot_port(direction2,wavelength);
            pair<int,int>p1=pre->get_slot_port(direction,wavelength);

            if(p1.first==-1 ||p2.first==-1){
              if(p1.first==-1){
                p1=pre->assign_slot_port(direction,wavelength);

                if(p1.first==-1){
                  //add new wlm slot in previous node;
                  int y=pre->getfree_slot();
                  pre->add_WLM(y);
                  pre->add_direction_wavelength(y,1,direction,wavelength);
                  p1=pre->get_slot_port(direction,wavelength);

                }
              }
              if(p2.first==-1){
                p2=this->assign_slot_port(direction2,wavelength);
                if(p2.first==-1){
                  // add new wlm slot;
                  int y=this->getfree_slot();
                  this->add_WLM(y);
                  this->add_direction_wavelength(y,1,direction2,wavelength);
                  p2=this->get_slot_port(direction2,wavelength);

                }
              }
            }

            WLM_card *wlm2=this->wlm_info[p2.first];
            WLM_card *wlm=pre->get_WLM(p1.first);
            //cout<<pre->getname()<<this->getname()<<wlm2->getslot()<<wlm->getslot()<<endl;
            set<int>a,b;
            a=wlm->port_tributary_set(p1.second);
            b=wlm2->port_tributary_set(p2.second);
            set<int>intersect;
            set_intersection(a.begin(),a.end(),b.begin(),b.end(),
                    std::inserter(intersect,intersect.begin()));
            if(intersect.size()>=required_tributaries){
              //pre->present_wlm[this->getname()]=make_pair(wlm2->getslot(),next_port);
              json tribu=create_tributaries(intersect,required_tributaries);
              wlm2->assign(p2.second,tribu);
              wlm->assign(p1.second,tribu);
              ans=this->create_ans(pre,tribu,p1.first,p2.first,p1.second,p2.second,wavelength);
              return ans;
            }
            return ans;
        }

};
json create_tributaries(set<int>intersect,int required_tributaries){
    set<int>::iterator it;
    json tribu;
    for(it=intersect.begin();it!=intersect.end() && required_tributaries>0;it++,required_tributaries--){
            tribu.push_back(*(it));
    }
    return tribu;
}
