#include<bits/stdc++.h>
#include "json.hpp"
using json = nlohmann::json;
#define WLM_EACH_PORT_TRIBUTARIES 80
#include<set>
using namespace std;


class WLM_card{

        private:
            int slot_no;
            set<int>port_1_tributaries_unused;
            set<int>port_2_tributaries_unused;
        public:
        WLM_card(int x){
            slot_no=x;
            for(int i=1;i<=WLM_EACH_PORT_TRIBUTARIES;i++){
                port_1_tributaries_unused.insert(i);
                port_2_tributaries_unused.insert(i);
            }
        };
        void assign(int port,json j){
                if(port==1){
                    json::iterator it;
                    for(it=j.begin();it!=j.end();it++){
                        port_1_tributaries_unused.erase(*(it));
                    }
                }
                else{
                    json::iterator it;
                    for(it=j.begin();it!=j.end();it++){
                        port_2_tributaries_unused.erase(*(it));
                    }
                }
        }
        int getfree(int port){
            if(port==1)
                return *(port_1_tributaries_unused.begin());
            if(port==2)
                return *(port_2_tributaries_unused.begin());


        }
        json gettributaries(int port,int required_tributaries){
            json j;
            if(port==1){
                for(int i=0;i<required_tributaries;i++){
                        int x=getfree(1);
                        j.push_back(x);
                        port_1_tributaries_unused.erase(x);
                }
            }
            if(port==2){
                for(int i=0;i<required_tributaries;i++){
                        int x=getfree(2);
                        j.push_back(x);
                        port_2_tributaries_unused.erase(x);
                }
            }
            return j;
        }
        int getsizeport1(){
            return port_1_tributaries_unused.size();
        }
        int getsizeport2(){
            return port_2_tributaries_unused.size();
        }
        set<int> port_tributary_set(int port){
            set<int >a;
            if(port==1)
                return port_1_tributaries_unused;
            if(port==2)
                return port_2_tributaries_unused;
            return a;
        }
        int getslot(){
            return slot_no;
        }
};
