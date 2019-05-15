#include<bits/stdc++.h>
#include "json.hpp"
using json = nlohmann::json;
#define MSMH_ports 20
#include<set>
using namespace std;


class MSMH_card{
    private:
        set<int>used;
        set<int>unused;
        int slot_no;
    public:

        MSMH_card(int x){
            slot_no=x;

            for(int i=1;i<=MSMH_ports;i++)
                unused.insert(i);
        }
        void add_MSMH_port(int x){
            used.insert(x);
            unused.erase(x);
        }
        void ports(json j){
                json::iterator it;
                for(it=j.begin();it!=j.end();it++){
                    add_MSMH_port(*(it));
                }
        }
        int getfree(){
            set<int>::iterator it;
            it=unused.begin();
            return *(it);
        }
        int getsize(){
            return unused.size();
        }
        int getslot(){
            return slot_no;
        }
        void release_port(int port){
        used.erase(port);
        unused.insert(port);
      }
};
